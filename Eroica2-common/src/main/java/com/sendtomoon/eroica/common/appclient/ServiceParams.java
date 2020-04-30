package com.sendtomoon.eroica.common.appclient;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ServiceParams implements Map<Object,Object>,java.io.Serializable {

	private  HashMap<Object,Object> map;

	private static final long serialVersionUID = 1L;
	
	public ServiceParams(){
		map=new HashMap<Object,Object> (8);
	}
	
	public ServiceParams(Map<?,?> params){
		map=new HashMap<Object,Object> (params);
	}
	
	
	public ServiceParams set(Object key,Object value){
		map.put(key, value);
		return this;
	}
	
	public Object get(Object key){
		return map.get(key);
	}
	
	
	
	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Set<Entry<Object, Object>> entrySet() {
		return map.entrySet();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public Set<Object> keySet() {
		return map.keySet();
	}

	@Override
	public Object put(Object key, Object value) {
		return map.put(key,value);
	}

	@Override
	public void putAll(Map<? extends Object, ? extends Object> t) {
		 map.putAll(t);
	}

	@Override
	public Object remove(Object key) {
		return  map.remove(key);
	}

	@Override
	public int size() {
		return  map.size();
	}

	@Override
	public Collection<Object> values() {
		return  map.values();
	}

	public static  ServiceParams newInstance(){
		return new ServiceParams();
	}
	
	public Map<Object,Object> getMap(){
		return map;
	}
}
