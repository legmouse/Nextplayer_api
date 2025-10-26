package kr.co.nextplayer.base.community.mapper;

import kr.co.nextplayer.base.community.dto.ReportDto;
import kr.co.nextplayer.base.community.dto.ReportReqDto;

public interface ReportMapper {

    int insertReport(ReportDto reportDto);

}
