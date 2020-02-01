package com.sendtomoon.eroica.common.web.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sendtomoon.eroica.common.web.WebException;

public class JsonToFormParamsUtils {

	private Map<String, String[]> paramsList = new HashMap<String, String[]>();

	public Map<String, String[]> toHttpParams(Map<Object, Object> jsonMap) {
		try {
			return _toHttpParams(jsonMap);
		} catch (Exception ex) {
			throw new WebException("Convert jsonMap[" + jsonMap + "] to Http form params error:" + ex.getMessage(), ex);
		}
	}

	private Map<String, String[]> _toHttpParams(Map<Object, Object> jsonMap) {
		pmap(jsonMap, null);
		return paramsList;
	}

	@SuppressWarnings("unchecked")
	private void pmap(Map<Object, Object> jsonMap, String prefix) {
		Iterator<Map.Entry<Object, Object>> iterator = jsonMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) iterator.next();
			String key = entry.getKey().toString();
			Object value = entry.getValue();
			if (value != null) {
				if (value instanceof java.util.Collection) {
					pcollection((Collection<?>) value, (prefix == null ? "" : prefix + ".") + key);
				} else if (value.getClass().isArray()) {
					Object[] arr = (Object[]) value;
					pcollection(Arrays.asList(arr), (prefix == null ? "" : prefix + ".") + key);
				} else if (value instanceof Map) {
					pmap(((Map<Object, Object>) value), (prefix == null ? "" : prefix + ".") + key);
				} else {
					paramsList.put((prefix == null ? "" : prefix + ".") + key, new String[] { value.toString() });
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void pcollection(Collection<?> datas, String prefix) {
		int i = 0;
		List<String> valueList = null;
		for (Object value : datas) {

			if (value != null) {
				if (value instanceof java.util.Collection) {
					pcollection((Collection<?>) value, prefix + "[" + i + "]");
					i++;
				} else if (value.getClass().isArray()) {
					Object[] arr = (Object[]) value;
					pcollection(Arrays.asList(arr), prefix + "[" + i + "]");
				} else if (value instanceof Map) {
					pmap(((Map<Object, Object>) value), prefix + "[" + i + "]");
					i++;
				} else {
					if (valueList == null) {
						valueList = new ArrayList<String>(8);
					}
					valueList.add(value.toString());
				}

			}
		}
		if (valueList != null) {
			String[] valueArray = new String[valueList.size()];
			paramsList.put(prefix, valueList.toArray(valueArray));
		}
	}
}
