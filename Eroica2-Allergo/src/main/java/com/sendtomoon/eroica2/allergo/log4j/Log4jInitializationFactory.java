package com.sendtomoon.eroica2.allergo.log4j;

import java.io.InputStream;

import org.apache.commons.logging.Log;

import com.sendtomoon.eroica2.allergo.AllergoContext;
import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;

public class Log4jInitializationFactory implements Log4jInitialization {

	private Log4jInitialization log4j;

	private AllergoContext allergoContext;

	public Log4jInitializationFactory() {
		if (DefaultLogUtils.isLog4j2()) {
			log4j = new Log4j2InitializationBean();
		} else {
			log4j = new Log4jInitializationBean();
		}
	}

	@Override
	public void onChanged(AllergoURL allergoURL, InputStream content) {
		log4j.onChanged(allergoURL, content);
	}

	@Override
	public AllergoURL getAllergoURL() {
		return log4j.getAllergoURL();
	}

	@Override
	public boolean isListenEnable() {
		return log4j.isListenEnable();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Log startupLogger = allergoContext.getStartupLogger();
		if (startupLogger.isDebugEnabled()) {
			startupLogger.debug("Loading Log4j configure by Pizza.");
		}
		log4j.afterPropertiesSet();
		if (startupLogger.isDebugEnabled()) {
			startupLogger.debug("Loaded log4j configure completed.");
		}
	}

	public AllergoContext getPizzaContext() {
		return allergoContext;
	}

	public void setPizzaContext(AllergoContext allergoContext) {
		this.allergoContext = allergoContext;
		log4j.setPizzaContext(allergoContext);
	}

}
