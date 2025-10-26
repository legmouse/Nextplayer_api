package kr.co.nextplayer.base.board.model;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Banner implements Serializable {

    private static final long serialVersionUID = 1L;

    private String bannerCrossId;
    private String bannerId;
    private String title;
    private String urlLink;
    private String urlAppLink;
    private String bannerType;
    private String bannerAppType;
    private String authFlag;
    private String openFlag;

    private String configKey;

    private Long viewCnt;

    private List<PublicFile> imgFiles = new ArrayList<PublicFile>();

}
