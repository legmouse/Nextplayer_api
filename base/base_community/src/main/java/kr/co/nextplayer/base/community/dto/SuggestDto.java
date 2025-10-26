package kr.co.nextplayer.base.community.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class SuggestDto {

    private String suggestId;

    private String memberCd;

    private String title;

    private String content;

    private String secretFlag;

    private List<String> fileId;

}
