package kr.co.nextplayer.base.cup.dto.cup;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BannerCupListDto {

    private String cup_id;
    private String cup_name;
    private String cup_name2;
    private String place;
    private String play_flag;
    private String play_sdate;
    private String play_edate;
    private String parsSDate;
    private String parsEDate;
    private String progress;
    private String uage;

}
