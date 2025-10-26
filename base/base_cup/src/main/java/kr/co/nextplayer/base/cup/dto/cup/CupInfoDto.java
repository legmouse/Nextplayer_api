package kr.co.nextplayer.base.cup.dto.cup;

import kr.co.nextplayer.base.file.model.PublicFileModel;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CupInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cup_id;
    private String cup_name;
    private String cup_team;
    private String cup_type;
    private String cup_info;
    private String cup_prize;
    private String tour_type;
    private String tour_team;
    private String play_sdate;
    private String play_edate;
    private String sdate;
    private String edate;
    private String sdate1;
    private String edate1;
    private String sdate2;
    private String edate2;
    private String lyears;
    private String play_flag;
    private String sub_team_count;
    private String main_team_count;
    private String group_count;
    private String team_count;
    private String m_group_count;
    private String m_team_count;

    private String sub_main_type;

    private String teamType;

    private List<PublicFileModel> files = new ArrayList<PublicFileModel>();

    private List<HashMap<String, Object>> allTimeChampions = new ArrayList<HashMap<String, Object>>();

}
