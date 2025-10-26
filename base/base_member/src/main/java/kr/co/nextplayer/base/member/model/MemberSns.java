package kr.co.nextplayer.base.member.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.nextplayer.base.config.BaseLocalDateTimeSerializerConfig;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class MemberSns implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 번호
     */
    @ApiModelProperty(value = "번호")
    private Integer snsNo;

    /**
     * sns구분(네이버:NAVER,카카오:KAKAO,구글:GOOGLE,애플:APPLE)
     */
    @ApiModelProperty(value = "sns구분(네이버:NAVER,카카오:KAKAO,구글:GOOGLE,애플:APPLE)")
    private String snsType;

    /**
     * sns_id
     */
    @ApiModelProperty(value = "sns_id")
    private String snsId;


    /**
     * 회원번호
     */
    @ApiModelProperty(value = "회원번호")
    private String memberCd;

    /**
     * 이메일
     */
    @ApiModelProperty(value = "이메일")
    private String email;

    /**
     * 이름
     */
    @ApiModelProperty(value = "이름")
    private String name;

    /**
     * 휴대폰번호
     */
    @ApiModelProperty(value = "휴대폰번호")
    private String phone;

    /**
     * 출생연도
     */
    @ApiModelProperty(value = "출생연도")
    private int age;

    /**
     * 생년월일
     */
    @ApiModelProperty(value = "생년월일")
    private String birthDate;

    /**
     * CI(고유식별정보)
     */
    @ApiModelProperty(value = "CI(고유식별정보)")
    private String ciNo;

    /**
     * 성별(여:W,남:M)
     */
    @ApiModelProperty(value = "성별(여:W,남:M)")
    private String sex;

    /**
     * 프로필사진
     */
    @ApiModelProperty(value = "프로필사진")
    private String photo;

    /**
     * 넥네임
     */
    @ApiModelProperty(value = "넥네임")
    private String nickname;


    @ApiModelProperty(value = "등록일자")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = BaseLocalDateTimeSerializerConfig.LocalDateTimeSerializer.class)
    @JsonDeserialize(using = BaseLocalDateTimeSerializerConfig.LocalDateTimeDeserializer.class)
    private LocalDateTime regDate;

}