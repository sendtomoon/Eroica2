package com.sendtomoon.eroica2.allergo;

import java.nio.charset.Charset;
import java.util.Date;

import org.apache.commons.logging.Log;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;
import com.sendtomoon.eroica2.allergo.classloader.URLStreamHandlerFactoryUtils;
import com.sendtomoon.eroica2.allergo.log4j.DefaultLogUtils;
import com.sendtomoon.eroica2.allergo.spring.AllergoResourceListener;
import com.sendtomoon.eroica2.allergo.spring.AllergoResourceListenerHandler;
import com.sendtomoon.eroica.common.utils.URLUtils;

public class EroicaContextBean implements EroicaContext, InitializingBean, DisposableBean {

	private Log startupLogger;

	private AllergoAttrs attrs = null;

	private volatile AllergoManager defaultManager;

	public EroicaContextBean() {
		startupLogger = DefaultLogUtils.getLogger();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		attrs = new AllergoAttrs();
		String domainId = attrs.getDomainId();
		String gvarPath = null;
		if (domainId != null && domainId.length() > 0) {
			defaultManager.setDefRootPath("/" + domainId, false);
			gvarPath = AllergoConstants.GROUP_GLOBALVARS + "/" + domainId + ".properties";
		}
		defaultManager.initGlobalVariablesHandler(gvarPath);
		URLStreamHandlerFactoryUtils.registerAllergoURLStreamHandler(defaultManager);
		com.sendtomoon.eroica2.allergo.Allergo.eroicaContext = this;
	}

	@Override
	public void destroy() throws Exception {
		startupLogger.info("AllergoShutdown,date=" + new Date());
	}

	public Log getStartupLogger() {
		return startupLogger;
	}

	public void setStartupLogger(Log startupLogger) {
		this.startupLogger = startupLogger;
	}

	@Override
	public Charset getCharset() {
		return this.defaultManager.getCharset();
	}

	@Override
	public String get(String path) {
		return this.defaultManager.get(path);
	}

	@Override
	public String get(String group, String key) {
		return this.defaultManager.get(group, key);
	}

	@Override
	public boolean exists(String group, String key) {
		return this.defaultManager.exists(group, key);
	}

	@Override
	public boolean exists(String path) {
		return this.defaultManager.exists(path);
	}

	@Override
	public AllergoManager getDefaultManager() {
		return defaultManager;
	}

	public boolean registerListener(AllergoResourceListener listener) {
		if (listener == null) {
			throw new FatalBeanException("listener required");
		}
		AllergoURL allergoURL = listener.getAllergoURL();
		if (allergoURL == null) {
			throw new FatalBeanException("allergoURL required for listener:" + listener);
		}
		AllergoManager manager = this.getDefaultManager();
		if (listener.isListenEnable()) {
			manager.setListener(allergoURL.getAllergoGroup(), allergoURL.getAllergoKey(),
					new AllergoResourceListenerHandler(allergoURL, listener));
			return true;
		}
		return false;
	}

	public boolean unregisterListener(AllergoURL allergoURL) {
		if (allergoURL == null) {
			throw new FatalBeanException("allergoURL required ");
		}
		AllergoManager manager = this.getDefaultManager();
		return manager.removeListener(allergoURL.getAllergoGroup(), allergoURL.getAllergoKey());
	}

	@Override
	public String getAppName() {
		return attrs.getAppName();
	}

	public String getProjectId() {
		return attrs.getDomainId();
	}

	@Override
	public String getDomainId() {
		return attrs.getDomainId();
	}

	@Override
	public String toString() {
		return defaultManager == null ? null : defaultManager.toString();
	}

	@Override
	public AllergoManager createManager(String configURL) {
		URLUtils URL = URLUtils.valueOf(configURL);
		ExtensionLoader<AllergoManagerFactory> factory = ExtensionLoader
				.getExtensionLoader(AllergoManagerFactory.class);
		String name = URL.getProtocol();
		return factory.getExtension(name).create(URL);
	}

	public void setDefaultManager(AllergoManager defaultManager) {
		this.defaultManager = defaultManager;
	}

}
