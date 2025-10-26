package kr.co.nextplayer.base.backend.util;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class JSONUtil {
	public static String getStringOfJsonp(String callback, Object obj) {
		String result = null;
		if(obj != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				result = callback + "("+mapper.writeValueAsString(obj)+")";
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result; 
	}
	
	public static String getStringOfJson(Object obj) {
		String result = null;
		if(obj != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				result = mapper.writeValueAsString(obj);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result; 
	}
	
	public static Map<?, ?> getJsonObject(String data) {
		Map<?, ?> result = null;
		if(data != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				result = mapper.readValue(data, Map.class);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result; 
	}
	
	public static String mapToJson(Map<String, Object> map) {
		return getStringOfJson(map);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonToMap(String json) {
		Map<String, Object> map = (Map<String, Object>)getJsonObject(json);
		if(map == null) {
			System.out.println("jsonToMap - ERROR: " + json);
		}
		return map;
	}
	
}
