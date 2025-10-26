package kr.co.nextplayer.base.backend.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ReferenceCrossVO {

    private String foreignId;
    private String foreignType;

}
