package kr.co.nextplayer.base.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.nextplayer.base.config.BaseLocalDateTimeSerializerConfig;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class MemberDto {

    @ApiModelProperty(value = "검색 시작일", example = "2022-03-01")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = BaseLocalDateTimeSerializerConfig.LocalDateTimeDeserializer.class)
    private LocalDateTime startRegDate;
    @ApiModelProperty(value = "검색 종료일", example = "2022-11-01")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = BaseLocalDateTimeSerializerConfig.LocalDateTimeDeserializer.class)
    private LocalDateTime endRegDate;

    @ApiModelProperty(value = "회원 상태(0:탈퇴,1:일반,3:차단,4:휴먼,5:삭제,null:오프라인회원)", example = "")
    private String memberState;

    @ApiModelProperty(value = "회원번호")
    private String memberCd;
    @ApiModelProperty(value = "이름")
    private String name;
    @ApiModelProperty(value = "닉네임")
    private String nickName;
    @ApiModelProperty(value = "휴대폰번호")
    private String mobile;
    @ApiModelProperty(value = "이메일")
    private String email;


    public LocalDateTime getEndRegDate() {
        if (this.endRegDate != null) {
            return endRegDate.plusHours(23).plusMinutes(59).plusSeconds(59);
        }
        return null;
    }
}
