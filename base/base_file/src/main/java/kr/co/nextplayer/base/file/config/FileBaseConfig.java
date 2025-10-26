package kr.co.nextplayer.base.file.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@ComponentScan({ "kr.co.nextplayer.base.file"})
@MapperScan({
    "kr.co.nextplayer.base.file.mapper"
})
public class FileBaseConfig implements WebMvcConfigurer {
}
