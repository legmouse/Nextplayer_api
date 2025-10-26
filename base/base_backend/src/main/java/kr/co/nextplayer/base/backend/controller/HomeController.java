package kr.co.nextplayer.base.backend.controller;

import kr.co.nextplayer.base.backend.service.TeamService;
import kr.co.nextplayer.base.backend.util.Define;
import kr.co.nextplayer.base.backend.util.StrUtil;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
public class HomeController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private TeamService teamService;

    @RequestMapping("/")
    public ModelAndView openCourseSearchScreen(){
        ModelAndView modelAndView = new ModelAndView("login");
        return modelAndView;
    }

    /*
     * ajax 조회
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/ajax", method = RequestMethod.POST)
    public String process_ajax(@RequestParam final Map<String, String> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("request ----> ajax params : "+ params);


        String cmd = request.getParameter("cmd");
        String szJsonData = request.getParameter("data");

        if(StrUtil.isEmpty(cmd)){
            String errsmg = "CMD IS NULL";
            String szRes = "{'code':'"+Define.CMD_NULL+"', 'msg':'"+errsmg+"' }";
            szRes = szRes.replace("'", "\"");
            //errorRecordLog("[nuclearPower_ajax 실패("+errsmg+")]-message: "+szRes+" 등록실패."); //접속하여 행위한 정보에대한 기록
            StrUtil.sendResponseStrMessage(response, szRes);
            return null;

        }else{
            System.out.println("cmd : "+ cmd + " , szJsonData : " + szJsonData);
            //HashMap<String, Object> param = StrUtil.JsonToMap(szJsonData);

            if("doSearchTeam".equals(cmd)){ //리그 - 참가팀 조회
                HashMap<String, Object> param = StrUtil.JsonToMap(szJsonData);

                List<HashMap<String, Object>> teamList = teamService.selectSearchTeamList(param);

                JSONObject json=new JSONObject();    // JASON 객체생성
                json.put("teamList", teamList);
                json.put("code", Define.RESULT_OK);

                StrUtil.sendResponseMessage(response, json);

            }else if("doSearchTeam2".equals(cmd)){ //팀관리 - 부모팀 닉네임 검색
                List<HashMap<String, Object>> sTeamList = teamService.selectSearchTeamList2(params);

                JSONObject json=new JSONObject();    // JASON 객체생성
                json.put("sTeamList", sTeamList);

                StrUtil.sendResponseMessage(response, json);

            }

        }

        return null;
    }

}
