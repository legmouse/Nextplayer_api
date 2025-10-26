package kr.co.nextplayer.base.likes.service;

import kr.co.nextplayer.base.likes.dto.LikesReqDto;
import kr.co.nextplayer.base.likes.mapper.LikesMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class LikesService {

    @Resource
    private LikesMapper likesMapper;

    public int insertLike(LikesReqDto likesReqDto) {
        return likesMapper.insertLikes(likesReqDto);
    }

    public int deleteLike(LikesReqDto likesReqDto) {
        return likesMapper.deleteLikes(likesReqDto);
    }

}
