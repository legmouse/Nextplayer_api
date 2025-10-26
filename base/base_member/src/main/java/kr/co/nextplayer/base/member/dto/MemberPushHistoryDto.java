package kr.co.nextplayer.base.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPushHistoryDto {

    @ApiModelProperty(value = "푸시 아이디", example = "1")
    private int pushId;
    @ApiModelProperty(value = "푸시 제목", example = "푸시 제목")
    private String title;
    @ApiModelProperty(value = "내용", example = "[알림] 금일 광주금호고 vs 경기청운고 경기의 라인업이 등록되었습니다.")
    private String body;
    @ApiModelProperty(value = "경로", example = "%2Fcontest%3FaccordionMenu%3DU20%26yearSelectionMenu%3D2024")
    private String path;
    @ApiModelProperty(value = "대회등록 알림", example = "param")
    private String param;
    @ApiModelProperty(value = "읽음 여부", example = "true")
    private Boolean readFlag ;
    @ApiModelProperty(value = "읽은 날짜", example = "2024-07-29 11:11:11")
    private String readDt;
    @ApiModelProperty(value = "발송 날짜", example = "2024-07-29 11:11:11")
    private String sendDt;
    @ApiModelProperty(value = "케이스 이름", example = "이벤트")
    private String caseName;

}
