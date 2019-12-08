package com.sendtomoon.eroica.common.beans.map;

import java.util.Map;

import com.sendtomoon.eroica.common.beans.BeansConfigInfo;
import com.sendtomoon.eroica.common.beans.ParameterInfo;

public class BeanMapUtils {
	
	private BeansConfigInfo beansConfigInfo=new BeansConfigInfo();
	
	private Bean2Map bean2Map=new Bean2Map(beansConfigInfo);
	
	private Map2Bean map2Bean=new Map2Bean(beansConfigInfo);

	public  Object _toMap(Object value){
		return bean2Map.toMap(value);
	}
	
	public  Object _toMap(ParameterInfo ps,Object value){
		return bean2Map.toMapField(ps, value);
	}
	
	public  Object _toBean(Class<?> clazz,Map<?,?> datas){
		return map2Bean.transform(clazz, datas);
	}
	
	public  Object _toBean(Object target,Map<?,?> datas){
		return map2Bean.transform(target, datas);
	}
	
	public  Object _toBean(ParameterInfo ps,Object value){
		return map2Bean.transform(ps, value);
	}
	
	//-------------------------------------------
	
	/**@deprecated*/
	public static Object toMap(Object value){
		return new BeanMapUtils()._toMap(value);
	}
	
	/***
	 * @deprecated
	 * @param value
	 * @param nestedLevels
	 * @return
	 */
	public static Object toMap(Object value,int nestedLevels){
		return new BeanMapUtils()._toMap(value);
	}
	
	/**@deprecated*/
	public static Object toMap(ParameterInfo ps,Object value){
		return new BeanMapUtils()._toMap(ps,value);
	}
	
	/**@deprecated*/
	public static Object toBean(Class<?> clazz,Map<?,?> datas){
		return new BeanMapUtils()._toBean(clazz,datas);
	}
	
	/**@deprecated*/
	public static  Object toBean(ParameterInfo ps,Object value){
		return new BeanMapUtils()._toBean(ps,value);
	}
	
	public synchronized void clear(){
		beansConfigInfo.clear();
	}
}
