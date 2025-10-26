package kr.co.nextplayer.base.media.service;

import kr.co.nextplayer.base.config.properties.MediaProperties;
import kr.co.nextplayer.base.media.dto.RequestMediaFileDto;
import kr.co.nextplayer.base.media.mapper.MediaFileMapper;
import kr.co.nextplayer.base.media.model.MediaFile;
import kr.co.nextplayer.next.lib.common.constants.SystemSettingCode;
import kr.co.nextplayer.next.lib.common.util.LocalDateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class MediaFileService {

    private final MediaProperties mediaProperties;
    private final MediaFileMapper mediaFileMapper;

    /**
     * 파일 삭제
     *
     * @param id
     * @return
     */
    public Mono<Long> delete(Long id) {
        long result = mediaFileMapper.deleteById(id);
        return Mono.just(result);
    }

    /**
     * 파일 삭제
     *
     * @param id
     * @return
     */
    public Mono<Long> deleteWithFile(Long id) {
        MediaFile mediaFile = mediaFileMapper.findById(id);
        if (mediaFile == null) {
            return Mono.empty();
        }
        File savedFile = Path.of(mediaProperties.getStoragePath(), mediaFile.getPath(), mediaFile.getName()).toFile();
        if (savedFile.exists()) {
            savedFile.delete();
        }
        long result = mediaFileMapper.deleteById(id);
        return Mono.just(result);
    }

    /**
     * 파일 수정
     *
     * @param mediaFile
     * @return
     */
    public Mono<Long> update(MediaFile mediaFile) {
        long result = mediaFileMapper.update(mediaFile);
        return Mono.just(result);
    }

    /**
     * 파일 저장
     *
     * @param mediaFile
     * @return
     */
    public Mono<Long> save(MediaFile mediaFile) {
        long result = -1;
        if (mediaFile.getId() == null) {
            result = mediaFileMapper.insert(mediaFile);
        } else {
            result = mediaFileMapper.update(mediaFile);
        }
        return Mono.just(result);
    }

    /**
     * 파일 정보
     *
     * @param id
     * @return
     */
    public Mono<MediaFile> findById(Long id) {
        MediaFile mediaFile = mediaFileMapper.findById(id);
        if (mediaFile != null) {
            return Mono.just(mediaFile);
        } else {
            return Mono.empty();
        }
    }

    /**
     * 파일 정보
     *
     * @param parentId
     * @param size
     * @return
     */
    public Mono<MediaFile> findByParentIdAndWidth(Long parentId, int size) {
        MediaFile mediaFile = mediaFileMapper.findByParentIdAndWidth(parentId, size);
        if (mediaFile != null) {
            return Mono.just(mediaFile);
        } else {
            return Mono.empty();
        }
    }

    /**
     * 신규 파일명 생성
     *
     * @param fileName
     * @return
     */
    private String getNewFileName(String fileName) {
        String uuid = UUID.randomUUID().toString();
        String fileExtention = FilenameUtils.getExtension(fileName);
        return uuid + "." + fileExtention;
    }

    /**
     * 파일 유형
     *
     * @param filePart
     * @return
     */
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

    /**
     * 파일 저장 경로 생성
     *
     * @param filePart
     * @param subPath
     * @return
     */
    private String createDirectory(String memberCd, FilePart filePart, String subPath) {

        String yearMonth = LocalDateUtil.formatUTC_format(LocalDateUtil.getLocalDateByZone(),"yyyyMM");
        String fileType = getFileType(filePart).toLowerCase();
        //String fileSavePath = Path.of(rootPath, fileType, subPath, yearMonth).toString();
        String fileSavePath = Path.of(fileType, subPath, yearMonth).toString();
        if (!new File(mediaProperties.getStoragePath(), fileSavePath).exists()) {
            new File(mediaProperties.getStoragePath(), fileSavePath).mkdirs();
        }
        return fileSavePath;
    }

    /**
     * 미디어 파일 객체 생성
     *
     * @param filePart
     * @param subPath
     * @return
     */
    private MediaFile initMediaFile(String memberCd, FilePart filePart, String subPath, String foreignId, String foreignType) {
        String saveFileName = getNewFileName(filePart.filename());
        String fileType = getFileType(filePart);
        String fileSavePath = createDirectory(memberCd, filePart, subPath);
        String contentType = filePart.headers().getContentType().toString();

        log.info("fileSavePath : {}", fileSavePath);
        log.info("fileType : {}", fileType);

        Long foreignIdParam = !StringUtils.isEmpty(foreignId) ? Long.parseLong(foreignId) : null;
        String foreignTypeParam = !StringUtils.isEmpty(foreignType) ? foreignType : null;

        String fileOrgName = "";

        if (Normalizer.isNormalized(filePart.filename(), Normalizer.Form.NFD)) {
            fileOrgName = Normalizer.normalize(filePart.filename(), Normalizer.Form.NFC);
        } else {
            fileOrgName = filePart.filename();
        }

        MediaFile mediaFile = MediaFile.builder()
            .id(1L)
            .parentId(0L)
            .originalName(fileOrgName)
            .name(saveFileName)
            .type(fileType)
            .path(File.separator + fileSavePath)
            .subPath(subPath)
            .state(SystemSettingCode.FILE_STATE_TEMP.getCode())
            .contentType(contentType)
            .isResizeFile(false)
            .size(filePart.headers().getContentLength())
            .memberCd(memberCd)
            .foreignId(foreignIdParam)
            .foreignType(foreignTypeParam)
            .build();
        return mediaFile;
    }

    /**
     * 파일 업로드
     *
     * @param memberCd
     * @param filePath
     * @param files
     * @return
     */
    public Flux<MediaFile> uploadFiles(String memberCd, String filePath, Flux<Part> files) {
        return this.uploadFiles(memberCd, filePath, files, false, "width", null, null);
    }

    /**
     * 파일 업로드
     *
     * @param request
     * @return
     */
    public Flux<MediaFile> uploadFiles(RequestMediaFileDto request) {
        return this.uploadFiles(request.getMemberCd(), request.getSubPath(), request.getFiles(), request.getIsResize(), request.getResizeType(), request.getForeignId(), request.getForeignType());
    }

    /**
     * 파일 업로드
     *
     * @param memberCd
     * @param filePath
     * @param files
     * @param isResize
     * @param resizeType
     * @return
     */
    public Flux<MediaFile> uploadFiles(String memberCd, String filePath, Flux<Part> files, boolean isResize, String resizeType, String foreignId, String foreignType) {
        return uploadFileSaveDisk(memberCd, filePath, files, foreignId, foreignType)
            .flatMap(mediaFile -> {
                String fileType = mediaFile.getType();
                File savedFile = Path.of(mediaProperties.getStoragePath(), mediaFile.getPath(), mediaFile.getName()).toFile();

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
                mediaFileMapper.update(mediaFile);

                if (isResize) {
                    if (fileType == SystemSettingCode.FILE_IMAGE.getCode()) {
                        Integer[] fileSize = getImageSize(savedFile);
                        mediaFile.setWidth(fileSize[0]);
                        mediaFile.setHeight(fileSize[1]);

                        return Flux.fromIterable(Arrays.asList(mediaProperties.getImageResizes())).flatMap(resizeSize -> {
                            log.info("uploadFileResize image flux : {}", resizeSize);
                            MediaFile resizeMediaFile = new MediaFile();
                            BeanUtils.copyProperties(mediaFile, resizeMediaFile);
                            resizeMediaFile.setId(null);
                            resizeMediaFile.setParentId(mediaFile.getId());
                            resizeMediaFile.setIsResizeFile(true);
                            return resizeImage(resizeMediaFile, savedFile, resizeSize, resizeType);
                        }).flatMap(resizeMediaFile -> {
                            log.info("uploadFileResize resize image flux : {}", resizeMediaFile);
                            return Flux.just(resizeMediaFile);
                        }).concatWith(Flux.just(mediaFile));
                    } else if (fileType == SystemSettingCode.FILE_VIDEO.getCode()) {
                        Integer[] fileSize = getVideoSize(savedFile);
                        mediaFile.setWidth(fileSize[0]);
                        mediaFile.setHeight(fileSize[1]);

                        return Flux.fromIterable(Arrays.asList(mediaProperties.getVideoResizes())).flatMap(resizeSize -> {
                            log.info("uploadFileResize video flux : {}", resizeSize);
                            MediaFile resizeMediaFile = new MediaFile();
                            BeanUtils.copyProperties(mediaFile, resizeMediaFile);
                            resizeMediaFile.setId(null);
                            resizeMediaFile.setParentId(mediaFile.getId());
                            resizeMediaFile.setIsResizeFile(true);
                            return resizeVideo(resizeMediaFile, savedFile, resizeSize, resizeType);
                        }).flatMap(resizeMediaFile -> {
                            log.info("uploadFileResize resize image flux : {}", resizeMediaFile);
                            return Flux.just(resizeMediaFile);
                        }).concatWith(Flux.just(mediaFile));

                    }
                }
                return Mono.just(mediaFile);
            });
    }


    /**
     * 업로드 파일 디스크 저장
     *
     * @param filePath
     * @param files
     * @return
     */
    private Flux<MediaFile> uploadFileSaveDisk(String memberCd, String filePath, Flux<Part> files, String foreignId, String foreignType) {
        return files.flatMap(part -> {
            if (part instanceof FilePart) {
                FilePart filePart = (FilePart) part;
                if (filePart.filename().isEmpty()) {
                    return Mono.empty();
                }
                MediaFile mediaFile = initMediaFile(memberCd, filePart, filePath, foreignId, foreignType);
                mediaFile.setIsResizeFile(false);
                mediaFileMapper.insert(mediaFile);
                File savedFile = Path.of(mediaProperties.getStoragePath(), mediaFile.getPath(), mediaFile.getName()).toFile();
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

    /**
     * 이미지 리사이즈
     *
     * @param mediaFile
     * @param orgFile
     * @param resizeSize
     * @param resizeType
     * @return
     */
    private Flux<MediaFile> resizeImage(MediaFile mediaFile, File orgFile, int resizeSize, String resizeType) {
        try {
            Image image = ImageIO.read(orgFile);

            String fileExtention = FilenameUtils.getExtension(orgFile.getName());
            String filePath = orgFile.getParent();
            String newFileName = FilenameUtils.getBaseName(orgFile.getName()) + "_" + resizeSize + "." + fileExtention;

            // log.info("resizeSize : {}", resizeSize);
            // log.info("fileExtention : {}", fileExtention);
            // log.info("filePath : {}", filePath);

            int width = image.getWidth(null);
            int height = image.getHeight(null);

            resizeType = resizeType.toUpperCase();
            if (resizeType.equals("WIDTH")) {
                height = (int) Math.round(((double) resizeSize / (double) width) * height);
                width = resizeSize;
            } else if (resizeType.equals("HEIGHT")) {
                width = (int) Math.round(((double) resizeSize / (double) height) * width);
                height = resizeSize;
            }

            // 이미지 리사이즈
            // Image.SCALE_DEFAULT          : 기본 이미지 스케일링 알고리즘 사용
            // Image.SCALE_FAST             : 이미지 부드러움보다 속도 우선
            // Image.SCALE_REPLICATE        : ReplicateScaleFilter 클래스로 구체화 된 이미지 크기 조절 알고리즘
            // Image.SCALE_SMOOTH           : 속도보다 이미지 부드러움을 우선
            // Image.SCALE_AREA_AVERAGING   : 평균 알고리즘 사용
            Image resizeImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

            // 새 이미지 저장하기
            BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics newImageGraphics = newImage.getGraphics();
            newImageGraphics.drawImage(resizeImage, 0, 0, null);
            newImageGraphics.dispose();

            File savedFile = new File(filePath, newFileName);
            ImageIO.write(newImage, fileExtention, savedFile);

            mediaFile.setName(savedFile.getName());
            mediaFile.setSize(savedFile.length());
            mediaFile.setWidth(width);
            mediaFile.setHeight(height);
            mediaFileMapper.insert(mediaFile);

            return Flux.just(mediaFile);

        } catch (Exception e) {
            log.warn("이미지 리사이즈 실패 : {} / {}", orgFile.getName(), e.getMessage());
            log.error("error", e);
        }
        return Flux.empty();
    }

    /**
     * 비디오 리사이즈
     *
     * @param orgFile
     * @param resizeType
     * @return
     */
    private Flux<MediaFile> resizeVideo(MediaFile mediaFile, File orgFile, int resizeSize, String resizeType) {
        try {
            int width = 0;
            int height = 0;
            if (resizeSize == 1920) {
                width = 1920;
                height = 1080;
            } else if (resizeSize == 1280) {
                width = 1280;
                height = 720;
            } else if (resizeSize == 960) {
                width = 960;
                height = 540;
            } else if (resizeSize == 640) {
                width = 640;
                height = 360;
            } else if (resizeSize == 320) {
                width = 320;
                height = 180;
            }

            if (width == 0 || height == 0) {
                return Flux.empty();
            }

            resizeType = resizeType.toUpperCase();
            if (resizeType == SystemSettingCode.RESIZE_HEIGHT.getCode()) {
                int w = height;
                int h = width;
                width = w;
                height = h;
            }

            log.info("start ffmpeg resize");
            log.info("resizeType : {}, width: {}, height: {}", resizeType, width, height);

            String fileExtention = FilenameUtils.getExtension(orgFile.getName());
            String filePath = orgFile.getParent();
            String resizeFileName = FilenameUtils.getBaseName(orgFile.getName()) + "_" + resizeSize + "." + fileExtention;
            File savedFile = new File(filePath, resizeFileName);

            log.info("resizeFile : {}", savedFile.getAbsolutePath());

            FFmpeg ffmpeg = new FFmpeg(mediaProperties.getFfmpegPath());
            FFprobe ffprobe = new FFprobe(mediaProperties.getFfmpegPath());
            FFmpegBuilder builder = new FFmpegBuilder()
                .overrideOutputFiles(true) // 오버라이드 여부
                .setInput(orgFile.getAbsolutePath()) // 생성대상 파일
                .addOutput(savedFile.getAbsolutePath()) // 생성 파일의 Path
                .setFormat("mp4")
                .setVideoCodec("libx264") // 비디오 코덱
                .setVideoFrameRate(30, 1) // 비디오 프레임
                .setVideoResolution(width, height) // 비디오 해상도
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // x264 사용
                .addExtraArgs("-crf", "28") // 화질
                .addExtraArgs("-movflags", "use_metadata_tags") // 메타데이터 복사
                .done();
            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            executor.createJob(builder).run();

            mediaFile.setName(savedFile.getName());
            mediaFile.setSize(savedFile.length());
            mediaFile.setWidth(width);
            mediaFile.setHeight(height);
            mediaFileMapper.insert(mediaFile);

            return Flux.just(mediaFile);

        } catch (IOException e) {
            log.warn("비디오 리사이즈 실패 : {} / {}", orgFile.getName(), e.getMessage());
            log.error("error", e);
        }

        return Flux.empty();
    }

    /**
     * 이미지 파일 사이즈 추출
     *
     * @param file
     * @return
     */
    private Integer[] getImageSize(File file) {
        int width = 0;
        int height = 0;

        try {
            Image image = ImageIO.read(file);
            width = image.getWidth(null);
            height = image.getHeight(null);
        } catch (IOException e) {
            log.error("getImageSize error", e);
        }
        return new Integer[]{width, height};
    }

    /**
     * 비디오 파일 사이즈 추출
     *
     * @param file
     * @return
     */
    private Integer[] getVideoSize(File file) {
        int width = 0;
        int height = 0;
        try {
            FFprobe ffprobe = new FFprobe(mediaProperties.getFfprobePath());
            FFmpegProbeResult probeResult = ffprobe.probe(file.getAbsolutePath());
            for (FFmpegStream stream : probeResult.getStreams()) {
                if (width == 0 && width > 0) {
                    width = stream.width;
                }
                if (height == 0 && height > 0) {
                    height = stream.height;
                }
                if (width > 0 && height > 0) {
                    break;
                }
            }

        } catch (IOException e) {
            log.error("getVideoSize error", e);
        }
        return new Integer[]{width, height};
    }
}
