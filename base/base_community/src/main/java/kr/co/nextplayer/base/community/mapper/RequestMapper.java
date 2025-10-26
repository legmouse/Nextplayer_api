package kr.co.nextplayer.base.community.mapper;

import kr.co.nextplayer.base.community.dto.*;
import kr.co.nextplayer.base.community.model.Request;
import kr.co.nextplayer.base.community.model.Suggest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface RequestMapper {

    int insertRequest(RequestDto requestDto);

    int selectRequestListCount(RequestListDto requestListDto);

    List<Request> selectRequestList(RequestListDto requestListDto);

    HashMap<String ,Object> selectRequestCupData(Map<String, String> params);

    HashMap<String ,Object> selectRequestLeagueData(Map<String, String> params);

    HashMap<String ,Object> selectRequestTeamData(Map<String, String> params);

    HashMap<String ,Object> selectRequestCupMatchData(Map<String, String> params);

    HashMap<String ,Object> selectRequestLeagueMatchData(Map<String, String> params);

    List<HashMap<String ,Object>> selectSearchCupInfo(Map<String, String> params);

    List<HashMap<String ,Object>> selectSearchLeagueInfo(Map<String, String> params);

    List<HashMap<String ,Object>> selectSearchTeamInfo(Map<String, String> params);

    int selectSuggestCount(SuggestListDto suggestListDto);

    List<Suggest> selectSuggestList(SuggestListDto suggestListDto);

    int insertSuggest(SuggestDto suggestDto);

    int insertPartnership(PartnershipDto partnershipDto);

}
