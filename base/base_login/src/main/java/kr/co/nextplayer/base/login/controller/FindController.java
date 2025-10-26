package kr.co.nextplayer.base.login.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.login.dto.FindCertificationDto;
import kr.co.nextplayer.base.login.dto.FindMemberDto;
import kr.co.nextplayer.base.login.service.FindService;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "auth-find")
@RequestMapping(path = "/back_login/base_login/api/v1/find")
@RestController
@CrossOrigin
public class FindController {

    @Resource
    private FindService findService;


    @ApiOperation(value = "비밀번호 & 아이디 찾기")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true)})
    @PutMapping("/pass-id")
    public Mono<ResponseDto<FindCertificationDto>> findPw( @RequestBody @Validated Mono<FindMemberDto>  monoDto) {

        Mono<ResponseDto<FindCertificationDto>> responseDtoMono = monoDto.flatMap(dto -> {
            try {
                FindCertificationDto result = findService.findConfirm(dto);
                return Mono.just(ResponseDto.<FindCertificationDto>builder().data(result).build());
            } catch (CommonLogicException e) {
                e.printStackTrace();
                return Mono.error(e);
            }
        });

        return responseDtoMono;
    }

    @ApiOperation(value = "비밀번호 재설정하기")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true)})
    @PutMapping("/reset-pass-word")
    public ResponseDto resetPw(@RequestParam String memberId, @RequestParam String memberCd, @RequestParam String newPass) throws CommonLogicException {
        findService.resetPw(memberId, memberCd, newPass);
        return ResponseDto.builder().build();
    }

}


