package kr.co.nextplayer.base.media.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import kr.co.nextplayer.base.media.dto.RequestMediaFileDto;
import kr.co.nextplayer.base.media.dto.ResultMediaFileDto;
import kr.co.nextplayer.base.media.dto.ResultMediaFileListDto;
import kr.co.nextplayer.base.media.model.MediaFile;
import kr.co.nextplayer.base.media.service.MediaFileService;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import kr.co.nextplayer.next.lib.common.resolver.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Api(tags = {"Media"})
@RestController
@RequestMapping("/back_front/base_media/api/v1/mediaFile")
public class MediaFileRestController {

    @Value("${nextplayer.media.file-server}")
    String fileServer;

    @Value("${nextplayer.media.image-resizes}")
    Integer[] imageResizes;

    @Autowired
    MediaFileService mediaFileService;

    /**
     * 파일 정보
     * @param id
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "파일 정보")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path", name = "id", value = "파일 ID", required = true, defaultValue = "None", dataTypeClass = Long.class)
    })
    @GetMapping("/{id}")
    public Mono<ResponseDto<ResultMediaFileDto>> view(@PathVariable Long id, @RequestParam(required = false) String size, @ApiIgnore ServerHttpResponse response) throws IOException {
        if (size == null){
            // size가 없으면 원본 파일
            return mediaFileService.findById(id).flatMap(mediaFile -> {
                String fullPath = fileServer + mediaFile.getPath() + "/" + mediaFile.getName();
                ResultMediaFileDto result = ResultMediaFileDto.builder()
                        .fullPath(fullPath)
                        .mediaFile(mediaFile)
                        .build();
                return Mono.just(ResponseDto.<ResultMediaFileDto>builder()
                        .state("SUCCESS")
                        .stateCode(HttpStatus.OK.value())
                        .msg("파일 정보")
                        .data(result)
                        .build());
            }).switchIfEmpty(Mono.just(ResponseDto.<ResultMediaFileDto>builder()
                    .state("FAIL")
                    .stateCode(HttpStatus.NOT_FOUND.value())
                    .msg("파일 정보가 없습니다.")
                    .build()));

        } else {
            List<String> imageResizeList = new ArrayList<>();
            for (Integer imageResize : imageResizes) {
                imageResizeList.add(imageResize.toString());
            }
            if( !imageResizeList.contains(size) ){
                return Mono.just(ResponseDto.<ResultMediaFileDto>builder()
                        .state("FAIL")
                        .stateCode(HttpStatus.BAD_REQUEST.value())
                        .msg("지원하지 않는 사이즈 입니다.")
                        .build());
            }
        }

        return mediaFileService.findByParentIdAndWidth(id, Integer.parseInt(size)).flatMap(mediaFile -> {
            String fullPath = fileServer + mediaFile.getPath() + "/" + mediaFile.getName();
            ResultMediaFileDto result = ResultMediaFileDto.builder()
                .fullPath(fullPath)
                .mediaFile(mediaFile)
                .build();
            return Mono.just(ResponseDto.<ResultMediaFileDto>builder()
                .state("SUCCESS")
                .stateCode(HttpStatus.OK.value())
                .msg("파일 정보")
                .data(result)
                .build());
        }).switchIfEmpty(Mono.just(ResponseDto.<ResultMediaFileDto>builder()
            .state("FAIL")
            .stateCode(HttpStatus.NOT_FOUND.value())
            .msg("파일 정보가 없습니다.")
            .build()));
    }

    /**
     * 파일 업로드
     * @return
     */
    @ApiOperation(value = "파일 업로드")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "header", name = "accessToken", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "path", name = "subPath", value = "서브 경로(커뮤니티: community, 문의 요청: request)", required = true, defaultValue = "None", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "form", name = "files", value = "첨부파일", required = true, allowMultiple = true, defaultValue = "None", dataTypeClass = MultipartFile.class),
        @ApiImplicitParam(paramType = "query", name = "isResize", value = "리사이즈 여부 (true, false)", required = false, defaultValue = "false", dataTypeClass = Boolean.class),
        @ApiImplicitParam(paramType = "query", name = "resizeType", value = "리사이즈 유형 (width, height)", required = false, defaultValue = "width", dataTypeClass = String.class),
        /*@ApiImplicitParam(paramType = "query", name = "foreignId", value = "참조 id", required = false, dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "foreignType", value = "참조 종류", required = false, dataTypeClass = String.class),*/
    })
    @PostMapping(value = "/upload/{subPath}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Mono<ResponseDto<ResultMediaFileListDto>> upload(
        @ApiIgnore UserModel userModel,
        @PathVariable String subPath,
        @RequestBody Flux<Part> files,
        @RequestParam(defaultValue = "true") Boolean isResize,
        @RequestParam(defaultValue = "width") String resizeType
        /*@RequestParam(required = false) String foreignId,
        @RequestParam(required = false) String foreignType*/
    ) throws CommonLogicException {

        // TODO: 회원번호
        //String memberCd = "admin".toString();
        String memberCd = userModel.getMemberCd();

        RequestMediaFileDto request = RequestMediaFileDto.builder()
            .files(files)
            .isResize(isResize)
            .resizeType(resizeType)
            .memberCd(userModel.getMemberCd())
            .subPath(subPath)
            .foreignType(subPath)
            .build();

        Flux<MediaFile> result = mediaFileService.uploadFiles(request);
        Mono<List<MediaFile>> list = result.collectList();

        final ResultMediaFileListDto dto = ResultMediaFileListDto.builder()
                .resizeFiles(new ArrayList<MediaFile>())
                .request(request)
                .build();

        List<MediaFile> media = new ArrayList<>();
        return list.map(mediaFiles -> {
            for (MediaFile mediaFile : mediaFiles) {
                if (!mediaFile.getIsResizeFile()) {
                    //dto.setMediaFile(mediaFile);
                    media.add(mediaFile);
                } else {
                    dto.getResizeFiles().add(mediaFile);
                }
            }
            dto.setMediaFiles(media);

            request.setFiles(null);
            return ResponseDto.<ResultMediaFileListDto>builder()
                .data(dto)
                .build();
        });
    }

    /**
     * 파일 삭제
     * @param id
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "파일 삭제")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "파일 ID", required = true, defaultValue = "None", dataTypeClass = Long.class, paramType = "path")
    })
    @DeleteMapping("/{id}")
    public Mono<ResponseDto<Long>> delete(@PathVariable Long id) throws IOException {
        return mediaFileService.deleteWithFile(id).flatMap(result -> {
            return Mono.just(ResponseDto.<Long>builder()
                .data(result)
                .build());
        });
    }
}