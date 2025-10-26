package kr.co.nextplayer.base.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;
import com.jakewharton.retrofit2.adapter.reactor.ReactorCallAdapterFactory;
import com.linecorp.armeria.client.logging.ContentPreviewingClient;
import com.linecorp.armeria.client.logging.LoggingClient;
import com.linecorp.armeria.client.retrofit2.ArmeriaRetrofit;
import com.linecorp.armeria.common.logging.LogLevel;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.time.Duration;

public class BaseRetrofitFactory {
    public static Retrofit createRetrofit(String baseUrl) {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ProtobufModule());

        var loggingClientBuilder = LoggingClient.builder()
                .requestLogLevel(LogLevel.INFO)
                .successfulResponseLogLevel(LogLevel.INFO);

        return ArmeriaRetrofit.builder(baseUrl)
                .responseTimeout(Duration.ofSeconds(10))
                .writeTimeout(Duration.ofSeconds(10))
                .addCallAdapterFactory(ReactorCallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .decorator(loggingClientBuilder.newDecorator())
                .decorator(ContentPreviewingClient.newDecorator(Integer.MAX_VALUE))
                .build();
    }
}
