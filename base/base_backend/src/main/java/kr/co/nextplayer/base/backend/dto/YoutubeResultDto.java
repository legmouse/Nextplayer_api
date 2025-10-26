package kr.co.nextplayer.base.backend.dto;

import lombok.Data;

@Data
public class YoutubeResultDto {

    private String title;
    private String thumbnail;
    private String link;
    private String uploadDate;
    private String infoText;
    private String infoLink;
    private String embedded;

}
