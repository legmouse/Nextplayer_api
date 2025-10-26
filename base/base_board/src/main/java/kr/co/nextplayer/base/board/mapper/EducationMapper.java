package kr.co.nextplayer.base.board.mapper;

import kr.co.nextplayer.base.board.dto.ColumnReqDto;
import kr.co.nextplayer.base.board.dto.EducationAuthReqDto;
import kr.co.nextplayer.base.board.dto.EducationReqDto;
import kr.co.nextplayer.base.board.model.Column;
import kr.co.nextplayer.base.board.model.EduFile;
import kr.co.nextplayer.base.board.model.Education;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface EducationMapper {

    int selectEducationCount(EducationReqDto educationDto);

    List<Education> selectEducationList(EducationReqDto educationDto);

    Education selectEducationDetail(EducationReqDto educationReqDto);

    int selectEducationQuestionCount(EducationReqDto educationReqDto);

    Education selectSearchEducationByGoods(EducationAuthReqDto educationAuthReqDto);

    int insertEducationAuth(Map<String, String> param);

    int selectCheckEducationAuthDuplicate(EducationAuthReqDto educationAuthReqDto);

    int selectColumnCnt(ColumnReqDto columnReqDto);

    List<Column> selectColumnList(ColumnReqDto columnReqDto);

    Column selectColumnDetail(ColumnReqDto columnReqDto);

    List<Column> selectPrevNextColumn(ColumnReqDto columnReqDto);

    int updateColumnViewCnt(ColumnReqDto columnReqDto);

    List<EduFile> selectEducationFileList(EducationReqDto educationReqDto);

    List<HashMap<String, Object>> selectHomeColumnList(HashMap<String, String> param);
}
