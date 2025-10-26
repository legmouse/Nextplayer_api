package kr.co.nextplayer.base.login.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.login.dto.Auth;
import kr.co.nextplayer.base.login.oauth.conf.AuthConfig;
import kr.co.nextplayer.base.login.oauth.conf.AuthDefaultSource;
import kr.co.nextplayer.base.login.oauth.model.AuthCallback;
import kr.co.nextplayer.base.login.oauth.request.AuthAppleRequest;
import kr.co.nextplayer.base.login.oauth.request.AuthKakaoRequest;
import kr.co.nextplayer.base.login.oauth.request.AuthNaverRequest;
import kr.co.nextplayer.base.login.oauth.utils.StringUtils;
import kr.co.nextplayer.base.login.service.AppleService;
import kr.co.nextplayer.base.login.service.GoogleService;
import kr.co.nextplayer.base.login.service.KakaoService;
import kr.co.nextplayer.base.login.service.NaverService;
import kr.co.nextplayer.base.member.dto.MemberSnsRegDto;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import kr.co.nextplayer.next.lib.common.operation.RedisOperation;
import kr.co.nextplayer.next.lib.common.property.JwtProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Api(tags = "auth-sns nextplayerLogin")
@RequestMapping(path = "/back_login/base_login/api/v1/auth_sns")
@RestController
@CrossOrigin
public class SnsLoginController {


    @Resource
    private GoogleService googleService;
    @Resource
    private AppleService appleService;
    @Resource
    private NaverService naverService;

    @Resource
    private KakaoService kakaoService;


    @Resource
    private JwtProperty jwtProperty;
    @Resource
    private RedisOperation redisOperation;

    // ============ naverAuth ============>>>>>>>
    @Value("${auth.naver.cas.client_id}")
    private String clientId;
    @Value("${auth.naver.cas.client_secret}")
    private String clientSecret;


    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true)
    })
    @GetMapping(value = "/naverAuthPath")
    public ResponseDto<String> naverAuthPath(@RequestParam String loginCallbackUrl) {
        log.info("================naverAuth=============");
        AuthNaverRequest request = new AuthNaverRequest(AuthConfig.builder()
            .redirectUri(loginCallbackUrl)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .build(),
            AuthDefaultSource.NAVER);
        log.info("==naverAuthUrl==>" + request.authorize(null));
        return ResponseDto.<String>builder()
            .data(request.authorize(null))
            .build();
    }


    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "platform", value = "PC OR MOBILE", example = "PC", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(name = "keepLogin", value = "NO:0,YES:1", example = "0", dataTypeClass = String.class, required = true)
    })
    @GetMapping(value = "/naverCallback")
    public ResponseDto<Auth> naverCallback(String code, String state, String error, String error_description,
                                           @RequestParam String loginCallbackUrl,
                                           @RequestParam String keepLogin,
                                           @RequestParam(required = false) String fcmToken,
                                           @ApiIgnore ServerHttpRequest request) throws CommonLogicException {

        AuthConfig authConfig = AuthConfig.builder()
            .redirectUri(loginCallbackUrl)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .build();
        Auth auth = naverService.naverCallback(code, state, fcmToken, error, error_description, authConfig, keepLogin, request);

        return ResponseDto.<Auth>builder()
            .data(auth)
            .build();
    }

    /*
    @ApiOperation(value = "네이버 연동")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true)
    })
    @GetMapping(value = "/naverCallback_mapping")
    public ResponseDto<Boolean> naverCallback_mapping(String code, String state, String error, String error_description,
                                                      @RequestParam String loginCallbackUrl,
                                                      @RequestParam String memberCd,
                                                      @ApiIgnore ServerHttpRequest request) throws CommonLogicException {

        AuthConfig authConfig = AuthConfig.builder()
            .redirectUri(loginCallbackUrl)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .build();
        Boolean mappingResult = naverService.naverCallback_mapping(code, state, error, error_description, authConfig, memberCd, request);

        return ResponseDto.<Boolean>builder()
            .data(mappingResult)
            .build();
    }

    @ApiOperation(value = "네이버 연동/회원 가입")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true)
    })
    @PutMapping(value = "/naverCallback_mapping_join")
    public Mono<ResponseDto> naverCallback_mapping_join(
        @RequestBody Mono<MemberSnsRegDto> memberSnsMono, @RequestParam String userId, @RequestParam String passwd,
        @ApiIgnore ServerHttpRequest request) throws CommonLogicException {

        Mono<ResponseDto> responseDtoMono = memberSnsMono.flatMap(memberSns -> {
            try {
                naverService.naverCallback_mapping_join(memberSns, userId, passwd, request);
            } catch (CommonLogicException e) {
                e.printStackTrace();
                return Mono.error(e);
            }
            return Mono.just(ResponseDto.builder().build());
        });

        return responseDtoMono;
    }
    */
    // <<<<<<<============ naverAuth ============


    // ============ AppleAuth ============>>>>>>>

    @Value("${auth.apple.cas.redirect_uri}")
    private String apple_loginCallbackUrl;
    @Value("${auth.apple.cas.client_id}")
    private String apple_clientId;
    @Value("${auth.apple.cas.client_secret}")
    private String apple_clientSecret;
    @Value("${auth.apple.cas.team_id}")
    private String apple_teamId;
    @Value("${auth.apple.cas.key_id}")
    private String apple_keyId;

    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true)
    })
    @GetMapping(value = "/appleAuthPath")
    public ResponseDto<String> appleAuth(@RequestParam String loginCallbackUrl) {
        log.warn("================appleAuth=============");
        AuthAppleRequest request = new AuthAppleRequest(AuthConfig.builder()
            .redirectUri(loginCallbackUrl)
            .clientId(apple_clientId)
            .clientSecret(apple_clientSecret)
            .build(),
            AuthDefaultSource.APPLE);
        log.info("================appleAuthUrl=============>" + request.authorize(null));
        return ResponseDto.<String>builder()
            .data(request.authorize(null))
            .build();
    }

    @GetMapping(value = "/appleCallback")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "platform", value = "PC OR MOBILE", example = "PC", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(name = "keepLogin", value = "NO:0,YES:1", example = "0", dataTypeClass = String.class, required = true)
    })
    public ResponseDto<Auth> appleCallback(String code, String id_token, String state, String error, String user,
                                           @RequestParam String loginCallbackUrl,
                                           @RequestParam String keepLogin,
                                           @RequestParam(required = false) String fcmToken,
                                           @ApiIgnore ServerHttpRequest request) throws CommonLogicException {

        log.warn("================appleCallback=============");
        log.warn("code: " + code);
        log.warn("state: " + state);
        log.warn("fcmToken: ", fcmToken);
        log.warn("error: " + error);

        AuthConfig authConfig = AuthConfig.builder()
            .redirectUri(loginCallbackUrl)
            .clientId(apple_clientId)
            .clientSecret(apple_clientSecret)
            .build();
        AuthCallback authCallbackConfig = AuthCallback.builder()
            .code(code).state(state)
            .keyId(apple_keyId).teamId(apple_teamId).secretCode(apple_clientSecret).clientId(apple_clientId)
            .build();
        Auth auth = appleService.appleCallback(code, id_token, state, fcmToken,error, user, authConfig, authCallbackConfig, keepLogin, request);

        return ResponseDto.<Auth>builder()
            .data(auth)
            .build();
    }

    // <<<<<<<============ AppleAuth ============

    // ============ googleAuth ============>>>>>>>
