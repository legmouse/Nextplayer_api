package kr.co.nextplayer.base.file.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class FileDto {

    private String publicFileId;
    private String foreignId;
    private String foreignType;
    private String fileName;
    private String fileSaveName;
    private String fileSavePath;
    private String fileExt;
    private String fileSize;

}
