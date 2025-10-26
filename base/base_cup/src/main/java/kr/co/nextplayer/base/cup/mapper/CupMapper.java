package kr.co.nextplayer.base.cup.mapper;

import kr.co.nextplayer.base.cup.dto.cup.*;
import kr.co.nextplayer.base.cup.model.Cup;
import kr.co.nextplayer.base.cup.model.CupInfo;
import kr.co.nextplayer.base.cup.model.CupMatch;
import kr.co.nextplayer.base.team.dto.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CupMapper {

    /**
     *  메인 배너 - 연령별 대회 일정
     */
    List<BannerCupListDto> selectBannerCupList(Cup cup);

    /**
     *  메인 - 연령별 대회 경기결과
     */
    List<CupInfoListDto> selectCupInfoList(Cup cup);

    /**
     *  메인 - 연령별 대회 경기결과
     */
    List<CupSubMatchDto> selectCupMatchList(CupMatch cupMatch);

    /**
     * 전연령 대회 리스트
     * @param cup
     * @return
     */
    List<TotalCupListDto> selectCupList(Cup cup);

    /**
     * 대회 정보
     * @param cup
     * @return
     */
    CupInfo selectCupInfo(Cup cup);

    /**
     * 대회 예선 팀내역 (순위표)
     * @param cup
     * @return
     */
    List<CupTeamDto> selectCupSubTeam(Cup cup);

    List<CupResultDto> selectCupSubMatchRankByFinal(Cup cup);

    List<CupMatchCalendarDto> selectCupSubMatchCalendar(Cup cup);

    List<CupSubMatchDto> selectCupSubMatchResultList(Cup cup);

    List<CupTeamDto> selectCupMainTeam(Cup cup);

    List<CupResultDto> selectCupMainMatchRankByFinal(Cup cup);

    List<CupMatchCalendarDto> selectCupMainMatchCalendar(Cup cup);

    List<CupMainMatchDto> selectCupMainMatchResultList(Cup cup);

    List<CupTourMatchDto> selectCupTourEmptyMatchList(Cup cup);

    List<CupTourMatchDto> selectCupTourMatchResultList(Cup cup);

    List<TeamCupResultDto> selectTeamCupResult(Cup cup);

    List<CupSubMatchDto> selectTeamCupMatch(Cup cup);

    List<TeamCupResultDto> selectTeamCupResultRenewal(Cup cup);

    List<CupSearchDto> selectSearchCupMatch(Cup cup);

    List<HashMap<String, Object>> selectCupInfoListForMedia(Cup cup);

    List<HashMap<String, Object>> selectCupMatchListForMedia(Cup cup);

    List<TeamCupResultDto> selectTeamJoinCupLastThreeYear(Cup cup);

    List<TeamCupResultDto> selectTeamJoinCupThisYear(Cup cup);

    HashMap<String, Object> selectCupSummaryData(Cup cup);

    List<HashMap<String, Object>> selectCupTodayMatchList(Cup cup);

    int selectIsTodayMatch(Map<String, Object> param);

    List<HomeAwayMatchPlayDataDto> selectPlayDataCup(Map<String, Object> param);
    List<HomeAwayMatchChangeDataDto> selectChangeDataCup(Map<String, Object> param);
    List<HomeAwayStaffDataDto> selectStaffDataCup(Map<String, Object> param);
    List<HomeAwayOwnGoalDataDto> selectOwnGoalDataCup(Map<String, Object> param);
    List<HomeAwayStaffPenaltyDataDto> selectStaffPenaltyDataCup(Map<String, Object> param);
}
