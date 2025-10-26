package kr.co.nextplayer.next.lib.common.util;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.google.gson.*;

public class GsonUtil {

    public static Gson getGsonClient(){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                JsonPrimitive asJsonPrimitive = json.getAsJsonPrimitive();
                String asString = asJsonPrimitive.getAsString();
                try {
                    return sf.parse(asString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        builder.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeSerializer());
        builder.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeDeserializer());

        builder.registerTypeAdapter(LocalDate.class, new GsonLocalDateSerializer());
        builder.registerTypeAdapter(LocalDate.class, new GsonLocalDateDeserializer());

        Gson gson = builder.create();
        return gson;
    }
}
