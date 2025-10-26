package kr.co.nextplayer.base.backend.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class UageDto {

    private String area;
    private String uage;

    private String areaId;
    private String areaName;
    private String ageGroup;
    private String sAreaName;

    private String areaSearch;

}
