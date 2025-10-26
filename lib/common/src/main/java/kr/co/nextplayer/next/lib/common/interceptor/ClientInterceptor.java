package kr.co.nextplayer.next.lib.common.interceptor;

import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ClientInterceptor implements HandlerInterceptor {

    private String clientId;
    public ClientInterceptor(String clientId){
        this.clientId = clientId;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        log.info("preHandle path : {}",requestURI);

        //check client id
        String requestClientId = request.getHeader("clientId");

        //=>>>>>>개발단계 프리패스, 알파/리얼 배포시 삭제
        requestClientId = clientId;
        //개발단계 프리패스, 알파/리얼 배포시 삭제<<<<<<=

        if(requestClientId != null && requestClientId.equals(clientId)){
            return true;
        }

        //client id is wrong
        log.warn("client id is wrong : {}", requestClientId);
        throw new CommonLogicException(ApiState.CLIENT.getCode(), ApiState.CLIENT.getMessage());
    }

}
