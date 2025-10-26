package kr.co.nextplayer.next.lib.common.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ResponseDto<T> implements Serializable {

    /**
     * 상태
     */
    @ApiModelProperty(example = "SUCCESS OR FAIL")
    @ApiParam(value = "상태과 코드", required = true)
    @Builder.Default
    private String state = "SUCCESS";

    /**
     * 결과 코드
     */
    @ApiModelProperty(example = "200,401,404,500")
    @ApiParam(value = "결과 코드", required = true)
    @Builder.Default
    private Integer stateCode = 200;

    /**
     * 결과 메시지
     */
    @ApiModelProperty(example = "성공")
    @ApiParam(value = "결과 메시지")
    private String msg;

    /**
     * 결과 데이터
     */
    @ApiParam(value = "결과 데이터")
    private T data;

    /**
     * error 발생시 봔환정보 저장함수
     */
    public void setError(Integer stateCode, String msg) {
        this.stateCode = stateCode;
        this.msg = msg;
        this.state = "FAIL";
    }

}
