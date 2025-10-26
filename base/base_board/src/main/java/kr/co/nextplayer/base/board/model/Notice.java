package kr.co.nextplayer.base.board.model;

import kr.co.nextplayer.base.file.model.PublicFileModel;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Notice implements Serializable {

    private static final long serialVersionUID = 1L;

    private String boardId;

    private String title;

    private String content;

    private Long viewCnt;

    private String regDate;

    private String parseDate;

    private String useFlag;

    private List<PublicFile> noticeFiles = new ArrayList<PublicFile>();

    private List<PublicFile> imgFiles = new ArrayList<PublicFile>();
    private PublicFile thumbImg = new PublicFile();

}
