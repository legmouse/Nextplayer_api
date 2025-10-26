package kr.co.nextplayer.base.model;

import lombok.Data;

/**
 * 로그인 인증된 회원
 */
@Data
public class AuthenticationMember {

    private String memberCd;
    private String memberId;
    private String memberState;

    /**
     * personal_info
     */
    private String memberNickname;
}
