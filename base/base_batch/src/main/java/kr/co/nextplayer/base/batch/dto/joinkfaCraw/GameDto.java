package kr.co.nextplayer.base.batch.dto.joinkfaCraw;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/*
    1depth
 */
@Data
public class GameDto {

    private String title;
    private String content;
    private String startDate;
    private String endDate;
    private String place;
    private String ageGroup;
    List<String> dateRange = new ArrayList<>();

}
