package kr.co.nextplayer.base.cup.dto.cup;

import kr.co.nextplayer.base.team.dto.HomeAwayTeamDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CupSearchDto {

    private String cup_id;
    private String cup_name;

    private String place;
    private String pdate;
    private String time_diff;
    private String parse_date;

    private String video_live;

    private String match_id;
    private String match_date;
    private String match_category;
    private String matchType;

    private String end_flag;


    private HomeAwayTeamDto homeAwayTeam;
    private List<CupMediaDto> mediaList = new ArrayList<CupMediaDto>();
}
