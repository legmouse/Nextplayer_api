package kr.co.nextplayer.base.typehandler;

import kr.co.nextplayer.base.enums.DeviceType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DeviceType
 */
public class DeviceTypeHandler extends BaseTypeHandler<DeviceType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DeviceType parameter, JdbcType jdbcType) throws SQLException {
        if (parameter != null) {
            ps.setInt(i, parameter.code());
        } else {
            ps.setInt(i, 0);
        }
    }

    @Override
    public DeviceType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return DeviceType.valueOfCode(rs.getInt(columnName));
    }

    @Override
    public DeviceType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return DeviceType.valueOfCode(rs.getInt(columnIndex));
    }

    @Override
    public DeviceType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return DeviceType.valueOfCode(cs.getInt(columnIndex));
    }
}
