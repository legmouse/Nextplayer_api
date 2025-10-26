package kr.co.nextplayer.base.batch.firebase;

import lombok.Data;

@Data
public class PushMember {
    private int pushId;
    private String memberCd;
    private String fcmToken;
}
