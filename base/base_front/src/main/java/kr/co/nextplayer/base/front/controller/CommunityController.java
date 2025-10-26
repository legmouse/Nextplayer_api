package kr.co.nextplayer.base.front.controller;

import com.sun.jdi.InvalidTypeException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.board.dto.BoardDto;
import kr.co.nextplayer.base.board.dto.ReferenceReqDto;
import kr.co.nextplayer.base.community.dto.*;
import kr.co.nextplayer.base.community.response.CommunityDetailResponse;
import kr.co.nextplayer.base.community.response.CommunityListResponse;
import kr.co.nextplayer.base.community.service.CommunityService;
import kr.co.nextplayer.base.file.dto.FileDto;
import kr.co.nextplayer.base.file.dto.RequestMediaFileDto;
import kr.co.nextplayer.base.file.dto.ResultMediaFileListDto;
import kr.co.nextplayer.base.file.model.MediaFile;
import kr.co.nextplayer.base.file.service.FileService;
import kr.co.nextplayer.base.front.service.BoardService;
import kr.co.nextplayer.base.front.util.Define;
import kr.co.nextplayer.base.front.util.StrUtil;
import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import kr.co.nextplayer.next.lib.common.resolver.UserModel;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Slf4j
@Api(tags = "community")
@RestController
@RequestMapping("/back_front/base_front/api/v1/community/")
@RequiredArgsConstructor
@CrossOrigin
public class CommunityController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private CommunityService communityService;

    @Resource
    private FileService fileService;

    @ApiOperation(value = "커뮤니티 등록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/communitySave", method = {RequestMethod.POST})
    public ResponseDto saveCommunity(@ApiIgnore UserModel userModel, @RequestBody CommunitySaveReqDto communitySaveReqDto) {

        try {

            CommunityReqDto communityReqDto = CommunityReqDto.builder()
                .memberCd(userModel.getMemberCd())
                .title(communitySaveReqDto.getTitle())
                .content(communitySaveReqDto.getContent())
                .type(communitySaveReqDto.getType())
                .subType(communitySaveReqDto.getSubType())
                .fileId(communitySaveReqDto.getFileId())
                .build();

            int result = communityService.saveCommunity(communityReqDto);

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

    @ApiOperation(value = "커뮤니티 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "sKeyword", value = "검색 ", required = false, dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "curPage", required = false, defaultValue = "1", dataTypeClass = Integer.class),
        @ApiImplicitParam(paramType = "query", name = "orderType", value = "정렬 기준(최신순: regDate, 인기순: view)", required = false, defaultValue = "regDate", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "type", value = "커뮤니티 종류(0: 블라인드, 1: 기능추가, 2: 정보수정 요청)", required = false, defaultValue = "0", dataTypeClass = String.class),
    })
    @RequestMapping(value = "/communityList", method = {RequestMethod.GET})
    public ResponseDto communityList(@RequestParam(required = false) String sKeyword, @RequestParam(required = false, defaultValue = "1") Integer curPage,
                                     @RequestParam(defaultValue = "regDate") String orderType, @RequestParam(required = false, defaultValue = "0") String type,
                                     @RequestParam(required = false) String subType) {

        try {

            CommunityListReqDto communityListReqDto = CommunityListReqDto.builder()
                .sKeyword(sKeyword)
                .orderType(orderType)
                .curPage(curPage)
                .type(type)
                .subType(subType)
                .build();

            CommunityListResponse communityResponse = communityService.getCommunityList(communityListReqDto);

            ResponseDto<Object> responseDto = ResponseDto.builder()
                .data(communityResponse)
                .build();
            return responseDto;
        } catch (Exception e) {
            ResponseDto<Object> responseDto = ResponseDto.builder().build();
            responseDto.setError(ApiState.SYSTEM.getCode(), "System error");
            return responseDto;
        }
    }

    @ApiOperation(value = "커뮤니티 상세")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "communityId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/communityDetail", method = {RequestMethod.GET})
    public ResponseDto communityDetail(@RequestParam String communityId, @ApiIgnore UserModel userModel) {
    //public ResponseDto communityDetail(@RequestParam String communityId, @RequestParam String memberCd) {

        try {

            CommunityDetailReqDto communityDetailReqDto = CommunityDetailReqDto.builder()
                .memberCd(userModel.getMemberCd())
                .communityId(communityId)
                .build();

            CommunityDetailResponse communityResponse = communityService.getCommunityDetail(communityDetailReqDto);

            ResponseDto<Object> responseDto = ResponseDto.builder()
                .data(communityResponse)
                .build();
            return responseDto;
        } catch (Exception e) {
            ResponseDto<Object> responseDto = ResponseDto.builder().build();
            responseDto.setError(ApiState.SYSTEM.getCode(), "System error");
            return responseDto;
        }
    }

    @ApiOperation(value = "커뮤니티 수정")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/communityModify", method = {RequestMethod.PUT})
    public ResponseDto communityUpdate(@ApiIgnore UserModel userModel, @RequestBody CommunityUpdateReqDto communityUpdateReqDto) {
        // 회원 인증 필요
        try {

            int result = 0;
            communityUpdateReqDto.setMemberCd(userModel.getMemberCd());
            result = communityService.updateCommunity(communityUpdateReqDto);


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

    @ApiOperation(value = "커뮤니티 삭제")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "community_id", dataTypeClass = String.class),
    })
    @RequestMapping(value = "/communityDelete", method = {RequestMethod.DELETE})
    public ResponseDto communityDelete(@ApiIgnore UserModel userModel, @RequestParam(value = "community_id") String communityId) throws CommonLogicException {
        // 회원 인증 필요
        try {

            int result = 0;
            CommunityUpdateReqDto communityUpdateReqDto = CommunityUpdateReqDto.builder()
                .memberCd(userModel.getMemberCd())
                .communityId(communityId)
                .build();

            result = communityService.deleteCommunity(communityUpdateReqDto);

            ResponseDto<Object> responseDto = ResponseDto.builder()
                .data(result)
                .build();
            return responseDto;
        } catch (Exception e) {
            e.printStackTrace();
            ResponseDto<Object> responseDto = ResponseDto.builder().build();
            responseDto.setError(ApiState.SYSTEM.getCode(), "System error");
            return responseDto;
        }
    }

}
