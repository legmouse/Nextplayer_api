package kr.co.nextplayer.base.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.linecorp.armeria.internal.shaded.fastutil.Hash;
import kr.co.nextplayer.base.backend.dto.BlogResultDto;
import kr.co.nextplayer.base.backend.dto.NewsResultDto;
import kr.co.nextplayer.base.backend.dto.YoutubeResultDto;
import kr.co.nextplayer.base.backend.model.Cup;
import kr.co.nextplayer.base.backend.service.*;
import kr.co.nextplayer.base.backend.util.DateUtil;
import kr.co.nextplayer.base.backend.util.Define;
import kr.co.nextplayer.base.backend.util.StrUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import static kr.co.nextplayer.base.backend.util.DateUtil.YYYY;

@Controller
public class MediaController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private MediaService mediaService;

    @Resource
    private UageService uageService;

    @Resource
    private CupService cupService;

    @Resource
    private LeagueService leagueService;

    @Resource
    private TeamService teamService;

    @RequestMapping(value = "/media{mediaType}")
    public String media(@RequestParam Map<String, String> params, @PathVariable String mediaType, Model model, HttpServletResponse resp) {
        logger.info("media was called. params:" + params);
        logger.info("mediaType:" + mediaType);

        if (StrUtil.isEmpty(mediaType)) {
            mediaType = "Video";
        }

        String subType = params.get("subType");
        if (StrUtil.isEmpty(subType)) {
            subType = "All";
            params.put("subType", subType);
        }

        model.addAttribute("subType", subType);

        model.addAttribute("mediaType", mediaType);
        params.put("mediaType", mediaType);

        String categoryType = "";

        if (mediaType.equals("Video")) {
            categoryType = "M0001";
        } else if (mediaType.equals("News")) {
            categoryType = "M0002";
        } else if (mediaType.equals("Blog")) {
            categoryType = "M0003";
        }

        List<HashMap<String, Object>> menuList = uageService.selectCategoryMenu(categoryType);
        model.addAttribute("menuList", menuList);

        if (mediaType.equals("Game")) {
            mediaType = "Video";
            subType = "0";

            params.put("mediaType", mediaType);
            params.put("subType", subType);
        }

        int totalCnt = mediaService.selectMediaListCnt(params);

        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; // 현재페이지 갯수

        int tp = 1;
        if (totalCnt > 0) {
            tp = (int) totalCnt / cpp;
            if ((totalCnt % cpp) > 0) {
                tp += 1;
            }
        }

        int sRow = (cp - 1) * cpp;

        params.put("sRow", "" + sRow);
        params.put("eCount", "" + cpp);

        HashMap<String, Object> map = StrUtil.calcPage(cp, totalCnt, Define.COUNT_PAGE);

        model.addAttribute("start", map.get("start"));
        model.addAttribute("end", map.get("end"));
        model.addAttribute("prev", map.get("prev"));
        model.addAttribute("next", map.get("next"));

        model.addAttribute("cp", cp); // 현재페이지번호
        model.addAttribute("cpp", cpp); // 현재페이지 갯수
        model.addAttribute("tp", tp); // 총 페이지 번호
        model.addAttribute("tc", totalCnt); // 총 리스트 갯수

        List<HashMap<String, Object>> mediaList = mediaService.selectMediaList(params);

        if ((mediaType.equals("Video") && subType.equals("0")) || mediaType.equals("News")) {
            for (HashMap<String, Object> media : mediaList) {
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

        List<HashMap<String, Object>> creatorList = new ArrayList<HashMap<String, Object>>();

        if (!mediaType.equals("Game")) {
            creatorList = mediaService.selectCreatorList(params);
        }
        model.addAttribute("creatorList", creatorList);

        model.addAttribute("mediaList", mediaList);
        model.addAttribute("params", params);

        return "media/media";
    }

    @RequestMapping(value = "/detail{mediaType}")
    public String detail(@RequestParam Map<String, String> params, @PathVariable String mediaType, Model model, HttpServletResponse resp) {
        logger.info("detail was called. params:" + params);
        logger.info("mediaType:" + mediaType);

        if (StrUtil.isEmpty(mediaType)) {
            mediaType = "Video";
        }

        model.addAttribute("mediaType", mediaType);
        params.put("mediaType", mediaType);

        model.addAttribute("subType", params.get("subType"));

        /*if (mediaType.equals("Game")) {
            mediaType = "Video";
            subType = "0";

            params.put("mediaType", mediaType);
            params.put("subType", subType);
        }*/

        HashMap<String, Object> mediaInfo = mediaService.selectMediaInfo(params);

        model.addAttribute("mediaInfo", mediaInfo);

        List<HashMap<String, Object>> childList = mediaService.selectMediaChildList(params);

        String categoryType = "";

        if (mediaType.equals("Video")) {
            categoryType = "M0001";
        } else if (mediaType.equals("News")) {
            categoryType = "M0002";
        } else if (mediaType.equals("Blog")) {
            categoryType = "M0003";
        }

        List<HashMap<String, Object>> menuList = uageService.selectCategoryMenu(categoryType);
        model.addAttribute("menuList", menuList);

        List<HashMap<String, Object>> cupData = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> cupResult = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> leagueResult = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> teamData = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> teamResult = new ArrayList<HashMap<String, Object>>();

        for (int i = 0; i < childList.size(); i++) {

            HashMap<String, String> pParams = new HashMap<String, String>();

            String parentTB = "";
            String childTB = "";

            if (mediaType.equals("Game")) {

                if (childList.get(i).get("parentTable") != null) {
                    parentTB = childList.get(i).get("parentTable").toString();
                    pParams.put("parentTB", parentTB);
                }

                if (childList.get(i).get("parentId") != null) {
                    String parentId = childList.get(i).get("parentId").toString();
                    pParams.put("parentId", parentId);
                }

                if (childList.get(i).get("childTable") != null) {
                    childTB = childList.get(i).get("childTable").toString();
                    pParams.put("childTB", childTB);
                }

                if (childList.get(i).get("childId") != null) {
                    String childId = "(" + childList.get(i).get("childId").toString() + ")";
                    pParams.put("childId", childId);
                }


                String childKey = "";

                if (childTB.contains("Cup_Sub")) {
                    childKey = "a.cup_sub_match_id";
                } else if (childTB.contains("Cup_Main")) {
                    childKey = "a.cup_main_match_id";
                } else if (childTB.contains("Cup_Tour")) {
                    childKey = "a.cup_tour_match_id";
                }

                if (childTB.contains("League")) {
                    childKey = "a.league_match_id";
                }


                pParams.put("childKey", childKey);

                if (parentTB.contains("Cup")) {
                    cupData = cupService.selectCupMatchListForMedia(pParams);
                    cupResult.addAll(cupData);
                }

                if (parentTB.contains("League")) {
                    leagueResult = leagueService.selectLeagueMatchListForMedia(pParams);
                }

                if (parentTB.contains("Team")) {
                    teamData = teamService.selectTeamListForMedia(pParams);
                    teamResult.addAll(teamData);
                }
            }

            if (mediaType.equals("News")) {

                String parentId = "";

                if (childList.get(i).get("parentTable") != null) {
                    parentTB = childList.get(i).get("parentTable").toString();

                }

                if (childList.get(i).get("parentId") != null) {
                    parentId = childList.get(i).get("parentId").toString();
                }

                if (childList.get(i).get("childId") != null) {
                    String childId = "(" + childList.get(i).get("childId").toString() + ")";
                    pParams.put("childId", childId);
                }

                if (parentTB.contains("Cup")) {
                    pParams.put("cupInfoTB", parentTB);
                    pParams.put("cupId", parentId);
                    HashMap<String, Object> result = cupService.selectGetCupInfoForMedia(pParams);
                    cupResult.add(result);
                }

                if (parentTB.contains("League")) {
                    pParams.put("leagueInfoTB", parentTB);
                    pParams.put("leagueId", parentId);
                    HashMap<String, Object> result = leagueService.selectGetLeagueInfoForMedia(pParams);
                    leagueResult.add(result);
                }

                if (parentTB.contains("Team")) {
                    teamData = teamService.selectTeamListForMedia(pParams);
                    teamResult.addAll(teamData);
                }
            }

        }

        System.out.println("cupResult = " + cupResult);
        System.out.println("leagueResult = " + leagueResult);

        model.addAttribute("cupMediaList", cupResult);
        model.addAttribute("leagueMediaList", leagueResult);
        model.addAttribute("teamMediaList", teamResult);

        return "media/detail";
    }

    @RequestMapping(value = "/regist{mediaType}")
    public String regist(@RequestParam Map<String, String> params, @PathVariable String mediaType, Model model, HttpServletResponse resp) {
        logger.info("media was called. params:" + params);
        logger.info("mediaType:" + mediaType);

        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        if (StrUtil.isEmpty(mediaType)) {
            mediaType = "Video";
        }

        List<HashMap<String, Object>> creatorList = uageService.selectCreatorList(mediaType);
        model.addAttribute("creatorList", creatorList);

        String categoryType = "";

        if (mediaType.equals("Video")) {
            categoryType = "M0001";
        } else if (mediaType.equals("News")) {
            categoryType = "M0002";
        } else if (mediaType.equals("Blog")) {
            categoryType = "M0003";
        }

        List<HashMap<String, Object>> menuList = uageService.selectCategoryMenu(categoryType);
        model.addAttribute("menuList", menuList);

        model.addAttribute("mediaType", mediaType);
        model.addAttribute("uageList", uageList);
        params.put("mediaType", mediaType);
        model.addAttribute("method", "regist");

        return "media/modify";
    }

    @RequestMapping(value = "/modify{mediaType}")
    public String modify(@RequestParam Map<String, String> params, @PathVariable String mediaType, Model model, HttpServletResponse resp) {
        logger.info("media was called. params:" + params);
        logger.info("mediaType:" + mediaType);

        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        if (StrUtil.isEmpty(mediaType)) {
            mediaType = "Video";
        }

        List<HashMap<String, Object>> creatorList = uageService.selectCreatorList(mediaType);
        model.addAttribute("creatorList", creatorList);

        String categoryType = "";

        if (mediaType.equals("Video")) {
            categoryType = "M0001";
        } else if (mediaType.equals("News")) {
            categoryType = "M0002";
        } else if (mediaType.equals("Blog")) {
            categoryType = "M0003";
        }

        List<HashMap<String, Object>> menuList = uageService.selectCategoryMenu(categoryType);
        model.addAttribute("menuList", menuList);

        model.addAttribute("mediaType", mediaType);
        model.addAttribute("uageList", uageList);
        params.put("mediaType", mediaType);

        model.addAttribute("mediaType", mediaType);
        params.put("mediaType", mediaType);

        HashMap<String, Object> mediaInfo = mediaService.selectMediaInfo(params);

        model.addAttribute("mediaInfo", mediaInfo);

        List<HashMap<String, Object>> childList = mediaService.selectMediaChildList(params);

        List<HashMap<String, Object>> cupData = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> cupResult = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> leagueResult = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> teamData = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> teamResult = new ArrayList<HashMap<String, Object>>();

        for (int i = 0; i < childList.size(); i++) {

            HashMap<String, String> pParams = new HashMap<String, String>();

            String parentTB = "";
            String childTB = "";

            if (mediaType.equals("Game")) {
                if (childList.get(i).get("parentTable") != null) {
                    parentTB = childList.get(i).get("parentTable").toString();
                    pParams.put("parentTB", parentTB);
                }

                if (childList.get(i).get("parentId") != null) {
                    String parentId = childList.get(i).get("parentId").toString();
                    pParams.put("parentId", parentId);
                }

                if (childList.get(i).get("childTable") != null) {
                    childTB = childList.get(i).get("childTable").toString();
                    pParams.put("childTB", childTB);
                }

                if (childList.get(i).get("childId") != null) {
                    String childId = "(" + childList.get(i).get("childId").toString() + ")";
                    pParams.put("childId", childId);
                }

                String childKey = "";

                if (childTB.contains("Cup_Sub")) {
                    childKey = "a.cup_sub_match_id";
                } else if (childTB.contains("Cup_Main")) {
                    childKey = "a.cup_main_match_id";
                } else if (childTB.contains("Cup_Tour")) {
                    childKey = "a.cup_tour_match_id";
                }

                if (childTB.contains("League")) {
                    childKey = "a.league_match_id";
                }


                pParams.put("childKey", childKey);

                if (parentTB.contains("Cup")) {
                    cupData = cupService.selectCupMatchListForMedia(pParams);
                    String cupType = childList.get(i).get("childTable").toString();
                    HashMap<String, Object> cupTypeMap = new HashMap<>();
                    //cupTypeMap.put("cupType", cupType);
                    //cupData.add(cupTypeMap);

                    for (int j = 0; j < cupData.size(); j++) {
                        cupData.get(j).put("cupType", cupType);
                    }
                    cupResult.addAll(cupData);
                }

                if (parentTB.contains("League")) {
                    leagueResult = leagueService.selectLeagueMatchListForMedia(pParams);
                }

                if (parentTB.contains("Team")) {
                    teamData = teamService.selectTeamListForMedia(pParams);
                    teamResult.addAll(teamData);
                }
            }

            if (mediaType.equals("News")) {

                String parentId = "";

                if (childList.get(i).get("parentTable") != null) {
                    parentTB = childList.get(i).get("parentTable").toString();
                }

                if (childList.get(i).get("parentId") != null) {
                    parentId = childList.get(i).get("parentId").toString();
                }

                if (childList.get(i).get("childId") != null) {
                    String childId = "(" + childList.get(i).get("childId").toString() + ")";
                    pParams.put("childId", childId);
                }

                if (parentTB.contains("Cup")) {
                    pParams.put("cupInfoTB", parentTB);
                    pParams.put("cupId", parentId);
                    HashMap<String, Object> result = cupService.selectGetCupInfoForMedia(pParams);
                    cupResult.add(result);
                }

                if (parentTB.contains("League")) {
                    pParams.put("leagueInfoTB", parentTB);
                    pParams.put("leagueId", parentId);
                    HashMap<String, Object> result = leagueService.selectGetLeagueInfoForMedia(pParams);
                    leagueResult.add(result);
                }

                if (parentTB.contains("Team")) {
                    teamData = teamService.selectTeamListForMedia(pParams);
                    teamResult.addAll(teamData);
                }
            }

        }

        System.out.println("cupResult = " + cupResult);
        System.out.println("leagueResult = " + leagueResult);

        model.addAttribute("cupMediaList", cupResult);
        model.addAttribute("leagueMediaList", leagueResult);
        model.addAttribute("teamMediaList", teamResult);
        model.addAttribute("method", "modify");

        return "media/modify";
    }

    @RequestMapping(value = "/saveMedia{mediaType}")
    public String saveMedia(@RequestParam Map<String, String> params,@PathVariable String mediaType, Model model, HttpServletResponse resp) {
        logger.info("saveMedia was called. params:" + params);
        logger.info("mediaType:" + mediaType);


        if (StrUtil.isEmpty(mediaType)) {
            mediaType = "Video";
        }
        params.put("mediaType", mediaType);

        String method = params.get("method");

        if (method.equals("regist")) {
            int mediaSave = mediaService.insertMedia(params);
        }

        if (method.equals("delete")) {
            int mediaDelete = mediaService.deleteMedia(params);
        }

        if (method.equals("modify")) {
            int mediaModify = mediaService.updateMedia(params);
        }

        model.addAttribute("mediaType", mediaType);
        params.put("mediaType", mediaType);



        return "redirect:/media" + mediaType;
    }

    @RequestMapping(value = "/craw_youtube")
    @ResponseBody
    public YoutubeResultDto craw_youtube(@RequestParam(required = true) String url) {
        return mediaService.craw_youtube(url);
    }

    @RequestMapping(value = "/craw_news")
    @ResponseBody
    public NewsResultDto craw_news(@RequestParam(required = true) String url) {
        return mediaService.craw_news(url);
    }

    @RequestMapping(value = "/craw_blog")
    @ResponseBody
    public BlogResultDto craw_blog(@RequestParam(required = true) String url) {
        return mediaService.craw_blog(url);
    }

    @RequestMapping(value = "/search_cup")
    @ResponseBody
    public Map<String, Object> search_cup(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("search_cup was called. params:" + params);
        String ageGroup = params.get("ageGroup");

        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        String sYear = params.get("sYear");

        if (StrUtil.isEmpty(sYear)) {
            sYear = DateUtil.getCurrentDate(YYYY);
            params.put("sYear", sYear);
        }

        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);

        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);

        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        params.put("cupMainMatchTB", cupMainMatchTB);

        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        params.put("cupTourMatchTB", cupTourMatchTB);

        List<HashMap<String, Object>> cupInfoList = new ArrayList<HashMap<String, Object>>();

        try {
            cupInfoList = cupService.selectSearchCupMatchList(params);

            resultMap.put("state", "SUCCESS");

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }


        resultMap.put("ageGroup", ageGroup);
        resultMap.put("sYear", sYear);
        resultMap.put("sCupName", params.get("sCupName"));
        resultMap.put("data", cupInfoList);

        return resultMap;
    }

    @RequestMapping(value = "/search_cup_detail")
    @ResponseBody
    public Map<String, Object> search_cup_detail(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        logger.info("search_cup_detail was called. params:" + params);
        String ageGroup = params.get("ageGroup");

        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        String sYear = params.get("sYear");

        if (StrUtil.isEmpty(sYear)) {
            sYear = DateUtil.getCurrentDate(YYYY);
            params.put("sYear", sYear);
        }

        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);

        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);

        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        params.put("cupMainMatchTB", cupMainMatchTB);

        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        params.put("cupTourMatchTB", cupTourMatchTB);

        List<HashMap<String, Object>> cupMatchList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, String> cupInfo = new HashMap<String, String>();

        String cupType = params.get("cupType");
        if (StrUtil.isEmpty(cupType)) {
            cupType = "0";
            params.put("cupType", cupType);
        }

        String matchType = params.get("matchType");
        if (StrUtil.isEmpty(matchType)) {
            matchType = "0";
            params.put("matchType", matchType);
        }

        String cupId = params.get("cupId");
        if (StrUtil.isEmpty(cupId)) {
            resultMap.put("state", "ERROR");
            return resultMap;
        }

        String groups = params.get("groups");
        if (StrUtil.isEmpty(groups)) {
            groups = "1";
            params.put("groups", groups);
        }

        try {

            cupInfo = cupService.selectGetCupInfo(params);

            // 예선 + 토너먼트
            if (cupType.equals("0")) {
                if (matchType.equals("0")) {
                    cupMatchList = cupService.selectCupSubMatchResultList(params);
                } else if (matchType.equals("1")) {

                    if (groups.equals("1")) {
                        groups = String.valueOf(cupInfo.get("tour_team"));
                    }

                    params.put("round", groups);
                    cupMatchList = cupService.selectCupTourMatchResultList(params);
                }
            }

            // 예선 + 본선 + 토너먼트
            if (cupType.equals("1")) {
                if (matchType.equals("0")) {
                    cupMatchList = cupService.selectCupSubMatchResultList(params);
                } else if(matchType.equals("1")) {
                    cupMatchList = cupService.selectCupMainMatchList(params);
                } else if(matchType.equals("2")) {

                    if (groups.equals("1")) {
                        groups = String.valueOf(cupInfo.get("tour_team"));
                    }

                    params.put("round", groups);

                    cupMatchList = cupService.selectCupTourMatchList(params);
                }
            }

            if (cupType.equals("3")) {
                if (groups.equals("1")) {
                    groups = String.valueOf(cupInfo.get("tour_team"));
                }
                params.put("round", groups);
                cupMatchList = cupService.selectCupTourMatchResultList(params);
            }

            // 풀리그
            if (cupType.equals("2")) {
                if (matchType.equals("0")) {
                    cupMatchList = cupService.selectCupSubMatchResultList(params);
                }
            }

            // 토너먼트
            if (cupType.equals("3")) {
                cupMatchList = cupService.selectCupTourMatchResultList(params);
            }

            // 풀리그 + 풀리그
            if (cupType.equals("4")) {

                matchType = matchType.equals("0") ? "1" : matchType;
                params.put("teamType", matchType);
                resultMap.put("teamType", matchType);
                cupMatchList = cupService.selectCupSubMatchResultList(params);
            }

            dataMap.put("matchList", cupMatchList);
            dataMap.put("cupInfoMap", cupInfo);
            resultMap.put("state", "SUCCESS");

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }



        resultMap.put("ageGroup", ageGroup);
        resultMap.put("matchType", matchType);
        resultMap.put("groups", groups);
        resultMap.put("sYear", sYear);
        resultMap.put("sCupName", params.get("sCupName"));
        resultMap.put("data", dataMap);

        return resultMap;
    }

    @RequestMapping(value = "/search_league")
    @ResponseBody
    public Map<String, Object> search_league(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("search_league was called. params:" + params);
        String ageGroup = params.get("ageGroup");

        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        String sYear = params.get("sYear");

        if (StrUtil.isEmpty(sYear)) {
            sYear = DateUtil.getCurrentDate(YYYY);
            params.put("sYear", sYear);
        }

        String leagueInfoTB = ageGroup + "_League_Info";
        params.put("leagueInfoTB", leagueInfoTB);


        List<HashMap<String, Object>> leagueInfoList = new ArrayList<HashMap<String, Object>>();

        try {
            leagueInfoList = leagueService.selectSearchLeagueInfoList(params);

            resultMap.put("state", "SUCCESS");

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }



        resultMap.put("ageGroup", ageGroup);
        resultMap.put("sYear", sYear);
        resultMap.put("data", leagueInfoList);

        return resultMap;
    }

    @RequestMapping(value = "/search_league_detail")
    @ResponseBody
    public Map<String, Object> search_league_detail(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        logger.info("search_league_detail was called. params:" + params);
        String ageGroup = params.get("ageGroup");

        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        String sYear = params.get("sYear");

        if (StrUtil.isEmpty(sYear)) {
            sYear = DateUtil.getCurrentDate(YYYY);
            params.put("sYear", sYear);
        }

        String months = params.get("months");

        if (StrUtil.isEmpty(months)) {
            months = Integer.toString(DateUtil.getMonth());
            params.put("months", months);
        }

        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);

        String leagueMatchTB = ageGroup + "_League_Match";
        params.put("leagueMatchTB", leagueMatchTB);

        String leagueInfoTB = ageGroup + "_League_Info";
        params.put("leagueInfoTB", leagueInfoTB);

        String leagueTeamTB = ageGroup + "_League_Team";
        params.put("leagueTeamTB", leagueTeamTB);

        List<HashMap<String, Object>> leagueMatchList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, String> leagueInfo = new HashMap<String, String>();

        try {

            leagueInfo = leagueService.selectGetLeagueInfo(params);
            leagueMatchList = leagueService.selectLeagueMatchSchedule(params);

            dataMap.put("leagueInfo", leagueInfo);
            dataMap.put("matchList", leagueMatchList);
            resultMap.put("state", "SUCCESS");

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }


        resultMap.put("ageGroup", ageGroup);
        resultMap.put("months", params.get("months"));
        resultMap.put("sYear", sYear);
        resultMap.put("data", dataMap);

        return resultMap;
    }

    @RequestMapping(value = "/search_team")
    @ResponseBody
    public Map<String, Object> search_team(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("search_team was called. params:" + params);
        String ageGroup = params.get("ageGroup");

        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }


        List<HashMap<String, Object>> teamList = new ArrayList<HashMap<String, Object>>();

        try {
            teamList = teamService.selectTeamList(params);

            resultMap.put("state", "SUCCESS");

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }

        resultMap.put("ageGroup", ageGroup);
        resultMap.put("data", teamList);

        return resultMap;
    }

}
