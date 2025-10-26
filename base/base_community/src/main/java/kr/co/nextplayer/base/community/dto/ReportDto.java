package kr.co.nextplayer.base.community.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class ReportDto {

    private String memberCd;

    private String foreignType;

    private String foreignId;

    private String reason;

    private String useFlag;

    private String regDate;

}
