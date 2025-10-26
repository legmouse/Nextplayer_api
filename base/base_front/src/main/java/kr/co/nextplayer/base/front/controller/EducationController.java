package kr.co.nextplayer.base.front.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.board.dto.BoardReqDto;
import kr.co.nextplayer.base.board.dto.ColumnReqDto;
import kr.co.nextplayer.base.board.dto.EducationAuthReqDto;
import kr.co.nextplayer.base.board.dto.EducationReqDto;
import kr.co.nextplayer.base.front.response.board.*;
import kr.co.nextplayer.base.front.service.EducationService;
import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import kr.co.nextplayer.next.lib.common.resolver.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.Map;
import java.util.logging.Logger;

@Slf4j
@Api(tags = "Education")
@RestController
@RequestMapping("/back_front/base_front/api/v1/education/")
@RequiredArgsConstructor
@CrossOrigin
public class EducationController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private EducationService educationService;

    @ApiOperation(value = "부모 교육 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "curPage", required = false, defaultValue = "1", dataTypeClass = Integer.class),
    })
    @RequestMapping(value = "/educationList", method = {RequestMethod.GET})
    public ResponseDto getEducationList(@ApiIgnore UserModel userModel, @RequestParam(required = false, defaultValue = "1") Integer curPage) throws Exception {

        EducationReqDto educationReqDto = EducationReqDto.builder()
            .memberCd(userModel.getMemberCd())
            .curPage(curPage)
            .build();

        EducationResponse educations = educationService.getEducationList(educationReqDto);

        ResponseDto result = ResponseDto.builder()
            .data(educations)
            .build();

        return result;
    }

    @ApiOperation(value = "부모 교육 상세")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "educationId", required = true),
    })
    @RequestMapping(value = "/educationDetail", method = {RequestMethod.GET})
    public ResponseDto getEducationDetail(@ApiIgnore UserModel userModel, @RequestParam String educationId) throws Exception {

        EducationReqDto educationReqDto = EducationReqDto.builder()
            .memberCd(userModel.getMemberCd())
            .educationId(educationId)
            .build();

        EducationDetailResponse edeDetail = educationService.getEducationDetail(educationReqDto);

        ResponseDto result = ResponseDto.builder()
            .data(edeDetail)
            .build();

        return result;
    }

    @ApiOperation(value = "스토어 교육 구매 권한 연동")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/save_education_auth", method = {RequestMethod.POST})
    public ResponseDto saveEducationAuth(@RequestBody EducationAuthReqDto param) {


        try {

            int result = educationService.addEducationAuth(param);

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

    @ApiOperation(value = "부모 교육 칼럼 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "columnType", value = "칼럼 타입", dataTypeClass = String.class, required = false),
    })
    @RequestMapping(value = "/columnList", method = {RequestMethod.GET})
    public ResponseDto getColumnList(@ApiIgnore UserModel userModel,
                                     @RequestParam(required = false, defaultValue = "") String columnType,
                                     @RequestParam(required = false, defaultValue = "1") Integer curPage) throws Exception {

        ColumnReqDto columnReqDto = ColumnReqDto.builder()
            .memberCd(userModel.getMemberCd())
            .columnType(columnType)
            .curPage(curPage)
            .build();

        ColumnListResponse columnList = educationService.getColumnList(columnReqDto);

        ResponseDto result = ResponseDto.builder()
            .data(columnList)
            .build();

        return result;
    }

    @ApiOperation(value = "부모 교육 칼럼 상세")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "educationColumnId", required = true),
    })
    @RequestMapping(value = "/columnDetail", method = {RequestMethod.GET})
    public ResponseDto getColumnDetail(@ApiIgnore UserModel userModel, @RequestParam String educationColumnId) throws Exception {

        ColumnReqDto columnReqDto = ColumnReqDto.builder()
            .educationColumnId(educationColumnId)
            .build();

        ColumnDetailResponse edeDetail = educationService.getColumnDetail(columnReqDto);

        ResponseDto result = ResponseDto.builder()
            .data(edeDetail)
            .build();

        return result;
    }

    @ApiOperation(value = "피츠인솔 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/educationFileList", method = {RequestMethod.GET})
    public ResponseDto educationFileList(@ApiIgnore UserModel userModel) {

        EducationReqDto educationReqDto = EducationReqDto.builder()
            .memberCd(userModel.getMemberCd())
            .build();

        EducationFileListResponse educations = educationService.getEducationFileList(educationReqDto);

        ResponseDto result = ResponseDto.builder()
            .data(educations)
            .build();

        return result;
    }

}
