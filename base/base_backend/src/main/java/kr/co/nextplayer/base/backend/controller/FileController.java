package kr.co.nextplayer.base.backend.controller;

import kr.co.nextplayer.base.backend.dto.AttchFileInfoDto;
import kr.co.nextplayer.base.backend.dto.MediaFileInfoDto;
import kr.co.nextplayer.base.backend.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping(value = "/common")
public class FileController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    FileService fileService;

    @RequestMapping(value = "/list.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> list(HttpServletRequest req, AttchFileInfoDto vo) throws Exception {
        logger.info("list was called. params:" + vo);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", fileService.selectFileInfoList(vo));
        map.put("vo", vo);
        return map;
    }

    @RequestMapping(value = "/imgFileList.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> list2(HttpServletRequest req, AttchFileInfoDto vo) throws Exception {
        logger.info("list was called. params:" + vo);
        vo.setSubType("imgFile");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", fileService.selectFileInfoList(vo));
        map.put("vo", vo);
        return map;
    }

    @RequestMapping(value = "/imgFileListParam.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> imgFileListParam(@RequestParam String subType, HttpServletRequest req, AttchFileInfoDto vo) throws Exception {
        logger.info("imgFileListParam was called. params:" + subType);
        vo.setSubType(subType);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", fileService.selectFileInfoList(vo));
        map.put("vo", vo);
        return map;
    }

    @RequestMapping(value = "/download.do")
    public void download(HttpServletRequest req, HttpServletResponse res, AttchFileInfoDto vo) throws IOException, SQLException {
        if (vo.getPublicFileId() > 0){
            fileService.download(res, vo);
        }
    }

    @RequestMapping(value = "/download2.do")
    public void download2(HttpServletRequest req, HttpServletResponse res, MediaFileInfoDto vo) throws IOException, SQLException {
        if (vo.getFileId() > 0){
            fileService.download2(res, vo);
        }
    }

}
