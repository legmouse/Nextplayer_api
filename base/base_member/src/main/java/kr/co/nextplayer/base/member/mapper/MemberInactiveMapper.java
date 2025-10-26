package kr.co.nextplayer.base.member.mapper;

import kr.co.nextplayer.base.member.model.MemberInactive;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface MemberInactiveMapper {

    @Results(id = "MemberInactiveResultMap", value = {
            @Result(column = "member_cd", property = "memberCd"),

            @Result(column = "inactive_type", property = "inactiveType"),
            @Result(column = "reason_cd", property = "reasonCd"),
            @Result(column = "reason_content", property = "reasonContent"),
            @Result(column = "inactive_date", property = "inactiveDate"),
            @Result(column = "inactive_user_type", property = "inactiveUserType"),
            @Result(column = "inactive_user_cd", property = "inactiveUserCd"),

            @Result(column = "member_id", property = "memberId"),
            @Result(column = "member_pwd", property = "memberPwd"),
            @Result(column = "member_type_cd", property = "memberTypeCd"),
            @Result(column = "card_flag", property = "cardFlag"),
            @Result(column = "member_grade", property = "memberGrade"),
            @Result(column = "mileage_flag", property = "mileageFlag"),
            @Result(column = "online_cert_flag", property = "onlineCertFlag"),
            @Result(column = "member_state", property = "memberState"),
            @Result(column = "member_reg_flag", property = "memberRegFlag"),
            @Result(column = "co_flag", property = "coFlag"),
            @Result(column = "dm_flag", property = "dmFlag"),
            @Result(column = "reg_date", property = "regDate"),
            @Result(column = "upd_admin_id", property = "updAdminId"),
            @Result(column = "upd_date", property = "updDate"),
    })
    @Select("select * from member_inactive where member_cd = #{memberCd}")
    MemberInactive selectByPrimaryKey(String memberCd);

    @ResultMap("MemberInactiveResultMap")
    @Select("select * from member_inactive where member_id = #{userId}")
    MemberInactive selectByInactive(String userId);

    int totalSizeBySearch(Map searchParam);

    @Delete("delete from member_inactive where member_cd = #{memberCd}")
    int deleteByPrimaryKey(String memberCd);

    int insertSelective(MemberInactive record);

    int updateByPrimaryKeySelective(MemberInactive record);



    @ResultMap("MemberInactiveResultMap")
    @Select(" select * " +
            "   from member_inactive " +
            "   where DATEDIFF(DATE_FORMAT(NOW(), '%Y%m%d'), DATE_FORMAT(inactive_date, '%Y%m%d')) >= 30 "
    )
    List<MemberInactive> selectByInactiveDelBatch();

}