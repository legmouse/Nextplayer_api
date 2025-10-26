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
public class Popup implements Serializable {

    private static final long serialVersionUID = 1L;

    private String popupId;
    private String title;
    private String urlLink;
    private String urlAppLink;
    private String popupType;
    private String popupAppType;
    private String authFlag;

    private List<PublicFile> imgFiles = new ArrayList<PublicFile>();

}
