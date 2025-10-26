package kr.co.nextplayer.base.board.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class EducationAuthReqDto {

    private String educationId;
    private String goodsId;
    private String goodsCode;
    private String memberCd;

}
