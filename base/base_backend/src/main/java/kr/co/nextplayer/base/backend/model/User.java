package kr.co.nextplayer.base.backend.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class User {

    private String userId;
    private String userName;
    private String id;
    private String pw;
    private String sessionKey;
    private String grade;
    private String useFlag;

    private String sessionDate;
    private String regDate;

}
