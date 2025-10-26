package kr.co.nextplayer.base.reply.mapper;

import kr.co.nextplayer.base.reply.dto.ReplyReqDto;
import kr.co.nextplayer.base.reply.model.Reply;

import java.util.List;

public interface ReplyMapper {

    int insertReply(ReplyReqDto replyReqDto);

    int updateReply(ReplyReqDto replyReqDto);

    int deleteReply(ReplyReqDto replyReqDto);

    List<Reply> selectReplyList(String communityId);

    List<Reply> selectRepliesByParentId(String replyId);

    int selectReplyLikesCnt(String replyId);

}
