package kr.co.nextplayer.base.cup.dto.cup;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CupResultDto {

    private CupTeamDto cupTeam;

    private String cup_result_id;
    private String result;
    private int sub_rank;
    private int main_rank;
    private int playTotalCnt;
    private int rankPoint;

    private int win;
    private int draw;
    private int lose;

    private int point;
    private int losePoint;
    private int goalPoint;

}
