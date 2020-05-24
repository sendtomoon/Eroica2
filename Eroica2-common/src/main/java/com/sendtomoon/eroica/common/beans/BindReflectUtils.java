package com.sendtomoon.eroica.common.beans;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

 class BindReflectUtils {

	
	 static Type getGenericType(Method readMethod,Method writeMethod){
		if(readMethod!=null){
			return readMethod.getGenericReturnType();
		}
		if(writeMethod!=null){
			return writeMethod.getGenericParameterTypes()[0];
		}
		return null;
	}
	
	 static Class<?> getCollectionGenericType(Type genericType){
		 if(genericType==null)return null;
		 return getGenericClass(genericType,0);
	}
	
	
	private static Class<?> getGenericClass(Type t, int i) {
		 if(t instanceof ParameterizedType){
			 ParameterizedType pt=(ParameterizedType)t;
			 Type[] types=pt.getActualTypeArguments();
			 if(types!=null && types.length>i){
				 Type genericType = pt.getActualTypeArguments()[i];
				 if(genericType!=null && genericType instanceof Class){
					 Class<?> temp= (Class<?>)genericType;
					 return temp;
				 }else if(genericType!=null && genericType instanceof ParameterizedType){
					  pt=(ParameterizedType)genericType;
					  return (Class<?>)pt.getRawType();
				 }
			 }
		 }
		return null;
	} 
	
}
