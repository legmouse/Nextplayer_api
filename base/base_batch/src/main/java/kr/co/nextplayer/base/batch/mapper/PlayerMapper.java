package kr.co.nextplayer.base.batch.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PlayerMapper {

    List<Integer> selectJoinkfaPlayerId(Map<String, String> params);

    List<Integer> selectJoinkfaPlayerIdOwnGoal(Map<String, String> params);

    int selectLatestJoinKfaOrd();

}
