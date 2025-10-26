package kr.co.nextplayer.base.webfilter;

import kr.co.nextplayer.next.lib.common.operation.RedisOperation;
import kr.co.nextplayer.next.lib.common.property.JwtProperty;
import kr.co.nextplayer.next.lib.common.util.filter.NextplayerAccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * kr.co.nextplayer.next.app.backstage.interceptor.AccessTokenInterceptor의 WebFlux 구현
 */
@Slf4j
@Component
public class Nextplayer_AccessTokenWebFilter extends PathPatternWebFilter {
    private final JwtProperty jwtProperty;
    private final RedisOperation redisOperation;

    public Nextplayer_AccessTokenWebFilter(JwtProperty jwtProperty, RedisOperation redisOperation) {
        this.jwtProperty = jwtProperty;
        this.redisOperation = redisOperation;

        this.addIncludePathPatterns("/*/base_front/api/v1/**");
        this.addIncludePathPatterns("/*/base_front/api/v1/member/**");
        this.addIncludePathPatterns("/*/base_front/api/v1/my/**");
        this.addIncludePathPatterns("/*/base_front/api/v1/scrap/**");
        this.addIncludePathPatterns("/*/base_media/api/v1/mediaFile/*/*");
        //this.addIncludePathPatterns("/*/base_front/api/v1/board/save_modifyResult");
        this.addExcludePathPatterns(
            "/",

            //"/*/base_front/api/v1/board/**",

            "/*/base_front/api/v1/test/testNoToken",
            //"/*/base_front/api/v1/**",
            "/*/base_front/api/v1/main/**",
            "/*/base_front/api/v1/cup**",
            "/*/base_front/api/v1/league**",
            "/*/base_front/api/v1/team**",
            "/*/base_front/api/v1/common/**",
            "/*/base_front/api/v1/media**",
            "/*/base_front/api/v1/updateViewCnt",
            "/*/base_front/api/v1/videoMediaList",
            "/*/base_front/api/v1/newsMediaList",
            "/*/base_front/api/v1/blogMediaList",
            "/*/base_front/api/v1/gameList",
            "/*/base_front/api/v1/reference**",

            "/*/base_front/api/v1/getIndirectMatches",
            "/*/base_front/api/v1/getInterestTeamInfo",

            "/*/base_front/api/v1/uage_service/**",

            "/back_front/base_front/api/v1/board/notice**",
            "/back_front/base_front/api/v1/board/bannerList",
            "/back_front/base_front/api/v1/board/subBannerList",
            "/back_front/base_front/api/v1/board/popupList",
            "/back_front/base_front/api/v1/education/save_education_auth",
            "/back_front/base_front/api/v1/main/pushClick",
            "/*/base_front/api/v1/player**",
            "/*/base_front/api/v1/roster**",
            "/*/base_front/api/v1/goldenAgeRosterList",
            "/*/base_front/api/v1/nationalRosterList",

            "/*/base_front/api/v1/searchGame",

            "/swagger-ui/**",
            "/swagger/**",
            "/css/**",
            "/fonts/**",
            "/images/**",
            "/js/**");
    }

    @Override
    public Mono<Void> filterMatched(ServerWebExchange exchange, WebFilterChain chain) {
        try {
            NextplayerAccessTokenUtil.checkToken(exchange, jwtProperty, redisOperation);
        } catch (Exception e) {
            return Mono.error(e);
        }

        return chain.filter(exchange);
    }


}
