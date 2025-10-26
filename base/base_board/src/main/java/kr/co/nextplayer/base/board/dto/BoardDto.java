package kr.co.nextplayer.base.board.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class BoardDto {

    private String customer;
    private String phone;
    private String email;
    private String title;
    private String content;

}
