package com.sendtomoon.eroica2.allergo.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sendtomoon.eroica2.allergo.AllergoException;
import com.sendtomoon.eroica2.allergo.AllergoListener;
import com.sendtomoon.eroica2.allergo.AllergoManager;
import com.sendtomoon.eroica.common.beans.json.Xml2JsonUtils;

public class GlobalVariablesHandler implements IGlobalVariablesHandler {

	private Log logger = LogFactory.getLog(this.getClass());

	private Lock lock = new ReentrantLock();

	private volatile AllergoListener allergoListener;

	private volatile AllergoManager allergoManager;

	private volatile String allergoPath;

	@Override
	public String handle(final String path, final String originalContent) {
		try {
			if (originalContent == null || originalContent.length() < 5) {
				return originalContent;
			}
			return GlobalVariablesConvertUtils.convert(originalContent);
		} catch (Throwable th) {
			throw new AllergoException(
					"Convert configContent for<" + path + "> by GlobalVariables error,cause:" + th.getMessage(), th);
		}
	}

	@Override
	public void init(String path, AllergoManager allergoManager) {
		if (path == null) {
			initVariables(null);
			return;
		}
		// ------------------------------------
		this.allergoManager = allergoManager;
		this.allergoPath = path;
		final String configureContent = allergoManager.get(path);
		initVariables(configureContent);
		allergoManager.setListener(path, new AllergoListener() {

			@Override
			public void handleConfigChange(String allergoContent) {
				if (allergoContent != null && allergoContent.length() > 0) {
					initVariables(allergoContent);
				}
			}
		});
	}

	@Override
	public void shutdown() {
		if (allergoListener != null) {
			try {
				allergoManager.removeListener(allergoPath);
			} catch (Throwable ex) {
			}
			allergoListener = null;
		}
		allergoManager = null;
	}

	protected void initVariables(final String configureContent) {
		lock.lock();
		try {
			loadProperties(configureContent);
		} finally {
			lock.unlock();
		}
	}

	protected void loadProperties(final String configureContent) {
		Properties gloablVars = new Properties();
		try {
			String envName = System.getenv("ALLERGO_ENV_NAME");
			if (envName == null || (envName = envName.trim()).length() == 0) {
				envName = System.getProperty("allergo.env.name");
			}
			if (envName != null && (envName = envName.trim()).length() > 0) {
				Enumeration<URL> urls = GlobalVariablesHandler.class.getClassLoader()
						.getResources("allergo-global-vars.xml");
				while (urls.hasMoreElements()) {
					URL configURL = urls.nextElement();
					InputStream xml = null;
					try {
						xml = configURL.openStream();
						Xml2JsonUtils xml2json = new Xml2JsonUtils(xml, "property");
						JSONObject json = JSONObject.parseObject(xml2json.toJSONString());
						JSONObject vars = json.getJSONObject(envName);
						if (vars == null || vars.size() == 0) {
							logger.warn("Not found any global vars for env:" + envName + " by xml:" + configURL);
							continue;
						}
						JSONArray items = vars.getJSONArray("property");
						if (items == null || items.size() == 0) {
							logger.warn("Not found any global vars for env:" + envName + " by xml:" + configURL);
							continue;
						}
						Properties props = new Properties();
						for (int i = 0; i < items.size(); i++) {
							JSONObject item = items.getJSONObject(i);
							String name = item.getString("name");
							String value = item.getString("value");
							props.setProperty(name, value);
						}
						gloablVars.putAll(props);
						if (logger.isInfoEnabled()) {
							logger.info("Found global vars by:" + configURL + ", and properties=" + props);
						}
					} finally {
						if (xml != null) {
							try {
								xml.close();
							} catch (IOException ex) {
							}
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (allergoPath != null && configureContent != null && configureContent.length() > 0) {
			if (logger.isInfoEnabled()) {
				logger.info("Found global vars by Allergo<" + allergoPath + ">,AllergoContent=\n" + configureContent);
			}
			Reader reader = new StringReader(configureContent);
			try {
				Properties variables = new Properties();
				variables.load(reader);

				gloablVars.putAll(variables);
				// ----------------------------------------------------------------
				if (logger.isInfoEnabled()) {
					logger.info("Found global vars by Allergo<" + allergoPath + "> :" + variables);
				}
			} catch (IOException e) {
			}
		} else {
			if (allergoPath != null && logger.isInfoEnabled()) {
				logger.info("Found global vars by Allergo<" + allergoPath + ">:{}.");
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("FoundGlobalVariables:" + gloablVars);
		}
		if (gloablVars.size() > 0) {
			Set<Object> keyset = gloablVars.keySet();
			for (Object key : keyset) {
				System.setProperty((String) key, gloablVars.getProperty((String) key));
			}
		}
	}

}
