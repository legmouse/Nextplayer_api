package kr.co.nextplayer.base.front.service;

import kr.co.nextplayer.base.board.dto.*;
import kr.co.nextplayer.base.board.mapper.BoardMapper;
import kr.co.nextplayer.base.board.model.*;
import kr.co.nextplayer.base.front.response.board.*;
import kr.co.nextplayer.base.media.mapper.MediaFileMapper;
import kr.co.nextplayer.base.media.model.MediaFile;
import kr.co.nextplayer.next.lib.common.mybatis.hander.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class BoardService {

    @Resource
    private BoardMapper boardMapper;

    @Resource
    private MediaFileMapper mediaFileMapper;

    /**
     * QnA 등록
     * @param boardDto
     * @return
     */
    public int insertQna(BoardDto boardDto) {
        return boardMapper.insertQna(boardDto);
    }

    public ReferenceResponse getReferenceList(ReferenceReqDto referenceReqDto) {

        int totalCount = boardMapper.selectReferenceListCount(referenceReqDto);

        PageInfo pageInfo = new PageInfo(referenceReqDto.getCurPage(), referenceReqDto.getPageSize());

        referenceReqDto.setOffset(pageInfo.getOffset());
        referenceReqDto.setPageSize(pageInfo.getPageSize());

        List<Reference> references = boardMapper.selectReferenceList(referenceReqDto);
        List<ReferenceDto> referenceList = new ArrayList<ReferenceDto>();

        if (references.size() > 0) {
            for (Reference ref : references) {
                referenceList.add(new ReferenceDto(ref.getReferenceId(), ref.getTitle(), ref.getContent(), ref.getRegDate(), ref.getFiles()));
            }
        }

        ReferenceResponse referenceResponse = ReferenceResponse.builder()
            .totalCount(totalCount)
            .referenceList(referenceList)
            .build();

        return referenceResponse;
    }

    public ReferenceResponse getReferenceLatestList(ReferenceReqDto referenceReqDto) {

        int totalCount = boardMapper.selectReferenceListCount(referenceReqDto);

        List<Reference> references = boardMapper.selectReferenceLatestList(referenceReqDto);
        List<ReferenceDto> referenceList = new ArrayList<ReferenceDto>();

        if (references.size() > 0) {
            for (Reference ref : references) {
                referenceList.add(new ReferenceDto(ref.getReferenceId(), ref.getTitle(), ref.getContent(), ref.getRegDate(), ref.getFiles()));
            }
        }

        ReferenceResponse referenceResponse = ReferenceResponse.builder()
            .totalCount(totalCount)
            .referenceList(referenceList)
            .build();

        return referenceResponse;
    }

    public ReferenceInfolResponse getReferenceInfo(ReferenceReqDto referenceReqDto) {
        List<Reference> reference = boardMapper.selectReferenceInfo(referenceReqDto);
        List<ReferenceDto> referenceInfo = new ArrayList<ReferenceDto>();
        if (reference.size() > 0) {
            for (Reference ref: reference) {
                referenceInfo.add(new ReferenceDto(ref.getReferenceId(), ref.getTitle(), ref.getContent(), ref.getRegDate(), ref.getFiles()));
            }
        }

        ReferenceInfolResponse referenceInfolResponse = ReferenceInfolResponse.builder()
            .referenceInfo(referenceInfo)
            .build();
        return referenceInfolResponse;
    }

    public ModifyResultResponse insertModifyResult(ModifyResultReqDto modifyResultReqDto) {
        int result = boardMapper.insertModifyResult(modifyResultReqDto);

        if (modifyResultReqDto.getFileId() != null && modifyResultReqDto.getFileId().size() > 0) {
            for (String file : modifyResultReqDto.getFileId()) {
                MediaFile mediaFile = MediaFile.builder()
                    .foreignId(Long.valueOf(modifyResultReqDto.getRequestId()))
                    .id(Long.valueOf(file))
                    .build();

                mediaFileMapper.updateSaveFile(mediaFile);
            }
        }

        ModifyResultResponse resultReqDto = ModifyResultResponse.builder()
            .result(result)
            .build();

        return resultReqDto;
    }

    public BoardInsertResponse insertBoard(BoardReqDto boardReqDto) {
        int result = boardMapper.insertBoard(boardReqDto);

        if (boardReqDto.getFileId() != null && boardReqDto.getFileId().size() > 0) {
            for (String file : boardReqDto.getFileId()) {
                MediaFile mediaFile = MediaFile.builder()
                    .foreignId(Long.valueOf(boardReqDto.getBoardId()))
                    .id(Long.valueOf(file))
                    .build();

                mediaFileMapper.updateSaveFile(mediaFile);
            }
        }

        BoardInsertResponse resultRes = BoardInsertResponse.builder()
            .result(result)
            .build();

        return resultRes;
    }

    public List<Board> selectBoardList(String memberCd){
        return boardMapper.selectBoardList(memberCd);
    }

    public Board selectBoardDetail(String memberCd, String boardId){
        Map map = new HashMap();
        map.put("memberCd", memberCd);
        map.put("boardId", boardId);
        return boardMapper.selectBoardDetail(map);
    }

    public NoticeInfoResponse getMainNotice() throws Exception {

        Notice notice = boardMapper.selectMainNotice();
        NoticeInfoResponse noticeRes = new NoticeInfoResponse(); 
        
        if (notice != null) {
            NoticeInfoDto noticeInfo = NoticeInfoDto.builder()
                    .boardId(notice.getBoardId())
                    .title(notice.getTitle())
                    .build();

            noticeRes = NoticeInfoResponse.builder()
            		.noticeInfo(noticeInfo)
            		.build();
        }

        return noticeRes;
    }

    public NoticeListResponse getNoticeList(BoardReqDto boardReqDto) throws Exception {

        int totalCount = 0;

        List<NoticeListDto> list = new ArrayList<NoticeListDto>();

        try {

            totalCount = boardMapper.selectNoticeListCount(boardReqDto);

            PageInfo pageInfo = new PageInfo(boardReqDto.getCurPage(), 10);
            boardReqDto.setOffset(pageInfo.getOffset());
            boardReqDto.setPageSize(pageInfo.getPageSize());

            List<Notice> notices = boardMapper.selectNoticeList(boardReqDto);

            if (notices.size() > 0) {
                for (Notice notice : notices) {
                    NoticeListDto noticeListDto = NoticeListDto.builder()
                        .boardId(notice.getBoardId())
                        .title(notice.getTitle())
                        .useFlag(notice.getUseFlag())
                        .regDate(notice.getRegDate())
                        .parseDate(notice.getParseDate())
                        .thumbImg(notice.getThumbImg())
                        .build();

                    list.add(noticeListDto);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        NoticeListResponse noticeRes = NoticeListResponse.builder()
            .totalCount(totalCount)
            .noticeList(list)
            .build();

        return noticeRes;
    }

    public NoticeInfoResponse getNoticeInfo(BoardReqDto boardReqDto) throws Exception {

        Notice notices = boardMapper.selectNoticeInfo(boardReqDto);

        Map<String, String> param = new HashMap<String, String>();
        param.put("boardId", notices.getBoardId());

        Long bfViewCnt = notices.getViewCnt();
        param.put("viewCnt", String.valueOf(bfViewCnt+1));

        boardMapper.updateBoardViewCnt(param);

        NoticeInfoDto notice = NoticeInfoDto.builder()
            .boardId(notices.getBoardId())
            .title(notices.getTitle())
            .content(notices.getContent())
            .regDate(notices.getRegDate())
            .noticeFiles(notices.getNoticeFiles())
            .imgFiles(notices.getImgFiles())
            .build();

        NoticeInfoResponse noticeRes = NoticeInfoResponse.builder()
            .noticeInfo(notice)
            .build();
        return noticeRes;
    }

    public BannerListResponse getBannerList() throws Exception {

        int totalCount = 0;
        List<Banner> banners = new ArrayList<Banner>();

        try {

            banners = boardMapper.selectBannerList();

        } catch (Exception e) {
            e.printStackTrace();
        }

        BannerListResponse bannerRes = BannerListResponse.builder()
            .bannerList(banners)
            .build();

        return bannerRes;
    }

    public PopupListResponse getPopupList() throws Exception {

        int totalCount = 0;
        List<Popup> popups = new ArrayList<Popup>();

        try {

            popups = boardMapper.selectPopupList();
            totalCount = popups.size();

        } catch (Exception e) {
            e.printStackTrace();
        }

        PopupListResponse popupRes = PopupListResponse.builder()
            .totalCount(totalCount)
            .popupList(popups)
            .build();

        return popupRes;
    }

    public BannerListResponse getSubBannerList(String key, String uage) {

        int totalCount = 0;
        List<Banner> banners = new ArrayList<Banner>();
        try {

            banners = boardMapper.selectSubBannerList(key, uage);

        } catch (Exception e) {
            e.printStackTrace();
        }

        BannerListResponse bannerRes = BannerListResponse.builder()
            .bannerList(banners)
            .build();

        return bannerRes;
    }

}
