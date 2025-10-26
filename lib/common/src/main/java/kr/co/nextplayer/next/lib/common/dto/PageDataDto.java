package kr.co.nextplayer.next.lib.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class PageDataDto<T> implements Serializable {

    @ApiModelProperty(value = "조회 결과 데이터")
    T dataList;

    @ApiModelProperty(value = "총 데이터 수", example = "0")
    private int totalCount;

}
