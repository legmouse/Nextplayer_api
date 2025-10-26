package kr.co.nextplayer.base.cup.dto.cup;

import kr.co.nextplayer.base.team.dto.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CupMainMatchDto {

    private String cup_id;

    private String cup_main_match_id;
    private String match_id;
    private String groups;
    private String playdate;
    private String pdate;
    private String ptime;
    private String place;
    private String time_diff;
    private String video_live;

    private String cup_type;
    private String match_date;

    private String url_link;
    private String media_type;

    private String match_type;
    private String match_category;

    private String reason;
    private String parse_date;

    private HomeAwayTeamDto homeAwayTeam;

    private List<HomeAwayMatchPlayDataDto> homeAwayMatchPlayData;
    private List<HomeAwayMatchChangeDataDto> homeAwayMatchChangeData;
    private List<HomeAwayStaffDataDto> homeAwayStaffData;
    private List<HomeAwayStaffPenaltyDataDto> homeAwayStaffPenaltyData;
    private List<HomeAwayOwnGoalDataDto> homeAwayOwnGoalData;

    private List<CupMediaDto> mediaList = new ArrayList<CupMediaDto>();
}
