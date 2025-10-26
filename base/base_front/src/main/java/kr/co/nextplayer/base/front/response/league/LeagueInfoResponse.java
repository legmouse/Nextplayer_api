package kr.co.nextplayer.base.front.response.league;

import kr.co.nextplayer.base.league.dto.LeagueInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeagueInfoResponse {
    String ageGroup;
    LeagueInfoDto leagueInfo;
}
