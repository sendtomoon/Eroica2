package com.sendtomoon.eroica.common.appclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sendtomoon.eroica.common.beans.map.BeanMapUtils;
import com.sendtomoon.eroica.common.utils.MapDatas;

public class ServiceResults extends MapDatas implements IServiceResults {

	protected BeanMapUtils beanMapUtils;

	public ServiceResults(Map<Object, Object> result, BeanMapUtils beanMapUtils) {
		super(result == null ? new HashMap<Object, Object>(8) : result);
		this.beanMapUtils = beanMapUtils;
	}

	private static final long serialVersionUID = 1L;

	public ServiceResults(Map<Object, Object> result) {
		super(result == null ? new HashMap<Object, Object>(8) : result);
		this.beanMapUtils = new BeanMapUtils();
	}

	@SuppressWarnings("unchecked")
	private <T> T _toDTO(String key, Object value, Class<T> dtoClazz) {
		if (value instanceof Map) {
			T t = (T) beanMapUtils._toBean(dtoClazz, (Map) value);
			// this.put(key, t);
			return t;
		} else {
			return (T) value;
		}
	}

	@SuppressWarnings("unchecked")
	private ServiceResults _getResults(String key, Object value) {
		if (value instanceof Map) {
			return new ServiceResults((Map) value, this.beanMapUtils);
		} else {
			throw new AppClientException("Not be a map");
		}
	}

	@SuppressWarnings("unchecked")
	private List<ServiceResults> _listResults(String key, Object value) {
		if (value instanceof Collection) {
			List<ServiceResults> list = new ArrayList<ServiceResults>(((Collection) value).size());
			int i = 0;
			for (Object v : (Collection) value) {
				if (v instanceof Map) {
					list.add(new ServiceResults((Map) v, this.beanMapUtils));
				} else {
					throw new AppClientException("index[" + i + "]Not be a map");
				}
				i++;
			}
			// this.put(key, list);
			return list;
		} else {
			throw new AppClientException("Not be Collection.");
		}
	}

	public List<ServiceResults> listSubResults(String key) {
		Object value = this.get(key);
		if (value == null)
			return null;
		try {
			return _listResults(key, value);
		} catch (Exception ex) {
			Exception cs = ex;
			if (ex instanceof AppClientException) {
				cs = null;
			}
			throw new AppClientException("Key[" + key + "]=[{" + value.getClass().getName() + "}" + value
					+ "] listResults error:" + ex.getMessage(), cs);
		}
	}

	public ServiceResults getSubResults(String key) {
		Object value = this.get(key);
		if (value == null)
			return null;
		try {
			return _getResults(key, value);
		} catch (Exception ex) {
			Exception cs = ex;
			if (ex instanceof AppClientException) {
				cs = null;
			}
			throw new AppClientException("Key[" + key + "]=[{" + value.getClass().getName() + "}" + value
					+ "] getResults error:" + ex.getMessage(), cs);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T toDTO(Class<T> dtoClazz) {
		try {
			return (T) beanMapUtils._toBean(dtoClazz, this);
		} catch (Exception ex) {
			throw new AppClientException(
					"Results=[" + this + "] to DTO[" + dtoClazz.getName() + "] error:" + ex.getMessage(), ex);
		}
	}

	public <T> T toDTO(String key, Class<T> dtoClazz) {
		Object value = this.get(key);
		if (value == null)
			return null;
		try {
			return _toDTO(key, value, dtoClazz);
		} catch (Exception ex) {
			throw new AppClientException("Key[" + key + "]=[{" + value.getClass().getName() + "}" + value + "] to DTO["
					+ dtoClazz.getName() + "] error:" + ex.getMessage(), ex);
		}
	}

	public <T> List<T> toDTOList(String key, Class<T> dtoClazz) {
		Object value = this.get(key);
		if (value == null)
			return null;
		try {
			return _listDTO(key, value, dtoClazz);
		} catch (Exception ex) {
			throw new AppClientException("Key[" + key + "]=[{" + value.getClass().getName() + "}" + value
					+ "] to DTOList[" + dtoClazz.getName() + "] error:" + ex.getMessage(), ex);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> _listDTO(String key, Object value, Class<T> dtoClazz) {
		if (value instanceof Collection) {
			List<T> list = new ArrayList<T>(((Collection) value).size());
			for (Object v : (Collection) value) {
				if (v instanceof Map) {
					list.add((T) beanMapUtils._toBean(dtoClazz, (Map) v));
				} else {
					list.add((T) v);
				}
			}
			// this.put(key, list);
			return list;
		} else {
			T t = this._toDTO(key, value, dtoClazz);
			if (t == null)
				return null;
			else {
				List<T> r = new ArrayList<T>();
				r.add(t);
				return r;
			}
		}
	}

	@Override
	protected void _throwEx(Object key, Object value, String msg) {
		throw new AppClientException("Key[" + key + "]=[{" + (value == null ? "" : value.getClass().getName()) + "}"
				+ value + "] error:" + msg);
	}

}
