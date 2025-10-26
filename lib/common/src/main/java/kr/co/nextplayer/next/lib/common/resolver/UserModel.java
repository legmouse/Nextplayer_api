package kr.co.nextplayer.next.lib.common.resolver;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserModel {

    private String memberCd;
    private String ip;
    private String accessToken;
    private String refreshToken;
    private String expireMinutes;
}
