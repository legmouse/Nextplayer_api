package kr.co.nextplayer.base.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nextplayer.base.backend.dto.joinkfaCraw.GameDto;
import kr.co.nextplayer.base.backend.dto.joinkfaCraw.MatchDto;
import kr.co.nextplayer.base.backend.dto.joinkfaCraw.MatchPlayDataDto;
import kr.co.nextplayer.base.backend.mapper.CrawMapper;
import kr.co.nextplayer.base.backend.mapper.CupMapper;
import kr.co.nextplayer.base.backend.mapper.LeagueMapper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CrawService {

    @Resource
    private CrawMapper crawMapper;

    @Resource
    private CupMapper cupMapper;
    
    @Resource
    private LeagueMapper leagueMapper;

    // private String API_URL = "http://210.120.112.49:8088";
    private String API_URL = "http://192.168.45.117:8088";

    public List<GameDto> getGameTitle(String level, String matchType) {
        List<GameDto> result = new ArrayList<>();

        switch (matchType) {
            case "MATCH":
                switch (level) {
                    case "51":
                        result.addAll(crawMapper.selectElementarySchoolCupTitle());
                        break;
                    case "52":
                        result.addAll(crawMapper.selectMiddleSchoolCupTitle());
                        break;
                    case "53":
                        result.addAll(crawMapper.selectHighSchoolCupTitle());
                        break;
                    case "54":
                        result.addAll(crawMapper.selectUniversityCupTitle());
                        break;
                }
                break;
            case "LEAGUE2":
                switch (level) {
                    case "1":
                        result.addAll(crawMapper.selectElementarySchoolLeagueTitle());
                        break;
                    case "2":
                        result.addAll(crawMapper.selectMiddleSchoolLeagueTitle());
                        break;
                    case "3":
                        result.addAll(crawMapper.selectHighSchoolLeagueTitle());
                        break;
                    case "5":
                        result.addAll(crawMapper.selectUniversityLeagueTitle());
                        break;
                }
                break;
        }

        return result;
    }

    public List<GameDto> getGame(String level, String matchType) {
        // uri 주소 생성
        URI uri = UriComponentsBuilder
            .fromUriString(API_URL) //http://localhost에 호출
            .path("/api/joinkfaGame")
            .queryParam("level", level)
            .queryParam("matchType", matchType)
            .encode()
            .build()
            .toUri();

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<List<GameDto>> responseEntity = restTemplate.exchange(
            uri,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<GameDto>>() {}
        );

        return responseEntity.getBody();
    }

    public List<MatchDto> getMatchLeague(String level, String matchType, String title) {

        // uri 주소 생성
        URI uri = UriComponentsBuilder
            .fromUriString(API_URL) //http://localhost에 호출
            .path("/api/joinkfaMatch")
            .queryParam("level", level)
            .queryParam("matchType", matchType)
            .queryParam("title", title)
            .encode()
            .build()
            .toUri();

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<List<MatchDto>> responseEntity = restTemplate.exchange(
            uri,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<MatchDto>>() {}
        );

        return responseEntity.getBody();
    }

    public void getMatchExcelLeague(String level, String matchType, String title, HttpServletResponse res) throws IOException {

        List<MatchDto> resultList = new ArrayList<>();
        try {
            // uri 주소 생성
            URI uri = UriComponentsBuilder
                .fromUriString(API_URL) //http://localhost에 호출
                .path("/api/joinkfaMatch")
                .queryParam("level", level)
                .queryParam("matchType", matchType)
                .queryParam("title", title)
                .encode()
                .build()
                .toUri();

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<List<MatchDto>> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MatchDto>>() {}
            );

            resultList = responseEntity.getBody();
        } catch (Exception e) {
            res.sendRedirect("/team");
        }

        if (resultList != null && !resultList.isEmpty()) {

            final String fileName = title + ".xlsx";

            /* 엑셀 그리기 */
            final String[] colNames = {
                "순번", "리그명", "경기일", "경기장소", "홈팀명", "어웨이팀명", "홈팀", "어웨이팀", "결과", "사유"
            };

            // 헤더 사이즈
            final int[] colWidths = {
                1400, 17000, 8000, 8000, 8000, 8000, 2000, 2000, 2000, 2000
            };

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = null;
            XSSFCell cell = null;
            XSSFRow row = null;

            CellStyle titleStyle = workbook.createCellStyle();
            CellStyle style = workbook.createCellStyle();

            //제목스타일
            Font font = workbook.createFont();
            font.setColor(IndexedColors.WHITE.index);
            font.setBold(true);
            titleStyle.setFillForegroundColor(IndexedColors.BLACK.index);
            titleStyle.setFont(font);
            titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setBorderLeft(BorderStyle.THIN);
            titleStyle.setBorderRight(BorderStyle.THIN);
            titleStyle.setBorderTop(BorderStyle.THIN);
            titleStyle.setBorderBottom(BorderStyle.THIN);

            //일반셀스타일
            style.setAlignment(HorizontalAlignment.LEFT);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);

            //rows
            int rowCnt = 0;
            int cellCnt = 0;

            // 엑셀 시트명 설정
            sheet = workbook.createSheet("Sheet1");
            sheet.createRow(rowCnt++);
            row = sheet.createRow(rowCnt++);
            //헤더 정보 구성
            for (int i = 0; i < colNames.length; i++) {
                cell = row.createCell(i);
                cell.setCellStyle(titleStyle);
                cell.setCellValue(colNames[i]);
                sheet.setColumnWidth(i, colWidths[i]);    //column width 지정
            }

            //데이터 부분 생성
            for (MatchDto vo : resultList) {
                cellCnt = 0;
                row = sheet.createRow(rowCnt++);

                cell = row.createCell(cellCnt++);
                cell.setCellStyle(style);
                cell.setCellValue(vo.getMatchOrder());

                cell = row.createCell(cellCnt++);
                cell.setCellStyle(style);
                cell.setCellValue(title);

                cell = row.createCell(cellCnt++);
                cell.setCellStyle(style);
                cell.setCellValue(vo.getMatchDateFormat());

                cell = row.createCell(cellCnt++);
                cell.setCellStyle(style);
                cell.setCellValue(vo.getMatchPlace());


                cell = row.createCell(cellCnt++);
                cell.setCellStyle(style);
                cell.setCellValue(vo.getHomeTeam());


                cell = row.createCell(cellCnt++);
                cell.setCellStyle(style);
                cell.setCellValue(vo.getAwayTeam());


                cell = row.createCell(cellCnt++);
                cell.setCellStyle(style);
                cell.setCellValue(vo.getHomeScore());

                cell = row.createCell(cellCnt++);
                cell.setCellStyle(style);
                cell.setCellValue(vo.getAwayScore());


                cell = row.createCell(cellCnt++);
                cell.setCellStyle(style);
                cell.setCellValue(getResultCode(vo.getScoreType()));

                cell = row.createCell(cellCnt++);
                cell.setCellStyle(style);
                cell.setCellValue("");
            }
            res.setContentType("application/vnd.ms-excel");
            // 엑셀 파일명 설정
            String outputFileName = new String(fileName.getBytes("KSC5601"), "8859_1");
            res.setHeader("Content-Disposition", "attachment;filename=" + outputFileName);
            try {
                ServletOutputStream output = res.getOutputStream();
                output.flush();
                workbook.write(output);
                output.flush();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public MatchPlayDataDto getPlayData(String level, String matchType, String title, int targetMatchOrder, String sDate) {

        // uri 주소 생성
        URI uri = UriComponentsBuilder
            .fromUriString(API_URL) //http://localhost에 호출
            .path("/api/joinkfaPlayData")
            .queryParam("level", level)
            .queryParam("matchType", matchType)
            .queryParam("title", title)
            .queryParam("targetMatchOrder", targetMatchOrder)
            .queryParam("sDate", sDate)
            .encode()
            .build()
            .toUri();

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<MatchPlayDataDto> responseEntity = restTemplate.exchange(
            uri,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<MatchPlayDataDto>() {}
        );

        return responseEntity.getBody();
    }

    public List<MatchPlayDataDto> getAllPlayData(String level, String ageGroup, String cupType, String matchType,
                               String foreignId, String title, String sDate, String sTime, String sMinute) {

        List<MatchPlayDataDto> resultDto = new ArrayList<>();

        Map<String, String> param = new HashMap<>();
        String matchTB = "";

        ResponseEntity<List<MatchPlayDataDto>> responseEntity = null;

        if (matchType.equals("MATCH")) {
            switch (cupType) {
                case "SUB" : matchTB = ageGroup + "_Cup_Sub_Match"; break;
                case "MAIN" : matchTB = ageGroup + "_Cup_Main_Match"; break;
                case "TOUR" : matchTB = ageGroup + "_Cup_Tour_Match"; break;
            }
            param.put("matchTB", matchTB);
            param.put("cupType", cupType);
            param.put("cupId", foreignId);
            param.put("sDate", sDate);
            param.put("sTime", sTime);
            param.put("sMinute", sMinute);

            List<HashMap<String, Object>> matchList = cupMapper.selectSearchMatchOrderForPlayDataCraw(param);
            System.out.println("matchList = " + matchList);
            List<String> matchOrder = matchList.stream().map(data -> data.get("match_order").toString()).collect(Collectors.toList());

            try {

                if (matchList.size() > 0) {
                        URI uri = UriComponentsBuilder
                            .fromUriString(API_URL) //http://localhost에 호출
                            .path("/api/joinkfaAllPlayData")
                            .queryParam("level", level)
                            .queryParam("matchType", matchType)
                            .queryParam("title", title)
                            .queryParam("sDate", sDate)
                            .queryParam("sTime", sTime)
                            //.queryParam("targetMatchOrders", objectMapper.writeValueAsString(matchList))
                            .queryParam("targetMatchOrders", encodeMatchList(matchList))
                            .encode()
                            .build()
                            .toUri();


                        RestTemplate restTemplate = new RestTemplate();
                        responseEntity = restTemplate.exchange(
                            uri,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<List<MatchPlayDataDto>>() {}
                        );
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (matchType.equals("LEAGUE2")) {
            matchTB = ageGroup + "_League_Match";
            param.put("matchTB", matchTB);
            param.put("sDate", sDate);
            param.put("leagueId", foreignId);
            
            List<HashMap<String, Object>> matchList = leagueMapper.selectSearchMatchOrderForPlayDataCraw(param);
            System.out.println("matchList = " + matchList);

            try {

                if (matchList.size() > 0) {
                        URI uri = UriComponentsBuilder
                            .fromUriString(API_URL) //http://localhost에 호출
                            .path("/api/joinkfaAllPlayData")
                            .queryParam("level", level)
                            .queryParam("matchType", matchType)
                            .queryParam("title", title)
                            .queryParam("sDate", sDate)
                            .queryParam("sTime", sTime)
                            .queryParam("targetMatchOrders", encodeMatchList(matchList))
                            .encode()
                            .build()
                            .toUri();


                        RestTemplate restTemplate = new RestTemplate();
                        responseEntity = restTemplate.exchange(
                            uri,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<List<MatchPlayDataDto>>() {}
                        );
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return responseEntity.getBody();
    }
    
    public List<MatchPlayDataDto> getAllMatchScoreData(String level, String ageGroup, String cupType, String matchType,
            String foreignId, String title, String sDate) {

    	List<MatchPlayDataDto> resultDto = new ArrayList<>();

    	Map<String, String> param = new HashMap<>();
    	String matchTB = "";

    	ResponseEntity<List<MatchPlayDataDto>> responseEntity = null;

    	if (matchType.equals("MATCH")) {
    		switch (cupType) {
    			case "SUB" : matchTB = ageGroup + "_Cup_Sub_Match"; break;
    			case "MAIN" : matchTB = ageGroup + "_Cup_Main_Match"; break;
    			case "TOUR" : matchTB = ageGroup + "_Cup_Tour_Match"; break;
    		}
    		
    		param.put("matchTB", matchTB);
    		param.put("cupType", cupType);
    		param.put("cupId", foreignId);

    		List<HashMap<String, Object>> matchList = cupMapper.selectSearchMatchOrderForPlayDataCraw(param);
    		System.out.println("matchList = " + matchList);

    		try {

    			if (matchList.size() > 0) {
    				URI uri = UriComponentsBuilder
    						.fromUriString(API_URL) //http://localhost에 호출
    						.path("/api/joinkfaAllMatchScore")
    						.queryParam("level", level)
    						.queryParam("matchType", matchType)
    						.queryParam("title", title)
    						.queryParam("sDate", sDate)
    						.queryParam("targetMatchOrders", encodeMatchList(matchList))
    						.encode()
    						.build()
    						.toUri();
    				System.out.println("uri = " + uri);
    				RestTemplate restTemplate = new RestTemplate();
    				responseEntity = restTemplate.exchange(
    						uri,
    						HttpMethod.GET,
    						null,
    						new ParameterizedTypeReference<List<MatchPlayDataDto>>() {}
    						);
    				System.out.println("encodeMatchList(matchList) = " + encodeMatchList(matchList));
    			} else {
    				return null;
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}

    	} else if (matchType.equals("LEAGUE2")) {
    		matchTB = ageGroup + "_League_Match";
            param.put("matchTB", matchTB);
            param.put("leagueId", foreignId);
            
            List<HashMap<String, Object>> matchList = leagueMapper.selectSearchMatchOrderForPlayDataCraw(param);
            System.out.println("matchList = " + matchList);

            try {

                if (matchList.size() > 0) {
                        URI uri = UriComponentsBuilder
                            .fromUriString(API_URL) //http://localhost에 호출
                            .path("/api/joinkfaAllMatchScore")
                            .queryParam("level", level)
                            .queryParam("matchType", matchType)
                            .queryParam("title", title)
                            .queryParam("sDate", sDate)
                            .queryParam("targetMatchOrders", encodeMatchList(matchList))
                            .encode()
                            .build()
                            .toUri();


                        RestTemplate restTemplate = new RestTemplate();
                        responseEntity = restTemplate.exchange(
                            uri,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<List<MatchPlayDataDto>>() {}
                        );
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    	}
    	return responseEntity.getBody();
    }

    private String encodeMatchList(List<HashMap<String, Object>> matchList) throws UnsupportedEncodingException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(matchList);
        return URLEncoder.encode(jsonString, StandardCharsets.UTF_8.toString());
    }

    private int getResultCode(String strValue) {
        switch (strValue) {
            case "정규":
                return 0;
            case "승부차기":
                return 1;
            case "홈팀부전승":
                return 2;
            case "어웨이팀부전승":
                return 3;
            case "홈팀몰수패":
                return 4;
            case "어웨이팀몰수패":
                return 5;
            default:
                return 6;
        }
    }
}
