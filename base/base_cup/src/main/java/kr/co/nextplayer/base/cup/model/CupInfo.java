package kr.co.nextplayer.base.cup.model;

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
public class CupInfo {

    private String cupId;
    private String cupName;
    private String cupTeam;
    private String cupType;
    private String cupInfo;
    private String cupPrize;
    private String tourType;
    private String tourTeam;
    private String playSdate;
    private String playEdate;
    private String sdate;
    private String edate;
    private String sdate1;
    private String edate1;
    private String sdate2;
    private String edate2;
    private String lyears;
    private String playFlag;
    private String subTeamCount;
    private String mainTeamCount;
    private String groupCount;
    private String teamCount;
    private String mGroupCount;
    private String mTeamCount;

    private String subMainType;

    private List<PublicFileModel> files = new ArrayList<PublicFileModel>();

    private List<HashMap<String, Object>> allTimeChampions = new ArrayList<HashMap<String, Object>>();

}
