package kr.co.nextplayer.base.backend.controller;

import kr.co.nextplayer.base.backend.dto.TeamDto;
import kr.co.nextplayer.base.backend.dto.UageDto;
import kr.co.nextplayer.base.backend.model.Team;
import kr.co.nextplayer.base.backend.model.Uage;
import kr.co.nextplayer.base.backend.service.*;
import kr.co.nextplayer.base.backend.util.DateUtil;
import kr.co.nextplayer.base.backend.util.DateUtils;
import kr.co.nextplayer.base.backend.util.Define;
import kr.co.nextplayer.base.backend.util.StrUtil;
import org.apache.xpath.operations.Bool;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static kr.co.nextplayer.base.backend.util.DateUtil.YYYYMMDDHHMM;

@Controller
public class TeamController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private TeamService teamService;

    @Resource
    private CupService cupService;

    @Resource
    private LeagueService leagueService;

    @Resource
    private MatchService matchService;

    @Resource
    private UageService uageService;

    @RequestMapping(value="/team")
    public String team(@RequestParam Map<String, String> params, Model model, TeamDto teamDto, HttpServletResponse resp) {
        logger.info("team was called. teamDto:"+teamDto);
        logger.info("team was called. params:"+params);

        //연령대
        String ageGroup = params.get("ageGroup");
        if(StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        //검색
        String sArea = params.get("sArea");
        String sTeamType = params.get("sTeamType");
        String sNickName = params.get("sNickName");
        String useFlag = params.get("useFlag");

        /*String sArea = teamDto.getSArea();
        String sTeamType = teamDto.getSTeamType();
        String sNickName = teamDto.getSNickName();*/

        model.addAttribute("sArea", sArea);
        model.addAttribute("sTeamType", sTeamType);
        model.addAttribute("sNickName", sNickName);
        model.addAttribute("useFlag", useFlag);

        /* 페이징 처리 ------------------------------------------------------------------------------------------------*/
        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; //현재페이지 갯수

        HashMap<String, Object> teamCountMap = teamService.selectTeamListCount(params);
        long totalCount = (long) (Long) teamCountMap.get("totalCount");

        int tp = 1;
        if (totalCount > 0) {
            tp = (int)totalCount / cpp;
            if ((totalCount % cpp) > 0) {
                tp += 1;
            }
        }

        int sRow = (cp -1) * cpp;

        params.put("sRow", "" + sRow);
        params.put("eCount", "" + cpp);

        teamDto.setSRow(sRow);
        teamDto.setECount(cpp);

        HashMap<String, Object> map = StrUtil.calcPage(cp, totalCount, Define.COUNT_PAGE);
        logger.info(" ------ [team list paging]  totalCount : " +totalCount + ", tp :"+ tp+", cp:"+cp+", start : "+ map.get("start")+", end : "+map.get("end"));

        model.addAttribute("start", map.get("start"));
        model.addAttribute("end",  map.get("end"));
        model.addAttribute("prev",  map.get("prev"));
        model.addAttribute("next",  map.get("next"));

        model.addAttribute("cp", cp); //현재페이지번호
        model.addAttribute("cpp", cpp); //현재페이지 갯수
        model.addAttribute("tp", tp); //총 페이지 번호
        model.addAttribute("tc", totalCount); //총 리스트 갯수
        /* 페이징 처리 ------------------------------------------------------------------------------------------------*/

        //학원.클럽.유스 리스트 getting team list
        List<HashMap<String, Object>> teamList = teamService.selectTeamList(params);
        logger.info("teamList > " + teamList);
        //연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();


        UageDto uageDto = UageDto.builder()
            .ageGroup(teamDto.getAgeGroup())
            .areaSearch(teamDto.getAreaSearch())
            .areaId(teamDto.getAreaId())
            .build();
        logger.info("uageDto > " + uageDto.getAreaSearch());
        List<HashMap<String, Object>> areaList = uageService.selectAreaList(params);

        model.addAttribute("teamList", teamList);
        model.addAttribute("uageList", uageList);
        model.addAttribute("areaList", areaList);

        model.addAttribute("ageGroup", ageGroup);

		String webPath = StrUtil.getWebPath();
		model.addAttribute("webPath", webPath);

        return "team/team";
    }

    /*
     * 확원/클럽 - 등록.수정.삭제
     */
    @RequestMapping(value = "/save_team", method = {RequestMethod.GET, RequestMethod.POST})
    public String save_team(MultipartHttpServletRequest request, @RequestParam Map<String, String> params, TeamDto teamDto, Model model, HttpServletResponse resp, RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_team params : "+ params);
        String sFlag = teamDto.getSFlag();
        String cp = teamDto.getCp();

        //연령대
        String ageGroup = teamDto.getAgeGroup();
        if(StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }
//		model.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("ageGroup", ageGroup);

        String fileName = "emblem";

        teamDto.setSFlag(sFlag);
        teamDto.setCp(cp);

        if(sFlag.equals(Define.MODE_ADD)){ //등록
            teamService.insertTeam(params);
            String teamId = String.valueOf(params.get("teamId"));
            System.out.println("---- params : "+ params);
            System.out.println("--- teamId : "+ teamId);

            //엠블럼 이미지 업로드
            String emblemFileParam = StrUtil.contentUpload(fileName, ageGroup, teamId, request);
            teamDto.setEmblemFileParam(emblemFileParam);
            System.out.println("---- emblemFile : "+ emblemFileParam);
            if(!StrUtil.isEmpty(emblemFileParam)){
                params.put("emblemFlag", "1");
                params.put("emblemFileParam", emblemFileParam);
                teamService.updateTeam(params);
            }

//			recordLog(Define.STR_SUCCESS,"[학원.클럽.유스] 등록-성공 -params:"+params.toString()); //접속하여 행위한 정보에대한 기록

        }else if(sFlag.equals(Define.MODE_FIX)) {//수정
            String teamId = params.get("teamId");
            //엠블럼 이미지 업로드
            String emblemFile = StrUtil.contentUpload(fileName, ageGroup, teamId, request);
            //teamDto.setEmblemFile(emblemFile);
            System.out.println("---- emblemFile : "+ emblemFile);
            if(StrUtil.isEmpty(emblemFile)){
                String imgFilePath = (String) params.get("imgFilePath");
                params.put("emblemFileParam", imgFilePath);
                teamDto.setEmblemFileParam(imgFilePath);
            }else{
                params.put("emblemFileParam", emblemFile);
                teamDto.setEmblemFileParam(emblemFile);
            }
            String pName = params.get("pIdName");
            if (StrUtil.isEmpty(pName)) {
            	params.put("pId", "0");
            }
            teamService.updateTeam(params);

            redirectAttributes.addAttribute("cp", cp);

        }else if(sFlag.equals(Define.MODE_DELETE)) {//삭제
            //sqlSessionInst.update("udtTeam", params);
            teamService.deleteTeam(params); //타겟 테이블 데이터 전체 삭제

            //파일 삭제
            String emblem = params.get("emblem");
            if(!StrUtil.isEmpty(emblem)) {
                String filePath = StrUtil.getContentPath();
                StrUtil.deleteFile(filePath+emblem);
            }

        }else if(sFlag.equals(Define.MODE_DATA_RESET)) {//연령별 전체삭제
            teamService.deleteTeam(params); //타겟 테이블 데이터 전체 삭제
            //파일 삭제
            String filePath = StrUtil.getContentPath();
            StrUtil.deleteDirectory(new File(filePath+"/emblem/"+ageGroup+"/"));
        }


        return "redirect:/team";
    }

    //학원/클럽
    @RequestMapping(value="/teamMgr")
    public String teamMgr(@RequestParam Map<String, String> params, TeamDto teamDto, Model model, HttpServletResponse resp) {
        logger.info("teamMgr was called. params:"+params);

        //연령대
        String ageGroup = params.get("ageGroup");
        if(StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);
        //대회 결과 연령별 테이블
        String cupResultTB = ageGroup + "_Cup_Result";
        params.put("cupResultTB", cupResultTB);

        // 연령별 테이블
        String leagueInfoTB = ageGroup + "_League_Info";
        params.put("leagueInfoTB", leagueInfoTB);
        //대회 결과 연령별 테이블
        String leagueResultTB = ageGroup + "_League_Result";
        params.put("leagueResultTB", leagueResultTB);


        //검색
        String sArea = params.get("sArea");
        String sTeamType = params.get("sTeamType");
        String sNickName = params.get("sNickName");
        String sYear = params.get("sYear");
        String useFlag = params.get("useFlag");
        String includeFlag = params.get("includeFlag");

        model.addAttribute("sArea", sArea);
        model.addAttribute("sTeamType", sTeamType);
        model.addAttribute("sNickName", sNickName);
        model.addAttribute("sYear", sYear);
        model.addAttribute("useFlag", useFlag);
        model.addAttribute("includeFlag", includeFlag);

        //정렬
        String order = params.get("order");
        String orderName = params.get("orderName");
        model.addAttribute("order", order);
        model.addAttribute("orderName", orderName);

        /* 페이징 처리 ------------------------------------------------------------------------------------------------*/
        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; //현재페이지 갯수

        HashMap<String, Object> teamCountMap = teamService.selectTeamMgrListCount(params);
        long totalCount = (long) (Long) teamCountMap.get("totalCount");

        int tp = 1;
        if (totalCount > 0) {
            tp = (int)totalCount / cpp;
            if ((totalCount % cpp) > 0) {
                tp += 1;
            }
        }

        int sRow = (cp -1) * cpp;

        params.put("sRow", "" + sRow);
        params.put("eCount", "" + cpp);

        HashMap<String, Object> map = StrUtil.calcPage(cp, totalCount, Define.COUNT_PAGE);
        logger.info(" ------ [team list paging]  totalCount : " +totalCount + ", tp :"+ tp+", cp:"+cp+", start : "+ map.get("start")+", end : "+map.get("end"));

        model.addAttribute("start", map.get("start"));
        model.addAttribute("end",  map.get("end"));
        model.addAttribute("prev",  map.get("prev"));
        model.addAttribute("next",  map.get("next"));

        model.addAttribute("cp", cp); //현재페이지번호
        model.addAttribute("cpp", cpp); //현재페이지 갯수
        model.addAttribute("tp", tp); //총 페이지 번호
        model.addAttribute("tc", totalCount); //총 리스트 갯수
        /* 페이징 처리 ------------------------------------------------------------------------------------------------*/


        //학원.클럽.유스 리스트 getting team list
        List<HashMap<String, Object>> teamList = teamService.selectTeamList(params);

        //연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();


        UageDto uageDto = UageDto.builder()
            .ageGroup(teamDto.getAgeGroup())
            .areaSearch(teamDto.getAreaSearch())
            .areaId(teamDto.getAreaId())
            .build();

        // 지역정보
        List<HashMap<String, Object>> areaList = uageService.selectAreaList(params);

        //팀정보 + 리그/대회 요약정보 리스트
        List<HashMap<String, Object>> teamMgrList = teamService.selectTeamMgrList(params);
        model.addAttribute("teamMgrList", teamMgrList);

        model.addAttribute("teamList", teamList);
        model.addAttribute("uageList", uageList);
        model.addAttribute("areaList", areaList);

        model.addAttribute("ageGroup", ageGroup);

//		String webPath = StrUtil.getWebPath();
//		model.addAttribute("webPath", webPath);

        return "team/teamMgr";
    }

    //학원/클럽 상세 - 요약정보
    @RequestMapping(value="/teamMgrDet")
    public String teamMgrDet(@RequestParam Map<String, String> params, TeamDto teamDto, Model model, HttpServletResponse resp) {
        logger.info("teamMgrDet was called. params:"+params);

        String cp = params.get("cp");
        model.addAttribute("cp", cp);

        //연령대
        String ageGroup = params.get("ageGroup");
        if(StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        // 대회 예선 경기일정 정보 연령별 테이블
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);
        // 대회 본선 경기일정 정보 연령별 테이블
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        params.put("cupMainMatchTB", cupMainMatchTB);
        // 대회 토너먼트(knockout stage) 경기일정 정보 연령별 테이블
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        params.put("cupTourMatchTB", cupTourMatchTB);
        //대회 결과 연령별 테이블
        String cupResultTB = ageGroup + "_Cup_Result";
        params.put("cupResultTB", cupResultTB);

        // 연령별 테이블
        String leagueInfoTB = ageGroup + "_League_Info";
        params.put("leagueInfoTB", leagueInfoTB);
        String leagueTeamTB = ageGroup + "_League_Team";
        params.put("leagueTeamTB", leagueTeamTB);
        String leagueMatchTB = ageGroup + "_League_Match";
        params.put("leagueMatchTB", leagueMatchTB);
        //대회 결과 연령별 테이블
        String leagueResultTB = ageGroup + "_League_Result";
        params.put("leagueResultTB", leagueResultTB);


        //검색
        String sArea = params.get("sArea");
        String sTeamType = params.get("sTeamType");
        String sNickName = params.get("sNickName");
        String sYear = params.get("sYear");

        model.addAttribute("sArea", sArea);
        model.addAttribute("sTeamType", sTeamType);
        model.addAttribute("sNickName", sNickName);
        model.addAttribute("sYear", sYear);

        //학원클럽 - 팀정보 상세
        HashMap<String, Object> teamInfoMap = teamService.selectTeamInfo(params);
        model.addAttribute("teamInfoMap", teamInfoMap);

        //학원클럽 - 해당 연도 대회성적
        List<HashMap<String, Object>> teamCupResultList = cupService.selectTeamCupResult(params);
        //model.addAttribute("teamCupResultList", teamCupResultList);

        //학원클럽 - 해당 연도 리그성적
        List<HashMap<String, Object>> teamLeagueEntertList = leagueService.selectTeamEnterLeague(params);
        //model.addAttribute("teamLeagueEntertList", teamLeagueEntertList);

        //해당 연도 전체, 대회, 리그, 승무패
        HashMap<String, Object> teamMatchResultMap = matchService.selectTeamMatchResult(params);
        //model.addAttribute("teamMatchResultMap", teamMatchResultMap);


        //해당 연도 리그 경기 결과
        List<HashMap<String, Object>> teamLeagueMatchList = leagueService.selectTeamLeagueMatch(params);
        //model.addAttribute("teamLeagueMatchList", teamLeagueMatchList);
        //해당 연도 대회 경기 결과
        List<HashMap<String, Object>> teamCupMatchList = cupService.selectTeamCupMatch(params);
        //model.addAttribute("teamCupMatchList", teamCupMatchList);
        //해당 연도 리그+대회 경기 결과
        List<HashMap<String, Object>> teamTotalMatchList = matchService.selectTeamTotalMatch(params);
        //model.addAttribute("teamTotalMatchList", teamTotalMatchList);

        //연도별 평균 득/실점
        List<HashMap<String, Object>> teamAvgGoalList = teamService.selectTeamAvgGoal(params);
        //model.addAttribute("teamAvgGoalList", teamAvgGoalList);
        //System.out.println(teamAvgGoalList.toString());

        String pId = String.valueOf(teamInfoMap.get("pId"));

        // 부모팀 요약정보 - 전체, 대회, 리그 승무패 데이터
        HashMap<String, Object> pTeamMatchResultMap = new HashMap<String, Object>();

        Boolean isAvgDataDuplicate = false;

        //해당클럽 리스트가 비어있을 경우
        //부모팀의 리스트를 가져옴
        if(!pId.equals("0")) {

            //System.out.println("====== pId:"+pId);

            if(!pId.equals("0")) {
                params.put("teamId", pId);
            }

            List<HashMap<String, Object>> pTeamAvgGoalList = teamService.selectTeamAvgGoal(params);
            if (pTeamAvgGoalList.size() > 0) {
                isAvgDataDuplicate = true;
                teamAvgGoalList.addAll(pTeamAvgGoalList);
                teamAvgGoalList = teamAvgGoalList.stream().sorted(Comparator.comparingInt(map -> Integer.parseInt(map.get("play_year").toString())))
                    .collect(Collectors.toList());
            }


            //학원클럽 - 해당 연도 대회성적
            //teamCupResultList = cupService.selectTeamCupResult(params);
            //model.addAttribute("teamCupResultList", teamCupResultList);
            teamCupResultList.addAll(cupService.selectTeamCupResult(params));

            //학원클럽 - 해당 연도 리그성적
            //teamLeagueEntertList = leagueService.selectTeamEnterLeague(params);
            //model.addAttribute("teamLeagueEntertList", teamLeagueEntertList);
            teamLeagueEntertList.addAll(leagueService.selectTeamEnterLeague(params));

            //해당 연도 전체, 대회, 리그, 승무패
            //teamMatchResultMap = matchService.selectTeamMatchResult(params);
            //model.addAttribute("teamMatchResultMap", teamMatchResultMap);
            pTeamMatchResultMap = matchService.selectTeamMatchResult(params);

            //해당 연도 리그 경기 결과
            //teamLeagueMatchList = leagueService.selectTeamLeagueMatch(params);
            //model.addAttribute("teamLeagueMatchList", teamLeagueMatchList);
            teamLeagueMatchList.addAll(leagueService.selectTeamLeagueMatch(params));


            //해당 연도 대회 경기 결과
            //teamCupMatchList = cupService.selectTeamCupMatch(params);
            //model.addAttribute("teamCupMatchList", teamCupMatchList);
            teamCupMatchList.addAll(cupService.selectTeamCupMatch(params));


            //해당 연도 리그+대회 경기 결과
            //teamTotalMatchList = matchService.selectTeamTotalMatch(params);
            //model.addAttribute("teamTotalMatchList", teamTotalMatchList);
            teamTotalMatchList.addAll(matchService.selectTeamTotalMatch(params));

            //연도별 평균 득/실점
//			teamAvgGoalList = sqlSession.selectList("selTeamAvgGoal", params);
//			model.addAttribute("teamAvgGoalList", teamAvgGoalList);

        }

        model.addAttribute("teamCupResultList", teamCupResultList);
        model.addAttribute("teamLeagueEntertList", teamLeagueEntertList);
        model.addAttribute("teamMatchResultMap", teamMatchResultMap);
        model.addAttribute("teamLeagueMatchList", teamLeagueMatchList);
        model.addAttribute("teamCupMatchList", teamCupMatchList);
        model.addAttribute("teamTotalMatchList", teamTotalMatchList);
        model.addAttribute("teamAvgGoalList", teamAvgGoalList);

        model.addAttribute("isAvgDataDuplicate", isAvgDataDuplicate);

        //연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();


        UageDto uageDto = UageDto.builder()
            .ageGroup(teamDto.getAgeGroup())
            .areaSearch(teamDto.getAreaSearch())
            .areaId(teamDto.getAreaId())
            .build();

        // 지역정보
        List<HashMap<String, Object>> areaList = uageService.selectAreaList(params);

        model.addAttribute("uageList", uageList);
        model.addAttribute("areaList", areaList);

        model.addAttribute("ageGroup", ageGroup);

//		String webPath = StrUtil.getWebPath();
//		model.addAttribute("webPath", webPath);

        return "team/teamMgrDet";
    }

    //학원/클럽 상세 - 대회정보
    @RequestMapping(value="/teamMgrDetCup")
    public String teamMgrDetCup(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("teamMgrDetCup was called. params:"+params);

        String cp = params.get("cp");
        model.addAttribute("cp", cp);

        //연령대
        String ageGroup = params.get("ageGroup");
        if(StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        // 대회 예선 경기일정 정보 연령별 테이블
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);
        // 대회 본선 경기일정 정보 연령별 테이블
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        params.put("cupMainMatchTB", cupMainMatchTB);
        // 대회 토너먼트(knockout stage) 경기일정 정보 연령별 테이블
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        params.put("cupTourMatchTB", cupTourMatchTB);
        //대회 결과 연령별 테이블
        String cupResultTB = ageGroup + "_Cup_Result";
        params.put("cupResultTB", cupResultTB);

        // 연령별 테이블
        String leagueInfoTB = ageGroup + "_League_Info";
        params.put("leagueInfoTB", leagueInfoTB);
        String leagueTeamTB = ageGroup + "_League_Team";
        params.put("leagueTeamTB", leagueTeamTB);
        String leagueMatchTB = ageGroup + "_League_Match";
        params.put("leagueMatchTB", leagueMatchTB);
        //대회 결과 연령별 테이블
        String leagueResultTB = ageGroup + "_League_Result";
        params.put("leagueResultTB", leagueResultTB);


        //검색
        String sArea = params.get("sArea");
        String sTeamType = params.get("sTeamType");
        String sNickName = params.get("sNickName");
        String sYear = params.get("sYear");

        model.addAttribute("sArea", sArea);
        model.addAttribute("sTeamType", sTeamType);
        model.addAttribute("sNickName", sNickName);
        model.addAttribute("sYear", sYear);

        //학원클럽 - 팀정보 상세
        HashMap<String, Object> teamInfoMap = teamService.selectTeamInfo(params);
        model.addAttribute("teamInfoMap", teamInfoMap);


        //연도별 대회 경기 수
        List<HashMap<String, Object>> teamCupPlayedList = cupService.selectTeamCupPlayedByYear(params);
        //model.addAttribute("teamCupPlayedList", teamCupPlayedList);

        //해당 연도 대회별 경기 결과
        List<HashMap<String, Object>> teamCupMatchList = cupService.selectTeamCupMatchByDetCup(params);
        //model.addAttribute("teamCupMatchList", teamCupMatchList);

        //해당 연도 대회리그 평균득실점
        HashMap<String, Object> teamCupAvgGoalMap = matchService.selectTeamCupAvgGoal(params);
        //model.addAttribute("teamCupAvgGoalMap", teamCupAvgGoalMap);

        //해당 연도 대회리그 평균득실점
        List<HashMap<String, Object>> teamCupAvgGoalList = cupService.selectTeamCupAvgGoalByYear(params);
        //model.addAttribute("teamCupAvgGoalList", teamCupAvgGoalList);


        String pId = String.valueOf(teamInfoMap.get("pId"));

        Boolean isCupPlayedDuplicate = false;
        Boolean isCupAvgGoalDuplicate = false;

        //해당클럽 리스트가 비어있을 경우
        //부모팀의 리스트를 가져옴
        if(!pId.equals("0")) {
            System.out.println("====== No List");

            if(!pId.equals("0")) {
                params.put("teamId", pId);
            }

            //연도별 대회 경기 수
            List<HashMap<String, Object>> pTeamCupPlayedList = cupService.selectTeamCupPlayedByYear(params);
            if (pTeamCupPlayedList.size() > 0) {
                isCupPlayedDuplicate = true;
                teamCupPlayedList.addAll(pTeamCupPlayedList);
                teamCupPlayedList = teamCupPlayedList.stream().sorted(Comparator.comparingInt(map -> Integer.parseInt(map.get("play_year").toString())))
                    .collect(Collectors.toList());
            }


            //해당 연도 대회별 경기 결과
            if (teamCupMatchList.size() == 0) {
                teamCupMatchList = cupService.selectTeamCupMatchByDetCup(params);
            }

            //해당 연도 대회리그 평균득실점
            if (teamCupAvgGoalMap.get("team_id") == null) {
                teamCupAvgGoalMap = matchService.selectTeamCupAvgGoal(params);
            }

            //해당 연도 대회리그 평균득실점
            List<HashMap<String, Object>> pTeamCupAvgGoalList = cupService.selectTeamCupAvgGoalByYear(params);
            if (pTeamCupAvgGoalList.size() > 0) {
                isCupAvgGoalDuplicate = true;
                teamCupAvgGoalList.addAll(pTeamCupAvgGoalList);
                teamCupAvgGoalList = teamCupAvgGoalList.stream().sorted(Comparator.comparingInt(map -> Integer.parseInt(map.get("play_year").toString())))
                    .collect(Collectors.toList());
            }
//			teamCupAvgGoalList = sqlSession.selectList("selTeamCupAvgGoalByYear", params);
//			model.addAttribute("teamCupAvgGoalList", teamCupAvgGoalList);

        }

        model.addAttribute("teamCupPlayedList", teamCupPlayedList);
        model.addAttribute("teamCupMatchList", teamCupMatchList);
        model.addAttribute("teamCupAvgGoalMap", teamCupAvgGoalMap);
        model.addAttribute("teamCupAvgGoalList", teamCupAvgGoalList);

        //연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();


        UageDto uageDto = UageDto.builder()
            .ageGroup(params.get("ageGroup"))
            .areaSearch(params.get("areaSearch"))
            .areaId(params.get("areaId"))
            .build();

        // 지역정보
        List<HashMap<String, Object>> areaList = uageService.selectAreaList(params);

        model.addAttribute("uageList", uageList);
        model.addAttribute("areaList", areaList);

        model.addAttribute("ageGroup", ageGroup);

        model.addAttribute("isCupPlayedDuplicate", isCupPlayedDuplicate);
        model.addAttribute("isCupAvgGoalDuplicate", isCupAvgGoalDuplicate);

        return "team/teamMgrDetCup";
    }

    //학원/클럽 상세 - 리그정보
    @RequestMapping(value="/teamMgrDetLeague")
    public String teamMgrDetLeague(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("teamMgrDetLeague was called. params:"+params);

        String cp = params.get("cp");
        model.addAttribute("cp", cp);

        //연령대
        String ageGroup = params.get("ageGroup");
        if(StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        // 대회 예선 경기일정 정보 연령별 테이블
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);
        // 대회 본선 경기일정 정보 연령별 테이블
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        params.put("cupMainMatchTB", cupMainMatchTB);
        // 대회 토너먼트(knockout stage) 경기일정 정보 연령별 테이블
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        params.put("cupTourMatchTB", cupTourMatchTB);
        //대회 결과 연령별 테이블
        String cupResultTB = ageGroup + "_Cup_Result";
        params.put("cupResultTB", cupResultTB);

        // 연령별 테이블
        String leagueInfoTB = ageGroup + "_League_Info";
        params.put("leagueInfoTB", leagueInfoTB);
        String leagueTeamTB = ageGroup + "_League_Team";
        params.put("leagueTeamTB", leagueTeamTB);
        String leagueMatchTB = ageGroup + "_League_Match";
        params.put("leagueMatchTB", leagueMatchTB);
        //대회 결과 연령별 테이블
        String leagueResultTB = ageGroup + "_League_Result";
        params.put("leagueResultTB", leagueResultTB);


        //검색
        String sArea = params.get("sArea");
        String sTeamType = params.get("sTeamType");
        String sNickName = params.get("sNickName");
        String sYear = params.get("sYear");

        model.addAttribute("sArea", sArea);
        model.addAttribute("sTeamType", sTeamType);
        model.addAttribute("sNickName", sNickName);
        model.addAttribute("sYear", sYear);

        //학원클럽 - 팀정보 상세
        HashMap<String, Object> teamInfoMap = teamService.selectTeamInfo(params);
        model.addAttribute("teamInfoMap", teamInfoMap);


        //학원클럽 - 해당 연도 리그성적표
        List<HashMap<String, Object>> teamLeagueEnterList = leagueService.selectTeamEnterLeague(params);
        //model.addAttribute("teamLeagueEnterList", teamLeagueEnterList);
        //학원클럽 - 해당 연도 리그 경기 결과
        List<HashMap<String, Object>> teamLeagueEntertMatchList = leagueService.selectTeamEnterLeagueMatch(params);
        //model.addAttribute("teamLeagueEntertMatchList", teamLeagueEntertMatchList);



        //학원클럽 - 해당 연도 리그 승무패
        List<HashMap<String, Object>> teamLeagueResultByYearList = leagueService.selectTeamLeagueResultByYear(params);
        //model.addAttribute("teamLeagueResultByYearList", teamLeagueResultByYearList);

        //해당 연도 대회리그 평균득실점
        HashMap<String, Object> teamCupAvgGoalMap = matchService.selectTeamCupAvgGoal(params);
        //model.addAttribute("teamCupAvgGoalMap", teamCupAvgGoalMap);




        //해당 연도 대회리그 순위추이
        List<HashMap<String, Object>> teamLeagueRankList = leagueService.selectTeamLeagueRankByYear(params);
        //model.addAttribute("teamLeagueRankList", teamLeagueRankList);

        //해당 연도 대회리그 평균득실점
        List<HashMap<String, Object>> teamLeagueAvgGoalList = leagueService.selectTeamLeagueAvgGoalByYear(params);
        //model.addAttribute("teamLeagueAvgGoalList", teamLeagueAvgGoalList);


        String pId = String.valueOf(teamInfoMap.get("pId"));

        Boolean isLeagueResultDuplicate = false;
        Boolean isLeagueRankDuplicate = false;
        Boolean isLeagueAvgGoalDuplicate = false;

        //해당클럽 리스트가 비어있을 경우
        //부모팀의 리스트를 가져옴
        if(!pId.equals("0")) {
            System.out.println("====== No List");

            //System.out.println("====== pId:"+pId);

            if(!pId.equals("0")) {
                params.put("teamId", pId);
            }

            //학원클럽 - 해당 연도 리그 승무패
            List<HashMap<String, Object>> pTeamLeagueResultByYearList = leagueService.selectTeamLeagueResultByYear(params);
            if (pTeamLeagueResultByYearList.size() > 0) {
                isLeagueResultDuplicate = true;
                teamLeagueResultByYearList.addAll(pTeamLeagueResultByYearList);
                teamLeagueResultByYearList = teamLeagueResultByYearList.stream().sorted(Comparator.comparingInt(map -> Integer.parseInt(map.get("play_year").toString())))
                    .collect(Collectors.toList());
            }

            //해당 연도 대회리그 순위추이
            List<HashMap<String, Object>> pTeamLeagueRankList = leagueService.selectTeamLeagueRankByYear(params);
            if (pTeamLeagueRankList.size() > 0) {
                isLeagueRankDuplicate = true;
                teamLeagueRankList.addAll(pTeamLeagueRankList);
                teamLeagueRankList = teamLeagueRankList.stream().sorted(Comparator.comparingInt(map -> Integer.parseInt(map.get("play_year").toString())))
                    .collect(Collectors.toList());
            }

            //해당 연도 대회리그 평균득실점
            List<HashMap<String, Object>> pTeamLeagueAvgGoalList = leagueService.selectTeamLeagueAvgGoalByYear(params);
            if (pTeamLeagueAvgGoalList.size() > 0) {
                isLeagueAvgGoalDuplicate = true;
                teamLeagueAvgGoalList.addAll(pTeamLeagueAvgGoalList);
                teamLeagueAvgGoalList = teamLeagueAvgGoalList.stream().sorted(Comparator.comparingInt(map -> Integer.parseInt(map.get("play_year").toString())))
                    .collect(Collectors.toList());
            }

            //학원클럽 - 해당 연도 리그성적표
            //teamLeagueEnterList = leagueService.selectTeamEnterLeague(params);
            //model.addAttribute("teamLeagueEnterList", teamLeagueEnterList);
            teamLeagueEnterList.addAll(leagueService.selectTeamEnterLeague(params));

            //학원클럽 - 해당 연도 리그 경기 결과
            //teamLeagueEntertMatchList = leagueService.selectTeamEnterLeagueMatch(params);
            //model.addAttribute("teamLeagueEntertMatchList", teamLeagueEntertMatchList);
            teamLeagueEntertMatchList.addAll(leagueService.selectTeamEnterLeagueMatch(params));

            //해당 연도 대회리그 평균득실점
            HashMap<String, Object> pTeamCupAvgGoalMap = matchService.selectTeamCupAvgGoal(params);

            if (teamCupAvgGoalMap.get("team_id") == null) {
                teamCupAvgGoalMap = pTeamCupAvgGoalMap;
            }
            //model.addAttribute("teamCupAvgGoalMap", teamCupAvgGoalMap);

        }

        model.addAttribute("teamLeagueEnterList", teamLeagueEnterList);
        model.addAttribute("teamLeagueEntertMatchList", teamLeagueEntertMatchList);
        model.addAttribute("teamLeagueResultByYearList", teamLeagueResultByYearList);
        model.addAttribute("teamCupAvgGoalMap", teamCupAvgGoalMap);
        model.addAttribute("teamLeagueRankList", teamLeagueRankList);
        model.addAttribute("teamLeagueAvgGoalList", teamLeagueAvgGoalList);

        model.addAttribute("isLeagueResultDuplicate", isLeagueResultDuplicate);
        model.addAttribute("isLeagueRankDuplicate", isLeagueRankDuplicate);
        model.addAttribute("isLeagueAvgGoalDuplicate", isLeagueAvgGoalDuplicate);

        //연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();


        UageDto uageDto = UageDto.builder()
            .ageGroup(params.get("ageGroup"))
            .areaSearch(params.get("areaSearch"))
            .areaId(params.get("areaId"))
            .build();

        // 지역정보
        List<HashMap<String, Object>> areaList = uageService.selectAreaList(params);

        model.addAttribute("uageList", uageList);
        model.addAttribute("areaList", areaList);

        model.addAttribute("ageGroup", ageGroup);

        return "team/teamMgrDetLeague";
    }
    
    //학원/클럽 상세 - 리그정보
    @RequestMapping(value="/teamMgrDetPlayer")
    public String teamMgrDetPlayer(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("teamMgrDetLeague was called. params:"+params);

        String cp = params.get("cp");
        model.addAttribute("cp", cp);

        //연령대
        String ageGroup = params.get("ageGroup");
        if(StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        // 대회 예선 경기일정 정보 연령별 테이블
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);
        // 대회 본선 경기일정 정보 연령별 테이블
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        params.put("cupMainMatchTB", cupMainMatchTB);
        // 대회 토너먼트(knockout stage) 경기일정 정보 연령별 테이블
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        params.put("cupTourMatchTB", cupTourMatchTB);
        //대회 결과 연령별 테이블
        String cupResultTB = ageGroup + "_Cup_Result";
        params.put("cupResultTB", cupResultTB);

        // 연령별 테이블
        String leagueInfoTB = ageGroup + "_League_Info";
        params.put("leagueInfoTB", leagueInfoTB);
        String leagueTeamTB = ageGroup + "_League_Team";
        params.put("leagueTeamTB", leagueTeamTB);
        String leagueMatchTB = ageGroup + "_League_Match";
        params.put("leagueMatchTB", leagueMatchTB);
        //대회 결과 연령별 테이블
        String leagueResultTB = ageGroup + "_League_Result";
        params.put("leagueResultTB", leagueResultTB);


        //검색
        String sArea = params.get("sArea");
        String sTeamType = params.get("sTeamType");
        String sNickName = params.get("sNickName");
        String sYear = params.get("sYear");

        model.addAttribute("sArea", sArea);
        model.addAttribute("sTeamType", sTeamType);
        model.addAttribute("sNickName", sNickName);
        model.addAttribute("sYear", sYear);

        //학원클럽 - 팀정보 상세
        HashMap<String, Object> teamInfoMap = teamService.selectTeamInfo(params);
        model.addAttribute("teamInfoMap", teamInfoMap);

        String teamGroupId = (String) teamInfoMap.get("team_group_id");
        String originalGroupName = (String) teamInfoMap.get("original_group_name");
        params.put("teamGroupId", teamGroupId);
        
        List<HashMap<String, Object>> playerList = teamService.selectJoinKfaPlayerList(params);
        model.addAttribute("playerList", playerList);
        
        params.put("originalGroupName", originalGroupName);
        
        List<HashMap<String, Object>> joinKfaTeamList = teamService.selectJoinKfaTeamList(params);
        
        if (joinKfaTeamList != null) {
        	if (joinKfaTeamList.size() == 1) {
        		String npTeamId = (String) joinKfaTeamList.get(0).get("np_team_id");
        		String paramTeamId = params.get("teamId");
        		if (!StrUtil.isEmpty(npTeamId)) {
        			if (npTeamId.indexOf(paramTeamId) > -1) {
            			model.addAttribute("includeTeam", 0);
            		} else {
            			model.addAttribute("includeTeam", 1);
            		}
        		} else {
        			model.addAttribute("includeTeam", 1);
        		}
        		model.addAttribute("joinKfaTeamId", joinKfaTeamList.get(0).get("team_id"));
        	}
        } else {
        	model.addAttribute("includeTeam", 0);
        }

        String pId = String.valueOf(teamInfoMap.get("pId"));

        //해당클럽 리스트가 비어있을 경우
        //부모팀의 리스트를 가져옴
        if(!pId.equals("0")) {
        	params.put("teamId", pId);
        }

        //연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();


        UageDto uageDto = UageDto.builder()
            .ageGroup(params.get("ageGroup"))
            .areaSearch(params.get("areaSearch"))
            .areaId(params.get("areaId"))
            .build();

        // 지역정보
        List<HashMap<String, Object>> areaList = uageService.selectAreaList(params);

        model.addAttribute("uageList", uageList);
        model.addAttribute("areaList", areaList);

        model.addAttribute("ageGroup", ageGroup);

        return "team/teamMgrDetPlayer";
    }

    @RequestMapping(value="/teamGroup")
    public String teamGroup(@RequestParam Map<String, String> params, TeamDto teamDto, Model model, HttpServletResponse resp) {
        logger.info("teamMgr was called. params:"+params);


        //검색
        String sNickName = params.get("sKeyword");
        model.addAttribute("sKeyword", sNickName);

        String useFlag = params.get("useFlag");
        model.addAttribute("useFlag", useFlag);

        String cnt = params.get("cnt");
        model.addAttribute("cnt", cnt);

        //정렬
        String order = params.get("order");
        String orderName = params.get("orderName");
        model.addAttribute("order", order);
        model.addAttribute("orderName", orderName);

        /* 페이징 처리 ------------------------------------------------------------------------------------------------*/
        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; //현재페이지 갯수

        int totalCount = teamService.selectTeamGroupListCount(params);


        int tp = 1;
        if (totalCount > 0) {
            tp = (int)totalCount / cpp;
            if ((totalCount % cpp) > 0) {
                tp += 1;
            }
        }

        int sRow = (cp -1) * cpp;

        params.put("sRow", "" + sRow);
        params.put("eCount", "" + cpp);

        HashMap<String, Object> map = StrUtil.calcPage(cp, totalCount, Define.COUNT_PAGE);
        logger.info(" ------ [team list paging]  totalCount : " +totalCount + ", tp :"+ tp+", cp:"+cp+", start : "+ map.get("start")+", end : "+map.get("end"));

        model.addAttribute("start", map.get("start"));
        model.addAttribute("end",  map.get("end"));
        model.addAttribute("prev",  map.get("prev"));
        model.addAttribute("next",  map.get("next"));

        model.addAttribute("cp", cp); //현재페이지번호
        model.addAttribute("cpp", cpp); //현재페이지 갯수
        model.addAttribute("tp", tp); //총 페이지 번호
        model.addAttribute("tc", totalCount); //총 리스트 갯수
        /* 페이징 처리 ------------------------------------------------------------------------------------------------*/


        //학원.클럽.유스 리스트 getting team list
        List<HashMap<String, Object>> teamGroupList = teamService.selectTeamGroupList(params);


        model.addAttribute("teamGroupList", teamGroupList);

        return "team/teamGroup";
    }

    @RequestMapping(value = "/save_teamGroup", method = {RequestMethod.GET, RequestMethod.POST})
    public String save_teamGroup(MultipartHttpServletRequest request, @RequestParam Map<String, String> params, TeamDto teamDto, Model model, HttpServletResponse resp, RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_team params : "+ params);
        String sFlag = teamDto.getSFlag();
        String cp = teamDto.getCp();


        String fileName = "emblem";

        teamDto.setSFlag(sFlag);
        teamDto.setCp(cp);

        if(sFlag.equals(Define.MODE_ADD)){ //등록
            teamService.insertTeamGroup(params);
        }else if(sFlag.equals(Define.MODE_FIX)) {//수정
            teamService.updateTeamGroup(params);
        }else if(sFlag.equals(Define.MODE_DELETE)) {//삭제
            teamService.deleteTeamGroup(params); //타겟 테이블 데이터 전체 삭제
        }

        return "redirect:/teamGroup";
    }

    @RequestMapping(value="/teamGroupDet")
    public String teamGroupDet(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("teamMgrDet was called. params:"+params);

        String cp = params.get("cp");
        model.addAttribute("cp", cp);

        model.addAttribute("groupDetail", teamService.selectTeamGroupDetail(params));

        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        model.addAttribute("uageList", uageList);

        return "team/teamGroupDet";
    }

    @RequestMapping(value="/add_teamGroup")
    @ResponseBody
    public Map<String, Object> addTeamGroup (@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("addTeamGroup was called. params:"+params);
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String method = params.get("method");

        int result = 0;

        try {
            if (method.equals("add")) {
                result = teamService.insertTeamGroupDetail(params);
                resultMap.put("state", "SUCCESS");
            }

            if (method.equals("delete")) {
                result = teamService.updateDeleteTeamForGroup(params);
                resultMap.put("state", "SUCCESS");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }

        resultMap.put("data", result);
        return resultMap;
    }

    @RequestMapping(value = "/teamGroupExcelUpload")
    @ResponseBody
    public Map<String, Object> excelUploadTeamGroup(MultipartHttpServletRequest request, @RequestParam Map<String, String> params) {
        logger.info("excelUploadTeamGroup was called. params:"+params);
        Map<String, Object> resultMap = new HashMap<String, Object>();

        Map<String, Object> result = new HashMap<String, Object>();

        try {
            result = teamService.insertExcelUploadTeamGroup(request, params);
            resultMap.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }

        resultMap.put("data", result);
        return resultMap;
    }
    
    // 팀명 수정 페이지
    @RequestMapping(value="/teamNameUpd")
    public String teamNameUpd(@RequestParam Map<String, String> params, Model model, TeamDto teamDto, HttpServletResponse resp) {
        logger.info("team was called. teamDto:"+teamDto);
        logger.info("team was called. params:"+params);

        //연령대
        String ageGroup = params.get("ageGroup");
        if(StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        //검색
        String sArea = params.get("sArea");
        String sTeamType = params.get("sTeamType");
        String sNickName = params.get("sNickName");

        /*String sArea = teamDto.getSArea();
        String sTeamType = teamDto.getSTeamType();
        String sNickName = teamDto.getSNickName();*/

        model.addAttribute("sArea", sArea);
        model.addAttribute("sTeamType", sTeamType);
        model.addAttribute("sNickName", sNickName);

        /* 페이징 처리 ------------------------------------------------------------------------------------------------*/
        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; //현재페이지 갯수

        HashMap<String, Object> teamCountMap = teamService.selectTeamListCount(params);
        long totalCount = (long) (Long) teamCountMap.get("totalCount");

        int tp = 1;
        if (totalCount > 0) {
            tp = (int)totalCount / cpp;
            if ((totalCount % cpp) > 0) {
                tp += 1;
            }
        }

        int sRow = (cp -1) * cpp;

        params.put("sRow", "" + sRow);
        params.put("eCount", "" + cpp);

        teamDto.setSRow(sRow);
        teamDto.setECount(cpp);

        HashMap<String, Object> map = StrUtil.calcPage(cp, totalCount, Define.COUNT_PAGE);
        logger.info(" ------ [team list paging]  totalCount : " +totalCount + ", tp :"+ tp+", cp:"+cp+", start : "+ map.get("start")+", end : "+map.get("end"));

        model.addAttribute("start", map.get("start"));
        model.addAttribute("end",  map.get("end"));
        model.addAttribute("prev",  map.get("prev"));
        model.addAttribute("next",  map.get("next"));

        model.addAttribute("cp", cp); //현재페이지번호
        model.addAttribute("cpp", cpp); //현재페이지 갯수
        model.addAttribute("tp", tp); //총 페이지 번호
        model.addAttribute("tc", totalCount); //총 리스트 갯수
        /* 페이징 처리 ------------------------------------------------------------------------------------------------*/

        //학원.클럽.유스 리스트 getting team list
        List<HashMap<String, Object>> teamList = teamService.selectTeamList(params);
        logger.info("teamList > " + teamList);
        //연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();


        UageDto uageDto = UageDto.builder()
            .ageGroup(teamDto.getAgeGroup())
            .areaSearch(teamDto.getAreaSearch())
            .areaId(teamDto.getAreaId())
            .build();
        logger.info("uageDto > " + uageDto.getAreaSearch());
        List<HashMap<String, Object>> areaList = uageService.selectAreaList(params);

        model.addAttribute("teamList", teamList);
        model.addAttribute("uageList", uageList);
        model.addAttribute("areaList", areaList);

        model.addAttribute("ageGroup", ageGroup);

		String webPath = StrUtil.getWebPath();
		model.addAttribute("webPath", webPath);
		
		String todayStr = DateUtil.getDefaultDate();
		Date today = DateUtils.parse(todayStr); 
		
		model.addAttribute("today", today);

        return "team/teamNameUpd";
    }
    
    @RequestMapping(value = "/update_teamName", method = {RequestMethod.GET, RequestMethod.POST})
    public String update_teamName(@RequestParam Map<String, String> params, TeamDto teamDto, Model model, HttpServletResponse resp) {
        logger.info("request ----> modify_team params : "+ params);

        //연령대
        String ageGroup = teamDto.getAgeGroup();
        if(StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }
        
        // 연령별 테이블
        // String cupInfoTB = ageGroup + "_Cup_Info";
        // params.put("cupInfoTB", cupInfoTB);
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        // 대회 예선 경기일정 정보 연령별 테이블
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);
        // 대회 본선 경기일정 정보 연령별 테이블
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        params.put("cupMainMatchTB", cupMainMatchTB);
        // 대회 토너먼트(knockout stage) 경기일정 정보 연령별 테이블
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        params.put("cupTourMatchTB", cupTourMatchTB);

        String leagueTeamTB = ageGroup + "_League_Team";
        params.put("leagueTeamTB", leagueTeamTB);
        String leagueMatchTB = ageGroup + "_League_Match";
        params.put("leagueMatchTB", leagueMatchTB);
        
        String year = params.get("year");
        String startDate = year + "-01-01";
        String endDate = year + "-12-31";
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        
        String newNickName = params.get("newNickName");
        
        if (!StrUtil.isEmpty(newNickName)) {
        	teamService.updateTeamForNickName(params);
        	
        	int selectYearNum = Integer.parseInt(year);
        	
        	String todayStr = DateUtil.getDefaultDate();
        	String yearStr = DateUtil.format(todayStr, "yyyy");
        	int todayYearNum = Integer.parseInt(yearStr);
        	
        	for (int i = selectYearNum; i <= todayYearNum; i++) {
        		params.put("year", String.valueOf(i));
        		cupService.updateTeamNameForChampion(params);
        	}
        	
        	HashMap<String, Object> teamNameCountMap = cupService.selectTeamNameForCupMainMatchCnt(params);
            long cupMainMatchCnt = (long) teamNameCountMap.get("cupMainMatchCnt");
            
            if (cupMainMatchCnt > 0) {
            	cupService.updateTeamNameForCupMainMatchHome(params);
            	cupService.updateTeamNameForCupMainMatchAway(params);
            }
            
            teamNameCountMap = cupService.selectTeamNameForCupSubMatchCnt(params);
            long cupSubMatchCnt = (long) teamNameCountMap.get("cupSubMatchCnt");
            
            if (cupSubMatchCnt > 0) {
            	cupService.updateTeamNameForCupSubMatchHome(params);
            	cupService.updateTeamNameForCupSubMatchAway(params);
            }
            
            teamNameCountMap = cupService.selectTeamNameForCupTourMatchCnt(params);
            long cupTourMatchCnt = (long) teamNameCountMap.get("cupTourMatchCnt");
            
            if (cupTourMatchCnt > 0) {
            	cupService.updateTeamNameForCupTourMatchHome(params);
            	cupService.updateTeamNameForCupTourMatchAway(params);
            }
            
            teamNameCountMap = cupService.selectTeamNameForCupTeamInSubMatchCnt(params);
            long cupTeamInMainMatchCnt = (long) teamNameCountMap.get("cupTeamCnt");
            
            if (cupTeamInMainMatchCnt > 0) {
            	cupService.updateTeamNameForCupTeamInMainMatch(params);
            }
            
            teamNameCountMap = cupService.selectTeamNameForCupTeamInMainMatchCnt(params);
            long cupTeamInSubMatchCnt = (long) teamNameCountMap.get("cupTeamCnt");
            
            if (cupTeamInSubMatchCnt > 0) {
            	cupService.updateTeamNameForCupTeamInSubMatch(params);
            }
            
            teamNameCountMap = leagueService.selectTeamNameForLeagueMatchCnt(params);
            long leagueMatchCnt = (long) teamNameCountMap.get("leagueMatchCnt");
            if (leagueMatchCnt > 0) {
            	leagueService.updateTeamNameForLeagueMatchHome(params);
            	leagueService.updateTeamNameForLeagueMatchAway(params);
            }
            
            teamNameCountMap = leagueService.selectTeamNameForLeagueTeamCnt(params);
            long leagueTeamCnt = (long) teamNameCountMap.get("leagueTeamCnt");
            
            if (leagueTeamCnt > 0) {
            	leagueService.updateTeamNameForLeagueTeam(params);
            }

        }
        
        String newTeamName = params.get("newTeamName");
        
        if (!StrUtil.isEmpty(newTeamName)) {
        	teamService.updateTeamForTeamName(params);
        }

        return "redirect:/teamNameUpd";
    }

    @RequestMapping(value = "/remove_emblem")
    @ResponseBody
    public Map<String, Object> removeEmblem(@RequestParam Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("removeEmblem was called. params:" + params);

        int result = 0;
        try {
            result = teamService.updateRemoveEmblem(params);
            resultMap.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }
        resultMap.put("data", result);
        return resultMap;
    }

    @RequestMapping(value = "/remove_teamGroup")
    @ResponseBody
    public Map<String, Object> removeTeamGroup(@RequestParam Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("removeTeamGroup was called. params:" + params);

        int result = 0;
        try {
            result = teamService.removeTeamGroup(params);
            resultMap.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }
        resultMap.put("data", result);
        return resultMap;
    }
    
    @RequestMapping(value = "/groupNameCheck")
    @ResponseBody
    public Map<String, Object> matchingTeam(@RequestParam Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("search_team was called. params:" + params);
        
        List<HashMap<String, Object>> joinKfaTeamList = teamService.selectJoinKfaTeamList(params);
        
        int teamSize = joinKfaTeamList.size();
        resultMap.put("data", teamSize);
        
        if (teamSize > 1) {
        	resultMap.put("teamList", joinKfaTeamList);
        } else if (teamSize == 1) {
        	resultMap.put("joinKfaTeamId", joinKfaTeamList.get(0).get("team_id"));
        	resultMap.put("npTeamId", joinKfaTeamList.get(0).get("np_team_id"));
        	String joinKfaTeamId = String.valueOf(joinKfaTeamList.get(0).get("team_id"));
        	String npTeamGroupId = String.valueOf(joinKfaTeamList.get(0).get("np_team_group_id"));
        	
        	params.put("joinKfaTeamId", joinKfaTeamId);
        	
        	String npTeamId = (String)joinKfaTeamList.get(0).get("np_team_id");
        	String paramTeamId = params.get("npTeamId");
        	String paramTeamGroupId = params.get("npTeamGroupId");
        	
        	if (!StrUtil.isEmpty(npTeamGroupId)) {
        		if (npTeamGroupId.equals(paramTeamGroupId)) {
        			if (!StrUtil.isEmpty(npTeamId)) {
                		if (npTeamId.indexOf(paramTeamId) < 0) {
                			String newNpTeamId = npTeamId + "," + paramTeamId;
                			params.put("npTeamId", newNpTeamId);
                    	} else {
                    		params.put("npTeamId", npTeamId);
                    	}
                		
                		int result = 0;
                    	try {
                            result = teamService.updateJoinKfaTeam(params);
                            resultMap.put("state", "SUCCESS");
                            resultMap.put("status", result);
                        } catch (Exception e) {
                            e.printStackTrace();
                            resultMap.put("state", "ERROR");
                        }
                	} else {
                		int result = 0;
                    	try {
                            result = teamService.updateJoinKfaTeam(params);
                            resultMap.put("state", "SUCCESS");
                            resultMap.put("status", result);
                        } catch (Exception e) {
                            e.printStackTrace();
                            resultMap.put("state", "ERROR");
                        }
                	}
        		} else {
        			resultMap.put("status", "-1");
        		}
        	} else {
        		int result = 0;
        		try {
                    result = teamService.updateJoinKfaTeam(params);
                    resultMap.put("state", "SUCCESS");
                    resultMap.put("status", result);
                } catch (Exception e) {
                    e.printStackTrace();
                    resultMap.put("state", "ERROR");
                }
        	}
        }
        
        return resultMap;
    }
    
    @RequestMapping(value = "/getPlayerList")
    @ResponseBody
    public Map<String, Object> getPlayerList(@RequestParam Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("search_team was called. params:" + params);
        
        List<HashMap<String, Object>> playerList = teamService.selectPlayerList(params);
        
        resultMap.put("list", playerList);
        
        return resultMap;
    }
    
    @RequestMapping(value = "/regPlayerList")
    @ResponseBody
    public Map<String, Object> regPlayerList(@RequestParam Map<String, String> params) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("search_team was called. params:" + params);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> teamDataMap = objectMapper.readValue(params.get("playerList"), new TypeReference<Map<String, Object>>() {});
        List<String> teamDataList = (List<String>) teamDataMap.get("playerList");
        for (String p : teamDataList) {
        	HashMap<String, String> teamDataItemMap = objectMapper.readValue(p, new TypeReference<HashMap<String, String>>() {});
        	String birthday = teamDataItemMap.get("playerBirthday");
        	String newbirth = birthday.replaceAll("-", "");
        	 teamDataItemMap.put("playerBirthday", newbirth);
        	 teamService.insertJoinKfaPlayer(teamDataItemMap);
        }
        
        return resultMap;
    }
    
    @RequestMapping(value = "/regPlayer")
    @ResponseBody
    public Map<String, Object> regPlayer(@RequestParam Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("search_team was called. params:" + params);
        String imgUrl = params.get("playerImg");
        if (!StrUtil.isEmpty(imgUrl)) {
        	String playerImgBinary = getBinary(imgUrl);
        	params.put("playerImgBinary", playerImgBinary);
        }
        teamService.insertJoinKfaPlayer(params);
        
        return resultMap;
    }
    
    @RequestMapping(value = "/save_team_mgr", method = {RequestMethod.GET, RequestMethod.POST})
    public String save_team_mgr(MultipartHttpServletRequest request, @RequestParam Map<String, String> params, TeamDto teamDto, Model model, HttpServletResponse resp, RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_team params : "+ params);
        String sFlag = teamDto.getSFlag();
        String cp = teamDto.getCp();

        //연령대
        String ageGroup = teamDto.getAgeGroup();
        if(StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }
        redirectAttributes.addAttribute("ageGroup", ageGroup);

        String fileName = "emblem";

        teamDto.setSFlag(sFlag);
        teamDto.setCp(cp);

        String teamId = params.get("teamId");
        //엠블럼 이미지 업로드
        String emblemFile = StrUtil.contentUpload(fileName, ageGroup, teamId, request);
        //teamDto.setEmblemFile(emblemFile);
        System.out.println("---- emblemFile : "+ emblemFile);
        if(StrUtil.isEmpty(emblemFile)){
            String imgFilePath = (String) params.get("imgFilePath");
            params.put("emblemFile", imgFilePath);
            teamDto.setEmblemFileParam(imgFilePath);
        }else{
            params.put("emblemFile", emblemFile);
            teamDto.setEmblemFileParam(emblemFile);
        }
        teamService.updateTeam(params);
        
        String teamGroupId = params.get("teamGroupId");
        
        if (!StrUtil.isEmpty(teamGroupId)) {
        	teamService.updateOriginalTeamGroupName(params);
        }

        redirectAttributes.addAttribute("cp", cp);
        redirectAttributes.addAttribute("teamId", teamId);
        
        String uri = params.get("uri");

        return "redirect:/" + uri;
    }
    
    @RequestMapping(value = "/save_player_mgr", method = {RequestMethod.GET, RequestMethod.POST})
    public String save_player_mgr(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp, RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_player_mgr params : "+ params);
        String ageGroup = params.get("ageGroup");
        if(StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }
        redirectAttributes.addAttribute("ageGroup", ageGroup);
        
        String imgUrl = params.get("playerImg");
        if (!StrUtil.isEmpty(imgUrl)) {
        	String playerImgBinary = getBinary(imgUrl);
        	params.put("playerImgBinary", playerImgBinary);
        }
        
        teamService.updateJoinKfaPlayer(params);
        
        String cp = params.get("cp");
        String teamId = params.get("npTeamId");

        redirectAttributes.addAttribute("cp", cp);
        redirectAttributes.addAttribute("teamId", teamId);

        return "redirect:/teamMgrDetPlayer";
    }
    
    @RequestMapping(value = "/remove_joinKfaPlayer")
    @ResponseBody
    public Map<String, Object> removeJoinKfaPlayer(@RequestParam Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("removeJoinKfaPlayer was called. params:" + params);

        int result = 0;
        try {
            result = teamService.updateDeleteJoinKfaPlayer(params);
            resultMap.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }
        resultMap.put("data", result);
        return resultMap;
    }
    
    @RequestMapping(value = "/modify_mapping")
    @ResponseBody
    public Map<String, Object> modifyMapping(@RequestParam Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("modifyMapping was called. params:" + params);

        int result = 0;
        try {
            result = teamService.updateJoinKfaPlayerMapping(params);
            resultMap.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }
        resultMap.put("data", result);
        return resultMap;
    }
    
    @RequestMapping(value = "/getTeamList")
    @ResponseBody
    public Map<String, Object> getTeamList(@RequestParam Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("getTeamList was called. params:" + params);
        
        List<HashMap<String, Object>> teamList = teamService.selectJoinKfaTeamListSearch(params);
        
        resultMap.put("list", teamList);
        
        return resultMap;
    }
    
    @RequestMapping(value = "/remove_joinKfaTeam")
    @ResponseBody
    public Map<String, Object> removeJoinKfaTeam(@RequestParam Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("remove_joinKfaTeam was called. params:" + params);
        
        int result = 0;
        try {
        	result = teamService.updateDeleteJoinKfaTeam(params);
            resultMap.put("state", "SUCCESS");
            List<HashMap<String, Object>> teamList = teamService.selectJoinKfaTeamListSearch(params);
            resultMap.put("list", teamList);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }
        
        resultMap.put("data", result);
        
        return resultMap;
    }
    
    @RequestMapping(value = "/regJoinKfaTeam")
    @ResponseBody
    public Map<String, Object> regJoinKfaTeam(@RequestParam Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("regJoinKfaTeam was called. params:" + params);
        
        int result = 0;
        try {
        	result = teamService.insertJoinKfaTeam(params);
            resultMap.put("state", "SUCCESS");
            List<HashMap<String, Object>> teamList = teamService.selectJoinKfaTeamListSearch(params);
            resultMap.put("list", teamList);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }
        
        resultMap.put("data", result);
        
        return resultMap;
    }
    
    @RequestMapping(value = "/modJoinKfaTeam")
    @ResponseBody
    public Map<String, Object> modJoinKfaTeam(@RequestParam Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("search_team was called. params:" + params);
        
        int result = 0;
        try {
        	result = teamService.updateModJoinKfaTeam(params);
            resultMap.put("state", "SUCCESS");
            List<HashMap<String, Object>> teamList = teamService.selectJoinKfaTeamListSearch(params);
            resultMap.put("list", teamList);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }
        
        return resultMap;
    }
    
    @RequestMapping(value = "/playerCheck")
    @ResponseBody
    public Map<String, Object> playerCheck(@RequestParam Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("playerCheck was called. params:" + params);
        
        try {
        	List<HashMap<String, Object>> player = teamService.selectPlayerToName(params);
        	resultMap.put("state", "SUCCESS");
        	if (player != null) {
        		resultMap.put("list", player);
        		resultMap.put("method", "modify");
        	} else {
        		resultMap.put("method", "save");
        	}
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }
        
        return resultMap;
    }
    
    private String getBinary(String imageUrl) {
        String changeString;
        try {
            URL url = new URL(imageUrl);
            BufferedImage img = ImageIO.read(url);
            // URL을 통해 File 생성
            File file = new File("downloaded.jpg");
            ImageIO.write(img, "jpg", file);
            InputStream finput = new FileInputStream(file);
            byte[] imageBytes = new byte[(int)file.length()];
            finput.read(imageBytes, 0, imageBytes.length);
            finput.close();
            String filePathName = imageUrl.replace("file:///", "");
            String fileExtName = filePathName.substring( filePathName.lastIndexOf(".") + 1);
            // Base64
            String imageStr = Base64.encodeBase64String(imageBytes);
            changeString = "data:image/"+ fileExtName +";base64, "+ imageStr;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return changeString;
    }
    
}
