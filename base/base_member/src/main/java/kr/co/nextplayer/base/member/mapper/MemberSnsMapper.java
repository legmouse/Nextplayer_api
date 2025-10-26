package kr.co.nextplayer.base.member.mapper;

import kr.co.nextplayer.base.member.model.MemberSns;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MemberSnsMapper {

    @Results(id = "MemberResultMap", value = {
        @Result(column = "sns_no", property = "snsNo"),
        @Result(column = "sns_type", property = "snsType"),
        @Result(column = "sns_id", property = "snsId"),
        @Result(column = "member_cd", property = "memberCd"),
        @Result(column = "email", property = "email"),
        @Result(column = "name", property = "name"),
        @Result(column = "phone", property = "phone"),
        @Result(column = "birth_date", property = "birthDate"),
        @Result(column = "ci_no", property = "ciNo"),
        @Result(column = "sex", property = "sex"),
        @Result(column = "photo", property = "photo"),
        @Result(column = "nickname", property = "nickname"),
        @Result(column = "age_group", property = "ageGroup"),
        @Result(column = "reg_date", property = "regDate"),
    })
    @Select("SELECT * FROM member_sns WHERE sns_no = #{snsNo,jdbcType=INTEGER}")
    MemberSns selectByPrimaryKey(Integer snsNo);

    @ResultMap("MemberResultMap")
    @Select("SELECT * FROM member_sns WHERE member_cd = #{memberCd}")
    List<MemberSns> selectByMemberCd(String memberCd);

    @ResultMap("MemberResultMap")
    @Select("SELECT * FROM member_sns WHERE sns_type = #{snsType,jdbcType=VARCHAR} and sns_id = #{snsId,jdbcType=VARCHAR} and use_flag = 0")
    MemberSns checkSnsExist(@Param("snsType") String snsType, @Param("snsId") String snsId);

    @ResultMap("MemberResultMap")
    @Select("SELECT * FROM member_sns WHERE sns_type = #{snsType,jdbcType=VARCHAR} and member_cd = #{memberCd,jdbcType=VARCHAR}")
    MemberSns checkSnsExistByMemberCd(@Param("snsType") String snsType, @Param("memberCd") String memberCd);

    int deleteByPrimaryKey(Integer snsNo);

    int insertSelective(MemberSns record);

    int updateByPrimaryKeySelective(MemberSns record);

}