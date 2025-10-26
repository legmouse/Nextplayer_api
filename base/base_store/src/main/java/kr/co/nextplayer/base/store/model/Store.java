package kr.co.nextplayer.base.store.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Store {

    private String memberCd;
    private int cMoney;

}
