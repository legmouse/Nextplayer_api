package kr.co.nextplayer.base.login.service;

import kr.co.nextplayer.base.config.BaseProfiles;
import kr.co.nextplayer.base.login.dto.AuthTokenDto;
import kr.co.nextplayer.base.member.mapper.MemberMapper;
import kr.co.nextplayer.next.lib.common.constants.SystemSettingCode;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import kr.co.nextplayer.next.lib.common.operation.RedisOperation;
import kr.co.nextplayer.next.lib.common.property.JwtProperty;
import kr.co.nextplayer.next.lib.common.resolver.Account;
import kr.co.nextplayer.next.lib.common.util.WebFluxIPUtil;
import kr.co.nextplayer.next.lib.common.util.filter.NextplayerAccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class CommService {

    @Resource
    private JwtProperty jwtProperty;
    @Resource
    private RedisOperation redisOperation;
    @Resource
    private MemberMapper memberMapper;

    @Resource
    private BaseProfiles profiles;

    public AuthTokenDto makeAccessToken(String memberCd, String keepLogin, ServerHttpRequest request) throws CommonLogicException {

        //사용자 IP
        String ip = WebFluxIPUtil.getClientIp(request);

        //유효시간:분
        String expireMinutes = SystemSettingCode.ACCESS_TOKEN_TIME_PC.getCode();
        // 로컬이나 개발환경에서는 세션유지 오래 되게 조건 추가
        if (profiles.isLocal() || profiles.isDev()) {
            expireMinutes =  SystemSettingCode.ACCESS_TOKEN_TIME_PC_LONG.getCode();
        }
        log.info("expireMinutes : {}", expireMinutes);
        //사용자 platform
        String platform = request.getHeaders().getFirst("platform");
        if ("MOBILE".equals(platform)) {
            expireMinutes = SystemSettingCode.ACCESS_TOKEN_TIME_MO.getCode();
        }
        if ("1".equals(keepLogin)) {
            expireMinutes = SystemSettingCode.ACCESS_TOKEN_TIME_KEEP.getCode();
        }

        //token에 로그인 계정 정보 저장
        Account account = Account.builder()
            .memberCd(memberCd)
            .expireMinutes(expireMinutes)
            .ip(ip)
            .build();
        NextplayerAccessTokenUtil.makeToken(account, jwtProperty, request);

        //redis에 로그인 정보 저장
        Account accountMocked = NextplayerAccessTokenUtil.accountMocked(account ,redisOperation);
        //DB에 로그인 정보 저장
        memberMapper.updateToken(memberCd, accountMocked.getAccessToken(), accountMocked.getRefreshToken(), account.getAccessTokenOrigin(), account.getRefreshTokenOrigin(), Integer.parseInt(account.getExpireMinutes()) * 60, Integer.parseInt(account.getExpireMinutes()) * 3 * 60);

        AuthTokenDto auth = AuthTokenDto.builder()
            .accessToken(accountMocked.getAccessToken())
            .refreshToken(accountMocked.getRefreshToken())
            .build();

        return auth;
    }


}
