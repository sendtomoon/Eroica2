package com.sendtomoon.eroica2.allergo;

import java.util.Properties;

/**
 * Allergo关键配置项
 */
class AllergoAttrs {

	/** 领域ID */
	private String domainId;

	/** 应用名 */
	private String appName;

	public AllergoAttrs() {
		init();
	}

	private void init() {
		this.appName = resolveAppName();
		String domainId = getProperty(AllergoConstants.KEY_DOMAIN_ID);
		if (domainId == null || (domainId = domainId.trim()).length() == 0) {
			System.err.println("---------------");
		}
		this.setDomainId(domainId);
	}

	protected static void _throwEx(String propertyName) {
		throw new AllergoException("Not found allergo property <" + propertyName + ">.");
	}

	public String getAppName() {
		return appName;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	/***
	 * 获取应用名
	 * 
	 * @return
	 */
	protected String resolveAppName() {
		String appName = getProperty(AllergoConstants.KEY_APP_NAME);
		if (appName == null || (appName = appName.trim()).length() == 0) {
			_throwEx(AllergoConstants.KEY_APP_NAME);
		}
		return appName;
	}

	protected boolean getBoolProperty(String key, String defValue) {
		return Boolean.valueOf(getProperty(key, defValue));
	}

	protected String getProperty(String key, String defaultValue) {
		String value = System.getProperty(key, defaultValue);
		if (value == null || (value = value.trim()).length() == 0) {
			return null;
		}
		return value;
	}

	protected String getProperty(String key) {
		return getProperty(key, null);
	}

	protected Properties getProperties() {
		return System.getProperties();
	}

	protected void setProperty(String key, String value) {
		System.setProperty(key, value);
	}

}
