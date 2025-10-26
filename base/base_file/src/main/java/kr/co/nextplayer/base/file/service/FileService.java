package kr.co.nextplayer.base.file.service;

import com.sun.jdi.InvalidTypeException;
import kr.co.nextplayer.base.file.dto.FileDto;
import kr.co.nextplayer.base.file.dto.RequestMediaFileDto;
import kr.co.nextplayer.base.file.mapper.FileMapper;
import kr.co.nextplayer.base.file.model.MediaFile;
import kr.co.nextplayer.base.file.model.PublicFileModel;
import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.constants.SystemSettingCode;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    @Resource
    private FileMapper fileMapper;

    private String getEncodedFileName(String originalFileName) {
        return URLEncoder.encode(originalFileName, StandardCharsets.UTF_8)
                        .replaceAll("\\+", "%20")
                        .replaceAll("\\%21", "!")
                        .replaceAll("\\%27", "'")
                        .replaceAll("\\%28", "(")
                        .replaceAll("\\%29", ")")
                        .replaceAll("\\%7E", "~")
                        .replaceAll("\\%2C", ",")
                        .replaceAll("\\%23", "#");
    }

    /*public Mono<List<FileDto>> test(Flux<FilePart> files, String type) {
        List<FileDto> list = new ArrayList<FileDto>();
        return test2(files).flatMap(filePart -> {
                System.out.println("filePart.filename() = " + filePart);
                return Mono.empty();
            }).collectList();
    }*/

    public Flux<FileDto> test2(Flux<FilePart> files) {
        return files.flatMap(file -> {
            FileDto fileDto = new FileDto();
            fileDto.setFileName(file.name());
            return Mono.just(fileDto).doOnSuccess(s -> {
                System.out.println("fileDto = " + fileDto);
            });
        });
    }

    public ResponseEntity<InputStreamResource> download(String vo) throws IOException {

        PublicFileModel eo = fileMapper.selectPublicFile(vo);

        Path filePath = Paths.get(eo.getFileSavePath());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(filePath.toString()));

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .cacheControl(CacheControl.noCache())
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + getEncodedFileName(eo.getFileName()))
            .body(resource);
    }

    final static List<String> ALLOW_EXT = Arrays.asList("JPEG","BMP","PNG","JPG");
    public List<FileDto> uploadFile(Flux<FilePart> files, String type) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        List<FileDto> list = new ArrayList<FileDto>();

        files.subscribe(file -> {

            boolean isUploadable = false;

            String uploadDir = "";

            if (type.equals("Community")) {
                uploadDir = "/Users/content_nextplayer/community";
            } else {
                throw new IllegalArgumentException("Invalid type: " + type);
            }

            String fileName = file.filename();
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            String saveFilename = UUID.randomUUID().toString() + "." + ext;
            String path = sdf.format(new Date()).replaceAll("-", "/");
            String savePath = uploadDir + "/" + path + "/"  + saveFilename;

            if(ALLOW_EXT.contains(ext)) {
                FileDto fileDto = new FileDto();
                fileDto.setFileName(fileName);
                fileDto.setFileSaveName(saveFilename);
                fileDto.setFileSavePath(savePath);
                fileDto.setFileExt(ext.replace(".", ""));

                try {
                    // 파일을 저장하고 싶은 경로로 변경해야 합니다.
                    Path targetDirectory = Path.of(uploadDir + "/" + path);
                    Files.createDirectories(targetDirectory); // 폴더가 없는 경우 폴더를 생성합니다.
                    Path targetPath = targetDirectory.resolve(saveFilename);
                    file.transferTo(targetPath).block(); // 파일을 해당 경로로 이동합니다.
                    System.out.println("Uploaded file: " + targetPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                list.add(fileDto);

            } else {
                try {
                    throw new CommonLogicException(ApiState.LOGIC.getCode(), "msg91_system_messageSend");
                } catch (CommonLogicException e) {
                    throw new RuntimeException(e);
                }
                //throw new CommonLogicException(ApiState.LOGIC.getCode(), "msg91_system_messageSend");
            }

            /*if (!ALLOW_EXT.contains(ext)) {
                try {
                    throw new Exception("허용되지 않는 확장자");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }*/

        });

        return list;
    }

    public Flux<FileDto> uploadFile3(Flux<FilePart> files, String type) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        List<FileDto> fileList = new ArrayList<>();
        return files.flatMap(file -> {
            String uploadDir = "";

            if (type.equals("Community")) {
                uploadDir = "/Users/content_nextplayer/community";
            } else {
                throw new IllegalArgumentException("Invalid type: " + type);
            }

            String fileName = file.filename();
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            String saveFilename = UUID.randomUUID().toString() + "." + ext;
            String path = sdf.format(new Date()).replaceAll("-", "/");
            String savePath = uploadDir + "/" + path + "/" + saveFilename;

            if (!ALLOW_EXT.contains(ext.toUpperCase())) {
                throw new IllegalArgumentException("Invalid file extension: " + ext);
            }

            File saveFile = Path.of(uploadDir, path, saveFilename).toFile();
            Mono<Void> transferTo = file.transferTo(saveFile);

            Path directory = Paths.get(uploadDir + "/" + path);
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Path targetPath = directory.resolve(saveFilename);

            return transferTo.then(Mono.fromRunnable(() -> {
                File savedFile = targetPath.toFile();

                FileDto fileDto = new FileDto();
                fileDto.setFileName(fileName);
                fileDto.setFileSaveName(saveFilename);
                fileDto.setFileSavePath(savePath);
                fileDto.setFileExt(ext);
                fileDto.setFileSize(Long.toString(savedFile.length()));

                synchronized (fileList) {
                    fileList.add(fileDto);
                }
            }));

        }).thenMany(Flux.fromIterable(fileList));
    }

    public List<FileDto> uploadFile2(List<FilePart> files, String type) throws Exception {
    //public Flux<FileDto> uploadFile2(Flux<FilePart> files, String type) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        List<FileDto> fileList = new ArrayList<>();

        for (FilePart file : files) {
            System.out.println("file.filename() = " + file.filename());

            String uploadDir = "";

            if (type.equals("Community")) {
                uploadDir = "/Users/content_nextplayer/community";
            } else {
                throw new IllegalArgumentException("Invalid type: " + type);
            }

            String fileName = file.filename();
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            String saveFilename = UUID.randomUUID().toString() + "." + ext;
            String path = sdf.format(new Date()).replaceAll("-", "/");
            String savePath = uploadDir + "/" + path + "/" + saveFilename;

            if (!ALLOW_EXT.contains(ext.toUpperCase())) {
                throw new IllegalArgumentException("Invalid file extension: " + ext);
            }

            Path directory = Paths.get(uploadDir + "/" + path);
            Files.createDirectories(directory);

            Path targetPath = directory.resolve(saveFilename);



            //file.transferTo(directory.resolve(saveFilename));
            File saveFile = Path.of(uploadDir, path, saveFilename).toFile();
            Mono<Void> transferTo = file.transferTo(saveFile);

            File savedFile = targetPath.toFile();

            FileDto fileDto = new FileDto();
            fileDto.setFileName(fileName);
            fileDto.setFileSaveName(saveFilename);
            fileDto.setFileSavePath(savePath);
            fileDto.setFileExt(ext);
            fileDto.setFileSize(Long.toString(savedFile.length()));

            fileList.add(fileDto);

        }
        
        return fileList;
    }

    public Flux<FileDto> uploadFileSaveDist(Flux<FilePart> files) {
        return files.flatMap(file -> {
            if (file instanceof FilePart) {

            }
            return Mono.empty();
        });
    }

    public Flux<MediaFile> uploadFiles(RequestMediaFileDto request) {
        return this.uploadFiles(request.getMemberCd(), "/Users/content_nextplayer/community", request.getFiles());
    }

    public Flux<MediaFile> uploadFiles(String memberCd, String filePath, Flux<FilePart> files) {
        return uploadFileSaveDisk(memberCd, filePath, files)
            .flatMap(mediaFile -> {
                String fileType = mediaFile.getType();
                File savedFile = Path.of(filePath, mediaFile.getPath(), mediaFile.getName()).toFile();

                log.info("savedFile : {}", savedFile.getAbsolutePath());

                mediaFile.setState(SystemSettingCode.FILE_STATE_SAVED.getCode());

                Image image = null;
                try {
                    image = ImageIO.read(savedFile);
                } catch (IOException e) {
                    log.error("error", e);
                }

                if (image != null) {
                    mediaFile.setWidth(image.getWidth(null));
                    mediaFile.setHeight(image.getHeight(null));
                }

                mediaFile.setSize(savedFile.length());

                return Mono.just(mediaFile);
            });
    }

    private Flux<MediaFile> uploadFileSaveDisk(String memberCd, String filePath, Flux<FilePart> files) {
        return files.flatMap(part -> {
            if (part instanceof FilePart) {
                FilePart filePart = (FilePart) part;
                if (filePart.filename().isEmpty()) {
                    return Mono.empty();
                }
                MediaFile mediaFile = initMediaFile(memberCd, filePart, filePath);
                File savedFile = Path.of(filePath, mediaFile.getPath(), mediaFile.getName()).toFile();
                Mono<Void> transferTo = filePart.transferTo(savedFile);
                return transferTo.then(Mono.just(mediaFile)).doOnSuccess(s -> {
                    log.info("uploadFileSaveDisk : {}", savedFile);
                    savedFile.setExecutable(true, false);
                    savedFile.setReadable(true, false);
                    savedFile.setWritable(true, false);
                });
            }
            return Mono.empty();
        });
    }

    private MediaFile initMediaFile(String memberCd, FilePart filePart, String subPath) {
        String saveFileName = getNewFileName(filePart.filename());
        String fileSavePath = createDirectory(memberCd, filePart, subPath);
        String contentType = filePart.headers().getContentType().toString();

        log.info("fileSavePath : {}", fileSavePath);

        MediaFile mediaFile = MediaFile.builder()
            .id(1L)
            .parentId(0L)
            .originalName(filePart.filename())
            .name(saveFileName)
            .path(File.separator + fileSavePath)
            .subPath(subPath)
            .state(SystemSettingCode.FILE_STATE_TEMP.getCode())
            .contentType(contentType)
            .isResizeFile(false)
            .size(filePart.headers().getContentLength())
            .memberCd(memberCd)
            .build();
        return mediaFile;
    }

    private String createDirectory(String memberCd, FilePart filePart, String subPath) {
        String fileSavePath = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String yearMonth = sdf.format(new Date()).replaceAll("-", "/");

            String fileType = getFileType(filePart).toLowerCase();
            fileSavePath = Path.of(memberCd, fileType, subPath, yearMonth).toString();

            Path directory = Paths.get(subPath + "/" + yearMonth);
            Files.createDirectories(directory);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileSavePath;
    }

    private String getNewFileName(String fileName) {
        String uuid = UUID.randomUUID().toString();
        String fileExtention = FilenameUtils.getExtension(fileName);
        return uuid + "." + fileExtention;
    }

    private String getFileType(FilePart filePart) {
        log.info("getFileType : {}", filePart.filename());
        String contentType = filePart.headers().getContentType().toString();
        SystemSettingCode mediaFileCode = SystemSettingCode.FILE_ETC;
        if (contentType.contains("image")) {
            mediaFileCode = SystemSettingCode.FILE_IMAGE;
        } else if (contentType.contains("video")) {
            mediaFileCode = SystemSettingCode.FILE_VIDEO;
        } else if (contentType.contains("audio")) {
            mediaFileCode = SystemSettingCode.FILE_AUDIO;
        }
        return mediaFileCode.getCode();
    }

}
