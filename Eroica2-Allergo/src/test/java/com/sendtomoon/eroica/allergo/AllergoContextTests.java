package com.sendtomoon.eroica.allergo;

import com.sendtomoon.eroica.common.utils.NetUtils;
import com.sendtomoon.eroica.common.utils.URLUtils;
import com.sendtomoon.eroica2.allergo.Allergo;
import com.sendtomoon.eroica2.allergo.AllergoManager;
import com.sendtomoon.eroica2.allergo.impl.ZookeeperAllergoManager;

public class AllergoContextTests {

	public static void main(String args[]) {
		AllergoManager m = new ZookeeperAllergoManager();
		m.init(URLUtils.valueOf("zookeeper://192.168.0.9:20001"));
		String instanceIp = NetUtils.getLocalHost();
		m.set("/allergo/instances/" + instanceIp, "allergo.app.name=Eroica");
		Allergo.getManager();
	}

}
