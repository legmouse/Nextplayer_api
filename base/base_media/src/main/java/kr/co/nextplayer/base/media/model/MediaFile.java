package kr.co.nextplayer.base.media.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "media_file")
public class MediaFile implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty(value = "파일 아이디")
    private Long id;

    @ApiModelProperty(value = "부모 파일 아이디")
    private Long parentId;

    @ApiModelProperty(value = "회원번호")
    private String memberCd;

    @ApiModelProperty(value = "파일 유형")
    private String type;

    @ApiModelProperty(value = "원본 파일 이름")
    private String originalName;

    @ApiModelProperty(value = "파일 이름")
    private String name;

    @ApiModelProperty(value = "파일 경로")
    private String path;

    @ApiModelProperty(value = "파일 하위 경로")
    private String subPath;

    @ApiModelProperty(value = "파일 크기")
    private Long size;

    @ApiModelProperty(value = "넓이")
    private Integer width;

    @ApiModelProperty(value = "높이")
    private Integer height;

    @ApiModelProperty(value = "파일 타입")
    private String contentType;

    @ApiModelProperty(value = "파일 상태")
    private String state;

    @ApiModelProperty(value = "리사이즈 파일 여부 Y/N")
    private Boolean isResizeFile;

    @ApiModelProperty(value = "등록 일시")
	private LocalDateTime regDate;

    @ApiModelProperty(value = "참조 키값")
    private Long foreignId;

    @ApiModelProperty(value = "참조 타입")
    private String foreignType;

    private String fileId;
    private String filePath;
}
