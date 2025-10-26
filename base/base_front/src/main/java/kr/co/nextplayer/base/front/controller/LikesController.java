package kr.co.nextplayer.base.front.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.likes.dto.LikesReqDto;
import kr.co.nextplayer.base.likes.service.LikesService;
import kr.co.nextplayer.base.reply.dto.ReplyReqDto;
import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import kr.co.nextplayer.next.lib.common.resolver.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "likes")
@RestController
@RequestMapping("/back_front/base_front/api/v1/likes/")
@RequiredArgsConstructor
@CrossOrigin
public class LikesController {

    @Resource
    private LikesService likesService;

    @ApiOperation(value = "좋아요 등록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/likesSave", method = {RequestMethod.POST})
    public ResponseDto saveLikes(@ApiIgnore UserModel userModel, @RequestBody LikesReqDto likesReqDto) throws Exception {

        try {

            likesReqDto.setMemberCd(userModel.getMemberCd());
            int result = likesService.insertLike(likesReqDto);

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

    @ApiOperation(value = "좋아요 삭제")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/likesDelete", method = {RequestMethod.DELETE})
    public ResponseDto deleteCommunity(@ApiIgnore UserModel userModel, @RequestBody LikesReqDto likesReqDto) {

        try {

            likesReqDto.setMemberCd(userModel.getMemberCd());
            int result = likesService.deleteLike(likesReqDto);

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

}
