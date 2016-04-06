package com.ericsson.api.codefactory.util;

import java.io.File;
import java.util.Map;

import com.jayway.restassured.path.json.JsonPath;

public class JsonUtil {

	// private static final Gson gson = new Gson();

	public static void main(String[] args) {
		File file = new File("jsons/CreateAnIAMProfile.json");
		System.out.println(file.isDirectory());
		JsonPath jsonPath = new JsonPath(JsonUtil.class.getResourceAsStream("/jsons/CreateAnIAMProfile.json"));
		Map<String, String> o = (Map) jsonPath.get();
		System.out.println(o);
	}

	// public static Collection<?> getObjList(Type type, String filePath) {
	// return gson.fromJson(loadFile(filePath), type);
	// }
	//
	// /**
	// * get String from json file
	// *
	// * @param filePath
	// * @return
	// * @author xuechong
	// */
	// private static String loadFile(String filePath) {
	// try {
	// return FileUtils.readFileToString(
	// new
	// File(Thread.currentThread().getContextClassLoader().getResource(filePath).toURI()));
	// } catch (IOException e) {
	// e.printStackTrace();
	// return "";
	// } catch (URISyntaxException e) {
	// e.printStackTrace();
	// return "";
	// }
	// }
}
