package com.ericsson.api.codefactory.util;

import java.io.File;
import java.util.Map;

import com.ericsson.api.codefactory.Config;
import com.google.gson.Gson;
/**
 * Created by yaochong.chen.
 */
import com.jayway.restassured.path.json.JsonPath;

public class JsonUtil {

	public static void main(String[] args) {
		Config config = get("/config.json", "config", Config.class);
		Gson gson = new Gson();
		System.out.println(gson.toJson(config));
	}

	public static <T> Map<String, T> parseJsonFile(File file) {
		JsonPath jsonPath = new JsonPath(file);
		return jsonPath.get();
	}

	public static Map<String, ?> parseJsonFile(String filePath) {
		JsonPath jsonPath = new JsonPath(JsonUtil.class.getResource(filePath));
		return (Map) jsonPath.get();
	}

	public static <T> T get(String filePath, String jsonPath, Class<T> clazz) {
		JsonPath j = new JsonPath(JsonUtil.class.getResource(filePath));
		return j.getObject(jsonPath, clazz);
	}
}
