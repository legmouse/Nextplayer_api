package kr.co.nextplayer.base.board.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class EduFile implements Serializable {

    private static final long serialVersionUID = 1L;

    private String educationFileId;
    private String memberCd;
    private String title;
    private String regDate;

    private String useFlag;

    private List<PublicFile> eduFile = new ArrayList<PublicFile>();

}
