package com.sendtomoon.eroica.common.appclient;

import java.util.List;
import java.util.Map;

public interface IServiceResults extends Map<Object,Object>{

	List<ServiceResults> listSubResults(String key);
	
	ServiceResults getSubResults(String key);
	
	<T> T toDTO(Class<T> dtoClazz);
	
	<T> T toDTO(String key,Class<T> dtoClazz);
	
	<T> List<T> toDTOList(String key,Class<T> dtoClazz);
	
	int getInt(Object key,int defaultValue);

	int getInt(Object key);
	
	long getLong(Object key);
	
	long getLong(Object key,long defaultValue);
	
	double getDouble(Object key,double defaultValue);
	
	double getDouble(Object key);
	
	
	String getString(Object key);
	
	String getString(Object key,String defaultValue);
	
	 boolean getBool(Object key);
	 
	 boolean getBool(Object key,boolean defaultValue);
	 
	
}
