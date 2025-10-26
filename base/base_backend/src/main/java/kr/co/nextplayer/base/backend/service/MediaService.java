package kr.co.nextplayer.base.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nextplayer.base.backend.dto.BlogResultDto;
import kr.co.nextplayer.base.backend.dto.NewsResultDto;
import kr.co.nextplayer.base.backend.dto.YoutubeResultDto;
import kr.co.nextplayer.base.backend.mapper.MediaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MediaService {

    @Resource
    private MediaMapper mediaMapper;

    public int selectMediaListCnt(Map<String, String> params) {
        return mediaMapper.selectMediaListCnt(params);
    }

    public List<HashMap<String, Object>> selectMediaList(Map<String, String> params) {
        return mediaMapper.selectMediaList(params);
    }

    public HashMap<String, Object> selectMediaInfo(Map<String, String> params) {
        return mediaMapper.selectMediaInfo(params);
    }

    public List<HashMap<String, Object>> selectMediaChildList(Map<String, String> params) {
        return mediaMapper.selectMediaChildList(params);
    }

    @Transactional
    public int insertMedia(Map<String, String> params) {

        String orgMediaType = params.get("mediaType");

        if (params.get("method").equals("regist")) {
            if(orgMediaType.equals("Game")) {
                params.put("mediaType", "Video");
                params.put("subType", "0");
            }
            int insertMedia = mediaMapper.insertMedia(params);
            if (params.get("mainFlag").equals("1")) {
                params.put("mediaOrder", "1");
                mediaMapper.updateMainMediaOrderPlusAll(params);
                mediaMapper.insertMainMediaData(params);
            }
        }
        int insertMediaCross = 0;

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> matchDataMap = objectMapper.readValue(params.get("matchData"), new TypeReference<Map<String, Object>>() {});
            //Map<String, Object> leagueDataMap = objectMapper.readValue(params.get("leagueData"), new TypeReference<Map<String, Object>>() {});

            List<HashMap<String, Object>> cParmas = new ArrayList<HashMap<String, Object>>();
            List<HashMap<String, Object>> lParmas = new ArrayList<HashMap<String, Object>>();
            List<HashMap<String, Object>> tParmas = new ArrayList<HashMap<String, Object>>();


            List<String> cupDataList = (List<String>) matchDataMap.get("cupData");

            if (cupDataList.size() > 0) {
                if (orgMediaType.equals("Game")) {
                    for (String cupDataItem : cupDataList) {
                        HashMap<String, Object> cupDataItemMap = objectMapper.readValue(cupDataItem, new TypeReference<HashMap<String, Object>>() {});

                        String parentType = cupDataItemMap.get("ageGroup").toString() + "_Cup_Info";
                        String childType = cupDataItemMap.get("ageGroup").toString() + "_" + cupDataItemMap.get("childType").toString();
                        cupDataItemMap.put("parentType", parentType);
                        cupDataItemMap.put("childType", childType);
                        cupDataItemMap.put("useFlag", params.get("useFlag"));
                        cupDataItemMap.put("mediaId", params.get("mediaId"));

                        cParmas.add(cupDataItemMap);
                    }
                }

                if (params.get("mediaType").equals("News")) {
                    for (String cupDataItem : cupDataList) {
                        HashMap<String, Object> cupDataItemMap = objectMapper.readValue(cupDataItem, new TypeReference<HashMap<String, Object>>() {});

                        String parentType = cupDataItemMap.get("ageGroup").toString() + "_Cup_Info";
                        cupDataItemMap.put("parentType", parentType);
                        cupDataItemMap.put("useFlag", params.get("useFlag"));
                        cupDataItemMap.put("mediaId", params.get("mediaId"));

                        cParmas.add(cupDataItemMap);
                    }
                }
                insertMediaCross = mediaMapper.insertMediaCross(cParmas);
            }

            List<String> leagueDataList = (List<String>) matchDataMap.get("leagueData");

            if (leagueDataList.size() > 0) {
                // 미디어 타입이 영상일 경우
                if (orgMediaType.equals("Game")) {
                    for (String leagueDataItem : leagueDataList) {
                        HashMap<String, Object> leagueDataItemMap = objectMapper.readValue(leagueDataItem, new TypeReference<HashMap<String, Object>>() {});

                        String parentType = leagueDataItemMap.get("ageGroup").toString() + "_League_Info";
                        String childType = leagueDataItemMap.get("ageGroup").toString() + "_" + leagueDataItemMap.get("childType").toString();
                        leagueDataItemMap.put("parentType", parentType);
                        leagueDataItemMap.put("childType", childType);
                        leagueDataItemMap.put("useFlag", params.get("useFlag"));
                        leagueDataItemMap.put("mediaId", params.get("mediaId"));

                        lParmas.add(leagueDataItemMap);
                    }
                }
                // 미디어 타입이 뉴스 기사일 경우
                if (params.get("mediaType").equals("News")) {
                    for (String leagueDataItem : leagueDataList) {
                        HashMap<String, Object> leagueDataItemMap = objectMapper.readValue(leagueDataItem, new TypeReference<HashMap<String, Object>>() {});

                        String parentType = leagueDataItemMap.get("ageGroup").toString() + "_League_Info";
                        leagueDataItemMap.put("parentType", parentType);
                        leagueDataItemMap.put("useFlag", params.get("useFlag"));
                        leagueDataItemMap.put("mediaId", params.get("mediaId"));

                        lParmas.add(leagueDataItemMap);
                    }
                }

                insertMediaCross = mediaMapper.insertMediaCross(lParmas);
            }

            List<String> teamDataList = (List<String>) matchDataMap.get("teamData");

            if (teamDataList.size() > 0) {
                for (String teamDataItem : teamDataList) {
                    HashMap<String, Object> teamDataItemMap = objectMapper.readValue(teamDataItem, new TypeReference<HashMap<String, Object>>() {});

                    String parentType = "Team";
                    String childType = teamDataItemMap.get("ageGroup").toString();
                    teamDataItemMap.put("parentType", parentType);
                    teamDataItemMap.put("childType", childType);
                    teamDataItemMap.put("useFlag", params.get("useFlag"));
                    teamDataItemMap.put("mediaId", params.get("mediaId"));

                    tParmas.add(teamDataItemMap);
                }
                insertMediaCross = mediaMapper.insertMediaCross(tParmas);
            }

        } catch (Exception e) {
            // JSON 파싱 오류 처리
            e.printStackTrace();
        }

        return insertMediaCross;
    }

    @Transactional
    public int deleteMedia(Map<String, String> params) {
        int deleteMediaResult = mediaMapper.updateMediaDelete(params);

        return deleteMediaResult;
    }

    @Transactional
    public int updateMedia(Map<String, String> params) {

        int mediaUpdateResult = mediaMapper.updateMedia(params);
        int mediaCrossDeleteResult = mediaMapper.deleteMediaCross(params);

        if (!params.get("bfMainFlag").equals(params.get("mainFlag"))) {
            if (params.get("mainFlag").equals("1")) {
                params.put("mediaOrder", "1");
                mediaMapper.updateMainMediaOrderPlusAll(params);
                mediaMapper.insertMainMediaData(params);
            } else {
                mediaMapper.deleteMainMediaOne(params);
                mediaMapper.updateMainMediaOrderMinusAll(params);
            }
        }

        int updateResult = 0;

        if (mediaUpdateResult > 0) {
            updateResult = insertMedia(params);
        }

        return updateResult;
    }

    public YoutubeResultDto craw_youtube(String youtube_url) {
        // uri 주소 생성
        URI uri = UriComponentsBuilder
            .fromUriString("http://210.120.112.49:8088") //http://localhost에 호출
            .path("/api/youtubeDetail")
            .queryParam("url", youtube_url)  // query parameter가 필요한 경우 이와 같이 사용
            .encode()
            .build()
            .toUri();

        System.out.println(uri.toString());

        RestTemplate restTemplete = new RestTemplate();

        return restTemplete.getForObject(uri, YoutubeResultDto.class);
    }
    public NewsResultDto craw_news(String news_url) {
        // uri 주소 생성
        URI uri = UriComponentsBuilder
            .fromUriString("http://210.120.112.49:8088") //http://localhost에 호출
            .path("/api/news")
            .queryParam("url", news_url)  // query parameter가 필요한 경우 이와 같이 사용
            .encode()
            .build()
            .toUri();

        System.out.println(uri.toString());

        RestTemplate restTemplete = new RestTemplate();

        return restTemplete.getForObject(uri, NewsResultDto.class);
    }

    public BlogResultDto craw_blog(String news_url) {
        // uri 주소 생성
        URI uri = UriComponentsBuilder
            .fromUriString("http://210.120.112.49:8088") //http://localhost에 호출
            .path("/api/blogDetail")
            .queryParam("url", news_url)  // query parameter가 필요한 경우 이와 같이 사용
            .encode()
            .build()
            .toUri();

        System.out.println(uri.toString());

        RestTemplate restTemplete = new RestTemplate();

        return restTemplete.getForObject(uri, BlogResultDto.class);
    }

    public List<Map<String, Object>> selectMainMediaList(Map<String, String> param) {
        return mediaMapper.selectMainMediaList(param);
    }

    public List<HashMap<String, Object>> selectCreatorList(Map<String, String> param) {
        return mediaMapper.selectCreatorList(param);
    }

}
