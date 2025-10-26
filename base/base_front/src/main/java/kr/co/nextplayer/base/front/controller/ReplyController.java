package kr.co.nextplayer.base.front.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.community.dto.CommunityReqDto;
import kr.co.nextplayer.base.reply.dto.ReplyReqDto;
import kr.co.nextplayer.base.reply.response.ReplyResponse;
import kr.co.nextplayer.base.reply.service.ReplyService;
import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import kr.co.nextplayer.next.lib.common.resolver.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "reply")
@RestController
@RequestMapping("/back_front/base_front/api/v1/reply/")
@RequiredArgsConstructor
@CrossOrigin
public class ReplyController {

    @Resource
    private ReplyService replyService;

    @ApiOperation(value = "댓글 등록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/replySave", method = {RequestMethod.POST})
    public ResponseDto saveReply(@ApiIgnore UserModel userModel, @RequestBody ReplyReqDto replyReqDto) throws Exception {

        try {

            replyReqDto.setMemberCd(userModel.getMemberCd());
            int result = replyService.saveReply(replyReqDto);

            ResponseDto<Object> responseDto = ResponseDto.builder()
                .data(result)
                .build();
            return responseDto;
        } catch (Exception e) {
            ResponseDto<Object> responseDto = ResponseDto.builder().build();
            responseDto.setError(ApiState.SYSTEM.getCode(), "System error");
            return responseDto;
        }
    }

    @ApiOperation(value = "댓글 수정")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/replyModify", method = {RequestMethod.PUT})
    public ResponseDto modifyReply(@ApiIgnore UserModel userModel, @RequestBody ReplyReqDto replyReqDto) {

        try {

            replyReqDto.setMemberCd(userModel.getMemberCd());
            int result = replyService.modifyReply(replyReqDto);

            ResponseDto<Object> responseDto = ResponseDto.builder()
                .data(result)
                .build();
            return responseDto;
        } catch (Exception e) {
            ResponseDto<Object> responseDto = ResponseDto.builder().build();
            responseDto.setError(ApiState.SYSTEM.getCode(), "System error");
            return responseDto;
        }
    }

    @ApiOperation(value = "댓글 삭제")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/replyDelete", method = {RequestMethod.DELETE})
    public ResponseDto deleteReply(@ApiIgnore UserModel userModel, @RequestBody ReplyReqDto replyReqDto) {

        try {

            replyReqDto.setMemberCd(userModel.getMemberCd());
            int result = replyService.deleteReply(replyReqDto);

            ResponseDto<Object> responseDto = ResponseDto.builder()
                .data(result)
                .build();
            return responseDto;
        } catch (Exception e) {
            ResponseDto<Object> responseDto = ResponseDto.builder().build();
            responseDto.setError(ApiState.SYSTEM.getCode(), "System error");
            return responseDto;
        }
    }

    @ApiOperation(value = "댓글 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/replyList", method = {RequestMethod.GET})
    public ResponseDto listReply(@ApiIgnore UserModel userModel, @RequestParam String communityId) {

        try {

            ReplyResponse replyResponse = replyService.replyList(communityId);

            ResponseDto<Object> responseDto = ResponseDto.builder()
                .data(replyResponse)
                .build();
            return responseDto;
        } catch (Exception e) {
            ResponseDto<Object> responseDto = ResponseDto.builder().build();
            responseDto.setError(ApiState.SYSTEM.getCode(), "System error");
            return responseDto;
        }
    }

}
