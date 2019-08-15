package com.sendtomoon.eroica.allergo.impl;

import com.sendtomoon.eroica.common.utils.URLUtils;
import com.sendtomoon.eroica2.allergo.AllergoManager;
import com.sendtomoon.eroica2.allergo.impl.ZookeeperAllergoManager;

public class ZookeeperAllergoManagerTests3 {

	public static void main(String args[]) throws Exception {
		AllergoManager m = new ZookeeperAllergoManager();
		m.init(URLUtils.valueOf("zookeeper://127.0.0.1:2181?rootPath=/test3"));
		m.createPath("sar");
		m.set("sar/hello.properties", "name=nangua", true);
		m.set("def/nangua/hello.xml", "<name>nangua</name>", true);
		System.in.read();
	}
}
