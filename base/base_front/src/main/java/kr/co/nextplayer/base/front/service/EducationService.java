package kr.co.nextplayer.base.front.service;

import kr.co.nextplayer.base.board.dto.*;
import kr.co.nextplayer.base.board.mapper.BoardMapper;
import kr.co.nextplayer.base.board.mapper.EducationMapper;
import kr.co.nextplayer.base.board.model.*;
import kr.co.nextplayer.base.front.mapper.UageMapper;
import kr.co.nextplayer.base.front.response.board.*;
import kr.co.nextplayer.base.media.mapper.MediaFileMapper;
import kr.co.nextplayer.base.media.model.Media;
import kr.co.nextplayer.base.media.model.MediaFile;
import kr.co.nextplayer.next.lib.common.mybatis.hander.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
@Transactional
public class EducationService {

    @Resource
    private EducationMapper educationMapper;

    @Resource
    private UageMapper uageMapper;

    public EducationResponse getEducationList(EducationReqDto educationReqDto) throws Exception {

        int totalCount = 0;
        List<EducationListDto> list = new ArrayList<EducationListDto>();

        try {

            totalCount = educationMapper.selectEducationCount(educationReqDto);

            PageInfo pageInfo = new PageInfo(educationReqDto.getCurPage(), 10);
            educationReqDto.setOffset(pageInfo.getOffset());
            educationReqDto.setPageSize(pageInfo.getPageSize());

            List<Education> educations = educationMapper.selectEducationList(educationReqDto);

            if (educations.size() > 0) {
                for (Education edu : educations) {
                    EducationListDto educationListDto = EducationListDto.builder()
                        .educationId(edu.getEducationId())
                        .title(edu.getTitle())
                        .summary(edu.getSummary())
                        .playTime(edu.getPlayTime())
                        .isChecked(edu.getIsChecked())
                        .regDate(edu.getRegDate())
                        .showFlag(edu.getShowFlag())
                        .imgFiles(edu.getImgFile())
                        .build();
                    list.add(educationListDto);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        EducationResponse eduRes = EducationResponse.builder()
            .totalCount(totalCount)
            .educationList(list)
            .build();

        return eduRes;
    }

    public EducationDetailResponse getEducationDetail(EducationReqDto educationReqDto) throws Exception {

        EducationDetailDto eduDetail = new EducationDetailDto();
        int questionCnt = 0;
        try {

            Education education = educationMapper.selectEducationDetail(educationReqDto);
            questionCnt = educationMapper.selectEducationQuestionCount(educationReqDto);

            eduDetail.setEducationId(education.getEducationId());
            eduDetail.setTitle(education.getTitle());
            eduDetail.setSummary(education.getSummary());
            eduDetail.setContent(education.getContent());
            eduDetail.setPlayTime(education.getPlayTime());
            eduDetail.setIsChecked(education.getIsChecked());
            eduDetail.setUrlLink(education.getUrlLink());
            eduDetail.setPreviewLink(education.getPreviewLink());
            eduDetail.setMoveUrl(education.getMoveUrl());
            eduDetail.setRegDate(education.getRegDate());
            eduDetail.setFaqList(education.getFaqList());
            eduDetail.setQuestionCnt(questionCnt);

        } catch (Exception e) {
            e.printStackTrace();
        }

        EducationDetailResponse eduRes = EducationDetailResponse.builder()
            .educationDetail(eduDetail)
            .build();

        return eduRes;
    }

    public int addEducationAuth(EducationAuthReqDto authReqDto) {
        int result = 0;

        Education educationInfo = educationMapper.selectSearchEducationByGoods(authReqDto);

        if (educationInfo != null) {
            Map<String, String> param = new HashMap<String, String>();
            param.put("educationId", educationInfo.getEducationId());
            param.put("memberCd", authReqDto.getMemberCd());
            authReqDto.setGoodsId(educationInfo.getGoodsId());

            int duplicateChk = educationMapper.selectCheckEducationAuthDuplicate(authReqDto);

            if (duplicateChk == 0) {
                result = educationMapper.insertEducationAuth(param);
            }
        }

        return result;
    }

    public ColumnListResponse getColumnList(ColumnReqDto columnReqDto) {

        List<HashMap<String, Object>> menuList = uageMapper.selectCategoryMenu("C0001");

        int totalCount = educationMapper.selectColumnCnt(columnReqDto);

        PageInfo pageInfo = new PageInfo(columnReqDto.getCurPage(), 10);
        columnReqDto.setOffset(pageInfo.getOffset());
        columnReqDto.setPageSize(pageInfo.getPageSize());

        List<Column> columnList = educationMapper.selectColumnList(columnReqDto);

        List<ColumnListDto> list = new ArrayList<ColumnListDto>();

        if (columnList.size() > 0) {
            for (Column column : columnList) {
                ColumnListDto columnListDto = ColumnListDto.builder()
                    .educationColumnId(column.getEducationColumnId())
                    .title(column.getTitle())
                    .writer(column.getWriter())
                    .summary(column.getSummary())
                    .regDate(column.getRegDate())
                    .columnType(column.getColumnType())
                    .showFlag(column.getShowFlag())
                    .submitDate(column.getSubmitDate())
                    .regDate(column.getRegDate())
                    .imgFiles(column.getImgFile())
                    .build();
                list.add(columnListDto);
            }
        }

        ColumnListResponse columnRes = ColumnListResponse.builder()
            .totalCount(totalCount)
            .columnList(list)
            .menuList(menuList)
            .build();

        return columnRes;
    }

    public ColumnDetailResponse getColumnDetail(ColumnReqDto columnReqDto) {

        Column column = educationMapper.selectColumnDetail(columnReqDto);
        educationMapper.updateColumnViewCnt(columnReqDto);

        columnReqDto.setColumnType(column.getColumnType());

        List<Column> type = educationMapper.selectPrevNextColumn(columnReqDto);

        Optional<Column> nextColumnOptional = type.stream() .filter(column1 -> column1.getMType().equals("next")) .findFirst();
        Column nextColumn = nextColumnOptional.orElse(null);

        Optional<Column> prevColumnOptional = type.stream() .filter(column2 -> column2.getMType().equals("prev")) .findFirst();
        Column prevColumn = prevColumnOptional.orElse(null);

        ColumnDetailDto columnDetailDto = ColumnDetailDto.builder()
            .educationColumnId(column.getEducationColumnId())
            .title(column.getTitle())
            .writer(column.getWriter())
            .content(column.getContent())
            .columnType(column.getColumnType())
            .submitDate(column.getSubmitDate())
            .regDate(column.getRegDate())
            .urlLink(column.getUrlLink())
            .prevColumn(prevColumn)
            .nextColumn(nextColumn)
            .build();

        ColumnDetailResponse eduRes = ColumnDetailResponse.builder()
            .columnDetail(columnDetailDto)
            .build();

        return eduRes;
    }

    public EducationFileListResponse getEducationFileList(EducationReqDto educationReqDto) {


        List<EducationFileListDto> list = new ArrayList<EducationFileListDto>();

        List<EduFile> eduFiles = educationMapper.selectEducationFileList(educationReqDto);

        if (eduFiles.size() > 0) {
            for (EduFile edu : eduFiles) {
                EducationFileListDto educationListDto = EducationFileListDto.builder()
                    .educationFileId(edu.getEducationFileId())
                    .title(edu.getTitle())
                    .regDate(edu.getRegDate())
                    .eduFile(edu.getEduFile())
                    .build();
                list.add(educationListDto);
            }
        }


    EducationFileListResponse eduRes = EducationFileListResponse.builder()
            .educationFileList(list)
            .build();

        return eduRes;
    }

}
