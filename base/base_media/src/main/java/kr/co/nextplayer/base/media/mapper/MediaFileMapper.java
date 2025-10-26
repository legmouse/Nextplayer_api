package kr.co.nextplayer.base.media.mapper;

import kr.co.nextplayer.base.media.model.MediaFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MediaFileMapper {

    long insert(MediaFile mediaFile);

    long update(MediaFile mediaFile);

    MediaFile findById(Long id);

    MediaFile findByParentIdAndWidth(@Param("parentId") Long parentId, @Param("width") Integer width);

    long deleteById(Long id);

    long updateSaveFile(MediaFile mediaFile);

    List<String> selectFindMediaFileList(String foreignId);
}
