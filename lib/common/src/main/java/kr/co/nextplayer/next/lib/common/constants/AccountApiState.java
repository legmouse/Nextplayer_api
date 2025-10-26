package kr.co.nextplayer.next.lib.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum AccountApiState {

    /**
    * 업무 api 관련 코드 정의 , 20000 이상의 코드로 지정
    */

    //use by back admin shop ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    APPROVAL(20001,"msg10_adminLogin_permission"),
    IP(20002,"userUuidMsg10003"),
    PW_EXPIRED(20003,"msg11_adminLogin_pass3month"),
    PW_INIT(20004,"msg12_adminLogin_passInit"),
    IP_NOT_AUTH(20005,"msg6_adminLogin_ipIsNotAuthorized"),
    //use by back admin shop ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    //use by fe shop ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    USER_NOT_EXIST(20002,"msg62_auth_userNotExist"),
    USER_INACTIVE(20001,"msg65_auth_inactiveUser"),
    // use by fe shop ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑


    OTHER(29999,"otherMsg");

    /**
     * api 상태 코드
     */
    private Integer code;
    /**
     * 업무 메세지 키 (DB에 저장될 메세지 키)
     */
    private String msgCode;


}
