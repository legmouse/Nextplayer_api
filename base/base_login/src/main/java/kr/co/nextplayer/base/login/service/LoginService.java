package kr.co.nextplayer.base.login.service;

import kr.co.nextplayer.base.login.dto.AuthTokenDto;
import kr.co.nextplayer.base.member.mapper.MemberInactiveMapper;
import kr.co.nextplayer.base.member.mapper.MemberMapper;
import kr.co.nextplayer.base.member.model.Member;
import kr.co.nextplayer.base.member.model.MemberInactive;
import kr.co.nextplayer.next.lib.common.constants.AccountApiState;
import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.constants.SystemSettingCode;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import kr.co.nextplayer.next.lib.common.operation.RedisOperation;
import kr.co.nextplayer.next.lib.common.property.JwtProperty;
import kr.co.nextplayer.next.lib.common.resolver.Account;
import kr.co.nextplayer.next.lib.common.resolver.UserModel;
import kr.co.nextplayer.next.lib.common.util.SecretUtil;
import kr.co.nextplayer.next.lib.common.util.filter.NextplayerAccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import javax.annotation.Resource;

@Service
@Slf4j
public class LoginService extends CommService {

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private MemberInactiveMapper memberInactiveMapper;

    @Resource
    private JwtProperty jwtProperty;
    @Resource
    private RedisOperation redisOperation;


    public Member mapping_sns_checkUser(String userId, String passwd) throws CommonLogicException {

        // checkAccount
        Member member = checkAccount(userId, passwd);

        //회원 상태 체크
        checkMemberState(member.getMemberState());


        return member;
    }

    public AuthTokenDto login(String userId, String passwd, String keepLogin, String fcmToken, ServerHttpRequest request) throws Exception {
        log.info(" fcmToken >>> : {}", fcmToken);
        // checkAccount
        Member member = checkAccount(userId, passwd);

        //회원 상태 체크
        checkMemberState(member.getMemberState());

        //토큰 생성
        AuthTokenDto auth = makeAccessToken(member.getMemberCd(), keepLogin, request);

        auth.setInterestAge(member.getInterestAge());

        if (!StringUtils.isEmpty(fcmToken)) {
            if(member.getFcmToken() == null || !member.getFcmToken().equals(fcmToken)) {
                member.setFcmToken(fcmToken);
                memberMapper.updateFcmToken(member);
            }
        }

        return auth;
    }

    public int checkFcmToken(UserModel userModel, String fcmToken) throws Exception {
        int result = 0;

        Member member = memberMapper.selectByMemberCd(userModel.getMemberCd());

        if (!StringUtils.isEmpty(fcmToken)) {
            if(member.getFcmToken() == null || !member.getFcmToken().equals(fcmToken)) {
                member.setFcmToken(fcmToken);
                result = memberMapper.updateFcmToken(member);
            }
        }

        return result;
    }

    //회원 상태 체크
    private Boolean checkMemberState(String memberState) throws CommonLogicException {
        if (SystemSettingCode.MEMBER_STATE_BLOCK.getCode().equals(memberState)) {
            throw new CommonLogicException(ApiState.ACCESS_BLOCKED.getCode(), ApiState.ACCESS_BLOCKED.getMessage());
        }
        return true;
    }

    private Member checkAccount(String userId, String passwd) throws CommonLogicException {
        // check id and password
        if (StringUtils.isEmpty(userId)) {
            throw new CommonLogicException(ApiState.PARAMETER.getCode(), "msg59_auth_idIsNull");
        }
        if (StringUtils.isEmpty(passwd)) {
            throw new CommonLogicException(ApiState.PARAMETER.getCode(), "msg60_auth_pwIsNull");
        }

        //입력한 비번 암호화
        String secretPw = SecretUtil.generateUserPassWord(passwd);

        //check active member db
        Member member = memberMapper.login(userId);

        //일치하는 아이디를 발견하지 못한 경우 : 휴면 회원 DB 에서 검색
        if (member == null) {
            //휴면 회원 에서 검색
            checkInactiveAccount(userId, secretPw);

            throw new CommonLogicException(ApiState.LOGIC.getCode(), "msg62_auth_userNotExist");
        }

        //비번 확인.
        if (!member.getMemberPwd().equals(secretPw)) {
            throw new CommonLogicException(ApiState.LOGIC.getCode(), "msg61_auth_idAndPwIsNotMatch");
        }

        //회원 확인 완료, 회원 정보 리턴
        return member;
    }

    private void checkInactiveAccount(String userId, String secretPw) throws CommonLogicException {
        //check Inactive member db
        MemberInactive memberInactive = memberInactiveMapper.selectByInactive(userId);

        //존재하지 않는 id, 가입 유도
        if (memberInactive == null) {
            throw new CommonLogicException(AccountApiState.USER_NOT_EXIST.getCode(), AccountApiState.USER_NOT_EXIST.getMsgCode());
        }

        if (memberInactive != null) {

            //비번 틀림.
            if (!memberInactive.getMemberPwd().equals(secretPw)) {
                throw new CommonLogicException(ApiState.LOGIC.getCode(), "msg61_auth_idAndPwIsNotMatch");
            }

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

    public boolean logout(UserModel userModel) throws CommonLogicException {

        try {
            if (redisOperation.hasKey(userModel.getMemberCd())) {
                redisOperation.del(userModel.getMemberCd());
            }
            if (redisOperation.hasKey(userModel.getAccessToken())) {
                redisOperation.del(userModel.getAccessToken());
            }
            if (redisOperation.hasKey(userModel.getRefreshToken())) {
                redisOperation.del(userModel.getRefreshToken());
            }
            memberMapper.logoutToken(userModel.getMemberCd());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CommonLogicException(ApiState.SYSTEM.getCode(), "error.900");
        }

        return true;
    }

    public AuthTokenDto refreshToken(String refreshToken, ServerWebExchange exchange) throws Exception {

        String refreshTokenOrigin = memberMapper.selectRefreshTokenOrigin(refreshToken);
        Account RefreshAccount = NextplayerAccessTokenUtil.checkRefreshToken(refreshToken, refreshTokenOrigin, redisOperation, jwtProperty, exchange);
        AuthTokenDto auth = AuthTokenDto.builder()
            .accessToken(RefreshAccount.getAccessToken())
            .refreshToken(RefreshAccount.getRefreshToken())
            .build();
        memberMapper.updateToken(RefreshAccount.getMemberCd(), RefreshAccount.getAccessToken(), RefreshAccount.getRefreshToken(), RefreshAccount.getAccessTokenOrigin(), RefreshAccount.getRefreshTokenOrigin(), Integer.parseInt(RefreshAccount.getExpireMinutes()) * 60, Integer.parseInt(RefreshAccount.getExpireMinutes()) * 3 * 60);

        Member memberInfo = memberMapper.selectByMemberCd(RefreshAccount.getMemberCd());
        auth.setInterestAge(memberInfo.getInterestAge());

        return auth;
    }

    public String loginShop(String accessToken, ServerWebExchange exchange) throws Exception {
        return NextplayerAccessTokenUtil.checkAccessTokenAndGetMemberCd(accessToken, exchange, jwtProperty, redisOperation);
    }

}
