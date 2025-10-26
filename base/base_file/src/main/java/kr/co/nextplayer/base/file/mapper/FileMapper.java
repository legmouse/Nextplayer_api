package kr.co.nextplayer.base.file.mapper;

import kr.co.nextplayer.base.file.model.PublicFileModel;

public interface FileMapper {

    PublicFileModel selectPublicFile(String fileId);

}
