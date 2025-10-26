package kr.co.nextplayer.base.login.service;

import com.google.gson.JsonObject;
import kr.co.nextplayer.base.login.dto.AuthTokenDto;
import kr.co.nextplayer.base.login.oauth.request.AuthAppleRequest;
import kr.co.nextplayer.base.login.util.AppleUtil;
import kr.co.nextplayer.base.login.dto.Auth;
import kr.co.nextplayer.base.login.oauth.conf.AuthConfig;
import kr.co.nextplayer.base.login.oauth.conf.AuthDefaultSource;
import kr.co.nextplayer.base.login.oauth.model.AuthCallback;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class AppleService extends CommService{

    @Resource
    private MemberMapper memberMapper;
    @Resource
    private MemberInactiveMapper memberInactiveMapper;
    @Resource
    private MemberSnsMapper snsMapper;


    public Auth appleCallback(String code, String id_token, String state, String fcmToken, String error, String user, AuthConfig config, AuthCallback authCallbackConfig,
                              String keepLogin,
                              ServerHttpRequest serverRequest) throws CommonLogicException {
        log.warn("================appleCallback=============");
        log.info("fcmToken: {}", fcmToken);
        log.warn("code: {}, id_token: {} , state: {}", code, id_token, state);
        log.warn("error: " + error);
        log.warn("user: " + user);

        Auth auth = new Auth();
        if (StringUtils.isNotEmpty(error)) {
            log.error(error);
            throw new CommonLogicException(ApiState.AUTH_SNS.getCode(), ApiState.AUTH_SNS.getMessage());
        } else {
            try {
                AuthAppleRequest request = new AuthAppleRequest(config, AuthDefaultSource.APPLE);
                Map<String, Object> tokenMap = request.getFirstRequestToken(authCallbackConfig);
                String identityToken = tokenMap.get("id_token").toString();
                JsonObject json = AppleUtil.parserIdentityToken(identityToken);

                //parseSnsInfo
                auth = parseSnsInfo(json);

                // fcm token set
                if (!StringUtils.isEmpty(fcmToken)) {
                    auth.setFcm_token(fcmToken);
                }

                //애플 사용자인지 체크
                auth = checkMemExistInfo(auth, keepLogin, serverRequest);

            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
                throw new CommonLogicException(ApiState.AUTH_SNS.getCode(), ApiState.AUTH_SNS.getMessage());
            }
        }

        return auth;
    }
    //카카오가 사용중인지 체크
    protected Auth checkMemExistInfo(Auth auth, String keepLogin, ServerHttpRequest serverRequest) throws CommonLogicException {

        //sns 연동 정보
        MemberSns memberSns = snsMapper.checkSnsExist("APPLE", auth.getMemberSns().getSnsId());

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

    //sns 정보를 시스템 정보로 전환
    protected Auth parseSnsInfo(JsonObject json) throws Exception {

        log.info("tokenMap: " + json);

        MemberSnsRegDto memberSns = MemberSnsRegDto.builder()
            .snsType("APPLE")
            .snsId(getValue(json.get("sub")))
            .email(getValue(json.get("email")))
            .build();

        return Auth.builder()
            .memberSns(memberSns)
            .expires_in(getValue(json.get("exp"))) //(초)
            .build();
    }

    private String getValue(Object obj) {
        if (obj != null) {
            return obj.toString().replaceAll("\"", "");
        } else {
            return null;
        }
    }

    private String getEmail(String email) {
        if (email != null) {
            if(email.contains("privaterelay.appleid.com")){
                return null;
            }
            return email;
        } else {
            return null;
        }
    }
}
