package kr.co.nextplayer.base.league.config;

import kr.co.nextplayer.base.team.config.TeamBaseConfig;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Slf4j
@Configuration
@Import({
    TeamBaseConfig.class
})
@ComponentScan({ "kr.co.nextplayer.base.league"})
@MapperScan({
    "kr.co.nextplayer.base.league.mapper"
})
public class LeagueBaseConfig implements WebFluxConfigurer {

}
