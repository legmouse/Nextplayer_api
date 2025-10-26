package kr.co.nextplayer.base.front.util;

import org.apache.commons.configuration2.Configuration;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Apache Common Configuration 지원 유틸
 * @version : 1.0
 * @author :  Copyright (c) 2015 by MIRINCOM CORP. All Rights Reserved.
 */
public class Config {

	private static Configuration commonConfig;

	/**
	 * Constructor of CommonConfiguration.java class
	 */
	private Config() {}

	@Autowired
	@Qualifier("commonConfig")
	public void setCommon(Configuration config) {
		commonConfig = config;
	}

	public static Configuration getCommon() {
		return commonConfig;
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
	
	public static  void sendResponseStrMessage(HttpServletResponse response, String szRes) {
		try {
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Content-Type", "text/html; charset=UTF-8");
			response.getOutputStream().write(szRes.getBytes());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
}