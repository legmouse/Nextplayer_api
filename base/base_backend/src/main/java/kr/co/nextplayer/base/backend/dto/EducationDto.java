package kr.co.nextplayer.base.backend.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EducationDto {

    private String education_id;
    private String goods_id;
    private String title;
    private String summary;
    private String content;
    private String play_time;
    private String url_link;
    private String preview_link;
    private String move_url;
    private String show_flag;
    private String view_cnt;
    private String reg_date;

    private List<AttchFileInfoDto> imgFiles = new ArrayList<AttchFileInfoDto>();
}
