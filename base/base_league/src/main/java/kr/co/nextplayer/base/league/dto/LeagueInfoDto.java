package kr.co.nextplayer.base.league.dto;

import kr.co.nextplayer.base.file.model.PublicFileModel;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LeagueInfoDto {

    private String uage;

    private String area_name;
    private String league_id;
    private String league_name;

    private String league_info;
    private String league_prize;

    private String playSdate;
    private String playEdate;
    private String sdate;
    private String edate;
    private String sdate1;
    private String edate1;
    private String sdate2;
    private String edate2;

    private String lyears;

    private String play_flag;
    private String ltCnt;

    private List<PublicFileModel> files = new ArrayList<PublicFileModel>();
    private List<HashMap<String, Object>> allTimeChampions = new ArrayList<HashMap<String, Object>>();

}
