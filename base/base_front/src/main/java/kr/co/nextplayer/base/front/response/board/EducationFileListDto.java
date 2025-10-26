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
public class EducationFileListDto {

    private String educationFileId;
    private String title;
    private String memberCd;
    private String regDate;

    private List<PublicFile> eduFile = new ArrayList<PublicFile>();

}
