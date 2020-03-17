package com.sendtomoon.eroica.sso;

import java.util.HashMap;
import java.util.Map;

/***
 * SSO 缓存数据
 */
public class CacheData {

	public static final String KEY_UID = "$_UID";

	public static final String KEY_IP = "$_IP";

	public static final String KEY_ExpiresTime = "$_ExpiresTime";

	public static final String KEY_LAST_APP_NAME = "$_lastAppName";

	public static final String KEY_LAST_ACC_TIME = "$_lastAccTime";

	public static final String KEY_LOGIN_TIME = "$_loginTime";

	public static final String KEY_ROLE_TYPE = "$_roleType";

	private Map<Object, Object> datas;

	public CacheData(String uid) {
		this.datas = new HashMap<Object, Object>(4);
		this.setUid(uid);
	}

	CacheData(Map<Object, Object> datas) {
		this.datas = datas;
	}

	public String getUid() {
		return (String) datas.get(KEY_UID);
	}

	public void setUid(String uid) {
		datas.put(KEY_UID, uid);
	}

	public void set(Object key, Object value) {
		datas.put(key, value);
	}

	public Object get(Object key) {
		return datas.get(key);
	}

	public void set(Map<Object, Object> map) {
		if (map != null) {
			datas.putAll(map);
		}
	}

	@Override
	public String toString() {
		return this.datas.toString();
	}

	public String getIp() {
		return (String) datas.get(KEY_IP);
	}

	public void setIp(String ip) {
		datas.put(KEY_IP, ip);
	}

	public Map<Object, Object> peek() {
		return datas;
	}

	public void setExpiresTime(int expiresTime) {
		datas.put(KEY_ExpiresTime, expiresTime);
	}

	public long getLoginTime() {
		Long r = (Long) datas.get(KEY_LOGIN_TIME);
		if (r == null)
			return 0;
		return r;
	}

	public void setLoginTime(long loginTime) {
		datas.put(KEY_LOGIN_TIME, loginTime);
	}

	public long getLastAccTime() {
		Long r = (Long) datas.get(KEY_LAST_ACC_TIME);
		if (r == null)
			return 0;
		return r;
	}

	public void setLastAccTime(long lastAccTime) {
		datas.put(KEY_LAST_ACC_TIME, lastAccTime);
	}

	public String getLastAppName() {
		return (String) datas.get(KEY_LAST_APP_NAME);
	}

	public void setLastAppName(String lastAppName) {
		datas.put(KEY_LAST_APP_NAME, lastAppName);
	}

	public Integer getRoleType() {
		return (Integer) datas.get(KEY_ROLE_TYPE);
	}

	public void setRoleType(Integer roleType) {
		datas.put(KEY_ROLE_TYPE, roleType);
	}

	public int getExpiresTime() {
		Integer r = (Integer) datas.get(KEY_ExpiresTime);
		if (r == null)
			return 0;
		return r;
	}

}
