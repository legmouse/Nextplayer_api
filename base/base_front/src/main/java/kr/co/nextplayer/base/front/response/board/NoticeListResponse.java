package kr.co.nextplayer.base.front.response.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeListResponse {

    private int totalCount;
    private List<NoticeListDto> noticeList;

}
