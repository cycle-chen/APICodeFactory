package com.ericsson.api.codefactory.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Created by yaochong.chen.
 */
import com.jayway.restassured.path.json.JsonPath;

public class JsonUtil {

	public static void main(String[] args) {
		Map<String,?> map = parseJsonFile("/jsons/CreateAnIAMProfile.json");
		for(Map.Entry<String, ?> entry :map.entrySet()){
			List<String> list = (ArrayList)entry.getValue();
			System.out.println(list);
		}
	}
	public static Map<String,?> parseJsonFile(String filePath){
		JsonPath jsonPath = new JsonPath(JsonUtil.class.getResource(filePath));
		return (Map)jsonPath.get();
	}
}
