package kr.co.nextplayer.base.front.service;

import kr.co.nextplayer.base.front.mapper.UageMapper;
import kr.co.nextplayer.base.front.model.Uage;
import kr.co.nextplayer.base.front.response.player.*;
import kr.co.nextplayer.base.player.dto.*;
import kr.co.nextplayer.base.player.mapper.PlayerMapper;
import kr.co.nextplayer.base.player.model.Player;
import kr.co.nextplayer.base.player.model.PlayerDetail;
import kr.co.nextplayer.base.player.model.Roster;
import kr.co.nextplayer.base.player.model.RosterPlayer;
import kr.co.nextplayer.next.lib.common.mybatis.hander.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@Transactional
public class PlayerService {

    @Resource
    private PlayerMapper playerMapper;

    @Resource
    private UageMapper uageMapper;

    public RosterResponse getRosterList(PlayerReqDto playerReqDto) {

        int totalCount = playerMapper.selectRosterListCnt(playerReqDto);

        int curPage = playerReqDto.getCurPage();

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

        playerReqDto.setSRow(sRow);
        playerReqDto.setECount(cpp);

        List<HashMap<String, Object>> yearList = uageMapper.selectYearList();

        List<Roster> rosterList = playerMapper.selectRosterList(playerReqDto);
        List<RosterDto> resultList = new ArrayList<RosterDto>();

        if (rosterList.size() > 0) {
            for (Roster roster : rosterList) {
                RosterDto rosterDto = RosterDto.builder()
                        .roster_id(roster.getRosterId())
                        .title(roster.getTitle())
                        .year(roster.getYear())
                        .uage(roster.getUage())
                        .roster_type(roster.getRosterType())
                        .type(roster.getType())
                        .reg_date(roster.getRegDate())
                        .center_type(roster.getCenterType())
                    .build();

                resultList.add(rosterDto);
            }
        }

        RosterResponse rosterResponse = RosterResponse.builder()
            .yearList(yearList)
            .totalCount(totalCount)
            .rosterList(resultList)
            .build();

        return rosterResponse;
    }

    public NationalRosterResponse getNationalRosterList(PlayerReqDto playerReqDto) {


        List<Roster> rosterList = playerMapper.selectNationalRosterList(playerReqDto);
        List<RosterDto> resultList = new ArrayList<RosterDto>();

        if (rosterList.size() > 0) {
            for (Roster roster : rosterList) {
                RosterDto rosterDto = RosterDto.builder()
                    .roster_id(roster.getRosterId())
                    .title(roster.getTitle())
                    .comment(roster.getComment())
                    .year(roster.getYear())
                    .uage(roster.getUage())
                    .roster_type(roster.getRosterType())
                    .reg_date(roster.getRegDate())
                    .build();

                resultList.add(rosterDto);
            }
        }

        NationalRosterResponse nationalRosterResponse = NationalRosterResponse.builder()
            .nationalRosterList(resultList)
            .build();

        return nationalRosterResponse;
    }

    public GoldenAgeRosterResponse getGoldenAgeRosterList(PlayerReqDto playerReqDto) {


        List<Roster> rosterList = playerMapper.selectGoldenAgeRosterList(playerReqDto);
        List<RosterDto> resultList = new ArrayList<RosterDto>();

        if (rosterList.size() > 0) {
            for (Roster roster : rosterList) {
                RosterDto rosterDto = RosterDto.builder()
                    .roster_id(roster.getRosterId())
                    .title(roster.getTitle())
                    .comment(roster.getComment())
                    .year(roster.getYear())
                    .roster_type(roster.getRosterType())
                    .center_type(roster.getCenterType())
                    .type(roster.getType())
                    .reg_date(roster.getRegDate())
                    .build();

                resultList.add(rosterDto);
            }
        }

        GoldenAgeRosterResponse goldenAgeRosterResponse = GoldenAgeRosterResponse.builder()
            .goldenAgeRosterList(resultList)
            .build();

        return goldenAgeRosterResponse;
    }

