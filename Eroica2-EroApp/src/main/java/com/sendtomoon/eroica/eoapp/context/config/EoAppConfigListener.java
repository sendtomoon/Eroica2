package com.sendtomoon.eroica.eoapp.context.config;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import com.sendtomoon.eroica.eoapp.context.event.EoAppConfigChangedEvent;
import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;
import com.sendtomoon.eroica2.allergo.spring.AllergoResourceListener;

public class EoAppConfigListener implements AllergoResourceListener, ApplicationContextAware {

	protected Log logger = LogFactory.getLog(this.getClass());

	private EoAppConfigProperties configProperties;

	@Override
	public void onChanged(AllergoURL allergoURL, InputStream content) {
		if (content != null) {
			try {
				configProperties.refresh(content);
				if (logger.isInfoEnabled()) {
					logger.info("EoAppConfigChanged:" + configProperties.toString());
				}
				applicationContext.publishEvent(new EoAppConfigChangedEvent(applicationContext, configProperties));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext) context;
	}

	private ConfigurableApplicationContext applicationContext;

	@Override
	public AllergoURL getAllergoURL() {
		return configProperties.getAllergoURL();
	}

	@Override
	public boolean isListenEnable() {
		return configProperties.getProperty(EoAppConstants.KEY_ALLERGO_LISTENER_ENABLE, false);
	}

	public EoAppConfigProperties getConfigProperties() {
		return configProperties;
	}

	public void setConfigProperties(EoAppConfigProperties configProperties) {
		this.configProperties = configProperties;
	}

}
