package kr.co.nextplayer.base.board.dto;

import io.swagger.annotations.ApiModel;
import kr.co.nextplayer.base.file.model.PublicFileModel;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class ReferenceDto {

    private String reference_id;
    private String title;
    private String content;
    private String regDate;

    private List<PublicFileModel> files = new ArrayList<PublicFileModel>();

}
