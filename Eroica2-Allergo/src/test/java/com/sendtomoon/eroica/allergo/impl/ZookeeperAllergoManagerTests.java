package com.sendtomoon.eroica.allergo.impl;

import java.util.List;

import org.junit.Test;

import com.sendtomoon.eroica.common.utils.URLUtils;
import com.sendtomoon.eroica2.allergo.AllergoListener;
import com.sendtomoon.eroica2.allergo.AllergoManager;
import com.sendtomoon.eroica2.allergo.AllergoPathListener;
import com.sendtomoon.eroica2.allergo.impl.ZookeeperAllergoManager;

public class ZookeeperAllergoManagerTests {

	@Test
	public void test11() throws Exception {
		AllergoManager m = new ZookeeperAllergoManager();
		m.init(URLUtils
				.valueOf("zookeeper://192.168.0.9:20001?domainId=testdomain&rootPath=/paconfigs&localBackup=true"));
		String path = "eroica/aaa/addr";
		m.set(path, "深南大道1号");
		String data = m.get(path);
		//
		m.del(path);
		System.err.println(path + "=" + data);
		// -------------------------------------
		m.forceDel("eroica");
		m.del("eroica", "abc.properties");
		// test add
		m.setListener("eroica", "abc.properties", new AllergoListener() {

			@Override
			public void handleConfigChange(String allergoContent) {
				System.err.println("Listener=" + allergoContent);
			}

		});
		m.setPathListener("eroica", new AllergoPathListener() {

			@Override
			public void allergoPathChanged(String parentPath, List<String> childrenPaths) {
				System.err.println("parentPath=" + childrenPaths);
			}
		});
		System.err.println("add1=" + m.add("eroica", "abc.properties", "2"));
		System.err.println("add2=" + m.add("eroica", "abc.properties", "2"));
		System.err.println("add3=" + m.add("eroica", "abc3.properties", "4"));
		System.err.println("add4=" + m.add("eroica", "abc5.properties", "4"));
		System.err.println("add5=" + m.get("eroica", "abc.properties"));
		System.err.println("del=" + m.del("eroica", "abc.properties"));
		System.err.println("get=" + m.get("eroica", "abc.properties"));
		System.err.println("add6=" + m.add("eroica", "abc6.properties", "9"));
		System.err.println("listChildren=" + m.listChildren("papp"));
		// test set
		m.set("eroica", "abc.properties", "4");
		m.removeListener("papp", "abc.properties");
		m.set("eroica", "abc.properties", "5");
		System.err.println("get2=" + m.get("eroica", "abc.properties"));
		System.in.read();
	}
}
