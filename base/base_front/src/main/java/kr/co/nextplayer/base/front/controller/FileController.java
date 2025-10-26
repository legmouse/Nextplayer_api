package kr.co.nextplayer.base.front.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.board.dto.ReferenceReqDto;
import kr.co.nextplayer.base.file.service.FileService;
import kr.co.nextplayer.base.front.response.board.ReferenceResponse;
import kr.co.nextplayer.base.front.util.StrUtil;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

@Slf4j
@Api(tags = "file")
@RestController
@RequestMapping("/back_front/base_front/api/v1/")
@RequiredArgsConstructor
@CrossOrigin
public class FileController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private FileService fileService;

    @ApiOperation(value = "파일 다운로드", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/common/download")
    public ResponseEntity<InputStreamResource>  referenceList(@RequestParam String fileId)  throws IOException {

        return fileService.download(fileId);
    }

}
