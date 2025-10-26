package kr.co.nextplayer.back.batch;

import kr.co.nextplayer.base.batch.config.BatchBaseConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ BatchBaseConfig.class })
@ComponentScan({
    "kr.co.nextplayer.next.lib.common"
})
public class BackBatchApplicationConfig {
}
