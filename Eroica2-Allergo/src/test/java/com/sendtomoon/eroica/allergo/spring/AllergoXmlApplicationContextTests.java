package com.sendtomoon.eroica.allergo.spring;

import com.sendtomoon.eroica2.allergo.spring.AllergoResource;
import com.sendtomoon.eroica2.allergo.spring.AllergoXmlApplicationContext;

public class AllergoXmlApplicationContextTests {

	public static void main(String args[]) {
		try {
			AllergoXmlApplicationContext a = new AllergoXmlApplicationContext("aaa",
					new AllergoResource[] { new AllergoResource("allergo:/def/test.spring.xml") });
			a.setConfigLocation("classpath*:com/sendtomoon/eroica/allergo/spring/*.spring.xml");
			a.refresh();
			System.err.println(a.getBean("_test") + "11111111111111");
			System.err.println(a.getBean("_test2") + "11111111111111");
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}
}
