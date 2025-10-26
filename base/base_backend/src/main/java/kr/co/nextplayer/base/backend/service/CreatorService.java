package kr.co.nextplayer.base.backend.service;

import kr.co.nextplayer.base.backend.dto.AttchFileInfoDto;
import kr.co.nextplayer.base.backend.mapper.CreatorMapper;
import kr.co.nextplayer.base.backend.util.LocalDateUtil;
import kr.co.nextplayer.base.backend.util.StrUtil;
import org.apache.commons.io.FilenameUtils;
import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static kr.co.nextplayer.base.backend.util.StrUtil.getContentPath;

@Service
public class CreatorService {

    @Resource
    private CreatorMapper creatorMapper;

    @Resource
    private FileService fileService;

    public int selectCreatorListCnt(Map<String, String> params) throws  Exception {
        int result = 0;

        try {
            result = creatorMapper.selectCreatorListCnt(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<HashMap<String, Object>> selectCreatorList(Map<String, String> params) throws Exception{
        List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();

        try {
            result = creatorMapper.selectCreatorList(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public HashMap<String, Object> selectCreatorInfo(Map<String, String> params) throws  Exception {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {
            result = creatorMapper.selectCreatorInfo(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Transactional
    public int insertCreator(Map<String, String> params, MultipartHttpServletRequest request) throws Exception {
        int result = 0;

        try {

            result = creatorMapper.insertCreator(params);

            if (request.getMultiFileMap().size() > 0) {

                List<AttchFileInfoDto> imgFiles = new ArrayList<>();

                if (request.getMultiFileMap().get("profileImg") != null) {
                    AttchFileInfoDto profileFilePath = fileService.uploadImgFile(request, params.get("mediaType"), "profileImg", "Creator");
                    profileFilePath.setSubType("profileImg");
                    imgFiles.add(profileFilePath);
                }

                if (request.getMultiFileMap().get("bannerImg") != null) {
                    AttchFileInfoDto bannerFilePath = fileService.uploadImgFile(request, params.get("mediaType"), "bannerImg", "Creator");
                    bannerFilePath.setSubType("bannerImg");
                    imgFiles.add(bannerFilePath);
                }

                fileService.insertImgFileInfo(imgFiles, params);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Transactional
    public int updateCreator(Map<String, String> params, MultipartHttpServletRequest request) throws Exception {
        int result = 0;

        try {

            if (!StrUtil.isEmpty(params.get("delProfileFile"))) {
                fileService.deleteImgFileInfo(params.get("delProfileFile"));
            }
            if (!StrUtil.isEmpty(params.get("delBannerFile"))) {
                fileService.deleteImgFileInfo(params.get("delBannerFile"));
            }

            result = creatorMapper.updateCreator(params);

            if (request.getMultiFileMap().size() > 0) {

                List<AttchFileInfoDto> imgFiles = new ArrayList<>();
                System.out.println("request.getMultiFileMap = " + request.getMultiFileMap().get("bannerImg"));
                System.out.println("request.getMultiFileMap = " + request.getMultiFileMap().get("profileImg"));
                if (request.getMultiFileMap().get("profileImg") != null) {
                    AttchFileInfoDto profileFilePath = fileService.uploadImgFile(request, params.get("mediaType"), "profileImg", "Creator");
                    profileFilePath.setForeignId(params.get("creatorId"));
                    profileFilePath.setSubType("profileImg");

                    imgFiles.add(profileFilePath);
                }

                if (request.getMultiFileMap().get("bannerImg") != null) {
                    AttchFileInfoDto bannerFilePath = fileService.uploadImgFile(request, params.get("mediaType"), "bannerImg", "Creator");
                    bannerFilePath.setForeignId(params.get("creatorId"));
                    bannerFilePath.setSubType("bannerImg");


                    imgFiles.add(bannerFilePath);
                }

                fileService.insertImgFileInfo(imgFiles, params);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Transactional
    public int updateCreatorDelete(Map<String, String> params) throws Exception {
        int result = 0;

        try {
            result = creatorMapper.updateCreatorDelete(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public HashMap<String, Object> selectImgFileInfo(Map<String, String> params) {
        return creatorMapper.selectImgFileInfo(params);
    }

}
