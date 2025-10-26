package kr.co.nextplayer.base.front.service;

import kr.co.nextplayer.base.board.mapper.BoardMapper;
import kr.co.nextplayer.base.board.mapper.EducationMapper;
import kr.co.nextplayer.base.cup.dto.cup.BannerCupListDto;
import kr.co.nextplayer.base.cup.dto.cup.CupInfoListDto;
import kr.co.nextplayer.base.cup.dto.cup.CupSubMatchDto;

import kr.co.nextplayer.base.cup.mapper.CupMapper;
import kr.co.nextplayer.base.cup.model.Cup;
import kr.co.nextplayer.base.cup.model.CupMatch;
import kr.co.nextplayer.base.front.mapper.HomeMapper;
import kr.co.nextplayer.base.front.mapper.UageMapper;
import kr.co.nextplayer.base.front.model.Uage;
import kr.co.nextplayer.base.front.response.HomeResponse;

import kr.co.nextplayer.base.front.response.HomeLeagueResponse;
import kr.co.nextplayer.base.front.response.HomeTodayResponse;
import kr.co.nextplayer.base.front.util.StrUtil;
import kr.co.nextplayer.base.league.dto.*;
import kr.co.nextplayer.base.league.mapper.LeagueMapper;
import kr.co.nextplayer.base.league.model.League;
import kr.co.nextplayer.base.media.dto.MediaDto;
import kr.co.nextplayer.base.media.dto.MediaReqDto;
import kr.co.nextplayer.base.media.mapper.MediaMapper;
import kr.co.nextplayer.base.media.model.Media;
import kr.co.nextplayer.base.store.mapper.StoreMapper;
import kr.co.nextplayer.base.team.dto.TeamDto;
import kr.co.nextplayer.base.team.mapper.TeamMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class HomeService {

    @Resource
    private UageMapper uageMapper;
    @Resource
    private CupMapper cupMapper;
    @Resource
    private LeagueMapper leagueMapper;
    @Resource
    private HomeMapper homeMapper;

    @Resource
    private MediaMapper mediaMapper;

    @Resource
    private TeamMapper teamMapper;

    @Resource
    private BoardMapper boardMapper;

    @Resource
    private StoreMapper storeMapper;

    @Resource
    private EducationMapper educationMapper;

    public HomeResponse getBannerProgressCupList(String ageGroup) {

        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();

        List<HashMap<String, Object>> uageList = uageMapper.selectUageList();

        List<BannerCupListDto> cupList = new ArrayList<BannerCupListDto>();

        List<BannerCupListDto> totalCupList = new ArrayList<BannerCupListDto>();

        String cupInfoTB = ageGroup + "_Cup_Info";

        Cup cupParam = Cup.builder()
            .cupInfoTB(cupInfoTB)
            .ageGroup(String.valueOf(ageGroup))
            .build();

        cupList = cupMapper.selectBannerCupList(cupParam);

        totalCupList.addAll(cupList);

        HomeResponse homeResponse = HomeResponse.builder()
            .uageList(uageList)
            .totalCupList(totalCupList)
            .build();

        return homeResponse;
    }

    public HomeResponse getBannerProgressLeagueList(String ageGroup) {

        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();

        List<HashMap<String, Object>> uageList = uageMapper.selectUageList();

        List<BannerLeagueListDto> leagueList = new ArrayList<BannerLeagueListDto>();

        List<BannerLeagueListDto> totalLeagueList = new ArrayList<BannerLeagueListDto>();

        String leagueInfoTB = ageGroup + "_League_Info";

        League leagueParam = League.builder()
            .leagueInfoTB(leagueInfoTB)
            .ageGroup(String.valueOf(ageGroup))
            .build();

        leagueList = leagueMapper.selectBannerLeagueList(leagueParam);

        totalLeagueList.addAll(leagueList);

        HomeResponse homeResponse = HomeResponse.builder()
            .uageList(uageList)
            .totalLeagueList(totalLeagueList)
            .build();

        return homeResponse;
    }

    public HomeTodayResponse getBannerTodayCupList(String ageGroup) {

        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();

        List<HashMap<String, Object>> uageList = uageMapper.selectUageList();

        List<BannerCupListDto> cupList = new ArrayList<BannerCupListDto>();
        List<CupInfoListDto> cupInfoList = new ArrayList<CupInfoListDto>();
        List<CupSubMatchDto> cupMatchList = new ArrayList<CupSubMatchDto>();

        List<BannerCupListDto> totalCupList = new ArrayList<BannerCupListDto>();
        List<CupInfoListDto> totalCupInfoList = new ArrayList<CupInfoListDto>();
        List<CupSubMatchDto> totalCupMatchList = new ArrayList<CupSubMatchDto>();

        String cupInfoTB = ageGroup + "_Cup_Info";
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";

        String cupSubMatchPlayDataTB = ageGroup + "_Cup_Sub_Match_Play_Data";
        String cupSubMatchChangeDataTB = ageGroup + "_Cup_Sub_Match_Change_Data";
        String cupMainMatchPlayDataTB = ageGroup + "_Cup_Main_Match_Play_Data";
        String cupMainMatchChangeDataTB = ageGroup + "_Cup_Main_Match_Change_Data";
        String cupTourMatchPlayDataTB = ageGroup + "_Cup_Tour_Match_Play_Data";
        String cupTourMatchChangeDataTB = ageGroup + "_Cup_Tour_Match_Change_Data";

        String cupSubStaffDataTB = ageGroup + "_Cup_Sub_Staff_Data";
        String cupSubStaffPenaltyDataTB = ageGroup + "_Cup_Sub_Staff_Penalty_Data";
        String cupSubOwnGoalDataTB = ageGroup + "_Cup_Sub_Own_Goal_Data";

        String cupMainStaffDataTB = ageGroup + "_Cup_Main_Staff_Data";
        String cupMainStaffPenaltyDataTB = ageGroup + "_Cup_Main_Staff_Penalty_Data";
        String cupMainOwnGoalDataTB = ageGroup + "_Cup_Main_Own_Goal_Data";

        String cupTourStaffDataTB = ageGroup + "_Cup_Tour_Staff_Data";
        String cupTourStaffPenaltyDataTB = ageGroup + "_Cup_Tour_Staff_Penalty_Data";
        String cupTourOwnGoalDataTB = ageGroup + "_Cup_Tour_Own_Goal_Data";

        Cup cupParam = Cup.builder()
            .cupInfoTB(cupInfoTB)
            .cupSubMatchTB(cupSubMatchTB)
            .cupMainMatchTB(cupMainMatchTB)
            .cupTourMatchTB(cupTourMatchTB)
            .ageGroup(String.valueOf(ageGroup))
            .build();

        CupMatch matchParam = CupMatch.builder()
            .ageGroup(ageGroup)
            .cupInfoTB(cupInfoTB)
            .cupSubMatchTB(cupSubMatchTB)
            .cupMainMatchTB(cupMainMatchTB)
            .cupTourMatchTB(cupTourMatchTB)
            .cupSubMatchPlayDataTB(cupSubMatchPlayDataTB)
            .cupSubMatchChangeDataTB(cupSubMatchChangeDataTB)
            .cupMainMatchPlayDataTB(cupMainMatchPlayDataTB)
            .cupMainMatchChangeDataTB(cupMainMatchChangeDataTB)
            .cupTourMatchPlayDataTB(cupTourMatchPlayDataTB)
            .cupTourMatchChangeDataTB(cupTourMatchChangeDataTB)
            .cupSubStaffDataTB(cupSubStaffDataTB)
            .cupSubStaffPenaltyDataTB(cupSubStaffPenaltyDataTB)
            .cupSubOwnGoalDataTB(cupSubOwnGoalDataTB)
            .cupMainStaffDataTB(cupMainStaffDataTB)
            .cupMainStaffPenaltyDataTB(cupMainStaffPenaltyDataTB)
            .cupMainOwnGoalDataTB(cupMainOwnGoalDataTB)
            .cupTourStaffDataTB(cupTourStaffDataTB)
            .cupTourStaffPenaltyDataTB(cupTourStaffPenaltyDataTB)
            .cupTourOwnGoalDataTB(cupTourOwnGoalDataTB)
            .build();

        cupList = cupMapper.selectBannerCupList(cupParam);
        cupInfoList = cupMapper.selectCupInfoList(cupParam);
        //cupMatchList = cupMapper.selectCupMatchList(matchParam);
        totalCupMatchList = cupMapper.selectCupMatchList(matchParam);

        totalCupList.addAll(cupList);
        totalCupInfoList.addAll(cupInfoList);
        //totalCupMatchList.addAll(cupMatchList);
        //totalCupMatchList.addAll(cupMatchList);

        HomeTodayResponse homeResponse = HomeTodayResponse.builder()
            .uageList(uageList)
            .totalCupList(totalCupList)
            .totalCupInfoList(totalCupInfoList)
            .totalCupMatchList(totalCupMatchList)
            .build();

        return homeResponse;
    }

    public HomeTodayResponse getBannerTodayLeagueList(String ageGroup) {

        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();

        List<HashMap<String, Object>> uageList = uageMapper.selectUageList();

        String leagueInfoTB = ageGroup + "_League_Info";
        String leagueMatchTB = ageGroup + "_League_Match";

        League leagueParam = League.builder()
            .leagueInfoTB(leagueInfoTB)
            .leagueMatchTB(leagueMatchTB)
            .ageGroup(String.valueOf(ageGroup))
            .build();

        List<BannerLeagueListDto> totalLeagueList = leagueMapper.selectBannerLeagueList(leagueParam);
        List<LeagueInfoDto> leagueInfoList = leagueMapper.selectLeagueInfoList(leagueParam);
        List<LeagueMatchDto> totalLeagueMatchList = leagueMapper.selectLeagueMatchList(leagueParam);

        HomeTodayResponse homeResponse = HomeTodayResponse.builder()
            .uageList(uageList)
            .totalLeagueList(totalLeagueList)
            .leagueInfoList(leagueInfoList)
            .totalLeagueMatchList(totalLeagueMatchList)
            .build();

        return homeResponse;
    }

    public HomeLeagueResponse getBannerLeagueList(String ageGroup) {

        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();

        List<HashMap<String, Object>> uageList = uageMapper.selectUageList();


        List<LeagueWinnerListDto> leagueWinnerList = new ArrayList<LeagueWinnerListDto>();
        List<LeagueWinnerListDto> totalleagueWinnerList = new ArrayList<LeagueWinnerListDto>();


        String leagueInfoTB = ageGroup + "_League_Info";
        String leagueResultTB = ageGroup + "_League_Result";

        League leagueparam = League.builder()
            .ageGroup(String.valueOf(ageGroup))
            .leagueInfoTB(leagueInfoTB)
            .leagueResultTB(leagueResultTB)
            .build();

        leagueWinnerList = leagueMapper.selectLeagueWinnerList(leagueparam);

        totalleagueWinnerList.addAll(leagueWinnerList);


        HomeLeagueResponse homeResponse = HomeLeagueResponse.builder()
            .uageList(uageList)
            .totalLeagueWinnerList(totalleagueWinnerList)
            .build();

        return homeResponse;
    }

    public Map getVisitorsHistoryCount(String ip, String userAgent){
        int myCount = homeMapper.selectCountVisitorsIpHistory(ip);
        if(myCount == 0){
            homeMapper.insertVisitorsHistory(ip);
            homeMapper.insertAccessInfo(userAgent);
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("total", homeMapper.selectCountVisitorsTotal());
        map.put("today", homeMapper.selectCountVisitorsToday());

        return map;
    }

    public Map getVisitorsHistoryCountMinute(String ip){
        int myCount = homeMapper.selectCountVisitorsIpHistoryMinute(ip);
        if(myCount == 0){
            homeMapper.insertVisitorsHistoryMinute(ip);
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("total", homeMapper.selectCountVisitorsTotalMinute());
        map.put("today", homeMapper.selectCountVisitorsTodayMinute());

        return map;
    }

    public Map getHomeMediaData() {
        //List<HashMap<String, Object>> videoData = homeMapper.selectHomeVideoData();
        //List<HashMap<String, Object>> newsData = homeMapper.selectHomeMediaData("News");
        //List<HashMap<String, Object>> blogData = homeMapper.selectHomeMediaData("Blog");

        HashMap<String, String> params = new HashMap<>();
        params.put("mediaType", "Video");
        List<HashMap<String, Object>> videoData = mediaMapper.selectHomeMediaList(params);

        params.put("mediaType", "News");
        List<HashMap<String, Object>> newsData = mediaMapper.selectHomeMediaList(params);

        params.put("mediaType", "Blog");
        List<HashMap<String, Object>> blogData = mediaMapper.selectHomeMediaList(params);

        Map<String, Object> result = new HashMap<>();
        result.put("videoData", videoData);
        result.put("newsData", newsData);
        result.put("blogData", blogData);

        return result;
    }

    public Map getHomeInterestAge() {

        Map<String, Object> resultMap = uageMapper.selectInterestAge();
        Map<String, Object> result = new HashMap<>();
        result.put("interestAge", resultMap.get("uage"));
        return result;
    }

    public Map getHomeGameMediaData(String ageGroup) {

        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }

        String cupInfoTB = ageGroup + "_Cup_Info";
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        String cupSubMatchPlayDataTB = ageGroup + "_cup_sub_match_play_data";
        String cupSubMatchChangeDataTB = ageGroup + "_cup_sub_match_change_data";

        String cupMainMatchPlayDataTB = ageGroup + "_cup_sub_match_play_data";
        String cupMainMatchChangeDataTB = ageGroup + "_cup_sub_match_change_data";

        String cupTourMatchPlayDataTB = ageGroup + "_cup_tour_match_play_data";
        String cupTourMatchChangeDataTB = ageGroup + "_cup_tour_match_change_data";

        String cupSubStaffDataTB = ageGroup + "_Cup_Sub_Staff_Data";
        String cupSubStaffPenaltyDataTB = ageGroup + "_Cup_Sub_Staff_Penalty_Data";
        String cupSubOwnGoalDataTB = ageGroup + "_Cup_Sub_Own_Goal_Data";

        String cupMainStaffDataTB = ageGroup + "_Cup_Main_Staff_Data";
        String cupMainStaffPenaltyDataTB = ageGroup + "_Cup_Main_Staff_Penalty_Data";
        String cupMainOwnGoalDataTB = ageGroup + "_Cup_Main_Own_Goal_Data";

        String cupTourStaffDataTB = ageGroup + "_Cup_Tour_Staff_Data";
        String cupTourStaffPenaltyDataTB = ageGroup + "_Cup_Tour_Staff_Penalty_Data";
        String cupTourOwnGoalDataTB = ageGroup + "_Cup_Tour_Own_Goal_Data";

        Cup cupParam = new Cup();
        cupParam.setCupInfoTB(cupInfoTB);
        cupParam.setCupSubMatchTB(cupSubMatchTB);
        cupParam.setCupMainMatchTB(cupMainMatchTB);
        cupParam.setCupTourMatchTB(cupTourMatchTB);
        cupParam.setCupSubMatchPlayDataTB(cupSubMatchPlayDataTB);
        cupParam.setCupSubMatchChangeDataTB(cupSubMatchChangeDataTB);
        cupParam.setCupMainMatchPlayDataTB(cupMainMatchPlayDataTB);
        cupParam.setCupMainMatchChangeDataTB(cupMainMatchChangeDataTB);
        cupParam.setCupTourMatchPlayDataTB(cupTourMatchPlayDataTB);
        cupParam.setCupTourMatchChangeDataTB(cupTourMatchChangeDataTB);

        cupParam.setCupSubStaffDataTB(cupSubStaffDataTB);
        cupParam.setCupSubStaffPenaltyDataTB(cupSubStaffPenaltyDataTB);
        cupParam.setCupSubOwnGoalDataTB(cupSubOwnGoalDataTB);

        cupParam.setCupMainStaffDataTB(cupMainStaffDataTB);
        cupParam.setCupMainStaffPenaltyDataTB(cupMainStaffPenaltyDataTB);
        cupParam.setCupMainOwnGoalDataTB(cupMainOwnGoalDataTB);

        cupParam.setCupTourStaffDataTB(cupTourStaffDataTB);
        cupParam.setCupTourStaffPenaltyDataTB(cupTourStaffPenaltyDataTB);
        cupParam.setCupTourOwnGoalDataTB(cupTourOwnGoalDataTB);

        List<HashMap<String, Object>> todayMatchData = cupMapper.selectCupTodayMatchList(cupParam);

        List<Media> mediaList = mediaMapper.selectMainGameData(ageGroup);

        List<MediaDto> resultList = new ArrayList<MediaDto>();
        if (mediaList.size() > 0) {
            for (Media media : mediaList) {
                List<Media> childList = mediaMapper.selectMediaChildList(media.getMediaId());

                List<HashMap<String, Object>> cup_tag = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> league_tag = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> team_tag = new ArrayList<HashMap<String, Object>>();

                List<HashMap<String, Object>> cup_match = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> league_match = new ArrayList<HashMap<String, Object>>();

                if (childList.size() > 0) {
                    for (Media child : childList) {

                        String childTB = "";
                        String childId = "";
                        String childKey = "";

                        if (child.getParentTable().contains("Cup")) {

                            childTB = !StrUtil.isEmpty(child.getChildTable()) ? child.getChildTable() : "";

                            childId = !StrUtil.isEmpty(child.getChildId()) ?  "(" + child.getChildId() + ")" : "";

                            if (childTB.contains("Cup_Sub")) {
                                childKey = "a.cup_sub_match_id";
                            } else if (childTB.contains("Cup_Main")) {
                                childKey = "a.cup_main_match_id";
                            } else if (childTB.contains("Cup_Tour")) {
                                childKey = "a.cup_tour_match_id";
                            }

                            Cup cup = Cup.builder()
                                .parentTB(child.getParentTable())
                                .parentId(child.getParentId())
                                .childTB(childTB)
                                .childId(childId)
                                .childKey(childKey)
                                .build();

                            cup_tag = cupMapper.selectCupInfoListForMedia(cup);

                            if (!StrUtil.isEmpty(childTB)) {
                                cup_match = cupMapper.selectCupMatchListForMedia(cup);
                            }
                        }

                        if (child.getParentTable().contains("League")) {

                            childTB = !StrUtil.isEmpty(child.getChildTable()) ? child.getChildTable() : "";

                            childId = !StrUtil.isEmpty(child.getChildId()) ?  "(" + child.getChildId() + ")" : "";

                            childKey = "a.league_match_id";

                            League league = League.builder()
                                .parentTB(child.getParentTable())
                                .parentId(child.getParentId())
                                .childTB(childTB)
                                .childId(childId)
                                .childKey(childKey)
                                .build();

                            league_tag = leagueMapper.selectLeagueInfoListForMedia(league);

                            if (!StrUtil.isEmpty(childTB)) {
                                league_match = leagueMapper.selectLeagueMatchListForMedia(league);
                            }
                        }

                        if (child.getParentTable().contains("Team")) {
                            TeamDto teamDto = TeamDto.builder()
                                .childId("(" + child.getChildId() + ")")
                                .build();
                            team_tag = teamMapper.selectTeamListForMedia(teamDto);
                        }

                    }
                }
                MediaDto mediaDto = MediaDto.builder()
                    .media_id(media.getMediaId())
                    .title(media.getTitle())
                    .content(media.getContent())
                    .source(media.getSource())
                    .summary(media.getSummary())
                    .url_link(media.getUrlLink())
                    .img_url(media.getImgUrl())
                    .media_type(media.getMediaType())
                    .type(media.getType())
                    .submit_date(media.getSubmitDate())
                    .cup_tag(cup_tag)
                    .league_tag(league_tag)
                    .team_tag(team_tag)
                    .video_type((media.getVideoType()))
                    .cup_match(cup_match)
                    .league_match(league_match)
                    .video_type((media.getVideoType()))
                    .creator(media.getCreator())
                    .showFlag(media.getShowFlag())
                    .mediaOrder(media.getMediaOrder())
                    .build();
                resultList.add(mediaDto);
            }

        }

        Map<String, Object> result = new HashMap<>();
        result.put("todayMatchData", todayMatchData);
        result.put("gameMediaData", resultList);

        return result;
    }

    public int updateViewCnt(String type, String pk) {
        int result = 0;

        Map<String, String> param = new HashMap<String, String>();
        param.put("type", type);
        param.put("pk", pk);

        result = boardMapper.updateViewCnt(param);

        return result;
    }

    public Map<String, Object> getUageList() {
        List<HashMap<String, Object>> uageList = uageMapper.selectUageList();
        Map<String, Object> result = new HashMap<>();
        List<HashMap<String, Object>> cupUageList = new ArrayList<>();
        List<HashMap<String, Object>> leagueUageList = new ArrayList<>();

        for (HashMap<String, Object> map: uageList) {
            int cupCnt = cupMapper.selectIsTodayMatch(map);
            int leagueCnt = leagueMapper.selectIsTodayMatch(map);

            if (cupCnt > 0) {
                map.put("isTodayMatch", "1");
            } else {
                map.put("isTodayMatch", "0");
            }
            HashMap<String, Object> cupMap = new HashMap<String, Object>();
            cupMap.putAll(map);
            cupUageList.add(cupMap);

            map.remove("isTodayMatch");

            if (leagueCnt > 0) {
                map.put("isTodayMatch", "1");
            } else {
                map.put("isTodayMatch", "0");
            }

            HashMap<String, Object> leagueMap = new HashMap<String, Object>();
            leagueMap.putAll(map);
            leagueUageList.add(leagueMap);
        }

        result.put("cupUage", cupUageList);
        result.put("leagueUage", leagueUageList);
        return result;
    }

    public Map getHomeColumnData() {

        HashMap<String, String> params = new HashMap<>();
        params.put("mediaType", "Video");
        List<HashMap<String, Object>> columnData = educationMapper.selectHomeColumnList(params);

        Map<String, Object> result = new HashMap<>();
        result.put("columnData", columnData);

        return result;
    }

    public void insertPushClick(String path) {
        homeMapper.insertPushClick(path);
    }

}
