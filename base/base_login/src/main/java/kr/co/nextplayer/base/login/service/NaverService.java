package kr.co.nextplayer.base.login.service;

import kr.co.nextplayer.base.login.dto.AuthTokenDto;
import kr.co.nextplayer.base.login.oauth.model.AuthCallback;
import kr.co.nextplayer.base.login.oauth.request.AuthNaverRequest;
import kr.co.nextplayer.base.login.dto.Auth;
import kr.co.nextplayer.base.login.oauth.conf.AuthConfig;
import kr.co.nextplayer.base.login.oauth.conf.AuthDefaultSource;
import kr.co.nextplayer.base.member.dto.MemberSnsRegDto;
import kr.co.nextplayer.base.member.mapper.MemberInactiveMapper;
import kr.co.nextplayer.base.member.mapper.MemberMapper;
import kr.co.nextplayer.base.member.mapper.MemberSnsMapper;
import kr.co.nextplayer.base.member.model.Member;
import kr.co.nextplayer.base.member.model.MemberInactive;
import kr.co.nextplayer.base.member.model.MemberSns;
import kr.co.nextplayer.next.lib.common.constants.AccountApiState;
import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.constants.SystemSettingCode;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import kr.co.nextplayer.next.lib.common.util.JsonUtil;
import kr.co.nextplayer.next.lib.common.util.LocalDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class NaverService extends CommService {

    //https://developers.naver.com/apps/#/list 네이버 개발자 사이트

    @Resource
    private MemberMapper memberMapper;
    @Resource
    private MemberInactiveMapper memberInactiveMapper;
    @Resource
    private MemberSnsMapper snsMapper;
    @Resource
    private LoginService loginService;


    public Auth naverCallback(String code, String state, String fcmToken, String error, String error_description, AuthConfig config,
                              String keepLogin,
                              ServerHttpRequest serverRequest) throws CommonLogicException {
        log.info("================naverCallback=============");
        log.info("fcmToken: {}", fcmToken);
        log.info("code: {} , state: {}", code, state);
        log.info("error: {}, error_description: {}", error, error_description);
        Auth auth = null;
        if (StringUtils.isNotEmpty(error)) {
            log.error(error);
            log.error(error_description);
            throw new CommonLogicException(ApiState.AUTH_SNS.getCode(), ApiState.AUTH_SNS.getMessage());
        } else {
            try {
                AuthNaverRequest request = new AuthNaverRequest(config, AuthDefaultSource.NAVER);
                Map<String, Object> tokenMap = request.getFirstRequestToken(AuthCallback.builder().code(code).state(state).build());

                //parseSnsInfo
                auth = parseSnsInfo(tokenMap);

                // fcm token set
                if (!StringUtils.isEmpty(fcmToken)) {
                    auth.setFcm_token(fcmToken);
                }

                //네이버가 사용중인지 체크
                auth = checkMemExistInfo(auth, keepLogin, serverRequest);

            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
                throw new CommonLogicException(ApiState.AUTH_SNS.getCode(), ApiState.AUTH_SNS.getMessage());
            }
        }
        return auth;
    }


    //네이버가 사용중인지 체크
    protected Auth checkMemExistInfo(Auth auth, String keepLogin, ServerHttpRequest serverRequest) throws CommonLogicException {

        //sns 연동 정보
        MemberSns memberSns = snsMapper.checkSnsExist("NAVER", auth.getMemberSns().getSnsId());

        if (memberSns == null) {
            //기존 회원 아님:0
            auth.setMemberExisted("0");
            return auth;
        } else {
            //sns 연동 정보 있음, 기존 회원 memberCd
            String memberCd = memberSns.getMemberCd();

            auth.getMemberSns().setMemberCd(memberCd);
            Member member = memberMapper.selectByMemberCd(memberCd);
            if (member != null) {
                //기존 회원 맞음:1
                auth.setMemberExisted("1");
                //로그인 & 토큰 생성
                AuthTokenDto authDto = makeAccessToken(member.getMemberCd(), keepLogin, serverRequest);
                auth.setAccessToken(authDto.getAccessToken());
                auth.setRefreshToken(authDto.getRefreshToken());
                auth.setInterestAge(member.getInterestAge());

                // FCM 토큰 갱신
                if (!StringUtils.isEmpty(auth.getFcm_token())) {
                    if (member.getFcmToken() == null || !member.getFcmToken().equals(auth.getFcm_token())) {
                        member.setFcmToken(auth.getFcm_token());
                        memberMapper.updateFcmToken(member);
                    }
                }

            } else {
                MemberInactive memberInactive = memberInactiveMapper.selectByPrimaryKey(memberCd);
                //휴면정보에도 없을시
                if (memberInactive == null) {
                    //기존 회원 아님:0
                    auth.setMemberExisted("0");
                } else {
                    //기존 회원 맞음:1
                    auth.setMemberExisted("1");
                    //휴면구분(휴면:0,탈퇴:1,삭제:2)
                    String inactiveType = memberInactive.getInactiveType();
                    //휴면
                    if (SystemSettingCode.INACTIVE_TYPE_SLEEP.getCode().equals(inactiveType)) {
                        throw new CommonLogicException(AccountApiState.USER_INACTIVE.getCode(), AccountApiState.USER_INACTIVE.getMsgCode());
                    }
                    //탈퇴 , 삭제
                    if (SystemSettingCode.INACTIVE_TYPE_WITHDRAWAL.getCode().equals(inactiveType)
                        || SystemSettingCode.INACTIVE_TYPE_DELETE.getCode().equals(inactiveType)) {
                        throw new CommonLogicException(ApiState.LOGIC.getCode(), "msg63_auth_withdrawalUser");
                    }
                }
            }
        }

        return auth;
    }

    //네이버 연동/회원 가입
    public Boolean naverCallback_mapping_join(MemberSnsRegDto memberSnsDto,
                                              String userId, String passwd,
                                              ServerHttpRequest serverRequest) throws CommonLogicException {

        log.info("================naverCallback_mapping_join=============");
        //회원체크
        Member member = loginService.mapping_sns_checkUser(userId, passwd);
        //해당 회원이 이미연동되어 있는지 체크
        MemberSns memberSns = snsMapper.checkSnsExistByMemberCd("NAVER", member.getMemberCd());
        if (memberSns != null) {
            throw new CommonLogicException(ApiState.LOGIC.getCode(), "msg93_member_snsIsConected");
        }

        Auth auth = Auth.builder().memberSns(memberSnsDto).build();
        Boolean mappingResult = mapping_sns(auth, member.getMemberCd(), serverRequest);

        return mappingResult;

    }

    //네이버 연동
    public Boolean naverCallback_mapping(String code, String state, String error, String error_description, AuthConfig config,
                                         String memberCd,
                                         ServerHttpRequest serverRequest) throws CommonLogicException {
        log.info("================naverCallback_mapping=============");
        log.info("code: {} , state: , {}error: {}, error_description: {}", code, state, error, error_description);
        if (StringUtils.isNotEmpty(error)) {
            log.error(error);
            log.error(error_description);
            throw new CommonLogicException(ApiState.AUTH_SNS.getCode(), ApiState.AUTH_SNS.getMessage());
        } else {
            try {
                AuthNaverRequest request = new AuthNaverRequest(config, AuthDefaultSource.NAVER);
                Map<String, Object> tokenMap = request.getFirstRequestToken(AuthCallback.builder().code(code).state(state).build());

                //parseSnsInfo
                Auth auth = parseSnsInfo(tokenMap);

                //회원연동
                return mapping_sns(auth, memberCd, serverRequest);

            } catch (CommonLogicException e) {
                log.error(e.getMessage());
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
                throw new CommonLogicException(ApiState.AUTH_SNS.getCode(), ApiState.AUTH_SNS.getMessage());
            }
        }
    }

    //회원연동
    protected Boolean mapping_sns(Auth auth, String memberCd, ServerHttpRequest serverRequest) throws CommonLogicException {

        //기존 연동된 회원이 있는지
        MemberSns old_memberSns = snsMapper.checkSnsExist("NAVER", auth.getMemberSns().getSnsId());
        if (old_memberSns != null) {
            throw new CommonLogicException(ApiState.LOGIC.getCode(), "msg90_member_snsIsUsed");
        }

        //연동
        MemberSnsRegDto memberSnsDto = auth.getMemberSns();
        MemberSns memberSns = MemberSns.builder().build();
        BeanUtils.copyProperties(memberSnsDto, memberSns);
        memberSns.setMemberCd(memberCd);
        memberSns.setRegDate(LocalDateUtil.getLocalDateByZone());
        int i = snsMapper.insertSelective(memberSns);
        if (i <= 0) {
            throw new CommonLogicException(ApiState.AUTH_SNS.getCode(), ApiState.AUTH_SNS.getMessage());
        }

        return true;
    }

    //sns 정보를 시스템 정보로 전환
    protected Auth parseSnsInfo(Map<String, Object> tokenMap) throws Exception {

        log.info("tokenMap: " + tokenMap);
        log.info("tokenMap: " + tokenMap.get("response"));

        Object res = tokenMap.get("response");
        Map<String, Object> resMap = JsonUtil.objectToMap(res);

        MemberSnsRegDto memberSns = MemberSnsRegDto.builder()
            .snsType("NAVER")
            .snsId(getValue(resMap.get("id")))
            .name(getValue(resMap.get("name")))
            .nickname(getValue(resMap.get("nickname")))
            .email(getValue(resMap.get("email")))
            .sex(getSex(resMap.get("gender")))
            .age(Integer.parseInt(getValue(resMap.get("birthyear"))))
            .ageGroup(StringUtils.isEmpty(getValue(resMap.get("age"))) ? null : getValue(resMap.get("age")).replace("-","~"))
            .birthDate(getValue(resMap.get("birthday")))
            .phone(getValue(resMap.get("mobile")).replaceAll("-",""))
            .photo(getValue(resMap.get("profile_image")))
            .build();

        //성별 - F: 여성
        String gender = getValue(resMap.get("gender"));
        if ("F".equals(gender)) {
            memberSns.setSex("W");
        }

        return Auth.builder()
            .memberSns(memberSns)
            .access_token(getValue(tokenMap.get("access_token")))
            .refresh_token(getValue(tokenMap.get("refresh_token")))
            .token_type(getValue(tokenMap.get("token_type")))
            .expires_in(getValue(tokenMap.get("expires_in"))) //(초)
            .build();
    }

    private String getValue(Object obj) {
        if (obj != null) {
            return obj.toString();
        } else {
            return null;
        }
    }

    private String getSex(Object obj) {
        if (obj != null) {
            return "M";
        } else {
            return null;
        }
    }
}
