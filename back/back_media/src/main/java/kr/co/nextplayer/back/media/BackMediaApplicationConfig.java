package kr.co.nextplayer.back.media;

import kr.co.nextplayer.base.media.config.MediaBaseConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ MediaBaseConfig.class })
public class BackMediaApplicationConfig {


}
