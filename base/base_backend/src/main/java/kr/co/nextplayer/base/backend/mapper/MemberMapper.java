package kr.co.nextplayer.base.backend.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MemberMapper {

    HashMap<String, Object> selectMemberCount();

    List<HashMap<String, Object>> selectMemberList(Map<String, String> params);

    HashMap<String, Object> selectMemberListCount(Map<String, String> params);

    HashMap<String, Object> selectMemberDetail(Map<String, String> params);

    int selectTotalDayVisitAvg(Map<String, String> params);

    List<HashMap<String, Object>> selectDayVisitAvg(Map<String, String> params);

    int selectTotalDayOrgVisitAvg(Map<String, String> params);

    List<HashMap<String, Object>> selectDayOrgVisitAvg(Map<String, String> params);

    int selectTotalMonthOrgVisitAvg(Map<String, String> params);

    List<HashMap<String, Object>> selectMonthOrgVisitAvg(Map<String, String> params);

    int selectTotalJoinMemberCnt(Map<String, String> params);

    List<HashMap<String, Object>> selectDayJoinMember(Map<String, String> params);

    List<HashMap<String, Object>> selectMemberCntByPosition();

    List<HashMap<String, Object>> selectMemberCntByAgeGroup();

    List<HashMap<String, Object>> selectMemberCntByCertType();

    List<HashMap<String, Object>> selectMemberCntByGender();

    List<HashMap<String, Object>> selectMemberCntByPositionAndAgeGroup(String ageGroup);

    List<HashMap<String, Object>> selectMemberCntByPositionAndGender(String gender);

    int selectMemberTotalCnt(Map<String, String> params);

    int updateMember(Map<String, String> params);

    int updateMemberConfirm(Map<String, String> params);

    int deleteMember(String memberCd);
    int deleteMemberConfirm(String memberCd);
    int deleteMemberSns(String memberCd);

    int selectCheckActiveEmail(Map<String, String> params);

    List<HashMap<String, Object>> selectMemberListForExcelDown(Map<String, String> param);

    int selectMemberEducationListCnt(Map<String, String> param);

    List<HashMap<String, Object>> selectMemberEducationList(Map<String, String> param);

    HashMap<String, Object> selectDetailMemberEducation(Map<String, String> param);

    int insertMemberEducation(Map<String, String> params);

    int updateMemberEducation(Map<String, String> params);

    int deleteMemberEducation(Map<String, String> params);
}
