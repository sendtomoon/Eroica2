package com.sendtomoon.eroica2.allergo.context;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RootContextBean extends ClassPathXmlApplicationContext {

	private static final String NAME = "eroica_root";

	private static final String CONGIGLOCATION_NAME = "classpath*:/META-INF/eroica/root.spring.xml";

	public RootContextBean() {
		setId(NAME);
		this.setDisplayName(NAME);
		this.setConfigLocation(CONGIGLOCATION_NAME);
		this.refresh();
		if (logger.isInfoEnabled()) {
			logger.info("EroicaRootContext=" + this + "");
		}
	}

}
