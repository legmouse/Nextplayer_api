package kr.co.nextplayer.base.likes.mapper;

import kr.co.nextplayer.base.likes.dto.LikesReqDto;

public interface LikesMapper {

    int insertLikes(LikesReqDto likesReqDto);

    int deleteLikes(LikesReqDto likesReqDto);

    boolean selectLikedContent(LikesReqDto likesReqDto);

}
