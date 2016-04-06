package com.ericsson.api.codefactory;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.VelocityContext;
import com.ericsson.api.codefactory.util.JsonUtil;
import com.ericsson.api.codefactory.util.StringUtil;
import com.ericsson.api.codefactory.util.VelocityUtil;
/**
 * Created by yaochong.chen.
 */
public class CFMain {

	public static void main(String[] args) throws Exception {
		String filePath = "/jsons/CreateAnIAMProfile.json";
		if ((null != args) && (args.length > 0)) {
			filePath = args[0];
		}
		generateCode(filePath);
	}
	public static void generateCode(String filePath)throws Exception{
		File file = new File(CFMain.class.getResource(filePath).getFile());
		if(file.exists()){
			if(file.isDirectory()){
				
			}else{
				VelocityUtil.VelocityProxy vp = VelocityUtil.getDefaultInitVelocity();				
				VelocityContext context = new VelocityContext();
				context.put("package", "com.ericsson.provisioning");
				context.put("authors", "yaochong.chen");
				context.put("createDate", new Date(System.currentTimeMillis()));
				Map<String,?> jsonMap = JsonUtil.parseJsonFile(filePath);
				for(Map.Entry<String, ?> entry : jsonMap.entrySet()){
					String className = entry.getKey();
					List<String> list = (ArrayList)entry.getValue();
					VelocityContext contextEntity = new VelocityContext(context);
					contextEntity.put("className", className);
					contextEntity.put("list", list);
					StringWriter sw = new StringWriter();
					if( vp.mergeTemplate("testCase.vm", "utf-8", contextEntity, sw)){
						String javaClz = sw.toString();
						File srcfile = new File(File.separator+ StringUtil.getFirstUpperCase(className)+".java");
						FileUtils.write(srcfile, javaClz);
					}
				}
			}
		}else{
			throw new Exception("the file is not exists:" + filePath);
		}
	}
}
