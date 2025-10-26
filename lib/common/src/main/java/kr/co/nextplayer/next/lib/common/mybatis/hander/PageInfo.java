package kr.co.nextplayer.next.lib.common.mybatis.hander;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageInfo {
    // 현재 페이지 번호
    private int currentPage = 1;
    // 페이지 사이즈
    private int pageSize = 10;
    // 데이터 총 수 (전체 페이지 수를 계산하는 데 사용)
    private int totalSize;

    // 현재 페이지의 시작 줄
    private int offset;


    public PageInfo(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        //현재 페이지의 시작 줄 가져오기
        this.offset = (this.currentPage - 1) * this.pageSize;
    }

    public void setCurrentPage(int current) {
        if (current >= 1) {
            this.currentPage = current;
        }
    }

    public void setPageSize(int pageSize) {
        if (pageSize >= 1 && pageSize <= 100) {
            this.pageSize = pageSize;
        }
    }

    public void setTotalSize(int totalSize) {
        if (totalSize >= 0) {
            this.totalSize = totalSize;
        }
    }

    /**
     * 전체 페이지 수 가져오기
     *
     * @return
     */
    public int getTotalPage() {
        // rows / limit [+1]
        if (totalSize % pageSize == 0) {
            return totalSize / pageSize;
        } else {
            return totalSize / pageSize + 1;
        }
    }

    /**
     * 시작 페이지 번호를 가져오기. 기본값은 현재 페이지의 앞과 뒤의 2개 페이지 번호를 보여줍니다.
     *
     * @return
     */
    public int getFrom() {
        int from = currentPage - 2;
        return from < 1 ? 1 : from;
    }

    /**
     * 끝 페이지 번호 가져오기
     *
     * @return
     */
    public int getTo() {
        int to = currentPage + 2;
        int total = getTotalPage();
        return to > total ? total : to;
    }
}
