package kr.co.nextplayer.base.backend.mapper;

import kr.co.nextplayer.base.backend.dto.EducationDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface EducationMapper {

    int selectEducationListCnt(Map<String, String> param);

    List<HashMap<String, Object>> selectEducationList(Map<String, String> param);

    int insertEducation(Map<String, String> param);

    int updateEducation(Map<String, String> param);

    int deleteEducation(Map<String, String> param);

    EducationDto selectEducationDetail(Map<String, String> param);

    int selectEducationQuestionListCount(Map<String, String> param);

    List<HashMap<String, Object>> selectEducationQuestionList(Map<String, String> param);

    int selectEducationFaqListCount(Map<String, String> param);

    List<HashMap<String, Object>> selectEducationFaqList(Map<String, String> param);

    int insertFaq(Map<String, String> param);

    int updateFaq(Map<String, String> param);

    int deleteFaq(Map<String, String> param);

    List<HashMap<String, Object>> selectEducationQuestionListForExcelDown(Map<String, String> param);

    int selectEducationMemberListCount(Map<String, String> param);

    List<HashMap<String, Object>> selectEducationMemberList(Map<String, String> param);

    int insertMember(Map<String, String> param);

    int deleteMember(Map<String, String> param);

    List<HashMap<String, Object>> selectSearchEducationMember(Map<String, String> param);

    int selectColumnListCnt(Map<String, String> param);

    List<HashMap<String, Object>> selectColumnList(Map<String, String> param);

    int insertColumn(Map<String, String> param);

    int updateColumn(Map<String, String> param);

    int deleteColumn(Map<String, String> param);

    Map<String, Object> selectColumnDetail(Map<String, String> param);

    int deleteMainColumnData();

    int insertMainColumnData(Map<String, String> param);

    List<Map<String, Object>> selectMainColumnList();

    int updateMainColumnOrderPlusAll();

    int deleteMainColumnOne(Map<String, String> param);

    int updateMainColumnOrderMinusAll(Map<String, String> param);
}
