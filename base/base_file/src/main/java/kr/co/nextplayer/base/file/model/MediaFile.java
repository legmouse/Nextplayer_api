package kr.co.nextplayer.base.file.model;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MediaFile {
    private Long id;

    private Long parentId;

    private String memberCd;

    private String type;

    private String originalName;

    private String name;

    private String path;

    private String subPath;

    private Long size;

    private Integer width;

    private Integer height;

    private String contentType;

    private String state;

    private Boolean isResizeFile;

    private LocalDateTime regDate;
}
