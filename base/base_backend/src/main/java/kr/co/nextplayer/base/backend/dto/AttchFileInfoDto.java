package kr.co.nextplayer.base.backend.dto;
import lombok.Data;

@Data
public class AttchFileInfoDto {

	private long fileSize;
	private long publicFileId;
	private String foreignId;
	private String foreignType;
	private String fileSaveName;
	private String fileName;
	private String fileSavePath;
	private String fileExt;
	private String fileUrl;
	private String regDt;
	private String updDt;

    private String subType;

	private String areaFlag;
	private String id;
	private String regId;
	private String korName;
	private String monitoring;
}
