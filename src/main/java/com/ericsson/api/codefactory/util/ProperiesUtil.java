package com.ericsson.api.codefactory.util;

import java.io.IOException;
import java.util.Properties;

public class ProperiesUtil {
	
	private static final String DEFAULT_LOCATION = "config.properties";
	
	public static String get(String key){
		return get(key,DEFAULT_LOCATION);
	}
	
	private static String get(String key, String fileName) {
		Properties propertie = new Properties();
		try {
			propertie.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return propertie.getProperty(key);
	}
}
