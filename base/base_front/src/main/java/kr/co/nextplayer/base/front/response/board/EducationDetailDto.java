package kr.co.nextplayer.base.front.response.board;

import io.swagger.annotations.ApiModel;
import kr.co.nextplayer.base.board.model.Faq;
import kr.co.nextplayer.base.board.model.PublicFile;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class EducationDetailDto {

    private String educationId;
    private String title;
    private String summary;
    private String content;
    private String playTime;
    private String isChecked;
    private String urlLink;
    private String previewLink;
    private String moveUrl;

    private String regDate;

    private String showFlag;
    private String useFlag;

    private int questionCnt;

    private List<Faq> faqList = new ArrayList<Faq>();

}
