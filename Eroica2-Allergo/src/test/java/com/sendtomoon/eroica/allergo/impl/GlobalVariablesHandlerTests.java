package com.sendtomoon.eroica.allergo.impl;

import org.junit.Test;

import com.sendtomoon.eroica.common.utils.URLUtils;
import com.sendtomoon.eroica2.allergo.Allergo;
import com.sendtomoon.eroica2.allergo.AllergoManager;
import com.sendtomoon.eroica2.allergo.impl.GlobalVariablesHandler;
import com.sendtomoon.eroica2.allergo.impl.ZookeeperAllergoManagerFactory;

public class GlobalVariablesHandlerTests {

	@Test
	public void testVars() {
		Allergo.getAllergoContext();
		GlobalVariablesHandler i = new GlobalVariablesHandler();
		AllergoManager local = new ZookeeperAllergoManagerFactory()
				.create(URLUtils.valueOf("zookeeper://127.0.0.1:2181"));

		i.init("/paconfigs/" + Allergo.GROUP_GLOBALVARS + "/FF", local);
		String originalValue = new String("test=@{pama.t3.url}\nabc=\tabc");
		// --------------------------------------------------------------------
		String result = i.handle("papp/test.propertyies", originalValue);
		System.err.println(result);
	}
}
