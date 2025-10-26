package kr.co.nextplayer.base.league.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LeagueListDto {

    private String uage;

    private String league_id;
    private String area_name;
    private String league_name;

}
