package kr.co.nextplayer.base.backend.vo;

import kr.co.nextplayer.base.backend.dto.AttchFileInfoDto;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class ReferenceVO {

    private String referenceId;

    private String foreignId;

    private String title;
    private String content;
    private String viewCnt;
    private String regDate;

    private String method;
    private String foreignType;

    private int cupCnt;

    private String[] cupId;

    /** 검색Keyword */
    private String searchKeyword = "";

    /** 검색사용여부 */
    private String searchUseYn = "";
    private String fromDate;
    private String toDate;
    private String today;
    private String yesterday;
    private String week;
    private String month;
    private String month3;
    private List<AttchFileInfoDto> delFileVO = new ArrayList<>(); //삭제파일목록
    private List<AttchFileInfoDto> fileVO = new ArrayList<>(); //첨부파일목록

    private List<ReferenceCrossVO> crossVO = new ArrayList<>(); // 중간 테이블 데이터

    private String orderBy;
    private String openYn;
    private String grade;

}
