package kr.co.nextplayer.base.reply.response;

import kr.co.nextplayer.base.reply.dto.ReplyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyResponse {

    private List<ReplyDto> replyList;

}
