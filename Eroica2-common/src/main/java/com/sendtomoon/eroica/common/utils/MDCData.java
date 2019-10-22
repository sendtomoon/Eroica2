package com.sendtomoon.eroica.common.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.NDC;

/***
 * 日志记录上下文,常量数据类
 */
@SuppressWarnings("unchecked")
public class MDCData implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public static final String KEY_UID = "U";

	public static final String KEY_REQUEST_ID = "T";

	@SuppressWarnings("rawtypes")
	public Map datas;

	protected MDCData(String str) {
		datas = this.stringToMap(str);
	}

	@SuppressWarnings("rawtypes")
	protected MDCData(Map datas) {
		this.datas = datas;
		refresh();
	}

	@SuppressWarnings("rawtypes")
	protected Map getDatas() {
		return datas;
	}

	/** 设置常量 */
	@SuppressWarnings("rawtypes")
	public void set(String key, String value) {
		if (this.datas == null) {
			this.datas = new HashMap(4);
		}
		this.datas.put(key, value);
		refresh();
	}

	private void refresh() {
		NDC.clear();
		NDC.push(this.toString());
	}

	public String get(String key) {
		if (key == null)
			return null;
		if (datas == null) {
			return null;
		}
		Object v = datas.get(key);
		return v == null ? null : v.toString();
	}

	public String toString() {
		return toString(datas);
	}

	@SuppressWarnings("rawtypes")
	private String toString(Map datas) {
		if (datas == null || datas.size() == 0)
			return null;
		StringBuilder buf = new StringBuilder(24);
		buf.append("<");
		mapToString(buf, datas);
		buf.append(">");
		return buf.toString();
	}

	@SuppressWarnings("rawtypes")
	private void mapToString(StringBuilder buf, Map datas) {
		if (datas == null)
			return;
		Iterator i = datas.entrySet().iterator();
		if (buf.length() > 2)
			buf.append(",");
		boolean hasNext = i.hasNext();
		while (hasNext) {
			Map.Entry e = (Map.Entry) i.next();
			buf.append(e.getKey()).append('=').append(e.getValue());
			hasNext = i.hasNext();
			if (hasNext)
				buf.append(",");
		}
	}

	@SuppressWarnings("rawtypes")
	private Map stringToMap(String str) {
		if (str == null || (str = str.trim()).length() < 3) {
			return null;
		}
		str = str.substring(1, str.length() - 1);
		Map datas = new HashMap();
		String temp[] = str.split(",");
		for (int i = 0; i < temp.length; i++) {
			String t2[] = temp[i].split("=");
			if (t2 != null && t2.length == 2) {
				datas.put(t2[0], t2[1]);
			}
		}
		return datas;
	}

	public String getRequestId() {
		return get(KEY_REQUEST_ID);
	}

	public void setRequestId(String requestId) {
		set(KEY_REQUEST_ID, requestId);
	}

	public int size() {
		if (this.datas == null)
			return 0;
		return this.datas.size();
	}

	public String getUid() {
		return get(KEY_UID);
	}

	public void setUid(String uid) {
		set(KEY_UID, uid);
	}

}
