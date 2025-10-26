package kr.co.nextplayer.base.login.oauth.model;

import java.io.Serializable;

import kr.co.nextplayer.base.login.oauth.enums.AuthResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse<T> implements Serializable {
    private int code;

    private String msg;

    private T data;

    public boolean ok() {
        return this.code == AuthResponseStatus.SUCCESS.getCode();
    }
}
