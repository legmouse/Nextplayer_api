package kr.co.nextplayer.base.login.service;

import kr.co.nextplayer.base.member.mapper.MemberMapper;
import kr.co.nextplayer.base.member.model.SmsCertSend;
import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import kr.co.nextplayer.next.lib.common.operation.RedisOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final int saveTimeSeconds = 3;

    @Resource
    private RedisOperation redisOperation;
    @Resource
    private MemberMapper memberMapper;

    @Value("${auth.solapi.cas.api_key}")
    private String apiKey;
    @Value("${auth.solapi.cas.api_secret}")
    private String apiSecret;
    @Value("${auth.solapi.cas.api_url}")
    private String apiUrl;
    @Value("${auth.solapi.cas.api_admin_tel}")
    private String apiAdminTel;

    /**
     * create six digits random code
     **/
    public String verificationCode() {
        Random random = new Random();
        String verificationCode = String.valueOf(random.nextInt(9) + 1);
        for (int i = 0; i < 5; i++) {
            verificationCode += random.nextInt(10);
        }
        return verificationCode;
    }

    /**
     * 인증코드 저장 -> redis
     **/
    public Boolean saveVerificationCode(String key, String value, int saveTimeSeconds) {
        if (key != null && value != null && saveTimeSeconds > 0) {
            return redisOperation.set(key, value, saveTimeSeconds);
        } else {
            return false;
        }
    }

    /**
     * 본인확인을 위한 인증 SMS
     **/
    public Boolean certification_check_code(String phoneNoOrEmail, String verificationCode) throws CommonLogicException {

        Boolean checkResult = false;

        if (phoneNoOrEmail != null && verificationCode != null) {
            String key = "verificationCode[" + phoneNoOrEmail + "]";
            if (redisOperation.hasKey(key)) {
                String redisCode = redisOperation.get(key).toString();
                if (verificationCode.equals(redisCode)) {
                    checkResult = true;
                }
            }
        }

        return checkResult;
    }

    /**
     * 본인확인을 위한 인증 SMS 발송
     **/
    public Boolean sms_certification(String CertGbn, String phoneNo) throws Exception {

        String verificationCode = verificationCode();

        Boolean sendResult = lmsSend(CertGbn, phoneNo, verificationCode);

        if (sendResult) {
            String key = "verificationCode[" + phoneNo + "]";
            String value = verificationCode;
            saveVerificationCode(key, value, saveTimeSeconds * 60);
        } else {
            throw new CommonLogicException(ApiState.LOGIC.getCode(), "msg91_system_messageSend");
        }

        return sendResult;
    }


    public boolean lmsSend(String certGbn, String fromPhoneNo, String certNumber) throws Exception {
        boolean result = false;
        try {
            // Talk DB LOG
            SmsCertSend smsCertSend = SmsCertSend.builder()
                .certGbn(certGbn)
                .fromPhoneNo(fromPhoneNo)
                .certNumber(certNumber)
                .build();

            DefaultMessageService messageService =  NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.solapi.com");

            String content = "넥스트플레이어 본인확인 인증번호["+certNumber+"] 입니다.";

            Message message = new Message();
            message.setFrom(apiAdminTel);
            message.setTo(fromPhoneNo);
            message.setText(content);

            try {
                // send 메소드로 ArrayList<Message> 객체를 넣어도 동작합니다!
                messageService.send(message);
                memberMapper.insertMemberCertNumber(smsCertSend);
            } catch (NurigoMessageNotReceivedException exception) {
                // 발송에 실패한 메시지 목록을 확인할 수 있습니다!
                //System.out.println(exception.getFailedMessageList());
                //System.out.println(exception.getMessage());
            } catch (Exception exception) {
                log.error(exception.getMessage());
            }

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
