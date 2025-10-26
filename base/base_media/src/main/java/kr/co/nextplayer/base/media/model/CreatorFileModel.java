package kr.co.nextplayer.base.media.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatorFileModel {

    private String publicFileId;
    private String foreignId;
    private String foreignType;
    private String subType;
    private String fileName;
    private String fileSaveName;
    private String fileSavePath;
    private String fileExt;
    private String fileSize;

}
