package kr.co.nextplayer.base.backend.controller;

import kr.co.nextplayer.base.backend.dto.joinkfaCraw.GameDto;
import kr.co.nextplayer.base.backend.dto.joinkfaCraw.MatchDataDto;
import kr.co.nextplayer.base.backend.dto.joinkfaCraw.MatchDto;
import kr.co.nextplayer.base.backend.dto.joinkfaCraw.MatchPlayDataDto;
import kr.co.nextplayer.base.backend.service.*;
import kr.co.nextplayer.base.backend.util.DateUtil;
import kr.co.nextplayer.base.backend.util.StrUtil;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
public class CrawController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private CrawService crawService;

    @Resource
    private LeagueService leagueService;

    @Resource
    private CupService cupService;

    @Resource
    private PushService pushService;

    @Resource
    private UageService uageService;

    @GetMapping("/craw/getGameTitle")
    @ResponseBody
    public List<GameDto> getGameTitle(@RequestParam(required = true) String level, @RequestParam(required = true) String matchType) {
        logger.info("level :" + level);
        logger.info("matchType :" + matchType);
        return crawService.getGameTitle(level, matchType);
    }
    @GetMapping("/craw/getGameCraw")
    @ResponseBody
    public List<GameDto> getGameCraw(@RequestParam(required = true) String level, @RequestParam(required = true) String matchType) {
        logger.info("level :" + level);
        logger.info("matchType :" + matchType);
        return crawService.getGame(level, matchType);
    }
    @GetMapping("/craw/getMatchCraw")
    @ResponseBody
    public List<MatchDto> getMatchCraw(@RequestParam(required = true) String level,
                                       @RequestParam(required = true) String matchType,
                                       @RequestParam(required = true) String title
    ) {
        logger.info("level :" + level);
        logger.info("matchType :" + matchType);
        logger.info("title :" + title);
        return crawService.getMatchLeague(level, matchType, title);
    }
    @GetMapping("/craw/getMatchCrawExcel")
    public void getMatchCrawExcel(@RequestParam(required = true) String level,
                                  @RequestParam(required = true) String matchType,
                                  @RequestParam(required = true) String title,
                                  HttpServletResponse res) throws IOException {
        logger.info("level :" + level);
        logger.info("matchType :" + matchType);
        logger.info("title :" + title);
        crawService.getMatchExcelLeague(level, matchType, title, res);
    }

    @GetMapping("/craw/getPlayDataCraw")
    @ResponseBody
    public MatchPlayDataDto getPlayDataCraw(@RequestParam(required = true) String level,
                                            @RequestParam(required = true) String matchType,
                                            @RequestParam(required = true) String title,
                                            @RequestParam(required = true) int targetMatchOrder,
                                            @RequestParam(required = true) String ageGroup,
                                            @RequestParam(required = true) int matchId,
                                            @RequestParam(required = false) String cupType,
                                            @RequestParam(required = false) String sDate,
                                            @RequestParam(required = false) String cupId
    ) {
        logger.info("level :" + level);
        logger.info("matchType :" + matchType);
        logger.info("title :" + title);
        logger.info("targetMatchOrder :" + targetMatchOrder);
        logger.info("ageGroup :" + ageGroup);
        logger.info("cupType :" + cupType);
        logger.info("sDate :" + sDate);


        MatchPlayDataDto playData = crawService.getPlayData(level, matchType, title, targetMatchOrder, sDate);

        System.out.println(playData);
        switch (matchType) {
            case "MATCH":
                cupService.updateCupMatchInfo(playData, ageGroup, matchId, level, cupType);
                pushService.insertPushLineup(ageGroup, matchId, cupType, cupId);
                break;
            case "LEAGUE2":
                leagueService.updateLeagueMatchInfo(playData, ageGroup, matchId, level);
                break;
        }
        return playData;
    }

    @GetMapping("/craw/getAllPlayDataCraw")
    @ResponseBody
    public Map<String, Object> getAllPlayDataCraw(@RequestParam(required = true) String level,
                                                  @RequestParam(required = true) String matchType,
                                                  @RequestParam(required = true) String title,
                                                  @RequestParam(required = true) String ageGroup,
                                                  @RequestParam(required = false) String cupType,
                                                  @RequestParam(required = true) String foreignId,
                                                  @RequestParam(required = true) String sDate,
                                                  @RequestParam(required = true) String sTime,
                                                  @RequestParam(required = true) String sMinute
    ) {
        logger.info("level :" + level);
        logger.info("matchType :" + matchType);
        logger.info("title :" + title);
        logger.info("ageGroup :" + ageGroup);
        logger.info("cupType :" + cupType);

        Map<String, Object> result = new HashMap<>();

        List<MatchPlayDataDto> playData = crawService.getAllPlayData(level, ageGroup, cupType, matchType, foreignId, title, sDate, sTime, sMinute);

        if (playData != null) {
            switch (matchType) {
                case "MATCH":
                    for (MatchPlayDataDto data : playData) {
                        cupService.updateCupMatchInfo(data, ageGroup, Integer.parseInt(data.getMatchId()), level, cupType);
                        pushService.insertPushLineup(ageGroup, Integer.parseInt(data.getMatchId()), cupType, foreignId);
                    }
                    break;
                case "LEAGUE2":
                    for (MatchPlayDataDto data : playData) {
                        leagueService.updateLeagueMatchInfo(data, ageGroup, Integer.parseInt(data.getMatchId()), level);
                    }
                    break;
            }
            result.put("state", "success");
        } else {
            result.put("state", "empty");
        }

        return result;
    }

    @GetMapping("/craw/getGameAllMatchScoreCraw")
    @ResponseBody
    public Map<String, Object> getGameAllMatchScoreCraw(@RequestParam(required = true) String level,
                                                        @RequestParam(required = true) String matchType,
                                                        @RequestParam(required = true) String title,
                                                        @RequestParam(required = true) String ageGroup,
                                                        @RequestParam(required = false) String cupType,
                                                        @RequestParam(required = true) String foreignId,
                                                        @RequestParam(required = true) String sDate
    ) {
        logger.info("level :" + level);
        logger.info("matchType :" + matchType);
        logger.info("title :" + title);
        logger.info("ageGroup :" + ageGroup);
        logger.info("cupType :" + cupType);
        logger.info("sDate :" + sDate);

        Map<String, Object> result = new HashMap<>();

        List<MatchPlayDataDto> matchData = crawService.getAllMatchScoreData(level, ageGroup, cupType, matchType, foreignId, title, sDate);

        logger.info("matchData :" + matchData);
        if (matchData != null) {
            List<MatchDataDto> matchList = new ArrayList<>();
            String cupMatchTB = "";
            String leagueMatchTB = "";
            Map<String, String> matchmap = new HashMap<>();

            if (!StrUtil.isEmpty(cupType)) {
                switch (cupType) {
                    case "MAIN":
                        cupMatchTB = ageGroup + "_Cup_Main_Match";
                        matchmap.put("matchTB", cupMatchTB);
                        matchmap.put("cupId", foreignId);
                        matchList = cupService.selectCupMainMatchListCraw(matchmap);
                        break;
                    case "SUB":
                        cupMatchTB = ageGroup + "_Cup_Sub_Match";
                        matchmap.put("matchTB", cupMatchTB);
                        matchmap.put("cupId", foreignId);
                        matchList = cupService.selectCupSubMatchListCraw(matchmap);
                        break;
                    case "TOUR":
                        cupMatchTB = ageGroup + "_Cup_Tour_Match";
                        matchmap.put("matchTB", cupMatchTB);
                        matchmap.put("cupId", foreignId);
                        matchList = cupService.selectCupTourMatchListCraw(matchmap);
                        break;
                }
            } else {
                leagueMatchTB = ageGroup + "_League_Match";
                matchmap.put("matchTB", leagueMatchTB);
                matchmap.put("leagueId", foreignId);
                matchList = leagueService.selectLeagueMatchListCraw(matchmap);
            }
            switch (matchType) {
                case "MATCH":
                    if (matchList.size() > 0) {
                        for (MatchPlayDataDto data : matchData) {
                            cupService.updateCupMatchInfoCraw(data, ageGroup, Integer.parseInt(data.getMatchId()), level, cupType, foreignId, matchList);
                        }
                    }
                    break;
                case "LEAGUE2":
                    for (MatchPlayDataDto data : matchData) {
                        leagueService.updateLeagueMatchInfoCraw(data, ageGroup, Integer.parseInt(data.getMatchId()), level, foreignId, matchList);
                    }
                    break;
            }
            result.put("state", "success");
        } else {
            result.put("state", "empty");
        }

        return result;
    }

    @RequestMapping("/crawSetting")
    public String crawSetting(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        logger.info("selectCupMatch was called. params:" + params);

        String sdate = params.get("sdate");
        if (StrUtil.isEmpty(sdate)) {
            sdate = DateUtil.getCurrentDate();
        }

        params.put("sdate", sdate);

        try {

            List<HashMap<String, Object>> uageList = uageService.selectUseUageList();

            for (HashMap<String, Object> uage: uageList) {
                String uageStr = uage.get("uage").toString();
                String cupInfoTB = uageStr + "_Cup_Info";

                params.put("cupInfoTB", cupInfoTB);

                List<HashMap<String, Object>> cupMatch = cupService.selectCrawSettingCupList(params);

                model.addAttribute(uageStr + "CupMatch", cupMatch);
            }

            model.addAttribute("sdate", sdate);

            String method = params.get("method");
            String text = params.get("bodyText");
            String title = params.get("titleText");
            String typeId = params.get("typeId");
            model.addAttribute("typeId", typeId);
            model.addAttribute("method", method);
            model.addAttribute("bodyText", text);
            model.addAttribute("titleText", title);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "craw/crawSetting";
    }

    @PostMapping("/crawSetting")
    @ResponseBody
    public Map<String, Object> execCrawSetting(@RequestBody HashMap<String,String> map) {

        Map<String, Object> result = new HashMap<>();

        cupService.updateCrawSetting(map);

        result.put("state", "success");

        return result;
    }


}
