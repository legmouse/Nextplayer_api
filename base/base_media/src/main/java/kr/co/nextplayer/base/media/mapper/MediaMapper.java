package kr.co.nextplayer.base.media.mapper;

import kr.co.nextplayer.base.media.dto.GameReqDto;
import kr.co.nextplayer.base.media.dto.MediaReqDto;
import kr.co.nextplayer.base.media.model.Creator;
import kr.co.nextplayer.base.media.model.Media;

import java.util.HashMap;
import java.util.List;

public interface MediaMapper {

    int selectMediaListCnt(MediaReqDto mediaReqDto);

    List<Media> selectMediaList(MediaReqDto mediaReqDto);

    int selectGameMediaListCnt(GameReqDto mediaReqDto);

    List<Media> selectGameMediaList(GameReqDto mediaReqDto);

    List<Media> selectMediaDetailList(MediaReqDto mediaReqDto);

    List<Media> selectMediaDetailListForTeam(MediaReqDto mediaReqDto);

    List<Media> selectVideoMediaList(MediaReqDto mediaReqDto);

    List<Media> selectNewsMediaList(MediaReqDto mediaReqDto);

    List<Media> selectBlogMediaList(MediaReqDto mediaReqDto);

    List<Media> selectMediaChildList(String mediaId);

    Media selectMediaDetailInfo(MediaReqDto mediaReqDto);

    List<Creator> selectCreatorList(MediaReqDto mediaReqDto);

    List<HashMap<String, Object>> selectHomeMediaList(HashMap<String, String> params);

    int updateMediaViewCnt(MediaReqDto mediaReqDto);

    List<Media> selectPrevNextMedia(MediaReqDto mediaReqDto);

    List<Media> selectMainGameData(String ageGroup);
}
