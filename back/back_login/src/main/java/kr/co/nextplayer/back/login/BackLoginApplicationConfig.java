package kr.co.nextplayer.back.login;

import kr.co.nextplayer.base.login.config.LoginBaseConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ LoginBaseConfig.class })
@ComponentScan({
    "kr.co.nextplayer.next.lib.common",
})

public class BackLoginApplicationConfig {

}
