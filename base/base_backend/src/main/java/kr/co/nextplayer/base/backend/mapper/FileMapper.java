package kr.co.nextplayer.base.backend.mapper;

import kr.co.nextplayer.base.backend.dto.AttchFileInfoDto;
import kr.co.nextplayer.base.backend.dto.MediaFileInfoDto;

import java.util.List;
import java.util.Map;

public interface FileMapper {

    void insertFileInfo(AttchFileInfoDto dto);

    int insertSummernoteFileInfo(AttchFileInfoDto dto);

    List<AttchFileInfoDto> selectFileInfoList(AttchFileInfoDto attchFileInfoDto);

    void deleteFileInfo(AttchFileInfoDto attchFileInfoDto);

    void deleteFile(AttchFileInfoDto attchFileInfoDto);

    AttchFileInfoDto selectFileInfoView(AttchFileInfoDto attchFileInfoDto);

    MediaFileInfoDto selectMediaFileInfoView(MediaFileInfoDto mediaFileInfoDto);

}
