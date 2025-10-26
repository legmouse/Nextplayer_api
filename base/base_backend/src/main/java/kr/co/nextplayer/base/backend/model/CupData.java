package kr.co.nextplayer.base.backend.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CupData {

    private String cupInfo;
    private List<String> cupSubMatch;
    private List<String> cupMainMatch;

}
