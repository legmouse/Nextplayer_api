package kr.co.nextplayer.base.front.service;

import kr.co.nextplayer.base.cup.dto.cup.CupInfoListDto;
import kr.co.nextplayer.base.cup.dto.cup.CupPlayDataDto;
import kr.co.nextplayer.base.cup.dto.cup.CupSearchDto;
import kr.co.nextplayer.base.cup.mapper.CupMapper;
import kr.co.nextplayer.base.cup.model.Cup;
import kr.co.nextplayer.base.front.response.GameResponse;
import kr.co.nextplayer.base.league.dto.LeagueInfoDto;
import kr.co.nextplayer.base.league.dto.LeagueSearchMatchDto;
import kr.co.nextplayer.base.league.mapper.LeagueMapper;
import kr.co.nextplayer.base.league.model.League;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@Transactional
public class GameService {

    @Resource
    private CupMapper cupdMapper;

    @Resource
    private LeagueMapper leagueMapper;

    public GameResponse getAllMatchList(Cup cup, League league, String ageGroup) {

        List<CupSearchDto> cupSearchList = cupdMapper.selectSearchCupMatch(cup);
        List<LeagueSearchMatchDto> leagueSearchList = leagueMapper.selectSearchLeagueMatch(league);

        List<CupInfoListDto> cupInfoList = cupdMapper.selectCupInfoList(cup);
        List<LeagueInfoDto> leagueInfoList = leagueMapper.selectLeagueInfoList(league);

        GameResponse gameResponse = GameResponse.builder()
                .ageGroup(ageGroup)
                .cupSearchList(cupSearchList)
                .leagueSearchList(leagueSearchList)
                .cupInfoList(cupInfoList)
                .leagueInfoList(leagueInfoList)
                .build();

        return gameResponse;
    }

    public CupPlayDataDto getMatchPlayData(String matchId, String ageGroup, String gameType, String cupCategory) {

        if (gameType.equals("Cup")) {
            HashMap<String, Object> cupMatchDataMap = makeMatchDataMap(matchId, ageGroup, gameType, cupCategory);
            CupPlayDataDto cupPlayDataDto = CupPlayDataDto.builder()
                .homeAwayMatchPlayData(cupdMapper.selectPlayDataCup(cupMatchDataMap))
                .homeAwayMatchChangeData(cupdMapper.selectChangeDataCup(cupMatchDataMap))
                .homeAwayOwnGoalData(cupdMapper.selectOwnGoalDataCup(cupMatchDataMap))
                .homeAwayStaffData(cupdMapper.selectStaffDataCup(cupMatchDataMap))
                .homeAwayStaffPenaltyData(cupdMapper.selectStaffPenaltyDataCup(cupMatchDataMap))
                .build();

            return cupPlayDataDto;
        } else if (gameType.equals("League")) {
            HashMap<String, Object> leagueMatchDataMap = makeMatchDataMap(matchId, ageGroup, gameType, cupCategory);
            CupPlayDataDto leaguePlayDataDto = CupPlayDataDto.builder()
            		.homeAwayMatchPlayData(cupdMapper.selectPlayDataCup(leagueMatchDataMap))
            		.homeAwayMatchChangeData(cupdMapper.selectChangeDataCup(leagueMatchDataMap))
                    .homeAwayOwnGoalData(cupdMapper.selectOwnGoalDataCup(leagueMatchDataMap))
                    .homeAwayStaffData(cupdMapper.selectStaffDataCup(leagueMatchDataMap))
                    .homeAwayStaffPenaltyData(cupdMapper.selectStaffPenaltyDataCup(leagueMatchDataMap))
            		.build();

            return leaguePlayDataDto;
        } else {
            return CupPlayDataDto.builder().build();
        }

    }

    private HashMap<String, Object> makeMatchDataMap(String matchId, String ageGroup, String gameType, String cupCategory) {
        HashMap<String, Object> hashMap = new HashMap<>();

        String playDataTB = "";
        String changeDataTB = "";
        String staffDataTB = "";
        String staffPenaltyDataTB = "";
        String ownGoalDataTB = "";

        if (gameType.equals("Cup")) {
            playDataTB = ageGroup + "_Cup_" + cupCategory + "_Match_Play_Data";
            changeDataTB = ageGroup + "_Cup_" + cupCategory + "_Match_Change_Data";
            staffDataTB = ageGroup + "_Cup_" + cupCategory + "_Staff_Data";
            staffPenaltyDataTB = ageGroup + "_Cup_" + cupCategory + "_Staff_Penalty_Data";
            ownGoalDataTB = ageGroup + "_Cup_" + cupCategory + "_Own_Goal_Data";

            hashMap.put("playDataTB", playDataTB);
            hashMap.put("changeDataTB", changeDataTB);
            hashMap.put("staffDataTB", staffDataTB);
            hashMap.put("staffPenaltyDataTB", staffPenaltyDataTB);
            hashMap.put("ownGoalDataTB", ownGoalDataTB);
            hashMap.put("matchId", matchId);
        } else if (gameType.equals("League")) {
            playDataTB = ageGroup + "_League_Match_Play_Data";
            changeDataTB = ageGroup + "_League_Match_Change_Data";
            staffDataTB = ageGroup + "_League_Staff_Data";
            staffPenaltyDataTB = ageGroup + "_League_Staff_Penalty_Data";
            ownGoalDataTB = ageGroup + "_League_Own_Goal_Data";

            hashMap.put("playDataTB", playDataTB);
            hashMap.put("changeDataTB", changeDataTB);
            hashMap.put("staffDataTB", staffDataTB);
            hashMap.put("staffPenaltyDataTB", staffPenaltyDataTB);
            hashMap.put("ownGoalDataTB", ownGoalDataTB);
            hashMap.put("matchId", matchId);
        }

        return hashMap;
    }

}
