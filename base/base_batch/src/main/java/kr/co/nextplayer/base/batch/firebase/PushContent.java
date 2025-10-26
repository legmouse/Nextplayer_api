package kr.co.nextplayer.base.batch.firebase;

import lombok.Data;

@Data
public class PushContent {
    private int pushContentId;
    private String title;
    private String body;
    private String param;
    private String path;
    private String description;
    private String sendFlag;
    private String regDt;
    private String sendDt;
}
