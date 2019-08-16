package com.sendtomoon.eroica.eoapp.context;

import com.sendtomoon.eroica2.allergo.spring.AllergoXmlApplicationContext;

/**
 * root层上下文
 *
 */
public class EoAppRootContext extends AllergoXmlApplicationContext {

	private static final String NAME = "eoapp_root";

	private static final String CONGIGLOCATION_NAME = "classpath:/META-INF/eroica/eoapp-root.spring.xml";

	public EoAppRootContext() {
		super(NAME);
		setId(NAME);
		this.setConfigLocation(CONGIGLOCATION_NAME);
		this.refresh();
		if (logger.isInfoEnabled()) {
			logger.info("EoAppRootContext=" + this + "");
		}
	}

}
