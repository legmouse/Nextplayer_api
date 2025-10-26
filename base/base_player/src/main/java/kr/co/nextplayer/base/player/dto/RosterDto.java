package kr.co.nextplayer.base.player.dto;

import io.swagger.annotations.ApiModel;
import kr.co.nextplayer.base.player.model.PublicFile;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class RosterDto {

    private String roster_id;
    private String title;

    private String comment;
    private String year;
    private String uage;
    private String roster_type;
    private String type;

    private String center_type;
    private String reg_date;

    private List<PublicFile> rosterFiles = new ArrayList<PublicFile>();
    /*public RosterDto (String rosterId, String title, String year, String uage, String rosterType, String type, String regDate) {
        this.roster_id = rosterId;
        this.title = title;
        this.year = year;
        this.uage = uage;
        this.roster_type = rosterType;
        this.type = type;
        this.reg_date = regDate;
    }

    public RosterDto(String rosterId, String title, String comment, String year, String uage, String rosterType, String type, String regDate) {
        this.roster_id = rosterId;
        this.title = title;
        this.comment = comment;
        this.year = year;
        this.uage = uage;
        this.roster_type = rosterType;
        this.type = type;
        this.reg_date = regDate;
    }*/
}
