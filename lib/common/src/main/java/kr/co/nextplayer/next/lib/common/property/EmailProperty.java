package kr.co.nextplayer.next.lib.common.property;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailProperty implements Serializable {
    private String senderAddress;
    private String senderName;
    String templateId;
    Map<String, Object> templateParameter;
    List<ReceiveMailAddress> receiverList;
}
