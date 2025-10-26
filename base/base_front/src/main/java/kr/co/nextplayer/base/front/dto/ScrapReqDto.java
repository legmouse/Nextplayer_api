package kr.co.nextplayer.base.front.dto;

import io.swagger.annotations.ApiModel;
import kr.co.nextplayer.base.front.constants.ScrapForeign;
import lombok.*;
import springfox.documentation.annotations.ApiIgnore;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class ScrapReqDto {

    private long scrapNo;
    private long foreignId;
    private String subject;
    private String ageGroup;
    private ScrapForeign foreignType;
    private String memberCd;

}
