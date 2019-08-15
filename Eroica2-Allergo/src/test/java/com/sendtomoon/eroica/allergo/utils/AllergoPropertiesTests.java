package com.sendtomoon.eroica.allergo.utils;

import java.util.Properties;

import org.junit.Test;

import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;
import com.sendtomoon.eroica2.allergo.utils.AllergoProperties;

public class AllergoPropertiesTests {

	@Test
	public void test() {
		Properties props = AllergoProperties.loadProperties(AllergoURL.valueOf("/papp/local.jetty.properties"));
		System.err.println(props.getProperty("abc"));
		for (Object key : props.keySet()) {
			System.err.println(key.toString().trim());
		}
		System.err.println(props);
	}
}
