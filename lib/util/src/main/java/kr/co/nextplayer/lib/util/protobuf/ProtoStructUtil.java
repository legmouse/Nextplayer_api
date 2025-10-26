package kr.co.nextplayer.lib.util.protobuf;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtoStructUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, Object> convertStructToMap(Struct struct) {
        try {
            String json = JsonFormat.printer().print(struct);

            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};

            return objectMapper.readValue(json, typeRef);
        } catch (InvalidProtocolBufferException | JsonProcessingException e) {
            log.error("Exception while converting Struct to Map", e);
        }

        return null;
    }

    public static Struct convertMapToStruct(Map<String, Object> values) {
        try {
            Struct.Builder builder = Struct.newBuilder();
            String json = objectMapper.writeValueAsString(values);
            JsonFormat.parser().merge(json, builder);
            return builder.build();
        } catch (JsonProcessingException | InvalidProtocolBufferException e) {
            log.error("Exception while converting Map to Struct", e);
        }

        return null;
    }
}
