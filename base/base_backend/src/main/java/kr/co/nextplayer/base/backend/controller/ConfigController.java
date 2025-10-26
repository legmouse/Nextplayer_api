package kr.co.nextplayer.base.backend.controller;



import com.linecorp.armeria.internal.shaded.fastutil.Hash;
import kr.co.nextplayer.base.backend.dto.UageDto;
import kr.co.nextplayer.base.backend.model.Uage;
import kr.co.nextplayer.base.backend.model.User;
import kr.co.nextplayer.base.backend.service.*;
import kr.co.nextplayer.base.backend.util.DateUtil;
import kr.co.nextplayer.base.backend.util.Define;
import kr.co.nextplayer.base.backend.util.StrUtil;
import org.apache.ibatis.session.SqlSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Controller
public class ConfigController {
	private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private UageService uageService;

    @Resource
    private ConfigService configService;

    @Resource
    private CupService cupService;

    @Resource
    private LeagueService leagueService;

    @Resource
    private MediaService mediaService;

    @Resource
    private TeamService teamService;

    @Resource
    private PlayerService playerService;

    @Resource
    private BoardService boardService;

    @Resource
    private EducationService educationService;


	//설정 - 광역
	@RequestMapping(value="/area")	
	public String area(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
		logger.info("area was called. params:"+params);
		
		//연령대
		String ageGroup = params.get("ageGroup");
		if(StrUtil.isEmpty(ageGroup)) {
			params.put("ageGroup", "U18");
		}
		
		//검색
		String areaSearch = params.get("areaSearch");
		if(!StrUtil.isEmpty(areaSearch) && "1".equals(areaSearch)) {
			System.out.println("-- areaSearch : "+ areaSearch);
			
			String sUage = params.get("sUage");
			String sAreaName = params.get("sAreaName");
			ageGroup = sUage;
			params.put("ageGroup", ageGroup);
			
			model.addAttribute("areaSearch", areaSearch);
			model.addAttribute("sUage", sUage);
			model.addAttribute("sAreaName", sAreaName);
		}
		
		/* 페이징 처리 ------------------------------------------------------------------------------------------------*/
		int cp = StrUtil.getCurrentPage(params);	
		int cpp = Define.COUNT_PER_PAGE; //현재페이지 갯수
		
		HashMap<String, Object> areaCountMap = uageService.selectAreaListCount(params);
		long totalCount = (long) (Long) areaCountMap.get("totalCount");
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
		logger.info(" ------ [area list paging]  totalCount : " +totalCount + ", tp :"+ tp+", cp:"+cp+", start : "+ map.get("start")+", end : "+map.get("end"));
		
		model.addAttribute("start", map.get("start")); 
		model.addAttribute("end",  map.get("end")); 
		model.addAttribute("prev",  map.get("prev")); 
		model.addAttribute("next",  map.get("next")); 

		model.addAttribute("cp", cp); //현재페이지번호
		model.addAttribute("cpp", cpp); //현재페이지 갯수
		model.addAttribute("tp", tp); //총 페이지 번호
		model.addAttribute("tc", totalCount); //총 리스트 갯수
		/* 페이징 처리 ------------------------------------------------------------------------------------------------*/

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
		
		return "config/area";
	}
	
	/*
	 * 광역 - 등록.수정.삭제
	 */
	@RequestMapping(value = "/save_area", method = {RequestMethod.GET, RequestMethod.POST})	
	public String save_area(@RequestParam final Map<String, String> params, Model model) throws Exception {
		logger.info("request ----> save_area params : "+ params);
		String sFlag = params.get("sFlag");
		
		if(sFlag.equals(Define.MODE_ADD)){ //등록
			//중복조회
			HashMap<String, Object> areaCountMap = uageService.selectAreaCount(params);
			long count = (long) (Long) areaCountMap.get("count");
			
			if(count > 0) {
				String ageGroup = params.get("selUage");
				String code = String.valueOf(Define.DATA_DUPLICATE);
				String msg = params.get("areaName");
//				redirectAttributes.addAttribute("code", code);
//				redirectAttributes.addAttribute("msg", URLEncoder.encode(msg, "UTF-8"));
				
				model.addAttribute("code", code);
				model.addAttribute("ageGroup", ageGroup);
				model.addAttribute("msg", URLEncoder.encode(msg, "UTF-8"));
				return "config/area";
			}else {
				uageService.insertArea(params);
				
			}
			
			
//			recordLog(Define.STR_SUCCESS,"[학원.클럽.유스] 등록-성공 -params:"+params.toString()); //접속하여 행위한 정보에대한 기록
			
		}else if(sFlag.equals(Define.MODE_FIX)) {//수정
			uageService.updateArea(params);
			
		}else if(sFlag.equals(Define.MODE_DELETE)) {//삭제
            uageService.updateArea(params);
        }
		
		String page = params.get("page");
		if(!StrUtil.isEmpty(page)) {
			return "redirect:/"+page;
		}
		
		return "redirect:/area";
	}

