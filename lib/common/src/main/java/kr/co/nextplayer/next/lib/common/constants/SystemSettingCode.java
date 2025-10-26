package kr.co.nextplayer.next.lib.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum SystemSettingCode {

    // in db table(admin_system_config) >> DB에서 관리하는 값 >>>>>>>>>>>>>>>>>>>>

    //비밀번호
    PW_EXPIRED_TIME("PW_EXPIRED_TIME"), //비밀번호 변경유지 기간
    FIND_PASS_URL("FIND_PASS_URL"), //비밀번호 찾기 url


    // in db table(admin_system_config) >> DB에서 관리하는 값 <<<<<<<<<<<<<<<<<<<<<


    //not in db >> DB에서 관리 않하는 값  >>>>>>>>>>>>>>>>>>>>>>>>>>>
    ACCESS_TOKEN_TIME("30"), // 어드민 > 세션 토큰 유지 시간(min)
    INIT_PASSWORD("1234"), // 비밀번호 초기화 값
    INIT_MEMBER_RESERVE_POINT("1000"), // 온라인 가입 회원에게 적립금 1,000원 지급

    // 쇼핑몰 세션 토큰 유지 시간(min)
    ACCESS_TOKEN_TIME_PC(Integer.toString(60 * 6)), //PC

    ACCESS_TOKEN_TIME_PC_LONG(Integer.toString(60 * 24)), //PC
    ACCESS_TOKEN_TIME_MO(Integer.toString(60 * 24 * 31)), //Mobile (테스트용 > 로그인 만료 시간 단축)
    //ACCESS_TOKEN_TIME_MO("20160"), //Mobile
    //ACCESS_TOKEN_TIME_KEEP("20160"), //쇼핑몰 + 로그인 유지
    ACCESS_TOKEN_TIME_KEEP(Integer.toString(60 * 24 * 31)), //쇼핑몰 + 로그인 유지


    //회원 로그인 블록 구분
    MEMBER_BLOCK_LOGIN("1"), //차단


    // 회원정보 조회 범위 (1:일부, 2:전체)
    USER_INFO_OPEN_PART("1"),
    USER_INFO_OPEN_ALL("2"),


    // 회원 상태(0:탈퇴,1:일반,3:차단,4:휴먼,5:삭제,NULL:오프라인회원)
    MEMBER_STATE_LEAVE("0"),
    MEMBER_STATE_GENERAL("1"),
    MEMBER_STATE_BLOCK("3"),
    MEMBER_STATE_HUMAN("4"),
    MEMBER_STATE_DELETE("5"),

    // 관리자 페이지에서 회원 관리일 경우
    CERT_TYPE_A("A"),
    //인증수단(휴대폰:P,SMS:S,이메일:E,기업:C) + sns구분(네이버:NAVER,카카오:KAKAO,구글:GOOGLE,애플:APPLE)
    CERT_TYPE_P("P"),
    CERT_TYPE_SMS("S"),
    CERT_TYPE_E("E"),
    CERT_TYPE_C("C"),
    CERT_TYPE_G("G"),
    CERT_TYPE_NAVER("NAVER"),
    CERT_TYPE_KAKAO("KAKAO"),
    CERT_TYPE_GOOGLE("GOOGLE"),
    CERT_TYPE_APPLE("APPLE"),

    //인증 타입
    CERT_GBN_JOIN("JOIN"),
    CERT_GBN_FIND("FIND"),

    //회원 휴면구분(휴면:0,탈퇴:1,삭제:2)
    INACTIVE_TYPE_SLEEP("0"),
    INACTIVE_TYPE_WITHDRAWAL("1"),
    INACTIVE_TYPE_DELETE("2"),

    //회원 휴면/탈퇴 등록자 구분(1:본인,2:운영자,3:배치작업)
    INACTIVE_SELF("1"),
    INACTIVE_ADMIN("2"),
    INACTIVE_BATH("3"),

    //회원 가입 방식
    REG_TYPE_SELF_MOBILE("REG_TYPE_SELF_MOBILE"),
    REG_TYPE_SELF_SMS("REG_TYPE_SELF_SMS"),
    REG_TYPE_SELF_EMAIL("REG_TYPE_SELF_EMAIL"),
    REG_TYPE_COMPANY("REG_TYPE_COMPANY"),
    REG_TYPE_SNS_NAVER("REG_TYPE_SNS_NAVER"),
    REG_TYPE_SNS_APPLE("REG_TYPE_SNS_APPLE"),

    // 증감구분(증감:+,차감:-)
    SYMBOL_PLUS("+"),
    SYMBOL_MINUS("-"),

    //파일 상태(임시저장:TEMP,저장확인:SAVE,삭제:DELETE)
    FILE_STATE_TEMP("TEMP"),
    FILE_STATE_SAVED("SAVED"),
    FILE_STATE_DELETE("DELETE"),

    // 파일 유형
    FILE_IMAGE("IMAGE"),
    FILE_VIDEO("VIDEO"),
    FILE_AUDIO("AUDIO"),
    FILE_ETC("ETC"),

    // 리사이즈 유형
    RESIZE_WIDTH("RESIZE_WIDTH"),
    RESIZE_HEIGHT("RESIZE_HEIGHT"),


    //not in db >> DB에서 관리 않하는 값 <<<<<<<<<<<<<<<<<<<<<<<<<<<

    //sample
    SYSTEM_XXX_CODE("XX"); //


    private String code;

}
