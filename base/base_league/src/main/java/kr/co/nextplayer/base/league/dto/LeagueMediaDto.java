package kr.co.nextplayer.base.league.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LeagueMediaDto {

    private String media_type;
    private String media_id;
    private String url_link;
    private String video_type;
    private String show_flag;
    private String img_url;

}
