package com.sendtomoon.eroica.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sendtomoon.eroica.common.web.WebException;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ParametersToMapUtils {

	public Map toMap(Map parameters) {
		Map map = new HashMap(16);
		Iterator<Map.Entry<Object, Object>> i = parameters.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) i.next();
			String k = entry.getKey().toString();
			String v[] = (String[]) entry.getValue();
			try {
				pmap(map, k, v);
			} catch (Throwable th) {
				throw new WebException("Paramter<" + k + "> format error: " + th.getMessage());
			}
		}
		return map;
	}

	protected void pmap(Map map, String name, String[] value) {
		int idx = name.indexOf('.');
		String cfn = (idx == -1 ? name : name.substring(0, idx));

		int t1 = cfn.indexOf('[');
		if (t1 != -1) {
			int t2 = cfn.indexOf(']', t1);
			int arrIdx = Integer.parseInt(cfn.substring(t1 + 1, t2));
			cfn = cfn.substring(0, t1);
			//
			List list = (List) map.get(cfn);
			if (list == null) {
				list = new ArrayList(8);
				map.put(cfn, list);
			}
			if (list.size() <= arrIdx) {
				for (int i = list.size(); i <= arrIdx; i++) {
					list.add(i, null);
				}
			}
			// ---------------------------
			if (idx == -1) {
				list.set(arrIdx, (value.length > 1 ? value : value[0]));
			} else {
				Map v = (Map) list.get(arrIdx);
				if (v == null) {
					v = new HashMap(8);
					list.set(arrIdx, v);
				}
				pmap(v, name.substring(idx + 1), value);
			}

		} else {
			if (idx == -1) {
				map.put(name, (value.length > 1 ? value : value[0]));
			} else {
				Map v = (Map) map.get(cfn);
				if (v == null) {
					v = new HashMap(8);
					map.put(cfn, v);
				}
				pmap(v, name.substring(idx + 1), value);
			}
		}
	}
}
