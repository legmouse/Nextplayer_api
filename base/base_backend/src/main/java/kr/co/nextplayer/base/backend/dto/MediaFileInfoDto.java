package kr.co.nextplayer.base.backend.dto;
import lombok.Data;

@Data
public class MediaFileInfoDto {

	private long fileId;
    private String name;
    private String originalName;
    private String path;
    private String fileExt;

    private String fileSavePath;

}
