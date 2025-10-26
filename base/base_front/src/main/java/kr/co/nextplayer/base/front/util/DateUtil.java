package kr.co.nextplayer.base.front.util;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Date Util
 * @version : 1.0
 * @author :  Copyright (c) 2015 by MIRINCOM CORP. All Rights Reserved.
 */
public class DateUtil {

	// 날짜포멧형식

    public static  final String YYYY = "yyyy";
	public static final String YYYYMM = "yyyy-MM";
	public static final String YYYYMMDOT = "yyyy.MM.dd";
	public static final String YYYYMMDD = "yyyy-MM-dd";
	public static final String YYYYMMDDHH = "yyyy-MM-dd HH";
	public static final String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
	public static final String YYYYMMDDTHHMM = "yyyy-MM-dd'T'HH:mm";
	public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYYMMDDTHHMMSS = "yyyy-MM-dd'T'HH:mm:ss";
	
	// 시간형식
	public static final String HHMMSS = "HH:mm:ss";
	public static final String HHMM = "HH:mm";

	/**
	 *
	 * @method main
	 * @param args
	 */
	public static void main(String[] args) {
		
		// test convert date
		System.out.println("###################################");
		System.out.println("convertDate start!!!!");

		Date fromDate = DateUtil.parse("2014-07-18 12:11:35");
		System.out.println("from date : " + fromDate);

		Date toDate = DateUtil.parse("2014-12-18 16:12:35");
		System.out.println("to date : " + toDate);

		String convertDateStr = DateUtil.convertDate(fromDate, toDate);
		System.out.println("convertDateStr : " + convertDateStr);
		System.out.println("convertDate end!!!!");
		System.out.println("###################################\r\n");
		
		System.out.println("start");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0);
		System.out.println(DateUtil.format(cal, "yyyy-MM-dd"));
		System.out.println(DateUtil.add(Calendar.MONDAY, -3));
		System.out.println(DateUtil.getFirstDay());
		System.out.println(DateUtil.getLastDay());
		System.out.println("end");
		
		String szDate = "2018-08-28 18:00:00";
		System.out.println(DateUtil.isTimeOverBy(szDate));
		
		String szDate1 = "2018-08-28 24:00";
		System.out.println("changeDateFormatYmdhm : "+DateUtil.changeDateFormatYmdhm(szDate1));
		
	    System.out.println("-- curDate : "+DateUtil.getCurrentDateTime());
	    System.out.println("-- getDefaultDate : "+DateUtil.getDefaultDate());
	    
	   System.out.println("-- changeDateFormatYmdhm : "+DateUtil.changeDateFormatYmdhm(DateUtil.getDefaultDate()));
	
	   System.out.println("-[현재시간-0 :"+DateUtil.format(DateUtil.getDefaultDate() , "yyyyMMddHHmmss")+"]");
	   System.out.println("-[현재시간-1 :"+DateUtil.format(DateUtil.getDefaultDate() , DateUtil.YYYYMMDOT)+"]");
	   System.out.println("-[현재시간-2 :"+DateUtil.format(DateUtil.getDefaultDate() , DateUtil.YYYYMMDDTHHMMSS)+"]");
	   System.out.println("-[현재시간 1분후 :"+DateUtil.getAddMinDate(1, DateUtil.YYYYMMDDTHHMM));
	   System.out.println("-[현재시간 1분전 :"+DateUtil.getAddMinDate(-1, DateUtil.YYYYMMDDTHHMM));
	   System.out.println("-[현재시간 하루전 :"+DateUtil.getAddHourDate(-24, DateUtil.YYYYMMDDHHMM));
	   
	   String tmp1 = "2019-10-29 19:09";
	   String tmp2 = "2019-10-29 19:09";
	   System.out.println(" tmp1:"+parse(tmp1, DateUtil.YYYYMMDDHHMM));
	   System.out.println(" tmp2:"+parse(tmp2, DateUtil.YYYYMMDDHHMM));
	   
