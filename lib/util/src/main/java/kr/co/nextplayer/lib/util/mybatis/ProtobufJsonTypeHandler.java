package kr.co.nextplayer.lib.util.mybatis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

/**
 * Protobuf <-> Json String 변환을 위한 TypeHandler
 */
@MappedJdbcTypes(value = { JdbcType.CHAR })
@MappedTypes({ GeneratedMessageV3.class })
public class ProtobufJsonTypeHandler extends BaseTypeHandler<GeneratedMessageV3> {
    private static final JsonFormat.Printer printer = JsonFormat.printer();
    private static final JsonFormat.Parser parser = JsonFormat.parser();

    public Class<? extends GeneratedMessageV3> type;

    public ProtobufJsonTypeHandler(Class<? extends GeneratedMessageV3> type)
            throws NoSuchMethodException, SecurityException {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, GeneratedMessageV3 parameter, JdbcType jdbcType)
            throws SQLException {
        try {
            ps.setString(i, printer.print(parameter));
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    private GeneratedMessageV3 stringToBuilder(String value) {
        try {

            Method method = type.getMethod("newBuilder");
            var builder = (GeneratedMessageV3.Builder<?>) method.invoke(null);

            parser.merge(value, builder);

            return (GeneratedMessageV3) builder.build();

        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidProtocolBufferException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // TODO Auto-generated method stub

        return null;
    }

    @Override
    public GeneratedMessageV3 getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return stringToBuilder(rs.getString(columnName));
    }

    @Override
    public GeneratedMessageV3 getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return stringToBuilder(rs.getString(columnIndex));
    }

    @Override
    public GeneratedMessageV3 getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return stringToBuilder(cs.getString(columnIndex));
    }


}