package kr.co.nextplayer.lib.util.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;


/**
 * Json String <-> Bean 변환을 위한 TypeHandler
 */
@Slf4j
public class JsonTypeHandler<T extends Object> extends BaseTypeHandler<T> {
    private static ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private Class<T> clazz;

    public JsonTypeHandler(Class<T> clazz) throws NoSuchMethodException, SecurityException {
        this.clazz = clazz;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, this.convertObjectToString(parameter));
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convertStringToObject(rs.getString(columnName));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convertStringToObject(rs.getString(columnIndex));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convertStringToObject(cs.getString(columnIndex));
    }

    private T convertStringToObject(String value) {
        if (value == null) {
            return null;
        }

        try {
            return mapper.readValue(value, clazz);
        } catch (JsonMappingException e) {
            log.error("JsonTypeHandler Exception:", e);
        } catch (JsonProcessingException e) {
            log.error("JsonTypeHandler Exception:", e);
        }

        return null;
    }

    private String convertObjectToString(Object object) {
        if (object == null) {
            return null;
        }

        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("JsonTypeHandler Exception:", e);
        }

        return null;
    }
}
