package kr.co.nextplayer.next.lib.common.resolver;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Account {

    private String memberCd;
    private String ip;
    private String accessToken;
    private String refreshToken;
    private String expireMinutes;

    private String accessTokenOrigin;
    private String refreshTokenOrigin;

}
