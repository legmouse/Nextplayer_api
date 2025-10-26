package kr.co.nextplayer.base.backend.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import kr.co.nextplayer.base.backend.excel.ExcelService;
import kr.co.nextplayer.base.backend.excel.ExcelRead;
import kr.co.nextplayer.base.backend.excel.ExcelRead2;
import kr.co.nextplayer.base.backend.excel.ExcelReadOption;
import kr.co.nextplayer.base.backend.service.*;
import kr.co.nextplayer.base.backend.util.Define;
import kr.co.nextplayer.base.backend.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ExcelController {
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private TeamService teamService;

    @Resource
    private CupService cupService;

    @Resource
    private LeagueService leagueService;

    @Resource
    private MemberService memberService;

    @Resource
    private EducationService educationService;

    @Resource
    private ExcelService excelCreate;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    private int recordLog(String type, String target) {

        String grade = "system master";
        String empName = "김덕래";

        // 접속기록 저장
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("grade", grade);
        params.put("empName", empName);
        params.put("target", target.replaceAll("\"", "'")); // 카테고리 행위 정보 ex)"main_write_01[인적사항 : 수정]"

        if (type.equals(Define.STR_SUCCESS)) {
//			sqlSessionInst.insert("insAccessRecordLog", params);

        } else if (type.equals(Define.STR_ERROR)) {
//			sqlSessionInst.insert("insErrorRecordLog", params);
        }

        return 0;
    }

    // excel 일괄등록
    @RequestMapping(value = "/excelUpload")
    public String excelUpload(MultipartHttpServletRequest request, @RequestParam Map<String, String> params,
                              Model model, HttpServletResponse resp, RedirectAttributes attributes) {
        logger.info("excelUpload was called. params:" + params);

        // transaction
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus status = transactionManager.getTransaction(def);

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }
        MultipartFile excelFile = request.getFile("excelFile");

        String cupId = params.get("cupId");
        String leagueId = params.get("leagueId");

        if (excelFile == null || excelFile.isEmpty()) {

            throw new RuntimeException("엑셀파일을 선택 해 주세요.");
        }
        // File destFile = new File("C:\\"+excelFile.getOriginalFilename());
        // D:/Image_test/upload/
        // File destFile = new
        // File(Define.SAVE_FILE_PATH+excelFile.getOriginalFilename());
        String contentPath = StrUtil.getContentPath();
        File destFile = new File(contentPath + excelFile.getOriginalFilename());

        String excelFlag = params.get("excelFlag");
        String cupType = ""; //토너먼트 타입

        if (StrUtil.isEmpty(excelFlag)) {
            return null;
        }

        int colSize = 0;
        if (excelFlag.equals("team")) { // 학원.클럽.유스 등록
            colSize = Define.TEAM_Column;

        } else if (excelFlag.equals("leagueInfo")) { // league_info 기본 정보 등록
            colSize = Define.LEAGUE_INFO_Column;

        } else if (excelFlag.equals("leagueTeam")) { // league_team 참가팀 정보 등록
            colSize = Define.LEAGUE_TEAM_Column;

        } else if (excelFlag.equals("leagueMatch")) { // league_match 경기일정 정보 등록
            colSize = Define.LEAGUE_MATCH_Column;

        } else if (excelFlag.equals("cupInfo")) { // cup_info 기본 정보 등록
            colSize = Define.CUP_INFO_Column;

        } else if (excelFlag.equals("cupTeam")) { // cup_team 참가팀 정보 등록
            //colSize = Define.CUP_TEAM_Column;
            //colSize = Integer.parseInt(params.get("teamColumn")); //컬럼수 = 그룹수

            cupType = params.get("cupType");

            //0:예선+토너먼트,  1:예선+본선+토너먼트, 2: 풀리그, 3: 토너먼트
            if(cupType.equals("3")) {
                colSize = Define.CUP_TEAM_ONLY_TOUR_Column; //3;//Integer.parseInt(params.get("teamColumn")); //컬럼수 = 그룹수

            }else {
                colSize = Integer.parseInt(params.get("teamColumn")); //컬럼수 = 그룹수, 동적변수로 인해 Define 미사용

            }
        } else if (excelFlag.equals("cupMainTeam")) { // cup_main_team 본선 참가팀 정보 등록
            colSize = Integer.parseInt(params.get("teamColumn")); //컬럼수 = 그룹수, 동적변수로 인해 Define 미사용


//		} else if (excelFlag.equals("cup_team_onlyTournament")) { // cup_team 토너먼트(단일) 참가팀 정보 등록
//			colSize = Define.CUP_TEAM_Column;

        } else if (excelFlag.equals("cupSubMatch")) { // cup_sub_match 경기일정 정보 등록
            colSize = Define.CUP_SUB_MATCH_Column;

        } else if (excelFlag.equals("cupMainMatch")) { // cup_main_match 경기일정 정보 등록
            colSize = Define.CUP_MAIN_MATCH_Column;

        } else if (excelFlag.equals("cupTourMatch")) { // cup_tour_match 경기일정 정보 등록
            colSize = Define.CUP_TOUR_MATCH_Column;
        }


        String[] colums = StrUtil.makeExcelColumns(colSize);

        ExcelReadOption excelReadOption = StrUtil.excelUpload(request, colums);

        /*
         * 대회 팀등록 방식으로 인한 분기처리  2020.05 MH1000
         */
        List<Map<String, String>> excelContent = null;
        if(excelFlag.equals("cupTeam") || excelFlag.equals("cupMainTeam")) {
            //excelContent = ExcelRead2.read(excelReadOption);

            //0:예선+토너먼트,  1:예선+본선+토너먼트, 2: 풀리그, 3: 토너먼트
            if(cupType.equals("3")) {
                excelContent = ExcelRead.read(excelReadOption, params);
            }else {
                excelContent = ExcelRead2.read(excelReadOption);
            }



        }else {
            //List<Map<String, String>> excelContent = ExcelRead.read(excelReadOption);
            excelContent = ExcelRead.read(excelReadOption, params);
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("excelContent", excelContent);


        // 리그 기본정보 연령별 테이블
        String leagueInfoTB = ageGroup + "_League_Info";
        // 리그 참가팀 정보 연령별 테이블
        String leagueTeamTB = ageGroup + "_League_Team";
        // 리그 경기일정 정보 연령별 테이블
        String leagueMatchTB = ageGroup + "_League_Match";


        // 대회 기본정보 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        // 대회 참가팀 정보 연령별 테이블
        String cupTeamTB = ageGroup + "_Cup_Team";
        // 대회 예선 경기일정 정보 연령별 테이블
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        // 대회 본선 경기일정 정보 연령별 테이블
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        // 대회 토너먼트(knockout stage) 경기일정 정보 연령별 테이블
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";

        if (excelFlag.equals("team")) { // 학원.클럽.유스 등록
            try {
                if (excelContent.size() > 0) {
                    for (Map<String, String> map : excelContent) {
                        teamService.insertTeamExcel(map); // 학원.클럽.유스 일괄 등록
                    }
                }
                recordLog(Define.STR_SUCCESS,
                    "[" + excelFlag + "] 학원.클럽.유스 excel 일괄등록 - 성공 -params:" + params.toString()); // 접속하여 행위한 정보에대한
                // 기록
                transactionManager.commit(status);
            } catch (Exception e) {
                transactionManager.rollback(status);

                recordLog(Define.STR_ERROR, "[" + excelFlag + "] 학원.클럽.유스 정보 excel 일괄등록 - 실패 message:등록실패로 롤백 시킴."); // 접속하여
                // 행위한
                // 정보에대한
                // 기록
            }
        } else if (excelFlag.equals("leagueInfo")) { // league_info 기본정보 등록
            paramMap.put("leagueInfoTB", leagueInfoTB);

            try {
                leagueService.insertLeagueInfoExcel(paramMap); // 리그 기본정보 일괄 등록
                recordLog(Define.STR_SUCCESS,
                    "[" + excelFlag + "] 리그 기본정보 excel 일괄등록 - 성공 -params:" + params.toString()); // 접속하여 행위한 정보에대한
                // 기록

                transactionManager.commit(status);
            } catch (Exception e) {
                transactionManager.rollback(status);

                recordLog(Define.STR_ERROR, "[" + excelFlag + "] 리그 기본정보 excel 일괄등록 - 실패 message:등록실패로 롤백 시킴."); // 접속하여
                // 행위한
                // 정보에대한
                // 기록
            }

            model.addAttribute("ageGroup", ageGroup);
            excelFlag = "league";

        } else if (excelFlag.equals("leagueTeam")) { // league_team 리그 참가팀 등록
            paramMap.put("leagueTeamTB", leagueTeamTB);
            params.put("leagueTeamTB", leagueTeamTB);
            params.put("leagueInfoTB", leagueInfoTB);

            leagueId = params.get("leagueId");
            paramMap.put("leagueId", leagueId);

            try {
                if (excelContent.size() > 0) {
                    for(Map<String, String> map : excelContent) {
                        map.put("leagueTeamTB", leagueTeamTB);
                        map.put("leagueId", leagueId);
                        leagueService.insertLeagueTeamExcel(map); // 리그 참가팀 일괄 등록
                    }
                }
                recordLog(Define.STR_SUCCESS,
                    "[" + excelFlag + "] 리그 참가팀 정보 excel 일괄등록 - 성공 -params:" + params.toString()); // 접속하여 행위한 정보에대한
                // 기록

                // leagueService.updateLeagueTeamExcelByleagueId(params); // 리그 참가팀 리그 아이디 수정
                recordLog(Define.STR_SUCCESS,
                    "[" + excelFlag + "] 리그 참가팀 리그키 정보 일괄수정 - 성공 -params:" + params.toString()); // 접속하여 행위한 정보에대한
                // 기록

                transactionManager.commit(status);
            } catch (Exception e) {
                e.printStackTrace();
                transactionManager.rollback(status);

                recordLog(Define.STR_ERROR, "[" + excelFlag + "] 리그 참가팀 정보 excel 일괄등록 - 실패 message:등록실패로 롤백 시킴."); // 접속하여
                // 행위한
                // 정보에대한
                // 기록
            }
            // 참가팀 아이디 조회 후 업데이트
            List<HashMap<String, Object>> list = leagueService.selectLeagueTeamList(params); // 리그 참가팀 정보

            if(list.size() > 0) {
                for (Map<String, Object> map: list) {
                    map.put("leagueTeamTB", leagueTeamTB);
                    map.put("leagueId", leagueId);
                    leagueService.updateLeagueTeamExcel_type2(map);

                }
            }


            model.addAttribute("ageGroup", ageGroup);
            model.addAttribute("leagueId", leagueId);
            model.addAttribute("sFlag", "1");

            excelFlag = "leagueTeam";

        } else if (excelFlag.equals("leagueMatch")) { // league_match 리그 경기일정 등록
            paramMap.put("leagueMatchTB", leagueMatchTB);
            params.put("leagueMatchTB", leagueMatchTB);
            params.put("leagueInfoTB", leagueInfoTB);

            leagueId = params.get("leagueId");
            paramMap.put("leagueId", leagueId);
            params.put("leagueId", leagueId);

            try {
                // 리그 경기일정 정보 조회 후 있으면 해당 경기일정 삭제
                HashMap<String, Object> countMap = leagueService.selectLeagueMatchListCount(params);
                long totalCount = (Long) countMap.get("totalCount");
                logger.info(" ------ [Excel leagueMatch]  totalCount : " + totalCount);
                if (totalCount > 0) {
                    leagueService.deleteLeagueMatch(params); // 타겟 테이블 데이터 전체 삭제
                }

                if(excelContent.size() > 0) {
                    for (Map<String, String> map : excelContent) {
                        map.put("leagueMatchTB", leagueMatchTB);
                        map.put("leagueId", leagueId);
                        leagueService.insertLeagueMatchExcel(map);
                    }
                }

                //leagueService.insertLeagueMatchExcel(paramMap); // 리그 경기일정 일괄 등록
                recordLog(Define.STR_SUCCESS,
                    "[" + excelFlag + "] 리그 경기일정 정보 excel 일괄등록 - 성공 -params:" + params.toString()); // 접속하여 행위한
                // 정보에대한 기록

                //leagueService.updateLeagueMatchExcelByLeagueId(params); // 리그 경기일 리그 아이디 수정
                recordLog(Define.STR_SUCCESS,
                    "[" + excelFlag + "] 리그 경기일정 리그키 정보 일괄수정 - 성공 -params:" + params.toString()); // 접속하여 행위한 정보에대한
                // 기록

                transactionManager.commit(status);
            } catch (Exception e) {
                transactionManager.rollback(status);

                recordLog(Define.STR_ERROR, "[" + excelFlag + "] 리그 참가팀 정보 excel 일괄등록 - 실패 message:등록실패로 롤백 시킴."); // 접속하여
                // 행위한
                // 정보에대한
                // 기록
            }

            // 참가팀 아이디 조회 후 업데이트 - 홈팀
            List<HashMap<String, Object>> homeList = leagueService.selectLeagueMatchListByHomeTeam(params); // 리그
            // 경기일정
            // 홈팀
            // 정보

            if(homeList.size() > 0) {
                for (Map<String, Object> map: homeList) {
                    map.put("leagueMatchTB", leagueMatchTB);
                    map.put("leagueId", leagueId);
                    leagueService.updateLeagueMatchExcel_home(map);
                }
            }


            // 참가팀 아이디 조회 후 업데이트 - 어웨이팀
            List<HashMap<String, Object>> awayList = leagueService.selectLeagueMatchListByAwayTeam(params); // 리그
            // 경기일정
            // 홈팀
            // 정보

            if(awayList.size() > 0) {
                for(Map<String, Object> map : awayList) {
                    map.put("leagueMatchTB", leagueMatchTB);
                    map.put("leagueId", leagueId);
                    leagueService.updateLeagueMatchExcel_away(map);
                }
            }

            model.addAttribute("ageGroup", ageGroup);
            model.addAttribute("leagueId", leagueId);
            model.addAttribute("sFlag", "1");

            String lMgrFlag = params.get("lMgrFlag");
            if (!StrUtil.isEmpty(lMgrFlag)) {
                excelFlag = "leagueMgrInfo";

            } else {
                excelFlag = "leagueMatch";

            }

//대회 시작 cup
        } else if (excelFlag.equals("cupInfo")) { // cup_Info 기본정보 등록
            paramMap.put("cupInfoTB", cupInfoTB);

            try {
                cupService.insertCupInfoExcel(paramMap); // 대회 기본정보 일괄 등록
                recordLog(Define.STR_SUCCESS,
                    "[" + excelFlag + "] 대회 기본정보 excel 일괄등록 - 성공 -params:" + params.toString()); // 접속하여 행위한 정보에대한
                // 기록

                transactionManager.commit(status);
            } catch (Exception e) {
                transactionManager.rollback(status);

                recordLog(Define.STR_ERROR, "[" + excelFlag + "] 대회 기본정보 excel 일괄등록 - 실패 message:등록실패로 롤백 시킴."); // 접속하여
                // 행위한
                // 정보에대한
                // 기록
            }

            model.addAttribute("ageGroup", ageGroup);
            excelFlag = "cup";

        } else if(excelFlag.equals("cupTeam")) { //cup_team 대회 참가팀 등록
            paramMap.put("cupTeamTB", cupTeamTB);
            params.put("cupTeamTB", cupTeamTB);
            params.put("cupInfoTB", cupInfoTB);

            cupId = params.get("cupId");
            paramMap.put("cupId", cupId);

            if (ageGroup.equals("U12") || ageGroup.equals("U11")) {
            paramMap.put("teamType", params.get("teamType"));
            }

            try {

                //0:예선+토너먼트,  1:예선+본선+토너먼트, 2: 풀리그, 3: 토너먼트
                if(cupType.equals("3")) {
                    if (excelContent.size() > 0) {
                        for(Map<String, String> map : excelContent) {
                            map.put("cupTeamTB", cupTeamTB);
                            map.put("cupId", cupId);
                            cupService.insertCupTeamExcelByOnlyTour(map); // 대회 참가팀 일괄 등록
                        }
                    }

                }else {
                    if (excelContent.size() > 0) {
                        for(Map<String, String> map : excelContent) {
                            map.put("cupTeamTB", cupTeamTB);
                            map.put("cupId", cupId);
                            if (ageGroup.equals("U12") || ageGroup.equals("U11")) {
                                map.put("teamType", params.get("teamType"));
                            }
                            cupService.insertCupTeamExcel(map); // 대회 참가팀 일괄 등록
                        }
                    }
                }


                recordLog(Define.STR_SUCCESS,
                    "[" + excelFlag + "] 대회 참가팀 정보 excel 일괄등록 - 성공 -params:" + params.toString()); // 접속하여 행위한 정보에대한
                // 기록

                // cupService.updateCupTeamExcelBycupId(params); // 대회 참가팀 리그 아이디 수정
                recordLog(Define.STR_SUCCESS,
                    "[" + excelFlag + "] 대회 참가팀 리그키 정보 일괄수정 - 성공 -params:" + params.toString()); // 접속하여 행위한 정보에대한
                // 기록

                transactionManager.commit(status);
            } catch (Exception e) {
                transactionManager.rollback(status);

                recordLog(Define.STR_ERROR, "[" + excelFlag + "] 대회 참가팀 정보 excel 일괄등록 - 실패 message:등록실패로 롤백 시킴."); // 접속하여
                // 행위한
                // 정보에대한
                // 기록
            }
            // 참가팀 아이디 조회 후 업데이트
            List<HashMap<String, Object>> list = cupService.selectCupTeamList(params); // 대회 참가팀 정보

            if(list.size() > 0) {
                for(Map<String, Object> map : list) {

                    map.put("cupTeamTB", cupTeamTB);
                    map.put("cupId", cupId);

                    cupService.updateCupTeamExcel_type2(map);

                }
            }


            model.addAttribute("ageGroup", ageGroup);
            model.addAttribute("cupId", cupId);
            model.addAttribute("sFlag", "1");

            excelFlag = "cupTeam";

        } else if (excelFlag.equals("cupSubMatch")) { // cupSubMatch 대회 예선 경기일정 등록
            paramMap.put("cupSubMatchTB", cupSubMatchTB);
            params.put("cupSubMatchTB", cupSubMatchTB);
            params.put("cupInfoTB", cupInfoTB);

            cupId = params.get("cupId");
            paramMap.put("cupId", cupId);
            params.put("cupId", cupId);

            String teamType = "";
            if (ageGroup.equals("U12") || ageGroup.equals("U11")) {
                // paramMap.put("teamType", params.get("teamType"));
                teamType = params.get("teamType");
            }

            try {
                // 대회 예선 경기일정 정보 조회 후 있으면 해당 경기일정 삭제
                HashMap<String, Object> countMap = cupService.selectCupSubMatchListCount(params);
                long totalCount = (Long) countMap.get("totalCount");
                logger.info(" ------ [Excel cupSubMatch]  totalCount : " + totalCount);
                if (totalCount > 0) {
                    cupService.deleteCupSubMatch(params); // 타겟 테이블 데이터 전체 삭제
                }
//TODO tour_type 분기

                if(excelContent.size() > 0) {
                    for (Map<String, String> map: excelContent) {
                        map.put("cupId", cupId);
                        map.put("cupSubMatchTB", cupSubMatchTB);
                        map.put("teamType", teamType);
                        cupService.insertCupSubMatchForExcel(map);
                    }
                }

                //cupService.insertCupSubMatchExcel(paramMap); // 대회 예선 경기일정 일괄 등록
                recordLog(Define.STR_SUCCESS,
                    "[" + excelFlag + "] 대회 예선  경기일정 정보 excel 일괄등록 - 성공 -params:" + params.toString()); // 접속하여 행위한
                // 정보에대한 기록

                //cupService.updateCupSubMatchExcelByCupId(params); // 대회 예선 경기일 리그 아이디 수정
                recordLog(Define.STR_SUCCESS,
                    "[" + excelFlag + "] 대회 예선  경기일정 리그키 정보 일괄수정 - 성공 -params:" + params.toString()); // 접속하여 행위한 정보에대한
                // 기록

                transactionManager.commit(status);
            } catch (Exception e) {
                transactionManager.rollback(status);

                recordLog(Define.STR_ERROR, "[" + excelFlag + "] 대회 예선  참가팀 정보 excel 일괄등록 - 실패 message:등록실패로 롤백 시킴."); // 접속하여
                // 행위한
                // 정보에대한
                // 기록
            }

            // 참가팀 아이디 조회 후 업데이트 - 홈팀
            List<HashMap<String, Object>> homeList = cupService.selectCupSubMatchListByHomeTeam(params); // 대회
            // 경기일정
            // 홈팀
            // 정보

            if(homeList.size() > 0) {
                for (Map<String, Object> map: homeList) {
                    map.put("cupSubMatchTB", cupSubMatchTB);
                    map.put("cupId", cupId);
                    cupService.updateCupSubMatchExcelHome(map);
                }
            }


            // 참가팀 아이디 조회 후 업데이트 - 어웨이팀
            List<HashMap<String, Object>> awayList = cupService.selectCupSubMatchListByAwayTeam(params); // 대회
            // 경기일정
            // 홈팀
            // 정보

            if(awayList.size() > 0) {
                for (Map<String, Object> map: awayList) {
                    map.put("cupSubMatchTB", cupSubMatchTB);
                    map.put("cupId", cupId);
                    cupService.updateCupSubMatchExcelAway(map);

                }
            }

            model.addAttribute("ageGroup", ageGroup);
            model.addAttribute("cupId", cupId);
            model.addAttribute("sFlag", "1");

            String cMgrFlag = params.get("cMgrFlag");
            if (!StrUtil.isEmpty(cMgrFlag)) {
                excelFlag = "cupMgrSubMatch";

            } else {
                excelFlag = "cupSubMatch";

            }



        } else if(excelFlag.equals("cupMainTeam")) { //cup_main_team 대회 본선 참가팀 등록
            paramMap.put("cupTeamTB", cupTeamTB);
            params.put("cupTeamTB", cupTeamTB);
            params.put("cupInfoTB", cupInfoTB);

            cupId = params.get("cupId");
            paramMap.put("cupId", cupId);

            try {

                if (excelContent.size() > 0) {
                    for (Map<String, String> map : excelContent) {
                        map.put("cupTeamTB", cupTeamTB);
                        map.put("cupInfoTB", cupInfoTB);
                        map.put("cupId", cupId);
                        cupService.updateCupMainTeam(map); // 대회 본선 참가팀 일괄 등록
                    }
                }

                recordLog(Define.STR_SUCCESS,
                    "[" + excelFlag + "] 대회 본선 참가팀 정보 excel 일괄등록 - 성공 -params:" + params.toString()); // 접속하여 행위한 정보에대한

                transactionManager.commit(status);
            } catch (Exception e) {
                e.printStackTrace();
                transactionManager.rollback(status);

                recordLog(Define.STR_ERROR, "[" + excelFlag + "] 대회 본선 참가팀 정보 excel 일괄등록 - 실패 message:등록실패로 롤백 시킴."); // 접속하여
                // 행위한
                // 정보에대한
                // 기록
            }

            model.addAttribute("ageGroup", ageGroup);
            model.addAttribute("cupId", cupId);
            model.addAttribute("sFlag", "1");

            excelFlag = "cupMainTeam";


        } else if (excelFlag.equals("cupMainMatch")) { // cupMainMatch 대회 본선 경기일정 등록
            paramMap.put("cupMainMatchTB", cupMainMatchTB);
            params.put("cupMainMatchTB", cupMainMatchTB);
            params.put("cupInfoTB", cupInfoTB);

            cupId = params.get("cupId");
            paramMap.put("cupId", cupId);
            params.put("cupId", cupId);


            try {
                // 대회 본선 경기일정 정보 조회 후 있으면 해당 경기일정 삭제
                HashMap<String, Object> countMap = cupService.selectCupMainMatchListCount(params);
                long totalCount = (Long) countMap.get("totalCount");
                logger.info(" ------ [Excel cupSubMatch]  totalCount : " + totalCount);
                if (totalCount > 0) {
                    cupService.deleteCupMainMatch(params); // 타겟 테이블 데이터 전체 삭제
                }

                if(excelContent.size() > 0) {
                    for (Map<String, String> map: excelContent) {
                        map.put("cupId", cupId);
                        map.put("cupMainMatchTB", cupMainMatchTB);
                        cupService.insertCupMainMatch(map);
                    }
                }

                //cupService.insertCupMainMatchExcel(paramMap); // 대회 본선 경기일정 일괄 등록
                recordLog(Define.STR_SUCCESS,
                    "[" + excelFlag + "] 대회 본선  경기일정 정보 excel 일괄등록 - 성공 -params:" + params.toString()); // 접속하여 행위한
                // 정보에대한 기록

                //cupService.updateCupMainMatchExcelByCupId(params); // 대회 본선 경기일 리그 아이디 수정
                recordLog(Define.STR_SUCCESS,
                    "[" + excelFlag + "] 대회 본선  경기일정 리그키 정보 일괄수정 - 성공 -params:" + params.toString()); // 접속하여 행위한 정보에대한
                // 기록

                transactionManager.commit(status);
            } catch (Exception e) {
                transactionManager.rollback(status);

                recordLog(Define.STR_ERROR, "[" + excelFlag + "] 대회 본선  참가팀 정보 excel 일괄등록 - 실패 message:등록실패로 롤백 시킴."); // 접속하여
                // 행위한
                // 정보에대한
                // 기록
            }

            // 참가팀 아이디 조회 후 업데이트 - 홈팀
            List<HashMap<String, Object>> homeList = cupService.selectCupMainMatchListByHomeTeam(params); // 대회
            // 경기일정
            // 홈팀
            // 정보

            if (homeList.size() > 0) {

                for (Map<String, Object> map: homeList) {
                    map.put("cupMainMatchTB", cupMainMatchTB);
                    map.put("cupId", cupId);
                    cupService.updateCupMainMatchExcelHome(map);
                }

            }


            // 참가팀 아이디 조회 후 업데이트 - 어웨이팀
            List<HashMap<String, Object>> awayList = cupService.selectCupMainMatchListByAwayTeam(params); // 대회
            // 경기일정
            // 홈팀
            // 정보

            if (awayList.size() > 0) {
                for(Map<String, Object> map : awayList) {
                    map.put("cupMainMatchTB", cupMainMatchTB);
                    map.put("cupId", cupId);
                    cupService.updateCupMainMatchExcelAway(map);
                }
            }

            model.addAttribute("ageGroup", ageGroup);
            model.addAttribute("cupId", cupId);
            model.addAttribute("sFlag", "1");

            String cMgrFlag = params.get("cMgrFlag");
            if (!StrUtil.isEmpty(cMgrFlag)) {
                excelFlag = "cupMgrMainMatch";

            } else {
                excelFlag = "cupMainMatch";

            }



        } else if (excelFlag.equals("cupTourMatch")) { // cupTourMatch 대회 토너먼트 경기일정 등록
            paramMap.put("cupTourMatchTB", cupTourMatchTB);
            params.put("cupTourMatchTB", cupTourMatchTB);
            params.put("cupInfoTB", cupInfoTB);

            cupId = params.get("cupId");
            paramMap.put("cupId", cupId);
            params.put("cupId", cupId);


            try {
                // 대회 토너먼트 경기일정 정보 조회 후 있으면 해당 경기일정 삭제
                HashMap<String, Object> countMap = cupService.selectCupTourMatchListCount(params);
                long totalCount = (Long) countMap.get("totalCount");
                logger.info(" ------ [Excel cupSubMatch]  totalCount : " + totalCount);
                if (totalCount > 0) {
                    cupService.deleteCupTourMatch(params); // 타겟 테이블 데이터 전체 삭제
                }

                if(excelContent.size() > 0) {
                    for (Map<String, String> map: excelContent) {
                        map.put("cupId", cupId);
                        map.put("cupTourMatchTB", cupTourMatchTB);
                        cupService.insertCupTourMatch(map);
                    }
                }

                //cupService.insertCupTourMatchExcel(paramMap); // 대회 토너먼트 경기일정 일괄 등록
                recordLog(Define.STR_SUCCESS,
                    "[" + excelFlag + "] 대회 토너먼트  경기일정 정보 excel 일괄등록 - 성공 -params:" + params.toString()); // 접속하여 행위한
                // 정보에대한 기록

                //cupService.updateCupTourMatchExcelByCupId(params); // 대회 토너먼트 경기일 리그 아이디 수정
                recordLog(Define.STR_SUCCESS,
                    "[" + excelFlag + "] 대회 토너먼트  경기일정 리그키 정보 일괄수정 - 성공 -params:" + params.toString()); // 접속하여 행위한 정보에대한
                // 기록

                transactionManager.commit(status);
            } catch (Exception e) {
                e.printStackTrace();
                transactionManager.rollback(status);

                recordLog(Define.STR_ERROR, "[" + excelFlag + "] 대회 토너먼트  참가팀 정보 excel 일괄등록 - 실패 message:등록실패로 롤백 시킴."); // 접속하여
                // 행위한
                // 정보에대한
                // 기록
            }
            try {
                List<HashMap<String, Object>> homeList = cupService.selectCupTourMatchListByHomeTeam(params); // 대회
                // 경기일정
                // 홈팀
                // 정보

                if (homeList.size() > 0) {
                    List<Object> cupTourMatchHomeList = makeLeagueTeamList(homeList);
                    HashMap<String, Object> hparams = new HashMap<String, Object>();
                    hparams.put("cupTourMatchTB", cupTourMatchTB);
                    hparams.put("cupId", cupId);
                    hparams.put("list", cupTourMatchHomeList);

                    for (Map<String, Object> map: homeList) {
                        map.put("cupTourMatchTB", cupTourMatchTB);
                        map.put("cupId", cupId);
                        cupService.updateCupTourMatchExcelHome(map);
                    }

                    //cupService.updateCupTourMatchExcel_home(hparams);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            // 참가팀 아이디 조회 후 업데이트 - 홈팀


            // 참가팀 아이디 조회 후 업데이트 - 어웨이팀
            List<HashMap<String, Object>> awayList = cupService.selectCupTourMatchListByAwayTeam(params); // 대회
            // 경기일정
            // 홈팀
            // 정보

            if (awayList.size() > 0) {
                List<Object> cupTourMatchAwayList = makeLeagueTeamList(awayList);
                HashMap<String, Object> aparams = new HashMap<String, Object>();
                aparams.put("cupTourMatchTB", cupTourMatchTB);
                aparams.put("cupId", cupId);
                aparams.put("list", cupTourMatchAwayList);

                for (Map<String, Object> map : awayList) {
                    map.put("cupTourMatchTB", cupTourMatchTB);
                    map.put("cupId", cupId);
                    cupService.updateCupTourMatchExcelAway(map);
                }

                //cupService.updateCupTourMatchExcel_away(aparams);
            }

            model.addAttribute("ageGroup", ageGroup);
            model.addAttribute("cupId", cupId);
            model.addAttribute("sFlag", "1");

            String cMgrFlag = params.get("cMgrFlag");
            if (!StrUtil.isEmpty(cMgrFlag)) {
                excelFlag = "cupMgrTourMatch";

            } else {
                excelFlag = "cupTourMatch";

            }
        }


        // 업로드를 진행하고 다시 지우기
        destFile.delete();

        return excelFlag.contains("league") ? "redirect:/" + excelFlag + "?ageGroup=" + ageGroup + "&leagueId=" + leagueId
                                            :
               StrUtil.isEmpty(params.get("teamType")) ? "redirect:/" + excelFlag + "?ageGroup=" + ageGroup + "&cupId=" + cupId
                                            : "redirect:/" + excelFlag + "?ageGroup=" + ageGroup + "&cupId=" + cupId + "&teamType=" + params.get("teamType");
    }

    @RequestMapping("excelDownload")
    @ResponseBody
    public void exceldownload_ajax(@RequestBody Map<String, String> params, HttpServletResponse response) throws Exception {
        logger.info("excelDownload was called. params:" + params);

        String ageGroup = params.get("ageGroup");
        String cupInfoTB = ageGroup + "_Cup_Info";
        String cupTeamTB = ageGroup + "_Cup_Team";
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        String cupResultTB = ageGroup + "_Cup_Result";

        String leagueInfoTB = ageGroup + "_League_Info";
        String leagueTeamTB = ageGroup + "_League_Team";
        String leagueMatchTB = ageGroup + "_League_Match";
        String leagueResultTB = ageGroup + "_League_Result";

        try {
            if(params.get("excelFlag").equals("Cup")) {
            }
            if(params.get("excelFlag").equals("League")) {

                params.put("leagueInfoTB", leagueInfoTB);
                params.put("leagueMatchTB", leagueMatchTB);

                List<HashMap<String, Object>> leagueMatchList = leagueService.selectLeagueMatchScheduleForExcel(params);

                excelCreate.createExcel(leagueMatchList, response);
            }
            if(params.get("excelFlag").equals("teamList")) {
                List<HashMap<String, Object>> teamList = teamService.selectTeamListForExcelDown(params);
                excelCreate.createExcelDownloadData(params, teamList, response);
            }
            if(params.get("excelFlag").equals("teamMgrList")) {
                params.put("cupResultTB", cupResultTB);
                params.put("cupInfoTB", cupInfoTB);
                params.put("leagueResultTB", leagueResultTB);
                params.put("leagueInfoTB", leagueInfoTB);

                List<HashMap<String, Object>> teamMgrList = teamService.selectTeamMgrListForExcelDown(params);
                excelCreate.createExcelDownloadData(params, teamMgrList, response);
            }
            if(params.get("excelFlag").equals("teamGroupList")) {
                List<HashMap<String, Object>> teamMgrList = teamService.selectTeamGroupListForExcelDown(params);
                excelCreate.createExcelDownloadData(params, teamMgrList, response);
            }
            if(params.get("excelFlag").equals("memberList")) {
                List<HashMap<String, Object>> memberList = memberService.selectMemberListForExcelDown(params);
                excelCreate.createExcelDownloadData(params, memberList, response);
            }
            if(params.get("excelFlag").equals("educationQuestion")) {
                List<HashMap<String, Object>> questionList = educationService.selectEducationQuestionListForExcelDown(params);
                excelCreate.createExcelDownloadData(params, questionList, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private List<Object> makeLeagueTeamList(List<HashMap<String, Object>> list) {
        List<Object> objList = new ArrayList<Object>();


        for (int i = 0; i < list.size(); i++) {
            HashMap<String, Object> map = list.get(i);
            map.put("teamId", map.get("team_id"));
            map.put("nickName", map.get("nick_name"));
            objList.add(map);
        }

        return objList;
    }

    private List<Object> makeCupMainTeamList(List<HashMap<String, Object>> list) {
        List<Object> objList = new ArrayList<Object>();

        for (int i = 0; i < list.size(); i++) {
            HashMap<String, Object> map = list.get(i);
            map.put("teamId", map.get("team_id"));
            map.put("nickName", map.get("nick_name"));
            objList.add(map);
        }

        return objList;
    }

}
