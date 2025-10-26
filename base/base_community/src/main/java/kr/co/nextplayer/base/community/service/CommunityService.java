package kr.co.nextplayer.base.community.service;

import kr.co.nextplayer.base.community.dto.*;
import kr.co.nextplayer.base.community.mapper.CommunityMapper;
import kr.co.nextplayer.base.community.model.Community;
import kr.co.nextplayer.base.community.response.CommunityDetailResponse;
import kr.co.nextplayer.base.community.response.CommunityListResponse;
import kr.co.nextplayer.base.likes.dto.LikesReqDto;
import kr.co.nextplayer.base.likes.mapper.LikesMapper;
import kr.co.nextplayer.base.media.mapper.MediaFileMapper;
import kr.co.nextplayer.base.media.model.MediaFile;
import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class CommunityService {

    @Resource
    private CommunityMapper communityMapper;

    @Resource
    private MediaFileMapper mediaFileMapper;

    @Resource
    private LikesMapper likesMapper;

    public int saveCommunity(CommunityReqDto communityReqDto) {

        int result = 0;

        try {

            result = communityMapper.insertCommunity(communityReqDto);

            if (communityReqDto.getFileId() != null && communityReqDto.getFileId().size() > 0) {
                for (String file: communityReqDto.getFileId()) {
                    System.out.println("communityReqDto.getFileId() = " + file);
                    MediaFile mediaFile = MediaFile.builder()
                        .foreignId(Long.valueOf(communityReqDto.getForeignId()))
                        .foreignType(communityReqDto.getForeignType())
                        .id(Long.valueOf(file))
                        .build();
                    mediaFileMapper.updateSaveFile(mediaFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public CommunityListResponse getCommunityList(CommunityListReqDto communityListReqDto) {

        int totalCount = 0;
        List<CommunityListDto> list = new ArrayList<CommunityListDto>();

        try {

            totalCount = communityMapper.selectCommunityListCount(communityListReqDto);

            int curPage = communityListReqDto.getCurPage();

            if(curPage <= 0) {
                curPage = 1;
            }

            int cp = curPage;
            int cpp = 10;
            int tp = 1;

            if (totalCount > 0) {
                tp = (int) totalCount / cpp;
                if ((totalCount % cpp) > 0) {
                    tp += 1;
                }
            }

            int sRow = (cp - 1) * cpp;

            communityListReqDto.setSRow(sRow);
            communityListReqDto.setECount(cpp);

            List<Community> communityList = communityMapper.selectCommunityList(communityListReqDto);

            if (communityList.size() > 0) {
                for (Community community: communityList) {
                    CommunityListDto listDto = CommunityListDto.builder()
                        .community_id(community.getCommunityId())
                        .member_cd(community.getMemberCd())
                        .title(community.getTitle())
                        .content(community.getContent())
                        .type(community.getType())
                        .sub_type(community.getSubType())
                        .reg_date(community.getRegDate())
                        .reply_cnt(community.getReplyCnt())
                        .likes_cnt(community.getLikesCnt())
                        .view_cnt(community.getViewCnt())
                        .build();

                    list.add(listDto);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        CommunityListResponse communityListResponse = CommunityListResponse.builder()
            .totalCount(totalCount)
            .communityList(list)
            .build();

        return communityListResponse;
    }

    public CommunityDetailResponse getCommunityDetail(CommunityDetailReqDto communityDetailReqDto) {

        Community community = Community.builder().build();
        CommunityDetailDto communityDetailDto = CommunityDetailDto.builder().build();

        LikesReqDto likesReqDto = LikesReqDto.builder()
            .foreignId(communityDetailReqDto.getCommunityId())
            .foreignType("Community")
            .memberCd(communityDetailReqDto.getMemberCd())
            .build();

        boolean isLiked = true;

        try {
            community = communityMapper.selectCommunityDetail(communityDetailReqDto);

            isLiked = likesMapper.selectLikedContent(likesReqDto);

            if (community != null) {
                communityDetailDto = CommunityDetailDto.builder()
                    .community_id(community.getCommunityId())
                    .member_cd(community.getMemberCd())
                    .title(community.getTitle())
                    .content(community.getContent())
                    .type(community.getType())
                    .sub_type(community.getSubType())
                    .likes_cnt(community.getLikesCnt())
                    .reg_date(community.getRegDate())
                    .files(community.getFileId())
                    .isLiked(isLiked)
                    .build();
            }

            HashMap<String, Object> params = new HashMap<String, Object>();
            int viewCnt = Integer.parseInt(community.getViewCnt());
            viewCnt++;
            params.put("viewCnt", String.valueOf(viewCnt));
            params.put("communityId", community.getCommunityId());


            communityMapper.updateCommunityViewCnt(params);


        } catch (Exception e) {
            e.printStackTrace();
        }

        CommunityDetailResponse communityDetailResponse = CommunityDetailResponse.builder()
            .communityDetail(communityDetailDto)
            .build();

        return communityDetailResponse;
    }

    public int deleteCommunity(CommunityUpdateReqDto communityUpdateReqDto) throws CommonLogicException {
        CommunityDetailReqDto communityDetailDto = CommunityDetailReqDto.builder().communityId(communityUpdateReqDto.getCommunityId()).build();
        Community community = communityMapper.selectCommunityDetail(communityDetailDto);

        if (community == null) {
            throw new CommonLogicException(ApiState.LOGIC.getCode(), "msg27_common_dataNotExist");
        }

        int deleteResult = communityMapper.deleteCommunity(communityUpdateReqDto);

        List<String> files = mediaFileMapper.selectFindMediaFileList(communityUpdateReqDto.getCommunityId());

        if (files.size() > 0) {
            for (int i = 0; i < files.size(); i++) {
                mediaFileMapper.deleteById(Long.valueOf(files.get(i)));
            }
        }

        return deleteResult;
    }

    public int updateCommunity(CommunityUpdateReqDto communityUpdateReqDto) throws CommonLogicException {
        CommunityDetailReqDto communityDetailDto = CommunityDetailReqDto.builder()
            .communityId(communityUpdateReqDto.getCommunityId())
            .build();
        Community community = communityMapper.selectCommunityDetail(communityDetailDto);

        if (community == null) {
            throw new CommonLogicException(ApiState.LOGIC.getCode(), "msg27_common_dataNotExist");
        }

        int updateResult = communityMapper.updateCommunity(communityUpdateReqDto);

        if (communityUpdateReqDto.getDeleteFileId() != null && communityUpdateReqDto.getDeleteFileId().size() > 0) {
            for (int i = 0; i < communityUpdateReqDto.getDeleteFileId().size(); i++) {
                mediaFileMapper.deleteById(Long.valueOf(communityUpdateReqDto.getDeleteFileId().get(i)));
            }
        }

        if (communityUpdateReqDto.getFileId() != null && communityUpdateReqDto.getFileId().size() > 0) {
            for (String file: communityUpdateReqDto.getFileId()) {
                MediaFile mediaFile = MediaFile.builder()
                    .foreignId(Long.valueOf(communityUpdateReqDto.getCommunityId()))
                    .foreignType(communityUpdateReqDto.getForeignType())
                    .id(Long.valueOf(file))
                    .build();
                mediaFileMapper.updateSaveFile(mediaFile);
            }
        }

        return updateResult;
    }

}
