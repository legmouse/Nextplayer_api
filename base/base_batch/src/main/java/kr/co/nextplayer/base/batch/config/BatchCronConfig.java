package kr.co.nextplayer.base.batch.config;

import kr.co.nextplayer.base.batch.firebase.PushContent;
import kr.co.nextplayer.base.batch.firebase.PushMember;
import kr.co.nextplayer.base.batch.mapper.PushMapper;
import kr.co.nextplayer.base.batch.service.BatchCrawService;
import kr.co.nextplayer.base.batch.service.PushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@EnableAsync
@Slf4j
public class BatchCronConfig {

    private final PushMapper pushMapper;

    private final PushService pushService;
    private final BatchCrawService batchCrawService;

    /**
     * 푸시 발송
     * 500건씩 끊어서 발송
     */
    @Scheduled(cron = "${cron.message-send}")
    public void messageSend() {
        log.info("메세지 전송 스케줄러 시작");
        PushContent pushContent = pushMapper.selectNonSendPushContent();
        if (null != pushContent) {
            log.info("pushContent : {}", pushContent.getTitle());
            pushService.sendFcm(pushContent);
        }
        log.info("메세지 전송 스케줄러 종료");
    }

    /**
     * 스코어 크롤링
     */
    @Scheduled(cron = "${cron.craw-score}")
    public void crawScore() {
        log.info("크롤링 스코어 시작");
        batchCrawService.scoreCraw();
        log.info("크롤링 스코어 종료");
    }

    /**
     * 라인업 크롤링
     */
    @Scheduled(cron = "${cron.craw-play}")
    public void crawPlay() {
        log.info("크롤링 플레이어 시작");
        batchCrawService.playDataCraw();
        log.info("크롤링 플레이어 종료");
    }

}
