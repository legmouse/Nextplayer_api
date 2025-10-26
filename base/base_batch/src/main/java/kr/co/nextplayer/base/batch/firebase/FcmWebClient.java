package kr.co.nextplayer.base.batch.firebase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.firebase.messaging.*;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import kr.co.nextplayer.base.batch.mapper.PushMapper;
import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * WebClient
 */
@Slf4j
@EnableAsync
public class FcmWebClient {
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/nextplayer-6e195/messages:send";
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final PushMapper pushMapper;

    public FcmWebClient(ObjectMapper objectMapper, PushMapper pushMapper) {
        this.objectMapper = objectMapper;
        this.pushMapper = pushMapper;
        String accecToken = "";
        try {
            accecToken = getAccessToken();
        } catch (Exception e) {
        }

        HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50000)
            .responseTimeout(Duration.ofMinutes(1))
            .doOnConnected(conn ->
                conn.addHandlerLast(new ReadTimeoutHandler(1, TimeUnit.MINUTES))
                    .addHandlerLast(new WriteTimeoutHandler(1, TimeUnit.MINUTES)));
        this.webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .baseUrl(API_URL)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accecToken)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
            .build();
    }


    /**
     * @param targetToken
     * @param title
     * @param body
     * @param param
     * @param description
     */
    @Async
    public void sendFcm(String pushId, String targetToken, String title, String body, String param, String path, String description) throws IOException {
        try {
            String message = makeMessage(targetToken, title, body, param, path, description);
            Mono<String> mono = webClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(message))
                .retrieve()
                .onStatus(status -> status.is5xxServerError() || status.is4xxClientError(),
                    clientResponse -> clientResponse.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(String.class);
            CompletableFuture<String> future = new CompletableFuture<>();
            mono.subscribe(s ->
            {
                log.info("send result : {}", s);
                future.complete(s);
            }, future::completeExceptionally);
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            //에러 기록
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage());
            errorMap.put("pushId", pushId);
            pushMapper.updateMemberPushSendError(errorMap);
            log.error("extraction result error", e);
            new CommonLogicException(ApiState.LOGIC.getCode(), "error-900");
        }
    }


    // 파라미터를 FCM이 요구하는 body 형태로 만들어준다.
    private String makeMessage(String targetToken, String title, String body, String param, String path, String description) throws JsonProcessingException {
        FCMMessage fcmMessage = FCMMessage.builder()
            .message(FCMMessage.Message.builder()
                .token(targetToken)
                .notification(FCMMessage.Notification.builder()
                    .title(title)
                    .body(body)
                    .image(null)
                    .build()
                )
                .data(
                    FCMMessage.Data.builder()
                        .param(param)
                        .path(path)
                        .description(description)
                        .build()
                )
                .apns(FCMMessage.Apns.builder()
                    .payload(
                        FCMMessage.Payload.builder()
                            .aps(FCMMessage.Aps.builder().sound("default").build())
                            .build())
                    .build())
                .android(FCMMessage.Android.builder()
                        .notification(
                            FCMMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()
                    )
                .build())
            .validate_only(false)
            .build();

        return objectMapper.writeValueAsString(fcmMessage);
    }


    private String getAccessToken() throws IOException {
        Resource resource = ResourcePatternUtils
            .getResourcePatternResolver(new DefaultResourceLoader())
            .getResource("classpath:config/nextplayer-6e195-firebase-adminsdk-j7m3n-8e9a2e5140.json");
        InputStream inputStream = resource.getInputStream();
        GoogleCredential googleCredential = GoogleCredential
            .fromStream(inputStream)
            .createScoped(Arrays.asList(SCOPES));
        googleCredential.refreshToken();
        return googleCredential.getAccessToken();
    }

}
