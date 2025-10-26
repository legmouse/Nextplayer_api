package kr.co.nextplayer.base.board.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class EducationReqDto {

    private String memberCd;
    private String educationId;
    private String title;


    private String sKeyword;
    private int curPage;
    private int pageSize;
    private int offset;

}
