package kr.co.nextplayer.base.backend.mapper;

import kr.co.nextplayer.base.backend.dto.UserDto;
import kr.co.nextplayer.base.backend.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.Date;

public interface UserMapper {

    @Select("SELECT * FROM User WHERE id = #{id} AND pw = #{pw} AND use_flag = 0")
    @Result(column = "user_id", property = "userId")
    @Result(column = "id", property = "id")
    @Result(column = "user_name", property = "userName")
    @Result(column = "session_key", property = "sessionKey")
    @Result(column = "use_flag", property = "useFlag")
    @Result(column = "session_date", property = "sessionDate")
    @Result(column = "reg_date", property = "regDate")
    User login(UserDto userDto);

    @Update("UPDATE User SET session_key = #{sessionId}, session_date = #{next} WHERE id= #{userId}")
    int updateKeepLogin(@Param("userId") String userId, @Param("sessionId") String sessionId, @Param("next") Date next);

    @Select("SELECT * FROM User WHERE session_key = #{sessionId} AND session_date > now()")
    @Result(column = "user_id", property = "userId")
    @Result(column = "user_name", property = "userName")
    @Result(column = "session_key", property = "sessionKey")
    @Result(column = "use_flag", property = "useFlag")
    @Result(column = "session_date", property = "sessionDate")
    @Result(column = "reg_date", property = "regDate")
    User checkUserWithSessionKey(String sessionId);

}
