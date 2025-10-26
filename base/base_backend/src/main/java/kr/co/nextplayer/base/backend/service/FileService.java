package kr.co.nextplayer.base.backend.service;

import kr.co.nextplayer.base.backend.dto.AttchFileInfoDto;
import kr.co.nextplayer.base.backend.dto.MediaFileInfoDto;
import kr.co.nextplayer.base.backend.mapper.FileMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.*;

import static kr.co.nextplayer.base.backend.util.StrUtil.getContentPath;

@Service
public class FileService {

    @Resource
    private FileMapper fileMapper;

    //String uploadDir = "/home/WAS/apps/manager.ehdb.kr_stable/webapps/EHDB_Manager/resources/uploadFile";
    String allowExt = "JPEG,BMP,PNG,JPG,GIF,XLS,XLSX,HWP,DOC,DOCX,PDF,ZIP";

    public List<AttchFileInfoDto> upload(MultipartHttpServletRequest req) throws IOException {
        return upload(req, "file");
    }

    public List<AttchFileInfoDto> upload(MultipartHttpServletRequest req, String paramName) throws IOException{
        return upload(req.getFiles(paramName));
    }

    public List<AttchFileInfoDto> upload(List<MultipartFile> mfileList) throws IOException{

        String uploadDir = getContentPath() + "reference/";

        boolean isUploadable = false;
        String ym = null, ext = null, fileName = null;
        String[] arrExt = allowExt.split(",");

        File file = null, dir = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        AttchFileInfoDto avo = new AttchFileInfoDto();
        List<AttchFileInfoDto> list = new ArrayList<AttchFileInfoDto>();

        ym = sdf.format(new Date());
        dir = new File(uploadDir, ym.replaceAll("-", "/"));

        if(!dir.exists()) {
            dir.mkdirs();
        }

        if(mfileList != null){
            for(MultipartFile mfile : mfileList) {
                avo = new AttchFileInfoDto();
                fileName = mfile.getOriginalFilename();

                if (Normalizer.isNormalized(fileName, Normalizer.Form.NFD)) {
                    fileName = Normalizer.normalize(fileName, Normalizer.Form.NFC);
                }

                if(fileName.lastIndexOf(".") < 0)
                    throw new IOException("확장자 없음");

                ext = fileName.substring(fileName.lastIndexOf(".")+1);

                for(String tmpExt : arrExt){
                    if(tmpExt.equals(ext.toUpperCase())){
                        isUploadable = true;
                        break;
                    }
                }

                if(!isUploadable)
                    throw new IOException("허용되지 않는 확장자");

                file = new File(dir, UUID.randomUUID().toString().concat(".").concat(ext));

                avo.setFileName(fileName);
                avo.setFileSize(mfile.getSize());
                avo.setFileSaveName(file.getName());
                avo.setFileSavePath(file.getAbsolutePath());
                avo.setFileExt(ext);

                mfile.transferTo(file);
                list.add(avo);
            }
        }

        return list;
    }

    public List<AttchFileInfoDto> upload2(Map<String, String> params, List<MultipartFile> mfileList, String fileType) throws IOException{


        String uploadDir = getContentPath() + params.get("foreignType") + "/";

        if (fileType.equals("imgFile")) {
            uploadDir += "image/";
        }

        boolean isUploadable = false;
        String ym = null, ext = null, fileName = null;
        String[] arrExt = allowExt.split(",");

        File file = null, dir = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        AttchFileInfoDto avo = new AttchFileInfoDto();
        List<AttchFileInfoDto> list = new ArrayList<AttchFileInfoDto>();

        ym = sdf.format(new Date());
        dir = new File(uploadDir, ym.replaceAll("-", "/"));

        if(!dir.exists()) {
            dir.mkdirs();
        }

        if(mfileList != null){
            for(MultipartFile mfile : mfileList) {
                avo = new AttchFileInfoDto();
                fileName = mfile.getOriginalFilename();

                if (Normalizer.isNormalized(fileName, Normalizer.Form.NFD)) {
                    fileName = Normalizer.normalize(fileName, Normalizer.Form.NFC);
                }

                if(fileName.lastIndexOf(".") < 0)
                    throw new IOException("확장자 없음");

                ext = fileName.substring(fileName.lastIndexOf(".")+1);

                for(String tmpExt : arrExt){
                    if(tmpExt.equals(ext.toUpperCase())){
                        isUploadable = true;
                        break;
                    }
                }

                if(!isUploadable)
                    throw new IOException("허용되지 않는 확장자");

                file = new File(dir, UUID.randomUUID().toString().concat(".").concat(ext));

                avo.setFileName(fileName);
                avo.setFileSize(mfile.getSize());
                avo.setFileSaveName(file.getName());
                avo.setFileSavePath(file.getAbsolutePath());
                avo.setFileExt(ext);

                if (fileType.equals("imgFile")) {
                    avo.setSubType("imgFile");
                }

                mfile.transferTo(file);
                list.add(avo);
            }
        }

        return list;
    }

