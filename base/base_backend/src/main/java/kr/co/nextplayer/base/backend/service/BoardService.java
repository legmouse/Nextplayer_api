package kr.co.nextplayer.base.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nextplayer.base.backend.dto.BoardDto;
import kr.co.nextplayer.base.backend.dto.PartnershipDto;
import kr.co.nextplayer.base.backend.vo.ReferenceVO;
import kr.co.nextplayer.base.backend.dto.AttchFileInfoDto;
import kr.co.nextplayer.base.backend.mapper.BoardMapper;
import kr.co.nextplayer.base.backend.util.StrUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardService {

    @Resource
    private BoardMapper boardMapper;

    @Resource
    private FileService fileService;

    public HashMap<String, Object> selectQnaListCount(Map<String, String> params) {
        return boardMapper.selectQnaListCount(params);
    }

    public List<HashMap<String, Object>> selectQnaList(Map<String, String> params) {
        return boardMapper.selectQnaList(params);
    }

    public HashMap<String, Object> selectQnaDet(Map<String, String> params) {
        return boardMapper.selectQnaDet(params);
    }

    @Transactional
    public int updateQnaUseFlag(Map<String, String> params) {
        return boardMapper.updateQnaUseFlag(params);
    }

    public int selectReferenceListCnt(Map<String, String> params) {
        return boardMapper.selectReferenceListCnt(params);
    }

    public List<HashMap<String, Object>> selectReferenceList(Map<String, String> params) {
        return boardMapper.selectReferenceList(params);
    }

    @Transactional
    public int insertReference(Map<String, String> params, List<AttchFileInfoDto> files) {

        if (params.get("method").equals("save")) {
            int resultCnt = boardMapper.insertReference(params);
        }

        int cupCnt = Integer.parseInt(params.get("cupCnt"));
        int leagueCnt = Integer.parseInt(params.get("leagueCnt"));

        List<Object> cupDataList = new ArrayList<Object>();
        List<Object> leagueDataList = new ArrayList<Object>();

        HashMap<String, Object> mparams = new HashMap<String, Object>();
        mparams.put("referenceId", params.get("referenceId"));

        if (cupCnt > 0) {
            cupDataList = makeReferenceList(params);
            mparams.put("list", cupDataList);
        }

        if (leagueCnt > 0) {
            leagueDataList = makeReferenceList(params);
            mparams.put("list", leagueDataList);
        }


        if (cupDataList.size() > 0 || leagueDataList.size() > 0) {
            int referenceCrossResult = boardMapper.insertReferenceCross(mparams);
        }

        if (files != null) {
            fileService.insertAttchFileInfo(files, params);
        }

        return 1;
    }

    public HashMap<String, Object> selectReferenceInfo(Map<String, String> params) {
        return boardMapper.selectReferenceInfo(params);
    }

    public List<HashMap<String, Object>> selectReferenceChildList(Map<String, String> params) {
        return boardMapper.selectReferenceChildList(params);
    }

    @Transactional
    public int updateDeleteReference(Map<String, String> params) {
        int result = boardMapper.updateDeleteReference(params);
        boardMapper.updateDeleteReferenceCross(params);

        return result;
    }

    @Transactional
    public int updateReference(Map<String, String> params, List<AttchFileInfoDto> files) {
        int referenceUpdateResult = boardMapper.updateReference(params);
        int deleteReferenceCross = boardMapper.deleteReferenceCross(params);

        fileService.deleteAttchFileInfo(files, params);

        int updateResult = 0;

        if (referenceUpdateResult > 0) {
            updateResult = insertReference(params, files);
        }

        return updateResult;
    }

    public int selectRequestListCount(Map<String, String> params) {
        return boardMapper.selectRequestListCount(params);
    }

    public List<HashMap<String, Object>> selectRequestList(Map<String, String> params) {
        return boardMapper.selectRequestList(params);
    }

    public HashMap<String, Object> selectRequestDetail(Map<String, String> params) {
        return boardMapper.selectRequestDetail(params);
    }

    public int selectOneToOneListCount(Map<String, String> params) {
        return boardMapper.selectOneToOneListCount(params);
    }

    public List<HashMap<String, Object>> selectOneToOneList(Map<String, String> params) {
        return boardMapper.selectOneToOneList(params);
    }

    public HashMap<String, Object> selectOneToOneDetail(Map<String, String> params) {
        return boardMapper.selectOneToOneDetail(params);
    }

    @Transactional
    public int updateModifyRequest(Map<String, String> params, List<AttchFileInfoDto> imgFiles) {


        int resultCnt = boardMapper.updateModifyRequest(params);

        if (params.get("method").equals("modify")) {
            fileService.deleteAttchFileInfo(imgFiles, params);
        }

        if (imgFiles != null) {
            fileService.insertAnswerAttchFileInfo(imgFiles, params);
        }

        return resultCnt;
    }

    @Transactional
    public int deleteRequest(Map<String, String> params) {

        int result = boardMapper.deleteRequest(params);

        return result;
    }

    @Transactional
    public int updateCancelAnswerRequest(Map<String, String> params) {
        return boardMapper.updateCancelAnswerRequest(params);
    }

    @Transactional
    public int updateSaveAnswer(Map<String, String> params, List<AttchFileInfoDto> imgFiles) {

        int result = boardMapper.updateSaveOneToOne(params);

        if (params.get("method").equals("modify")) {
            fileService.deleteAttchFileInfo(imgFiles, params);
        }

        if (imgFiles != null) {
            fileService.insertAnswerAttchFileInfo(imgFiles, params);
        }

        return result;
    }

    public int deleteOneToOne(Map<String, String> param) {
        return boardMapper.deleteOneToOneData(param);
    }

    private List<Object> makeReferenceList(Map<String, String> params) {
        List<Object> objList = new ArrayList<Object>();

        int cupCnt = Integer.parseInt(params.get("cupCnt"));
        int leagueCnt = Integer.parseInt(params.get("leagueCnt"));

        if (cupCnt > 0) {
            for (int i = 0; i < cupCnt; i++) {
                HashMap<String, Object> map =new HashMap<String, Object>();
                if (!StrUtil.isEmpty(params.get("cupId" + i))) {
                    map.put("foreignId", params.get("cupId" + i));
                    map.put("foreignType", params.get("uage" + i).toLowerCase() + "_cup_info");

                    objList.add(map);
                }
            }
        }

        if (leagueCnt > 0) {
            for (int i = 0; i < leagueCnt; i++) {
                HashMap<String, Object> map =new HashMap<String, Object>();
                if (!StrUtil.isEmpty(params.get("leagueId" + i))) {
                    map.put("foreignId", params.get("leagueId" + i));
                    map.put("foreignType", params.get("uage" + i).toLowerCase() + "_league_info");

                    objList.add(map);
                }
            }
        }

        return objList;
    }

    public int selectNoticeListCount(Map<String, String> params) {
        return boardMapper.selectNoticeListCount(params);
    }

    /*public List<HashMap<String, Object>> selectNoticeList(Map<String, String> params) {
        return boardMapper.selectNoticeList(params);
    }*/
    public List<BoardDto> selectNoticeList(Map<String, String> params) {
        return boardMapper.selectNoticeList(params);
    }

    public BoardDto selectNoticeDetail(Map<String, String> params) {
        return boardMapper.selectNoticeDetail(params);
    }

    @Transactional
    public int insertNotice(Map<String, String> params, List<AttchFileInfoDto> files, List<AttchFileInfoDto> imgFiles) {

        if (params.get("method").equals("save")) {
            int resultCnt = boardMapper.insertNotice(params);
        }

        if (files != null) {
            fileService.insertNoticeAttchFileInfo(files, params);
        }

        if (imgFiles != null) {
            fileService.insertNoticeAttchFileInfo(imgFiles, params);
        }

        return 1;
    }

    @Transactional
    public int updateDeleteNotice(Map<String, String> params) {
        int result = boardMapper.updateDeleteNotice(params);

        AttchFileInfoDto attchFileInfoDto = new AttchFileInfoDto();
        attchFileInfoDto.setForeignId(params.get("boardId"));
        attchFileInfoDto.setForeignType(params.get("foreignType"));

        fileService.deleteImgFile(attchFileInfoDto);

        return result;
    }

    @Transactional
    public int updateNotice(Map<String, String> params, List<AttchFileInfoDto> files, List<AttchFileInfoDto> imgFiles) {
        int referenceUpdateResult = boardMapper.updateNotice(params);

        fileService.deleteAttchFileInfo(files, params);

        int updateResult = 0;

        if (referenceUpdateResult > 0) {
            updateResult = insertNotice(params, files, imgFiles);
        }

        return updateResult;
    }

    public int selectSuggestListCount(Map<String, String> params) {
        return boardMapper.selectSuggestListCount(params);
    }

    public List<HashMap<String, Object>> selectSuggestList(Map<String, String> params) {
        return boardMapper.selectSuggestList(params);
    }

    public HashMap<String, Object> selectSuggestDetail(Map<String, String> params) {
        return boardMapper.selectSuggestDetail(params);
    }

    @Transactional
    public int updateSaveSuggest(Map<String, String> params, List<AttchFileInfoDto> imgFiles) {

        int result = boardMapper.updateSaveSuggest(params);

        if (params.get("method").equals("modify")) {
            fileService.deleteAttchFileInfo(imgFiles, params);
        }

        if (imgFiles != null) {
            fileService.insertAnswerAttchFileInfo(imgFiles, params);
        }

        return result;
    }

    @Transactional
    public int deleteSuggest(Map<String, String> params) {
        int result = boardMapper.deleteSuggest(params);

        AttchFileInfoDto attchFileInfoDto = new AttchFileInfoDto();
        attchFileInfoDto.setForeignId(params.get("suggestId"));
        attchFileInfoDto.setForeignType("Suggest");

        fileService.deleteImgFile(attchFileInfoDto);

        return result;
    }

    public int selectPartnershipListCount(Map<String, String> params) {
        return boardMapper.selectPartnershipListCount(params);
    }

    /*public List<HashMap<String, Object>> selectPartnershipList(Map<String, String> params) {
        return boardMapper.selectPartnershipList(params);
    }*/
    public List<PartnershipDto> selectPartnershipList(Map<String, String> params) {
        return boardMapper.selectPartnershipList(params);
    }

    public HashMap<String, Object> selectPartnershipDetail(Map<String, String> params) {
        return boardMapper.selectPartnershipDetail(params);
    }

    @Transactional
    public int deletePartnership(Map<String, String> params) {
        return boardMapper.deletePartnership(params);
    }

    public int selectBannerListCnt(Map<String, String> params) {
        return boardMapper.selectBannerListCnt(params);
    }

    public List<HashMap<String, Object>> selectBannerList(Map<String, String> params) {
        return boardMapper.selectBannerList(params);
    }

    @Transactional
    public int insertBanner(Map<String, String> params, List<AttchFileInfoDto> files, List<AttchFileInfoDto> mobileFiles) {

        int resultCnt = 0;

        if (params.get("method").equals("save")) {
            resultCnt = boardMapper.insertBanner(params);
        }


        HashMap<String, Object> mparams = new HashMap<String, Object>();
        mparams.put("referenceId", params.get("referenceId"));


        if (files != null) {
            params.put("subType", "PC");
            fileService.insertBannerAttchFileInfo(files, params);
        }

        if (mobileFiles != null) {
            params.put("subType", "Mobile");
            fileService.insertBannerAttchFileInfo(mobileFiles, params);
        }

        return resultCnt;
    }

    @Transactional
    public int updateBanner(Map<String, String> params, List<AttchFileInfoDto> files, List<AttchFileInfoDto> mobileFiles) {
        int bannerUpdateResult = boardMapper.updateBanner(params);

        fileService.deleteAttchFileInfo(files, params);

        int updateResult = 0;

        if (bannerUpdateResult > 0) {
            updateResult = insertBanner(params, files, mobileFiles);
        }

        return updateResult;
    }

    public HashMap<String, Object> selectBannerInfo(Map<String, String> params) {
        return boardMapper.selectBannerInfo(params);
    }

    @Transactional
    public int updateDeleteBanner(Map<String, String> params) {
        int result = boardMapper.updateDeleteBanner(params);

        return result;
    }

    public List<Map<String, Object>> selectMainBannerList() {
        return boardMapper.selectMainBannerList();
    }

    public List<Map<String, Object>> selectBannerConfigList() {
        return boardMapper.selectBannerConfigList();
    }

    public List<Map<String, Object>> selectSubBannerList(Map<String, String> param){
        return boardMapper.selectSubBannerList(param);
    }

    @Transactional
    public int updateBannerConfig(List<Map<String, String>> param) {
        int result = 0;
        if (param != null) {
            for(Map<String, String> map : param) {
                result = boardMapper.updateBannerConfig(map);
            }
        }
        return result;
    }


    @Transactional
    public int saveSubBanner(Map<String, String> param) {
        int result = 0;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, String>> bannerDataMap = objectMapper.readValue(param.get("bannerList"), new TypeReference<List<Map<String, String>>>() {});

            if (param.size() > 0) {
                boardMapper.deleteSubBannerCross(param);
                for (Map<String, String> map : bannerDataMap) {
                    map.put("bannerConfigId", param.get("bannerConfigId"));
                    result = boardMapper.insertSubBannerCross(map);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    
    public String selectNoticeShowCheck(Map<String, String> params) {
        return boardMapper.selectNoticeShowCheck(params);
    }

}
