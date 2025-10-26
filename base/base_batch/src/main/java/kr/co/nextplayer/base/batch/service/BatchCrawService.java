package kr.co.nextplayer.base.batch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nextplayer.base.batch.dto.CupCrawMatchList;
import kr.co.nextplayer.base.batch.enums.MatchType;
import kr.co.nextplayer.base.batch.mapper.CrawMapper;
import kr.co.nextplayer.next.lib.common.util.LocalDateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchCrawService {

    private final CrawMapper crawMapper;
    private final CrawService crawService;


    public void scoreCraw() {
        String[] ageList = {"u11", "u12", "u14", "u15", "u17", "u18", "u20", "u22"};

        for (String age : ageList) {
            List<CupCrawMatchList> matchList = crawMapper.selectCrawMatchList(age);
            if (matchList.isEmpty()) continue;
            List<CupCrawMatchList> mainList = matchList.stream()
                .filter(f -> f.getMatchType() == MatchType.MAIN)
                .collect(Collectors.toList());
            List<CupCrawMatchList> subList = matchList.stream()
                .filter(f -> f.getMatchType() == MatchType.SUB)
                .collect(Collectors.toList());
            List<CupCrawMatchList> tourList = matchList.stream()
                .filter(f -> f.getMatchType() == MatchType.TOUR)
                .collect(Collectors.toList());
            if (!mainList.isEmpty()) {
                Map<String, List<Map<String, Integer>>> groupedMatches = mainList.stream()
                    .collect(Collectors.groupingBy(
                        match -> match.getCupId() + "," + match.getTitle() + "," + match.getMatchType(),
                        LinkedHashMap::new,
                        Collectors.mapping(match -> {
                            Map<String, Integer> map = new HashMap<>();
                            map.put("match_id", match.getMatchId());
                            map.put("match_order", match.getMatchOrder());
                            return map;
                        }, Collectors.toList())
                    ));
                groupedMatches.forEach((group, mapList) -> {
                    crawService.getAllMatchScoreData(matchList.get(0).getAgeGroup()
                        , Integer.parseInt(group.split(",")[0])
                        , matchList.get(0).getAgeGroup().getLevel()
                        , "MATCH"
                        , MatchType.valueOf(group.split(",")[2])
                        , group.split(",")[1]
                        , LocalDateUtil.getNowDate()
                        , encodeMatchList(mapList));

                });
            }

            if (!subList.isEmpty()) {
                Map<String, List<Map<String, Integer>>> groupedMatches = subList.stream()
                    .collect(Collectors.groupingBy(
                        match -> match.getCupId() + "," + match.getTitle() + "," + match.getMatchType(),
                        LinkedHashMap::new,
                        Collectors.mapping(match -> {
                            Map<String, Integer> map = new HashMap<>();
                            map.put("match_id", match.getMatchId());
                            map.put("match_order", match.getMatchOrder());
                            return map;
                        }, Collectors.toList())
                    ));
                groupedMatches.forEach((group, mapList) -> {
                    crawService.getAllMatchScoreData(matchList.get(0).getAgeGroup()
                        , Integer.parseInt(group.split(",")[0])
                        , matchList.get(0).getAgeGroup().getLevel()
                        , "MATCH"
                        , MatchType.valueOf(group.split(",")[2])
                        , group.split(",")[1]
                        , LocalDateUtil.getNowDate()
                        , encodeMatchList(mapList));

                });
            }

            if (!tourList.isEmpty()) {
                Map<String, List<Map<String, Integer>>> groupedMatches = tourList.stream()
                    .collect(Collectors.groupingBy(
                        match -> match.getCupId() + "," + match.getTitle() + "," + match.getMatchType(),
                        LinkedHashMap::new,
                        Collectors.mapping(match -> {
                            Map<String, Integer> map = new HashMap<>();
                            map.put("match_id", match.getMatchId());
                            map.put("match_order", match.getMatchOrder());
                            return map;
                        }, Collectors.toList())
                    ));
                groupedMatches.forEach((group, mapList) -> {
                    crawService.getAllMatchScoreData(matchList.get(0).getAgeGroup()
                        , Integer.parseInt(group.split(",")[0])
                        , matchList.get(0).getAgeGroup().getLevel()
                        , "MATCH"
                        , MatchType.valueOf(group.split(",")[2])
                        , group.split(",")[1]
                        , LocalDateUtil.getNowDate()
                        , encodeMatchList(mapList));

                });
            }
        }

    }


    public void playDataCraw() {
        String[] ageList = {"u11", "u12", "u14", "u15", "u17", "u18", "u20", "u22"};

        for (String age : ageList) {
            List<CupCrawMatchList> matchList = crawMapper.selectCrawMatchDataList(age);
            if (matchList.isEmpty()) continue;

            //끝난게임 한번만 더 돌리는것 체크
            List<CupCrawMatchList> collect = matchList.stream().filter(
                f -> f.getEndFlag() == 1
            ).collect(Collectors.toList());
            //체크후 또 안돌도록 업데이트
            collect.forEach(match -> {
                HashMap<String, Object> map = new HashMap<>();
                String cupMatchTB;
                switch (match.getMatchType()) {
                    case MAIN:
                        cupMatchTB = StringUtils.lowerCase((match.getAgeGroup().name())) + "_Cup_Main_Match";
                        /*기본적인 매치정보 업데이트*/
                        map.put("cupMatchTB", cupMatchTB);
                        map.put("matchId", match.getMatchId());
                        map.put("fieldNm", "cup_main_match_id");
                        crawMapper.updateAfterEndCnt(map);
                        break;
                    case SUB:
                        cupMatchTB = StringUtils.lowerCase((match.getAgeGroup().name())) + "_Cup_Sub_Match";
                        /*기본적인 매치정보 업데이트*/
                        map.put("cupMatchTB", cupMatchTB);
                        map.put("matchId", match.getMatchId());
                        map.put("fieldNm", "cup_sub_match_id");
                        crawMapper.updateAfterEndCnt(map);
                        break;
                    case TOUR:
                        cupMatchTB = StringUtils.lowerCase((match.getAgeGroup().name())) + "_Cup_Tour_Match";
                        /*기본적인 매치정보 업데이트*/
                        map.put("cupMatchTB", cupMatchTB);
                        map.put("matchId", match.getMatchId());
                        map.put("fieldNm", "cup_tour_match_id");
                        crawMapper.updateAfterEndCnt(map);
                        break;
                }
            });

            List<CupCrawMatchList> mainList = matchList.stream()
                .filter(f -> f.getMatchType() == MatchType.MAIN)
                .collect(Collectors.toList());
            List<CupCrawMatchList> subList = matchList.stream()
                .filter(f -> f.getMatchType() == MatchType.SUB)
                .collect(Collectors.toList());
            List<CupCrawMatchList> tourList = matchList.stream()
                .filter(f -> f.getMatchType() == MatchType.TOUR)
                .collect(Collectors.toList());
            if (!mainList.isEmpty()) {
                Map<String, List<Map<String, Integer>>> groupedMatches = mainList.stream()
                    .collect(Collectors.groupingBy(
                        match -> match.getCupId() + "," + match.getTitle() + "," + match.getMatchType(),
                        LinkedHashMap::new,
                        Collectors.mapping(match -> {
                            Map<String, Integer> map = new HashMap<>();
                            map.put("match_id", match.getMatchId());
                            map.put("match_order", match.getMatchOrder());
                            return map;
                        }, Collectors.toList())
                    ));
                groupedMatches.forEach((group, mapList) -> {
                    crawService.getAllPlayData(matchList.get(0).getAgeGroup()
                        , matchList.get(0).getAgeGroup().getLevel()
                        , "MATCH"
                        , MatchType.valueOf(group.split(",")[2])
                        , group.split(",")[1]
                        , LocalDateUtil.getNowDate()
                        , encodeMatchList(mapList));
                });
            }

            if (!subList.isEmpty()) {
                Map<String, List<Map<String, Integer>>> groupedMatches = subList.stream()
                    .collect(Collectors.groupingBy(
                        match -> match.getCupId() + "," + match.getTitle() + "," + match.getMatchType(),
                        LinkedHashMap::new,
                        Collectors.mapping(match -> {
                            Map<String, Integer> map = new HashMap<>();
                            map.put("match_id", match.getMatchId());
                            map.put("match_order", match.getMatchOrder());
                            return map;
                        }, Collectors.toList())
                    ));
                groupedMatches.forEach((group, mapList) -> {
                    crawService.getAllPlayData(matchList.get(0).getAgeGroup()
                        , matchList.get(0).getAgeGroup().getLevel()
                        , "MATCH"
                        , MatchType.valueOf(group.split(",")[2])
                        , group.split(",")[1]
                        , LocalDateUtil.getNowDate()
                        , encodeMatchList(mapList));
                });
            }

            if (!tourList.isEmpty()) {
                Map<String, List<Map<String, Integer>>> groupedMatches = tourList.stream()
                    .collect(Collectors.groupingBy(
                        match -> match.getCupId() + "," + match.getTitle() + "," + match.getMatchType(),
                        LinkedHashMap::new,
                        Collectors.mapping(match -> {
                            Map<String, Integer> map = new HashMap<>();
                            map.put("match_id", match.getMatchId());
                            map.put("match_order", match.getMatchOrder());
                            return map;
                        }, Collectors.toList())
                    ));
                groupedMatches.forEach((group, mapList) -> {
                    crawService.getAllPlayData(matchList.get(0).getAgeGroup()
                        , matchList.get(0).getAgeGroup().getLevel()
                        , "MATCH"
                        , MatchType.valueOf(group.split(",")[2])
                        , group.split(",")[1]
                        , LocalDateUtil.getNowDate()
                        , encodeMatchList(mapList));
                });
            }
        }

    }


    private String encodeMatchList(List<Map<String, Integer>> matchList) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(matchList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return URLEncoder.encode(jsonString, StandardCharsets.UTF_8);
    }

}
