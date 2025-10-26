package kr.co.nextplayer.base.front.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.member.dto.*;
import kr.co.nextplayer.base.member.service.MemberFrontService;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import kr.co.nextplayer.next.lib.common.resolver.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Api(tags = "member")
@RestController
@RequestMapping("/back_front/base_front/api/v1/member/")
@RequiredArgsConstructor
@CrossOrigin
public class MemberController {

    @Resource
    MemberFrontService memberService;

    @ApiOperation(value = "회원 정보 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
        //@ApiImplicitParam(paramType = "header", name = "memberCd", dataTypeClass = String.class, required = true)
    })
    @PostMapping(value = "/member_info")
    //public Mono<ResponseDto<MemberInfoResDto>> memberInfo(@RequestBody @Validated Mono<MemberInfoDto> memberMono) {
    public Mono<ResponseDto<MemberInfoResDto>> memberInfo(@ApiIgnore UserModel userModel) {
        Mono<MemberInfoDto> memberMono = Mono.just(new MemberInfoDto());
        Mono<ResponseDto<MemberInfoResDto>> responseDtoMono = memberMono.flatMap(member -> {
            member.setMemberCd(userModel.getMemberCd());
            try {
                MemberInfoResDto memberInfo = memberService.memberInfo(member);
                return Mono.just(ResponseDto.<MemberInfoResDto>builder().data(memberInfo).build());
            } catch (CommonLogicException e) {
                e.printStackTrace();
                return Mono.error(e);
            } catch (JsonProcessingException e) {
                return Mono.error(e);
            }
        });

        return responseDtoMono;
    }

    @ApiOperation(value = "회원 수정 이메일 중복 확인")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @GetMapping("/emailUseCheckForModify")
    public ResponseDto<Integer> eamilUseCheckForModify(@ApiIgnore UserModel userModel, @RequestParam String email) {
        int emailUseCheckCount = memberService.emailUseCheckForModify(email, userModel.getMemberCd());
        return ResponseDto.<Integer>builder()
            .data(emailUseCheckCount)
            .build();
    }

    @ApiOperation(value = "회원 삭제")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @PutMapping("/inactive_member")
    public ResponseDto<Integer> memberInactive(@ApiIgnore UserModel userModel) {
        int deleteMember = memberService.deleteMember(userModel.getMemberCd());
        return ResponseDto.<Integer>builder()
            .data(deleteMember)
            .build();
    }

    @ApiOperation(value = "개인회원 수정")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @PostMapping(value = "/personal_modify")
    public Mono<ResponseDto> modify(
        @ApiIgnore UserModel userModel,
        @RequestBody @Validated Mono<MemberModifyDto> memberMono
        ,@ApiIgnore ServerHttpRequest request) throws Exception {


        Mono<ResponseDto> responseDtoMono = memberMono.flatMap(member -> {
            try {
                member.setMemberCd(userModel.getMemberCd());
                memberService.modifyMember(member, request);
            } catch (CommonLogicException e) {
                e.printStackTrace();
                return Mono.error(e);
            } catch (JsonProcessingException e) {
                return Mono.error(e);
            }

            return Mono.just(ResponseDto.builder().data(1).build());
        });

        return responseDtoMono;
    }

    @ApiOperation(value = "비밀번호 재 확인")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true)
    })
    @GetMapping(value = "/checkPassWord")
    public ResponseDto checkPassWord(@ApiIgnore UserModel userModel, @RequestParam String passWord) throws CommonLogicException {
        memberService.checkPassWord(userModel.getMemberCd(), passWord);
        return ResponseDto.builder().build();
    }

    @ApiOperation(value = "관심 연령, 팀 수정")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @PutMapping(value = "/interest_modify")
    public Mono<ResponseDto> interestModify(@ApiIgnore UserModel userModel, @RequestBody @Validated Mono<MemberInterestReqDto> reqDto, @ApiIgnore ServerHttpRequest request) {

        /*MemberModifyDto memberModifyDto = MemberModifyDto.builder()
            .memberCd(userModel.getMemberCd())
            .interestType(reqDto.getInterestType())
            .interestValue(reqDto.getInterestValue())
            .build();*/

        /*Mono<ResponseDto> responseDtoMono = memberMono.flatMap(member -> {
            try {
                member.setMemberCd(userModel.getMemberCd());
                memberService.modifyMember(member, request);
            } catch (CommonLogicException e) {
                e.printStackTrace();
                return Mono.error(e);
            } catch (JsonProcessingException e) {
                return Mono.error(e);
            }

            return Mono.just(ResponseDto.builder().data(1).build());
        });*/

        Mono<ResponseDto> responseDtoMono = reqDto.flatMap(member -> {
            try {
                member.setMemberCd(userModel.getMemberCd());
                memberService.modifyMemberInterest(member, request);
            } catch (CommonLogicException e) {
                e.printStackTrace();
                return Mono.error(e);
            } catch (JsonProcessingException e) {
                return Mono.error(e);
            }

            return Mono.just(ResponseDto.builder().data(1).build());
        });

        return responseDtoMono;
    }
