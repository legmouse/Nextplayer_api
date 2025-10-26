package kr.co.nextplayer.base.front.response.board;

import io.swagger.annotations.ApiModel;
import kr.co.nextplayer.base.board.model.PublicFile;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class ColumnListDto {

    private String educationColumnId;
    private String title;
    private String writer;
    private String summary;
    private String columnType;

    private String submitDate;
    private String regDate;

    private String showFlag;
    private String useFlag;

    private List<PublicFile> imgFiles = new ArrayList<PublicFile>();

}
