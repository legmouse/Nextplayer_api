package kr.co.nextplayer.base.resolver;

import kr.co.nextplayer.base.authentication.AuthenticationTokenManager;
import kr.co.nextplayer.base.model.AuthenticationMember;
import kr.co.nextplayer.base.service.BaseMemberService;
import kr.co.nextplayer.next.lib.common.resolver.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * AuthenticationMemberMethodArgumentResolver
 */
@RequiredArgsConstructor
@Slf4j
public class AuthenticationMemberMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final BaseMemberService memberService;
    private final AuthenticationTokenManager tokenManager;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(AuthenticationMember.class);
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext, ServerWebExchange exchange) {
        Account account = tokenManager.getAccount(exchange);
        log.info("account : {}", account);

        Assert.notNull(account, "토큰에 회원 정보가 존재하지 않습니다.");
        AuthenticationMember member = memberService.getMemberByMemberCd(account.getMemberCd());
        log.info("member : {}", member);
        Assert.notNull(member, "회원이 존재하지 않습니다.");
        return Mono.just(member);

    }
}