    public AttchFileInfoDto upload3(Map<String, String> params, MultipartFile mfile) throws IOException{


        String uploadDir = getContentPath() + params.get("foreignType") + "/";

        boolean isUploadable = false;
        String ym = null, ext = null, fileName = null;
        String[] arrExt = allowExt.split(",");

        File file = null, dir = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        AttchFileInfoDto avo = new AttchFileInfoDto();
        List<AttchFileInfoDto> list = new ArrayList<AttchFileInfoDto>();

        ym = sdf.format(new Date());
        dir = new File(uploadDir, ym.replaceAll("-", "/"));

        if(!dir.exists()) {
            dir.mkdirs();
        }

        if(mfile != null){
            avo = new AttchFileInfoDto();
            fileName = mfile.getOriginalFilename();

            if (Normalizer.isNormalized(fileName, Normalizer.Form.NFD)) {
                fileName = Normalizer.normalize(fileName, Normalizer.Form.NFC);
            }

            if(fileName.lastIndexOf(".") < 0)
                throw new IOException("확장자 없음");

            ext = fileName.substring(fileName.lastIndexOf(".")+1);

            for(String tmpExt : arrExt){
                if(tmpExt.equals(ext.toUpperCase())){
                    isUploadable = true;
                    break;
                }
            }

            if(!isUploadable)
                throw new IOException("허용되지 않는 확장자");

            file = new File(dir, UUID.randomUUID().toString().concat(".").concat(ext));

            avo.setFileName(fileName);
            avo.setFileSize(mfile.getSize());
            avo.setFileSaveName(file.getName());
            avo.setFileSavePath(file.getAbsolutePath());
            avo.setFileExt(ext);

            mfile.transferTo(file);
            list.add(avo);
        }

        return avo;
    }

    @Transactional
    public int insertSummernoteFileInfo(AttchFileInfoDto file, Map<String, String> params) {
        int result = 0;
        if (file != null) {
            result = fileMapper.insertSummernoteFileInfo(file);
        }
        return result;
    }

    @Transactional
    public void insertAttchFileInfo(List<AttchFileInfoDto> files, Map<String, String> params) {
        if (files != null) {
            for (AttchFileInfoDto eo: files) {
                eo.setForeignId(params.get("referenceId"));
                eo.setForeignType(params.get("foreignType"));
                fileMapper.insertFileInfo(eo);
            }
        }
    }

    public void insertNoticeAttchFileInfo(List<AttchFileInfoDto> files, Map<String, String> params) {
        if (files != null) {
            for (AttchFileInfoDto eo: files) {
                eo.setForeignId(params.get("boardId"));
                eo.setForeignType(params.get("foreignType"));
                fileMapper.insertFileInfo(eo);
            }
        }
    }

    @Transactional
    public void insertPlayerAttchFileInfo(List<AttchFileInfoDto> files, Map<String, String> params) {
        if (files != null) {
            for (AttchFileInfoDto eo: files) {
                eo.setForeignId(params.get("rosterId"));
                eo.setForeignType(params.get("foreignType"));
                fileMapper.insertFileInfo(eo);
            }
        }
    }

    @Transactional
    public void insertAnswerAttchFileInfo(List<AttchFileInfoDto> files, Map<String, String> params) {

        if (params.get("foreignType").equals("Request")) {
            if (files != null) {
                for (AttchFileInfoDto eo: files) {
                    eo.setForeignId(params.get("requestId"));
                    eo.setForeignType(params.get("foreignType"));
                    fileMapper.insertFileInfo(eo);
                }
            }
        }

        if (params.get("foreignType").equals("Board")) {
            if (files != null) {
                for (AttchFileInfoDto eo: files) {
                    eo.setForeignId(params.get("boardId"));
                    eo.setForeignType(params.get("foreignType"));
                    fileMapper.insertFileInfo(eo);
                }
            }
        }

        if (params.get("foreignType").equals("Suggest")) {
            if (files != null) {
                for (AttchFileInfoDto eo: files) {
                    eo.setForeignId(params.get("suggestId"));
                    eo.setForeignType(params.get("foreignType"));
                    fileMapper.insertFileInfo(eo);
                }
            }
        }

    }

