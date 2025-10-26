package kr.co.nextplayer.base.front.response.board;

import kr.co.nextplayer.base.board.model.Popup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PopupListResponse {

    private int totalCount;
    private List<Popup> popupList;

}
