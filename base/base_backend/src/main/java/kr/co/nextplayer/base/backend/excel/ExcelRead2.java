package kr.co.nextplayer.base.backend.excel;

import kr.co.nextplayer.base.backend.util.DateUtil;
import kr.co.nextplayer.base.backend.util.Define;
import kr.co.nextplayer.base.backend.util.StrUtil;

import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelRead2 {
    @SuppressWarnings("unchecked")
    public static List<Map<String, String>> read(ExcelReadOption excelReadOption) {

        // 엑셀 파일 자체
        // 엑셀파일을 읽어 들인다.
        // FileType.getWorkbook() <-- 파일의 확장자에 따라서 적절하게 가져온다.
        Workbook wb = ExcelFileType.getWorkbook(excelReadOption.getFilePath());

        //	엑셀 파일에서 첫번째 시트를 가지고 온다.
        Sheet sheet = wb.getSheetAt(0);

        System.out.println("Sheet 이름: " + wb.getSheetName(0));
        System.out.println("데이터가 있는 Sheet의 수 :" + wb.getNumberOfSheets());

        // sheet에서 유효한(데이터가 있는) 행의 개수를 가져온다.
        int numOfRows = sheet.getPhysicalNumberOfRows();
        int numOfCells = 0;

        Row row = null;
        Cell cell = null;

        String cellName = "";

        /**
         * 셀별 지정된 문자열 만큼 map 생성 준비
         */
        Map[] maps = new Map[Define.AR_ExcelColumns.length];


        /*
         * 각 Row를 리스트에 담는다. 하나의 Row를 하나의 Map으로 표현되며 List에는 모든 Row가 포함될 것이다.
         */
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();

        String isEnd = "0";

        for (int rowIndex = excelReadOption.getStartRow() - 1; rowIndex <= numOfRows; rowIndex++) {
            /*
             * 워크북에서 가져온 시트에서 rowIndex에 해당하는 Row를 가져온다. 하나의 Row는 여러개의 Cell을 가진다.
             */
            row = sheet.getRow(rowIndex);

            if (row != null) {
                /*
                 * 가져온 Row의 Cell의 개수를 구한다.
                 */
                //numOfCells = row.getPhysicalNumberOfCells();
                numOfCells = excelReadOption.getOutputColumns().size();

                /*
                 * 셀인덱스문자 추출을 위한 index
                 */
                int arIndex = 0;

                /*
                 * cell의 수 만큼 반복한다.
                 */
                for (int cellIndex = 0; cellIndex < numOfCells; cellIndex++) {
                    /*
                     * Row에서 CellIndex에 해당하는 Cell을 가져온다.
                     */
                    cell = row.getCell(cellIndex);
                    //System.out.println("--- cell:"+cell);

                    /*
                     * 현재 Cell의 이름을 가져온다 이름의 예 : A,B,C,D,......
                     */
                    cellName = ExcelCellRef.getName(cell, cellIndex);
                    //System.out.println("--- cellName:"+cellName);
                    /*
                     * 추출 대상 컬럼인지 확인한다 추출 대상 컬럼이 아니라면, for로 다시 올라간다
                     */
                    if (!excelReadOption.getOutputColumns().contains(cellName)) {
                        continue;
                    }

                    /*
                     * Cell별 map에 별도로 데이터를 담는다.
                     * map은 가변으로 생성됨
                     */

                    if(arIndex == cellIndex) {
                        String letter = Define.AR_ExcelColumns[arIndex];
                        //System.out.println("letter:"+letter);

                        if(letter.equals(cellName) && !StrUtil.isEmpty(cell)) {
                            maps[arIndex] = new HashMap<String, String>();

                            /*
                             * Cell별 map에 별도로 데이터를 담는다.
                             */
                            maps[arIndex].put("nick_name", ExcelCellRef.getValue(cell));
                            maps[arIndex].put("groups", String.valueOf(arIndex+1));
                            maps[arIndex].put("teams",  String.valueOf(rowIndex-1));

                            /*
                             * 만들어진 Map객체를 List로 넣는다.
                             */
                            result.add(maps[arIndex]);
                        }
                        arIndex++;
                    }//if map


                }//for cell


            }//for row

        }

//	    System.out.println("----- result:"+ result.toString());
//	    return null;

        return result;

    }

    public static List<Map<String, Object>> readPlayerExcel(MultipartHttpServletRequest request) {

        List<Map<String, Object>> players = new ArrayList<>();

        MultipartFile excelFile = request.getFile("excelFile");

        if(excelFile==null || excelFile.isEmpty()){

            throw new RuntimeException("엑셀파일을 선택 해 주세요.");
        }

        String contentPath = StrUtil.getContentPath();
        File destFile = new File(contentPath+excelFile.getOriginalFilename());

        ExcelReadOption excelReadOption = new ExcelReadOption();
        excelReadOption.setFilePath(destFile.getAbsolutePath());
        excelReadOption.setStartRow(3);

        Workbook wb = ExcelFileType.getWorkbook(excelReadOption.getFilePath());

        int sheetCnt = wb.getNumberOfSheets();

        for (int index = 0; index < sheetCnt; index++) {

            Sheet sheet = wb.getSheetAt(index);

            int numOfRows = sheet.getPhysicalNumberOfRows();

            Row row = null;

            for (int rowIndex = excelReadOption.getStartRow() - 1; rowIndex <= numOfRows; rowIndex++) {

                Map<String, Object> player = new HashMap<>();
                List<Map<String, Object>> teamInfoList = new ArrayList<>();

                row = sheet.getRow(rowIndex);

                if (row != null) {
                    String name = ExcelCellRef.getValue(row.getCell(0));
                    String position = ExcelCellRef.getValue(row.getCell(1));
                    String birthday = ExcelCellRef.getValue(row.getCell(2));
                    String useFlag = ExcelCellRef.getValue(row.getCell(3));

                    if (!StrUtil.isEmpty(name) && !StrUtil.isEmpty(birthday)) {

                        player.put("name", name);
                        player.put("position", position);
                        player.put("birthday", birthday);
                        player.put("useFlag", useFlag);

                        for (int i = 4; i < row.getLastCellNum(); i += 4) {

                            Map<String, Object> teamInfo = new HashMap<>();

                            String year = ExcelCellRef.getValue(row.getCell(i));
                            String teamType = ExcelCellRef.getValue(row.getCell(i + 1));
                            String team = ExcelCellRef.getValue(row.getCell(i + 2));
                            String teamUseFlag = ExcelCellRef.getValue(row.getCell(i + 3));

                            if (StrUtil.isEmpty(year) || StrUtil.isEmpty(teamType) || StrUtil.isEmpty(team)) {
                                continue;
                            }

                            teamInfo.put("year", year);
                            teamInfo.put("teamType", teamType);
                            teamInfo.put("teamName", team);
                            teamInfo.put("useFlag", teamUseFlag);
                            teamInfoList.add(teamInfo);

                        }

                        player.put("teamInfo", teamInfoList);
                        players.add(player);
                    }

                }
            }

        }

        return players;
    }

    public static Map readRosterExcel(MultipartHttpServletRequest request, String rosterType) {
        Map rosterData = new HashMap<>();

        MultipartFile excelFile = request.getFile("excelFile");

        if(excelFile==null || excelFile.isEmpty()){

            throw new RuntimeException("엑셀파일을 선택 해 주세요.");
        }

        String contentPath = StrUtil.getContentPath();
        File destFile = new File(contentPath+excelFile.getOriginalFilename());

        ExcelReadOption excelReadOption = new ExcelReadOption();
        excelReadOption.setFilePath(destFile.getAbsolutePath());
        excelReadOption.setStartRow(3);

        Workbook wb = ExcelFileType.getWorkbook(excelReadOption.getFilePath());

        int sheetCnt = wb.getNumberOfSheets();

        List<Map<String, Object>> playerInfoList = new ArrayList<>();

        for (int i = 0; i < sheetCnt; i++) {
            Sheet sheet = wb.getSheetAt(i);

            int numOfRows = sheet.getPhysicalNumberOfRows();

            Row row = null;

            if (i == 0) {
                Map<String, String> roster = new HashMap<>();
                for (int rowIndex = excelReadOption.getStartRow() - 1; rowIndex <= numOfRows; rowIndex++) {
                    row = sheet.getRow(rowIndex);

                    if (row != null) {
                        String title = ExcelCellRef.getValue(row.getCell(0));
                        String year = ExcelCellRef.getValue(row.getCell(1));

                        if (rosterType.equals("national")) {
                            String ageGroup = ExcelCellRef.getValue(row.getCell(2));
                            roster.put("ageGroup", ageGroup);
                        } else if (rosterType.equals("golden")) {
                            String type = ExcelCellRef.getValue(row.getCell(2));
                            roster.put("type", type);
                        }

                        String useFlag = ExcelCellRef.getValue(row.getCell(3));

                        roster.put("title", title);
                        roster.put("year", year);
                        roster.put("useFlag", useFlag);
                    }

                }
                rosterData.put("rosterParam", roster);
            }

            if (i == 1) {
                Map<String, String> player = new HashMap<>();
                for (int rowIndex = excelReadOption.getStartRow() - 1; rowIndex <= numOfRows; rowIndex++) {
                    row = sheet.getRow(rowIndex);

                    if (row != null) {
                        Map<String, Object> playerInfo = new HashMap<>();

                        String name = ExcelCellRef.getValue(row.getCell(i - 1));
                        String position = ExcelCellRef.getValue(row.getCell((i - 1) + 1));
                        String birthday = ExcelCellRef.getValue(row.getCell((i - 1) + 2));
                        String teamType = ExcelCellRef.getValue(row.getCell((i - 1) + 3));
                        String teamName = ExcelCellRef.getValue(row.getCell((i - 1) + 4));
                        String ageGroup = ExcelCellRef.getValue(row.getCell((i - 1) + 5));

                        if (!StrUtil.isEmpty(name) && !StrUtil.isEmpty(birthday)) {
                            playerInfo.put("name", name);
                            playerInfo.put("position", position);
                            playerInfo.put("birthday", birthday);
                            playerInfo.put("teamType", teamType);
                            playerInfo.put("teamName", teamName);
                            playerInfo.put("ageGroup", ageGroup);
                            playerInfoList.add(playerInfo);
                        }

                    }

                }
                rosterData.put("rosterPlayerParam", playerInfoList);
            }

        }

        return rosterData;
    }

    public static List<Map<String, Object>> readTeamGroupExcel(MultipartHttpServletRequest request) {

        List<Map<String, Object>> teamGroups = new ArrayList<>();

        MultipartFile excelFile = request.getFile("excelFile");

        if(excelFile==null || excelFile.isEmpty()){

            throw new RuntimeException("엑셀파일을 선택 해 주세요.");
        }

        String contentPath = StrUtil.getContentPath();
        File destFile = new File(contentPath+excelFile.getOriginalFilename());

        ExcelReadOption excelReadOption = new ExcelReadOption();
        excelReadOption.setFilePath(destFile.getAbsolutePath());
        excelReadOption.setStartRow(3);

        Workbook wb = ExcelFileType.getWorkbook(excelReadOption.getFilePath());

        int sheetCnt = wb.getNumberOfSheets();

        for (int index = 0; index < sheetCnt; index++) {

            Sheet sheet = wb.getSheetAt(index);

            int numOfRows = sheet.getPhysicalNumberOfRows();

            Row row = null;

            for (int rowIndex = excelReadOption.getStartRow() - 1; rowIndex <= numOfRows; rowIndex++) {

                Map<String, Object> teamGroup = new HashMap<>();
                List<Map<String, Object>> teamInfoList = new ArrayList<>();

                row = sheet.getRow(rowIndex);

                if (row != null) {
                    String groupName = ExcelCellRef.getValue(row.getCell(0));

                    if (StrUtil.isEmpty(groupName)) {
                        continue;
                    }

                    teamGroup.put("groupName", groupName);

                    for (int i = 1; i < row.getLastCellNum(); i += 2) {

                        Map<String, Object> teamInfo = new HashMap<>();

                        String uage = ExcelCellRef.getValue(row.getCell(i));
                        String team = ExcelCellRef.getValue(row.getCell(i + 1));

                        if (StrUtil.isEmpty(uage) || StrUtil.isEmpty(team)) {
                            continue;
                        }

                        teamInfo.put("uage", uage);
                        teamInfo.put("teamName", team);
                        teamInfoList.add(teamInfo);
                    }

                    teamGroup.put("teamInfo", teamInfoList);
                    teamGroups.add(teamGroup);
                }
            }

        }

        return teamGroups;
    }

}
