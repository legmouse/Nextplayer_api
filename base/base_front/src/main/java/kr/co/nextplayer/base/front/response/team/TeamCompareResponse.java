package kr.co.nextplayer.base.front.response.team;

import kr.co.nextplayer.base.cup.dto.cup.CupSubMatchDto;
import kr.co.nextplayer.base.front.model.Uage;
import kr.co.nextplayer.base.league.dto.LeagueMatchDto;
import kr.co.nextplayer.base.league.dto.LeagueYearRankDto;
import kr.co.nextplayer.base.team.dto.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamCompareResponse {

    List<HashMap<String, Uage>> areaList;

    CommonTeamDto homeTeamInfoMap;
    TeamTotalResultDto homeTeamMatchResultMap;
    List<TeamMatchDto> homeTeamTotalMatchList;
    List<TeamResultDto> homeTeamAvgGoalList;
    List<TeamResultDto> homeWinningRate;
    List<TeamCupResultDto> homeJoinCupList;
    //List<CupSubMatchDto> homeTeamCupMatchList;
    List<TeamCupResultDto> homeTeamCupResultList;
    List<TeamLeagueEnterDto> homeTeamLeagueEntertList;
    List<LeagueMatchDto> homeTeamLeagueMatchList;
    List<LeagueYearRankDto> homeTeamTeamRankByYear;

    CommonTeamDto awayTeamInfoMap;
    TeamTotalResultDto awayTeamMatchResultMap;
    List<TeamMatchDto> awayTeamTotalMatchList;
    List<TeamResultDto> awayTeamAvgGoalList;
    List<TeamResultDto> awayWinningRate;
    List<TeamCupResultDto> awayJoinCupList;
    //List<CupSubMatchDto> awayTeamCupMatchList;
    List<TeamCupResultDto> awayTeamCupResultList;
    List<TeamLeagueEnterDto> awayTeamLeagueEntertList;
    List<LeagueMatchDto> awayTeamLeagueMatchList;
    List<LeagueYearRankDto> awayTeamTeamRankByYear;

    List<TeamOpponentDto> opponentMatchList;

}
