package kr.co.nextplayer.base.batch.dto;

import kr.co.nextplayer.base.batch.enums.AgeGroup;
import kr.co.nextplayer.base.batch.enums.MatchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CupCrawMatchList {
    private int cupId;
    private int matchId;
    private int matchOrder;
    private int endFlag;
    private int afterEndCrawCnt;
    private String title;
    private MatchType matchType;
    private AgeGroup ageGroup;
}
