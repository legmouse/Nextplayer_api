package kr.co.nextplayer.base.board.mapper;

import kr.co.nextplayer.base.board.dto.*;
import kr.co.nextplayer.base.board.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BoardMapper {

    int insertQna(BoardDto boardDto);

    int selectReferenceListCount(ReferenceReqDto referenceDto);

    List<Reference> selectReferenceList(ReferenceReqDto referenceDto);

    List<Reference> selectReferenceLatestList(ReferenceReqDto referenceDto);

    List<Reference> selectReferenceInfo(ReferenceReqDto referenceReqDto);

    int insertModifyResult(ModifyResultReqDto modifyResultReqDto);

    int insertBoard(BoardReqDto boardReqDto);

    List<Board> selectBoardList(String memberCd);

    Board selectBoardDetail(Map map);

    int selectNoticeListCount(BoardReqDto boardReqDto);

    Notice selectMainNotice();

    List<Notice> selectNoticeList(BoardReqDto boardReqDto);

    Notice selectNoticeInfo(BoardReqDto boardReqDto);

    int updateBoardViewCnt(Map<String, String> params);

    List<Banner> selectBannerList();

    int updateViewCnt(Map<String ,String> param);

    List<Popup> selectPopupList();

    List<Banner> selectSubBannerList(@Param("key") String key, @Param("uage") String uage);

}

