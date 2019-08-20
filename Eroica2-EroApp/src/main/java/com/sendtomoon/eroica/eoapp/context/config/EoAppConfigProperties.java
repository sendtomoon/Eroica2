package com.sendtomoon.eroica.eoapp.context.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;
import com.sendtomoon.eroica2.allergo.utils.AllergoProperties;

/**
 * 主程序配置项
 * 
 */
public class EoAppConfigProperties extends AllergoProperties {

	protected Log logger = LogFactory.getLog(this.getClass());

	private static final long serialVersionUID = 1L;

	private String appName;

	private AllergoURL allergoURL;

	public EoAppConfigProperties(String appName, AllergoURL allergoURL) {
		this.appName = appName;
		this.allergoURL = allergoURL;
	}

	public String getAppName() {
		return appName;
	}

	public boolean getProperty(String key, boolean defValue) {
		String v = getProperty(key);
		return v == null ? defValue : Boolean.valueOf(v);
	}

	public int getProperty(String key, int defValue) {
		String v = getProperty(key);
		return v == null ? defValue : Integer.valueOf(v);
	}

	public double getProperty(String key, double defValue) {
		String v = getProperty(key);
		return v == null ? defValue : Double.valueOf(v);
	}

	public void refresh(InputStream input) throws IOException {
		if (input != null) {
			refresh();
			this.load(input);
		}
	}

	void refresh() {
		Properties systemProperties = System.getProperties();
		Enumeration<Object> keys = systemProperties.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if (key.startsWith("eoapp.")) {
				String v = systemProperties.getProperty(key);
				if (v != null && v.length() > 0) {
					this.setProperty(key, v);
				}

			}
		}
		setProperty("eoapp.name", appName);
		if (logger.isInfoEnabled()) {
			logger.info("EoApp:" + appName + " configure properties=" + this.toString());
		}
	}

	public AllergoURL getAllergoURL() {
		return allergoURL;
	}

}
