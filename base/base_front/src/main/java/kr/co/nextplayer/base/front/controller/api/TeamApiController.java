package kr.co.nextplayer.base.front.controller.api;

import io.swagger.annotations.Api;
import kr.co.nextplayer.base.front.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "team api")
@RestController
@RequestMapping("/back_front/base_front/api/v1/team_service")
@RequiredArgsConstructor
@CrossOrigin
public class TeamApiController {

    @Resource
    private TeamService teamService;

}
