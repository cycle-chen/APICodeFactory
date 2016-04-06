package com.ericsson.api.codefactory;

public class CFMain {

	public static void main(String[] args) throws Exception {
		String filePath = null;
		if ((null != args) && (args.length > 0)) {
			filePath = args[0];
		} else {
			throw new Exception("miss the path of the file");
		}

	}

}
