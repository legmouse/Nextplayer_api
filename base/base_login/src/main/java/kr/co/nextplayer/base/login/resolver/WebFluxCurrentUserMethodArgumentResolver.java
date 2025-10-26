package kr.co.nextplayer.base.login.resolver;


import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nextplayer.next.lib.common.resolver.Account;
import kr.co.nextplayer.next.lib.common.resolver.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * kr.co.nextplayer.next.app.backstage.handler.CurrentUserMethodArgumentResolver 의 WebFlux 구현
 */
public class WebFluxCurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(UserModel.class);
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext, ServerWebExchange exchange) {
        UserModel userModel = UserModel.builder().build();

        Object accountByToken = exchange.getAttribute("accountByToken");

        if (accountByToken != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            Account account = objectMapper.convertValue(accountByToken, Account.class);

            BeanUtils.copyProperties(account, userModel);
        }

        return Mono.just(userModel);
    }
}
