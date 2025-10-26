package kr.co.nextplayer.base.batch.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum AgeGroup {
    U11("51"),
    U12("51"),
    U14("52"),
    U15("52"),
    U17("53"),
    U18("53"),
    U20("54"),
    U22("54");

    private String level;

    public String getLevel() {
        return level;
    }

}
