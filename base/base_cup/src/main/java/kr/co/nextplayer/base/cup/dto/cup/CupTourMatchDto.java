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
public class CupTourMatchDto {

    private String cup_id;

    private String cup_tour_match_id;
    private String match_id;
    private String round;

    private String tour_no;
    private String next_tour_no;
    private String next_tour_port;

    private String playdate;
    private String pdate;
    private String ptime;
    private String place;
    private String time_diff;
    private String video_live;
    private String video_rep;
    private String video_high;

    private String url_link;
    private String media_type;


    private String cup_type;
    private String match_type;
    private String match_category;

    private String match_date;
    private String reason;
    private String parse_date;

    private String end_flag;

    private HomeAwayTeamDto homeAwayTeam;

    private List<HomeAwayMatchPlayDataDto> homeAwayMatchPlayData;
    private List<HomeAwayMatchChangeDataDto> homeAwayMatchChangeData;
    private List<HomeAwayStaffDataDto> homeAwayStaffData;
    private List<HomeAwayStaffPenaltyDataDto> homeAwayStaffPenaltyData;
    private List<HomeAwayOwnGoalDataDto> homeAwayOwnGoalData;

    private List<CupMediaDto> mediaList = new ArrayList<CupMediaDto>();
}