	   System.out.println(DateUtil.dateCompare(tmp1, tmp2));
	}
	
	public static int dateCompare(String cDay, String szDay) {
		int compare = parse(cDay, DateUtil.YYYYMMDDHHMM).compareTo(parse(szDay, DateUtil.YYYYMMDDHHMM));
		
		if(compare > 0) {
			System.out.println("[dateCompare] currentTime:"+cDay+" >  dateTime:"+szDay);
		}else if(compare < 0) {
			System.out.println("[dateCompare] currentTime:"+cDay+" < dateTime:"+szDay);
		}else {
			System.out.println("[dateCompare] currentTime:"+cDay+" = dateTime:"+szDay);
		}
		
		return compare;
		
	}
	
	public static long changeMillis (String szRes){
		long result = 0;
		try {
			SimpleDateFormat format = new SimpleDateFormat(YYYYMMDDHHMMSS);
			Date date = format.parse(szRes);
			result = date.getTime() / 1000;
		
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String changeDateFormatYmdhm(String szRes){
		String result = "";
		try {
			SimpleDateFormat format = new SimpleDateFormat(YYYYMMDDHHMM);
			Date date = format.parse(szRes);
			result = format.format(date);
		
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 시간을 비교한다. date1이 date2보다 이후 일때 true, 아니면 false
	 * @Mehtod Name : isOverTimeBy
	 * @return boolean
	 */
	public static boolean isTimeOverBy(String szDate) {
        
		SimpleDateFormat format = new SimpleDateFormat(YYYYMMDDHHMMSS);
		String today = format.format(new Date(System.currentTimeMillis()));
		
		try {
			//System.out.println("today : "+ DateUtil.getDefaultDate()+", target : "+ szDate +", simple : "+ today);
			
			Date date1 = format.parse(DateUtil.getDefaultDate());
			Date date2 = format.parse(szDate);
			if(date1.after(date2)){    // 시간을 비교한다. date1이 date2보다 이후 일때 true, 아니면 false
				System.out.println("--- time over");
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return false;
    }

	
	/**
	 * @desc 특정시간 1분전 날짜 형식을 정해 포맷으로 돌려준다.
	 * @return
	 */
	public static String getAddMinDateFormat(int min, String date, String format) {
//		String fmtDate = formatYmdhm(date);
		
		System.out.println("-- date : "+ date+", size : "+ date.length());
		if(date.length() != 16) {
			return null;
		}
//		System.out.println("-- YEAR : "+ date.substring(0,4) +", "+ Integer.parseInt(date.substring(0,4)));
//		System.out.println("-- MONTH : "+ date.substring(5,7) +", "+ Integer.parseInt(date.substring(5,7)));
//		System.out.println("-- DATE : "+ date.substring(8,10) +", "+ Integer.parseInt(date.substring(8,10)));
//		System.out.println("-- HOUR : "+ date.substring(11,13) +", "+ Integer.parseInt(date.substring(11,13)));
//		System.out.println("-- MINUTE : "+ date.substring(14,16) +", "+ Integer.parseInt(date.substring(14,16)));
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(date.substring(0,4)));
		cal.set(Calendar.MONTH, Integer.parseInt(date.substring(5,7))-1);
		cal.set(Calendar.DATE, Integer.parseInt(date.substring(8,10)));
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.substring(11,13)));
		cal.set(Calendar.MINUTE, Integer.parseInt(date.substring(14,16)));
		
//		System.out.println("-[getAddMinDateFormat :"+DateUtil.format(cal , DateUtil.YYYYMMDDTHHMM));
		// 분 더하기 or 빼기 
		cal.add(Calendar.MINUTE, min);
		
//		System.out.println("-[getAddMinDateFormat :"+DateUtil.format(cal , DateUtil.YYYYMMDDTHHMM));
		
		return DateUtil.format(cal , format);
	}
	
	/**
	 * @desc 현재시간 시간단위 날짜 형식을 정해 포맷으로 돌려준다.
	 * @return
	 */
	public static String getAddHourDate(int hour, String format) {
		Calendar cal = Calendar.getInstance();
		// 분 더하기 or 빼기 
		cal.add(Calendar.HOUR_OF_DAY ,hour);
		
		//System.out.println("-[getAddMinDate :"+DateUtil.format(cal , DateUtil.YYYYMMDDTHHMM));
		
		return DateUtil.format(cal , format);
	}
	
	/**
	 * @desc 현재시간 1분전 날짜 형식을 정해 포맷으로 돌려준다.
	 * @return
	 */
	public static String getAddMinDate(int min, String format) {
		Calendar cal = Calendar.getInstance();
		// 분 더하기 or 빼기 
		cal.add(Calendar.MINUTE, min);
		
		//System.out.println("-[getAddMinDate :"+DateUtil.format(cal , DateUtil.YYYYMMDDTHHMM));
		
		return DateUtil.format(cal , format);
	}
	
	/**
	 * @desc 날짜 기본형식을 정해진 포멧으로 돌려준다.
	 * @return
	 */
	public static String getDefaultDate() {
		return DateTime.now().toString(YYYYMMDDHHMMSS);
	}

	/**
	 * @Mehtod Name : getSystemDate
	 * @return
	 */
	public static Calendar getSystemDate() {
		Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        return cal;
	}

	/**
	 * 현재 일자를 돌려준다.
	 * @Mehtod Name : getCurrentDate
	 * @return
	 */
	public static String getCurrentDate() {
        return DateTime.now().toString(YYYYMMDD);
    }

	/**
	 * 현재 일자시간을 돌려준다.
	 * @Mehtod Name : getCurrentDateTime
	 * @return
	 */
	public static String getCurrentDateTime() {
		return DateTime.now().toString(YYYYMMDDHH);
	}

	/**
	 * 현재 일자를 정해진 포맷으로 돌려준다.
	 * @Mehtod Name : getCurrentDate
	 * @param format
	 * @return
	 */
	public static String getCurrentDate(String format) {
        return format(getSystemDate(), format);
    }

	/**
	 * @Mehtod Name : parse
	 * @param date
	 * @return
	 */
	public static Date parse(String date) {

		date = date.replaceAll("\\W", "");

		if (date.length() == 8) {
			return DateUtil.parse(date, "yyyyMMdd");
		} else if (date.length() == 12) {
			return DateUtil.parse(date, "yyyyMMddHHmm");
		} else if (date.length() == 14) {
			return DateUtil.parse(date, "yyyyMMddHHmmss");
		}
		return null;
	}

	/**
	 * @Mehtod Name : parse
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date parse(String date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @Mehtod Name : format
	 * @param format
	 * @return
	 */
	public static String format(long time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(time);
	}

	/**
	 * @Mehtod Name : format
	 * @param cal
	 * @param format
	 * @return
	 */
	public static String format(Calendar cal, String format) {
		
		if (cal == null) {
			return null;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(cal.getTime());
	}

	/**
	 * @Mehtod Name : format
	 * @param s
	 * @param format
	 * @return
	 */
	public static String format(String s, String format) {
		
		if (s == null) {
			return null;
		}
		
		Calendar c = toCalendar(s);
		return format(c, format);
	}
	
	/**
	 * 기본 포멧을 이용하여 날짜를 돌려준다.
	 * yyyyMM
	 * @Mehtod Name : formatYm
	 * @param cal
	 * @return
	 */
	public static String formatYm(Calendar cal) {
		return DateUtil.format(cal, YYYYMM);
	}

	/**
	 * 기본 포멧을 이용하여 날짜를 돌려준다.
	 * yyyyMM
	 * @Mehtod Name : formatYm
	 * @return
	 */
	public static String formatYm(String s) {
		return DateUtil.format(s, YYYYMM);
	}

	/**
	 * 기본 포멧을 이용하여 날짜를 돌려준다.
	 * yyyy.MM.dd
	 * @Mehtod Name : formatYmdDot
	 * @param s
	 * @return
	 */
	public static String formatYmdDot(String s) { return DateUtil.format(s, YYYYMMDOT); }

	/**
	 * 기본 포멧을 이용하여 날짜를 돌려준다.
	 * yyyyMMdd
	 * @Mehtod Name : formatYmd
	 * @param cal
	 * @return
	 */
	public static String formatYmd(Calendar cal) {
		if (cal == null) {
			return null;
		}
		return DateUtil.format(cal, YYYYMMDD);
	}

	/**
	 * 기본 포멧을 이용하여 날짜를 돌려준다.
	 * yyyyMMdd
	 * @Mehtod Name : formatYmd
	 * @return
	 */
	public static String formatYmd(String s) {
		if (s == null) {
			return null;
		}
		return DateUtil.format(s, YYYYMMDD);
	}

	/**
	 * 기본 포멧을 이용하여 날짜를 돌려준다.
	 * yyyyMMdd HH
	 * @Mehtod Name : formatYmdh
	 * @param cal
	 * @return
	 */
	public static String formatYmdh(Calendar cal) {
		return DateUtil.format(cal, YYYYMMDDHH);
	}

	/**
	 * 기본 포멧을 이용하여 날짜를 돌려준다.
	 * yyyyMMdd HH
	 * @Mehtod Name : formatYmdh
	 * @return
	 */
	public static String formatYmdh(String s) {
		return DateUtil.format(s, YYYYMMDDHH);
	}

	/**
	 * 기본 포멧을 이용하여 날짜를 돌려준다.
	 * yyyyMMdd HHdd
	 * @Mehtod Name : formatYmdhm
	 * @param cal
	 * @return
	 */
	public static String formatYmdhm(Calendar cal) {
		return DateUtil.format(cal, YYYYMMDDHHMM);
	}

	/**
	 * 기본 포멧을 이용하여 날짜를 돌려준다.
	 * yyyyMMdd HHdd
	 * @Mehtod Name : formatYmdhm
	 * @return
	 */
	public static String formatYmdhm(String s) {
		return DateUtil.format(s, YYYYMMDDHHMM);
	}

	/**
	 * 기본 포멧을 이용하여 날짜를 돌려준다.
	 * yyyyMMdd HHddss
	 * @Mehtod Name : formatYmdhms
	 * @param cal
	 * @return
	 */
	public static String formatYmdhms(Calendar cal) {
		return DateUtil.format(cal, YYYYMMDDHHMMSS);
	}

	/**
	 * 기본 포멧을 이용하여 날짜를 돌려준다.
	 * yyyyMMdd HHddss
	 * @Mehtod Name : formatYmdhms
	 * @return
	 */
	public static String formatYmdhms(String s) {
		return DateUtil.format(s, YYYYMMDDHHMMSS);
	}

	/**
	 * 기본 포멧을 이용하여 시분초 형식으로 돌려준다.
	 * @method formatHms
	 * @param s
	 * @return
	 */
	public static String formatHms(String s) {
		if (s == null) {
			return null;
		}

		/*if (s.length() >= 6) {
			s = DateUtil.getCurrentDate("yyyyMMdd") + s;
		}*/

		return DateUtil.format(s, HHMMSS);
	}

	/**
	 * 기본 포멧을 이용하여 시분 형식으로 돌려준다.
	 * @method formatHm
	 * @param s
	 * @return
	 */
	public static String formatHm(String s) {
		if (s == null) {
			return null;
		}

		return DateUtil.format(s, HHMM);
	}
	
	/**
	 * @Mehtod Name : toCalendar
	 * @param date
	 * @return
	 */
	public static Calendar toCalendar(String date) {

		Calendar cal = Calendar.getInstance();

		if (date.length() == 8) {
			cal.setTime(parse(date, "yyyyMMdd"));
		} else if (date.length() == 7) {
			cal.setTime(parse(date, "yyyy-MM"));
		} else if (date.length() == 10) {
			cal.setTime(parse(date, "yyyy-MM-dd"));
		} else if (date.length() == 12) {
			cal.setTime(parse(date, "yyyyMMddHHmm"));
		} else if (date.length() == 14) {
			cal.setTime(parse(date, "yyyyMMddHHmmss"));
		}

		return cal;
	}

	/**
	 * 현재일을 기준으로 연산된 날짜를 돌려준다.
	 * @Mehtod Name : add
	 * @param field
	 * @param amount
	 * @return
	 */
	public static String add(int field, int amount) {
		Calendar cal = getSystemDate();
		cal.add(field, amount);
		return format(cal, "yyyy-MM-dd");
	}

	/**
	 * 현재일을 기준으로 연산된 날짜를 돌려준다.
	 * @param field
	 * @param amount
	 * @param fmt
	 * @return
	 */
	public static String add(int field, int amount, String fmt) {
		Calendar cal = getSystemDate();
		cal.add(field, amount);
		return format(cal, fmt);
	}

	/**
	 * @Mehtod Name : add
	 * @param date
	 * @param field
	 * @param amount
	 * @return
	 */
	public static String add(String date, int field, int amount, String fmt) {
		Calendar cal = toCalendar(date);
		cal.add(field, amount);
		return format(cal, fmt);
    }
	
	/**
	 * 날짜검색 기본 시작일자
	 * @Mehtod Name : getDefaultStart
	 * @return
	 */
	public static String getDefaultStart(String param) {
		LocalDate localDate = new LocalDate();
		localDate.minusDays(Config.getCommon().getInt(param));
		return localDate.toString("yyyy-MM-dd");
	}

	/**
	 * 날짜검색 기본 종료일자
	 * @Mehtod Name : getDefaultEnd
	 * @return
	 */
	public static String getDefaultEnd(String param) {
		LocalDate localDate = new LocalDate();
		localDate.minusDays(Config.getCommon().getInt(param));
		return localDate.toString("yyyy-MM-dd");
	}

	/**
	 * 날짜검색 기본 시작일자
	 * @Mehtod Name : getDefaultStart
	 * @return
	 */
	public static String getDefaultStart() {
		return DateUtil.getDefaultStart("SEARCH_CONDITION.DEFAULT_START_DATE");
	}

	/**
	 * 날짜검색 기본 종료일자
	 * @Mehtod Name : getDefaultEnd
	 * @return
	 */
	public static String getDefaultEnd() {
		return DateUtil.getDefaultEnd("SEARCH_CONDITION.DEFAULT_END_DATE");
	}

	/**
	 * 날짜검색 기본 시작일자
	 * @Mehtod Name : getDefaultStart
	 * @return
	 */
	public static String getStatDefaultStart() {
		Calendar cal = getSystemDate();
		cal.add(Calendar.DATE, Config.getCommon().getInt("STAT_SEARCH_CONDITION.DEFAULT_START_DATE"));
		return format(cal, "yyyy-MM-dd");
	}

	/**
	 * 날짜검색 기본 종료일자
	 * @Mehtod Name : getDefaultEnd
	 * @return
	 */
	public static String getStatDefaultEnd() {
		Calendar cal = getSystemDate();
		cal.add(Calendar.DATE, Config.getCommon().getInt("STAT_SEARCH_CONDITION.DEFAULT_END_DATE"));
		return format(cal, "yyyy-MM-dd");
	}
	
	/**
	 * 날짜검색 기본 시작일자
	 * @Mehtod Name : getDefaultStart
	 * @return
	 */
	public static String getMornitoringDefaultStart() {
		Calendar cal = getSystemDate();
		cal.add(Calendar.DATE, Config.getCommon().getInt("MORNITORING_SEARCH_CONDITION.DEFAULT_START_DATE"));
		return format(cal, "yyyy-MM-dd");
	}

	/**
	 * 날짜검색 기본 종료일자
	 * @Mehtod Name : getDefaultEnd
	 * @return
	 */
	public static String getMornitoringDefaultEnd() {
		Calendar cal = getSystemDate();
		cal.add(Calendar.DATE, Config.getCommon().getInt("MORNITORING_SEARCH_CONDITION.DEFAULT_END_DATE"));
		return format(cal, "yyyy-MM-dd");
	}
	
	/**
	 * Dashboard 날짜검색 기본 시작일자
	 * @Mehtod Name : getDefaultStart
	 * @return
	 */
	public static String getDashboardDefaultStart() {
		return DateUtil.getDefaultStart("DASHBOARD_SEARCH_CONDITION.DEFAULT_START_DATE");
	}

	/**
	 * Dashboard 날짜검색 기본 종료일자
	 * @Mehtod Name : getDefaultEnd
	 * @return
	 */
	public static String getDashboardDefaultEnd() {
		return DateUtil.getDefaultEnd("DASHBOARD_SEARCH_CONDITION.DEFAULT_END_DATE");
	}

	/**
	 * 해당월의 시작 일자를 가져온다.
	 * @Mehtod Name : getFirstDay
	 * @param cal
	 * @return
	 */
	public static String getFirstDay(Calendar cal) {
		int firstDay = cal.getActualMinimum(Calendar.DATE);
		cal.set(Calendar.DATE, firstDay);
		return format(cal, "yyyy-MM-dd");
	}

	/**
	 * 현재월의 시작 일자를 가져온다.
	 * @Mehtod Name : getFirstDay
	 * @return
	 */
	public static String getFirstDay() {
		return getFirstDay(Calendar.getInstance());
	}

	/**
	 * 해당월의 마지막 일자를 가져온다.
	 * @Mehtod Name : getLastDay
	 * @param cal
	 * @return
	 */
	public static String getLastDay(Calendar cal) {
		int lastDay = cal.getActualMaximum(Calendar.DATE);
		cal.set(Calendar.DATE, lastDay);
		return format(cal, "yyyy-MM-dd");
	}

	/**
	 * 현재월 마지막 일자를 가져온다.
	 * @Mehtod Name : getLastDay
	 * @return
	 */
	public static String getLastDay() {
		return getLastDay(Calendar.getInstance());
	}
	
	/**
	 * @desc 시간을 흐름에 따라 데이트 표시 형식을 보여준다.
	 * @param fromDate
	 * @return
	 */
	public static String convertDate(String fromDate) {
		return DateUtil.convertDate(DateUtil.parse(fromDate), new Date());
	}
	
	/**
	 * @desc 시간을 흐름에 따라 데이트 표시 형식을 보여준다.
	 * @param fromDate
	 * @return
	 */
	public static String convertDate(Date fromDate) {
		return DateUtil.convertDate(fromDate, new Date());
	}

	/**
	 * @desc 시간을 흐름에 따라 데이트 표시 형식을 보여준다.
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public static String convertDate(String fromDate, String toDate) {
		return DateUtil.convertDate(DateUtil.parse(fromDate), DateUtil.parse(toDate));
	}

	/**
	 * @desc 시간을 흐름에 따라 데이트 표시 형식을 보여준다.
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public static String convertDate(Date fromDate, Date toDate) {

		long diff = toDate.getTime() - fromDate.getTime();

		int sec = (int) (diff / 1000);
		if (sec < 60) {
			return sec + " seconds ago";/*"초 전";*/
		}
		
		int min = sec / 60;
		if (min < 60) {
			return min + " miniutes ago";/*"분 전";*/
		}

		int hour = min / 60;
		if (hour < 24) {
			return hour + " hours ago";/*"시간 전";*/
		}

		int day = hour / 24;
		if (day < 30) {
			return day + " days"/*"일 "*/ + (hour % 24) + "hours ago";/*"시간 전";*/
		}

		int month = day / 30;
		if (month < 12) {
			return month + " months ago";/*"개월 전";*/
		}

		return DateUtil.format(fromDate.getTime(), "MMMonth ddDays"/*"MM월 dd일"*/);
	}

	public static List<Integer> monthsToArray(Date strDate, Date endDate) {
		
		Set<Integer> MM = new HashSet<Integer>();
		
		LocalDate date1 = new LocalDate(strDate);
		LocalDate date2 = new LocalDate(endDate);
		
		// strDate를 증가시키면서 endDate와 비교하여 
		// 둘 사이의 월 데이터를 추출하여 리스트에 저장한다.
		while (date1.isBefore(date2)) {
			MM.add(Integer.parseInt(date1.toString("MM")));
		    date1 = date1.plus(Period.months(1));
		}
		// 마지막 월이 누락되므로, 추가로 endDate의 월을 넣어준다.
		MM.add(date2.getMonthOfYear());

		// SET을 리스트에 담아서 리턴한다.
		List<Integer> list = new ArrayList<Integer>();
		list.addAll(MM);

		return list;
	}
	
	/**
	 * @desc 모니터링 조회시 검색기간 내의 월을 배열로 반환한다. (데이터 타입이 String일 경우)
	 * @param strDate
	 * @param endDate
	 * @return
	 */
	public static List<Integer> monthArray(String strDate, String endDate) {
		
		//Date 타입으로 변환
		Date start = parse(strDate);
		Date end   = parse(endDate);
		
		return monthsToArray(start,end);
	}

	//Date 기반으로 요일을 구할 수 있는 함수
	public static String getDayOfWeek(String date) {
		String day = "일";

        Date inDate = parse(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);

        switch(dayNum){
            case 1:
                day = "일";
                break;
            case 2:
                day = "월";
                break;
            case 3:
                day = "화";
                break;
            case 4:
                day = "수";
                break;
            case 5:
                day = "목";
                break;
            case 6:
                day = "금";
                break;
            case 7:
                day = "토";
                break;
        }

		return day;
	}

}