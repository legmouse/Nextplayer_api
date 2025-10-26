package kr.co.nextplayer.base.backend.excel;

import kr.co.nextplayer.base.backend.util.StrUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ExcelService {

    private static final int MAX_ROW = 1040000;

    public void createExcel(List<HashMap<String, Object>> data, HttpServletResponse response) {
        SXSSFWorkbook sxssfWorkbook = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            sxssfWorkbook = getWorkBook(data, 2, sxssfWorkbook);
            //response.setContentType("application/vnd.ms-excel");
            //response.setHeader("Content-Disposition", "attachment;filename=" + sheetNm + ".xlsx");
            response.setContentType("ms-vnd/excel");
            response.setHeader("Content-Disposition", "attachment; filename=\"".concat(URLEncoder.encode(data.get(0).get("1").toString().concat("_").concat(sdf.format(new Date())),"UTF-8")).concat(".xlsx\";"));

            ServletOutputStream output = response.getOutputStream();
            output.flush();
            sxssfWorkbook.write(output);
            output.flush();
            output.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createExcelDownloadData(Map<String, String> params, List<HashMap<String, Object>> data, HttpServletResponse response) {
        SXSSFWorkbook sxssfWorkbook = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            if (params.get("excelFlag").equals("educationQuestion")) {
                sxssfWorkbook = getWorkBookEnterData(params, data, 2, sxssfWorkbook);
            } else {
                sxssfWorkbook = getWorkBook(params, data, 2, sxssfWorkbook);
            }
            //response.setContentType("application/vnd.ms-excel");
            //response.setHeader("Content-Disposition", "attachment;filename=" + sheetNm + ".xlsx");
            response.setContentType("ms-vnd/excel");

            if (params.get("excelFlag").equals("teamList") || params.get("excelFlag").equals("teamMgrList")) {
                String area = params.get("sArea");
                String teamType = params.get("sTeamType");

                if (area.equals("-1")) {
                    area = "";
                }

                if (teamType.equals("-1")) {
                    teamType = "";
                } else if (teamType.equals("0")) {
                    teamType = "학원";
                } else if (teamType.equals("1")) {
                    teamType = "클럽";
                } else if (teamType.equals("2")) {
                    teamType = "유스";
                }

                response.setHeader("Content-Disposition", "attachment; filename=\"".concat(URLEncoder.encode(params.get("ageGroup")
                        .concat("_목록_").concat(sdf.format(new Date())),"UTF-8")).concat(".xlsx\";"));
            }

            if (params.get("excelFlag").equals("teamGroupList")) {
                //response.setHeader("Content-Disposition", "attachment; filename=\"".concat(URLEncoder.encode(params.get("ageGroup").concat("팀_그룹_목록_").concat(sdf.format(new Date())),"UTF-8")).concat(".xlsx\";"));
                response.setHeader("Content-Disposition", "attachment; filename=\"".concat(URLEncoder.encode("팀_그룹_목록_".concat(sdf.format(new Date())),"UTF-8")).concat(".xlsx\";"));
            }

            if (params.get("excelFlag").equals("memberList")) {
                response.setHeader("Content-Disposition", "attachment; filename=\"".concat(URLEncoder.encode("회원_목록_".concat(sdf.format(new Date())),"UTF-8")).concat(".xlsx\";"));
            }

            if (params.get("excelFlag").equals("educationQuestion")) {
                response.setHeader("Content-Disposition", "attachment; filename=\"".concat(URLEncoder.encode("질문_목록_".concat(sdf.format(new Date())),"UTF-8")).concat(".xlsx\";"));
            }

            ServletOutputStream output = response.getOutputStream();
            output.flush();
            sxssfWorkbook.write(output);
            output.flush();
            output.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private SXSSFWorkbook getWorkBook(List<HashMap<String, Object>> data, int rowIdx, SXSSFWorkbook sxssfWorkbook) throws Exception {
        int cellIdx = 0;
        Iterator<String> key = null;
        SXSSFWorkbook workbook = !ObjectUtils.isEmpty(sxssfWorkbook) ? sxssfWorkbook : new SXSSFWorkbook(-1);

        String testNm = "Sheet";

        Sheet sheet = ObjectUtils.isEmpty(workbook.getSheet(testNm)) ? workbook.createSheet(testNm) : workbook.getSheet(testNm);

        Row row = null;
        Cell cell = null;

        int rowNo = rowIdx % MAX_ROW;

        Row titleRow = sheet.createRow(1);

        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("리그명");

        Cell titleCell2 = titleRow.createCell(1);
        titleCell2.setCellValue("경기일");

        Cell titleCell3 = titleRow.createCell(2);
        titleCell3.setCellValue("경기장소");

        Cell titleCell4 = titleRow.createCell(3);
        titleCell4.setCellValue("홈팀명");

        Cell titleCell5 = titleRow.createCell(4);
        titleCell5.setCellValue("어웨이팀명");

        Cell titleCell6 = titleRow.createCell(5);
        titleCell6.setCellValue("홈팀");

        Cell titleCell7 = titleRow.createCell(6);
        titleCell7.setCellValue("어웨이팀");

        Cell titleCell8 = titleRow.createCell(7);
        titleCell8.setCellValue("결과");

        Cell titleCell9 = titleRow.createCell(8);
        titleCell9.setCellValue("사유");

        for(Map<String, Object> map : data){

            //값세팅
            cellIdx = 0;
            row = sheet.createRow(rowIdx++);
            key = map.keySet().iterator();
            while(key.hasNext()){
                cell = row.createCell(cellIdx++);
                cell.setCellValue(String.valueOf(map.get(key.next())));
            }

            if (rowNo % 100 == 0) {
                ((SXSSFSheet) sheet).flushRows(100);
            }
        }
        //((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
        for(int i=0; i<cellIdx; i++){
            //sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, Math.min(24 * 256, sheet.getColumnWidth(i) + (short)512));
        }

        return workbook;

    }

    private SXSSFWorkbook getWorkBook(Map<String, String> params, List<HashMap<String, Object>> data, int rowIdx, SXSSFWorkbook sxssfWorkbook) throws Exception {
        int cellIdx = 0;
        Iterator<String> key = null;
        SXSSFWorkbook workbook = !ObjectUtils.isEmpty(sxssfWorkbook) ? sxssfWorkbook : new SXSSFWorkbook(-1);

        String testNm = "Sheet";

        Sheet sheet = ObjectUtils.isEmpty(workbook.getSheet(testNm)) ? workbook.createSheet(testNm) : workbook.getSheet(testNm);

        Row row = null;
        Cell cell = null;

        int rowNo = rowIdx % MAX_ROW;

        Row titleRow = sheet.createRow(1);

        if (data.size() > 0) {
            for(Map<String, Object> map : data){

                //제목세팅
                if(key == null){
                    row = sheet.createRow(rowIdx++);
                    key = map.keySet().iterator();

                    while(key.hasNext()){
                        cell = row.createCell(cellIdx++);
                        cell.setCellValue(key.next());
                    }
                }

                //값세팅
                cellIdx = 0;
                row = sheet.createRow(rowIdx++);
                key = map.keySet().iterator();
                while(key.hasNext()){
                    cell = row.createCell(cellIdx++);
                    cell.setCellValue(String.valueOf(map.get(key.next())));
                }

                if (rowNo % 100 == 0) {
                    ((SXSSFSheet) sheet).flushRows(100);
                }
            }
            //((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
            for(int i=0; i<cellIdx; i++){
                //sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, Math.min(24 * 256, sheet.getColumnWidth(i) + (short)512));
            }
        }

        return workbook;

    }

    private SXSSFWorkbook getWorkBookEnterData(Map<String, String> params, List<HashMap<String, Object>> data, int rowIdx, SXSSFWorkbook sxssfWorkbook) throws Exception {
        int cellIdx = 0;
        Iterator<String> key = null;
        SXSSFWorkbook workbook = !ObjectUtils.isEmpty(sxssfWorkbook) ? sxssfWorkbook : new SXSSFWorkbook(-1);

        String testNm = "Sheet";

        Sheet sheet = ObjectUtils.isEmpty(workbook.getSheet(testNm)) ? workbook.createSheet(testNm) : workbook.getSheet(testNm);

        Row row = null;
        Cell cell = null;

        int rowNo = rowIdx % MAX_ROW;

        Row titleRow = sheet.createRow(1);

        if (data.size() > 0) {
            for(Map<String, Object> map : data){

                //제목세팅
                if(key == null){
                    row = sheet.createRow(rowIdx++);
                    key = map.keySet().iterator();

                    while(key.hasNext()){
                        cell = row.createCell(cellIdx++);
                        cell.setCellValue(key.next());
                    }
                }

                //값세팅
                cellIdx = 0;
                row = sheet.createRow(rowIdx++);
                key = map.keySet().iterator();
                while (key.hasNext()) {
                    cell = row.createCell(cellIdx++);
                    Object value = map.get(key.next());
                    if (value instanceof String) {
                        // 개행 문자를 포함한 문자열을 하나의 셀에 입력
                        cell.setCellValue((String)value);

                        // 셀에 개행 적용
                        CellStyle cellStyle = workbook.createCellStyle();
                        cellStyle.setWrapText(true);
                        cell.setCellStyle(cellStyle);
                    } else {
                        cell.setCellValue(String.valueOf(value));
                    }
                }

                if (rowNo % 100 == 0) {
                    ((SXSSFSheet) sheet).flushRows(100);
                }
            }
            //((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
            for(int i=0; i<cellIdx; i++){
                //sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, Math.min(24 * 256, sheet.getColumnWidth(i) + (short)512));
            }
        }

        return workbook;

    }

}
