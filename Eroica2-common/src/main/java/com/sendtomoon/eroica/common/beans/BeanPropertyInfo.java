package com.sendtomoon.eroica.common.beans;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.springframework.beans.InvalidPropertyException;



public class BeanPropertyInfo extends ParameterInfo {
	
	//private PropertyDescriptor descriptor;
	
	private Method writeMethod;
	
	private Method readMethod;
	
	private Class<?> targetClass;
	
	public BeanPropertyInfo(Field field,Method writeMethod,Method readMethod,Class<?> targetClass){
		super(field.getName(),field.getType()
				,BindReflectUtils.getGenericType(readMethod,writeMethod));
		this.writeMethod=writeMethod;
		this.readMethod=readMethod;
		this.targetClass=targetClass;
		//this.descriptor=descriptor;
	}
	
	protected void _throwEx(String msg){
		throw new InvalidPropertyException(targetClass,getName(),msg);
	}
	
	public void set(Object target,Object value) throws Exception{
		//if(writeMethod==null)_throwEx("not found writeMethod.");
		if(writeMethod==null){
			return ;
		}
		writeMethod.invoke(target, value);
	}
	 
	
	
	public Object get(Object target)  throws Exception{
		if(readMethod==null){
			return null;
		}
		//if(readMethod==null)_throwEx("not found readMethod.");
		return readMethod.invoke(target);
	}
	
	public Method getReadMethod(){
		return readMethod;
	}
	
	public Method getWriteMethod(){
		return writeMethod;
	}
}
