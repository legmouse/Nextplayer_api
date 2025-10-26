package kr.co.nextplayer.base.backend.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//import kr.enq.np.excel.ExcelReadOption;

import kr.co.nextplayer.base.backend.excel.ExcelReadOption;
import org.json.simple.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



public class StrUtil {

    public static boolean isEmpty(String src) {
    	return (src == null || "".equals(src.trim()) || src.length() == 0 || src == "null");
    }
    
	public static void sendResponseStrMessage(HttpServletResponse response, String szRes) {
		try {
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Content-Type", "text/html; charset=UTF-8");
			response.getOutputStream().write(szRes.getBytes());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendResponseMessage(HttpServletResponse response, JSONObject jsonObj) {
		try {
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Content-Type", "text/html; charset=UTF-8");
			PrintWriter out=response.getWriter();
			out.print(jsonObj.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static  HashMap<String, Object> JsonToMap(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<HashMap<String, Object>> typeReference = new TypeReference<HashMap<String, Object>>() { };
        HashMap<String, Object> object = null;
		try {
			object = objectMapper.readValue(json, typeReference);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(object == null){
			System.out.println("--- JsonToMap Error --> "+ json);
		}
        return object;
    }
	
	public static String calDiffByDate(String szDate){
		String result = "";
		if(isEmpty(szDate) || szDate.equalsIgnoreCase("null")){
			return null;
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String today = format.format(new Date(System.currentTimeMillis()));
		
		System.out.println(" --- [calDiffByDate] szDate : "+ szDate +", today : "+ today+ ", szDate length : "+ szDate.length());
		
		
		Date beginDate;
		try {
			beginDate = format.parse(szDate);
			Date endDate = format.parse(today);
			// 날짜 차이 알아 내기
			long diff = endDate.getTime() - beginDate.getTime();
			long diffDays = 24 * 60 * 60 * 1000; // 시 * 분 * 초 * 밀리세컨
			long diffMonth = diffDays * 30;	// 월 만듬
			long diffYear = diffMonth * 12;	 // 년 만듬
			
			long totalMonth = diff/diffMonth;
			
			System.out.println("** 날짜 두개 : " + beginDate + ", " + endDate +", diff :"+diff/diffDays) ;
			System.out.println("* 일수 차이 : " + diff/diffDays + " 일");
			System.out.println("* 월수 차이 : " + diff/diffMonth + " 월");
			System.out.println("* 년수 차이 : " + diff/diffYear + " 년");
			System.out.println("* 년수 차이 : " + totalMonth%12 + " 개월");
			
			result = diff/diffYear+"."+totalMonth%12;
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	 
		
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String[] getParams(HttpServletRequest req, String key) {
		Map map = new HashMap();
		
		Enumeration e = req.getParameterNames();
		
		while(e.hasMoreElements()){
			String str = (String)e.nextElement();
			map.put(str,req.getParameterValues(str));
		}
		
		if(!isEmpty(map.get(key))) return (String[]) map.get(key);
		else return new String[0];
	}
	
	public static boolean isEmpty(Object obj) {
		if(obj == null || "".equals(toString(obj).trim())) return true;
		else return false;
	}
	
	public static String toString(Object obj) {
		return obj == null ? "" : obj.toString();
	}
	
	public static int getCurrentPage(String strCp) {
		
		int cp = 1;
		if (strCp == null || strCp.equals("") || strCp.equals("null")) {
			cp = 1;
		} else {
			System.out.println("--- strCp : "+ strCp);
			cp = Integer.parseInt(strCp);
		}		
		return cp;
		
	}
	
	public static int getCurrentPage(Map<String, String> params) {
		
		String strCp = params.get("cp");
		int cp = 1;
		if (strCp == null || strCp.equals("") || strCp.equals("null")) {
			cp = 1;
		} else {
			System.out.println("--- strCp : "+ strCp);
			cp = Integer.parseInt(strCp);
		}		
		return cp;
		
	}
	
	public static HashMap<String, Object> calcPage(int cp, long totalCount, double countPerPage) { 
		HashMap<String, Object> mres = new HashMap<String, Object>();
		int b_pageNum_list = 10; //블럭에 나타낼 페이지 번호 갯수
		//int tb = (int) Math.ceil(totalCount / 20.0); //총블럭수
		int tb = (int) Math.ceil(totalCount / countPerPage); //총블럭수
		
		
		// cp변수는 현재 페이지번호 
		int tempEnd = (int)(Math.ceil(cp / 10.0) * b_pageNum_list); 
		// 현재 페이지번호를 기준으로 끝 페이지를 계산한다. 
//		System.out.println("-- cp = " +cp); 
//		System.out.println("-- tempEnd = "+tempEnd); 
//		System.out.println("-- totalCount =" +totalCount); 
//		System.out.println("-- tb =" +tb); 
		
		// 시작 페이지 계산 
		int start = tempEnd - 9; 
		int end = 0;
		
		if (tempEnd * countPerPage > totalCount) { 
			// 가상으로 계산한 tempEnd크기가 실제 count보다 많을경우 
			//end = (int) Math.ceil(totalCount / 20.0); 
			end = (int) Math.ceil(totalCount / countPerPage); 
			//System.out.println("-- 11 : "+ tempEnd * 20);
		} else { 
			// 실제 count가 tempEnd보다 많을경우 
			end = tempEnd; 
			//System.out.println("-- 22");
		} 
		
//		System.out.println("-- start = "+start); 
//		System.out.println("-- end = "+end); 
		boolean prev = start != 1; 
		boolean next = end * countPerPage < totalCount; 
//		System.out.println("-- prev =" +prev); 
//		System.out.println("-- next =" +next); 
		
		mres.put("start", start);
		mres.put("end", end);
		mres.put("prev", prev);
		mres.put("next", next);
		
		return mres;
	}
	
	public static String readFile(String path, Charset encoding) throws IOException {
		  byte[] encoded = Files.readAllBytes(Paths.get(path));
		  return new String(encoded, encoding);
	}
	
	public static int fileDelete(String filePath){
		int result = 1;
		if(isEmpty(filePath)){
			return 300;
		}
		File file = new File(filePath);
		
		if(file.exists() && file.isFile()){
			file.delete();
			result = 0;
		}else{
			return 200;
		}
		
		return result;
	}
	
	public static boolean deleteFile(String filePath){
		if(isEmpty(filePath)){
			return false;
		}
		
		File file = new File(filePath);
		
		if(file.exists() && file.isFile()){
			file.delete();
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean deleteDirectory(File path) {
        
        if(!path.exists()) { //경로가 존재하는지 확인
            return false; 
        }
        
        File[] files = path.listFiles(); //경로안의 파일 또는 디렉토리 리스트 추출
        
        for (File file : files) {
 
            if (file.isDirectory()) { //디렉토리 라면 아래 실행
 
                deleteDirectory(file); //deleteDirectory 메소드 재귀호출
 
            } else {
 
                file.delete(); //파일이라면 파일 삭제
 
            } 
 
        } 
        
        if(path.delete()) { //경로 삭제
            return true; //성공
        }else {
            return false; //실패
        }
    }
	
    public static String[] split(Object obj, String regex) {
		String result[] = null;
		if (obj != null) {
		    String str = String.valueOf(obj);
		    result = str.split(regex);
		}
		return result;
    }

    /*
    private static String makeAddCateCode(String categoryCode){
	   	 String arrNum = "0123456789";
	   	 String arrStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // 26
	   	
	   	 char[] numArr = arrNum.toCharArray();
	   	 char[] strArr = arrStr.toCharArray();
	   	 Hashtable CateCodeKeyHash = new Hashtable();
	     Hashtable CateCodeValueHash = new Hashtable();
   	
//	   String [][] arrCateCode = new String [numArr.length][strArr.length];
	     int idx =0;
	     String code = "";
	     for (int i = 0 ; i < numArr.length ; i++) {
//   		 System.out.println(i+" 번째 , "+numArr[i]+", "+ numArr.length);
   		 for (int j = 0; j < strArr.length; j++) {
   			 idx ++;
   			 code = ""+numArr[i]+strArr[j];
   			 System.out.println(" -- "+idx+" 번째 , "+code);
   			 CateCodeKeyHash.put(""+idx, code);
   			 CateCodeValueHash.put(""+code,""+idx);
			}
	     }
	     System.out.println("categoryCode : " + categoryCode);
	     
	     if("ZZ".equals(categoryCode)){
	    	 return "0A";
	     }else if("9Z".equals(categoryCode)){
	    	 return "0000";
	     }else{
	    	   int cateIdx = Integer.parseInt(String.valueOf(CateCodeValueHash.get(""+categoryCode)));
	    	   cateIdx += 1;
	    	   System.out.println(cateIdx+ " , "+ CateCodeKeyHash.get(""+cateIdx));
	    	   return String.valueOf(CateCodeKeyHash.get(""+cateIdx));
	     }
   }
   
   private static int checkCateCode(String strCateCode){
   	   String CateCodeString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";         ///36진수 사용하기위해
	       char[] codeArr = CateCodeString.toCharArray();
	       Hashtable CateCodeKeyHash = new Hashtable();
	       Hashtable CateCodeValueHash = new Hashtable();
	       
	       int result = 0;
	       for (int i = 0 ; i < codeArr.length ; i++) {
	            CateCodeKeyHash.put(""+i, ""+codeArr[i]);
	            CateCodeValueHash.put(""+codeArr[i],""+i);
	       }
	        
	       try{     /////99이하인 경우는 숫자만 있음
	    	   result = Integer.parseInt(strCateCode);
          }catch(Exception e){   /////문자가 있을경우 10진수로 변환
       	   /////////////////////36진수 10진수로 변환///////////////////////////
       	   String R_TenJari = strCateCode.substring(0,1);
       	   String R_OneJari = strCateCode.substring(1,2);
       	   int intR_TenJari = (Integer.parseInt((String)CateCodeValueHash.get(R_TenJari))-10)*36;
       	   int intR_OneJari = Integer.parseInt((String)CateCodeValueHash.get(R_OneJari));
       	   System.out.println("10자리 : "+intR_TenJari +", 1자리 : "+ intR_OneJari);
       	   result  = (intR_TenJari+intR_OneJari)+100;
       	   System.out.println("36진수 10진수로 변환 result :  "+ result);
          }
	       	result +=1;
	         int chg_i_categoryCode=result-100;
	         int intTenJari = (chg_i_categoryCode/36)+10;
	       	 int intOneKey = chg_i_categoryCode%36;
            System.out.println(" 십자리:"+intTenJari + ", 일자리:"+intOneKey);
            String TenJari = (String)CateCodeKeyHash.get(""+intTenJari);
            String OneJari = (String)CateCodeKeyHash.get(""+intOneKey);
            System.out.println(" 222 십자리:"+TenJari + ", 일자리:"+OneJari);
	       
	       return result;
   }
 */  
   
   /**
    * 문자열을 MD-5 방식으로 암호화
    * @param txt 암호화 하려하는 문자열
    * @return String
    * @throws Exception
    */
   public static String getEncMD5(String txt) throws Exception {
        
       StringBuffer sbuf = new StringBuffer();
        
        
       MessageDigest mDigest = MessageDigest.getInstance("MD5");
       mDigest.update(txt.getBytes());
        
       byte[] msgStr = mDigest.digest() ;
        
       for(int i=0; i < msgStr.length; i++){
           String tmpEncTxt = Integer.toHexString((int)msgStr[i] & 0x00ff) ;          
           sbuf.append(tmpEncTxt) ;
       }
        
        
        
       return sbuf.toString() ;
   }

   public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
   
   /**
    * byte 계산
    * @param bytes 문자열 데이터
    * @return String
    */
   public static String byteCalculation(String bytes) {
      String result = "0";
      Double size = Double.parseDouble(bytes);

       String[] s = { "bytes", "KB", "MB", "GB", "TB", "PB" };
       

       if (bytes != "0") {
             int idx = (int) Math.floor(Math.log(size) / Math.log(1024));
             DecimalFormat df = new DecimalFormat("#,###.##");
             double ret = ((size / Math.pow(1024, Math.floor(idx))));
             result = df.format(ret) + " " + s[idx];
        } else {
        	result += " " + s[0];
        }

        return result;
   }
   
   /**
    * 데이터를 리스트로 생성
    * @param szRes 문자열 데이터
    * @return List
    */
   public static List<String> makeObjList(String szRes) {
		List<String> objList = new ArrayList<String>();
		
		String[] arObjs = szRes.split(",");
		for (int i = 0; i < arObjs.length; i++) {
			objList.add(arObjs[i]);
		}
		
		return objList;
	}
   
   
   public static String[] makeExcelColumns(int szLen) {
		String[] szColumn = Define.AR_ExcelColumns;
		String[] arResult = new String[szLen];
		
		for (int i = 0; i < szColumn.length; i++) {
			if(szLen > i) {
				arResult[i] = szColumn[i];
			}
		}
		
//		System.out.println("-- arResult : "+ arResult.length);
		
		return arResult;
	}   

	public static ExcelReadOption excelUpload(MultipartHttpServletRequest request, String[] colums) {
		MultipartFile excelFile = request.getFile("excelFile");
		
		if(excelFile==null || excelFile.isEmpty()){
			
			throw new RuntimeException("엑셀파일을 선택 해 주세요.");
		}
		String contentPath = StrUtil.getContentPath();
		//File destFile = new File("C:\\"+excelFile.getOriginalFilename()); D:/Image_test/upload/
	    File destFile = new File(contentPath+excelFile.getOriginalFilename());
		
	    try{
	        //내가 설정한 위치에 내가 올린 파일을 만들고
	        excelFile.transferTo(destFile);

	    }catch(Exception e){
	        throw new RuntimeException(e.getMessage(),e);
	    }

	    //업로드를 진행하고 다시 지우기
	    //excelService.excelUpload(destFile);
		ExcelReadOption excelReadOption = new ExcelReadOption();
	
		//파일경로 추가
	    excelReadOption.setFilePath(destFile.getAbsolutePath());
	    
	    //추출할 컬럼 명 추가
	    //excelReadOption.setOutputColumns("A","B","C","D","E","F","G","H","I", "J");
	    excelReadOption.setOutputColumns(colums);
	        
	    // 시작 행
	    excelReadOption.setStartRow(3);
	        
	    return excelReadOption;
	}
	                                   //emblem , U18, teamId, requst 
	public static String contentUpload(String fileName, String uage, String key, MultipartHttpServletRequest request){
		//파일 디렉토리 만들기
		String rootPath = getContentPath();
		
		String uploadPath = rootPath+fileName+"/"+uage;
//		System.out.println("--- [contentUpload] rootPath : "+ rootPath);
		File fileDir = new File(uploadPath);
		
		if(!fileDir.isDirectory()){
			fileDir.mkdirs();
		}
		
		//파일명구하기
		Iterator<String> iter = request.getFileNames();
		
		//파일 웹 경로
		String filePath = "";

        Long currentUnixTime = System.currentTimeMillis();
		
		while (iter.hasNext()) {
			String uploadFileName = iter.next();
			MultipartFile mFile = request.getFile(uploadFileName);
			String orgFileName = mFile.getOriginalFilename();
			System.out.println("--- uploadFileName : "+uploadFileName+ ", orgFileName : "+ orgFileName);
		    
//			if(orgFileName != null && !orgFileName.equals("")) {
			if(!StrUtil.isEmpty(orgFileName)) {	
					
				//확장자 구하기
				String exc = orgFileName.substring(orgFileName.lastIndexOf(".")+1, orgFileName.length());
				System.out.println("-- orgFileName : "+ orgFileName +", exc : "+ exc);
				
				//저장파일명
				//String saveFileName = key+"_"+System.currentTimeMillis()+"."+exc;
				String saveFileName = fileName+"_"+uage+"_"+key+ "_" + currentUnixTime + "."+exc;
				System.out.println("-- saveFileName : "+ saveFileName );
				
				//파일 저장
				try {
					File destFile = new File(uploadPath+"/"+ saveFileName);
					mFile.transferTo(destFile);
					//destFile.setWritable(true);
					//destFile.setReadable(true);
					Runtime.getRuntime().exec("chmod -R 777 " + destFile);
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
				}
				
				filePath = "/"+fileName+"/"+uage+"/"+saveFileName;
				

			}
			
		}
		return filePath;
	}

    public static String profileUpload(String fileName, MultipartHttpServletRequest request){
        //파일 디렉토리 만들기

        String rootPath = getContentPath();

        String uploadPath = rootPath+fileName+"/" + new SimpleDateFormat ("yyyy-MM").format(new Date());
        File fileDir = new File(uploadPath);

        if(!fileDir.isDirectory()){
            fileDir.mkdirs();
        }

        //파일명구하기
        Iterator<String> iter = request.getFileNames();

        //파일 웹 경로
        String filePath = "";

        while (iter.hasNext()) {
            String uploadFileName = iter.next();
            MultipartFile mFile = request.getFile(uploadFileName);
            String orgFileName = mFile.getOriginalFilename();

            int index = orgFileName.lastIndexOf(".");
            String fileExt = orgFileName.substring(index + 1);

//			if(orgFileName != null && !orgFileName.equals("")) {
            if(!StrUtil.isEmpty(orgFileName)) {

                //확장자 구하기
                String exc = orgFileName.substring(orgFileName.lastIndexOf(".")+1, orgFileName.length());
                System.out.println("-- orgFileName : "+ orgFileName +", exc : "+ exc);

                //저장파일명
                //String saveFileName = key+"_"+System.currentTimeMillis()+"."+exc;
                String saveFileName = fileName+"_"+DateUtil.getTimeStamp()+ "." + fileExt;

                //파일 저장
                try {
                    File destFile = new File(uploadPath+"/"+ saveFileName);
                    mFile.transferTo(destFile);
                    //destFile.setWritable(true);
                    //destFile.setReadable(true);
                    Runtime.getRuntime().exec("chmod -R 777 " + destFile);
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                }

                filePath = "/"+fileName+"/" + new SimpleDateFormat ("yyyy-MM").format(new Date()) + "/"+saveFileName;

            }

        }
        return filePath;
    }
   
	
	public static String getContentPath(){
		String contentPath = "";
		
		if(Define.TEST_BED){
			//contentPath = "/Users/content_upload"; //"D:/Image_test/upload/";
			contentPath = Define.LOCAL_SAVE_FILE_PATH;
		}else{
			contentPath = Define.LIVE_SAVE_FILE_PATH;
		}
		System.out.println("-- [getContentPath] TEST BED : "+Define.TEST_BED+" , CONTENT_PATH:"+contentPath);
		
		return contentPath;
	}
	
	
	public static String getWebPath() {
		String webPath = "";
		
		if (Define.TEST_BED) {
//			// http://127.0.0.1:8080/GH
//			String propWebPath = System.getProperty("WEB_PATH");
////			String propWebPath = System.getProperty("file.location.env");
//			if(propWebPath == null){
//				propWebPath = "http://127.0.0.1:8080/GH";
//			}
//			System.out.println("TEST_BED -> WEB_PATH:"+propWebPath);
//			webPath = propWebPath;
			
			webPath = Define.LOCAL_WEB_PATH;
			
		} else {			
			webPath = Define.LIVE_WEB_PATH;
		}
		System.out.println("-- [getWebPath] TEST BED : "+Define.TEST_BED+" , WEB_PATH:"+webPath);
		
		return webPath;
	}

	
	public static void main (String args[]) throws Exception{
		
		
		String szNum = "113";
		System.out.println(isInteger(szNum));
		
//		int serial = 113; 
//		String suffix = String.format("%02d", serial); 
//		System.out.println(suffix);


//		String szStr = "yiadmin123"; //yiadmin123
//		String szResult = getEncMD5(szStr);
//		System.out.println(szResult+","+ szResult.length());
		
//		String categoryCode = "9Y"; //랜덤 테스트;
//		String szResult = makeAddCateCode(categoryCode);
//    	System.out.println("--- szResult : "+ szResult);
		
//		DateUtil du = new DateUtil();
//		System.out.println(du.getDefaultDate());
//		System.out.println(du.formatYmdh(du.getDefaultDate()));
		
//		String path = "D:/Image_test/txt/env.txt";
//		String content = readFile(path, StandardCharsets.UTF_8);
//		System.out.println("--- content : "+ content);
		
		StrUtil strutil = new StrUtil();
		//String macAddr = "00-50-56-C0-00-01";
		String macAddr = "00-50-56-C0-00-01,00-50-56-C0-00-08,18-67-B0-CB-0C-98";
		String defaultLoc = "00-50-56-C0-00-02,00-50-56-C0-00-09,18-67-B0-CB-0C-98,00-50-56-C0-00-03";
		//calDiffByDate("2017-01-12");
	//	int result = strutil.compareMacAddress(macAddr, defaultLoc);
	//	System.out.println("--- compareMacAddress result : "+ result);
//		System.out.println("-- makeTodate : "+ makeTodayDATE());
//		System.out.println("-- addDateFormat : "+ addDateFormat("2012-04-25"));
//		System.out.println("default 마지막 일:"+lastDayByMonth(null));
//		System.out.println("search 마지막 일:"+lastDayByMonth("2016-11-01"));
		
		//http://127.0.0.1:8080/GH/cc/rw/2/rw_1_2.jpg
		//String temp = "http://127.0.0.1:8080/GH/tl/2/tl_1_2.jpg";
//		String temp = "http://127.0.0.1:8080/GH/cc/rw/2/rw_1_2.jpg";
//		String [] arStr = temp.split("GH/");
//		for (int i = 0; i < arStr.length; i++) {
//			System.out.println("-- arStr["+i+"] : "+ arStr[i]);
//		}
		
//		String szStr = ",3,7";
//		String szTemp = szStr.substring(1,szStr.length());
//		System.out.println("--- szTemp : "+ szTemp);
		
		//String szTemp = "123456789ab";
		String szTemp = "00000000001";
		StringBuilder sb = new StringBuilder(szTemp);
		int step = 1;
//		String szResult = szTemp.substring(step-1, step);
//		System.out.println("--- szResult : " + szResult);
		
		//szTemp = szTemp.replace(szResult, "*");
		sb.setCharAt(step-1, '*');
//		System.out.println("--- sb : " + sb);
		
//		int processDigit = GHDefine.AR_WRITE.length;
//		System.out.println("-- process digit cnt : "+processDigit);
//		String szT = "";
//		for (int i = 0; i < processDigit; i++) {
//			szT +="0";
//		}
//		System.out.println("-- szT : "+szT +", szT cnt : "+ szT.length() +", szTemp cnt : "+ szTemp.length());
//		
//		if(szTemp.equals(szT)){
//			System.out.println("-- same");
//		}
		
		long curTime = System.currentTimeMillis();
		 
		System.out.println("curTime is "+ curTime);
		System.out.println("second is "+ curTime / 1000);
		System.out.println("minute is "+ curTime / (1000 * 60) );
		System.out.println("hour is "+ curTime / (1000 * 60 * 60));
		
		long curTime1 = 1568981554010L;
		long curTime2 = 1569199731L;//1568865112L;//1568981553L;
		long curTime3 = 1569214090L;//1568865112L;//1568981553L;
		System.out.println("-[현재시간-1 :"+DateUtil.getDefaultDate()+"]");
		System.out.println("-[현재시간-2 :"+DateUtil.format(curTime1 , "yyyy-MM-dd HH:mm:ss")+"]");
		System.out.println("-[현재시간-3 :"+DateUtil.format(curTime2 * 1000, "yyyy-MM-dd HH:mm:ss")+"]");
		System.out.println("-[현재시간-4 :"+DateUtil.format(curTime3 * 1000, "yyyy-MM-dd HH:mm:ss")+"]");
		System.out.println("-[현재시간-5 :"+DateUtil.format(1575436671L * 1000, "yyyy-MM-dd HH:mm:ss")+"]");
		
		String hour = "9";
		System.out.println(String.format("%02d", 8));
		System.out.println(String.format("%02d", 22));
		System.out.println(String.format("%02d", Integer.parseInt(hour)));


		
	}
	
}