    @RequestMapping(value="/interestAge")
    public String interestAge(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("area was called. params:"+params);

        //연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUseUageList();

        model.addAttribute("uageList", uageList);

        return "config/interestAge";
    }

    @RequestMapping(value="/save_interestAge")
    @ResponseBody
    public Map<String, Object> save_interestAge(@RequestBody Map<String, Object> params, Model model, HttpServletResponse resp) {
        logger.info("area was called. params:"+params);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        int result = 0;

        try {
            result = uageService.updateInterestAge(params);
            resultMap.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }

        resultMap.put("data", result);
        return resultMap;
    }

    @RequestMapping(value="/menuAuth")
    public String menuAuth(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("area was called. params:"+params);

        //연령 정보
        List<HashMap<String, Object>> managerList = configService.selectManagerList();

        model.addAttribute("managerList", managerList);

        return "config/menuAuth";
    }

    @RequestMapping(value = "update_pw")
    @ResponseBody
    public Map<String, Object> update_pw(@RequestParam Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("request ----> update_pw params : "+ params);

        int result = 0;

        try {
            result = configService.updatePw(params);
            resultMap.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }
        resultMap.put("data", result);
        return resultMap;
    }

    @RequestMapping(value = "update_manager")
    @ResponseBody
    public Map<String, Object> update_manager(@RequestParam Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("request ----> update_manager params : "+ params);

        int result = 0;

        try {

            String method = params.get("method");

            if (method.equals("Regist")) {
                result = configService.registManager(params);
            }

            if (method.equals("Modify")) {
                result = configService.modifyManager(params);
            }

            if (method.equals("Delete")) {
                result = configService.deleteManager(params);
            }

            resultMap.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }
        resultMap.put("data", result);
        return resultMap;
    }

