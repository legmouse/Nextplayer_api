package kr.co.nextplayer.base.batch.firebase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.*;
import kr.co.nextplayer.base.batch.mapper.PushMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmSender {

    private final ObjectMapper objectMapper;

    private final PushMapper pushMapper;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Data{
        private String    param;
        private String    path;
        private String    description;
    }

    /**
     * Builds the android config that will customize how a message is received on Android.
     *
     * @return android config of an FCM request.
     */
    private AndroidConfig buildAndroidOverridePayload() {
        AndroidNotification androidNotification = AndroidNotification.builder()
            .build();

        AndroidConfig androidConfig = AndroidConfig.builder()
            .setNotification(androidNotification)
            .build();

        return androidConfig;
    }

    /**
     * Builds the apns config that will customize how a message is received on iOS.
     *
     * @return apns config of an FCM request.
     */
    private ApnsConfig buildApnsOverridePayload() {
        Aps aps = Aps.builder()
            .setBadge(1)
            .setSound("defalut")
            .build();

        ApnsConfig apnsConfig = ApnsConfig.builder()
            .putHeader("apns-priority", "10")
            .setAps(aps)
            .build();

        return apnsConfig;
    }

    private MulticastMessage makeMessage(List<String> targetToken, String title, String body, String param, String path, String description) {
        String data = null;
        try {
            data = objectMapper.writeValueAsString(Data.builder()
                .description(description)
                .param(param)
                .path(path)
                .build());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        MulticastMessage message = MulticastMessage.builder()
            .setNotification(Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build())
            .addAllTokens(targetToken)
            .putData("data", data)
            .setAndroidConfig(buildAndroidOverridePayload())
            .setApnsConfig(buildApnsOverridePayload())
            .build();

        return message;
    }


    public Map<String, Integer> sendFcm(List<Integer> pushIds, List<String> targetToken, String title, String body, String param, String path, String description) {
        MulticastMessage multicastMessage = makeMessage(targetToken, title, body, param, path, description);

        BatchResponse response = null;
        try {
            response = FirebaseMessaging.getInstance()
                .sendMulticast(multicastMessage);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }

        List<SendResponse> responses = response.getResponses();

        for (int i = 0; i < responses.size(); i++) {
            if (responses.get(i).isSuccessful()) {
                //성공 기록은 성능상 제거
//                pushMapper.updateMemberPushSending(pushIds.get(i));
            } else {
                //에러 기록
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put("error", responses.get(i).getException().getMessage());
                errorMap.put("pushId", String.valueOf(pushIds.get(i)));
                pushMapper.updateMemberPushSendError(errorMap);
            }
        }

        HashMap<String, Integer> resultMap = new HashMap<>();
        resultMap.put("successCount", response.getSuccessCount());
        resultMap.put("failureCount", response.getFailureCount());

        return resultMap;
    }

}
