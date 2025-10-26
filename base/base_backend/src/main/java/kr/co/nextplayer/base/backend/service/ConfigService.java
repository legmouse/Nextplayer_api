package kr.co.nextplayer.base.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nextplayer.base.backend.mapper.*;
import kr.co.nextplayer.base.backend.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ConfigService {

    @Resource
    private ConfigMapper configMapper;

    @Resource
    private CupMapper cupMapper;

    @Resource
    private LeagueMapper leagueMapper;

    @Resource
    private MediaMapper mediaMapper;

    @Resource
    private PlayerMapper playerMapper;

    @Resource
    private BoardMapper boardMapper;

    @Resource
    private EducationMapper educationMapper;

    public List<HashMap<String, Object>> selectManagerList() {
        return configMapper.selectManagerList();
    }

    @Transactional
    public int updatePw(Map<String, String> param) {
        return configMapper.updateManagerPw(param);
    }

    @Transactional
    public int deleteManager(Map<String, String> param) {
        return configMapper.deleteManager(param);
    }

    public List<HashMap<String, Object>> selectMenuInfoList() {
        return configMapper.selectMenuInfoList();
    }

    @Transactional
    public int registManager(Map<String, String> param) {

        int result = 0;

        String id = param.get("id");
        String pw = param.get("pw");
        String userName = param.get("userName");
        String grade = param.get("grade");

        if (StrUtil.isEmpty(id) || StrUtil.isEmpty(pw) || StrUtil.isEmpty(userName)) {
            throw new NullPointerException();
        }

        // User (관리자) insert start
        result = configMapper.insertManager(param);


        // role_info insert start
        String roleName = id + "_role";
        String role_dc = grade.equals("127") ? "최고 관리자 권한" : "일반 관리자 권한";

        param.put("roleName", roleName);
        param.put("roleDc", role_dc);
        configMapper.insertRoleInfo(param);
        // role_info insert end

        // trget_role_relate insert start
        param.put("trget", id);
        param.put("se", "TRSE0001");
        configMapper.insertTrgetRoleRelate(param);
        // trget_role_relate insert end

        // author_role_relate start
        param.put("authorCd", id + param.get("userId"));
        configMapper.insertAuthorRoleRelate(param);
        // author_role_relate end

        String[] menuArr = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // 선택된 메뉴들 배열로 변환
            menuArr = objectMapper.readValue((String) param.get("menuArr"), String[].class);

            List<HashMap<String, String>> menuParamList = new ArrayList<>();
            if (menuArr.length > 0) {
                for(String menu : menuArr) {
                    HashMap<String, String> menuParam = new HashMap<>();
                    menuParam.put("menuId", menu);
                    menuParam.put("authorCd", id + param.get("userId"));

                    menuParamList.add(menuParam);
                }
                configMapper.insertMenuAuthorRelate(menuParamList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Transactional
    public int modifyManager(Map<String, String> param) {
        int result = 0;

        HashMap<String, Object> userInfo = configMapper.selectUserInfo(param);

        if (userInfo == null) {
            throw new NullPointerException();
        }

        // User Update start
        result = configMapper.updateUserInfo(param);
        // User Update end

        // menu_author_relate data delete start
        String authorCd = userInfo.get("id").toString() + userInfo.get("user_id");
        param.put("authorCd", authorCd);
        configMapper.deleteMenuAuthorRelateData(param);
        // menu_author_relate data delete end

        String[] menuArr = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // 선택된 메뉴들 배열로 변환
            menuArr = objectMapper.readValue((String) param.get("menuArr"), String[].class);

            List<HashMap<String, String>> menuParamList = new ArrayList<>();
            if (menuArr.length > 0) {
                for(String menu : menuArr) {
                    HashMap<String, String> menuParam = new HashMap<>();
                    menuParam.put("menuId", menu);
                    menuParam.put("authorCd", userInfo.get("id") + param.get("userId"));

                    menuParamList.add(menuParam);
                }
                configMapper.insertMenuAuthorRelate(menuParamList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<HashMap<String, Object>> selectUserMenuList(Map<String, String> parma) {
        return configMapper.selectUserMenuList(parma);
    }

    public List<HashMap<String, Object>> selectUserUpperMenuList(Map<String, String> parma) {
        return configMapper.selectUserUpperMenuList(parma);
    }

    public HashMap<String, Object> selectUserInfo(Map<String, String> param) {
        return configMapper.selectUserInfo(param);
    }

    public List<HashMap<String, Object>> selectUpperMenuList() {
        return configMapper.selectUpperMenuList();
    }

    public List<HashMap<String, Object>> selectMainMenuList() {
        return configMapper.selectMainMenuList();
    }

    @Transactional
    public int updateMainMenu(List<Map<String, String>> param) {
        int result = 0;
        if (param != null) {
            for(Map<String, String> map : param) {
                result = configMapper.updateMainMenu(map);
            }
        }
        return result;
    }

    @Transactional
    public int saveShowPointFlag(List<Map<String, String>> param) {
        int result = 0;

        if(param.size() > 0) {
            for (Map<String, String> map : param) {
                String ageGroup = map.get("ageGroup").toLowerCase();
                if (map.get("type").equals("cup")) {
                    String matchCategory = map.get("matchCategory");
                    String cupMatchTB = ageGroup + "_cup_" + matchCategory + "_match";
                    map.put("cupMatchTB", cupMatchTB);
                    result = cupMapper.updateMatchScoreShowFlag(map);
                }
                if (map.get("type").equals("league")) {
                    String leagueMatchTB = ageGroup + "_league_match";
                    map.put("leagueMatchTB", leagueMatchTB);
                    result = leagueMapper.updateMatchScoreShowFlag(map);
                }
            }
        }

        return result;
    }

    @Transactional
    public int saveShowFlagProgressCup(List<Map<String, String>> param) {
        int result = 0;

        if(param.size() > 0) {
            for (Map<String, String> map : param) {
                String ageGroup = map.get("ageGroup").toLowerCase();
                String cupInfoTB = ageGroup + "_cup_info";
                map.put("cupInfoTB", cupInfoTB);
                result = cupMapper.updateShowFlagProgressCup(map);
            }
        }

        return result;
    }

    @Transactional
    public int saveShowFlagProgressLeague(List<Map<String, String>> param) {
        int result = 0;

        if(param.size() > 0) {
            for (Map<String, String> map : param) {
                String ageGroup = map.get("ageGroup").toLowerCase();
                String leagueInfoTB = ageGroup + "_league_info";
                map.put("leagueInfoTB", leagueInfoTB);
                result = leagueMapper.updateShowFlagProgressLeague(map);
            }
        }

        return result;
    }

    @Transactional
    public int saveLiveShowFlag(List<Map<String, String>> param) {
        int result = 0;

        if(param.size() > 0) {
            for (Map<String, String> map : param) {
                String ageGroup = map.get("ageGroup").toLowerCase();
                String matchCategory = map.get("matchCategory");
                String cupMatchTB = ageGroup + "_cup_" + matchCategory + "_match";
                map.put("cupMatchTB", cupMatchTB);
                result = cupMapper.updateLiveShowFlag(map);
            }
        }

        return result;
    }

    @Transactional
    public int saveMainMediaOrder(Map<String, String> param) {
        int result = 0;

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, String>> mediaDataMap = objectMapper.readValue(param.get("mediaList"), new TypeReference<List<Map<String, String>>>() {});


            if (param.size() > 0) {
                mediaMapper.deleteMainMediaMainFlag(param);
                mediaMapper.deleteMainMediaData(param);
                if (mediaDataMap.size() > 0) {
                    for (Map<String, String> map : mediaDataMap) {
                        map.put("mediaType", param.get("mediaType"));
                        System.out.println("map = " + map);
                        result = mediaMapper.insertMainMediaData(map);
                        map.put("mainFlag", "1");
                        mediaMapper.updateMediaMainFlag(map);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Transactional
    public int saveMainRosterOrder(List<Map<String, String>> param) {
        int result = 0;

        try {
            if (param.size() > 0) {
                playerMapper.deleteMainRosterData();
                for (Map<String, String> map : param) {
                    result = playerMapper.insertMainRosterData(map);
                }
            }
        } catch (Exception e) {

        }

        return result;
    }

    @Transactional
    public int saveMainBannerOrder(Map<String, String> param) {
        int result = 0;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, String>> bannerDataMap = objectMapper.readValue(param.get("bannerList"), new TypeReference<List<Map<String, String>>>() {});


            if (param.size() > 0) {
                boardMapper.deleteMainBannerData(param);
                for (Map<String, String> map : bannerDataMap) {
                    map.put("bannerType", param.get("bannerType"));
                    result = boardMapper.insertMainBannerData(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Transactional
    public int saveMainColumnOrder(Map<String, String> param) {
        int result = 0;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, String>> columnDataMap = objectMapper.readValue(param.get("columnList"), new TypeReference<List<Map<String, String>>>() {});
            if (param.size() > 0) {
                educationMapper.deleteMainColumnData();
                for (Map<String, String> map : columnDataMap) {
                    result = educationMapper.insertMainColumnData(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