    public RosterPlayerResponse getRosterPlayerList(PlayerReqDto playerReqDto) {

        int totalCount = playerMapper.selectRosterPlayerListCnt(playerReqDto);

        PageInfo pageInfo = new PageInfo(playerReqDto.getCurPage(), playerReqDto.getPageSize());

        playerReqDto.setOffset(pageInfo.getOffset());
        playerReqDto.setPageSize(pageInfo.getPageSize());

        List<RosterPlayer> rosterPlayerList = playerMapper.selectRosterPlayerList(playerReqDto);
        List<RosterPlayerDto> resultList = new ArrayList<RosterPlayerDto>();

        List<HashMap<String, Object>> yearList = uageMapper.selectYearList();

        if (rosterPlayerList.size() > 0) {
            for (RosterPlayer player : rosterPlayerList) {
                resultList.add(new RosterPlayerDto(player.getRosterId(), player.getPlayerId(), player.getName(),
                                                    player.getPosition(), player.getBirthday(), player.getNickName(), player.getEmblem()));
            }
        }

        RosterPlayerResponse rosterResponse = RosterPlayerResponse.builder()
            .total_count(totalCount)
            .rosterPlayerList(resultList)
            .yearList(yearList)
            .build();

        return rosterResponse;
    }

    public RosterInfoResponse getRosterInfo(PlayerReqDto playerReqDto) {
        Roster rosterInfo = playerMapper.selectRosterInfo(playerReqDto);

        RosterDto rosterDto = RosterDto.builder()
            .roster_id(rosterInfo.getRosterId())
            .title(rosterInfo.getTitle())
            .comment(rosterInfo.getComment())
            .year(rosterInfo.getYear())
            .uage(rosterInfo.getUage())
            .roster_type(rosterInfo.getRosterType())
            .type(rosterInfo.getType())
            .reg_date(rosterInfo.getRegDate())
            .rosterFiles(rosterInfo.getRosterFiles())
            .build();

        List<Player> players = playerMapper.selectPlayerList(playerReqDto);
        List<PlayerDto> playerList = new ArrayList<PlayerDto>();
        if (players.size() >0) {
            for (Player player : players) {
                playerList.add(new PlayerDto(player.getPlayerId(), player.getName(), player.getPosition()
                    , player.getBirthday() ,player.getTeamId(), player.getTeamName(), player.getTeamType(), player.getEmblem()));
            }
        }

        RosterInfoResponse rosterInfoResponse = RosterInfoResponse.builder()
            .rosterInfo(rosterDto)
            .playerList(playerList)
            .build();

        return rosterInfoResponse;
    }

    public PlayerInfoResponse getPlayerInfo(PlayerReqDto playerReqDto) {
        PlayerDetail playerInfo = playerMapper.selectPlayerInfo(playerReqDto);

        PlayerDetailDto playerDto = PlayerDetailDto.builder()
            .player_id(playerInfo.getPlayerId())
            .name(playerInfo.getName())
            .birthday(playerInfo.getBirthday())
            .position(playerInfo.getPosition())
            .team_id(playerInfo.getTeamId())
            .emblem(playerInfo.getEmblem())
            .build();

        List<Player> history = playerMapper.selectPlayerHistory(playerReqDto);
        List<PlayerHistoryDto> playerList = new ArrayList<PlayerHistoryDto>();
        if (history.size() >0) {
            for (Player player : history) {
                playerList.add(new PlayerHistoryDto(player.getTeamId(), player.getTeamName(), player.getTeamType(), player.getYear(), player.getEmblem()));
            }
        }

        PlayerInfoResponse rosterInfoResponse = PlayerInfoResponse.builder()
            .playerInfo(playerDto)
            .playerHistoryList(playerList)
            .build();

        return rosterInfoResponse;
    }

