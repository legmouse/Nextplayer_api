package kr.co.nextplayer.base.front.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.cup.model.Cup;
import kr.co.nextplayer.base.front.response.CupResponse;
import kr.co.nextplayer.base.front.service.CupService;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Api(tags = "cup api")
@RestController
@RequestMapping("/back_front/base_front/api/v1/cup_service")
@RequiredArgsConstructor
@CrossOrigin
public class CupApiController {

    @Resource
    private CupService cupService;

    @ApiOperation(value = "대회 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "sYear", dataTypeClass = String.class),
    })
    @GetMapping("/cupList")
    public ResponseDto cupList(@RequestParam String ageGroup, @RequestParam String sYear) {

        String cupInfoTB = ageGroup + "_Cup_Info";

        Cup cupParam = Cup.builder()
            .ageGroup(ageGroup)
            .cupInfoTB(cupInfoTB)
            .sYear(sYear)
            .build();

        ResponseDto result = ResponseDto.builder()
            .data(cupService.selectCupList(cupParam))
            .build();

        return result;
    }

    @ApiOperation(value = "대회 상세")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "cupId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
    })
    @GetMapping("/cupInfo")
    public ResponseDto cupInfo(@RequestParam String cupId, @RequestParam String ageGroup) {

        String cupInfoTB = ageGroup + "_Cup_Info";

        Cup cupParam = Cup.builder()
            .cupId(cupId)
            .cupInfoTB(cupInfoTB)
            .ageGroup(ageGroup)
            .build();

        ResponseDto result = ResponseDto.builder()
            .data(cupService.selectCupInfo(cupParam))
            .build();

        return result;
    }

    @ApiOperation(value = "조별 대회 예선 참가 팀 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "cupId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "groups", dataTypeClass = String.class, required = false),
    })
    @GetMapping("/cupSubTeamList")
    public ResponseDto cupSubTeamList(
            @RequestParam String cupId,
            @RequestParam String ageGroup,
            @RequestParam(name = "groups", required = false) String groups
    ) {

        String cupTeamTB = ageGroup + "_Cup_Team";

        Cup cupParam = Cup.builder()
            .cupId(cupId)
            .cupTeamTB(cupTeamTB)
            .ageGroup(ageGroup)
            .groups(groups)
            .build();

        ResponseDto result = ResponseDto.builder()
            .data(cupService.selectCupSubTeam(cupParam))
            .build();

        return result;
    }

    @ApiOperation(value = "조별 대회 예선 순위 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "cupId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "groups", dataTypeClass = String.class, required = false),
    })
    @GetMapping("/cupSubRankList")
    public ResponseDto cupSubRankList(
        @RequestParam String cupId,
        @RequestParam String ageGroup,
        @RequestParam(name = "groups", required = false) String groups
    ) {

        String cupTeamTB = ageGroup + "_Cup_Team";
        String cupResultTB = ageGroup + "_Cup_Result";

        Cup cupParam = Cup.builder()
            .cupId(cupId)
            .cupResultTB(cupResultTB)
            .cupTeamTB(cupTeamTB)
            .ageGroup(ageGroup)
            .groups(groups)
            .build();

        ResponseDto result = ResponseDto.builder()
            .data(cupService.selectCupSubMatchRankByFinal(cupParam))
            .build();

        return result;
    }

    @ApiOperation(value = "조별 대회 예선 일정 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "cupId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "groups", dataTypeClass = String.class, required = false),
    })
    @GetMapping("/cupSubMatchCalendar")
    public ResponseDto cupSubMatchCalendar(
        @RequestParam String cupId,
        @RequestParam String ageGroup,
        @RequestParam(name = "groups", required = false) String groups
    ) {

        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";

        Cup cupParam = Cup.builder()
            .cupId(cupId)
            .cupSubMatchTB(cupSubMatchTB)
            .ageGroup(ageGroup)
            .groups(groups)
            .build();

        ResponseDto result = ResponseDto.builder()
            .data(cupService.selectCupSubMatchCalendar(cupParam))
            .build();

        return result;
    }

    @ApiOperation(value = "조별 대회 예선 결과 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "cupId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "groups", dataTypeClass = String.class, required = false),
    })
    @GetMapping("/cupSubMatchList")
    public ResponseDto cupSubMatchList(
        @RequestParam String cupId,
        @RequestParam String ageGroup,
        @RequestParam(name = "groups", required = false) String groups
    ) {

        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";

        Cup cupParam = Cup.builder()
            .cupId(cupId)
            .cupSubMatchTB(cupSubMatchTB)
            .ageGroup(ageGroup)
            .groups(groups)
            .build();

        ResponseDto result = ResponseDto.builder()
            .data(cupService.selectCupSubMatchResultList(cupParam))
            .build();

        return result;
    }

    @ApiOperation(value = "조별 대회 본선 참가 팀 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "cupId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "groups", dataTypeClass = String.class, required = false),
    })
    @GetMapping("/cupMainTeamList")
    public ResponseDto cupMainTeamList(
        @RequestParam String cupId,
        @RequestParam String ageGroup,
        @RequestParam(name = "groups", required = false) String groups
    ) {

        String cupTeamTB = ageGroup + "_Cup_Team";

        Cup cupParam = Cup.builder()
            .cupId(cupId)
            .cupTeamTB(cupTeamTB)
            .ageGroup(ageGroup)
            .groups(groups)
            .build();

        ResponseDto result = ResponseDto.builder()
            .data(cupService.selectCupMainTeam(cupParam))
            .build();

        return result;
    }

    @ApiOperation(value = "조별 대회 본선 순위 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "cupId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "groups", dataTypeClass = String.class, required = false),
    })
    @GetMapping("/cupMainRankList")
    public ResponseDto cupMainRankList(
        @RequestParam String cupId,
        @RequestParam String ageGroup,
        @RequestParam(name = "groups", required = false) String groups
    ) {

        String cupTeamTB = ageGroup + "_Cup_Team";
        String cupResultTB = ageGroup + "_Cup_Result";

        Cup cupParam = Cup.builder()
            .cupId(cupId)
            .cupResultTB(cupResultTB)
            .cupTeamTB(cupTeamTB)
            .ageGroup(ageGroup)
            .groups(groups)
            .build();

        ResponseDto result = ResponseDto.builder()
            .data(cupService.selectCupMainMatchRankByFinal(cupParam))
            .build();

        return result;
    }

    @ApiOperation(value = "조별 대회 본선 일정 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "cupId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "groups", dataTypeClass = String.class, required = false),
    })
    @GetMapping("/cupMainMatchCalendar")
    public ResponseDto cupMainMatchCalendar(
        @RequestParam String cupId,
        @RequestParam String ageGroup,
        @RequestParam(name = "groups", required = false) String groups
    ) {

        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";

        Cup cupParam = Cup.builder()
            .cupId(cupId)
            .cupMainMatchTB(cupMainMatchTB)
            .ageGroup(ageGroup)
            .groups(groups)
            .build();

        ResponseDto result = ResponseDto.builder()
            .data(cupService.selectCupMainMatchCalendar(cupParam))
            .build();

        return result;
    }

    @ApiOperation(value = "조별 대회 본선 결과 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "cupId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "groups", dataTypeClass = String.class, required = false),
    })
    @GetMapping("/cupMainMatchList")
    public ResponseDto cupMainMatchList(
        @RequestParam String cupId,
        @RequestParam String ageGroup,
        @RequestParam(name = "groups", required = false) String groups
    ) {

        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";

        Cup cupParam = Cup.builder()
            .cupId(cupId)
            .cupMainMatchTB(cupMainMatchTB)
            .ageGroup(ageGroup)
            .groups(groups)
            .build();

        ResponseDto result = ResponseDto.builder()
            .data(cupService.selectCupMainMatchResultList(cupParam))
            .build();

        return result;
    }

    @ApiOperation(value = "조별 대회 본선 결과 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "cupId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
    })
    @GetMapping("/cupTourEmptyMatchList")
    public ResponseDto cupTourEmptyMatchList(@RequestParam String cupId, @RequestParam String ageGroup) {

        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";

        Cup cupParam = Cup.builder()
            .cupId(cupId)
            .cupTourMatchTB(cupTourMatchTB)
            .ageGroup(ageGroup)
            .build();

        ResponseDto result = ResponseDto.builder()
            .data(cupService.selectCupTourEmptyMatchList(cupParam))
            .build();

        return result;
    }

    @ApiOperation(value = "조별 대회 본선 결과 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "cupId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
    })
    @GetMapping("/cupTourMatchList")
    public ResponseDto cupTourMatchList(@RequestParam String cupId, @RequestParam String ageGroup) {

        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        String cupResultTB = ageGroup + "_Cup_Result";

        Cup cupParam = Cup.builder()
            .cupId(cupId)
            .cupTourMatchTB(cupTourMatchTB)
            .cupResultTB(cupResultTB)
            .ageGroup(ageGroup)
            .build();

        ResponseDto result = ResponseDto.builder()
            .data(cupService.selectCupTourMatchResultList(cupParam))
            .build();

        return result;
    }

}
