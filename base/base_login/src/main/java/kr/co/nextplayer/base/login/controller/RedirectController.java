package kr.co.nextplayer.base.login.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Api(tags = "redirect")
@RequestMapping(path = "/back_login/base_login/api/v1/redirect")
@Controller
public class RedirectController {

    @RequestMapping(value="/callbackApple", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Mono<Void> callbackApple(@ModelAttribute dto code, ServerHttpResponse response) {
        log.info("code : {}", code.getCode());
        String fullRedirectUrl = UriComponentsBuilder
            .fromUriString("https://mobile.nextplayer.co.kr/auth/callbackApple")
            .queryParam("code", code.getCode())
            .build()
            .toUriString();

        response.setStatusCode(MOVED_PERMANENTLY);
        response.getHeaders().setLocation(URI.create(fullRedirectUrl));
        return response.setComplete();
    }

    class dto {
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

}
