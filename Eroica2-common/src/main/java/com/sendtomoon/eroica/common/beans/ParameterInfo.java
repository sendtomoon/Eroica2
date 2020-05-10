package com.sendtomoon.eroica.common.beans;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.ui.ModelMap;

import com.sendtomoon.eroica.common.beans.format.FieldFormatter;




public abstract class ParameterInfo {
	
	
	public static final int CT_NOT=0;
	
	public static final int CT_ARRAY=1;
	
	public static final int CT_COLLECTION=2;
	
	public static final int CT_BYTES=3;
	
	public static final int CT_MAP=4;
	
	//--------------------------------------
	
	public static final int T_BEAN=1;
	
	public static final int T_DATE=2;
	
	public static final int T_ENUM=3;
	
	public static final int T_STRING=4;
	
	public static final int T_NUMBER=5;
	
	public static final int T_BOOL=6;
	
	public static final int T_CHAR=8;
	
	
	public static final int T_UNKOWN=0;
	
	private final static Map<Class<?>,Class<?>> TYPES_MAP=new HashMap<Class<?>,Class<?>>();
	
	static{
		TYPES_MAP.put(int.class, Integer.class);
		TYPES_MAP.put(double.class, Double.class);
		TYPES_MAP.put(boolean.class, Boolean.class);
		TYPES_MAP.put(long.class, Long.class);
		TYPES_MAP.put(char.class, Character.class);
		TYPES_MAP.put(float.class, Float.class);
		TYPES_MAP.put(short.class, Short.class);
		TYPES_MAP.put(byte.class, Byte.class);
		//--------------------------------------
		TYPES_MAP.put(Collection.class, ArrayList.class);
		TYPES_MAP.put(List.class, ArrayList.class);
		TYPES_MAP.put(LinkedList.class, LinkedList.class);
		TYPES_MAP.put(ArrayList.class, ArrayList.class);
		TYPES_MAP.put(Set.class, HashSet.class);
		TYPES_MAP.put(HashSet.class, HashSet.class);
		TYPES_MAP.put(LinkedHashSet.class, LinkedHashSet.class);
		//
		TYPES_MAP.put(Map.class, HashMap.class);
		TYPES_MAP.put(HashMap.class, HashMap.class);
		TYPES_MAP.put(Hashtable.class, Hashtable.class);
		TYPES_MAP.put(Properties.class, Properties.class);
		TYPES_MAP.put(ModelMap.class, ModelMap.class);
		TYPES_MAP.put(LinkedHashMap.class, LinkedHashMap.class);
	}
	
	
	
	
	private Class<?> paramClass;
	
	private String name;
	
	private int componentType;
	
	private Class<?> componentInstanceClass;
	
	private int bindType;
	
	private Class<?> bindClass;
	
	private FieldFormatter  formatter;
	
	public ParameterInfo(Class<?> paramClass,Type genericType){
		this(null,paramClass,genericType);
	}
	

	public ParameterInfo(String name,Class<?> paramClass,Type genericType){
		this.name=name;
		this.paramClass=paramClass;
		//--------------------
		if(paramClass.isArray()){
			Class<?> memberClass=paramClass.getComponentType();
			if(memberClass.isArray()){
				_throwEx(" not support mutil-dimension array.");
			}
			if(memberClass.equals(Byte.class) || memberClass.equals(byte.class)){
				componentType=CT_BYTES;
			}else{
				componentType=CT_ARRAY;
			}
			componentInstanceClass=memberClass;
			//-
			resolveBindClass(memberClass);
			//------------------------------
		}else if(Collection.class.isAssignableFrom(paramClass)){
			componentType=CT_COLLECTION;
			componentInstanceClass=resolveComponentInstanceClass(paramClass);
			Class<?> gClass=BindReflectUtils.getCollectionGenericType(genericType);
			if(gClass==null){
				 this.bindType=T_UNKOWN;
			}else{
				resolveBindClass(gClass);
			}
		}else if(Map.class.isAssignableFrom(paramClass)){
			componentType=CT_MAP;
			componentInstanceClass=resolveComponentInstanceClass(paramClass);
		}else{
			resolveBindClass(paramClass);
		}
	}
	
	private void resolveBindClass(Class<?> clazz){
		this.bindClass=getInstanceClass(clazz);
		if(bindClass==null){
			bindClass=clazz;
		}
		this.bindType=resolveBindType(bindClass);
	}
	
	
	
	private  int resolveBindType(Class<?> c){
		int bindType=T_BEAN;
		if(c.equals(String.class)){
			bindType=T_STRING;
			//
		}else if(Number.class.isAssignableFrom(c)){
			bindType=T_NUMBER;
			//
		}else if(Boolean.class.equals(c)){
			bindType=T_BOOL;
		}else if(Character.class.equals(c)){
			bindType=T_CHAR;
		}else if(Date.class.isAssignableFrom(c) || Calendar.class.equals(c)){
			bindType=T_DATE;
		}else if(c.isEnum()){
			bindType=T_ENUM;
		}else if(Collection.class.isAssignableFrom(c)){
			bindType= T_UNKOWN;
		}else if(c.getName().startsWith("java")){
			bindType= T_UNKOWN;
		}
		return bindType;
	}
	
	private  Class<?> getInstanceClass(Class<?> clazz){
		return TYPES_MAP.get(clazz);
	}

	
	private Class<?> resolveComponentInstanceClass(Class<?> clazz){
		Class<?> c=getInstanceClass(clazz);
		if(c==null && !clazz.isInterface() && !clazz.isArray() && !clazz.isEnum()){
			c=clazz;
		}
		return c;
	}
	
	
	
	private void _throwEx(String msg){
		throw new IllegalArgumentException("parameter<"+this.name+"> defined error:"+msg);
	}
	
	public String getName() {
		return name;
	}
	
	


	public int getComponentType() {
		return componentType;
	}


	public void setComponentType(int componentType) {
		this.componentType = componentType;
	}


	public Class<?> getComponentInstanceClass() {
		return componentInstanceClass;
	}


	public void setComponentInstanceClass(Class<?> componentInstanceClass) {
		this.componentInstanceClass = componentInstanceClass;
	}


	public void setBindClass(Class<?> bindClass) {
		this.bindClass = bindClass;
	}
	

	
	
	
	
	public FieldFormatter getFormatter() {
		return formatter;
	}
	public void setFormatter(FieldFormatter formatter) {
		this.formatter = formatter;
	}


	


	
	public void setBindType(int bindType) {
		this.bindType = bindType;
	}




	public int getBindType() {
		return bindType;
	}


	public Class<?> getBindClass() {
		return bindClass;
	}

	
	public Class<?> getParamClass() {
		return paramClass;
	}


	public void setParamClass(Class<?> paramClass) {
		this.paramClass = paramClass;
	}


	public void setName(String name) {
		this.name = name;
	}


	public abstract void set(Object target,Object value) throws Exception;

	public abstract Object get(Object target)  throws Exception;
	
	
}
