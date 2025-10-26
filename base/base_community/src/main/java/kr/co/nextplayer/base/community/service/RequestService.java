package kr.co.nextplayer.base.community.service;

import kr.co.nextplayer.base.community.dto.*;
import kr.co.nextplayer.base.community.mapper.RequestMapper;
import kr.co.nextplayer.base.community.model.Request;
import kr.co.nextplayer.base.community.model.Suggest;
import kr.co.nextplayer.base.community.response.RequestListResponse;
import kr.co.nextplayer.base.community.response.RequestSearchResponse;
import kr.co.nextplayer.base.community.response.SuggestListResponse;
import kr.co.nextplayer.base.media.mapper.MediaFileMapper;
import kr.co.nextplayer.base.media.model.MediaFile;
import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import kr.co.nextplayer.next.lib.common.mybatis.hander.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RequestService {

    @Resource
    private RequestMapper requestMapper;

    @Resource
    private MediaFileMapper mediaFileMapper;


    public RequestSearchResponse getSearchInfo(Map params) throws Exception {

        List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
        String ageGroup = params.get("ageGroup").toString();
        if (params.get("sType").equals("0")) {
            if (params.get("parentType").equals("Cup")) {
                String cupInfoTB = params.get("ageGroup") + "_Cup_Info";
                String cupSubMatchTB = params.get("ageGroup") + "_Cup_Sub_Match";
                String cupMainMatchTB = params.get("ageGroup") + "_Cup_Main_Match";
                String cupTourMatchTB = params.get("ageGroup") + "_Cup_Tour_Match";

                params.put("cupInfoTB", cupInfoTB);
                params.put("cupSubMatchTB", cupSubMatchTB);
                params.put("cupMainMatchTB", cupMainMatchTB);
                params.put("cupTourMatchTB", cupTourMatchTB);

                result = requestMapper.selectSearchCupInfo(params);
            } else if (params.get("parentType").equals("League")) {
                String leagueInfoTB = params.get("ageGroup") + "_League_Info";
                String leagueMatchTB = params.get("ageGroup") + "_League_Match";
                params.put("leagueInfoTB", leagueInfoTB);
                params.put("leagueMatchTB", leagueMatchTB);

                result = requestMapper.selectSearchLeagueInfo(params);
            }
        } else if (params.get("sType").equals("1")) {
            result = requestMapper.selectSearchTeamInfo(params);
        }

        RequestSearchResponse searchResponse = RequestSearchResponse.builder()
            .ageGroup(ageGroup)
            .searchList(result)
            .build();

        return searchResponse;
    }

    public int saveRequest(RequestDto requestDto) throws Exception{
        int result = 0;

        String ageGroup = requestDto.getAgeGroup();

        String parentType = requestDto.getParentType();
        String foreignType = requestDto.getForeignType();

        if (!StringUtils.isEmpty(parentType)) {
            if (parentType.equals("Cup")) {
                parentType = ageGroup + "_Cup_Info";
            } else if (parentType.equals("League")) {
                parentType = ageGroup + "_League_Info";
            } else if (parentType.equals("Team")) {
                parentType = "Team";
            } else if (parentType.equals("Education")) {
                parentType = "Education";
            }
        }

        if (!StringUtils.isEmpty(foreignType)) {
            if (foreignType.equals("Sub")) {
                foreignType = ageGroup + "_Cup_Sub_Match";
            } else if (foreignType.equals("Main")) {
                foreignType = ageGroup + "_Cup_Main_Match";
            } else if (foreignType.equals("Tour")) {
                foreignType = ageGroup + "_Cup_Tour_Match";
            } else if (foreignType.equals("League")) {
                foreignType = ageGroup + "_League_Match";
            }
        }

        requestDto.setParentType(parentType);
        requestDto.setForeignType(foreignType);

        int requestResult = requestMapper.insertRequest(requestDto);

        if (requestDto.getFileId() != null && requestDto.getFileId().size() > 0) {
            for (String file : requestDto.getFileId()) {
                MediaFile mediaFile = MediaFile.builder()
                    .foreignId(Long.valueOf(requestDto.getRequestId()))
                    .id(Long.valueOf(file))
                    .build();

                mediaFileMapper.updateSaveFile(mediaFile);
            }
        }

        return result;
    }

    public RequestListResponse getRequestList(RequestListDto requestListDto) {

        int totalCount = 0;
        List<RequestListDto> list = new ArrayList<RequestListDto>();

        totalCount = requestMapper.selectRequestListCount(requestListDto);
        int curPage = requestListDto.getCurPage();

        if(curPage <= 0) {
            curPage = 1;
        }

        int cp = curPage;
        int cpp = 10;
        int tp = 1;

        if (totalCount > 0) {
            tp = (int) totalCount / cpp;
            if ((totalCount % cpp) > 0) {
                tp += 1;
            }
        }

        int sRow = (cp - 1) * cpp + 1;
        int eNum = cp * cpp;

        requestListDto.setSRow(sRow);
        requestListDto.setECount(eNum);

        List<Request> requests = requestMapper.selectRequestList(requestListDto);

        if (requests.size() > 0) {
            for (Request request : requests) {
                Map<String, String> params = new HashMap<>();

                params.put("parentId", request.getParentId());
                params.put("parentTB", request.getParentType());

                String childTB = "";
                String childId = "";
                String childKey = "";

                Map<String, Object> foreignInfo = new HashMap<>();
                HashMap<String, Object> matchData = new HashMap<String, Object>();

                if (request.getParentType().contains("Cup")) {

                    if (!StringUtils.isEmpty(request.getParentId())) {
                        foreignInfo = requestMapper.selectRequestCupData(params);
                        //request.setForeignType("Cup");
                    }


                    if (!StringUtils.isEmpty(request.getForeignId())) {



                        childTB = !StringUtils.isEmpty(request.getForeignType()) ? request.getForeignType() : "";
                        childId = !StringUtils.isEmpty(request.getForeignId()) ?  "(" + request.getForeignId() + ")" : "";

                        if (childTB.contains("Cup_Sub")) {
                            childKey = "a.cup_sub_match_id";
                        } else if (childTB.contains("Cup_Main")) {
                            childKey = "a.cup_main_match_id";
                        } else if (childTB.contains("Cup_Tour")) {
                            childKey = "a.cup_tour_match_id";
                        }

                        params.put("childTB", childTB);
                        params.put("childId", childId);
                        params.put("childKey", childKey);

                        matchData = requestMapper.selectRequestCupMatchData(params);
                        //foreignInfo.put("matchData", matchData);

                    }

                }

                if (request.getParentType().contains("League")) {

                    if (!StringUtils.isEmpty(request.getParentId())) {
                        foreignInfo = requestMapper.selectRequestLeagueData(params);
                    }

                    if (!StringUtils.isEmpty(request.getForeignId())) {

                        childTB = !StringUtils.isEmpty(request.getForeignType()) ? request.getForeignType() : "";
                        childId = !StringUtils.isEmpty(request.getForeignId()) ?  "(" + request.getForeignId() + ")" : "";
                        childKey = "a.league_match_id";

                        params.put("childTB", childTB);
                        params.put("childId", childId);
                        params.put("childKey", childKey);

                        matchData = requestMapper.selectRequestLeagueMatchData(params);

                    }

                    /*foreignInfo.put("matchData", matchData);
                    requestDto.setForeignInfo(foreignInfo);*/

                }

                if (request.getParentType().contains("Team")) {

                    if (!StringUtils.isEmpty(request.getParentId())) {
                        foreignInfo = requestMapper.selectRequestTeamData(params);
                    }

                }

                RequestListDto requestDto = RequestListDto.builder()
                    .requestId(request.getRequestId())
                    .memberCd(request.getMember().getMemberCd())
                    .memberId(request.getMember().getMemberId())
                    .memberNickname(request.getMember().getMemberNickName())
                    .content(request.getContent())
                    .parentId(request.getParentId())
                    .parentType(request.getParentType())
                    .foreignId(request.getForeignId())
                    .foreignType(request.getForeignType())
                    .requestType(request.getRequestType())
                    .useFlag(request.getUseFlag())
                    .answer(request.getAnswer())
                    .answerFlag(request.getAnswerFlag())
                    .regDate(request.getRegDate())
                    .answerDate(request.getAnswerDate())
                    .secretFlag(request.getSecretFlag())
                    .mediaFile(request.getMediaFile())
                    .foreignInfo(foreignInfo)
                    .matchData(matchData)
                    .answerImg(request.getAnswerImg())
                    .build();

                list.add(requestDto);
            }
        }

        RequestListResponse requestListResponse = RequestListResponse.builder()
            .totalCount(totalCount)
            .requestList(list)
            .build();

        return requestListResponse;
    }

    public SuggestListResponse getSuggestList(SuggestListDto suggestListDto) {

        int totalCount = 0;
        List<SuggestListDto> list = new ArrayList<SuggestListDto>();

        totalCount = requestMapper.selectSuggestCount(suggestListDto);

        int curPage = suggestListDto.getCurPage();

        if(curPage <= 0) {
            curPage = 1;
        }

        int cp = curPage;
        int cpp = 10;
        int tp = 1;

        if (totalCount > 0) {
            tp = (int) totalCount / cpp;
            if ((totalCount % cpp) > 0) {
                tp += 1;
            }
        }

        int sRow = (cp - 1) * cpp;

        suggestListDto.setSRow(sRow);
        suggestListDto.setECount(cpp);

        List<Suggest> suggests = requestMapper.selectSuggestList(suggestListDto);

        if (suggests.size() > 0) {
            for (Suggest suggest : suggests) {
                SuggestListDto suggestDto = SuggestListDto.builder()
                    .suggestId(suggest.getSuggestId())
                    .memberCd(suggest.getMember().getMemberCd())
                    .memberId(suggest.getMember().getMemberId())
                    .memberNickname(suggest.getMember().getMemberNickName())
                    .title(suggest.getTitle())
                    .content(suggest.getContent())
                    .answer(suggest.getAnswer())
                    .answerFlag(suggest.getAnswerFlag())
                    .answerDate(suggest.getAnswerDate())
                    .secretFlag(suggest.getSecretFlag())
                    .mediaFile(suggest.getMediaFile())
                    .answerImg(suggest.getAnswerImg())
                    .regDate(suggest.getRegDate())
                .build();

                if (suggest.getSecretFlag().equals("1") && !suggest.getMember().getMemberCd().equals(suggestListDto.getMemberCd())) {
                    suggestDto.setTitle("비밀글 입니다.");
                    suggestDto.setContent("비밀글 입니다.");

                    suggestDto.setOpenFlag("1");

                } else {
                    suggestDto.setOpenFlag("0");
                }
                
                list.add(suggestDto);
            }
        }

        SuggestListResponse suggestListResponse = SuggestListResponse.builder()
            .totalCount(totalCount)
            .suggestList(list)
            .build();

        return suggestListResponse;
    }

    public int saveSuggest(SuggestDto suggestDto) throws Exception{
        int result = 0;

        result = requestMapper.insertSuggest(suggestDto);

        if (suggestDto.getFileId() != null && suggestDto.getFileId().size() > 0) {
            for (String file : suggestDto.getFileId()) {
                MediaFile mediaFile = MediaFile.builder()
                    .foreignId(Long.valueOf(suggestDto.getSuggestId()))
                    .id(Long.valueOf(file))
                    .build();

                mediaFileMapper.updateSaveFile(mediaFile);
            }
        }

        return result;
    }

    public int savePartnership(PartnershipDto partnershipDto) throws Exception {
        int result = 0;

        result = requestMapper.insertPartnership(partnershipDto);

        if (partnershipDto.getFileId() != null && partnershipDto.getFileId().size() > 0) {
            for (String file : partnershipDto.getFileId()) {
                MediaFile mediaFile = MediaFile.builder()
                    .foreignId(Long.valueOf(partnershipDto.getSuggestId()))
                    .id(Long.valueOf(file))
                    .build();

                mediaFileMapper.updateSaveFile(mediaFile);
            }
        }

        return result;
    }

}
