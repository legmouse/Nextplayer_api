package kr.co.nextplayer.back.front;

import kr.co.nextplayer.base.front.config.FrontBaseConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ FrontBaseConfig.class})
@ComponentScan({
    "kr.co.nextplayer.base.webfilter",
    "kr.co.nextplayer.next.lib.common",
})
public class BackFrontApplicationConfig {

}
