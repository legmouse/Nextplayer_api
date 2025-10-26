package kr.co.nextplayer.base.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nextplayer.base.backend.dto.AttchFileInfoDto;
import kr.co.nextplayer.base.backend.mapper.MemberMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberService {

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private FileService fileService;

    public HashMap<String, Object> selectMemberCount() {
        return memberMapper.selectMemberCount();
    }

    public List<HashMap<String, Object>> selectMemberList(Map<String, String> params) {
        return memberMapper.selectMemberList(params);
}

    public HashMap<String, Object> selectMemberListCount(Map<String, String> params) {
        return memberMapper.selectMemberListCount(params);
    }

    public HashMap<String, Object> selectMemberDetail(Map<String, String> params) {
        return memberMapper.selectMemberDetail(params);
    }

    public int selectTotalDayVisitAvg(Map<String, String> params) {
        return memberMapper.selectTotalDayVisitAvg(params);
    }

    public List<HashMap<String, Object>> selectDayVisitAvg(Map<String, String> params) {
        return memberMapper.selectDayVisitAvg(params);
    }

    public int selectTotalDayOrgVisitAvg(Map<String, String> params) {
        return memberMapper.selectTotalDayOrgVisitAvg(params);
    }

    public List<HashMap<String, Object>> selectDayOrgVisitAvg(Map<String, String> params) {
        return memberMapper.selectDayOrgVisitAvg(params);
    }

    public int selectTotalMonthOrgVisitAvg(Map<String, String> params) {
        return memberMapper.selectTotalMonthOrgVisitAvg(params);
    }

    public List<HashMap<String, Object>> selectMonthOrgVisitAvg(Map<String, String> params) {
        return memberMapper.selectMonthOrgVisitAvg(params);
    }

    public int selectTotalJoinMemberCnt(Map<String, String> params) {
        return memberMapper.selectTotalJoinMemberCnt(params);
    }

    public List<HashMap<String, Object>> selectDayJoinMember(Map<String, String> params) {
        return memberMapper.selectDayJoinMember(params);
    }

    public List<HashMap<String, Object>> selectMemberCntByPosition() {
        return memberMapper.selectMemberCntByPosition();
    }

    public List<HashMap<String, Object>> selectMemberCntByAgeGroup() {
        return memberMapper.selectMemberCntByAgeGroup();
    }

    public List<HashMap<String, Object>> selectMemberCntByCertType() {
        return memberMapper.selectMemberCntByCertType();
    }

    public List<HashMap<String, Object>> selectMemberCntByGender() {
        return memberMapper.selectMemberCntByGender();
    }

    public List<HashMap<String, Object>> selectMemberCntByPositionAndAgeGroup(String ageGroup) {
        return memberMapper.selectMemberCntByPositionAndAgeGroup(ageGroup);
    }

    public List<HashMap<String, Object>> selectMemberCntByPositionAndGender(String gender) {
        return memberMapper.selectMemberCntByPositionAndGender(gender);
    }

    public int selectMemberTotalCnt(Map<String, String> params) {
        return memberMapper.selectMemberTotalCnt(params);
    }

    @Transactional
    public int modifyMember(Map<String, String> params) {
        int result = 0;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> memberConfirmMap = objectMapper.readValue(params.get("memberConfirm"), new TypeReference<Map<String, String>>() {});
            System.out.println("memberConfirmMap = " + memberConfirmMap);

            if (memberConfirmMap != null) {
                memberConfirmMap.put("memberCd", params.get("memberCd"));
                memberMapper.updateMemberConfirm(memberConfirmMap);
            }

            result = memberMapper.updateMember(params);

        } catch (Exception e) {

        }

        return result;
    }

    @Transactional
    public int deleteMember(String memberCd) {
        int memberDelete = memberMapper.deleteMember(memberCd);
        int memberConfirmDelete = memberMapper.deleteMemberConfirm(memberCd);
        memberMapper.deleteMemberSns(memberCd);

        if (memberDelete > 0 && memberConfirmDelete > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public int selectCheckActiveEmail(Map<String, String> params) {
        return memberMapper.selectCheckActiveEmail(params);
    }

    public List<HashMap<String, Object>> selectMemberListForExcelDown(Map<String, String> param) {
        return memberMapper.selectMemberListForExcelDown(param);
    }

    public int selectMemberEducationListCnt(Map<String, String> param) {
        return memberMapper.selectMemberEducationListCnt(param);
    }

    public List<HashMap<String, Object>> selectMemberEducationList(Map<String, String> param) {
        return memberMapper.selectMemberEducationList(param);
    }

    public HashMap<String ,Object> selectDetailMemberEducation(Map<String, String> params) {
        return memberMapper.selectDetailMemberEducation(params);
    }

    @Transactional
    public int insertMemberEducation(Map<String, String> params, List<AttchFileInfoDto> files) {
        int resultCnt = 0;
        if (params.get("method").equals("save")) {
            resultCnt = memberMapper.insertMemberEducation(params);
        }

        if (files != null) {
            fileService.insertEducationFileAttchFileInfo(files, params);
        }

        return resultCnt;
    }

    @Transactional
    public int updateMemberEducation(Map<String, String> params, List<AttchFileInfoDto> files) {
        int updateResult = memberMapper.updateMemberEducation(params);

        fileService.deleteAttchFileInfo(files, params);

        if (updateResult > 0) {
            int updateFile = insertMemberEducation(params, files);
        }
        return updateResult;
    }

    @Transactional
    public int deleteMemberEducation(Map<String, String> params) {
        int result = memberMapper.deleteMemberEducation(params);
        AttchFileInfoDto attchFileInfoDto = new AttchFileInfoDto();
        attchFileInfoDto.setForeignId(params.get("educationFileId"));
        attchFileInfoDto.setForeignType(params.get("foreignType"));

        fileService.deleteImgFile(attchFileInfoDto);

        return result;
    }

}
