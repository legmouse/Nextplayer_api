package kr.co.nextplayer.base.front.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.community.dto.*;
import kr.co.nextplayer.base.community.response.RequestListResponse;
import kr.co.nextplayer.base.community.response.RequestSearchResponse;
import kr.co.nextplayer.base.community.response.SuggestListResponse;
import kr.co.nextplayer.base.community.service.RequestService;
import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import kr.co.nextplayer.next.lib.common.resolver.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(tags = "request")
@RestController
@RequestMapping("/back_front/base_front/api/v1/request/")
@RequiredArgsConstructor
@CrossOrigin
public class RequestController {

    @Resource
    private RequestService requestService;

    @ApiOperation(value = "정보 수정 요청 구분 검색")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", value = "연령", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "sKeyword", value = "검색 키워드", dataTypeClass = String.class, required = false),
        @ApiImplicitParam(paramType = "query", name = "sType", value="검색 구분(경기 0: , 팀 : 1)", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "parentType", value="경기 구분(대회 : Cup, 리그 : League)", dataTypeClass = String.class, required = false),
        @ApiImplicitParam(paramType = "query", name = "sDate", value="경기 일자", dataTypeClass = String.class, required = false),
    })
    @RequestMapping(value = "/requestSearchType", method = {RequestMethod.GET})
    public ResponseDto searchRequestType(@ApiIgnore UserModel userModel, @RequestParam String ageGroup,
                                         @RequestParam(required = false) String sKeyword, @RequestParam String sType,
                                         @RequestParam(required = false) String parentType, @RequestParam(required = false) String sDate) {

        try {

            Map params = new HashMap();
            params.put("ageGroup", ageGroup);
            params.put("sKeyword", sKeyword);
            params.put("sType", sType);
            params.put("parentType", parentType);
            params.put("sDate", sDate);

            RequestSearchResponse requestSearchResponse = requestService.getSearchInfo(params);

            ResponseDto<Object> responseDto = ResponseDto.builder()
                .data(requestSearchResponse)
                .build();
            return responseDto;
        } catch (Exception e) {
            ResponseDto<Object> responseDto = ResponseDto.builder().build();
            responseDto.setError(ApiState.SYSTEM.getCode(), "System error");
            return responseDto;
        }
    }

    @ApiOperation(value = "정보 수정 요청 등록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/requestSave", method = {RequestMethod.POST})
    public ResponseDto saveRequest(@ApiIgnore UserModel userModel, @RequestBody RequestReqDto requestReqDto) {

        try {

            RequestDto requestDto = RequestDto.builder()
                .memberCd(userModel.getMemberCd())
                .ageGroup(requestReqDto.getAgeGroup())
                .parentId(requestReqDto.getParentId())
                .parentType(requestReqDto.getParentType())
                .foreignId(requestReqDto.getForeignId())
                .foreignType(requestReqDto.getForeignType())
                .content(requestReqDto.getContent())
                .requestType(requestReqDto.getRequestType())
                .fileId(requestReqDto.getFileId())
                .build();

            int result = requestService.saveRequest(requestDto);

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

    @ApiOperation(value = "정보 수정 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "sKeyword", value = "검색 ", required = false, dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "curPage", required = false, defaultValue = "1", dataTypeClass = Integer.class),
        @ApiImplicitParam(paramType = "query", name = "sType", value = "정보수정 요청 타입 (경기: 0, 팀/기타: 1)", defaultValue = "0", required = true, dataTypeClass = String.class),
    })
    @RequestMapping(value = "/requestList", method = {RequestMethod.GET})
    public ResponseDto requestList(@ApiIgnore UserModel userModel, @RequestParam(required = false) String sKeyword,
                                   @RequestParam(required = true, defaultValue = "0") String sType,
                                   @RequestParam(required = false, defaultValue = "1") Integer curPage) {

        try {

            RequestListDto requestListDto = RequestListDto.builder()
                .memberCd(userModel.getMemberCd())
                .sKeyword(sKeyword)
                .sType(sType)
                .curPage(curPage)
                .build();


            RequestListResponse requestListResponse = requestService.getRequestList(requestListDto);

            ResponseDto<Object> responseDto = ResponseDto.builder()
                .data(requestListResponse)
                .build();
            return responseDto;
        } catch (Exception e) {
            ResponseDto<Object> responseDto = ResponseDto.builder().build();
            responseDto.setError(ApiState.SYSTEM.getCode(), "System error");
            return responseDto;
        }
    }

    @ApiOperation(value = "컨텐츠 추가 요청 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "sKeyword", value = "검색 ", required = false, dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "curPage", required = false, defaultValue = "1", dataTypeClass = Integer.class),
        /*@ApiImplicitParam(paramType = "query", name = "sType", value = "내가 쓴 글만 보기(0: false, 1: true),", defaultValue = "0", required = false, dataTypeClass = String.class),*/
    })
    @RequestMapping(value = "/suggestList", method = {RequestMethod.GET})
    public ResponseDto suggestList(@ApiIgnore UserModel userModel, @RequestParam(required = false) String sKeyword,
                                   /*@RequestParam(required = false, defaultValue = "0") String sType,*/
                                   @RequestParam(required = false, defaultValue = "1") Integer curPage) {

        try {

            SuggestListDto suggestListDto = SuggestListDto.builder()
                .memberCd(userModel.getMemberCd())
                .sKeyword(sKeyword)
                /*.sType(sType)*/
                .curPage(curPage)
                .build();

            SuggestListResponse suggestListResponse = requestService.getSuggestList(suggestListDto);

            ResponseDto<Object> responseDto = ResponseDto.builder()
                .data(suggestListResponse)
                .build();
            return responseDto;
        } catch (Exception e) {
            ResponseDto<Object> responseDto = ResponseDto.builder().build();
            responseDto.setError(ApiState.SYSTEM.getCode(), "System error");
            return responseDto;
        }
    }

    @ApiOperation(value = "컨텐츠 추가 요청 등록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/suggestSave", method = {RequestMethod.POST})
    public ResponseDto saveSuggest(@ApiIgnore UserModel userModel, @RequestBody SuggestReqDto suggestReqDto) {

        try {

            SuggestDto suggestDto = SuggestDto.builder()
                .memberCd(userModel.getMemberCd())
                .title(suggestReqDto.getTitle())
                .content(suggestReqDto.getContent())
                .secretFlag(suggestReqDto.getSecretFlag())
                .fileId(suggestReqDto.getFileId())
                .build();

            int result = requestService.saveSuggest(suggestDto);

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

    @ApiOperation(value = "제휴 문의 등록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/partnershipSave", method = {RequestMethod.POST})
    public ResponseDto savePartnership(@ApiIgnore UserModel userModel, @RequestBody PartnershipReqDto partnershipReqDto) {

        try {

            PartnershipDto partnershipDto = PartnershipDto.builder()
                .memberCd(userModel.getMemberCd())
                .phone(partnershipReqDto.getPhone())
                .email(partnershipReqDto.getEmail())
                .customer(partnershipReqDto.getCustomer())
                .email(partnershipReqDto.getEmail())
                .content(partnershipReqDto.getContent())
                .fileId(partnershipReqDto.getFileId())
                .build();

            int result = requestService.savePartnership(partnershipDto);

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
