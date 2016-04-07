package com.ericsson.api.codefactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import com.ericsson.api.codefactory.util.DateTimeUtil;
import com.ericsson.api.codefactory.util.JsonUtil;
import com.ericsson.api.codefactory.util.StringUtil;
import com.ericsson.api.codefactory.util.VelocityUtil;

/**
 * Created by yaochong.chen.
 */
public class CFMain {

	public static void main(String[] args) throws Exception {
		String filePath = "/jsons/";
		String apiInfoPath = "/apiInfo.properties";
		String configPath = "/config.json";
		if (null != args) {
			if (args.length > 0) {
				filePath = args[0];
			}
			if (args.length > 1) {
				apiInfoPath = args[1];
			}
			if (args.length > 2) {
				configPath = args[2];
			}
		}
		generateApiJsonByApiInfo(apiInfoPath);
		// generateCode(filePath,configPath);
	}

	public static void generateCode(String filePath, String configPath) throws Exception {
		Config config = JsonUtil.get(configPath, "config", Config.class);
		VelocityUtil.VelocityProxy vp = VelocityUtil.getDefaultInitVelocity();
		VelocityContext context = new VelocityContext();
		context.put("package", config.getPackages());
		context.put("authors", config.getAuthors());
		context.put("createDate", DateTimeUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
		context.put("importList", config.getImportList());
		File file = new File(CFMain.class.getResource(filePath).getFile());
		Map<String, Map<String, String>> jsonMap = null;
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.contains(".json") ? true : false;
					}
				});
				jsonMap = new HashMap<String, Map<String, String>>();
				for (File jsonFile : files) {
					Map<String, Map<String, String>> tempMap = JsonUtil.parseJsonFile(jsonFile);
					jsonMap.putAll(tempMap);
				}
			} else {
				jsonMap = JsonUtil.parseJsonFile(file);
			}
			for (Map.Entry<String, ?> entry : jsonMap.entrySet()) {
				String className = entry.getKey();
				Map<String, Map<String, String>> subMap = (Map) entry.getValue();
				VelocityContext contextEntity = new VelocityContext(context);
				contextEntity.put("className", className);
				contextEntity.put("url", subMap.get("url"));
				subMap.remove("url");
				contextEntity.put("subMap", subMap);
				StringWriter sw = new StringWriter();
				if (vp.mergeTemplate("testCase.vm", "utf-8", contextEntity, sw)) {
					String javaClz = sw.toString();
					File srcfile = new File(StringUtil.getFirstUpperCase(className) + ".java");
					FileUtils.write(srcfile, javaClz);
				}
			}
		} else {
			throw new Exception("the file is not exists:" + filePath);
		}
	}

	private static void generateApiJsonByApiInfo(String apiInfoPath) throws Exception {
		Properties p = new Properties();
		p.load(CFMain.class.getResourceAsStream(apiInfoPath));
		Set<Entry<Object, Object>> sets = p.entrySet();
		TreeMap<String, TreeMap<String, TreeMap<String, String>>> allMap = new TreeMap<>();
		for (Map.Entry<Object, Object> entry : sets) {
			String key = entry.getKey().toString();
			String value = ObjectUtils.defaultIfNull(entry.getValue(), "").toString();
			if (!StringUtils.isNotBlank(value)) {
				TreeMap<String, TreeMap<String, TreeMap<String, String>>> rootMap = new TreeMap<>();
				String className = key;
				String[] methodInfos = value.split(",");
				for (String methodInfo : methodInfos) {
					TreeMap<String, TreeMap<String, String>> methodMap = new TreeMap<>();
					String[] methodStatusErrorCodes = methodInfo.split("\\|");
					if (!"success".equals(methodInfo)) {

					}
					for (String methodStatusErrorCode : methodStatusErrorCodes) {
						TreeMap<String, String> methodStatusErrorCodeMap = new TreeMap<>();

					}
				}
			}
		}
	}
}
