package kr.co.nextplayer.base.front.service;

import io.swagger.models.auth.In;
import kr.co.nextplayer.base.cup.dto.cup.*;
import kr.co.nextplayer.base.cup.mapper.CupMapper;
import kr.co.nextplayer.base.cup.model.Cup;
import kr.co.nextplayer.base.cup.model.CupInfo;
import kr.co.nextplayer.base.front.mapper.UageMapper;
import kr.co.nextplayer.base.front.model.Uage;
import kr.co.nextplayer.base.front.response.CupResponse;
import kr.co.nextplayer.base.front.response.cup.*;
import kr.co.nextplayer.base.front.util.StrUtil;
import kr.co.nextplayer.base.team.dto.HomeAwayMatchPlayDataDto;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

import static java.util.Arrays.asList;

@Slf4j
@Service
@Transactional
public class CupService {

    @Resource
    private UageMapper uageMapper;
    @Resource
    private CupMapper cupMapper;

    /**
     * 진행중인 대회 목록
     * @param cup
     * @return
     */
    public List<TotalCupListDto> selectCupList(Cup cup) {
        return cupMapper.selectCupList(cup);
    }

    /**
     * 대회 상세
     * @param cup
     * @return
     */
    public CupInfo selectCupInfo(Cup cup) {
        return cupMapper.selectCupInfo(cup);
    }

    /**
     * 조별 예선 참가 팀 목록
     * @param cup
     * @return
     */
    public List<CupTeamDto> selectCupSubTeam(Cup cup) {
        return cupMapper.selectCupSubTeam(cup);
    }

    /**
     * 예선 순위 목록
     * @param cup
     * @return
     */
    public List<CupResultDto> selectCupSubMatchRankByFinal(Cup cup) {
        return cupMapper.selectCupSubMatchRankByFinal(cup);
    }

    /**
     * 예선 경기일정 캘린더
     * @param cup
     * @return
     */
    public List<CupMatchCalendarDto> selectCupSubMatchCalendar(Cup cup) {
        return cupMapper.selectCupSubMatchCalendar(cup);
    }

    /**
     * 예선 결과 리스트
     * @param cup
     * @return
     */
    public List<CupSubMatchDto> selectCupSubMatchResultList(Cup cup) {
        return cupMapper.selectCupSubMatchResultList(cup);
    }

    /**
     * 조별 본선 참가 팀 목록
     * @param cup
     * @return
     */
    public List<CupTeamDto> selectCupMainTeam(Cup cup) {
        return cupMapper.selectCupMainTeam(cup);
    }

    /**
     * 본선 순위 목록
     * @param cup
     * @return
     */
    public List<CupResultDto> selectCupMainMatchRankByFinal(Cup cup) {
        return cupMapper.selectCupMainMatchRankByFinal(cup);
    }

    /**
     * 본선 경기일정 캘린더
     * @param cup
     * @return
     */
    public List<CupMatchCalendarDto> selectCupMainMatchCalendar(Cup cup) {
        return cupMapper.selectCupMainMatchCalendar(cup);
    }

    /**
     * 본선 결과 리스트
     * @param cup
     * @return
     */
    public List<CupMainMatchDto> selectCupMainMatchResultList(Cup cup) {
        return cupMapper.selectCupMainMatchResultList(cup);
    }

    /**
     * 대회 토너먼트 경기 목록
     * @param cup
     * @return
     */
    public List<CupTourMatchDto> selectCupTourEmptyMatchList(Cup cup) {
        return cupMapper.selectCupTourEmptyMatchList(cup);
    }

    /**
     * 대회 토너먼트 경기 결과
     * @param cup
     * @return
     */
    public List<CupTourMatchDto> selectCupTourMatchResultList(Cup cup) {
        return cupMapper.selectCupTourMatchResultList(cup);
    }


