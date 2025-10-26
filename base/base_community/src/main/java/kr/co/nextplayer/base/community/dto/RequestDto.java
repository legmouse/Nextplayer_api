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
public class RequestDto {

    private String requestId;

    private String memberCd;

    private String ageGroup;

    private String parentType;

    private String parentId;

    private String foreignType;

    private String foreignId;

    private String content;

    private String requestType;

    private List<String> fileId;

}
