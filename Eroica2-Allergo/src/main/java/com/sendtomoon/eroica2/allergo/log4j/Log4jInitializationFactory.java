package com.sendtomoon.eroica2.allergo.log4j;

import java.io.InputStream;

import org.apache.commons.logging.Log;

import com.sendtomoon.eroica2.allergo.EroicaContext;
import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;

public class Log4jInitializationFactory implements Log4jInitialization {

	private Log4jInitialization log4j;

	private EroicaContext eroicaContext;

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
		Log startupLogger = eroicaContext.getStartupLogger();
		if (startupLogger.isDebugEnabled()) {
			startupLogger.debug("Loading Log4j configure by Allergo.");
		}
		log4j.afterPropertiesSet();
		if (startupLogger.isDebugEnabled()) {
			startupLogger.debug("Loaded log4j configure completed.");
		}
	}

	public EroicaContext getAllergoContext() {
		return eroicaContext;
	}

	public void setAllergoContext(EroicaContext eroicaContext) {
		this.eroicaContext = eroicaContext;
		log4j.setAllergoContext(eroicaContext);
	}

}
