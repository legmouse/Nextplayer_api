package kr.co.nextplayer.base.member.mapper;

import kr.co.nextplayer.base.member.dto.MemberConfirmDto;
import kr.co.nextplayer.base.member.model.MemberConfirm;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface MemberConfirmMapper {

    @Results(id = "memberConfirmResultMap", value = {
        @Result(column = "confirm_no", property = "confirmNo"),
        @Result(column = "member_cd", property = "memberCd"),
        @Result(column = "member_name", property = "memberName"),
        @Result(column = "birth_date", property = "birthDate"),
        @Result(column = "sex", property = "sex"),
        @Result(column = "phone_no", property = "phoneNo"),
        @Result(column = "ci_no", property = "ciNo"),
        @Result(column = "email", property = "email"),
        @Result(column = "cert_type", property = "certType"),
        @Result(column = "cert_date", property = "certDate"),
    })
    @Select("SELECT * FROM member_confirm WHERE member_cd = #{memberCd} ")
    List<MemberConfirm> selectByPrimaryKey(@Param("memberCd") String memberCd);

    @ResultMap("memberConfirmResultMap")
    @Select("SELECT * FROM member_confirm WHERE member_cd = #{memberCd} ")
    List<MemberConfirm> selectListByPrimaryKey(String memberCd);

    List<MemberConfirm> selectBySearch(Map searchParam);

    int totalSizeBySearch(Map searchParam);

    List<MemberConfirmDto> selectListBySearch(Map searchParam);

    @ResultMap("memberConfirmResultMap")
    @Select("SELECT * FROM member_confirm WHERE email = #{email,jdbcType=VARCHAR} and DATE_ADD(now(),INTERVAL -7 DAY)")
    MemberConfirm checkEmailExist(@Param("email") String email);

    @ResultMap("memberConfirmResultMap")
    @Select("SELECT * FROM member_confirm WHERE member_cd = #{memberCd} and email = #{email,jdbcType=VARCHAR} ")
    MemberConfirm checkMemberExistByEmail(@Param("memberCd") String memberCd, @Param("email") String email);

    @ResultMap("memberConfirmResultMap")
    @Select("SELECT * FROM member_confirm WHERE ci_no = #{ciNo} AND use_flag = 0")
    MemberConfirm checkCiExist(@Param("ciNo") String ciNo);

    @ResultMap("memberConfirmResultMap")
    @Select("SELECT * FROM member_confirm WHERE member_cd = #{memberCd} ")
    MemberConfirm getCompany(@Param("memberCd") String memberCd);

    @ResultMap("memberConfirmResultMap")
    @Select("SELECT * FROM member_confirm " +
        " WHERE member_cd = #{memberCd} " +
        " and cert_type = 'P' ")
    MemberConfirm getCiByMemberCd(String memberCd);

    @ResultMap("memberConfirmResultMap")
    @Select("SELECT * FROM member_confirm " +
        " WHERE member_cd = #{memberCd} " +
        " and cert_type = #{certType} ")
    MemberConfirm getConfirmByMemberCdAndCertType(@Param("memberCd") String memberCd, @Param("certType") String certType);

    @Delete("delete from member_confirm where member_cd = #{memberCd}")
    int deleteByPrimaryKey(String memberCd);

    int insertSelective(MemberConfirm record);

    int updateByPrimaryKeySelective(MemberConfirm record);

    @Update("UPDATE member_confirm SET use_flag = 1, inactive_date = now() WHERE member_cd = #{memberCd} AND use_flag = 0")
    int deleteMemberConfirm(String memberCd);

    @Update("UPDATE member_sns SET use_flag = 1, inactive_date = now() WHERE member_cd = #{memberCd} AND use_flag = 0")
    int deleteMemberSns(String memberCd);

    int updateSelective(MemberConfirm member);
}