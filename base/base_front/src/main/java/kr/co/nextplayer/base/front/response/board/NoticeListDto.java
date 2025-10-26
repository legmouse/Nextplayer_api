package kr.co.nextplayer.base.front.response.board;

import io.swagger.annotations.ApiModel;
import kr.co.nextplayer.base.board.model.PublicFile;
import kr.co.nextplayer.base.file.model.PublicFileModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class NoticeListDto {

    private String boardId;
    private String title;

    private String regDate;
    private String parseDate;

    private String useFlag;

    private PublicFile thumbImg = new PublicFile();

}
