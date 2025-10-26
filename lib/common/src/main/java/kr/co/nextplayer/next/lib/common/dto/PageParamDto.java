package kr.co.nextplayer.next.lib.common.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * kr.co.nextplayer.next.lib.common.mybatis.hander.PageInfo의 DTO 버전
 */
public class PageParamDto {
    @Getter
    @Schema(description = "현재 페이지 번호")
    private int currentPage = 1;

    @Getter
    @Schema(description = "페이지 사이즈")
    private int pageSize = 10;

    /** 전체 Row 갯수(페이지 갯수, 끝 페이지 계산에 사) */
    private int totalCount = 0;

    public void setCurrentPage(int value) {
        if (value >= 1) {
            this.currentPage = value;
        }
    }

    public void setPageSize(int value) {
        if (value >= 1 && value <= 100) {
            this.pageSize = value;
        }
    }

    public void setTotalCount(int value) {
        if (value >= 0) {
            this.totalCount = value;
        }
    }

    /**
     * 전체 페이지 수 가져오기
     *
     * @return
     */
    @ApiModelProperty(hidden = true)
    public int getTotalPage() {
        // rows / limit [+1]
        if (totalCount % pageSize == 0) {
            return totalCount / pageSize;
        } else {
            return totalCount / pageSize + 1;
        }
    }

    /**
     * 시작 페이지 번호를 가져오기. 기본값은 현재 페이지의 앞과 뒤의 2개 페이지 번호를 보여줍니다.
     *
     * @return
     */
    @ApiModelProperty(hidden = true)
    public int getFrom() {
        int from = currentPage - 2;
        return from < 1 ? 1 : from;
    }

    /**
     * 끝 페이지 번호 가져오기
     *
     * @return
     */
    @ApiModelProperty(hidden = true)
    public int getTo() {
        int to = currentPage + 2;
        int total = getTotalPage();
        return to > total ? total : to;
    }

    /** 현재 페이지의 DB Offset */
    @ApiModelProperty(hidden = true)
    public int getOffset() {
        return (this.currentPage - 1) * this.pageSize;
    }
}
