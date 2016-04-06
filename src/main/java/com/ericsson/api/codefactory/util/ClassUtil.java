package com.ericsson.api.codefactory.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Created by yaochong.chen.
 */
public class ClassUtil {
	
	private static Log log = LogFactory.getLog(ClassUtil.class);
	
	private static Map<Class<?>,Map<String,String>> classInfo = Collections.synchronizedMap(new HashMap<Class<?>,Map<String,String>>());
	
	public  static <T> BeanInfo getBeanInfo( Class<T>  beanClass ){
		try {
			return Introspector.getBeanInfo(beanClass, Object.class);
		} catch (IntrospectionException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获得类定义的属性
	 * 
	 * @param beanClass
	 * @return
	 */
	public static <T> List<String> getBeanProperties(Class<T> beanClass){
		BeanInfo beanInfo = getBeanInfo(beanClass);
		List<String> properties = new ArrayList<String>();
		if( beanInfo != null ){
			PropertyDescriptor[] pdArr = beanInfo.getPropertyDescriptors();
			for( PropertyDescriptor pd : pdArr ){
				properties.add(pd.getName());
			}
		}
		return properties;
	}
	
	/**
	 *  获得类定义的属性
	 *  
	 * @param beanClass
	 * @param noCollectionProperty 不返回是集合类型的属性 （true）
	 * @return
	 */
	public static <T> List<String> getBeanProperties(Class<T> beanClass,boolean noCollectionProperty){
		BeanInfo beanInfo = getBeanInfo(beanClass);
		List<String> properties = new ArrayList<String>();
		if( beanInfo != null ){
			PropertyDescriptor[] pdArr = beanInfo.getPropertyDescriptors();
			for( PropertyDescriptor pd : pdArr ){
				if(noCollectionProperty ){
					if(!Collection.class.isAssignableFrom(pd.getPropertyType())){
						properties.add(pd.getName());
					}
				}else{
					properties.add(pd.getName());
				}
			}
		}
		return properties;
	}
	
	/**
	 * 
	 * @param properties 所有实体中属性集合
	 * @param propertyName 
	 * @return
	 */
	public static String getStandardProperty(List<String> properties,String propertyName){
		for(String p : properties){
			if( p.equalsIgnoreCase(propertyName)){
				return p;
			}
		}
		return null;
		
	}
	
	/**
	 * 填充属性的值
	 * 
	 * set方法
	 * 
	 * @param beanClass
	 * @param propertyName
	 * @param obj
	 * @param value
	 */
	public static <T> void  fillPropertyValue(Class<T> beanClass,String propertyName,T obj,Object value) throws RuntimeException{
		PropertyDescriptor pd = getPropertyDescriptor(beanClass,propertyName);
		if( pd != null ){
			try {
				Class<?> ptype = pd.getPropertyType();
				value = cast(ptype,value);
				pd.getWriteMethod().invoke(obj, value);
			} catch (Exception e) {
				throw new RuntimeException(" 填充 "+ beanClass.getName() +" " +propertyName +" 属性值为["+value+"]出错 "+e.getMessage(),e);
			}
		}
	}
	
	/**
	 * 读取属性值
	 * 
	 * get方法
	 * 
	 * @param obj
	 * @param propertyName
	 * @return
	 */
	public static Object getPropertyVlaue(Object obj,String propertyName ){
		PropertyDescriptor pd = getPropertyDescriptor(obj.getClass(), propertyName);
		if( pd != null ){
			try {
				return pd.getReadMethod().invoke(obj, null);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(" 读取 "+ obj.getClass().getName() +" " +propertyName +" 属性值出错 "+e.getMessage() );
			}
		}
		return null;
	}
	
	
	
	
	/**
	 * 得到属性的描述器
	 * 
	 * @param beanClass
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static PropertyDescriptor getPropertyDescriptor(Class beanClass,String propertyName){
		BeanInfo beanInfo = getBeanInfo(beanClass);
		if( beanInfo != null ){
			PropertyDescriptor[] pdArr = beanInfo.getPropertyDescriptors();
			for( PropertyDescriptor pd : pdArr ){
				if( pd.getName().equalsIgnoreCase(propertyName)){
					return pd;
				}
			}
		}
		return null;
	}

	/**
	 * 将字符串值转换成包装类型
	 * 
	 * @param clz
	 * @param obj
	 * @return
	 */
	public static Object cast(Class clz ,Object obj) throws  RuntimeException{
		String simpleName = clz.getSimpleName();
		DataType basicType = DataType.getBasicTypeClz(simpleName);
		if( basicType != null ){
			try {
				Class c = Class.forName(basicType.getType());
				Method m = c.getMethod("valueOf", String.class);
				return m.invoke(null, obj.toString());
			} catch (Exception e) {
				throw new RuntimeException(" 调用基本类型的包装类 valueOf出错 ",e);
			}
		}
		return obj;
	}
	

	
	/**
	 * from spring code
	 * 
	 * @return
	 */
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		}
		catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system class loader...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = ClassUtil.class.getClassLoader();
		}
		return cl;
	}
	
	/**
	 * 
	 * @param clz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> T instance(Class<T> clz) throws InstantiationException, IllegalAccessException{
		return clz.newInstance();
	}
	
}
