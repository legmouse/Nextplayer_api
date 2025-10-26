package kr.co.nextplayer.next.lib.common.property;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SMSProperty implements Serializable {
    private String templatedId;
    private List<SMSRecipient> recipientList;
}
