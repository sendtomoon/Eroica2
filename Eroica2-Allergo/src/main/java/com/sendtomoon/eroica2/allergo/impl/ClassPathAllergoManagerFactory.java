package com.sendtomoon.eroica2.allergo.impl;

import com.sendtomoon.eroica2.allergo.AllergoManager;
import com.sendtomoon.eroica2.allergo.AllergoManagerFactory;
import com.sendtomoon.eroica2.common.utils.URLUtils;

public class ClassPathAllergoManagerFactory implements AllergoManagerFactory {

	@Override
	public AllergoManager create(URLUtils configURL) {
		ClassPathAllergoManager manager = new ClassPathAllergoManager();
		configURL = configURL.addParameter("localBackup", false);
		if (configURL.getParameter("rootPath") == null) {
			configURL = configURL.addParameter("rootPath", "/");
		}
		manager.init(configURL);
		return manager;
	}

}
