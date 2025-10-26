package kr.co.nextplayer.base.cup.dto.cup;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CupRoundDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String label;
    private int value;

}