    @Transactional
    public void insertEducationAttchFileInfo(List<AttchFileInfoDto> files, Map<String, String> params) {
        if (files != null) {
            for (AttchFileInfoDto eo: files) {
                eo.setForeignId(params.get("educationId"));
                eo.setForeignType(params.get("foreignType"));
                fileMapper.insertFileInfo(eo);
            }
        }
    }

    @Transactional
    public void insertColumnAttchFileInfo(List<AttchFileInfoDto> files, Map<String, String> params) {
        if (files != null) {
            for (AttchFileInfoDto eo: files) {
                eo.setForeignId(params.get("educationColumnId"));
                eo.setForeignType(params.get("foreignType"));
                fileMapper.insertFileInfo(eo);
            }
        }
    }

    @Transactional
    public void insertEducationFileAttchFileInfo(List<AttchFileInfoDto> files, Map<String, String> params) {
        if (files != null) {
            for (AttchFileInfoDto eo: files) {
                eo.setForeignId(params.get("educationFileId"));
                eo.setForeignType(params.get("foreignType"));
                fileMapper.insertFileInfo(eo);
            }
        }
    }

    @Transactional
    public void deleteAttchFileInfo(List<AttchFileInfoDto> files, Map<String, String> params) {

        if (params.get("delCnt") != null) {
            int delCnt = Integer.parseInt(params.get("delCnt"));

            if (delCnt > 0) {
                for (int i = 0; i < delCnt; i++) {
                    AttchFileInfoDto attchFileInfoDto = new AttchFileInfoDto();
                    attchFileInfoDto.setPublicFileId(Integer.parseInt(params.get("delFile" + i)));
                    fileMapper.deleteFileInfo(attchFileInfoDto);
                }
            }
        }

        if (params.get("imgDelCnt") != null) {
            int imgDelCnt = Integer.parseInt(params.get("imgDelCnt"));

            if (imgDelCnt > 0) {
                for (int i = 0; i < imgDelCnt; i++) {
                    AttchFileInfoDto attchFileInfoDto = new AttchFileInfoDto();
                    attchFileInfoDto.setPublicFileId(Integer.parseInt(params.get("imgDelFile" + i)));
                    fileMapper.deleteFileInfo(attchFileInfoDto);
                }
            }
        }

        //insertAttchFileInfo(files, params);

    }

    @Transactional
    public void insertBannerAttchFileInfo(List<AttchFileInfoDto> files, Map<String, String> params) {
        if (files != null) {
            for (AttchFileInfoDto eo: files) {
                eo.setSubType(params.get("subType"));
                eo.setForeignId(params.get("bannerId"));
                eo.setForeignType(params.get("foreignType"));
                fileMapper.insertFileInfo(eo);
            }
        }
    }

    @Transactional
    public void insertPopupAttchFileInfo(List<AttchFileInfoDto> files, Map<String, String> params) {
        if (files != null) {
            for (AttchFileInfoDto eo: files) {
                eo.setForeignId(params.get("popupId"));
                eo.setForeignType(params.get("foreignType"));
                fileMapper.insertFileInfo(eo);
            }
        }
    }

    public List<AttchFileInfoDto> selectFileInfoList(AttchFileInfoDto attchFileInfoDto) {
        return fileMapper.selectFileInfoList(attchFileInfoDto);
    }

    public void download(HttpServletResponse res, AttchFileInfoDto vo)  throws IOException, SQLException {
        int read = 0;
        byte b[] = new byte[1024*100];

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        FileInputStream fis = null;

        AttchFileInfoDto eo = fileMapper.selectFileInfoView(vo);
        File file = new File(eo.getFileSavePath());
        if(file.exists()){
            if("jpeg|bmp|png|jpg|gif".indexOf(eo.getFileExt().toLowerCase()) >= 0) {
                res.setContentType("image/".concat(eo.getFileExt().toLowerCase().replace("jpg", "jpeg")));
            }

            res.setHeader("Content-Disposition","attachment;filename=".concat(URLEncoder.encode(eo.getFileName(), "UTF-8")).concat(";"));
            res.setContentLength((int)file.length());
            fis = new FileInputStream(file);

            try {
                bis = new BufferedInputStream(fis);
                bos = new BufferedOutputStream(res.getOutputStream());

                while((read = bis.read(b)) != -1)
                    bos.write(b, 0, read);

                bos.flush();

            }finally {
                if(bis != null) bis.close();
                if(bos != null) bos.close();
                if(fis != null) fis.close();

            }

        }else{
            throw new FileNotFoundException("파일을 찾을수 없습니다(".concat(file.getAbsolutePath()).concat(")"));

        }

    }