/*

    @Value("${auth.google.cas.client_id}")
    private String google_clientId;
    @Value("${auth.google.cas.client_secret}")
    private String google_clientSecret;
    @Value("${auth.google.cas.application_name}")
    private String google_applicationName;
    @Value("${auth.google.cas.redirect_uri}")
    private String google_redirectUrl;

    @ApiOperation(value = "googleAuth")
    @GetMapping(value = "/googleAuth")
    public Mono<Rendering> googleAuth() {
        log.info("================googleAuth=============");
        String authorizingUrl = "";
        try {
            authorizingUrl = googleService.authorizingUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("===authorizingUrl=====>" + authorizingUrl);
        return Mono.just(Rendering.redirectTo(authorizingUrl).build());
    }

    @GetMapping(value = "/googleAuthorizing")
    public Auth authorizing(String code) {
        log.info("================googleAuthorizing=============");
        log.warn("code: " + code);
        Auth auth = null;
        try {
            auth = googleService.authorizing(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return auth;
    }
*/

    // <<<<<<<============ googleAuth ============


    // ============ kakaoAuth ============>>>>>>>

    @Value("${auth.kakao.cas.client_id}")
    private String kakao_clientId;

    @Value("${auth.kakao.cas.client_secret}")
    private String kakao_clientSecret;

    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true)
    })
    @GetMapping(value = "/kakaoAuthPath")
    public ResponseDto<String> kakaoAuthPath(@RequestParam String loginCallbackUrl) {
        log.warn("================kakaoAuthPath=============");
        AuthKakaoRequest request = new AuthKakaoRequest(AuthConfig.builder()
            .redirectUri(loginCallbackUrl)
            .clientId(kakao_clientId)
            .clientSecret(kakao_clientSecret)
            .build(),
            AuthDefaultSource.KAKAO);
        log.warn("================RedirectView=============>" + request.authorize(null));
        return ResponseDto.<String>builder()
            .data(request.authorize(null))
            .build();
    }

    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "platform", value = "PC OR MOBILE", example = "PC", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(name = "keepLogin", value = "NO:0,YES:1", example = "0", dataTypeClass = String.class, required = true)
    })
    @GetMapping(value = "/kakaoCallback")
    public ResponseDto<Auth> kakaoCallback(String code, String state, String error, String error_description,
                              @RequestParam String loginCallbackUrl,
                              @RequestParam String keepLogin,
                              @RequestParam(required = false) String fcmToken,
                              @ApiIgnore ServerHttpRequest request) throws CommonLogicException {
        log.warn("================kakaoCallback=============");
        log.warn("code: " + code);
        log.warn("state: " + state);
        log.warn("fcmToken: ", fcmToken);
        log.warn("error: " + error);
        log.warn("error_description: " + error_description);
        AuthConfig authConfig = AuthConfig.builder()
            .redirectUri(loginCallbackUrl)
            .clientId(kakao_clientId)
            .clientSecret(kakao_clientSecret)
            .build();
        Auth auth = kakaoService.kakaoCallback(code, state, fcmToken, error, error_description, authConfig, keepLogin, request);

        return ResponseDto.<Auth>builder()
            .data(auth)
            .build();
    }

    // <<<<<<<============ kakaoAuth ============


}
