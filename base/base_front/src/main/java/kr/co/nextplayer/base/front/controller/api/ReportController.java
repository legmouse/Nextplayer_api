package kr.co.nextplayer.base.front.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.community.dto.CommunityReqDto;
import kr.co.nextplayer.base.community.dto.CommunitySaveReqDto;
import kr.co.nextplayer.base.community.dto.ReportDto;
import kr.co.nextplayer.base.community.dto.ReportReqDto;
import kr.co.nextplayer.base.community.service.ReportService;
import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import kr.co.nextplayer.next.lib.common.resolver.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "report")
@RestController
@RequestMapping("/back_front/base_front/api/v1/report/")
@RequiredArgsConstructor
@CrossOrigin
public class ReportController {

    @Resource
    private ReportService reportService;

    @ApiOperation(value = "신고 등록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/reportSave", method = {RequestMethod.POST})
    public ResponseDto saveCommunity(@ApiIgnore UserModel userModel, @RequestBody ReportReqDto reportReqDto) {

        try {

            ReportDto reportDto = ReportDto.builder()
                .memberCd(userModel.getMemberCd())
                .foreignId(reportReqDto.getForeignId())
                .foreignType(reportReqDto.getForeignType())
                .reason(reportReqDto.getReason())
                .build();

            int result = reportService.saveReport(reportDto);

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
