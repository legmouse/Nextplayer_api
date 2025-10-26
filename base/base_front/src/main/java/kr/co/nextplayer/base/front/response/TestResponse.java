package kr.co.nextplayer.base.front.response;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestResponse {
    private String memberCd;
    private String test;
}
