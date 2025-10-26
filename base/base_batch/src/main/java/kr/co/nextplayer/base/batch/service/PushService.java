package kr.co.nextplayer.base.batch.service;

import kr.co.nextplayer.base.batch.firebase.FcmSender;
import kr.co.nextplayer.base.batch.firebase.PushContent;
import kr.co.nextplayer.base.batch.firebase.PushMember;
import kr.co.nextplayer.base.batch.mapper.PushMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableAsync
public class PushService {

    private final FcmSender fcmSender;

    private final PushMapper pushMapper;

    private final int batchSize = 500;

    @Async
    public void sendFcm(PushContent pushContent) {

        //푸시 받을 맴버들
        List<PushMember> pushMembers = pushMapper.selectNonSendMemberPushList(pushContent.getPushContentId());
        int successCount = 0;
        int failureCount = 0;

        //n건씩 나눠서 발송
        for (int i = 0; i < pushMembers.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, pushMembers.size());
            List<PushMember> batchList = pushMembers.subList(i, endIndex);
            Map<String, Integer> resultMap = processPush(batchList, pushContent);
            successCount += resultMap.get("successCount");
            failureCount += resultMap.get("failureCount");
        }

        //푸시 컨텐츠 업데이트
        HashMap<String, Integer> param = new HashMap<>();
        param.put("successCount", successCount);
        param.put("failureCount", failureCount);
        param.put("pushContentId", pushContent.getPushContentId());
        pushMapper.updatePushContentSend(param);
    }

    public Map<String, Integer> processPush(List<PushMember> pushMembers, PushContent pushContent) {
        List<Integer> pushIds = pushMembers.stream()
            .map(PushMember::getPushId)
            .collect(Collectors.toList());

        List<String> tokens = pushMembers.stream()
            .map(PushMember::getFcmToken)
            .collect(Collectors.toList());

        return fcmSender.sendFcm(pushIds, tokens, pushContent.getTitle(), pushContent.getBody(), pushContent.getParam(), pushContent.getPath(), pushContent.getDescription());
    }

}
