package kr.co.nextplayer.base.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nextplayer.base.backend.dto.AttchFileInfoDto;
import kr.co.nextplayer.base.backend.dto.EducationDto;
import kr.co.nextplayer.base.backend.mapper.EducationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EducationService {

    @Resource
    EducationMapper educationMapper;

    @Resource
    private FileService fileService;

    public int selectEducationListCnt(Map<String, String> param) {
        return educationMapper.selectEducationListCnt(param);
    }

    public List<HashMap<String, Object>> selectEducationList(Map<String, String> param) {
        return educationMapper.selectEducationList(param);
    }

    @Transactional
    public int insertEducation(Map<String, String> params, List<AttchFileInfoDto> imgFiles) {

        int resultCnt = 0;

        if (params.get("method").equals("save")) {
            resultCnt = educationMapper.insertEducation(params);
        }

        if (imgFiles != null) {
            fileService.insertEducationAttchFileInfo(imgFiles, params);
        }

        return resultCnt;
    }

    @Transactional
    public int updateEducation(Map<String, String> params, List<AttchFileInfoDto> imgFiles) {

        int referenceUpdateResult = educationMapper.updateEducation(params);

        fileService.deleteAttchFileInfo(imgFiles, params);

        int updateResult = 0;

        if (referenceUpdateResult > 0) {
            updateResult = insertEducation(params, imgFiles);
        }

        return updateResult;
    }

    @Transactional
    public int deleteEducation(Map<String, String> params) {
        int result = educationMapper.deleteEducation(params);

        AttchFileInfoDto attchFileInfoDto = new AttchFileInfoDto();
        attchFileInfoDto.setForeignId(params.get("educationId"));
        attchFileInfoDto.setForeignType(params.get("foreignType"));

        fileService.deleteImgFile(attchFileInfoDto);

        return result;
    }

    @Transactional
    public int insertColumn(Map<String, String> param, List<AttchFileInfoDto> imgFiles) {
        int resultCnt = 0;

        if (param.get("method").equals("save")) {
            resultCnt = educationMapper.insertColumn(param);

            if (param.get("mainFlag").equals("1")) {
                param.put("columnOrder", "1");
                educationMapper.updateMainColumnOrderPlusAll();
                educationMapper.insertMainColumnData(param);
            }

        }

        if (imgFiles != null) {
            fileService.insertColumnAttchFileInfo(imgFiles, param);
        }

        return resultCnt;
    }

    @Transactional
    public int updateColumn(Map<String, String> param, List<AttchFileInfoDto> imgFiles) {

        int referenceUpdateResult = educationMapper.updateColumn(param);

        if (!param.get("bfMainFlag").equals(param.get("mainFlag"))) {
            if (param.get("mainFlag").equals("1")) {
                param.put("columnOrder", "1");
                educationMapper.updateMainColumnOrderPlusAll();
                educationMapper.insertMainColumnData(param);
            } else {
                educationMapper.deleteMainColumnOne(param);
                educationMapper.updateMainColumnOrderMinusAll(param);
            }
        }

        fileService.deleteAttchFileInfo(imgFiles, param);

        int updateResult = 0;

        if (referenceUpdateResult > 0) {
            updateResult = insertColumn(param, imgFiles);
        }

        return referenceUpdateResult;
    }

    @Transactional
    public int deleteColumn(Map<String, String> param) {

        AttchFileInfoDto attchFileInfoDto = new AttchFileInfoDto();
        attchFileInfoDto.setForeignId(param.get("educationColumnId"));
        attchFileInfoDto.setForeignType(param.get("foreignType"));

        fileService.deleteImgFile(attchFileInfoDto);

        return educationMapper.deleteColumn(param);
    }


    public EducationDto selectEducationDetail(Map<String, String> param) {
        return educationMapper.selectEducationDetail(param);
    }

    public int selectEducationQuestionListCount(Map<String, String> param) {
        return educationMapper.selectEducationQuestionListCount(param);
    }

    public List<HashMap<String, Object>> selectEducationQuestionList(Map<String, String> param) {
        return educationMapper.selectEducationQuestionList(param);
    }

    public int selectEducationFaqListCount(Map<String, String> param) {
        return educationMapper.selectEducationFaqListCount(param);
    }

    public List<HashMap<String, Object>> selectEducationFaqList(Map<String, String> param) {
        return educationMapper.selectEducationFaqList(param);
    }

    @Transactional
    public int insertFaq(Map<String, String> param) {
        return educationMapper.insertFaq(param);
    }

    @Transactional
    public int updateFaq(Map<String, String> param) {
        return educationMapper.updateFaq(param);
    }

    @Transactional
    public int deleteFaq(Map<String, String> param) {
        return educationMapper.deleteFaq(param);
    }

    public List<HashMap<String, Object>> selectEducationQuestionListForExcelDown(Map<String, String> param) {
        return educationMapper.selectEducationQuestionListForExcelDown(param);
    }

    public int selectEducationMemberListCount(Map<String, String> param) {
        return educationMapper.selectEducationMemberListCount(param);
    }

    public List<HashMap<String, Object>> selectEducationMemberList(Map<String, String> param) {
        return educationMapper.selectEducationMemberList(param);
    }

    @Transactional
    public int insertMember(Map<String, String> param) {

        int result = 0;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> memberDataMap = objectMapper.readValue(param.get("data"), new TypeReference<Map<String, Object>>() {});
            List<HashMap<String,String>> memberDataList = (List<HashMap<String,String>>) memberDataMap.get("data");

            if (memberDataList.size() > 0) {
                for (Map<String, String> item : memberDataList) {
                    param.put("memberCd", item.get("memberCd"));
                    result = educationMapper.insertMember(param);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Transactional
    public int deleteMember(Map<String, String> param) {
        return educationMapper.deleteMember(param);
    }

    public List<HashMap<String, Object>> selectSearchEducationMember(Map<String, String> param) {
        return educationMapper.selectSearchEducationMember(param);
    }

    public int selectColumnListCnt(Map<String, String> param) {
        return educationMapper.selectColumnListCnt(param);
    }

    public List<HashMap<String, Object>> selectColumnList(Map<String, String> param) {
        return educationMapper.selectColumnList(param);
    }

    public Map<String ,Object> selectColumnDetail(Map<String, String> param) {
        return educationMapper.selectColumnDetail(param);
    }

    public List<Map<String, Object>> selectMainColumnList() {
        return educationMapper.selectMainColumnList();
    }

}
