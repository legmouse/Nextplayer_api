package kr.co.nextplayer.base.cup.dto.cup;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CupInfoListDto {

    private String cup_id;
    private String cup_name;
    private String play_sdate;
    private String play_edate;
    private String ageGroup;

}
