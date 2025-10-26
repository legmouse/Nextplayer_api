package kr.co.nextplayer.base.front.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.front.dto.ScrapReqDto;
import kr.co.nextplayer.base.front.model.Scrap;
import kr.co.nextplayer.base.front.service.ScrapService;
import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import kr.co.nextplayer.next.lib.common.resolver.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Api(tags = "scrap")
@RestController
@RequestMapping("/back_front/base_front/api/v1/scrap/")
@RequiredArgsConstructor
@CrossOrigin
public class ScrapController {

    @Resource
    private ScrapService scrapService;

    @ApiOperation(value = "스크랩 등록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/saveScrap", method = {RequestMethod.POST})
    public ResponseDto saveScrap(@ApiIgnore UserModel userModel, @RequestBody ScrapReqDto scrapReqDto) throws Exception {

        try {

            scrapReqDto.setMemberCd(userModel.getMemberCd());
            int result = scrapService.insertScrap(scrapReqDto);

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

    @ApiOperation(value = "스크랩 삭제")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/deleteScrap", method = {RequestMethod.DELETE})
    public ResponseDto deleteScrap(@ApiIgnore UserModel userModel, @RequestBody ScrapReqDto scrapReqDto) {

        try {
            scrapReqDto.setMemberCd(userModel.getMemberCd());
            int result = scrapService.deleteScrap(scrapReqDto);

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

    @ApiOperation(value = "스크랩 가져오기")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/selectScrap", method = {RequestMethod.GET})
    public ResponseDto selectScrap(@ApiIgnore UserModel userModel, @ModelAttribute ScrapReqDto scrapReqDto) {

        try {
            scrapReqDto.setMemberCd(userModel.getMemberCd());

            List<Scrap> list = scrapService.selectScrap(scrapReqDto);

            ResponseDto<Object> responseDto = ResponseDto.builder()
                .data(list)
                .build();
            return responseDto;
        } catch (Exception e) {
            ResponseDto<Object> responseDto = ResponseDto.builder().build();
            responseDto.setError(ApiState.SYSTEM.getCode(), "System error");
            return responseDto;
        }
    }

}
