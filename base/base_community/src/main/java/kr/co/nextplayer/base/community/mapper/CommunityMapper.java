package kr.co.nextplayer.base.community.mapper;

import kr.co.nextplayer.base.community.dto.CommunityDetailReqDto;
import kr.co.nextplayer.base.community.dto.CommunityListReqDto;
import kr.co.nextplayer.base.community.dto.CommunityReqDto;
import kr.co.nextplayer.base.community.dto.CommunityUpdateReqDto;
import kr.co.nextplayer.base.community.model.Community;

import java.util.HashMap;
import java.util.List;

public interface CommunityMapper {

    int insertCommunity(CommunityReqDto communityReqDto);

    int selectCommunityListCount(CommunityListReqDto communityListReqDto);

    List<Community> selectCommunityList(CommunityListReqDto communityListReqDto);

    Community selectCommunityDetail(CommunityDetailReqDto communityDetailReqDto);

    int deleteCommunity(CommunityUpdateReqDto communityUpdateReqDto);

    int updateCommunity(CommunityUpdateReqDto communityUpdateReqDto);

    int updateCommunityViewCnt(HashMap<String, Object> params);

}
