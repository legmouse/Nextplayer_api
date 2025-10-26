package kr.co.nextplayer.base.backend.service;

import kr.co.nextplayer.base.backend.dto.AttchFileInfoDto;
import kr.co.nextplayer.base.backend.mapper.PopupMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PopupService {

    @Resource
    PopupMapper popupMapper;

    @Resource
    private FileService fileService;

    public int selectPopupListCnt(Map<String, String> param) {
        return popupMapper.selectPopupListCnt(param);
    }

    public List<HashMap<String, Object>> selectPopupList(Map<String, String> param) {
        return popupMapper.selectPopupList(param);
    }

    @Transactional
    public int insertPopup(Map<String, String> param, List<AttchFileInfoDto> imgFiles) {
        int result = 0;

        if (param.get("method").equals("save")) {
            result = popupMapper.insertPopup(param);
        }

        if (imgFiles != null) {
            fileService.insertPopupAttchFileInfo(imgFiles, param);
        }

        return result;
    }

    @Transactional
    public int updatePopup(Map<String, String> param, List<AttchFileInfoDto> imgFiles) {
        int popupUpdateResult = popupMapper.updatePopup(param);

        fileService.deleteAttchFileInfo(imgFiles, param);

        int updateResult = 0;

        if (popupUpdateResult > 0) {
            updateResult = insertPopup(param, imgFiles);
        }

        return updateResult;
    }

    @Transactional
    public int deleteEducation(Map<String, String> params) {
        int result = popupMapper.deletePopup(params);

        AttchFileInfoDto attchFileInfoDto = new AttchFileInfoDto();
        attchFileInfoDto.setForeignId(params.get("popupId"));
        attchFileInfoDto.setForeignType(params.get("foreignType"));

        fileService.deleteImgFile(attchFileInfoDto);

        return result;
    }

    public Map<String, Object> selectPopupDetail(Map<String, String> params) {
        return popupMapper.selectPopupDetail(params);
    }

}
