package com.sendtomoon.eroica.common.beans.map;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.BeanUtils;

import com.sendtomoon.eroica.common.beans.BeanTransformException;
import com.sendtomoon.eroica.common.beans.BeansConfigInfo;
import com.sendtomoon.eroica.common.beans.ParameterInfo;


@SuppressWarnings({"unchecked","rawtypes"})
 class Map2Bean{
	
	private BeansConfigInfo beansConfigInfo;
	
	public Map2Bean(BeansConfigInfo beansConfigInfo){
		this.beansConfigInfo=beansConfigInfo;
	}
	
	public Map2Bean(){
		this.beansConfigInfo=new BeansConfigInfo();
	}
	
	   Object transform(Class<?> clazz,Map<?,?> datas){
			try{
				return map2Bean(clazz,datas);
			}catch(Throwable th){
				throw new BeanTransformException("Transform map to bean["+clazz.getName()+"]  error, datas=[{"+(datas==null?"":datas.getClass().getName())+"}"+datas+"]",th);
			}
	 }
	   
	   Object transform(Object targetBean,Map<?,?> datas){
			try{
				return map2Bean(targetBean,datas);
			}catch(Throwable th){
				throw new BeanTransformException("Transform map to bean["+targetBean.getClass().getName()+"]  error, datas=[{"+(datas==null?"":datas.getClass().getName())+"}"+datas+"]",th);
			}
	 }
	
	   Object transform(ParameterInfo ps,Object value){
		try{
			if(ps.getComponentType()==ParameterInfo.CT_NOT){
				return map2BeanProperty(ps,value);
			}else{
				return map2BeanCollectionProperty(ps,value);
			}
		}catch(Throwable th){
			_throwEx(ps,value,th.getMessage(),th);
		}
		return null;
	}
	   
	   private   Object map2Bean(Object targetBean,Map<?,?> datas)  {
			Object owner=targetBean;
			Class<?> clazz=owner.getClass();
			List<ParameterInfo> params=beansConfigInfo.getRestFields(clazz);
			for(ParameterInfo ps:params){
				Object tv=datas.get(ps.getName());
				if(tv!=null){
					Object v2=transform(ps,tv);
					try {
						ps.set(owner, v2);
					} catch (Exception e) {
						_throwEx(ps,v2,e.getMessage(),e);
					} 
				}
			}
			return owner;
		}
	
	private   Object map2Bean(Class<?> clazz,Map<?,?> datas)  {
		Object owner=BeanUtils.instantiate(clazz);
		List<ParameterInfo> params=beansConfigInfo.getRestFields(clazz);
		for(ParameterInfo ps:params){
			Object tv=datas.get(ps.getName());
			if(tv!=null){
				Object v2=transform(ps,tv);
				try {
					ps.set(owner, v2);
				} catch (Exception e) {
					_throwEx(ps,v2,e.getMessage(),e);
				} 
			}
		}
		return owner;
	}

	private  Object map2BeanCollectionProperty(ParameterInfo ps,Object value) throws Exception {
		if(value==null)return null;
		//
		int componentType=ps.getComponentType();
		if(componentType==ParameterInfo.CT_ARRAY || componentType==ParameterInfo.CT_COLLECTION ){
			if(value.getClass().isArray()){
				int arrLen=Array.getLength(value);
				List<Object> arr=new ArrayList<Object>(arrLen);
				// 
				for(int i=0;i<arrLen;i++){
					arr.add(map2BeanProperty(ps,Array.get(value, i)));
				}
				return forCollection(arr,ps);
			}if(value instanceof Collection){
				Collection<?> c=(Collection<?>)value;
				List<Object> arr=new ArrayList<Object>(c.size());
				// 
				for(Object v:c){
					arr.add(map2BeanProperty(ps,v));
				}
				return forCollection(arr,ps);
			}else{
				List<Object> arr=new ArrayList<Object>(1);
				// 
				arr.add(map2BeanProperty(ps,value));
				return forCollection(arr,ps);
			}
		}else if(componentType==ParameterInfo.CT_MAP){
			if(value instanceof Map){
				Class<?> instanceClass=ps.getComponentInstanceClass();
				if(instanceClass==null || instanceClass.equals(value.getClass())){
					return value;
				}else{
					Map temp=(Map)BeanUtils.instantiate(instanceClass);
					temp.putAll((Map)value);
					return temp;
				}
			}else{
				_throwEx(ps, value, "Not be a map",null);
			}
			return null;
		}else if(componentType==ParameterInfo.CT_BYTES){
			Object datas=null;
			if(value instanceof String){
				datas=Base64.decodeBase64((String)value);
			}else{
				Class<?> vClass=value.getClass();
				if(vClass.isArray() && (vClass.getComponentType().equals(byte.class) || vClass.getComponentType().equals(Byte.class))){
					datas=value;
				}else{
					_throwEx(ps, value, "Not be a byte array",null);
				}
			}
			Class<?> instanceClass=ps.getComponentInstanceClass();
			if(instanceClass==null || datas.getClass().getComponentType().equals(instanceClass)){
				return datas;
			}else{
				int len=Array.getLength(datas);
				Object temp=Array.newInstance(instanceClass,len);
				for(int i=0;i<len;i++){
					Array.set(temp, i, Array.get(datas,i));
				}
				return temp;
			}
		}else{
			return map2BeanProperty(ps,value);
		}
	}
	
	
	
	private  Object forCollection(List<Object> arr,ParameterInfo ps){
		Class<?> instanceClass=ps.getComponentInstanceClass();
		if(instanceClass ==null || instanceClass.equals(arr.getClass())){
			return arr;
		}else if(ps.getComponentType()==ParameterInfo.CT_ARRAY){
			Object temp=Array.newInstance(instanceClass,arr.size());
			for(int i=0;i<arr.size();i++){
				Array.set(temp, i, arr.get(i));
			}
			return temp;
		}else{
			Collection temp=(Collection)BeanUtils.instantiate(instanceClass);
			temp.addAll(arr);
			return temp;
		}
	}
	
	private  Object map2BeanProperty(ParameterInfo p,Object value) throws Exception{
		if(value==null)return null;
		if(value instanceof String){
			String temp=((String)value).trim();
			if(temp.length()==0){
				return null;
			}else{
				value=temp;
			}
		}
		//---------------------------
		int bindType=p.getBindType();
		if(bindType==ParameterInfo.T_NUMBER ||bindType==ParameterInfo.T_BOOL){
			String str=value.toString();
			if(p.getFormatter()!=null){
				str=p.getFormatter().parse(str).toString();
			}
			return p.getBindClass().getConstructor(String.class).newInstance(str);
		}else if(bindType==ParameterInfo.T_CHAR){
			String str=value.toString();
			if(str.length()!=1){
				throw new java.lang.IllegalArgumentException("not be a char,length="+str.length());
			}
			return new java.lang.Character(str.charAt(0));
		}else if(bindType==ParameterInfo.T_DATE){
			return DateFormatResolver.getInstance().toDate(value,p.getFormatter());
		}else if(bindType==ParameterInfo.T_ENUM){
			return Enum.valueOf((Class<? extends Enum>)p.getBindClass(), value.toString());
		}else if(bindType==ParameterInfo.T_STRING){
			return value.toString();
		}else if(bindType==ParameterInfo.T_BEAN){
			if(p.getBindClass()!=null && value instanceof Map){
				Map temp=(Map)value;
				return map2Bean(p.getBindClass(),temp);
			}else{
				return value;
			}
		}else if(bindType==ParameterInfo.T_UNKOWN){
			Class<?> bindClazz=p.getBindClass();
			if(bindClazz==null){
				return value;
			}else if(bindClazz.isAssignableFrom(value.getClass())){
				return value;
			}else{
				_throwEx(p,value,"Not support type/unknown type.",null);
			}
		}
		return value;
	}
	
	
	
	private static void _throwEx(ParameterInfo p,Object value,String msg,Throwable th){
		if(th!=null && th instanceof InvocationTargetException){
			th=th.getCause();
		}
		if(th!=null && th instanceof BeanTransformException){
			throw (BeanTransformException)th;
		}else{
			if(msg==null)msg=th.getMessage();
			throw new BeanTransformException("property["+p.getName()+"]bind [{"+(value==null?"":value.getClass().getName())+"}"+value+"]  error:"+msg,th);
		}
	}
	
}

