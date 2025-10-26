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
public class EducationListDto {

    private String educationId;
    private String title;
    private String summary;
    private String playTime;
    private String isChecked;

    private String regDate;

    private String showFlag;
    private String useFlag;

    private List<PublicFile> imgFiles = new ArrayList<PublicFile>();

}