    public CupListResponse getCupList(Cup cupParam) {

        Map<String, String> params = new HashMap<String, String>();

        List<HashMap<String, Object>> uageList = uageMapper.selectUageList();

        List<TotalCupListDto> cupList = new ArrayList<TotalCupListDto>();

        List<TotalCupListDto> totalCupList = new ArrayList<TotalCupListDto>();

        if (StrUtil.isEmpty(cupParam.getAgeGroup())) {

            for (HashMap<String, Object> map : uageList) {
                if (map.get("interest_flag").toString().equals("1")) {
                    cupParam.setAgeGroup(map.get("uage").toString());
                }
            }
        }

        String cupInfoTB = cupParam.getAgeGroup() + "_Cup_Info";

        cupParam.setCupInfoTB(cupInfoTB);

        cupList = cupMapper.selectCupList(cupParam);

        totalCupList.addAll(cupList);

        CupListResponse cupResponse = CupListResponse.builder()
            .uageList(uageList)
            .totalCupList(totalCupList)
            .build();

        return cupResponse;
    }

    public CupInfoResponse getCupInfo(Cup cupParam) {

        CupInfo cupInfo = cupMapper.selectCupInfo(cupParam);

        cupParam.setCupInfoTB(cupParam.getAgeGroup() + "_Cup_Info");
        cupParam.setCupSubMatchTB(cupParam.getAgeGroup() + "_Cup_Sub_Match");
        cupParam.setCupMainMatchTB(cupParam.getAgeGroup() + "_Cup_Main_Match");
        cupParam.setCupTourMatchTB(cupParam.getAgeGroup() + "_Cup_Tour_Match");
        cupParam.setCupResultTB(cupParam.getAgeGroup() + "_Cup_Result");
        HashMap<String, Object> summaryData = cupMapper.selectCupSummaryData(cupParam);

        CupInfoDto cupInfoMap = CupInfoDto.builder()
            .cup_id(cupInfo.getCupId())
            .cup_name(cupInfo.getCupName())
            .cup_team(cupInfo.getCupTeam())
            .cup_type(cupInfo.getCupType())
            .cup_info(cupInfo.getCupInfo())
            .cup_prize(cupInfo.getCupPrize())
            .tour_type(cupInfo.getTourType())
            .tour_team(cupInfo.getTourTeam())
            .play_sdate(cupInfo.getSdate())
            .play_edate(cupInfo.getEdate())
            .sdate(cupInfo.getSdate())
            .edate(cupInfo.getEdate())
            .sdate1(cupInfo.getSdate1())
            .edate1(cupInfo.getEdate1())
            .sdate2(cupInfo.getSdate2())
            .edate2(cupInfo.getEdate2())
            .lyears(cupInfo.getLyears())
            .play_flag(cupInfo.getPlayFlag())
            .sub_team_count(cupInfo.getSubTeamCount())
            .main_team_count(cupInfo.getMainTeamCount())
            .group_count(cupInfo.getGroupCount())
            .team_count(cupInfo.getTeamCount())
            .m_group_count(cupInfo.getMGroupCount())
            .m_team_count(cupInfo.getMTeamCount())
            .files(cupInfo.getFiles())
            .allTimeChampions(cupInfo.getAllTimeChampions())
            .build();

        CupInfoResponse cupResponse = CupInfoResponse.builder()
            .ageGroup(cupParam.getAgeGroup())
            .cupInfoMap(cupInfoMap)
            .summaryData(summaryData)
            .build();

        return cupResponse;
    }

