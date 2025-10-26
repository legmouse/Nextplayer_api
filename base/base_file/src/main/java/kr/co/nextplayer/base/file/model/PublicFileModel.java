package kr.co.nextplayer.base.file.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicFileModel {

    private String publicFileId;
    private String foreignId;
    private String foreignType;
    private String fileName;
    private String fileSaveName;
    private String fileSavePath;
    private String fileExt;
    private String fileSize;

}
