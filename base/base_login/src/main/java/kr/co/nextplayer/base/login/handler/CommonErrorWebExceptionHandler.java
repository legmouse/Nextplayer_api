package kr.co.nextplayer.base.login.handler;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import kr.co.nextplayer.next.lib.common.validation.ValidationBingingResult;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * kr.co.nextplayer.next.app.backstage.handler.CommonExceptionHandler 의 WebFlux 버전
 */
@Slf4j
@Component
@Order(-2)
public class CommonErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {
    private final ValidationBingingResult validationBingingResult;

    public CommonErrorWebExceptionHandler(ErrorAttributes errorAttributes, ApplicationContext applicationContext,
                                          ServerCodecConfigurer serverCodecConfigurer,
                                          ValidationBingingResult validationBingingResult) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());

        this.validationBingingResult = validationBingingResult;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), (request) -> {
            return renderErrorResponse(request, errorAttributes);
        });
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request, ErrorAttributes errorAttributes) {
        Throwable error = errorAttributes.getError(request);

        ResponseDto<Object> responseDto = ResponseDto.builder().build();

        // Default Error
        responseDto.setError(ApiState.SYSTEM.getCode(), "System error");

        if (error instanceof CommonLogicException) {
            CommonLogicException ex = (CommonLogicException) error;

            responseDto.setError(ex.getCode(), ex.getExMessage(ex.getCode(), ex.getMessageKey(), ex.getArgs()));

            if (ArrayUtils.isNotEmpty(ex.getValidateMessage())) {
                responseDto.setData(ex.getValidateMessage());
            }
        } else if (error instanceof WebExchangeBindException) {
            BindingResult bindingResult = ((WebExchangeBindException) error).getBindingResult();

            try {
                validationBingingResult.checkParamsCommon(bindingResult);
            } catch (CommonLogicException ex) {
                responseDto.setError(ex.getCode(), ex.getExMessage(ex.getCode(), ex.getMessageKey(), ex.getArgs()));

                if (ArrayUtils.isNotEmpty(ex.getValidateMessage())) {
                    responseDto.setData(ex.getValidateMessage());
                }
            }
        }

        log.error("Unknown System Error: ", error);

        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(responseDto));
    }
}
