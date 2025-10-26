package kr.co.nextplayer.base.file.dto;

import kr.co.nextplayer.base.file.model.MediaFile;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResultMediaFileListDto {
    private MediaFile mediaFile;
    private RequestMediaFileDto request;
    private List<MediaFile> resizeFiles;
}
