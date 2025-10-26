package kr.co.nextplayer.base.front.response.board;

import kr.co.nextplayer.base.board.dto.NoticeDto;
import kr.co.nextplayer.base.board.dto.NoticeInfoDto;
import kr.co.nextplayer.base.board.dto.ReferenceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeInfoResponse {

    NoticeInfoDto noticeInfo;

}
