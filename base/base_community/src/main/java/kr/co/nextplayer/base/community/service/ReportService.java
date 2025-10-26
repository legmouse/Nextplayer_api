package kr.co.nextplayer.base.community.service;

import kr.co.nextplayer.base.community.dto.ReportDto;
import kr.co.nextplayer.base.community.dto.ReportReqDto;
import kr.co.nextplayer.base.community.mapper.ReportMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class ReportService {

    @Resource
    private ReportMapper reportMapper;

    public int saveReport(ReportDto reportDto) throws Exception {
        int result = 0;
        try {
            result = reportMapper.insertReport(reportDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }

}
