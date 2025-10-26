package kr.co.nextplayer.base.backend.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PartnershipDto {

    private String suggest_id;
    private String customer;
    private String phone;
    private String email;
    private String content;
    private String member_cd;
    private String member_nickname;
    private String member_type;
    private String member_name;
    private String reg_date;

    private List<AttchFileInfoDto> files = new ArrayList<AttchFileInfoDto>();

}
