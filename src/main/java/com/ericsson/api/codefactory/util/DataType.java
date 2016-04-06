package com.ericsson.api.codefactory.util;
/**
 * Created by yaochong.chen.
 */
public enum DataType {
	INT("int","java.lang.Integer"),
	
	FLOAT("float","java.lang.Float"),
	
	DOUBLE("double","java.lang.Double"),
	
	INTWRAPPER("Integer","java.lang.Integer"),
	
	FLOATWRAPPER("Float","java.lang.Float"),
	
	DOUBLEWRAPPER("Double","java.lang.Double");
	
	private String typeName;
	
	private String type;
	
	

	private DataType(String typeName, String type) {
		this.typeName = typeName;
		this.type = type;
	}


	public static DataType getBasicTypeClz(String typeName ){
		for(DataType dataType : DataType.values()){
			if( dataType.getTypeName().equals(typeName)){
				return dataType;
			}
		}
		return null;
	}
	
	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}



	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
}
