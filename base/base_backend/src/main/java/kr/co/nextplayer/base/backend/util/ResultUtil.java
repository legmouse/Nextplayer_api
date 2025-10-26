package kr.co.nextplayer.base.backend.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ResultUtil {
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

	public static Map<String, Object> createResMap() {
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("CODE", "100");
		resMap.put("DESC", "OK");
		resMap.put("VERSION", String.valueOf(System.currentTimeMillis()));
		return resMap;
	}
	
	public static String toDateString(String szFormat, Timestamp timeStamp) {
		SimpleDateFormat sdf = new SimpleDateFormat( szFormat, Locale.KOREA );
		String str = sdf.format( new Date( timeStamp.getTime( ) ) );
		//System.out.println( "toDateString : " + str ); 
		return str; 
	}
	public static String toDashedPhoneNumber(String szPhone, boolean bHidden) {
		byte[] byPhone = szPhone.getBytes();
		byte[] byPhoneFormat = new byte[byPhone.length+2];
		int idx = 0;
		for(int i = 0; i < byPhone.length; i++) {
			
			byPhoneFormat[idx] = byPhone[i];
			if(bHidden == true) {
				if(i >= 3 && i <= 6 ) {
					byPhoneFormat[idx] = '*';
				}
			}
			
			// 01012345678
			if(i == 2 || i == 6) {
				idx++;
				byPhoneFormat[idx] = '-';	
			}
			idx++;
		}
		return new String(byPhoneFormat);
	}
	
	
	
}
