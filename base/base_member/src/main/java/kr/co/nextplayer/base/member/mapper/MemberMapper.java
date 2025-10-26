package kr.co.nextplayer.base.member.mapper;


import kr.co.nextplayer.base.member.dto.MemberInterestReqDto;
import kr.co.nextplayer.base.member.dto.MemberModifyDto;
import kr.co.nextplayer.base.member.dto.MemberPushHistoryDto;
import kr.co.nextplayer.base.member.dto.MemberPushSettingDto;
import kr.co.nextplayer.base.member.model.Member;
import kr.co.nextplayer.base.member.model.MemberInfo;
import kr.co.nextplayer.base.member.model.SmsCertSend;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MemberMapper {

    @Delete("DELETE FROM member WHERE member_cd = #{memberCd}")
    int deleteByPrimaryKey(String memberCd);

    int insertSelective(Member record);

    @Delete("<script>" +
        "DELETE FROM member WHERE member_cd IN" +
        "<foreach item='item' index='index' collection='memberCds' open='(' separator=',' close=')'>" +
        "#{item}" +
        "</foreach>" +
        "</script>"
    )
    int deleteByPrimaryKeys(Set<String> memberCds);

    int updateByPrimaryKeySelective(Member record);

    @Results(id = "resultMapAll", value = {
        @Result(column = "member_cd", property = "memberCd"),
        @Result(column = "member_id", property = "memberId"),
        @Result(column = "member_pwd", property = "memberPwd"),
        @Result(column = "member_state", property = "memberState"),
        @Result(column = "reg_date", property = "regDate"),
        @Result(column = "upd_admin_id", property = "updAdminId"),
        @Result(column = "upd_date", property = "updDate"),
        @Result(column = "interest_age", property = "interestAge"),
        @Result(column = "fcm_token", property = "fcmToken"),
    })
    @Select("SELECT * FROM member WHERE member_cd = #{memberCd} AND use_flag = 0")
    Member selectByMemberCd(String memberCd);

    int totalSizeBySearch(Map searchParam);

    @ResultMap(value = "resultMapAll")
    @Select("SELECT * FROM member WHERE member_id = #{memberId}")
    Member selectByMemberId(String memberId);

    @ResultMap(value = "resultMapAll")
    @Select("<script>" +
        " SELECT * FROM member " +
        "  <trim prefix='WHERE' prefixOverrides='AND |OR'> " +
        "   <if test='memberCds != null and memberCds.length >0'>" +
        "       and member_cd in " +
        "       <foreach item='item' collection='memberCds' open='(' separator=',' close=')'>" +
        "           #{item}" +
        "       </foreach>" +
        "   </if>" +
        "   <if test='memberIds != null and memberIds.length >0'>" +
        "       and member_id in " +
        "       <foreach item='item' collection='memberIds' open='(' separator=',' close=')'> " +
        "           #{item}" +
        "       </foreach>" +
        "   </if>" +
        "   </trim>" +
        "</script>"
    )
    List<Member> popupMemberList(@Param("memberCds") String[] memberCds, @Param("memberIds") String[] memberIds);

    // use by fe shop ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    @ResultMap("resultMapAll")
    @Select("SELECT * FROM member WHERE member_id = #{userId} AND use_flag = 0")
    Member login(@Param("userId") String userId);

    @Select("SELECT count(member_id) FROM member WHERE member_id = #{memberId} AND DATE_ADD(now(),INTERVAL -7 DAY)")
    int checkActiveId(String memberId);

    @Select("SELECT count(member_id) FROM member_inactive WHERE member_id = #{memberId} AND use_flag = 0")
    int checkInactiveId(String memberId);

    @Select("SELECT count(email) FROM member_confirm WHERE email = #{email} and DATE_ADD(now(),INTERVAL -7 DAY)")
    int checkActiveEmail(String email);

    @Select("SELECT count(email) FROM member_confirm WHERE email = #{email} AND DATE_ADD(now(),INTERVAL -7 DAY) AND member_cd != #{memberCd}")
    int checkActiveEmailForModify(String email, String memberCd);

    @Select("SELECT count(phone_no) FROM member_confirm WHERE phone_no = #{phoneNo} and DATE_ADD(now(),INTERVAL -7 DAY)")
    int checkActivePhoneNo(String phoneNo);

    @Select("select count(member_nickname) from member where member_nickname = #{nickname} and DATE_ADD(now(),INTERVAL -7 DAY)")
    int checkActiveNickname(String nickname);

    @ResultMap("resultMapAll")
    @Select(" SELECT * FROM member WHERE member_cd = #{corporateCd} " +
        " AND member_type_cd in (7,8,9)")
    Member selectCorporationByMemberCd(String corporateCd);

    @Update("<script>" +
        " UPDATE member" +
        " SET co_flag = 0 " +
        " WHERE member_cd IN " +
        "<foreach item='item' index='index' collection='memberCds' open='(' separator=',' close=')'>" +
        "#{item}" +
        "</foreach>" +
        "</script>")
    int updateMemberCorpToGeneral(List<String> memberCds);

    @ResultMap("resultMapAll")
    @Select("<script>" +
        "SELECT member_cd, member_id, personal_info FROM member " +
        "WHERE " +
        "<if test=\"platform != null and platform == 'PC' \">" +
        "   JSON_VALUE(personal_info, '$.mkgTodayFlag') = #{flag}" +
        "</if>" +
        "<if test=\"platform != null and platform == 'SP' \">" +
        "  JSON_VALUE(personal_info, '$.mkgTodaySpFlag') = #{flag}" +
        "</if>" +
        "</script>")
    List<Member> selectByMkgTodayFlag(@Param("flag") String flag,@Param("platform") String platform);
    // use by fe shop ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    // use by batch ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    @ResultMap("resultMapAll")
    @Select("SELECT m.* " +
        "  FROM member m " +
        " WHERE m.info_expiry_year > -1 " +
        "   AND DATEDIFF(DATE_FORMAT(NOW(), '%Y%m%d'), DATE_FORMAT(JSON_VALUE(m.act_hist_info, '$.lastConnectDate'), '%Y%m%d')) > m.info_expiry_year * 365 "
    )
    List<Member> findInactiveList();

    @ResultMap("resultMapAll")
    @Select("SELECT m.* " +
        "  FROM member m " +
        " WHERE m.info_expiry_year > -1 " +
        "   AND DATEDIFF(DATE_FORMAT(NOW(), '%Y%m%d'), DATE_FORMAT(JSON_VALUE(m.act_hist_info, '$.lastConnectDate'), '%Y%m%d')) > (m.info_expiry_year * 365 -60) " +
        "   AND (JSON_VALUE(m.act_hist_info, '$.inactiveAlimFlag') = 0 or JSON_VALUE(m.act_hist_info, '$.inactiveAlimFlag') is null) "
    )
    List<Member> findInactiveAlimList();
    // use by batch ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑


    public void resetBasket(@Param("memberCd") String memberCd, @Param("basketDiv") String basketDiv);

    @Update("UPDATE member SET access_token = #{accessToken} " +
        ", refresh_token = #{refreshToken}" +
        ", access_token_origin = #{refreshTokenOrigin}" +
        ", refresh_token_origin = #{refreshTokenOrigin}" +
        ", access_token_expire = DATE_ADD(NOW(), INTERVAL #{accessTokenExpire} SECOND)" +
        ", refresh_token_expire = DATE_ADD(NOW(), INTERVAL #{refreshTokenExpire} SECOND) WHERE member_cd = #{memberCd}")
    int updateToken(@Param("memberCd") String memberCd,
                    @Param("accessToken") String accessToken,
                    @Param("refreshToken") String refreshToken,
                    @Param("accessTokenOrigin") String accessTokenOrigin,
                    @Param("refreshTokenOrigin") String refreshTokenOrigin,
                    @Param("accessTokenExpire") int accessTokenExpire,
                    @Param("refreshTokenExpire") int refreshTokenExpire);

    @Update("UPDATE member SET access_token = NULL " +
        ", refresh_token = NULL" +
        ", access_token_expire = NULL" +
        ", refresh_token_expire = NULL WHERE member_cd = #{memberCd}")
    int logoutToken(@Param("memberCd") String memberCd);

    @Select("select refresh_token_origin from member where refresh_token = #{refreshToken}")
    String selectRefreshTokenOrigin(@Param("refreshToken") String refreshToken);

    int insertMemberCertNumber(SmsCertSend smsCertSend);

    @Update("UPDATE member SET use_flag = 1, inactive_date = now() WHERE member_cd = #{memberCd} AND use_flag = 0")
    int deleteMember(String memberCd);

    int updateSelective(Member member);

    MemberInfo selectMemberInfo(String memberCd);

    int updateMemberInterest(MemberInterestReqDto dto);

    @Update("UPDATE member SET fcm_token =#{fcmToken} WHERE member_cd = #{memberCd} AND use_flag = 0")
    int updateFcmToken(Member member);

    MemberPushSettingDto selectMemberPush(String memberCd);

    void updateMemberPushSetting(MemberPushSettingDto memberPushSettingDto);

    List<MemberPushHistoryDto> selectPushHistory(String memberCd);

    void updatePushRead(int pushId);

    boolean selectPushBellCheck(String memberCd);

    void updatePushReadAll(String memberCd);
}