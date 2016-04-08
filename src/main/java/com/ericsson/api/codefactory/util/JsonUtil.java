package com.ericsson.api.codefactory.util;

import java.io.File;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
/**
 * Created by yaochong.chen.
 */
import com.jayway.restassured.path.json.JsonPath;

public class JsonUtil {

	public static String jsonFormatter(String uglyJSONString) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(uglyJSONString);
		String prettyJsonString = gson.toJson(je);
		return prettyJsonString;
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
