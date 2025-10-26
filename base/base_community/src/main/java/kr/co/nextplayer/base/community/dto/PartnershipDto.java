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
public class PartnershipDto {

    private String suggestId;

    private String memberCd;

    private String email;

    private String customer;

    private String phone;

    private String content;

    private List<String> fileId;

}
