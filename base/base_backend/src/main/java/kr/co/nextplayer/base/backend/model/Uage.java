package kr.co.nextplayer.base.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Uage {

    private String area;
    private String uage;

    private String areaId;
    private String areaName;
    private String ageGroup;
    private String sAreaName;

    private String areaSearch;

    private String uageFlag;

    private String regDate;

}
