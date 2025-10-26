package kr.co.nextplayer.base.front.model;

import kr.co.nextplayer.base.front.constants.ScrapForeign;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Scrap {
    private long scrapNo;
    private String ageGroup;
    private ScrapForeign foreignType;
    private long foreignId;
    private String subject;
    private LocalDateTime regDate;
}
