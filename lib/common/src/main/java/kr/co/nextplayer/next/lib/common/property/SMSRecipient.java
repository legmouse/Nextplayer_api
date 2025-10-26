package kr.co.nextplayer.next.lib.common.property;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SMSRecipient implements Serializable {
    private String recipientNo;
    private String countryCode;
    private Map<String, String> templateParameter;
}
