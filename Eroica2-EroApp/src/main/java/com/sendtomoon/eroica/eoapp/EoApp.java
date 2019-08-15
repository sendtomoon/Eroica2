package com.sendtomoon.eroica.eoapp;

import org.springframework.context.ConfigurableApplicationContext;

import com.sendtomoon.eroica2.allergo.Allergo;

/**
 * 引导程序之一，启动组件之一
 *
 */
public abstract class EoApp implements EoAppContext {

	private volatile static EoApp eoApp;

	public synchronized static EoApp getInstance() {
		if (eoApp == null) {
			ConfigurableApplicationContext cac = Allergo.getSpringContext();
			eoApp = cac.getBean(EoApp.class);
		}
		return eoApp;
	}

}
