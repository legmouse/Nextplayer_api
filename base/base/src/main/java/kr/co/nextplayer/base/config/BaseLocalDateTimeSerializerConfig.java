package kr.co.nextplayer.base.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import kr.co.nextplayer.next.lib.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Configuration
@Slf4j
public class BaseLocalDateTimeSerializerConfig implements WebFluxConfigurer {

    public final static ZoneId ZONE_KST = ZoneId.of("Asia/Seoul");
    public final static ZoneId ZONE_UTC = ZoneId.of("UTC");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Bean
    @Primary
    public ObjectMapper DateFormatConfigMapper() {
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        // 필드에 null이 있는경우 나오게 처리하기 위해 주석처리
        // mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(timeModule).registerModule(new ParameterNamesModule()).registerModules(ObjectMapper.findModules());
        return mapper;
    }

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        ObjectMapper mapper = DateFormatConfigMapper();
        configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(mapper));
        configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(mapper));
    }


    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

        @Override
        public void serialize(LocalDateTime localDateTime, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {

            if (localDateTime != null) {
                String dateTimeFormatter = formatter.format(localDateTime.atZone(ZONE_UTC).withZoneSameInstant(ZONE_KST));
                gen.writeString(dateTimeFormatter);
            }
        }
    }

    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

            String timestamp = jsonParser.getValueAsString();
            if (StringUtils.isNotEmpty(timestamp)) {
                Date parseDate = DateUtils.parse(timestamp);
                LocalDateTime localDateTime = DateUtils.asLocalDateTime(parseDate);
                ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZONE_KST).withZoneSameInstant(ZONE_UTC);
                LocalDateTime localDateTime1 = zonedDateTime.toLocalDateTime();
                log.info("DateDeserializer: {}", localDateTime1);
                return localDateTime1;
            } else {
                return null;
            }
        }
    }


    public static class UnixTimeToLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

        @Override
        public void serialize(LocalDateTime localDateTime, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {

            if (localDateTime != null) {
                gen.writeNumber(localDateTime.atZone(ZONE_UTC).withZoneSameInstant(ZONE_KST).toInstant().toEpochMilli());
            }
        }
    }

    public static class UnixTimeToLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

            String timestamp = jsonParser.getValueAsString();
            if (StringUtils.isNotEmpty(timestamp)) {
                Instant instant = Instant.ofEpochMilli(jsonParser.getLongValue());
                ZonedDateTime zonedDateTime = instant.atZone(ZONE_KST).withZoneSameInstant(ZONE_UTC);
                return zonedDateTime.toLocalDateTime();
            } else {
                return null;
            }


        }
    }

}