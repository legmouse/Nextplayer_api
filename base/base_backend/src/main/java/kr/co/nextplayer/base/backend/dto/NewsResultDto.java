package kr.co.nextplayer.base.backend.dto;

import lombok.Data;

@Data
public class NewsResultDto {

    private String title;
    private String writeDate;
    private String content;
    private String imageList;
    private String source;

}