/*
    @ApiOperation(value = "회원 정보 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
        //@ApiImplicitParam(paramType = "header", name = "memberCd", dataTypeClass = String.class, required = true)
    })
    @PostMapping(value = "/member_info")
    //public Mono<ResponseDto<MemberInfoResDto>> memberInfo(@RequestBody @Validated Mono<MemberInfoDto> memberMono) {
    public ResponseDto memberInfo(@ApiIgnore UserModel userModel) {


        ResponseDto result = ResponseDto.builder()
            .build();

        return result;
    }*/


    @ApiOperation(value = "푸시 세팅 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @GetMapping(value = "/member-push-setting")
    public ResponseDto<MemberPushSettingDto> memberPushSetting(@ApiIgnore UserModel userModel) {
        return ResponseDto.<MemberPushSettingDto>builder()
            .data(memberService.selectMemberPush(userModel.getMemberCd()))
            .build();
    }

    @ApiOperation(value = "푸시 세팅")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @PatchMapping(value = "/member-push-setting")
    public ResponseDto<Boolean> updateMemberPushSetting(@ApiIgnore UserModel userModel, @RequestBody MemberPushSettingDto memberPushSettingDto) {
        memberPushSettingDto.setMemberCd(userModel.getMemberCd());
        memberService.updateMemberPushSetting(memberPushSettingDto);
        return ResponseDto.<Boolean>builder()
            .data(true)
            .build();
    }

    @ApiOperation(value = "푸시 히스토리 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @GetMapping(value = "/member-push-history")
    public ResponseDto<List<MemberPushHistoryDto>> memberPushHistory(@ApiIgnore UserModel userModel) {
        return ResponseDto.<List<MemberPushHistoryDto>>builder()
            .data(memberService.selectPushHistory(userModel.getMemberCd()))
            .build();
    }

    @ApiOperation(value = "안읽은 푸시 있는지 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @GetMapping(value = "/member-push-history-check")
    public ResponseDto<Boolean> memberPushHistoryCheck(@ApiIgnore UserModel userModel) {
        return ResponseDto.<Boolean>builder()
            .data(memberService.selectPushBellCheck(userModel.getMemberCd()))
            .build();
    }

    @ApiOperation(value = "푸시 읽음처리")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @PutMapping(value = "/member-push-read/{pushId}")
    public ResponseDto<Boolean> updateMemberPushRead(@ApiIgnore UserModel userModel, @PathVariable int pushId) {
        memberService.updatePushRead(pushId);
        return ResponseDto.<Boolean>builder()
            .data(true)
            .build();
    }

    @ApiOperation(value = "푸시 읽음처리")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
    })
    @PutMapping(value = "/member-push-read-all")
    public ResponseDto<Boolean> updateMemberPushReadAll(@ApiIgnore UserModel userModel) {
        memberService.updatePushReadAll(userModel.getMemberCd());
        return ResponseDto.<Boolean>builder()
            .data(true)
            .build();
    }
}
