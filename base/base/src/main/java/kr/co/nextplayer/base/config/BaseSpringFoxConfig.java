package kr.co.nextplayer.base.config;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import com.hubspot.jackson.datatype.protobuf.ProtobufModule;

import kr.co.nextplayer.lib.util.jackson.ProtobufPropertiesModule;
import springfox.documentation.schema.configuration.ObjectMapperConfigured;

/**
 * SpringFox 공통 설정
 */
@Configuration
public class BaseSpringFoxConfig implements ApplicationListener<ObjectMapperConfigured> {
    /** Configure SpringFox's internal ObjectMapper. */
	@Override
	public void onApplicationEvent(ObjectMapperConfigured event) {
		event.getObjectMapper().registerModule(new ProtobufModule());
		event.getObjectMapper().registerModule(new ProtobufPropertiesModule());
	}
}
