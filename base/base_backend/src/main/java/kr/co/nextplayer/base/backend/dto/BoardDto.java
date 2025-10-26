package kr.co.nextplayer.base.backend.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BoardDto {

    private String board_id;
    private String title;
    private String content;
    private String view_cnt;
    private String reg_date;
    private String show_flag;

    private List<AttchFileInfoDto> files = new ArrayList<AttchFileInfoDto>();

    private List<AttchFileInfoDto> imgFiles = new ArrayList<AttchFileInfoDto>();
}
