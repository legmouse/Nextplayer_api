package kr.co.nextplayer.base.batch.mapper;

import kr.co.nextplayer.base.batch.firebase.PushContent;
import kr.co.nextplayer.base.batch.firebase.PushMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PushMapper {
    PushContent selectNonSendPushContent();

    List<PushMember> selectNonSendMemberPushList(int pushContentId);

    void updateMemberPushSending(int pushId);

    void updateMemberPushSendError(Map<String, String> param);

    void updatePushContentSend(Map<String, Integer> param);

}
