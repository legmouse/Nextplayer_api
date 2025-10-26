package kr.co.nextplayer.base.batch.controller;


import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.batch.firebase.FcmSender;
import kr.co.nextplayer.base.batch.firebase.FcmWebClient;
import kr.co.nextplayer.base.batch.firebase.PushContent;
import kr.co.nextplayer.base.batch.mapper.PushMapper;
import kr.co.nextplayer.base.batch.service.BatchCrawService;
import kr.co.nextplayer.base.batch.service.PushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TestController {

    private final FcmWebClient fcmWebClient;

    private final FcmSender fcmSender;

    private final PushMapper pushMapper;

    private final PushService pushService;

    private final BatchCrawService batchCrawService;

    @ApiOperation(value = "fcm테스트")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true)
    })
    @GetMapping("/test")
    public void test() throws IOException {
        fcmWebClient.sendFcm("1","dD3Kk3uRakvDjB-EVYKm9T:APA91bEkeLHdCJ4OcSu5IHOHWjbrtFy9ke_pgunZx75Wq85LvoLu_9PQr99d9t1tHFxJD2b0ZmlLEyvefd0wHEThBqdY7gMX_RzYXO_RDFRnR3Bcv-7f4fc444qqKsfMorKVUDRHA6TD", "title", "body", "3", "rending", "설명");
    }

    @ApiOperation(value = "fcm테스트 다중발송")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true)
    })
    @GetMapping("/testMany")
    public void testMany() {
//        List<String> list = Arrays.asList("dD3Kk3uRakvDjB-EVYKm9T:APA91bEkeLHdCJ4OcSu5IHOHWjbrtFy9ke_pgunZx75Wq85LvoLu_9PQr99d9t1tHFxJD2b0ZmlLEyvefd0wHEThBqdY7gMX_RzYXO_RDFRnR3Bcv-7f4fc444qqKsfMorKVUDRHA6TD", "cdiqB4ObvEjwvR3pShm0Jn:APA91bH38wwTNBlnmuIc_lGC4kacAd13tQPkvFfOJeH4yngMGRBnzQ6pqNT1GBhuimx-x1Et7cxLvSAZ1lDxXqAgoyCoar5X_vFAyCUzBuzuq3j1hgcyiGh01cMR912QGYSHbr3e3JUO", "ckBQwSHJYk5Gq0fYmVXkhd:APA91bHPmq3dM6G58Sa_FuTKjBanfHR40STK_hdf-_KSQjVoCTNVDHV60D4wKUKxGH1zSp1-OszerBwfiOTfqJuvm7etc2ThKQLJJ62DVKFS6_qqFl8eHnTd6CnXwxp9Wq7cQGa7k6zr", "eNjcecIaQ72LkjKl06J_G2:APA91bFJIiGCRPnQ1mGCP9K-BZExjZVLQ8Kpl-jdnZXKlGdmGYfZeFH3ZGXfiMT-9tcV-TU9kjT6aIn-6yAOHY1-0sLqm-j3PVFo2LoC_8JZr3MWqGEBhDIjc-4Z_EDaF8n_4M0fx1Oy", "cf0ZzCQd_Uo6oHLaFCwoSA:APA91bEMfzUwwjWezu27NfQ3GPJiTEbQ0Qp0R_-17xjS3H0c0IFXpNOLBAME46GhiinlPoptog0Fl03vGRjqz-V4y69rFDOKBQRetISs7E3L6DoMNviKlchxNEd-YjeCriPHHq8fM-Kw", "ebHuYQN6hUB3mSHuEYq_T1:APA91bF155dzK437_bKAJ8MqZn641xn4rZ2oagHORaITFHVC0ptMRGMMLMWQQCeMADDwM06fRb1R3rZSsOMB3XLoi3P8RvSbuvCAd5Lpyy-OhayBwaF_TUOmye3uQ4QYYXyqfiKt0UIu");
        List<String> list = Arrays.asList("dD3Kk3uRakvDjB-EVYKm9T:APA91bEkeLHdCJ4OcSu5IHOHWjbrtFy9ke_pgunZx75Wq85LvoLu_9PQr99d9t1tHFxJD2b0ZmlLEyvefd0wHEThBqdY7gMX_RzYXO_RDFRnR3Bcv-7f4fc444qqKsfMorKVUDRHA6TD", "dXInJPZZQvu8QbcDsIvXS9:APA91bGjkhgWHOmJrvTSYGtOhPQkZhInH5V_uyzp8mROMz1HDXXrPyI16dg3rsv4hjpxHZc8EtS62pVwVuVYh9dXDkFysuv2IDPyZK9C48QTTYv9VFd9Cok6C1me9h1XC-nVmrgDp9yL", "cf0ZzCQd_Uo6oHLaFCwoSA:APA91bEMfzUwwjWezu27NfQ3GPJiTEbQ0Qp0R_-17xjS3H0c0IFXpNOLBAME46GhiinlPoptog0Fl03vGRjqz-V4y69rFDOKBQRetISs7E3L6DoMNviKlchxNEd-YjeCriPHHq8fM-Kw");
        fcmSender.sendFcm(Arrays.asList(1,6,176),list, "테스트좀", "할게요", "3", "rending", "설명");
    }

    @ApiOperation(value = "fcm테스트 추상")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true)
    })
    @GetMapping("/testManyAb")
    public void testManyAb() {
        log.debug("시작");
        PushContent pushContent = pushMapper.selectNonSendPushContent();
        pushService.sendFcm(pushContent);
        log.debug("끝");
    }

    @ApiOperation(value = "craw 테스트")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true)
    })
    @GetMapping("/testCrawScore")
    public void testCrawScore() {
        log.debug("시작");
        batchCrawService.scoreCraw();
        log.debug("끝");
    }
    @ApiOperation(value = "craw 테스트")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true)
    })
    @GetMapping("/testCrawData")
    public void testCrawData() {
        log.debug("시작");
        batchCrawService.playDataCraw();
        log.debug("끝");
    }

}
