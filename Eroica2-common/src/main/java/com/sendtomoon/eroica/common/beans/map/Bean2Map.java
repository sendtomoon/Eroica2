package com.sendtomoon.eroica.common.beans.map;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sendtomoon.eroica.common.beans.BeanTransformException;
import com.sendtomoon.eroica.common.beans.BeansConfigInfo;
import com.sendtomoon.eroica.common.beans.ParameterInfo;


  class Bean2Map   {
	
	private BeansConfigInfo beansConfigInfo;
	
	public Bean2Map(BeansConfigInfo beansConfigInfo){
		this.beansConfigInfo=beansConfigInfo;
	}
	
	public Bean2Map(){
		this.beansConfigInfo=new BeansConfigInfo();
	}
	
	Object toMap(Object value){
		try{
			return _toMap(value);
		}catch(Throwable th){
			if(th!=null && th instanceof InvocationTargetException){
				th=th.getCause();
			}
			throw new BeanTransformException("Class["+value.getClass().getName()+"]  to map error:"+th.getMessage(),th);
		}
	}
	
	

	
	private  Object _toMap(Object value) throws Throwable{
		//--------------------------
		if(value==null)return null;
		if(value.getClass().isArray()){
			Class<?> clazz=value.getClass().getComponentType();
			if(clazz.isPrimitive() || Number.class.isAssignableFrom(clazz) || String.class.equals(clazz)
					|| Character.class.equals(clazz) || Boolean.class.equals(clazz)){
				return value;
			}else{
				int len=Array.getLength(value);
				Object arr=Array.newInstance(Object.class,len);
				for(int i=0;i<len;i++){
					Object v=Array.get(value, i);
					if(v==null){
						Array.set(arr, i,null);
					}else{
						Array.set(arr, i,_toMap(v));
					}
				}
				return arr;
			}
		}else if(value instanceof Collection){
			List list=new ArrayList(((Collection)value).size());
			for(Object v:((Collection)value)){
				if(v!=null){
					list.add(_toMap(v));
				}else{
					list.add(null);
				}
			}
			return list;
		}else if( value instanceof Map){
			Map map=(Map)value;
			Map res=new HashMap(map.size());
			Iterator i=map.entrySet().iterator();
			while(i.hasNext()){
				Map.Entry entry=(Map.Entry)i.next();
				Object v=entry.getValue();
				if(v!=null ){
					res.put(entry.getKey(),_toMap(v));
				}
			}
			return res;
		}else if(value instanceof Enum){
			return ((Enum)value).name();
		}else if(!value.getClass().getName().startsWith("java")){
			return bean2map(value);
		}
		return value;
	}
	
	public  Object toMapField(ParameterInfo ps,Object value){
		try{
			return _toMapField(ps,value);
		}catch(Throwable th){
			if(th!=null && th instanceof InvocationTargetException){
				th=th.getCause();
			}
			throw new BeanTransformException("Class["+value.getClass().getName()+"]  to map error:"+th.getMessage(),th);
		}
		
	}
	
	private  Object _toMapField(ParameterInfo ps,Object value) throws Throwable{
		Object v=_toMap(value);
		if(v!=null && ps.getFormatter()!=null){
			v=ps.getFormatter().print(value);
		} 
		return v;
	}
	
	private  Map<Object,Object> bean2map(Object bean) throws Throwable{
		HashMap<Object,Object> datas=new HashMap<Object,Object>(8);
		List<ParameterInfo> params=beansConfigInfo.getRestFields(bean.getClass());
		for(ParameterInfo ps:params){
			Object tv= ps.get(bean);
			if(tv!=null){
				if(ps.getComponentType()==ParameterInfo.CT_NOT){
					datas.put(ps.getName(), _toMapField(ps,tv));
				}else{
					datas.put(ps.getName(), _toMap(tv));
				}
			}
		}
		return datas;
	}

	

	
}

