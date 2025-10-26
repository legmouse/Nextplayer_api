package kr.co.nextplayer.next.lib.common.property;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveMailAddress implements Serializable {
    private String receiveMailAddr;
    private String receiveName;
    private String receiveType;
}
