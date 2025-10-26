package kr.co.nextplayer.base.backend.dto;

import lombok.Data;

@Data
public class BlogResultDto {

    private String title;
    private String link;
    private String writeDate;
    private String content;
    private String contentImage;

}
