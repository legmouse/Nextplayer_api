package kr.co.nextplayer.base.front.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.board.dto.BoardDto;
import kr.co.nextplayer.base.board.dto.BoardReqDto;
import kr.co.nextplayer.base.board.dto.ModifyResultReqDto;
import kr.co.nextplayer.base.board.dto.ReferenceReqDto;
import kr.co.nextplayer.base.board.model.Board;
import kr.co.nextplayer.base.board.model.Notice;
import kr.co.nextplayer.base.board.model.Request;
import kr.co.nextplayer.base.front.response.board.*;
import kr.co.nextplayer.base.front.service.BoardService;
import kr.co.nextplayer.base.front.util.Define;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import kr.co.nextplayer.next.lib.common.resolver.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Slf4j
@Api(tags = "board")
@RestController
@RequestMapping("/back_front/base_front/api/v1/board/")
@RequiredArgsConstructor
@CrossOrigin
public class BoardController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private BoardService boardService;

    @ApiOperation(value = "QnA 등록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/save_qna", method = {RequestMethod.POST})
    public ResponseDto saveQna(@RequestParam Map<String, String> params) {

        int result = 0;

        String sFlag = params.get("sFlag");

        ResponseDto responseDto = null;

        if(sFlag.equals(Define.MODE_ADD)) {
            BoardDto boardDto = BoardDto.builder()
                .customer(params.get("customer"))
                .phone(params.get("phone"))
                .email(params.get("email"))
                .title(params.get("title"))
                .content(params.get("content"))
                .build();
            result = boardService.insertQna(boardDto);

            responseDto = ResponseDto.builder()
                .build();

        }
        return responseDto;
    }

    @ApiOperation(value = "경기 결과 수정 요청 등록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/save_modifyResult", method = {RequestMethod.POST})
    public ResponseDto saveResultModify(@ApiIgnore UserModel userModel, @RequestBody ModifyResultReqDto params) {


        params.setMemberCd(userModel.getMemberCd());
        //params.setMemberCd("test");
        ModifyResultResponse resultResponse = boardService.insertModifyResult(params);

        ResponseDto result = ResponseDto.builder()
            .data(resultResponse)
            .build();


        return result;

    }

    @ApiOperation(value = "1:1 문의 등록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/save_oneToOne", method = {RequestMethod.POST})
    public ResponseDto saveOneToOne(@ApiIgnore UserModel userModel, @RequestBody BoardReqDto params) {


        params.setMemberCd(userModel.getMemberCd());
        BoardInsertResponse resultResponse = boardService.insertBoard(params);

        ResponseDto result = ResponseDto.builder()
            .data(resultResponse)
            .build();


        return result;
    }

    @ApiOperation(value = "1:1 문의 내역")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/get_oneToOne", method = {RequestMethod.GET})
    public ResponseDto getOneToOne(@ApiIgnore UserModel userModel) {

        List<Board> boards = boardService.selectBoardList(userModel.getMemberCd());

        ResponseDto result = ResponseDto.builder()
            .data(boards)
            .build();

        return result;
    }

    @ApiOperation(value = "1:1 문의 내역 상세")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/get_oneToOneDetail", method = {RequestMethod.GET})
    public ResponseDto getOneToOneDetail(@RequestParam String boardId, @ApiIgnore UserModel userModel) {

        Board board = boardService.selectBoardDetail(userModel.getMemberCd(), boardId);

        ResponseDto result = ResponseDto.builder()
            .data(board)
            .build();

        return result;
    }

    @ApiOperation(value = "메인 공지사항")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/noticeMain", method = {RequestMethod.GET})
    public ResponseDto getMainNotice() throws Exception {


        NoticeInfoResponse notices = boardService.getMainNotice();

        ResponseDto result = ResponseDto.builder()
            .data(notices)
            .build();

        return result;
    }

    @ApiOperation(value = "공지사항 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "sKeyword", value = "검색 ", required = false, dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "curPage", required = false, defaultValue = "1", dataTypeClass = Integer.class),
    })
    @RequestMapping(value = "/noticeList", method = {RequestMethod.GET})
    public ResponseDto getNotice(@RequestParam(required = false) String sKeyword, @RequestParam(required = false, defaultValue = "1") Integer curPage) throws Exception {

        BoardReqDto boardReqDto = BoardReqDto.builder()
            .sKeyword(sKeyword)
            .curPage(curPage)
            .build();

        NoticeListResponse notices = boardService.getNoticeList(boardReqDto);

        ResponseDto result = ResponseDto.builder()
            .data(notices)
            .build();

        return result;
    }

    @ApiOperation(value = "공지사항 상세")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "boardId", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/noticeDetail")
    public ResponseDto referenceDetail(@RequestParam String boardId) throws Exception {

        BoardReqDto boardReqDto = BoardReqDto.builder()
            .boardId(boardId)
            .build();

        NoticeInfoResponse noticeInfoResponse = boardService.getNoticeInfo(boardReqDto);

        ResponseDto result = ResponseDto.builder()
            .data(noticeInfoResponse)
            .build();

        return result;
    }

    @ApiOperation(value = "배너 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/bannerList", method = {RequestMethod.GET})
    public ResponseDto getBannerList() throws Exception {


        BannerListResponse bannerListResponse = boardService.getBannerList();

        ResponseDto result = ResponseDto.builder()
            .data(bannerListResponse)
            .build();

        return result;
    }

    @ApiOperation(value = "팝업 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/popupList", method = {RequestMethod.GET})
    public ResponseDto getPopupList() throws Exception {


        PopupListResponse popupListResponse = boardService.getPopupList();

        ResponseDto result = ResponseDto.builder()
            .data(popupListResponse)
            .build();

        return result;
    }

    @ApiOperation(value = "서브 배너 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "key", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/subBannerList", method = {RequestMethod.GET})
    public ResponseDto getSubBannerList(@RequestParam String key, @RequestParam(required = false) String uage) {


        BannerListResponse bannerListResponse = boardService.getSubBannerList(key, uage);

        ResponseDto result = ResponseDto.builder()
            .data(bannerListResponse)
            .build();

        return result;
    }

}
