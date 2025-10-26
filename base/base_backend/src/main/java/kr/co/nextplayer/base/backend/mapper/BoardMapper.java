package kr.co.nextplayer.base.backend.mapper;

import kr.co.nextplayer.base.backend.dto.BoardDto;
import kr.co.nextplayer.base.backend.dto.PartnershipDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BoardMapper {

    HashMap<String, Object> selectQnaListCount(Map<String, String> params);

    List<HashMap<String, Object>> selectQnaList(Map<String, String> params);

    HashMap<String, Object> selectQnaDet(Map<String, String> params);

    int updateQnaUseFlag(Map<String, String> params);

    int selectReferenceListCnt(Map<String, String> params);

    List<HashMap<String, Object>> selectReferenceList(Map<String, String> params);

    int insertReference(Map<String, String> parmas);

    int insertReferenceCross(Map<String, Object> parmas);

    HashMap<String, Object> selectReferenceInfo(Map<String, String> params);

    List<HashMap<String, Object>> selectReferenceChildList(Map<String, String> params);

    int updateDeleteReference(Map<String, String> params);

    int updateDeleteReferenceCross(Map<String, String> params);

    int updateReference(Map<String, String> params);

    int deleteReferenceCross(Map<String, String> params);

    int selectRequestListCount(Map<String, String> params);

    List<HashMap<String, Object>> selectRequestList(Map<String, String> params);

    HashMap<String, Object> selectRequestDetail(Map<String, String> params);

    int updateModifyRequest(Map<String, String> params);

    int deleteRequest(Map<String, String> params);

    int updateCancelAnswerRequest(Map<String, String> params);

    int selectOneToOneListCount(Map<String, String> params);

    List<HashMap<String, Object>> selectOneToOneList(Map<String, String> params);

    HashMap<String, Object> selectOneToOneDetail(Map<String, String> params);

    int updateSaveOneToOne(Map<String, String> params);

    int deleteOneToOneData(Map<String, String> params);

    int selectNoticeListCount(Map<String, String> params);

    //List<HashMap<String, Object>> selectNoticeList(Map<String, String> params);
    List<BoardDto> selectNoticeList(Map<String, String> params);

    int insertNotice(Map<String, String> parmas);

    BoardDto selectNoticeDetail(Map<String, String> params);

    int updateDeleteNotice(Map<String, String> params);

    int updateNotice(Map<String, String> params);

    int selectSuggestListCount(Map<String, String> params);

    List<HashMap<String, Object>> selectSuggestList(Map<String, String> params);

    HashMap<String, Object> selectSuggestDetail(Map<String, String> params);

    int updateSaveSuggest(Map<String, String> params);

    int deleteSuggest(Map<String, String> params);

    int selectPartnershipListCount(Map<String, String> params);

    //List<HashMap<String, Object>> selectPartnershipList(Map<String, String> params);
    List<PartnershipDto> selectPartnershipList(Map<String, String> params);

    HashMap<String, Object> selectPartnershipDetail(Map<String, String> params);

    int deletePartnership(Map<String, String> params);

    int selectBannerListCnt(Map<String, String> params);

    List<HashMap<String, Object>> selectBannerList(Map<String, String> params);

    int insertBanner(Map<String, String> params);

    int updateBanner(Map<String, String> params);

    int updateDeleteBanner(Map<String, String> params);

    HashMap<String, Object> selectBannerInfo(Map<String, String> params);

    int deleteMainBannerData(Map<String, String> param);

    int insertMainBannerData(Map<String, String> param);

    List<Map<String, Object>> selectMainBannerList();

    List<Map<String, Object>> selectBannerConfigList();

    List<Map<String, Object>> selectSubBannerList(Map<String, String> param);

    int updateBannerConfig(Map<String, String> param);

    int insertSubBannerCross(Map<String, String> param);

    int deleteSubBannerCross(Map<String, String> param);
    
    String selectNoticeShowCheck(Map<String, String> param);

}
