package kr.co.nextplayer.base.reply.service;

import kr.co.nextplayer.base.reply.dto.ReplyDto;
import kr.co.nextplayer.base.reply.dto.ReplyReqDto;
import kr.co.nextplayer.base.reply.mapper.ReplyMapper;
import kr.co.nextplayer.base.reply.model.Reply;
import kr.co.nextplayer.base.reply.response.ReplyResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ReplyService {

    @Resource
    private ReplyMapper replyMapper;

    public int saveReply(ReplyReqDto replyReqDto) {
        return replyMapper.insertReply(replyReqDto);
    }

    public int modifyReply(ReplyReqDto replyReqDto) {
        return replyMapper.updateReply(replyReqDto);
    }

    public int deleteReply(ReplyReqDto replyReqDto) {
        return replyMapper.deleteReply(replyReqDto);
    }

    public ReplyResponse replyList(String communityId) {

        List<ReplyDto> replyList = new ArrayList<ReplyDto>();

        try {
            List<Reply> list = replyMapper.selectReplyList(communityId);
            if (list.size() > 0) {
                for (Reply reply : list) {
                    int likeCnt = replyMapper.selectReplyLikesCnt(reply.getReplyId());

                    ReplyDto replyDto = ReplyDto.builder()
                        .reply_id(reply.getReplyId())
                        .community_id(reply.getCommunityId())
                        .member_cd(reply.getMemberCd())
                        .content(reply.getContent())
                        .parent_id(reply.getParentId())
                        .use_flag(reply.getUseFlag())
                        .reg_date(reply.getRegDate())
                        .replies(reply.getReplies())
                        //.likes_cnt(likeCnt)
                        .build();

                    replyList.add(replyDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ReplyResponse replyResponse = ReplyResponse.builder()
            .replyList(replyList)
            .build();

        return replyResponse;
    }

}
