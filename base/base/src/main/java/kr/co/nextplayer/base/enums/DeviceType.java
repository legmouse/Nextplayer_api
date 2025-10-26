package kr.co.nextplayer.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원구분
 */
@Getter
@AllArgsConstructor
public enum DeviceType implements BaseEnum<Integer> {

    PC(1,"PC"),
    MOBILE(2,"모바일"),
    ;

    private int code;
    private String label;

    @Override
    public String description() {
        return label;
    }

    @Override
    public Integer code() {
        return code;
    }


    public static DeviceType valueOfCode(int code) {
        DeviceType[] values = values();
        for (DeviceType value : values) {
            if (value.code() == code) {
                return value;
            }
        }
        return null;
    }

    public static DeviceType valueOfName(String name) {
        DeviceType[] values = values();
        for (DeviceType value : values) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return PC;
    }

    public boolean mobile() {
        return this == MOBILE;
    }

}
