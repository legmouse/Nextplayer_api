package kr.co.nextplayer.base.member.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Slf4j
@Configuration
@ComponentScan({ "kr.co.nextplayer.base.member"})
@MapperScan({
    "kr.co.nextplayer.base.member.mapper"
})
public class MemberBaseConfig implements WebFluxConfigurer {

}
