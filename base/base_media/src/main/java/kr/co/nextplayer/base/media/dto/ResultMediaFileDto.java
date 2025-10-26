package kr.co.nextplayer.base.media.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.nextplayer.base.media.model.MediaFile;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResultMediaFileDto {
    
    @ApiModelProperty(value = "파일 URL")
    String fullPath;

    @ApiModelProperty(value = "MediaFile 정보")
    private MediaFile mediaFile;

}
