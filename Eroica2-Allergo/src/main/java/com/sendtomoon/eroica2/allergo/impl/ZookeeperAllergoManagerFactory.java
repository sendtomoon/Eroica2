package com.sendtomoon.eroica2.allergo.impl;

import com.sendtomoon.eroica2.allergo.AllergoManager;
import com.sendtomoon.eroica2.allergo.AllergoManagerFactory;
import com.sendtomoon.eroica2.common.utils.URLUtils;

public class ZookeeperAllergoManagerFactory implements AllergoManagerFactory {

	@Override
	public AllergoManager create(URLUtils configURL) {
		ZookeeperAllergoManager mananger = new ZookeeperAllergoManager();
		mananger.init(configURL);
		return mananger;
	}

}
