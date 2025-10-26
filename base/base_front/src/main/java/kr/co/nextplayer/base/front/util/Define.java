package kr.co.nextplayer.base.front.util;

public class Define {
	public static final String ROOT_URL = "/NPMob/";

//	public static final boolean TEST_BED = true; // LOCAL
	public static final boolean TEST_BED = false; // LIVE

	public static final String LOCAL_WEB_PATH = "http://127.0.0.1:8080/NP"; // local server qwqwqw
	public static final String LIVE_WEB_PATH = "http://115.68.182.186:10933/NP"; // LIVE server

	public static final String LOCAL_SAVE_FILE_PATH = "/Users/content_nextplayer/";// "/content_nextplayer/upload/";
																					// //common
	public static final String LIVE_SAVE_FILE_PATH = "/content_nextplayer/"; // common

	public static final String STR_SUCCESS = "SUCCESS";
	public static final String STR_ERROR = "ERROR";

	public static final String S_HHMMSS = "00:00:00";
	public static final String E_HHMMSS = "23:59:59";
	public static final String S_MMSS = "00:00";
	public static final String E_MMSS = "59:59";
	public static final String S_SS = "00";
	public static final String E_SS = "59";
	public static final String T_DELIMITER = ":";
	// 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26
	public static final String[] AR_ExcelColumns = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
			"N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	public static final int TEAM_Column = 11; // 팀 엑셀 컬럼 갯수
	
	public static final int LEAGUE_INFO_Column = 5; // 리그 기본 정보 엑셀 컬럼 갯수
	public static final int LEAGUE_TEAM_Column = 2; // 리그 참가팀 정보 엑셀 컬럼 갯수
	public static final int LEAGUE_MATCH_Column = 9; // 리그 경기일정 정보 엑셀 컬럼 갯수
	
	public static final int CUP_INFO_Column = 13; // 대회 리그 기본 정보 엑셀 컬럼 갯수
	//public static final int CUP_TEAM_Column = 3; // 대회 참가팀 정보 엑셀 컬럼 갯수
	public static final int CUP_TEAM_ONLY_TOUR_Column = 3; // 대회( ONLY토너먼트) 참가팀 정보 엑셀 컬럼 갯수 
	public static final int CUP_SUB_MATCH_Column = 11; // 대회 예선 경기일정 정보 엑셀 컬럼 갯수
	public static final int CUP_MAIN_MATCH_Column = 11; // 대회 본선 경기일정 정보 엑셀 컬럼 갯수
	public static final int CUP_TOUR_MATCH_Column = 14; // 대회 토너먼트 경기일정 정보 엑셀 컬럼 갯수

	public static final int COUNT_PER_PAGE = 20; // Page View 갯수
	public static final double COUNT_PAGE = 20.0; // Page View 갯수

	public static final int COUNT_PER_PAGE_50 = 50; // Page View 갯수
	public static final double COUNT_PAGE_50 = 50.0; // Page View 갯수

	public static final int COUNT_PER_PAGE_10 = 10; // Page View 갯수
	public static final double COUNT_PAGE_10 = 10.0; // Page View 갯수

	public static final String MODE_ADD = "0";
	public static final String MODE_FIX = "1";
	public static final String MODE_DELETE = "2";
	public static final String MODE_MOVE = "5";
	public static final String MODE_ALL_FIX = "6";
	public static final String MODE_DATA_RESET = "10";

	// 정상
	public static final int RESULT_OK = 0;

	// 내부오류 : 100번대
	public static final int DATA_NOT_EMPTY = 98;
	public static final int DATA_NULL = 99;
	public static final int DATA_ERROR = 100;
	public static final int DATA_DUPLICATE = 101;

	public static final int SESSION_EXPIRED = 111;

	public static final int CMD_NULL = 120;
	public static final int PARAM_NULL = 121;
	public static final int PARAM_VALUE_EMPTY = 122;

	// 외부오류 : 400번대
	public static final int DB_CONNECTION_ERROR = 400;
	public static final int NETWORK_CONNECTION_ERROR = 401;

}
