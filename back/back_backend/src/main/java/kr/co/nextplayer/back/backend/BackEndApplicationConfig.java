package kr.co.nextplayer.back.backend;

import kr.co.nextplayer.base.backend.config.BackendBaseConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({BackendBaseConfig.class})
public class BackEndApplicationConfig {
}