    @Transactional
    public AttchFileInfoDto uploadImgFile(MultipartHttpServletRequest request, String mediaType, String imgType, String foreignType) throws Exception {

        String[] arrExt = "JPEG,PNG,JPG".split(",");
        boolean isUploadable = false;

        String rootPath = getContentPath();

        String uploadDir = "";

        if (foreignType.equals("Creator")) {
            uploadDir = rootPath + foreignType + "/" + mediaType + "/" + imgType + "/";
        } else {
            uploadDir = rootPath + foreignType + "/";
        }


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        String ym = sdf.format(new Date());
        File dir = new File(uploadDir, ym.replaceAll("-", "/"));

        if(!dir.exists()) {
            dir.mkdirs();
        }

        AttchFileInfoDto avo = new AttchFileInfoDto();
        String fileName = request.getFile(imgType).getOriginalFilename();

        if(fileName.lastIndexOf(".") < 0){
            throw new IOException("확장자 없음");
        }

        String ext = fileName.substring(fileName.lastIndexOf(".")+1);

        for(String tmpExt : arrExt){
            if(tmpExt.equals(ext.toUpperCase())){
                isUploadable = true;
            }
        }

        if(!isUploadable) {
            throw new IOException("허용되지 않는 확장자");
        }

        File file = new File(dir, UUID.randomUUID().toString().concat(".").concat(ext));

        avo.setFileName(fileName);
        avo.setFileSize(request.getFile(imgType).getSize());
        avo.setFileSaveName(file.getName());
        avo.setFileSavePath(file.getAbsolutePath());
        avo.setFileExt(ext);

        request.getFile(imgType).transferTo(file);

        return avo;
    }

    @Transactional
    public void insertImgFileInfo(List<AttchFileInfoDto> files, Map<String, String> params) {
        if (files != null) {
            for (AttchFileInfoDto eo: files) {
                eo.setForeignId(params.get("creatorId"));
                eo.setForeignType(params.get("foreignType"));
                fileMapper.insertFileInfo(eo);
            }
        }
    }

    @Transactional
    public void deleteImgFileInfo(String params) {

        AttchFileInfoDto attchFileInfoDto = new AttchFileInfoDto();
        attchFileInfoDto.setPublicFileId(Integer.parseInt(params));
        fileMapper.deleteFileInfo(attchFileInfoDto);

        //insertAttchFileInfo(files, params);

    }

    @Transactional
    public void deleteImgFile(AttchFileInfoDto attchFileInfoDto) {
        fileMapper.deleteFile(attchFileInfoDto);
    }

    public void download2(HttpServletResponse res, MediaFileInfoDto vo)  throws IOException, SQLException {
        int read = 0;
        byte b[] = new byte[1024*100];

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        FileInputStream fis = null;

        MediaFileInfoDto eo = fileMapper.selectMediaFileInfoView(vo);
        File file = new File(eo.getFileSavePath());
        if(file.exists()){
            if("jpeg|bmp|png|jpg|gif".indexOf(eo.getFileExt().toLowerCase()) >= 0) {
                res.setContentType("image/".concat(eo.getFileExt().toLowerCase().replace("jpg", "jpeg")));
            }

            res.setHeader("Content-Disposition","attachment;filename=".concat(URLEncoder.encode(eo.getOriginalName(), "UTF-8")).concat(";"));
            res.setContentLength((int)file.length());
            fis = new FileInputStream(file);

            try {
                bis = new BufferedInputStream(fis);
                bos = new BufferedOutputStream(res.getOutputStream());

                while((read = bis.read(b)) != -1)
                    bos.write(b, 0, read);

                bos.flush();

            }finally {
                if(bis != null) bis.close();
                if(bos != null) bos.close();
                if(fis != null) fis.close();

            }

        }else{
            throw new FileNotFoundException("파일을 찾을수 없습니다(".concat(file.getAbsolutePath()).concat(")"));

        }

    }

}
