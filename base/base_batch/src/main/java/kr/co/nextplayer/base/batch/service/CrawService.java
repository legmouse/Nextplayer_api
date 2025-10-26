package kr.co.nextplayer.base.batch.service;

import kr.co.nextplayer.base.batch.dto.joinkfaCraw.MatchPlayDataDto;
import kr.co.nextplayer.base.batch.enums.AgeGroup;
import kr.co.nextplayer.base.batch.enums.MatchType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@EnableAsync
@RequiredArgsConstructor
@Slf4j
public class CrawService {

    private final CupService cupService;

    private String API_URL = "http://210.120.112.49:8088";
//    private String API_URL = "http://localhost";

    @Async
    public void getAllPlayData(AgeGroup ageGroup,String level, String gameType, MatchType matchType, String title, String sDate, String matchList) {
        ResponseEntity<List<MatchPlayDataDto>> responseEntity = null;
        int retryCount = 0;
        int maxRetries = 5;

        while (retryCount < maxRetries) {
            try {
                URI uri = UriComponentsBuilder
                    .fromUriString(API_URL) // http://localhost에 호출
                    .path("/api/joinkfaAllPlayData")
                    .queryParam("level", level)
                    .queryParam("matchType", gameType)
                    .queryParam("title", title)
                    .queryParam("sDate", sDate)
                    .queryParam("targetMatchOrders", matchList)
                    .encode()
                    .build()
                    .toUri();
                log.debug("craw_uri : {}", uri);
                RestTemplate restTemplate = new RestTemplate();
                responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<MatchPlayDataDto>>() {}
                );

                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    break;
                }

            } catch (HttpStatusCodeException e) {
                if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                    log.error("500 error occurred. Retry count: {}", retryCount + 1);
                    retryCount++;
                    if (retryCount >= maxRetries) {
                        log.error("Max retries reached. Unable to fetch data.");
                        break;
                    }
                } else {
                    e.printStackTrace();
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }

        if (responseEntity == null) return;

        List<MatchPlayDataDto> body = responseEntity.getBody();
        cupService.updateCupMatchInfo(body, ageGroup, level, matchType);

    }

    @Async
    public void getAllMatchScoreData(AgeGroup ageGroup, int cupId, String level, String gameType, MatchType matchType, String title, String sDate, String matchList) {
        ResponseEntity<List<MatchPlayDataDto>> responseEntity = null;
        int retryCount = 0;
        int maxRetries = 5;

        while (retryCount < maxRetries) {
            try {
                URI uri = UriComponentsBuilder
                    .fromUriString(API_URL) // http://localhost에 호출
                    .path("/api/joinkfaAllMatchScoreToday")
                    .queryParam("level", level)
                    .queryParam("matchType", gameType)
                    .queryParam("title", title)
                    .queryParam("sDate", sDate)
                    .queryParam("targetMatchOrders", matchList)
                    .encode()
                    .build()
                    .toUri();
                log.debug("craw_uri : {}", uri);
                RestTemplate restTemplate = new RestTemplate();
                responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<MatchPlayDataDto>>() {}
                );

                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    break;
                }

            } catch (HttpStatusCodeException e) {
                if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                    log.error("500 error occurred. Retry count: {}", retryCount + 1);
                    retryCount++;
                    if (retryCount >= maxRetries) {
                        log.error("Max retries reached. Unable to fetch data.");
                        break;
                    }
                } else {
                    e.printStackTrace();
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }

        if (responseEntity == null) return;

        List<MatchPlayDataDto> body = responseEntity.getBody();
        cupService.updateCupScore(body, matchType, ageGroup, cupId);

    }
}
