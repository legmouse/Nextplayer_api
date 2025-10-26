package kr.co.nextplayer.base.batch.service;

import kr.co.nextplayer.base.batch.dto.joinkfaCraw.*;
import kr.co.nextplayer.base.batch.enums.AgeGroup;
import kr.co.nextplayer.base.batch.enums.MatchType;
import kr.co.nextplayer.base.batch.mapper.CupMapper;
import kr.co.nextplayer.base.batch.mapper.PlayerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CupService {

    private final CupMapper cupMapper;

    private final PlayerMapper playerMapper;

    @Transactional
    public void updateCupScore(List<MatchPlayDataDto> playData, MatchType matchType, AgeGroup ageGroup, int cupId) {
        Map<String, String> matchmap = new HashMap<>();
        String cupMatchTB = "";
        List<MatchDataDto> matchList = new ArrayList<>();
        switch (matchType) {
            case MAIN:
                cupMatchTB = StringUtils.lowerCase(ageGroup.name()) + "_Cup_Main_Match";
                matchmap.put("matchTB", cupMatchTB);
                matchmap.put("cupId", String.valueOf(cupId));
                matchList = cupMapper.selectCupMainMatchListCraw(matchmap);
                break;
            case TOUR:
                cupMatchTB = StringUtils.lowerCase(ageGroup.name()) + "_Cup_Tour_Match";
                matchmap.put("matchTB", cupMatchTB);
                matchmap.put("cupId", String.valueOf(cupId));
                matchList = cupMapper.selectCupTourMatchListCraw(matchmap);
                break;
            case SUB:
                cupMatchTB = StringUtils.lowerCase(ageGroup.name()) + "_Cup_Sub_Match";
                matchmap.put("matchTB", cupMatchTB);
                matchmap.put("cupId", String.valueOf(cupId));
                matchList = cupMapper.selectCupSubMatchListCraw(matchmap);
                break;
        }

        for (MatchPlayDataDto data : playData) {
            Map<String, Object> map = new HashMap<>();

            MatchPlayDataDto.MatchStatus status = data.getStatus();

            try {
                Optional<MatchDataDto> matchOptional = matchList.stream()
                    .filter(item -> item.getMatchId().equals(data.getMatchId())).findFirst();

                if (matchOptional.isPresent()) {
                    MatchDataDto match = matchOptional.get();
                    int homeScore = data.getHomeScore();
                    int awayScore = data.getAwayScore();
                    int homePk = data.getHomePenaltyScore();
                    int awayPk = data.getAwayPenaltyScore();
                    int homeBfScore = match.getHomeBfScore();
                    int awayBfScore = match.getAwayBfScore();
                    int homeBfPk = match.getHomePk();
                    int awayBfPk = match.getAwayPk();
                    int endFlag = match.getEndFlag();
                    int updFlag = 0;
                    // 이미 종료 처리한 경기는 업데이트 하지 않음
                    if (endFlag == 0) {
                        switch (matchType) {
                            case MAIN:
                                // 수정 전 점수와 크롤링한 점수를 비교하여 다르면 수정
                                // 같으면 수정하지 않음
                                if (homeScore != homeBfScore || awayScore != awayBfScore) {
                                    updFlag = 1;
                                    cupMatchTB = StringUtils.lowerCase(ageGroup.name()) + "_Cup_Main_Match";

                                    if (homePk > 0 && awayPk > 0) {
                                        if (homeBfPk != homePk || awayBfPk != awayPk) {
                                            map.put("homePk", homePk);
                                            map.put("awayPk", awayPk);
                                        } else {
                                            map.put("homePk", homeBfPk);
                                            map.put("awayPk", awayBfPk);
                                        }
                                        map.put("selMatchType", 1);

                                        if (updFlag == 1) {
                                            updFlag = 2;
                                        } else if (updFlag == 0) {
                                            updFlag = 3;
                                        }
                                    } else {
                                        if (homeBfPk > 0 && awayBfPk > 0) {
                                            map.put("homePk", homeBfPk);
                                            map.put("awayPk", awayBfPk);
                                            map.put("selMatchType", 1);
                                        } else if (homeBfPk == 0 && awayBfPk == 0) {
                                            map.put("homePk", homeBfPk);
                                            map.put("awayPk", awayBfPk);
                                            map.put("selMatchType", 0);
                                        }
                                    }

                                    if (status.toString().equals(MatchPlayDataDto.MatchStatus.END.toString())) {
                                        map.put("endFlag", 1);
                                    }

                                    map.put("homeScore", homeScore);
                                    map.put("awayScore", awayScore);
                                    map.put("cupMainMatchTB", cupMatchTB);
                                    map.put("cupMainMatchId", data.getMatchId());
                                    map.put("updFlag", updFlag);
                                    map.put("reason", "");
                                    cupMapper.updateCupMainMatchScore(map);
                                }
                                break;
                            case SUB:
                                cupMatchTB = StringUtils.lowerCase(ageGroup.name()) + "_Cup_Sub_Match";
                                if (homeScore != homeBfScore || awayScore != awayBfScore) {
                                    updFlag = 1;
                                    if (homePk > 0 && awayPk > 0) {
                                        if (homeBfPk != homePk || awayBfPk != awayPk) {
                                            map.put("homePk", homePk);
                                            map.put("awayPk", awayPk);
                                        } else {
                                            map.put("homePk", homeBfPk);
                                            map.put("awayPk", awayBfPk);
                                        }
                                        map.put("selMatchType", 1);

                                        if (updFlag == 1) {
                                            updFlag = 2;
                                        } else if (updFlag == 0) {
                                            updFlag = 3;
                                        }
                                    } else {
                                        if (homeBfPk > 0 && awayBfPk > 0) {
                                            map.put("homePk", homeBfPk);
                                            map.put("awayPk", awayBfPk);
                                            map.put("selMatchType", 1);
                                        } else if (homeBfPk == 0 && awayBfPk == 0) {
                                            map.put("homePk", homeBfPk);
                                            map.put("awayPk", awayBfPk);
                                            map.put("selMatchType", 0);
                                        }
                                    }

                                    if (status.toString().equals(MatchPlayDataDto.MatchStatus.END.toString())) {
                                        map.put("endFlag", 1);
                                    }

                                    map.put("homeScore", homeScore);
                                    map.put("awayScore", awayScore);
                                    map.put("cupSubMatchTB", cupMatchTB);
                                    map.put("cupSubMatchId", data.getMatchId());
                                    map.put("updFlag", updFlag);
                                    map.put("reason", "");
                                    cupMapper.updateCupSubMatchScore(map);
                                } else {
                                    if (status.toString().equals(MatchPlayDataDto.MatchStatus.END.toString())) {
                                        Map<String, String> param = new HashMap<>();
                                        param.put("cupId", String.valueOf(cupId));
                                        param.put("cupSubMatchTB", cupMatchTB);
                                        param.put("cupSubMatchId", match.getMatchId());
                                        cupMapper.updateCupSubMatchEndFlag(param);
                                    }
                                }
                                break;
                            case TOUR:
                                if (homeScore != homeBfScore || awayScore != awayBfScore) {

                                    updFlag = 1;

                                    cupMatchTB = StringUtils.lowerCase(ageGroup.name()) + "_Cup_Tour_Match";

                                    if (homePk > 0 && awayPk > 0) {
                                        if (homeBfPk != homePk || awayBfPk != awayPk) {
                                            map.put("homePk", homePk);
                                            map.put("awayPk", awayPk);
                                        } else {
                                            map.put("homePk", homeBfPk);
                                            map.put("awayPk", awayBfPk);
                                        }
                                        map.put("selMatchType", 1);

                                        if (updFlag == 1) {
                                            updFlag = 2;
                                        } else if (updFlag == 0) {
                                            updFlag = 3;
                                        }
                                    } else {
                                        if (homeBfPk > 0 && awayBfPk > 0) {
                                            map.put("homePk", homeBfPk);
                                            map.put("awayPk", awayBfPk);
                                            map.put("selMatchType", 1);
                                        } else if (homeBfPk == 0 && awayBfPk == 0) {
                                            map.put("homePk", homeBfPk);
                                            map.put("awayPk", awayBfPk);
                                            map.put("selMatchType", 0);
                                        }
                                    }

                                    map.put("homeScore", homeScore);
                                    map.put("awayScore", awayScore);
                                    map.put("cupTourMatchTB", cupMatchTB);
                                    map.put("cupTourMatchId", data.getMatchId());
                                    map.put("updFlag", updFlag);
                                    map.put("reason", "");
                                    cupMapper.updateCupTourMatchScore(map);

                                    Map<String, String> matchParam = new HashMap<String, String>();

                                    if (match.getRound() != 2)  {
                                        //if (tourMatchInfo.get("home_score").toString().equals(tourMatchInfo.get("away_score").toString())) {
                                        if (homeScore == awayScore) {
                                            // 동점인 경우
                                            if (homePk > awayPk) {
                                                // home 팀이 pk 승
                                                if (match.getNextTourPort() == 0) {
                                                    // 다음 매치 home일 때
                                                    matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                    matchParam.put("home", match.getHome());
                                                    matchParam.put("homeId", String.valueOf(match.getHomeId()));
                                                } else {
                                                    // 다음 매치 away일 때
                                                    matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                    matchParam.put("away", match.getHome());
                                                    matchParam.put("awayId", String.valueOf(match.getHomeId()));
                                                }
                                            } else {
                                                // away 팀이 pk 승
                                                if (match.getNextTourPort() == 0) {
                                                    // 다음 매치 home일 때
                                                    matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                    matchParam.put("home", match.getAway());
                                                    matchParam.put("homeId", String.valueOf(match.getAwayId()));

                                                } else {
                                                    // away일 때
                                                    matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                    matchParam.put("away", match.getAway());
                                                    matchParam.put("awayId", String.valueOf(match.getAwayId()));
                                                }
                                            }
                                        } else {
                                            if (homeScore > awayScore){
                                                if (match.getNextTourPort() == 0) {
                                                    // 다음 매치 home일 때
                                                    matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                    matchParam.put("home", match.getHome());
                                                    matchParam.put("homeId", String.valueOf(match.getHomeId()));

                                                } else {
                                                    // 다음 매치 away일 때
                                                    matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                    matchParam.put("away", match.getHome());
                                                    matchParam.put("awayId",  String.valueOf(match.getHomeId()));
                                                }
                                            } else {
                                                if (match.getNextTourPort() == 0) {
                                                    // 다음 매치 home일 때
                                                    matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                    matchParam.put("home", match.getAway());
                                                    matchParam.put("homeId", String.valueOf(match.getAwayId()));

                                                } else {
                                                    // 다음 매치 away일 때
                                                    matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                    matchParam.put("away", match.getAway());
                                                    matchParam.put("awayId", String.valueOf(match.getAwayId()));
                                                }
                                            }
                                        }
                                        matchParam.put("cupId", String.valueOf(cupId));
                                        matchParam.put("cupTourMatchTB", cupMatchTB);
                                        HashMap<String, Object> nextMacthInfo = new HashMap<String, Object>();
                                        nextMacthInfo = cupMapper.selectCupNextTourMatch(matchParam);
                                        matchParam.put("cupTourMatchId", nextMacthInfo.get("cup_tour_match_id").toString());

                                        if (status.toString().equals(MatchPlayDataDto.MatchStatus.END.toString())) {
                                            map.put("endFlag", 1);
                                            map.put("fixFlag", 1);
                                            cupMapper.updateCupTourNextMatch(matchParam);
                                        }
                                    }
                                } else {
                                    if (homePk > 0 && awayPk > 0) {
                                        cupMatchTB = StringUtils.lowerCase(ageGroup.name()) + "_Cup_Tour_Match";

                                        if (homeBfPk != homePk || awayBfPk != awayPk) {
                                            map.put("homePk", homePk);
                                            map.put("awayPk", awayPk);
                                        } else {
                                            map.put("homePk", homeBfPk);
                                            map.put("awayPk", awayBfPk);
                                        }
                                        map.put("selMatchType", 1);

                                        if (updFlag == 1) {
                                            updFlag = 2;
                                        } else if (updFlag == 0) {
                                            updFlag = 3;
                                        }

                                        map.put("homeScore", homeBfScore);
                                        map.put("awayScore", awayBfScore);
                                        map.put("cupTourMatchTB", cupMatchTB);
                                        map.put("cupTourMatchId", data.getMatchId());
                                        map.put("updFlag", updFlag);
                                        map.put("reason", "");
                                        cupMapper.updateCupTourMatchScore(map);

                                        Map<String, String> matchParam = new HashMap<String, String>();
                                        if (match.getRound() != 2)  {
                                            if (homePk > awayPk) {
                                                // home 팀이 pk 승
                                                if (match.getNextTourPort() == 0) {
                                                    // 다음 매치 home일 때
                                                    matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                    matchParam.put("home", match.getHome());
                                                    matchParam.put("homeId", String.valueOf(match.getHomeId()));
                                                } else {
                                                    // 다음 매치 away일 때
                                                    matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                    matchParam.put("away", match.getHome());
                                                    matchParam.put("awayId", String.valueOf(match.getHomeId()));
                                                }
                                            } else {
                                                // away 팀이 pk 승
                                                if (match.getNextTourPort() == 0) {
                                                    // 다음 매치 home일 때
                                                    matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                    matchParam.put("home", match.getAway());
                                                    matchParam.put("homeId", String.valueOf(match.getAwayId()));

                                                } else {
                                                    // away일 때
                                                    matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                    matchParam.put("away", match.getAway());
                                                    matchParam.put("awayId", String.valueOf(match.getAwayId()));
                                                }
                                            }
                                            matchParam.put("cupId", String.valueOf(cupId));
                                            matchParam.put("cupTourMatchTB", cupMatchTB);
                                            HashMap<String, Object> nextMacthInfo = new HashMap<String, Object>();
                                            nextMacthInfo = cupMapper.selectCupNextTourMatch(matchParam);
                                            matchParam.put("cupTourMatchId", nextMacthInfo.get("cup_tour_match_id").toString());

                                            if (status.toString().equals(MatchPlayDataDto.MatchStatus.END.toString())) {
                                                map.put("endFlag", 1);
                                                map.put("fixFlag", 1);
                                               cupMapper.updateCupTourNextMatch(matchParam);
                                            }
                                        }
                                    } else {
                                        if (status.toString().equals(MatchPlayDataDto.MatchStatus.END.toString())) {
                                            cupMatchTB = StringUtils.lowerCase(ageGroup.name()) + "_Cup_Tour_Match";
                                            Map<String, String> param = new HashMap<>();
                                            param.put("cupId", String.valueOf(cupId));
                                            param.put("cupTourMatchTB", cupMatchTB);
                                            param.put("cupTourMatchId", match.getMatchId());
                                            cupMapper.updateCupTourMatchFixFlag(param);
                                            Map<String, String> matchParam = new HashMap<String, String>();

                                            if (match.getRound() != 2)  {
                                                if ((homeBfPk > awayBfPk) || (homeScore > awayScore)) {
                                                    // home 팀이 pk 승 or home 팀 승
                                                    if (match.getNextTourPort() == 0) {
                                                        // 다음 매치 home일 때
                                                        matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                        matchParam.put("home", match.getHome());
                                                        matchParam.put("homeId", String.valueOf(match.getHomeId()));
                                                    } else {
                                                        // 다음 매치 away일 때
                                                        matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                        matchParam.put("away", match.getHome());
                                                        matchParam.put("awayId", String.valueOf(match.getHomeId()));
                                                    }
                                                } else {
                                                    // away 팀이 pk 승
                                                    if (match.getNextTourPort() == 0) {
                                                        // 다음 매치 home일 때
                                                        matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                        matchParam.put("home", match.getAway());
                                                        matchParam.put("homeId", String.valueOf(match.getAwayId()));

                                                    } else {
                                                        // away일 때
                                                        matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                        matchParam.put("away", match.getAway());
                                                        matchParam.put("awayId", String.valueOf(match.getAwayId()));
                                                    }
                                                }
                                                matchParam.put("cupId", String.valueOf(cupId));
                                                matchParam.put("cupTourMatchTB", cupMatchTB);
                                                HashMap<String, Object> nextMacthInfo = new HashMap<String, Object>();
                                                nextMacthInfo = cupMapper.selectCupNextTourMatch(matchParam);
                                                matchParam.put("cupTourMatchId", nextMacthInfo.get("cup_tour_match_id").toString());
                                                cupMapper.updateCupTourNextMatch(matchParam);
                                            }
                                        }
                                    }
                                }
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @Transactional
    public void updateCupMatchInfo(List<MatchPlayDataDto> playDataList, AgeGroup ageGroup, String level, MatchType cupType) {

        for (MatchPlayDataDto playData : playDataList) {

            Map<String, Object> map = new HashMap<>();
            String convertLevel = getLevel(level);

            String cupMatchTB = "";
            String cupMatchPlayDataTB = "";
            String cupMatchChangeDataTB = "";
            String cupStaffDataTB = "";
            String cupStaffPenaltyDataTB = "";
            String cupOwnGoalDataTB = "";

            int ord = playerMapper.selectLatestJoinKfaOrd();

            switch (cupType) {
                case MAIN:
                    cupMatchTB = StringUtils.lowerCase((ageGroup.name())) + "_Cup_Main_Match";
                    cupMatchPlayDataTB = StringUtils.lowerCase((ageGroup.name())) + "_Cup_Main_Match_Play_Data";
                    cupMatchChangeDataTB = StringUtils.lowerCase((ageGroup.name())) + "_Cup_Main_Match_Change_Data";
                    cupStaffDataTB = StringUtils.lowerCase((ageGroup.name())) + "_Cup_Main_Staff_Data";
                    cupStaffPenaltyDataTB = StringUtils.lowerCase((ageGroup.name())) + "_Cup_Main_Staff_Penalty_Data";
                    cupOwnGoalDataTB = StringUtils.lowerCase((ageGroup.name())) + "_Cup_Main_Own_Goal_Data";

                    /*기본적인 매치정보 업데이트*/
                    map.put("cupMatchTB", cupMatchTB);
                    map.put("matchId", playData.getMatchId());
                    map.put("playData", playData);
                    cupMapper.updateMainMatchInfo(map);
                    break;
                case SUB:
                    cupMatchTB = StringUtils.lowerCase((ageGroup.name())) + "_Cup_Sub_Match";
                    cupMatchPlayDataTB = StringUtils.lowerCase((ageGroup.name())) + "_Cup_Sub_Match_Play_Data";
                    cupMatchChangeDataTB = StringUtils.lowerCase((ageGroup.name())) + "_Cup_Sub_Match_Change_Data";
                    cupStaffDataTB = StringUtils.lowerCase((ageGroup.name())) + "_Cup_Sub_Staff_Data";
                    cupStaffPenaltyDataTB = StringUtils.lowerCase((ageGroup.name())) + "_Cup_Sub_Staff_Penalty_Data";
                    cupOwnGoalDataTB = StringUtils.lowerCase((ageGroup.name())) + "_Cup_Sub_Own_Goal_Data";

                    /*기본적인 매치정보 업데이트*/
                    map.put("cupMatchTB", cupMatchTB);
                    map.put("matchId", playData.getMatchId());
                    map.put("playData", playData);
                    cupMapper.updateSubMatchInfo(map);
                    break;
                case TOUR:
                    cupMatchTB = StringUtils.lowerCase((ageGroup.name())) + "_Cup_Tour_Match";
                    cupMatchPlayDataTB = StringUtils.lowerCase((ageGroup.name())) + "_Cup_Tour_Match_Play_Data";
                    cupMatchChangeDataTB = StringUtils.lowerCase((ageGroup.name())) + "_Cup_Tour_Match_Change_Data";
                    cupStaffDataTB = StringUtils.lowerCase((ageGroup.name())) + "_Cup_Tour_Staff_Data";
                    cupStaffPenaltyDataTB = StringUtils.lowerCase((ageGroup.name())) + "_Cup_Tour_Staff_Penalty_Data";
                    cupOwnGoalDataTB = StringUtils.lowerCase((ageGroup.name())) + "_Cup_Tour_Own_Goal_Data";

                    /*기본적인 매치정보 업데이트*/
                    map.put("cupMatchTB", cupMatchTB);
                    map.put("matchId", playData.getMatchId());
                    map.put("playData", playData);
                    cupMapper.updateTourMatchInfo(map);
                    break;
            }

            /*기존 경기결과 삭제*/
            Map<String, String> deletePlayDataMap = new HashMap<>();
            deletePlayDataMap.put("matchPlayDataOrChangeDataTB", cupMatchPlayDataTB);
            deletePlayDataMap.put("matchId", String.valueOf(playData.getMatchId()));
            cupMapper.deleteMatchPlayDataOrChangeData(deletePlayDataMap);

            /*기존 교체선수 삭제*/
            Map<String, String> deleteChangeDataMap = new HashMap<>();
            deleteChangeDataMap.put("matchPlayDataOrChangeDataTB", cupMatchChangeDataTB);
            deleteChangeDataMap.put("matchId", String.valueOf(playData.getMatchId()));
            cupMapper.deleteMatchPlayDataOrChangeData(deleteChangeDataMap);

            /*기존 감독,코치 삭제*/
            Map<String, String> deleteStaffDataMap = new HashMap<>();
            deleteStaffDataMap.put("staffDataOrStaffPenaltyDataOrOwnGoalDataTB", cupStaffDataTB);
            deleteStaffDataMap.put("matchId", String.valueOf(playData.getMatchId()));
            cupMapper.deleteMatchStaffDataOrStaffPenaltyDataOrOwnGoalData(deleteStaffDataMap);

            /*기존 감독,코치 경고 삭제*/
            Map<String, String> deleteStaffPenaltyDataMap = new HashMap<>();
            deleteStaffPenaltyDataMap.put("staffDataOrStaffPenaltyDataOrOwnGoalDataTB", cupStaffPenaltyDataTB);
            deleteStaffPenaltyDataMap.put("matchId", String.valueOf(playData.getMatchId()));
            cupMapper.deleteMatchStaffDataOrStaffPenaltyDataOrOwnGoalData(deleteStaffPenaltyDataMap);

            /*기존 자책골 삭제*/
            Map<String, String> deleteOwnGoalDataMap = new HashMap<>();
            deleteOwnGoalDataMap.put("staffDataOrStaffPenaltyDataOrOwnGoalDataTB", cupOwnGoalDataTB);
            deleteOwnGoalDataMap.put("matchId", String.valueOf(playData.getMatchId()));
            cupMapper.deleteMatchStaffDataOrStaffPenaltyDataOrOwnGoalData(deleteOwnGoalDataMap);

            /*선발 홈 플레이데이타 넣기*/
            final String finalCupMatchPlayDataTB = cupMatchPlayDataTB;
            playData.getHomePlaySelectionData().forEach(e ->{
                /*PlayerId Set*/
                e.setTeamName(playData.getHomeTeamName());
                setPlayerIdFromPlayData(convertLevel, e, ord);

                e.setHomeAwayGbn("home");
                e.setSelCanGbn("sel");
                e.setLeagueMatchId(Long.parseLong(playData.getMatchId()));

                /* 주장일 경우 처리 */
                if (e.isCaptain()) {
                    e.setCaptainFlag("1");
                } else {
                    e.setCaptainFlag("0");
                }

                Map<String, Object> playDataInsertMap = new HashMap<>();
                playDataInsertMap.put("matchPlayDataTB", finalCupMatchPlayDataTB);
                playDataInsertMap.put("playData", e);

                cupMapper.insertMatchPlayData(playDataInsertMap);
            });

            /*후발 홈 플레이데이타 넣기*/
            playData.getHomePlayCandidateData().forEach(e ->{
                /*PlayerId Set*/
                e.setTeamName(playData.getHomeTeamName());
                setPlayerIdFromPlayData(convertLevel, e, ord);

                e.setHomeAwayGbn("home");
                e.setSelCanGbn("can");
                e.setLeagueMatchId(Long.parseLong(playData.getMatchId()));

                /* 주장일 경우 처리 */
                if (e.isCaptain()) {
                    e.setCaptainFlag("1");
                } else {
                    e.setCaptainFlag("0");
                }

                Map<String, Object> playDataInsertMap = new HashMap<>();
                playDataInsertMap.put("matchPlayDataTB", finalCupMatchPlayDataTB);
                playDataInsertMap.put("playData", e);

                cupMapper.insertMatchPlayData(playDataInsertMap);
            });

            /*선발 어웨이 플레이데이타 넣기*/
            playData.getAwayPlaySelectionData().forEach(e ->{
                /*PlayerId Set*/
                e.setTeamName(playData.getAwayTeamName());
                setPlayerIdFromPlayData(convertLevel, e, ord);

                e.setHomeAwayGbn("away");
                e.setSelCanGbn("sel");
                e.setLeagueMatchId(Long.parseLong(playData.getMatchId()));

                /* 주장일 경우 처리 */
                if (e.isCaptain()) {
                    e.setCaptainFlag("1");
                } else {
                    e.setCaptainFlag("0");
                }

                Map<String, Object> playDataInsertMap = new HashMap<>();
                playDataInsertMap.put("matchPlayDataTB", finalCupMatchPlayDataTB);
                playDataInsertMap.put("playData", e);

                cupMapper.insertMatchPlayData(playDataInsertMap);
            });

            /*후발 어웨이 플레이데이타 넣기*/
            playData.getAwayPlayCandidateData().forEach(e ->{
                /*PlayerId Set*/
                e.setTeamName(playData.getAwayTeamName());
                setPlayerIdFromPlayData(convertLevel, e, ord);

                e.setHomeAwayGbn("away");
                e.setSelCanGbn("can");
                e.setLeagueMatchId(Long.parseLong(playData.getMatchId()));

                /* 주장일 경우 처리 */
                if (e.isCaptain()) {
                    e.setCaptainFlag("1");
                } else {
                    e.setCaptainFlag("0");
                }

                Map<String, Object> playDataInsertMap = new HashMap<>();
                playDataInsertMap.put("matchPlayDataTB", finalCupMatchPlayDataTB);
                playDataInsertMap.put("playData", e);

                cupMapper.insertMatchPlayData(playDataInsertMap);
            });

            /*홈팀 교체선수*/
            final String finalCupMatchChangeDataTB = cupMatchChangeDataTB;
            playData.getHomeChangeData().forEach(e -> {
                /*플레이어 아이디 SET*/
                e.setTeamName(playData.getHomeTeamName());
                setPlayerIdFromChangeData(playData, convertLevel, e, "home", ord);

                e.setHomeAwayGbn("home");
                e.setMatchId(Integer.parseInt(playData.getMatchId()));

                /*교체데이터 인서트*/
                Map<String, Object> insertMap = new HashMap<>();
                insertMap.put("matchChangeDataTB", finalCupMatchChangeDataTB);
                insertMap.put("changeData", e);
                cupMapper.insertMatchChangeData(insertMap);
            });

            /*어웨이팀 교체선수*/
            playData.getAwayChangeData().forEach(e -> {
                /*플레이어 아이디 SET*/
                e.setTeamName(playData.getAwayTeamName());
                setPlayerIdFromChangeData(playData, convertLevel, e, "away", ord);

                e.setHomeAwayGbn("away");
                e.setMatchId(Integer.parseInt(playData.getMatchId()));

                /*교체데이터 인서트*/
                Map<String, Object> insertMap = new HashMap<>();
                insertMap.put("matchChangeDataTB", finalCupMatchChangeDataTB);
                insertMap.put("changeData", e);
                cupMapper.insertMatchChangeData(insertMap);
            });

            /*홈팀 감독, 코치 데이터*/
            final String finalCupStaffDataTB = cupStaffDataTB;
            playData.getHomeStaffData().forEach(e -> {
                e.setHomeAwayGbn("home");
                e.setMatchId(Integer.parseInt(playData.getMatchId()));



                /*감독 코치 데이터 인서트*/
                Map<String, Object> insertMap = new HashMap<>();
                insertMap.put("cupStaffDataTB", finalCupStaffDataTB);
                insertMap.put("staffData", e);
                cupMapper.insertStaffData(insertMap);
            });

            /*어웨이팀 감독, 코치 데이터*/
            playData.getAwayStaffData().forEach(e -> {
                e.setHomeAwayGbn("away");
                e.setMatchId(Integer.parseInt(playData.getMatchId()));

                /*감독 코치 데이터 인서트*/
                Map<String, Object> insertMap = new HashMap<>();
                insertMap.put("cupStaffDataTB", finalCupStaffDataTB);
                insertMap.put("staffData", e);
                cupMapper.insertStaffData(insertMap);
            });

            /*홈팀 감독, 코치 페널티 데이터*/
            final String finalCupStaffPenaltyDataTB = cupStaffPenaltyDataTB;
            playData.getHomeStaffPenaltyData().forEach(e -> {
                e.setHomeAwayGbn("home");
                e.setMatchId(Integer.parseInt(playData.getMatchId()));

                /*감독 코치 데이터 페널티 인서트*/
                Map<String, Object> insertMap = new HashMap<>();
                insertMap.put("cupStaffPenaltyDataTB", finalCupStaffPenaltyDataTB);
                insertMap.put("staffPenaltyData", e);
                cupMapper.insertStaffPenaltyData(insertMap);
            });

            /*어웨이팀 감독, 코치 페널티 데이터*/
            playData.getAwayStaffPenaltyData().forEach(e -> {
                e.setHomeAwayGbn("away");
                e.setMatchId(Integer.parseInt(playData.getMatchId()));

                /*감독 코치 데이터 페널티 인서트*/
                Map<String, Object> insertMap = new HashMap<>();
                insertMap.put("cupStaffPenaltyDataTB", finalCupStaffPenaltyDataTB);
                insertMap.put("staffPenaltyData", e);
                cupMapper.insertStaffPenaltyData(insertMap);
            });

            /*홈팀 자살골 데이터*/
            final String finalCupOwnGoalDataTB = cupOwnGoalDataTB;
            playData.getHomeOwnGoalData().forEach(e -> {

                e.setTeamName(playData.getHomeTeamName());
                setPlayerIdFromOwnGoalData(convertLevel, e, ord);

                e.setHomeAwayGbn("home");
                e.setMatchId(Integer.parseInt(playData.getMatchId()));

                Map<String, Object> insertMap = new HashMap<>();
                insertMap.put("cupOwnGoalDataTB", finalCupOwnGoalDataTB);
                insertMap.put("ownGoalData", e);
                cupMapper.insertOwnGoalData(insertMap);
            });

            playData.getAwayOwnGoalData().forEach(e -> {

                e.setTeamName(playData.getAwayTeamName());
                setPlayerIdFromOwnGoalData(convertLevel, e, ord);

                e.setHomeAwayGbn("away");
                e.setMatchId(Integer.parseInt(playData.getMatchId()));

                Map<String, Object> insertMap = new HashMap<>();
                insertMap.put("cupOwnGoalDataTB", finalCupOwnGoalDataTB);
                insertMap.put("ownGoalData", e);
                cupMapper.insertOwnGoalData(insertMap);
            });
   
        }
    }


    private void setPlayerIdFromPlayData(String convertLevel, PlayDataDto e, int ord) {
        Map<String, String> playerSearchMap = new HashMap<>();
        playerSearchMap.put("level", convertLevel);
        playerSearchMap.put("position", e.getPosition());
        playerSearchMap.put("playerName", e.getPlayerName());
        playerSearchMap.put("ord", String.valueOf(ord));
        playerSearchMap.put("teamName", e.getTeamName());
        List<Integer> playerIds = playerMapper.selectJoinkfaPlayerId(playerSearchMap);
        /*선수명단에 있으면 선수명단 아이디 넣기*/
        if (playerIds.size() == 1) e.setPlayerId(playerIds.get(0));
    }

    private void setPlayerIdFromOwnGoalData(String convertLevel, OwnGoalDataDto e, int ord) {
        Map<String, String> playerSearchMap = new HashMap<>();
        playerSearchMap.put("level", convertLevel);
        playerSearchMap.put("playerName", e.getPlayerName());
        playerSearchMap.put("ord", String.valueOf(ord));
        playerSearchMap.put("teamName", e.getTeamName());
        List<Integer> playerIds = playerMapper.selectJoinkfaPlayerIdOwnGoal(playerSearchMap);
        if (playerIds.size() == 1) e.setPlayerId(playerIds.get(0));
    }

    private void setPlayerIdFromChangeData(MatchPlayDataDto playData, String convertLevel, ChangeDataDto e, String homeAwayGbn, int ord) {
        Map<String, String> playerSearchMap = new HashMap<>();
        String inPlayerPosition = "";
        String outPlayerPosition = "";
        if (homeAwayGbn.equals("home")) {
            inPlayerPosition = getHomeInPlayerPosition(playData, e);
            if (!convertLevel.equals("ES")) {
                /*초등경기에는 in 밖에 없음*/
                outPlayerPosition = getHomeOutPlayerPosition(playData, e);
            }
        } else if (homeAwayGbn.equals("away")) {
            inPlayerPosition = getAwayInPlayerPosition(playData, e);
            if (!convertLevel.equals("ES")) {
                outPlayerPosition = getAwayOutPlayerPosition(playData, e);
            }
        }


        playerSearchMap.put("level", convertLevel);
        playerSearchMap.put("position", inPlayerPosition);
        playerSearchMap.put("playerName", e.getInPlayerName());
        playerSearchMap.put("ord", String.valueOf(ord));
        playerSearchMap.put("teamName", e.getTeamName());
        List<Integer> inPlayerIds = playerMapper.selectJoinkfaPlayerId(playerSearchMap);
        /*선수명단에 있으면 선수명단 아이디 넣기*/
        if (inPlayerIds.size() == 1) e.setInPlayerId(inPlayerIds.get(0));

        playerSearchMap.put("level", convertLevel);
        playerSearchMap.put("position", outPlayerPosition);
        playerSearchMap.put("playerName", e.getOutPlayerName());
        playerSearchMap.put("ord", String.valueOf(ord));
        playerSearchMap.put("teamName", e.getTeamName());
        List<Integer> outPlayerIds = playerMapper.selectJoinkfaPlayerId(playerSearchMap);
        /*선수명단에 있으면 선수명단 아이디 넣기*/
        if (outPlayerIds.size() == 1) e.setOutPlayerId(outPlayerIds.get(0));
    }

    private String getAwayInPlayerPosition(MatchPlayDataDto playData, ChangeDataDto e) {
        PlayDataDto playCandidateDataDto = playData.getAwayPlayCandidateData()
            .stream()
            .filter(d -> d.getPlayerNumber().equals(e.getInPlayerNumber()))
            .findFirst().orElse(null);
        if (null == playCandidateDataDto) {
            playCandidateDataDto = playData.getAwayPlaySelectionData()
                .stream()
                .filter(d -> d.getPlayerNumber().equals(e.getInPlayerNumber()))
                .findFirst().get();
        }
        return playCandidateDataDto.getPosition();
    }

    private String getAwayOutPlayerPosition(MatchPlayDataDto playData, ChangeDataDto e) {
        PlayDataDto playSelectionDataDto = playData.getAwayPlaySelectionData()
            .stream()
            .filter(d -> d.getPlayerNumber().equals(e.getOutPlayerNumber()))
            .findFirst().orElse(null);
        if (null == playSelectionDataDto) {
            playSelectionDataDto = playData.getAwayPlayCandidateData()
                .stream()
                .filter(d -> d.getPlayerNumber().equals(e.getOutPlayerNumber()))
                .findFirst().get();
        }
        return playSelectionDataDto.getPosition();
    }

    private String getHomeInPlayerPosition(MatchPlayDataDto playData, ChangeDataDto e) {
        PlayDataDto playCandidateDataDto = playData.getHomePlayCandidateData()
            .stream()
            .filter(d -> d.getPlayerNumber().equals(e.getInPlayerNumber()))
            .findFirst().orElse(null);
        if (null == playCandidateDataDto) {
            playCandidateDataDto = playData.getHomePlaySelectionData()
                .stream()
                .filter(d -> d.getPlayerNumber().equals(e.getInPlayerNumber()))
                .findFirst().get();
        }
        return playCandidateDataDto.getPosition();
    }

    private String getHomeOutPlayerPosition(MatchPlayDataDto playData, ChangeDataDto e) {
        PlayDataDto playSelectionDataDto = playData.getHomePlaySelectionData()
            .stream()
            .filter(d -> d.getPlayerNumber().equals(e.getOutPlayerNumber()))
            .findFirst().orElse(null);
        if (null == playSelectionDataDto) {
            playSelectionDataDto = playData.getHomePlayCandidateData()
                .stream()
                .filter(d -> d.getPlayerNumber().equals(e.getOutPlayerNumber()))
                .findFirst().get();
        }
        return playSelectionDataDto.getPosition();
    }

    private String getLevel(String strValue) {
        switch (strValue) {
            case "51":
                return "ES";
            case "52":
                return "MS";
            case "53":
                return "HS";
            case "54":
                return "UV";
            default:
                return "";
        }
    }
}
