package kr.co.nextplayer.base.front.controller;

import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Parameter;
import kr.co.nextplayer.base.cup.model.Cup;
import kr.co.nextplayer.base.front.model.Uage;
import kr.co.nextplayer.base.front.response.CupResponse;
import kr.co.nextplayer.base.front.response.HomeResponse;
import kr.co.nextplayer.base.front.response.cup.*;
import kr.co.nextplayer.base.front.service.CupService;
import kr.co.nextplayer.base.front.service.HomeService;
import kr.co.nextplayer.base.front.service.UageService;
import kr.co.nextplayer.base.front.util.DateUtil;
import kr.co.nextplayer.base.front.util.StrUtil;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import kr.co.nextplayer.next.lib.common.resolver.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static kr.co.nextplayer.base.front.util.DateUtil.YYYYMMDD;

@Slf4j
@Api(tags = "cup")
@RestController
@RequestMapping("/back_front/base_front/api/v1/")
@RequiredArgsConstructor
@CrossOrigin
public class CupController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private UageService uageService;

    @Resource
    private CupService cupService;

    @ApiOperation(value = "대회 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        //@ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "sYear", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "regionType", dataTypeClass = String.class),
    })
    @GetMapping( "/cup")
    //public ResponseDto cup(@ApiIgnore UserModel userModel, @RequestParam String ageGroup, @RequestParam(required = false) String sYear) {
    public ResponseDto cup(@RequestParam String ageGroup, @RequestParam(required = false) String sYear, @RequestParam(required = false) String regionType) {
        //System.out.println("userModel = " + userModel);
        Cup cupParam = Cup.builder()
            .ageGroup(ageGroup)
            .sYear(sYear)
            .regionType(regionType)
            .build();

        CupListResponse cupListData = cupService.getCupList(cupParam);


        ResponseDto result = ResponseDto.builder()
            .data(cupListData)
            .build();

        return result;
    }

    @ApiOperation(value = "대회 상세")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "cupId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
    })
    @GetMapping( "/cupInfo")
    public ResponseDto cupInfo(@RequestParam String cupId, @RequestParam String ageGroup) {

        String cupInfoTB = ageGroup + "_Cup_Info";

        Cup cupParam = Cup.builder()
            .cupId(cupId)
            .cupInfoTB(cupInfoTB)
            .ageGroup(ageGroup)
            .build();

        CupInfoResponse cupInfoData = cupService.getCupInfo(cupParam);


        ResponseDto result = ResponseDto.builder()
            .data(cupInfoData)
            .build();

        return result;
    }

    @ApiOperation(value = "대회 예선")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "cupId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "groups", dataTypeClass = String.class, required = false),
        @ApiImplicitParam(paramType = "query", name = "teamType", dataTypeClass = String.class, required = false),
    })
    @GetMapping( "/cupSubMatch")
    public ResponseDto cupSubMatch(@RequestParam String cupId, @RequestParam String ageGroup, @RequestParam(required = false) String groups, @RequestParam(required = false) String teamType) {
        String cupTeamTB = ageGroup + "_Cup_Team";
        String cupInfoTB = ageGroup + "_Cup_Info";
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        String cupResultTB = ageGroup + "_Cup_Result";
        String cupSubMatchPlayDataTB = ageGroup + "_Cup_Sub_Match_Play_Data";
        String cupSubMatchChangeDataTB = ageGroup + "_Cup_Sub_Match_Change_Data";

        String cupSubStaffDataTB = ageGroup + "_Cup_Sub_Staff_Data";
        String cupSubStaffPenaltyDataTB = ageGroup + "_Cup_Sub_Staff_Penalty_Data";
        String cupSubOwnGoalDataTB = ageGroup + "_Cup_Sub_Own_Goal_Data";

        Cup cupParam = Cup.builder()
            .cupId(cupId)
            .cupTeamTB(cupTeamTB)
            .cupInfoTB(cupInfoTB)
            .cupSubMatchTB(cupSubMatchTB)
            .cupMainMatchTB(cupMainMatchTB)
            .cupTourMatchTB(cupTourMatchTB)
            .cupResultTB(cupResultTB)
            .cupSubMatchPlayDataTB(cupSubMatchPlayDataTB)
            .cupSubMatchChangeDataTB(cupSubMatchChangeDataTB)
            .cupSubStaffDataTB(cupSubStaffDataTB)
            .cupSubStaffPenaltyDataTB(cupSubStaffPenaltyDataTB)
            .cupSubOwnGoalDataTB(cupSubOwnGoalDataTB)
            .ageGroup(ageGroup)
            .groups(groups)
            .teamType(teamType)
            .build();

        CupSubMatchResponse cupInfoData = cupService.getCupSubMatch(cupParam);


        ResponseDto result = ResponseDto.builder()
            .data(cupInfoData)
            .build();

        return result;
    }

    @ApiOperation(value = "대회 본선")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "cupId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "groups", dataTypeClass = String.class, required = false),
        @ApiImplicitParam(paramType = "query", name = "teamType", dataTypeClass = String.class, required = false),
    })
    @GetMapping( "/cupMainMatch")
    public ResponseDto cupMainMatch(@RequestParam String cupId, @RequestParam String ageGroup, @RequestParam(required = false) String groups, @RequestParam(required = false) String teamType) {

        String cupTeamTB = ageGroup + "_Cup_Team";
        String cupInfoTB = ageGroup + "_Cup_Info";
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        String cupResultTB = ageGroup + "_Cup_Result";
        String cupMainMatchPlayDataTB = ageGroup + "_Cup_Main_Match_Play_Data";
        String cupMainMatchChangeDataTB = ageGroup + "_Cup_Main_Match_Change_Data";

        String cupMainStaffDataTB = ageGroup + "_Cup_Main_Staff_Data";
        String cupMainStaffPenaltyDataTB = ageGroup + "_Cup_Main_Staff_Penalty_Data";
        String cupMainOwnGoalDataTB = ageGroup + "_Cup_Main_Own_Goal_Data";

        Cup cupParam = Cup.builder()
            .cupId(cupId)
            .cupTeamTB(cupTeamTB)
            .cupInfoTB(cupInfoTB)
            .cupSubMatchTB(cupSubMatchTB)
            .cupMainMatchTB(cupMainMatchTB)
            .cupTourMatchTB(cupTourMatchTB)
            .cupResultTB(cupResultTB)
            .cupMainMatchPlayDataTB(cupMainMatchPlayDataTB)
            .cupMainMatchChangeDataTB(cupMainMatchChangeDataTB)
            .cupMainStaffDataTB(cupMainStaffDataTB)
            .cupMainStaffPenaltyDataTB(cupMainStaffPenaltyDataTB)
            .cupMainOwnGoalDataTB(cupMainOwnGoalDataTB)
            .ageGroup(ageGroup)
            .groups(groups)
            .teamType(teamType)
            .build();

        CupMainMatchResponse cupInfoData = cupService.getCupMainMatch(cupParam);


        ResponseDto result = ResponseDto.builder()
            .data(cupInfoData)
            .build();

        return result;
    }

    @ApiOperation(value = "대회 토너먼트")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "cupId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "round", dataTypeClass = String.class, required = false),
        @ApiImplicitParam(paramType = "query", name = "teamType", dataTypeClass = String.class, required = false),
    })
    @GetMapping( "/cupTourMatch")
    public ResponseDto cupTourMatch(@RequestParam String cupId, @RequestParam String ageGroup, @RequestParam(required = false) String round) {
        // @RequestParam @ApiParam(name = "params", required = false, type = "Map<string,string>", value = "Parameters map") Map<String, String> params
        String cupTeamTB = ageGroup + "_Cup_Team";
        String cupInfoTB = ageGroup + "_Cup_Info";
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        String cupResultTB = ageGroup + "_Cup_Result";
        String cupTourMatchPlayDataTB = ageGroup + "_Cup_Tour_Match_Play_Data";
        String cupTourMatchChangeDataTB = ageGroup + "_Cup_Tour_Match_Change_Data";

        String cupTourStaffDataTB = ageGroup + "_Cup_Tour_Staff_Data";
        String cupTourStaffPenaltyDataTB = ageGroup + "_Cup_Tour_Staff_Penalty_Data";
        String cupTourOwnGoalDataTB = ageGroup + "_Cup_Tour_Own_Goal_Data";

        Cup cupParam = Cup.builder()
            .cupId(cupId)
            .cupTeamTB(cupTeamTB)
            .cupInfoTB(cupInfoTB)
            .cupSubMatchTB(cupSubMatchTB)
            .cupMainMatchTB(cupMainMatchTB)
            .cupTourMatchTB(cupTourMatchTB)
            .cupResultTB(cupResultTB)
            .cupTourMatchPlayDataTB(cupTourMatchPlayDataTB)
            .cupTourMatchChangeDataTB(cupTourMatchChangeDataTB)
            .cupTourStaffDataTB(cupTourStaffDataTB)
            .cupTourStaffPenaltyDataTB(cupTourStaffPenaltyDataTB)
            .cupTourOwnGoalDataTB(cupTourOwnGoalDataTB)
            .ageGroup(ageGroup)
            .round(round)
            .build();

        CupTourMatchResponse cupInfoData = cupService.getTourMainMatch(cupParam);

        /*String tourType = cupInfoData.getCupInfoMap().getTourType();

        if (tourType.equals("1")) {
            cupInfoData.setTourType(true);
        } else {
            cupInfoData.setTourType(false);
        }

        if (round == null || round == "") {
            round = cupInfoData.getCupInfoMap().getTourTeam();
        }

        cupInfoData.setRound(round);*/

        ResponseDto result = ResponseDto.builder()
            .data(cupInfoData)
            .build();

        return result;
    }

}
