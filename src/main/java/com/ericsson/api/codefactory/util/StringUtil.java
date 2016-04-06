package com.ericsson.api.codefactory.util;
/**
 * Created by yaochong.chen.
 */
public class StringUtil {
	
	public static boolean isEmpty(String source) {
		if(source == null || source.length() == 0 || source.trim().equals("null"))
			return true;
		else
			return false;
	}
	
	public static String getFirstUpperCase(String str){
		if(StringUtil.isEmpty(str)){
			return str;
		}else{
			return str.substring(0, 1).toUpperCase().concat(str.substring(1, str.length()));
		}
	}
}
