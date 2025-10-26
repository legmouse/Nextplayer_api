package kr.co.nextplayer.base.front.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.board.dto.ReferenceDto;
import kr.co.nextplayer.base.board.dto.ReferenceReqDto;
import kr.co.nextplayer.base.front.response.board.ReferenceInfolResponse;
import kr.co.nextplayer.base.front.response.board.ReferenceResponse;
import kr.co.nextplayer.base.front.response.media.MediaResponse;
import kr.co.nextplayer.base.front.service.BoardService;
import kr.co.nextplayer.base.media.dto.MediaReqDto;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.logging.Logger;

@Slf4j
@Api(tags = "reference")
@RestController
@RequestMapping("/back_front/base_front/api/v1/")
@RequiredArgsConstructor
@CrossOrigin
public class ReferenceController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private BoardService boardService;

    @ApiOperation(value = "자료실 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/referenceList")
    public ResponseDto referenceList(@RequestParam int curPage, @RequestParam int pageSize, @RequestParam(required = false) String sKeyword) {

        ReferenceReqDto referenceDto = ReferenceReqDto.builder()
            .curPage(curPage)
            .pageSize(pageSize)
            .sKeyword(sKeyword)
            .build();

        ReferenceResponse referenceResponse = boardService.getReferenceList(referenceDto);

        ResponseDto result = ResponseDto.builder().data(referenceResponse).build();

        return result;
    }

    @ApiOperation(value = "최신 10개 자료실 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/referenceLatestList")
    public ResponseDto referenceLatestList() {

        ReferenceReqDto referenceDto = ReferenceReqDto.builder()
            .build();

        ReferenceResponse referenceResponse = boardService.getReferenceLatestList(referenceDto);

        ResponseDto result = ResponseDto.builder().data(referenceResponse).build();

        return result;
    }


    @ApiOperation(value = "자료실 상세")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "referenceId", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/referenceDetail")
    public ResponseDto referenceDetail(@RequestParam String referenceId) {

        ReferenceReqDto referenceDto = ReferenceReqDto.builder()
            .referenceId(referenceId)
            .build();

        ReferenceInfolResponse referenceResponse = boardService.getReferenceInfo(referenceDto);

        ResponseDto result = ResponseDto.builder().data(referenceResponse).build();

        return result;
    }

}
