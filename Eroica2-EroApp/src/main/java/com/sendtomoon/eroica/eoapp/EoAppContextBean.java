package com.sendtomoon.eroica.eoapp;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ConfigurableApplicationContext;

import com.sendtomoon.eroica.common.app.dto.ServiceRequest;
import com.sendtomoon.eroica.common.app.dto.ServiceResponse;
import com.sendtomoon.eroica2.allergo.Allergo;
import com.sendtomoon.eroica.eoapp.context.EoAppLifecycle;
import com.sendtomoon.eroica.eoapp.context.EoAppRootContext;
import com.sendtomoon.eroica.eoapp.context.EoAppSpringContext;
import com.sendtomoon.eroica.eoapp.context.config.EoAppConfigProperties;
import com.sendtomoon.eroica.eoapp.sar.SARContext;
import com.sendtomoon.eroica.eoapp.web.EoAppWebDispatcher;

public class EoAppContextBean extends EoApp implements EoAppContext, DisposableBean {

	private volatile ServletContext originalServletContext;

	private Log logger = LogFactory.getLog(EoApp.class);

	private volatile EoAppRootContext rootContext;

	private volatile EoAppLifecycle lifecycle;

	private volatile EoAppSpringContext springContext;

	public SARContext getSARContext(String sarName) {
		return getSARContext(sarName, true);
	}

	public SARContext getSARContext(String sarName, boolean requiredExists) {
		this.runningCheck();
		return springContext.getSarManager().getSARContext(sarName, requiredExists);
	}

	@Override
	public boolean isRunning() {
		if (null == lifecycle) {
			return false;
		} else {
			return lifecycle.isRunning();
		}
	}

	public EoAppContextBean() {

	}

	@Override
	public boolean unexportAll() {
		runningCheck();
		this.springContext.getEsaManager().unexportAll();
		return true;
	}

	@Override
	public boolean unexport(String sarName) {
		runningCheck();
		SARContext context = this.springContext.getSarManager().getSARContext(sarName, false);
		if (context == null) {
			return false;
		}
		this.springContext.getEsaManager().unexport(context);
		return true;
	}

	@Override
	public boolean isExported(String esaName) {
		this.runningCheck();
		return springContext.getEsaManager().isExported(esaName);
	}

	@Override
	public ClassLoader getClassLoader() {
		this.runningCheck();
		return lifecycle.getClassLoader();
	}

	@Override
	public ConfigurableApplicationContext getSpringContext() {
		this.runningCheck();
		return this.lifecycle.getSpringContext().getApplicationContext();
	}

	@Override
	public ServiceResponse handleRequest(ServiceRequest request, boolean includeFilters) {
		runningCheck();
		ClassLoader classLoader = lifecycle.getClassLoader();
		if (classLoader != null) {
			Thread.currentThread().setContextClassLoader(classLoader);
		}
		return this.springContext.getEsaManager().handleRequest(request, includeFilters);
	}

	@Override
	public ServiceResponse handleRequest(ServiceRequest request) throws EoAppException {
		runningCheck();
		ClassLoader classLoader = lifecycle.getClassLoader();
		if (classLoader != null) {
			Thread.currentThread().setContextClassLoader(classLoader);
		}
		return this.springContext.getEsaManager().handleRequest(request);
	}

	public boolean exists(String SARName) {
		runningCheck();
		return this.springContext.getSarManager().exists(SARName);
	}

	protected void runningCheck() {
		if (rootContext != null && lifecycle != null && lifecycle.isRunning()) {

		} else {
			throw new EoAppException("Eroica<" + Allergo.getAppName() + "> startup failed or shutdowned!");
		}
	}

	@Override
	public synchronized boolean startup() {
		try {
			// 判断生命周期是否在初始化中
			if (!initEoAppLifecycle()) {
				return false;
			}

			this.lifecycle.start();

			this.springContext = this.lifecycle.getSpringContext();
			boolean b = lifecycle.isRunning();
			return b;
		} catch (Throwable ex) {
			logger.error(ex.getMessage(), ex);
			return false;
		}
	}

	private boolean initEoAppLifecycle() {
		try {
			if (rootContext != null && lifecycle != null && lifecycle.isRunning()) {
				return false;
			}
			rootContext = new EoAppRootContext();
			this.lifecycle = rootContext.getBean(EoAppLifecycle.class);
			lifecycle.setOriginalServletContext(this.originalServletContext);
			return true;
		} catch (Throwable ex) {
			logger.error(ex.getMessage(), ex);
			return false;
		}

	}

	public void handleWebRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		this.runningCheck();
		EoAppWebDispatcher webDispatcher = this.springContext.getWebDispatcher();
		if (webDispatcher != null) {
			try {
				ClassLoader classLoader = lifecycle.getClassLoader();
				if (classLoader != null) {
					Thread.currentThread().setContextClassLoader(classLoader);
				}
				webDispatcher.dispatchRequest(request, response);
			} finally {
				// Thread.currentThread().setContextClassLoader(null);
			}
		} else {
			throw new ServletException("Not found dispatcher for web request");
		}
	}

	@Override
	public synchronized boolean shutdown() {
		if (rootContext != null) {
			EoAppRootContext rootContext = this.rootContext;
			this.rootContext = null;
			rootContext.close();
			this.lifecycle = null;
			this.springContext = null;
		}
		return true;
	}

	@Override
	public void destroy() throws Exception {
		shutdown();
	}

	@Override
	public synchronized boolean shutdown(String sarName) {
		if (springContext != null && lifecycle != null && lifecycle.isRunning()) {
			return this.springContext.getSarManager().shutdown(sarName);
		} else {
			logger.error("SAR:" + sarName + " Shutdown  failed,Cause EoApp not running.");
		}
		return false;
	}

	@Override
	public boolean isRunning(String sarName) {
		return springContext != null && lifecycle != null && lifecycle.isRunning()
				&& this.springContext.getSarManager().isRunning(sarName);
	}

	@Override
	public synchronized boolean startup(String sarName) {
		if (springContext != null && lifecycle != null && lifecycle.isRunning()) {
			return this.springContext.getSarManager().startup(sarName);
		} else {
			logger.error("SAR:" + sarName + " Startup  failed,Cause EoApp not running.");
		}
		return false;
	}

	public void setServletContext(ServletContext servletContext) {
		if (servletContext != null && this.originalServletContext == null) {
			this.originalServletContext = servletContext;
		}
	}

	public ServletContext getServletContext() {
		this.runningCheck();
		return this.lifecycle.getServletContext();
	}

	@Override
	public EoAppConfigProperties getConfigProperties() {
		this.runningCheck();
		return this.lifecycle.getConfigProperties();
	}

	@Override
	public String getEoAppName() {
		return this.getConfigProperties().getAppName();
	}

	@Override
	public String[] getActiveSARs() {
		this.runningCheck();
		List<SARContext> sarContexts = springContext.getSarManager().listSARContext();
		if (sarContexts != null && sarContexts.size() > 0) {
			String[] sars = new String[sarContexts.size()];
			for (int i = 0; i < sarContexts.size(); i++) {
				SARContext sarContext = sarContexts.get(i);
				sars[i] = sarContext.getSARName();
			}
			return sars;
		}
		return new String[0];
	}

}
