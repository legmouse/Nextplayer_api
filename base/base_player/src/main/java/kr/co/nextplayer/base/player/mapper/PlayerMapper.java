package kr.co.nextplayer.base.player.mapper;

import kr.co.nextplayer.base.player.dto.PlayerReqDto;
import kr.co.nextplayer.base.player.model.Player;
import kr.co.nextplayer.base.player.model.PlayerDetail;
import kr.co.nextplayer.base.player.model.Roster;
import kr.co.nextplayer.base.player.model.RosterPlayer;

import java.util.List;

public interface PlayerMapper {

    int selectRosterListCnt(PlayerReqDto playerReqDto);

    List<Roster> selectRosterList(PlayerReqDto playerReqDto);

    List<Roster> selectNationalRosterList(PlayerReqDto playerReqDto);

    List<Roster> selectGoldenAgeRosterList(PlayerReqDto playerReqDto);

    int selectRosterPlayerListCnt(PlayerReqDto playerReqDto);

    List<RosterPlayer> selectRosterPlayerList(PlayerReqDto playerReqDto);

    Roster selectRosterInfo(PlayerReqDto playerReqDto);

    List<Player> selectPlayerList(PlayerReqDto playerReqDto);

    PlayerDetail selectPlayerInfo(PlayerReqDto playerReqDto);

    List<Player> selectPlayerHistory(PlayerReqDto playerReqDto);

    List<Roster> selectPlayerJoinRosterList(PlayerReqDto playerReqDto);

    List<Roster> selectPlayerJoinNationalRoster(PlayerReqDto playerReqDto);

    List<Roster> selectPlayerJoinGoldenAgeRoster(PlayerReqDto playerReqDto);

    List<Roster> selectRosterInfoPageList(PlayerReqDto playerReqDto);
}
