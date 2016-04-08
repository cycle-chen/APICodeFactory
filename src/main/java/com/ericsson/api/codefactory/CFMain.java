package com.ericsson.api.codefactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import com.ericsson.api.codefactory.util.DateTimeUtil;
import com.ericsson.api.codefactory.util.JsonUtil;
import com.ericsson.api.codefactory.util.StringUtil;
import com.ericsson.api.codefactory.util.VelocityUtil;
import com.google.gson.Gson;

/**
 * Created by yaochong.chen.
 */
public class CFMain {
	private static Config CONFIG = new Config();
	private static String configPath = "/config.json";
	private static String apiInfoPath = "/apiInfo.properties";
	private static String jsonFilePath = "";
	private static String javaFilePath = "";
	private static final String SEPARATOR = System.getProperty("file.separator");

	static {
		String userDir = System.getProperty("user.dir");
		jsonFilePath = userDir.substring(0, userDir.lastIndexOf(SEPARATOR) + 1) + "jsons" + SEPARATOR;
		javaFilePath = userDir.substring(0, userDir.lastIndexOf(SEPARATOR) + 1) + "java" + SEPARATOR;
	}

	public static void main(String[] args) throws Exception {
		if (null != args) {
			if (args.length > 0) {
				configPath = args[0];
			}
			if (args.length > 1) {
				apiInfoPath = args[1];
			}
			if (args.length > 2) {
				jsonFilePath = args[2];
			}
		}
		CFMain.init(configPath);
		LinkedHashMap<String, LinkedHashMap<String, Object>> apiInfoMap = CFMain.generateApiJsonByApiInfo(apiInfoPath,
				jsonFilePath);
		CFMain.generateCode(apiInfoMap);
	}

	public static void generateCode(LinkedHashMap<String, LinkedHashMap<String, Object>> apiInfoMap) throws Exception {
		VelocityUtil.VelocityProxy vp = VelocityUtil.getDefaultInitVelocity();
		VelocityContext context = new VelocityContext();
		context.put("package", CONFIG.getPackages());
		context.put("authors", CONFIG.getAuthors());
		context.put("createDate", DateTimeUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
		context.put("importList", CONFIG.getImportList());
		for (Map.Entry<String, LinkedHashMap<String, Object>> entry : apiInfoMap.entrySet()) {
			String className = entry.getKey();
			LinkedHashMap<String, Object> methodMap = entry.getValue();
			VelocityContext contextEntity = new VelocityContext(context);
			contextEntity.put("className", className);
			contextEntity.put("url", methodMap.get("url"));
			methodMap.remove("url");
			contextEntity.put("method", methodMap.get("method"));
			methodMap.remove("method");
			contextEntity.put("methodMap", methodMap);
			StringWriter sw = new StringWriter();
			// the package of the java file
			File packagePath = new File(javaFilePath + CONFIG.getPackages().replaceAll("\\.", "/"));
			if (!packagePath.exists()) {
				packagePath.mkdirs();
			}
			if (vp.mergeTemplate("testCaseSuccess.vm", "utf-8", contextEntity, sw)) {
				String javaClz = sw.toString();
				File srcfile = new File(packagePath.getAbsolutePath() + "/" + className.toLowerCase() + "/"
						+ StringUtil.getFirstUpperCase(className + "SuccessTest") + ".java");
				FileUtils.write(srcfile, javaClz);
			}
			sw = new StringWriter();
			if (vp.mergeTemplate("testCaseError.vm", "utf-8", contextEntity, sw)) {
				String javaClz = sw.toString();
				File srcfile = new File(packagePath.getAbsolutePath() + "/" + className.toLowerCase() + "/"
						+ StringUtil.getFirstUpperCase(className + "ErrorTest") + ".java");
				FileUtils.write(srcfile, javaClz);
			}
		}
	}

	private static void init(String configPath) {
		CONFIG = JsonUtil.get(configPath, "config", Config.class);
	}

	private static LinkedHashMap<String, LinkedHashMap<String, Object>> generateApiJsonByApiInfo(String apiInfoPath,
			String jsonFilePath) throws Exception {
		Properties p = new Properties();
		p.load(CFMain.class.getResourceAsStream(apiInfoPath));
		Set<Entry<Object, Object>> sets = p.entrySet();
		// apiInfoMap include all of the API information in apiInfo.properties
		LinkedHashMap<String, LinkedHashMap<String, Object>> apiInfoMap = new LinkedHashMap<>();
		for (Map.Entry<Object, Object> entry : sets) {
			String key = entry.getKey().toString();
			String value = ObjectUtils.defaultIfNull(entry.getValue(), "").toString();
			if (StringUtils.isNotBlank(value) && !key.matches(".+\\.params")) {
				// rootMap include an api information
				LinkedHashMap<String, LinkedHashMap<String, Object>> rootMap = new LinkedHashMap<>();
				String className = key;
				String[] methodInfos = value.split(",");
				// methodMap include a method of api
				LinkedHashMap<String, Object> methodMap = new LinkedHashMap<>();
				String[] params = p.getProperty(className + ".params").split(",");
				methodMap.put("url", params[0]);
				methodMap.put("method", params[1]);
				for (String methodInfo : methodInfos) {
					// methodPropMap include the properties of method
					LinkedHashMap<String, String> methodPropMap = new LinkedHashMap<>();
					String[] methodProps = methodInfo.split("\\|");
					if (methodProps.length <= 1) {
						System.out.println();
					}
					methodPropMap.put("status", StringUtils.trim(methodProps[1]));
					if (!methodInfo.contains("success") && (methodProps.length == 3)) {
						methodPropMap.put("error_code", StringUtils.trim(methodProps[2]));
					}
					methodMap.put(StringUtils.trim(methodProps[0]), methodPropMap);
				}
				rootMap.put(className, methodMap);
				apiInfoMap.putAll(rootMap);
				// write the json file of the api
				CFMain.wirteFile(jsonFilePath, className, rootMap);
			}
		}
		return apiInfoMap;
	}

	private static void wirteFile(String jsonFilePath, String className,
			LinkedHashMap<String, LinkedHashMap<String, Object>> rootMap) {
		try {
			File jsonDir = new File(jsonFilePath);
			if (!jsonDir.exists()) {
				jsonDir.mkdirs();
			}
			FileWriter writer = new FileWriter(jsonFilePath + className + ".json");
			writer.write(JsonUtil.jsonFormatter(new Gson().toJson(rootMap)));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
