package kr.co.nextplayer.base.front.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.front.service.UageService;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "uage api")
@RestController
@RequestMapping("/back_front/base_front/api/v1/uage_service")
@RequiredArgsConstructor
@CrossOrigin
public class UageApiController {

    @Resource
    private UageService uageService;

    @ApiOperation(value = "연령 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @GetMapping("/uageList")
    public ResponseDto uageList() {

        ResponseDto result = ResponseDto.builder()
            .data(uageService.selectUageList())
            .build();

        return result;
    }

    @ApiOperation(value = "지역 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @GetMapping("/areaList")
    public ResponseDto areaList(@RequestParam String ageGroup) {

        ResponseDto result = ResponseDto.builder()
            .data(uageService.selectAreaList(ageGroup))
            .build();

        return result;
    }

    @ApiOperation(value = "연령별 지역 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class, required = true),
    })
    @GetMapping("/areaListByAgeGroup")
    public ResponseDto areaListByUageList(String ageGroup) {

        ResponseDto result = ResponseDto.builder()
            .data(uageService.selectAreaByUageList(ageGroup))
            .build();

        return result;
    }

}
