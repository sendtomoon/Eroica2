package com.sendtomoon.eroica.allergo.log4j;

import org.junit.Test;

import com.sendtomoon.eroica2.allergo.Allergo;
import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;
import com.sendtomoon.eroica2.allergo.log4j.Log4j2Utils;

public class Log4j2UtilsTests {

	@Test
	public void test() {
		Allergo.getAllergoContext();
		Log4j2Utils.reconfigure(new AllergoURL("eroica/local.log4j.xml").toJavaURI());
	}
}
