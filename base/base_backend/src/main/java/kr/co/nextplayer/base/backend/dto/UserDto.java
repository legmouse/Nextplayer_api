package kr.co.nextplayer.base.backend.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class UserDto {

    private String userId;
    private String userName;
    private String id;
    private String pw;
    private String sessionKey;
    private String grade;
    private String useFlag;

    private String useCookieId;
    private String useCookiePw;

    private String sessionDate;
    private String regDate;

}
