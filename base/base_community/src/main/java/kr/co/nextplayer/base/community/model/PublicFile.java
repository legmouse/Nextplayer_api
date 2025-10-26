package kr.co.nextplayer.base.community.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PublicFile {

    private String publicFileId;
    private String fileName;
    private String fileSaveName;
    private String fileSavePath;
    private String fileExt;
    private String fileSize;

}
