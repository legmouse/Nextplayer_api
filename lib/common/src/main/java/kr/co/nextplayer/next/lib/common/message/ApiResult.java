package kr.co.nextplayer.next.lib.common.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResult {
    private Integer resultCode;
    private String resultMessage;
    private Integer[] recipientList;

    public ApiResult() {
    }

    public ApiResult(Integer resultCode, String resultMessage, Integer[] recipientList) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.recipientList = recipientList;
    }
}