    public CupSubMatchResponse getCupSubMatch(Cup cupParam) {

        CupInfo cupInfo = cupMapper.selectCupInfo(cupParam);
        cupInfo.setSubMainType("1");
        CupInfoDto cupInfoMap = CupInfoDto.builder()
            .cup_id(cupInfo.getCupId())
            .cup_name(cupInfo.getCupName())
            .cup_team(cupInfo.getCupTeam())
            .cup_type(cupInfo.getCupType())
            .cup_info(cupInfo.getCupInfo())
            .cup_prize(cupInfo.getCupPrize())
            .tour_type(cupInfo.getTourType())
            .tour_team(cupInfo.getTourTeam())
            .play_sdate(cupInfo.getSdate())
            .play_edate(cupInfo.getEdate())
            .sdate(cupInfo.getSdate())
            .edate(cupInfo.getEdate())
            .sdate1(cupInfo.getSdate1())
            .edate1(cupInfo.getEdate1())
            .sdate2(cupInfo.getSdate2())
            .edate2(cupInfo.getEdate2())
            .lyears(cupInfo.getLyears())
            .play_flag(cupInfo.getPlayFlag())
            .sub_team_count(cupInfo.getSubTeamCount())
            .main_team_count(cupInfo.getMainTeamCount())
            .group_count(cupInfo.getGroupCount())
            .team_count(cupInfo.getTeamCount())
            .m_group_count(cupInfo.getMGroupCount())
            .m_team_count(cupInfo.getMTeamCount())
            .files(cupInfo.getFiles())
            .teamType(cupParam.getTeamType())
            .sub_main_type(cupInfo.getSubMainType())
            .build();

        List<CupTeamDto> cupSubTeamList = cupMapper.selectCupSubTeam(cupParam);
        List<CupResultDto> cupSubRankList = cupMapper.selectCupSubMatchRankByFinal(cupParam);
        List<CupMatchCalendarDto> cupSubMatchCalendar = cupMapper.selectCupSubMatchCalendar(cupParam);
        List<CupSubMatchDto> cupSubMatchList = cupMapper.selectCupSubMatchResultList(cupParam);

        CupSubMatchResponse cupResponse = CupSubMatchResponse.builder()
            .ageGroup(cupParam.getAgeGroup())
            .cupInfoMap(cupInfoMap)
            .cupSubTeamList(cupSubTeamList)
            .cupSubRankList(cupSubRankList)
            .cupSubMatchCalendar(cupSubMatchCalendar)
            .cupSubMatchList(cupSubMatchList)
            .build();

        return cupResponse;
    }

