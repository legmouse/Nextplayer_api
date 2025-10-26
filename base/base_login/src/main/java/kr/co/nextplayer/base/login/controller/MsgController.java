package kr.co.nextplayer.base.login.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.login.service.MemberService;
import kr.co.nextplayer.base.login.service.MessageService;
import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.constants.SystemSettingCode;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.ArrayList;

@Slf4j
@Api(tags = "auth-Certification")
@RequestMapping(path = "/back_login/base_login/api/v1/msg")
@RestController
@CrossOrigin
public class MsgController {

    @Resource
    private MessageService messageService;
    @Resource
    private MemberService memberService;

    @ApiOperation(value = "sms_certification")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true)
    })
    @PostMapping(value = "/sms_certification")
    public Mono<ResponseDto> sms_certification(
        @RequestParam String phoneNo,
        @RequestParam String certGbn,
        @ApiIgnore ServerHttpRequest request) throws Exception {

        try {
            boolean sendResult = false;
            if(certGbn.equals("JOIN")){
                sendResult = messageService.sms_certification(SystemSettingCode.CERT_GBN_JOIN.getCode(), phoneNo);
            }else if(certGbn.equals("FIND")){
                sendResult = messageService.sms_certification(SystemSettingCode.CERT_GBN_FIND.getCode(), phoneNo);
            }

            return Mono.just(ResponseDto.builder().data(sendResult).build());
        } catch (CommonLogicException e) {
            log.error(e.getMessage());
            ResponseDto<Object> responseDto = ResponseDto.builder().build();
            responseDto.setError(ApiState.SYSTEM.getCode(), "System error");
            return Mono.just(responseDto);
        }

    }

    @ApiOperation(value = "certification_check_code")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true)
    })
    @PostMapping(value = "/certification_check_code")
    public Mono<ResponseDto> certification_check_code(
        @RequestParam String phoneNoOrEmail, @RequestParam String verificationCode,
        @ApiIgnore ServerHttpRequest request) throws Exception {

        try {
            Boolean result = messageService.certification_check_code(phoneNoOrEmail, verificationCode);
            return Mono.just(ResponseDto.builder().data(result).build());
        } catch (CommonLogicException e) {
            log.error(e.getMessage());
            ResponseDto<Object> responseDto = ResponseDto.builder().build();
            responseDto.setError(ApiState.SYSTEM.getCode(), "System error");
            return Mono.just(responseDto);
        }

    }


}


