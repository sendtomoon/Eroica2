package com.sendtomoon.eroica.allergo.classloader;

import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;

public class AllergoURLTests {

	@Test
	public void test() throws Exception {
		try {
			AllergoURL.valueOf("test.properites");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		AllergoURL.valueOf("def/test.properites");
		//
		AllergoURL.valueOf("allergo:/abc/aaa.properites");
		//
		AllergoURL.valueOf("eroica/local.properties").getInputStream();

		System.err.println(AllergoURL.valueOf("eroica/local.properties").getTextContent());
		System.err
				.println("abc=" + AllergoURL.valueOf("allergo:/abdd/abc/papp/local.properties").toJavaURL().getPath());

		System.err.println(IOUtils.toString(AllergoURL.valueOf("eroica/local.properties").toJavaURL().openStream()));

		System.err.println(IOUtils.toString(new URL("allergo:/eroica/local.properties").openStream()));
		// System.err.println(new URL("file:/papp/local.properties").openStream());
		// new URL("http://127.0.0.1:2181").openStream();

		System.err.println("abc=" + AllergoURL.valueOf("allergo:/abdd/abc/eroica/local.properties").getRootPath());

	}
}