    public CupMainMatchResponse getCupMainMatch(Cup cupParam) {

        CupInfo cupInfo = cupMapper.selectCupInfo(cupParam);
        cupInfo.setSubMainType("2");
        CupInfoDto cupInfoMap = CupInfoDto.builder()
            .cup_id(cupInfo.getCupId())
            .cup_name(cupInfo.getCupName())
            .cup_team(cupInfo.getCupTeam())
            .cup_type(cupInfo.getCupType())
            .cup_info(cupInfo.getCupInfo())
            .cup_prize(cupInfo.getCupPrize())
            .tour_type(cupInfo.getTourType())
            .tour_team(cupInfo.getTourTeam())
            .play_sdate(cupInfo.getSdate())
            .play_edate(cupInfo.getEdate())
            .sdate(cupInfo.getSdate())
            .edate(cupInfo.getEdate())
            .sdate1(cupInfo.getSdate1())
            .edate1(cupInfo.getEdate1())
            .sdate2(cupInfo.getSdate2())
            .edate2(cupInfo.getEdate2())
            .lyears(cupInfo.getLyears())
            .play_flag(cupInfo.getPlayFlag())
            .sub_team_count(cupInfo.getSubTeamCount())
            .main_team_count(cupInfo.getMainTeamCount())
            .group_count(cupInfo.getGroupCount())
            .team_count(cupInfo.getTeamCount())
            .m_group_count(cupInfo.getMGroupCount())
            .m_team_count(cupInfo.getMTeamCount())
            .files(cupInfo.getFiles())
            .sub_main_type(cupInfo.getSubMainType())
            .build();

        List<CupTeamDto> cupMainTeamList = cupMapper.selectCupMainTeam(cupParam);
        List<CupResultDto> cupMainRankList = cupMapper.selectCupMainMatchRankByFinal(cupParam);
        List<CupMatchCalendarDto> cupMainMatchCalendar = cupMapper.selectCupMainMatchCalendar(cupParam);
        List<CupMainMatchDto> cupMainMatchList = cupMapper.selectCupMainMatchResultList(cupParam);

        CupMainMatchResponse cupResponse = CupMainMatchResponse.builder()
            .ageGroup(cupParam.getAgeGroup())
            .cupInfoMap(cupInfoMap)
            .cupMainTeamList(cupMainTeamList)
            .cupSubRankList(cupMainRankList)
            .cupSubMatchCalendar(cupMainMatchCalendar)
            .cupSubMatchList(cupMainMatchList)
            .build();

        return cupResponse;
    }
    @Builder
    private static class RoundDto {
        private String label;
        private int value;
    }
    public CupTourMatchResponse getTourMainMatch(Cup cupParam) {

        CupInfo cupInfo = cupMapper.selectCupInfo(cupParam);

        List<Integer> arr = new ArrayList<Integer>(asList(128, 64, 32, 16, 8, 4, 2));

        int tourTeam = Integer.parseInt(cupInfo.getTourTeam());

        List<CupRoundDto> roundData = new ArrayList();

        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i) == 2) {
                CupRoundDto roundDto = CupRoundDto.builder()
                    .label("결승")
                    .value(arr.get(i))
                    .build();
                roundData.add(roundDto);
            }
            if (arr.get(i) > 2 && arr.get(i) <= tourTeam) {
                CupRoundDto roundDto = CupRoundDto.builder()
                    .label(arr.get(i) + "강")
                    .value(arr.get(i))
                    .build();
                roundData.add(roundDto);
            }
            if (arr.get(i) > tourTeam && (arr.get(i) - tourTeam) < tourTeam) {
                CupRoundDto roundDto = CupRoundDto.builder()
                    .label("PO")
                    .value(tourTeam)
                    .build();
                roundData.add(roundDto);
            }
        }

        CupInfoDto cupInfoMap = CupInfoDto.builder()
            .cup_id(cupInfo.getCupId())
            .cup_name(cupInfo.getCupName())
            .cup_team(cupInfo.getCupTeam())
            .cup_type(cupInfo.getCupType())
            .cup_info(cupInfo.getCupInfo())
            .cup_prize(cupInfo.getCupPrize())
            .tour_type(cupInfo.getTourType())
            .tour_team(cupInfo.getTourTeam())
            .play_sdate(cupInfo.getSdate())
            .play_edate(cupInfo.getEdate())
            .sdate(cupInfo.getSdate())
            .edate(cupInfo.getEdate())
            .sdate1(cupInfo.getSdate1())
            .edate1(cupInfo.getEdate1())
            .sdate2(cupInfo.getSdate2())
            .edate2(cupInfo.getEdate2())
            .lyears(cupInfo.getLyears())
            .play_flag(cupInfo.getPlayFlag())
            .sub_team_count(cupInfo.getSubTeamCount())
            .main_team_count(cupInfo.getMainTeamCount())
            .group_count(cupInfo.getGroupCount())
            .team_count(cupInfo.getTeamCount())
            .m_group_count(cupInfo.getMGroupCount())
            .m_team_count(cupInfo.getMTeamCount())
            .files(cupInfo.getFiles())
            .build();

        List<CupTourMatchDto> cupTourEmptyMatchList = cupMapper.selectCupTourEmptyMatchList(cupParam);
        List<CupTourMatchDto> cupTourMatchList = cupMapper.selectCupTourMatchResultList(cupParam);

        CupTourMatchResponse cupResponse = CupTourMatchResponse.builder()
            .ageGroup(cupParam.getAgeGroup())
            .cupInfoMap(cupInfoMap)
            .cupTourEmptyMatchList(cupTourEmptyMatchList)
            .cupTourMatchList(cupTourMatchList)
            .cupRoundData(roundData)
            .build();

        return cupResponse;
    }

    public CupSearchMatchResponse getSearchMatch(Cup cupParam) {

        List<CupSearchDto> cupSearchMatchList = cupMapper.selectSearchCupMatch(cupParam);
        List<CupInfoListDto> cupInfoList = cupMapper.selectCupInfoList(cupParam);

        CupSearchMatchResponse cupResponse = CupSearchMatchResponse.builder()
            .ageGroup(cupParam.getAgeGroup())
            .cupSearchList(cupSearchMatchList)
            .cupInfoList(cupInfoList)
            .build();

        return cupResponse;
    }

    public CupPlayDataDto selectPlayDataCup(Map<String, Object> param) {
        return CupPlayDataDto.builder()
            .homeAwayMatchPlayData(cupMapper.selectPlayDataCup(param))
            .homeAwayMatchChangeData(cupMapper.selectChangeDataCup(param))
            .homeAwayOwnGoalData(cupMapper.selectOwnGoalDataCup(param))
            .homeAwayStaffData(cupMapper.selectStaffDataCup(param))
            .homeAwayStaffPenaltyData(cupMapper.selectStaffPenaltyDataCup(param))
            .build();
    }

}