    public PlayerJoinRosterResponse getPlayerJoinRosterInfo(PlayerReqDto playerReqDto) {

        List<Roster> joinRosterList = playerMapper.selectPlayerJoinRosterList(playerReqDto);
        List<RosterDto> rosterList = new ArrayList<RosterDto>();
        if (joinRosterList.size() > 0) {
            for (Roster list : joinRosterList) {
                RosterDto rosterDto = RosterDto.builder()
                    .roster_id(list.getRosterId())
                    .title(list.getTitle())
                    .comment(list.getComment())
                    .year(list.getYear())
                    .uage(list.getUage())
                    .roster_type(list.getRosterType())
                    .center_type((list.getCenterType()))
                    .type(list.getType())
                    .reg_date(list.getRegDate())
                    .build();

                rosterList.add(rosterDto);
            }
        }

        List<Object> joinRosterByYearList = new ArrayList<Object>();

        if (playerReqDto.getRosterType().equals("0")) {

            List<Roster> nationalRosters = playerMapper.selectPlayerJoinNationalRoster(playerReqDto);

            if (nationalRosters.size() > 0) {
                for (Roster roster : nationalRosters) {
                    joinRosterByYearList.add(new PlayerNationalRosterDto(roster.getYear(), roster.getEnterCnt()));
                }
            }

        } else if (playerReqDto.getRosterType().equals("1")) {
            List<Roster> goldenAgeRosters = playerMapper.selectPlayerJoinGoldenAgeRoster(playerReqDto);

            if (goldenAgeRosters.size() > 0) {
                for (Roster roster : goldenAgeRosters) {
                    joinRosterByYearList.add(new PlayerGoldenAgeRosterDto(roster.getYear(), roster.getLocalCnt(), roster.getFiveAreaCnt(), roster.getAllAreaCnt(), roster.getGiftedCnt(), roster.getFutureCnt(), roster.getEliteCnt()));
                }
            }

        }


        PlayerJoinRosterResponse playerJoinRosterResponse = PlayerJoinRosterResponse.builder()
            .rosterList(rosterList)
            .joinRosterByYearList(joinRosterByYearList)
            .build();

        return playerJoinRosterResponse;
    }

    public RosterInfoPageResponse getRosterInfoPageList(PlayerReqDto playerReqDto) {


        playerReqDto.setRosterType("0");
        List<Roster> joinRosterList = playerMapper.selectRosterInfoPageList(playerReqDto);
        List<RosterDto> nationalRosterList = new ArrayList<RosterDto>();
        if (joinRosterList.size() > 0) {
            for (Roster list : joinRosterList) {
                RosterDto rosterDto = RosterDto.builder()
                    .roster_id(list.getRosterId())
                    .title(list.getTitle())
                    .comment(list.getComment())
                    .year(list.getYear())
                    .uage(list.getUage())
                    .roster_type(list.getRosterType())
                    .type(list.getType())
                    .center_type(list.getType())
                    .reg_date(list.getRegDate())
                    .build();

                nationalRosterList.add(rosterDto);
            }
        }

        playerReqDto.setRosterType("1");
        List<Roster> joinGoldenRosterList = playerMapper.selectRosterInfoPageList(playerReqDto);
        List<RosterDto> goldenAgeRosterList = new ArrayList<RosterDto>();
        if (joinRosterList.size() > 0) {
            for (Roster list : joinGoldenRosterList) {
                RosterDto rosterDto = RosterDto.builder()
                    .roster_id(list.getRosterId())
                    .title(list.getTitle())
                    .comment(list.getComment())
                    .year(list.getYear())
                    .uage(list.getUage())
                    .roster_type(list.getRosterType())
                    .type(list.getType())
                    .center_type(list.getType())
                    .reg_date(list.getRegDate())
                    .build();

                goldenAgeRosterList.add(rosterDto);
            }
        }



        RosterInfoPageResponse playerJoinRosterResponse = RosterInfoPageResponse.builder()
            .nationalRosterList(nationalRosterList)
            .goldenAgeRosterList(goldenAgeRosterList)
            .build();

        return playerJoinRosterResponse;
    }

}
