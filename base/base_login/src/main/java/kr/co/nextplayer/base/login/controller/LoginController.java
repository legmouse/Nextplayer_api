package kr.co.nextplayer.base.login.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.login.dto.AuthTokenDto;
import kr.co.nextplayer.base.login.service.LoginService;
import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import kr.co.nextplayer.next.lib.common.operation.RedisOperation;
import kr.co.nextplayer.next.lib.common.property.JwtProperty;
import kr.co.nextplayer.next.lib.common.resolver.UserModel;
import kr.co.nextplayer.next.lib.common.util.filter.NextplayerAccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Api(tags = "auth")
@RequestMapping(path = "/back_login/base_login/api/v1/auth")
@RestController
@CrossOrigin
public class LoginController {

    @Resource
    private LoginService service;

    @Value("${nextplayer.shopDomainPath}")
    private String shopDomainPath;

    @Value("${nextplayer.client-id}")
    private String shopClientId;

    @Resource
    private JwtProperty jwtProperty;
    @Resource
    private RedisOperation redisOperation;

    @ApiOperation(value = "로그인")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "platform", value = "PC OR MOBILE", example = "PC", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(name = "keepLogin", value = "NO:0,YES:1", example = "0", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(name = "fcmToken", value = "", dataTypeClass = String.class, required = false)
    })
    @PutMapping("/login")
    public ResponseDto<AuthTokenDto> login(
        @RequestParam String userId, @RequestParam String passwd, String keepLogin,
        @RequestParam(required = false) String fcmToken, @ApiIgnore ServerHttpRequest request) throws Exception {

        AuthTokenDto auth = service.login(userId, passwd, keepLogin, fcmToken, request);
        return ResponseDto.<AuthTokenDto>builder()
            .data(auth)
            .build();

    }

    @ApiOperation(value = "FCM 체크")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true)
    })
    @PutMapping("/checkFCM")
    public ResponseDto<Integer> checkFCM(@ApiIgnore UserModel userModel, @RequestParam(required = false) String fcmToken) throws Exception {
        log.info(" fcmToken >>> : {}", fcmToken);
        int result = service.checkFcmToken(userModel, fcmToken);

        return ResponseDto.<Integer>builder()
            .data(result)
            .build();

    }

    @ApiOperation(value = "로그아웃")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true)
    })
    @PutMapping("/logout")
    public ResponseDto<Boolean> logout(@ApiIgnore UserModel userModel) throws Exception {
        service.logout(userModel);
        return ResponseDto.<Boolean>builder().build();
    }

    @ApiOperation(value = "refreshToken")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", value = "clientId", dataTypeClass = String.class, required = true)
    })
    @PutMapping(value = "/refreshToken")
    public ResponseDto<AuthTokenDto> refreshToken(@RequestParam String refreshToken, @ApiIgnore ServerWebExchange exchange) throws Exception {

        AuthTokenDto auth = service.refreshToken(refreshToken, exchange);
        return ResponseDto.<AuthTokenDto>builder()
            .data(auth)
            .build();
    }

    @ApiOperation(value = "loginShop")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", value = "clientId", dataTypeClass = String.class, required = true)
    })
    @PutMapping(value = "/loginShop")
    public ResponseDto<String> loginShop(@RequestHeader("reqOrigin") Optional<String> origin,
                                         @RequestHeader("clientId") Optional<String> clientId,
                                         @RequestParam String accessToken,
                                         @ApiIgnore ServerWebExchange exchange) throws Exception {

        String siteOrigin = origin.orElseThrow(() -> new IllegalArgumentException("origin is null"));
        String siteClientId = clientId.orElseThrow(() -> new IllegalArgumentException("clientId is null"));
        log.info("referer : {}", siteOrigin);

        if (!siteOrigin.equals(shopDomainPath)) {
            throw new CommonLogicException(ApiState.SYSTEM.getCode(), "error.900");
        }
        if (!siteClientId.equals(shopClientId)) {
            throw new CommonLogicException(ApiState.SYSTEM.getCode(), "error.900");
        }

        if (accessToken.contains("?")) accessToken = accessToken.split("\\?")[0];

        String memberCd = service.loginShop(accessToken, exchange);

        return ResponseDto.<String>builder()
            .data(memberCd)
            .build();
    }

    /**
     * 쇼핑몰 토큰 검사
     * @param exchange
     * @return
     */

    @ApiOperation(value = "토큰 검사")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true)
    })
    @GetMapping("/check-token")
    public ResponseDto<Boolean> checkToken(@ApiIgnore ServerWebExchange exchange) throws Exception {
        ResponseDto<Boolean> responseDto = ResponseDto.<Boolean>builder().build();
        Boolean checkToken = NextplayerAccessTokenUtil.checkToken(exchange, jwtProperty, redisOperation);
        log.info(" checkToken >>> : {}", checkToken);
        responseDto.setData(checkToken);
        return responseDto;
    }

}


