package kr.co.nextplayer.base.cup.dto.cup;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TotalCupListDto {

    String cup_id;
    String cup_name;
    String cup_team;
    String play_flag;
    String uage;
    String progress;

}
