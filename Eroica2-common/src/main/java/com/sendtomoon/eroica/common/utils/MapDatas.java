package com.sendtomoon.eroica.common.utils;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sendtomoon.eroica.common.exception.EroicaException;

/***
 * 数据集
 */
public class MapDatas implements java.io.Serializable, Map<Object, Object> {

	/** 序列化版本号 */
	private static final long serialVersionUID = 1L;

	private Map<Object, Object> datas;

	/** 数据集 **/
	public MapDatas() {
		datas = new HashMap<Object, Object>(8);
	}

	public MapDatas(Map<Object, Object> result) {
		datas = result;
	}

	@Override
	public void clear() {
		datas.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return datas.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return datas.containsValue(value);
	}

	@Override
	public Set<Entry<Object, Object>> entrySet() {
		return datas.entrySet();
	}

	@Override
	public Object get(Object key) {
		return datas.get(key);
	}

	@Override
	public boolean isEmpty() {
		return datas.isEmpty();
	}

	@Override
	public Set<Object> keySet() {
		return datas.keySet();
	}

	@Override
	public Object put(Object key, Object value) {
		return datas.put(key, value);
	}

	@Override
	public void putAll(Map<? extends Object, ? extends Object> t) {
		datas.putAll(t);
	}

	@Override
	public int size() {
		return datas.size();
	}

	@Override
	public Collection<Object> values() {
		return datas.values();
	}

	public int getInt(Object key, int defaultValue) {
		Object t = get(key);
		if (t == null)
			return defaultValue;
		if (t instanceof Number) {
			return ((Number) t).intValue();
		} else if (t instanceof String) {
			String temp = null;
			if ((temp = t.toString().trim()).length() == 0)
				return defaultValue;
			try {
				return Integer.parseInt(temp);
			} catch (NumberFormatException ex) {
				_throwEx(key, t, ex.getMessage());
			}
		} else {
			_throwEx(key, t, "Type[" + t.getClass().getName() + "]can't transform to int.");
		}
		return defaultValue;
	}

	public int getInt(Object key) {
		return getInt(key, 0);
	}

	public long getLong(Object key) {
		return this.getLong(key, 0l);
	}

	public long getLong(Object key, long defaultValue) {
		Object t = get(key);
		if (t == null)
			return defaultValue;
		if (t instanceof Number) {
			return ((Number) t).longValue();
		} else if (t instanceof String) {
			String temp = null;
			if ((temp = t.toString().trim()).length() == 0)
				return defaultValue;
			try {
				return Long.parseLong(temp);
			} catch (NumberFormatException ex) {
				_throwEx(key, t, ex.getMessage());
			}
		} else {
			_throwEx(key, t, "Type[" + t.getClass().getName() + "]can't transform to long.");
		}
		return 0l;
	}

	public double getDouble(Object key, double defaultValue) {
		Object t = get(key);
		if (t == null)
			return defaultValue;
		if (t instanceof Number) {
			return ((Number) t).doubleValue();
		} else if (t instanceof String) {
			String temp = null;
			if ((temp = t.toString().trim()).length() == 0)
				return defaultValue;
			try {
				return Double.parseDouble(temp);
			} catch (NumberFormatException ex) {
				_throwEx(key, t, ex.getMessage());
			}
		} else {
			_throwEx(key, t, "Type[" + t.getClass().getName() + "]can't transform to double.");
		}
		return 0d;
	}

	public boolean getBool(Object key) {
		return getBool(key, false);
	}

	public boolean getBool(Object key, boolean defaultValue) {
		Object t = get(key);
		if (t == null)
			return defaultValue;
		if (t instanceof Boolean) {
			return ((Boolean) t).booleanValue();
		} else if (t instanceof String) {
			String temp = ((String) t).trim();
			return (temp.equalsIgnoreCase("Y") || temp.equalsIgnoreCase("TRUE") || temp.equalsIgnoreCase("1")
					|| temp.equalsIgnoreCase("YES"));
		} else if (t instanceof Number) {
			Number temp = (Number) t;
			return temp.intValue() != 0;
		} else {
			_throwEx(key, t, "Type[" + t.getClass().getName() + "]can't transform to boolean.");
		}
		return false;
	}

	public double getDouble(Object key) {
		return getDouble(key, 0d);
	}

	/** 设置值 */
	public MapDatas set(Object key, Object value) {
		this.put(key, value);
		return this;
	}

	/** 设置Date类型值 */
	public MapDatas setDate(Object key, Date date) {
		this.put(key, date);
		return this;
	}

	/** 获取String类型值,非字符串类型将转换 */
	public String getString(Object key) {
		return getString(key, null);
	}

	/** 获取String类型值,非字符串类型将转换 */
	public String getString(Object key, String defaultValue) {
		Object t = get(key);
		if (t == null)
			return defaultValue;
		String temp = t.toString();
		temp = temp.trim();
		if (temp.length() == 0)
			return defaultValue;
		return temp;
	}

	/** 删除值 */
	public Object remove(Object key) {
		return this.put(key, null);
	}

	protected void _throwEx(Object key, Object value, String msg) {
		throw new EroicaException("Key[" + key + "] data[" + value + "] error.");
	}

	@Override
	public boolean equals(Object obj) {
		return datas.equals(obj);
	}

	@Override
	public int hashCode() {
		return datas.hashCode();
	}

	@Override
	public String toString() {
		return datas.toString();
	}

}
