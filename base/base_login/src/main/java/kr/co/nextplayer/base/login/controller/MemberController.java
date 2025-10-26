package kr.co.nextplayer.base.login.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.login.dto.*;
//import kr.co.nextplayer.base.login.service.FileService;
import kr.co.nextplayer.base.login.service.MemberService;
import kr.co.nextplayer.base.member.model.Member;
import kr.co.nextplayer.base.member.model.MemberInfo;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import kr.co.nextplayer.next.lib.common.resolver.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.Part;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Api(tags = "auth-Member")
@RequestMapping(path = "/back_login/base_login/api/v1/auth")
@RestController
@CrossOrigin
public class MemberController {

    @Resource
    private MemberService memberService;

    @ApiOperation(value = "개인회원 가입")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true)
    })
    @PostMapping(value = "/personal_register")
    public Mono<ResponseDto> register(
        @RequestBody @Validated Mono<MemberRegisterDto> memberMono
        ,@ApiIgnore ServerHttpRequest request) throws Exception {

        Mono<ResponseDto> responseDtoMono = memberMono.flatMap(member -> {
            try {
                memberService.registerMember(member, request);
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

    @ApiOperation(value = "회원 아이디 중복 확인")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true)})
    @GetMapping("/idUseCheck")
    public ResponseDto<Integer> idUseCheck(@RequestParam String memberId) {
        int idUseCheckCount = memberService.idUseCheck(memberId);
        return ResponseDto.<Integer>builder()
            .data(idUseCheckCount)
            .build();
    }

    @ApiOperation(value = "회원 이메일 중복 확인")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true)})
    @GetMapping("/emailUseCheck")
    public ResponseDto<Integer> eamilUseCheck(@RequestParam String email) {
        int emailUseCheckCount = memberService.emailUseCheck(email);
        return ResponseDto.<Integer>builder()
            .data(emailUseCheckCount)
            .build();
    }

    @ApiOperation(value = "회원 전화번호 중복 확인")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true)})
    @GetMapping("/phoneNoUseCheck")
    public ResponseDto<Integer> phoneNoUseCheck(@RequestParam String phoneNo) {
        int phoneNoUseCheckCount = memberService.phoneNoUseCheck(phoneNo);
        return ResponseDto.<Integer>builder()
            .data(phoneNoUseCheckCount)
            .build();
    }

    @ApiOperation(value = "회원 닉네임 중복 확인")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true)})
    @GetMapping("/nicknameUseCheck")
    public ResponseDto<Integer> nicknameUseCheck(@RequestParam String nickname) {
        int nicknameUseCheckCount = memberService.checkActiveNickname(nickname);
        return ResponseDto.<Integer>builder()
            .data(nicknameUseCheckCount)
            .build();
    }



}


