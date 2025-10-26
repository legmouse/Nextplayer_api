package kr.co.nextplayer.next.lib.common.exception;

import java.text.MessageFormat;
import java.util.Map;

import org.springframework.context.MessageSource;

import kr.co.nextplayer.next.lib.common.locale.WebFluxLocaleContextHolder;
import kr.co.nextplayer.next.lib.common.operation.RedisOperation;
import kr.co.nextplayer.next.lib.common.util.JsonUtil;
import kr.co.nextplayer.next.lib.common.util.LocalUtil;
import kr.co.nextplayer.next.lib.common.util.SpringUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class ExceptionMessage extends Exception {

    /**
     * 메세지중 {0...1}부분 파라미터로 전환
     */
    private String getArgsMessage(String msg, Object[] args){
        String resultMsg = msg;
        resultMsg = MessageFormat.format(msg, args);
        return resultMsg;
    }


    public String getExMessage(Integer code, String messageKey, Object[] args) {
        String resultMessage = "";
        String langCode = LocalUtil.getLang();
        if (code < 1000) {
            //system 관련 메세지
            MessageSource messageSource = SpringUtil.getBean(MessageSource.class);
            resultMessage = messageSource.getMessage(messageKey, args, WebFluxLocaleContextHolder.getLocale());
        } else {
            //업무 메세지,DB에 저장, redis 에서 획득
            RedisOperation redisOperation = SpringUtil.getBean(RedisOperation.class);
            Object msg = redisOperation.getEntireHashFromKey(messageKey);
            try {
                Map<String, Object> map = JsonUtil.objectToMap(msg);
                Object langMsg = map.get(langCode);
                if(langMsg == null){
                    resultMessage = "message code:("+messageKey+") is not mapping message content";
                    log.error(resultMessage);
                } else {
                    resultMessage = getArgsMessage(String.valueOf(langMsg), args);
                }

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return resultMessage;
    }
}
