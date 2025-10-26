package kr.co.nextplayer.base.board.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class ColumnReqDto {

    private String educationColumnId;

    private String memberCd;
    private String columnId;
    private String columnType;
    private String title;


    private String sKeyword;
    private int curPage;
    private int pageSize;
    private int offset;

}