    @RequestMapping(value = "menuAuthModify")
    public String menuAuthModify(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        logger.info("request ----> menuAuthModify params : "+ params);

        List<HashMap<String, Object>> upperMenuList = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> menuList = new ArrayList<HashMap<String, Object>>();
        String method = params.get("method");

        try {
            menuList = configService.selectMenuInfoList();
            upperMenuList = configService.selectUpperMenuList();

            if (method.equals("Modify")) {
                HashMap<String, Object> userInfo = configService.selectUserInfo(params);

                params.put("id", userInfo.get("id").toString());
                List<HashMap<String, Object>> userMenuList = configService.selectUserMenuList(params);

                model.addAttribute("userInfo", userInfo);
                model.addAttribute("userMenuList", userMenuList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("menuList", menuList);
        model.addAttribute("upperMenuList", upperMenuList);
        model.addAttribute("method", method);
        return "config/menuAuthModify";
    }

    @RequestMapping(value = "mainMenuConfig")
    public String mainMenuConfig(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        logger.info("request ----> mainMenuConfig params : "+ params);


        return "config/mainMenuConfig";
    }

    @RequestMapping(value = "/main_menu_config")
    public @ResponseBody Map<String, Object> main_menu_config () {
        Map<String, Object> result = new HashMap<>();

        List<HashMap<String, Object>> menuList = new ArrayList<HashMap<String, Object>>();

        try {

            menuList = configService.selectMainMenuList();
            result.put("menuList", menuList);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return result;
    }

    @RequestMapping(value = "/saveMainMenu.do", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> saveMainMenu(@RequestBody List<Map<String, String>> params) throws Exception {
        logger.info("request ----> saveMainMenu params : "+ params);

        Map<String, Object> map = new HashMap<>();

        int result = configService.updateMainMenu(params);
        map.put("result", result == 1 ? "success" : "fail");

        return map;
    }

    @RequestMapping(value = "mainMenuDet")
    public String mainMenuDet(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        logger.info("request ----> mainMenuConfig params : "+ params);

        String key = params.get("key");

        String sdate = DateUtil.getCurrentDate();
        params.put("sdate", sdate);

        List<HashMap<String, Object>> uageList = uageService.selectUseUageList();

        if (key.equals("leagueMatch") || key.equals("cupMatch")) {
            List<HashMap<String, Object>> menuList = configService.selectMainMenuList();
            model.addAttribute("menuList", menuList);

            List matchList = new ArrayList();

            if (key.equals("cupMatch")) {
                for (HashMap<String, Object> uage: uageList) {
                    String uageStr = uage.get("uage").toString();
                    String cupInfoTB = uageStr + "_Cup_Info";
                    String cupSubMatchTB = uageStr + "_Cup_Sub_Match";
                    String cupMainMatchTB = uageStr + "_Cup_Main_Match";
                    String cupTourMatchTB = uageStr + "_Cup_Tour_Match";

                    params.put("ageGroup", uage.get("uage").toString());
                    params.put("cupInfoTB", cupInfoTB);
                    params.put("cupSubMatchTB", cupSubMatchTB);
                    params.put("cupMainMatchTB", cupMainMatchTB);
                    params.put("cupTourMatchTB", cupTourMatchTB);

                    List<HashMap<String, Object>> cupMatch = cupService.selectCupMatchList(params);

                    if (cupMatch.size() > 0) {
                        for (Map<String, Object> map: cupMatch) {
                            matchList.add(map);
                        }
                    }
                }
            }

            if (key.equals("leagueMatch")) {
                for (HashMap<String, Object> uage: uageList) {
                    String uageStr = uage.get("uage").toString();
                    String leagueInfoTB = uageStr + "_League_Info";
                    String leagueMatchTB = uageStr + "_League_Match";

                    params.put("ageGroup", uage.get("uage").toString());
                    params.put("leagueInfoTB", leagueInfoTB);
                    params.put("leagueMatchTB", leagueMatchTB);

                    List<HashMap<String, Object>> leagueMatch = leagueService.selectListOfLeagueMatch(params);

                    if (leagueMatch.size() > 0) {
                        for (Map<String, Object> map : leagueMatch) {
                            matchList.add(map);
                        }
                    }
                }
            }

            model.addAttribute("matchList", matchList);
        }

        if (key.equals("progressCup")) {

            List cupList = new ArrayList();

            for (HashMap<String, Object> uage: uageList) {
                String uageStr = uage.get("uage").toString().toLowerCase();
                String cupInfoTB = uageStr + "_cup_info";

                params.put("ageGroup", uage.get("uage").toString());
                params.put("cupInfoTB", cupInfoTB);

                List<HashMap<String, Object>> progressCup = cupService.selectProgressCupInfo(params);

                if (progressCup.size() > 0) {
                    for (Map<String, Object> map : progressCup) {
                        cupList.add(map);
                    }
                }
            }

            model.addAttribute("cupList", cupList);
        }

        if (key.equals("progressLeague")) {
            List leagueList = new ArrayList();

            for (HashMap<String, Object> uage: uageList) {
                String uageStr = uage.get("uage").toString().toLowerCase();
                String leagueInfoTB = uageStr + "_league_info";
                String leagueTeamTB = uageStr + "_league_team";

                params.put("ageGroup", uage.get("uage").toString());
                params.put("leagueInfoTB", leagueInfoTB);
                params.put("leagueTeamTB", leagueTeamTB);

                List<HashMap<String, Object>> progressLeague = leagueService.selectProgressLeagueInfo(params);

                if (progressLeague.size() > 0) {
                    for (Map<String, Object> map : progressLeague) {
                        leagueList.add(map);
                    }
                }
            }

            model.addAttribute("leagueList", leagueList);
        }

        if (key.contains("media")) {

            String categoryType = "";

            if (key.contains("Video")) {
                categoryType = "M0001";
                params.put("mediaType", "Video");
            } else if (key.contains("News")) {
                categoryType = "M0002";
                params.put("mediaType", "News");
            } else if (key.contains("Blog")) {
                categoryType = "M0003";
                params.put("mediaType", "Blog");
            } else if (key.contains("Game")) {
                params.put("mediaType", "Game");
            }

            List<Map<String, Object>> mediaList = mediaService.selectMainMediaList(params);
            List<HashMap<String, Object>> menuList = uageService.selectCategoryMenu(categoryType);

            if (key.contains("News") || key.contains("Game")) {
                if (mediaList.size() > 0) {
                    for (Map<String, Object> media : mediaList) {
                        params.put("mediaId", media.get("media_id").toString());

                        List<HashMap<String, Object>> childList = mediaService.selectMediaChildList(params);

                        List<HashMap<String, Object>> cupInfoList = new ArrayList<>();
                        List<HashMap<String, Object>> leagueInfoList = new ArrayList<>();
                        List<HashMap<String, Object>> teamList = new ArrayList<HashMap<String, Object>>();

                        if (childList.size() > 0) {
                            for (HashMap<String, Object> child : childList) {
                                String parentTB = "";
                                String parentId = "";

                                HashMap<String, String> pParams = new HashMap<String, String>();

                                if (child.get("parentTable") != null) {
                                    parentTB = child.get("parentTable").toString();
                                }

                                if (child.get("parentId") != null) {
                                    parentId = child.get("parentId").toString();
                                }

                                if (parentTB.contains("Cup")) {
                                    pParams.put("cupInfoTB", parentTB);
                                    pParams.put("cupId", parentId);
                                    HashMap<String, Object> cupInfo = cupService.selectGetCupInfoForMedia(pParams);
                                    cupInfoList.add(cupInfo);
                                }

                                if (parentTB.contains("League")) {
                                    pParams.put("leagueInfoTB", parentTB);
                                    pParams.put("leagueId", parentId);
                                    HashMap<String, Object> leagueInfo = leagueService.selectGetLeagueInfoForMedia(pParams);
                                    leagueInfoList.add(leagueInfo);
                                }

                                if (parentTB.contains("Team")) {
                                    if (child.get("childId") != null) {
                                        String childId = "(" + child.get("childId").toString() + ")";
                                        pParams.put("childId", childId);
                                    }

                                    teamList = teamService.selectTeamListForMedia(pParams);
                                }

                            }

                            media.put("cupInfoList", cupInfoList);
                            media.put("leagueInfoList", leagueInfoList);
                            media.put("teamList", teamList);
                        }

                    }
                }
            }

            if (key.contains("Game")) {

                List liveList = new ArrayList();

                for (HashMap<String, Object> uage: uageList) {
                    String uageStr = uage.get("uage").toString();
                    String cupInfoTB = uageStr + "_Cup_Info";
                    String cupSubMatchTB = uageStr + "_Cup_Sub_Match";
                    String cupMainMatchTB = uageStr + "_Cup_Main_Match";
                    String cupTourMatchTB = uageStr + "_Cup_Tour_Match";

                    params.put("ageGroup", uage.get("uage").toString());
                    params.put("cupInfoTB", cupInfoTB);
                    params.put("cupSubMatchTB", cupSubMatchTB);
                    params.put("cupMainMatchTB", cupMainMatchTB);
                    params.put("cupTourMatchTB", cupTourMatchTB);

                    List<HashMap<String, Object>> cupMatch = cupService.selectCupMatchList(params);

                    if (cupMatch.size() > 0) {
                        for (Map<String, Object> map: cupMatch) {
                            liveList.add(map);
                        }
                    }
                }
                model.addAttribute("liveList", liveList);
            }

            model.addAttribute("mediaList", mediaList);
            model.addAttribute("menuList", menuList);
        }

        if (key.contains("roster")) {
            List<Map<String, Object>> rosterList = playerService.selectMainRosterList();
            model.addAttribute("rosterList", rosterList);
        }

        if (key.contains("banner")) {
            List<Map<String, Object>> bannerList = boardService.selectMainBannerList();
            model.addAttribute("bannerList", bannerList);
        }

        if (key.contains("column")) {

            String categoryType = "";
            List<HashMap<String, Object>> menuList = uageService.selectCategoryMenu("C0001");
            List<Map<String, Object>> columnList = educationService.selectMainColumnList();
            model.addAttribute("columnList", columnList);
            model.addAttribute("menuList", menuList);
        }

        model.addAttribute("key", key);
        return "config/mainMenuDet";
    }

    @RequestMapping("/save_show_point_flag")
    public @ResponseBody Map<String, Object> saveShowPointFlag(@RequestBody List<Map<String, String>> params) throws Exception {
        logger.info("request ----> saveShowPointFlag params : "+ params);

        Map<String, Object> map = new HashMap<>();


        int result = configService.saveShowPointFlag(params);
        map.put("result", result == 1 ? "success" : "fail");

        return map;
    }

    @RequestMapping("/save_show_flag")
    public @ResponseBody Map<String, Object> saveShowFlagProgressCup(@RequestBody List<Map<String, String>> params) throws Exception {
        logger.info("request ----> saveShowFlagProgressCup params : "+ params);

        Map<String, Object> map = new HashMap<>();


        int result = configService.saveShowFlagProgressCup(params);
        map.put("result", result == 1 ? "success" : "fail");

        return map;
    }

    @RequestMapping("/save_league_show_flag")
    public @ResponseBody Map<String, Object> saveShowFlagProgressLeague(@RequestBody List<Map<String, String>> params) throws Exception {
        logger.info("request ----> saveShowFlagProgressLeague params : "+ params);

        Map<String, Object> map = new HashMap<>();


        int result = configService.saveShowFlagProgressLeague(params);
        map.put("result", result == 1 ? "success" : "fail");

        return map;
    }

    @RequestMapping("/save_live_show_flag")
    public @ResponseBody Map<String, Object> saveLiveShowFlag(@RequestBody List<Map<String, String>> params) throws Exception {
        logger.info("request ----> saveLiveShowFlag params : "+ params);

        Map<String, Object> map = new HashMap<>();

        int result = configService.saveLiveShowFlag(params);
        map.put("result", result == 1 ? "success" : "fail");

        return map;
    }

    @RequestMapping("/search_media_data")
    public @ResponseBody Map<String, Object> searchMediaData(@RequestParam Map<String, String> param) throws Exception {
        logger.info("request ----> searchMediaData params : "+ param);

        Map<String, Object> map = new HashMap<>();

        String key = param.get("key");
        String categoryType = "";

        if (key.contains("Video")) {
            categoryType = "M0001";
            param.put("mediaType", "Video");
            param.put("subType", "All");
        } else if (key.contains("News")) {
            categoryType = "M0002";
            param.put("mediaType", "News");
        } else if (key.contains("Blog")) {
            categoryType = "M0003";
            param.put("mediaType", "Blog");
        } else if (key.contains("Game")) {
            param.put("mediaType", "Video");
            param.put("subType", "0");
        } else if (key.contains("column")) {
            categoryType = "C0001";
        }

        List<HashMap<String, Object>> menuList = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

        try {

            if (key.contains("media")) {
                menuList = uageService.selectCategoryMenu(categoryType);
                data = mediaService.selectMediaList(param);

                if (key.contains("News") || key.contains("Game")) {
                    if (data.size() > 0) {
                        for (Map<String, Object> media : data) {
                            param.put("mediaId", media.get("media_id").toString());

                            List<HashMap<String, Object>> childList = mediaService.selectMediaChildList(param);

                            List<HashMap<String, Object>> cupInfoList = new ArrayList<>();
                            List<HashMap<String, Object>> leagueInfoList = new ArrayList<>();
                            List<HashMap<String, Object>> teamList = new ArrayList<HashMap<String, Object>>();

                            if (childList.size() > 0) {
                                for (HashMap<String, Object> child : childList) {
                                    String parentTB = "";
                                    String parentId = "";

                                    HashMap<String, String> pParams = new HashMap<String, String>();

                                    if (child.get("parentTable") != null) {
                                        parentTB = child.get("parentTable").toString();
                                    }

                                    if (child.get("parentId") != null) {
                                        parentId = child.get("parentId").toString();
                                    }

                                    if (parentTB.contains("Cup")) {
                                        pParams.put("cupInfoTB", parentTB);
                                        pParams.put("cupId", parentId);
                                        HashMap<String, Object> cupInfo = cupService.selectGetCupInfoForMedia(pParams);
                                        cupInfoList.add(cupInfo);
                                    }

                                    if (parentTB.contains("League")) {
                                        pParams.put("leagueInfoTB", parentTB);
                                        pParams.put("leagueId", parentId);
                                        HashMap<String, Object> leagueInfo = leagueService.selectGetLeagueInfoForMedia(pParams);
                                        leagueInfoList.add(leagueInfo);
                                    }

                                    if (parentTB.contains("Team")) {
                                        if (child.get("childId") != null) {
                                            String childId = "(" + child.get("childId").toString() + ")";
                                            pParams.put("childId", childId);
                                        }

                                        teamList = teamService.selectTeamListForMedia(pParams);
                                    }

                                }

                                media.put("cupInfoList", cupInfoList);
                                media.put("leagueInfoList", leagueInfoList);
                                media.put("teamList", teamList);
                            }

                        }
                    }
                }
            } else if (key.equals("roster")) {
                data = playerService.selectRosterPlayerList(param);
            } else if (key.equals("banner")) {
                data = boardService.selectBannerList(param);
            } else if (key.equals("column")) {
                menuList = uageService.selectCategoryMenu(categoryType);
                data = educationService.selectColumnList(param);
            }


            map.put("state", "success");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state", "fail");
        }

        map.put("data", data);
        map.put("menuList", menuList);
        return map;
    }

    @RequestMapping("/save_main_media_order")
    public @ResponseBody Map<String, Object> saveMainMediaOrder(@RequestBody Map<String, String> params) throws Exception {
        logger.info("request ----> saveMainMediaOrder params : "+ params);

        Map<String, Object> map = new HashMap<>();


        int result = configService.saveMainMediaOrder(params);
        map.put("result", result == 1 ? "success" : "fail");

        return map;
    }

    @RequestMapping("/save_main_roster_order")
    public @ResponseBody Map<String, Object> saveMainRosterOrder(@RequestBody List<Map<String, String>> params) throws Exception {
        logger.info("request ----> saveMainRosterOrder params : "+ params);

        Map<String, Object> map = new HashMap<>();


        int result = configService.saveMainRosterOrder(params);
        map.put("result", result == 1 ? "success" : "fail");

        return map;
    }

    @RequestMapping("/save_main_banner_order")
    public @ResponseBody Map<String, Object> saveMainBannerOrder(@RequestBody Map<String, String> params) throws Exception {
        logger.info("request ----> saveMainBannerOrder params : "+ params);

        Map<String, Object> map = new HashMap<>();


        int result = configService.saveMainBannerOrder(params);
        map.put("result", result == 1 ? "success" : "fail");

        return map;
    }

    @RequestMapping("/save_main_column_order")
    public @ResponseBody Map<String, Object> saveMainColumnOrder(@RequestBody Map<String, String> params) throws Exception {
        logger.info("request ----> saveMainColumnOrder params : "+ params);

        Map<String, Object> map = new HashMap<>();


        int result = configService.saveMainColumnOrder(params);
        map.put("result", result == 1 ? "success" : "fail");

        return map;
    }

    @RequestMapping("/subBannerList")
    public String subBannerList(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        logger.info("request ----> subBannerList params : "+ params);

        return "config/subBanner/list";
    }

    @RequestMapping(value = "/banner_config")
    public @ResponseBody Map<String, Object> banner_config () {
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> bannerConfigList = new ArrayList<Map<String, Object>>();

        try {

            bannerConfigList = boardService.selectBannerConfigList();
            result.put("bannerConfigList", bannerConfigList);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return result;
    }

    @RequestMapping(value = "/saveBannerConfig.do", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> saveBannerConfig(@RequestBody List<Map<String, String>> params) throws Exception {
        logger.info("request ----> saveBannerConfig params : "+ params);

        Map<String, Object> map = new HashMap<>();

        int result = boardService.updateBannerConfig(params);
        map.put("result", result == 1 ? "success" : "fail");

        return map;
    }

    @RequestMapping(value = "subBannerDet")
    public String subBannerDet(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        logger.info("request ----> subBannerDet params : "+ params);

        String configKey = params.get("configKey");
        String bannerConfigId = params.get("bannerConfigId");
        String sdate = DateUtil.getCurrentDate();
        params.put("sdate", sdate);


        List<Map<String, Object>> bannerList = boardService.selectSubBannerList(params);
        model.addAttribute("bannerList", bannerList);


        model.addAttribute("configKey", configKey);
        model.addAttribute("bannerConfigId", bannerConfigId);
        return "config/subBanner/detail";
    }

    @RequestMapping("/save_sub_banner")
    public @ResponseBody Map<String, Object> saveSubBanner(@RequestBody Map<String, String> params) throws Exception {
        logger.info("request ----> saveSubBanner params : "+ params);

        Map<String, Object> map = new HashMap<>();


        int result = boardService.saveSubBanner(params);
        map.put("result", result == 1 ? "success" : "fail");

        return map;
    }

//    @RequestMapping(value = "contestPush")
//    public String contestPush(@RequestParam Map<String, String> params, Model model, HttpSession session) {
//        logger.info("cupMgr was called. params:" + params);
//
//        // 연령대
//        String ageGroup = params.get("ageGroup");
//        if (StrUtil.isEmpty(ageGroup)) {
//            ageGroup = "U18";
//            params.put("ageGroup", ageGroup);
//        }
//
//        // 연령별 테이블
//        String cupInfoTB = ageGroup + "_Cup_Info";
//        params.put("cupInfoTB", cupInfoTB);
//        String cupTeamTB = ageGroup + "_Cup_Team";
//        params.put("cupTeamTB", cupTeamTB);
//
//        // 대회 예선 경기일정 정보 연령별 테이블
//        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
//        params.put("cupSubMatchTB", cupSubMatchTB);
//        // 대회 본선 경기일정 정보 연령별 테이블
//        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
//        params.put("cupMainMatchTB", cupMainMatchTB);
//        // 대회 토너먼트(knockout stage) 경기일정 정보 연령별 테이블
//        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
//        params.put("cupTourMatchTB", cupTourMatchTB);
//
//        //대회 결과 연령별 테이블
//        String cupResultTB = ageGroup + "_Cup_Result";
//        params.put("cupResultTB", cupResultTB);
//
//        // 검색
//        String sCupName = params.get("sCupName");
//        if (!StrUtil.isEmpty(sCupName)) {
//            model.addAttribute("sCupName", sCupName);
//        }
//
//        /*
//         * 페이징 처리
//         * -----------------------------------------------------------------------------
//         * -------------------
//         */
//        int cp = StrUtil.getCurrentPage(params);
//        int cpp = Define.COUNT_PER_PAGE; // 현재페이지 갯수
//
//        HashMap<String, Object> countMap = cupService.selectCupInfoListCount(params);
//        long totalCount = (Long) countMap.get("totalCount");
//
//        int tp = 1;
//        if (totalCount > 0) {
//            tp = (int) totalCount / cpp;
//            if ((totalCount % cpp) > 0) {
//                tp += 1;
//            }
//        }
//
//        int sRow = (cp - 1) * cpp;
//
//        params.put("sRow", "" + sRow);
//        params.put("eCount", "" + cpp);
//
//        HashMap<String, Object> map = StrUtil.calcPage(cp, totalCount, Define.COUNT_PAGE);
//        logger.info(" ------ [team list paging]  totalCount : " + totalCount + ", tp :" + tp + ", cp:" + cp
//            + ", start : " + map.get("start") + ", end : " + map.get("end"));
//
//        model.addAttribute("start", map.get("start"));
//        model.addAttribute("end", map.get("end"));
//        model.addAttribute("prev", map.get("prev"));
//        model.addAttribute("next", map.get("next"));
//
//        model.addAttribute("cp", cp); // 현재페이지번호
//        model.addAttribute("cpp", cpp); // 현재페이지 갯수
//        model.addAttribute("tp", tp); // 총 페이지 번호
//        model.addAttribute("tc", totalCount); // 총 리스트 갯수
//        /*
//         * 페이징 처리
//         * -----------------------------------------------------------------------------
//         * -------------------
//         */
//
//        // 연령별 대회 리스트 getting league list
//        //cupInfoList
//        List<HashMap<String, Object>> cupMgrList = cupService.selectCupMgrList(params);
//
//
//        // 연령 정보
//        List<HashMap<String, Object>> uageList = uageService.selectUageList();
//
//        model.addAttribute("cupMgrList", cupMgrList);
//        model.addAttribute("uageList", uageList);
//
//        model.addAttribute("ageGroup", ageGroup);
//        model.addAttribute("sYear", params.get("sYear"));
//        model.addAttribute("sCupName", params.get("sCupName"));
//        return "config/contestPush";
//    }
    
//    @RequestMapping("/alrim")
//    public String alrimList(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp){
//        logger.info("alrimListr was called. params:" + params);
//        
//        List<HashMap<String, Object>> pushList = configService.selectPushInfoList();
//        
//        List<HashMap<String, Object>> uageList = uageService.selectUseUageList();
//        
//        model.addAttribute("pushList", pushList);
//        model.addAttribute("uageList", uageList);
//
////        HashMap<String, Object> bannerInfo = boardService.selectBannerInfo(params);
////        model.addAttribute("bannerInfo", bannerInfo);
//
//        return "alrim/list";
//    }
//    
//    
//    @RequestMapping("/selectCupMatch")
//    public String contestPush(@RequestParam Map<String, String> params, Model model, HttpSession session) {
//        logger.info("selectCupMatch was called. params:" + params);
//        
//        String sdate = params.get("sdate");
//        if (StrUtil.isEmpty(sdate)) {
//            sdate = DateUtil.getCurrentDate();
//        }
//
//        params.put("sdate", sdate);
//
//        try {
//
//            List<HashMap<String, Object>> uageList = uageService.selectUseUageList();
//
//            for (HashMap<String, Object> uage: uageList) {
//                String uageStr = uage.get("uage").toString();
//                String cupInfoTB = uageStr + "_Cup_Info";
//                String cupSubMatchTB = uageStr + "_Cup_Sub_Match";
//                String cupMainMatchTB = uageStr + "_Cup_Main_Match";
//                String cupTourMatchTB = uageStr + "_Cup_Tour_Match";
//
//                params.put("cupInfoTB", cupInfoTB);
//                params.put("cupSubMatchTB", cupSubMatchTB);
//                params.put("cupMainMatchTB", cupMainMatchTB);
//                params.put("cupTourMatchTB", cupTourMatchTB);
//
//                List<HashMap<String, Object>> cupMatch = cupService.selectCupMatchList(params);
//
//                model.addAttribute(uageStr + "CupMatch", cupMatch);
//            }
//
//            model.addAttribute("sdate", sdate);
//            
//            String method = params.get("method");
//            String text = params.get("bodyText");
//            model.addAttribute("method", method);
//            model.addAttribute("bodyText", text);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return "alrim/cupMatchList";
//    }
//
//    @PostMapping("/insertContestPush")
//    public @ResponseBody Map<String, Object> insertContestPush(@RequestBody Map<String, String> params) throws Exception {
//        logger.info("request ----> insertContestPush params : "+ params);
//
//        Map<String, Object> map = new HashMap<>();
//
//
//        int result = configService.insertInterestTeamMemberPush(params);
//        if (result > 0) {
//        	String matchType = params.get("matchType");
//        	String matchTB = "";
//        	switch(matchType) {
//        		case "SUB" :
//        			matchTB = params.get("uage") + "_cup_sub_match";
//        			params.put("matchTB", matchTB);
//        			configService.updateSubMatchSendFlag(params);
//        			break;
//        		case "MAIN" :
//        			matchTB = params.get("uage") + "_cup_main_match";
//        			params.put("matchTB", matchTB);
//        			configService.updateMainMatchSendFlag(params);
//        			break;
//        		case "TOUR" :
//        			matchTB = params.get("uage") + "_cup_tour_match";
//        			params.put("matchTB", matchTB);
//        			configService.updateTourMatchSendFlag(params);
//        			break;
//        	}
//        }
//        map.put("result", result > 0 ? "success" : "fail");
//
//        return map;
//    }
//    
//    @PostMapping("/insertCupPush")
//    public @ResponseBody Map<String, Object> insertCupPush(@RequestBody Map<String, String> params) throws Exception {
//        logger.info("request ----> insertCupPush params : "+ params);
//
//        Map<String, Object> map = new HashMap<>();
//
//        
//        int result = configService.insertInterestAgeGorupMemberPush(params);
//        if (result > 0) {
//        	String cupInfoTB = params.get("uage") + "_cup_info";
//        	params.put("cupInfoTB", cupInfoTB);
//        	configService.updateCupSendFlag(params);
//        }
//        map.put("result", result > 0 ? "success" : "fail");
//
//        return map;
//    }
//    
//    @PostMapping("/insertPush")
//    public @ResponseBody Map<String, Object> insertPush(@RequestBody Map<String, String> params) throws Exception {
//        logger.info("request ----> insertContestPush params : "+ params);
//
//        Map<String, Object> map = new HashMap<>();
//
//
//        int result = configService.insertMemberPush(params);
//        map.put("result", result > 0 ? "success" : "fail");
//
//        return map;
//    }
    
//    @PostMapping("/insertInterestTeamPush")
//    public @ResponseBody Map<String, Object> insertInterestTeamPush(@RequestBody Map<String, String> params) throws Exception {
//        logger.info("request ----> insertInterestTeamPush params : "+ params);
//
//        Map<String, Object> map = new HashMap<>();
//
//
//        int result = configService.insertInterestTeamMemberPush(params);
//        map.put("result", result == 1 ? "success" : "fail");
//
//        return map;
//    }
    
//    @RequestMapping(value = "/push_info")
//    @ResponseBody
//    public Map<String, Object> push_info(@RequestParam Map<String, String> params) {
//        Map<String, Object> resultMap = new HashMap<String, Object>();
//        logger.info("request ----> push_info params : "+ params);
//        
//        HashMap<String, Object> pushInfo = configService.selectPushInfo(params);
//        
//        resultMap.put("data", pushInfo);
//        return resultMap;
//    }
//    
//    @RequestMapping(value = "/update_push")
//    @ResponseBody
//    public Map<String, Object> update_push(@RequestParam Map<String, String> params) {
//        Map<String, Object> resultMap = new HashMap<String, Object>();
//        logger.info("request ----> update_push params : "+ params);
//        
//        int result = 0;
//
//        try {
//        	result = configService.updatePush(params);
//
//            resultMap.put("state", "SUCCESS");
//        } catch (Exception e) {
//            e.printStackTrace();
//            resultMap.put("state", "ERROR");
//        }
//        resultMap.put("data", result);
//        
//        return resultMap;
//    }
//    
//    @RequestMapping(value = "/cupSelectList")
//    @ResponseBody
//    public Map<String, Object> cupSelectList(@RequestParam Map<String, String> params) {
//        Map<String, Object> resultMap = new HashMap<String, Object>();
//        // logger.info("request ----> cupSelectList params : "+ params);
//        
//        String ageGroup = params.get("ageGroup");
//        if (StrUtil.isEmpty(ageGroup)) {
//            ageGroup = "U18";
//            params.put("ageGroup", ageGroup);
//        }
//
//        // 연령별 테이블
//        String cupInfoTB = ageGroup + "_Cup_Info";
//        params.put("cupInfoTB", cupInfoTB);
//        String cupTeamTB = ageGroup + "_Cup_Team";
//        params.put("cupTeamTB", cupTeamTB);
//
//        // 대회 예선 경기일정 정보 연령별 테이블
//        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
//        params.put("cupSubMatchTB", cupSubMatchTB);
//        // 대회 본선 경기일정 정보 연령별 테이블
//        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
//        params.put("cupMainMatchTB", cupMainMatchTB);
//        // 대회 토너먼트(knockout stage) 경기일정 정보 연령별 테이블
//        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
//        params.put("cupTourMatchTB", cupTourMatchTB);
//        
//        List<HashMap<String, Object>> cupInfoList = cupService.selectCupInfoList(params);
//        logger.info("params : "+ params);
//        logger.info("cupInfoList : "+ cupInfoList);
//        resultMap.put("data", cupInfoList);
//        
//        return resultMap;
//    }
//    
//    @RequestMapping(value = "/addPush")
//    @ResponseBody
//    public Map<String, Object> addPush(@RequestParam Map<String, String> params) {
//        Map<String, Object> resultMap = new HashMap<String, Object>();
//        // logger.info("request ----> cupSelectList params : "+ params);
//        
//        int result = 0;
//
//        try {
//        	result = configService.insertPush(params);
//
//            resultMap.put("state", "SUCCESS");
//        } catch (Exception e) {
//            e.printStackTrace();
//            resultMap.put("state", "ERROR");
//        }
//        resultMap.put("data", result);
//        
//        return resultMap;
//    }

}
