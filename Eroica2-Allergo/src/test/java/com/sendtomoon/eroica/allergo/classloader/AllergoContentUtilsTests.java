package com.sendtomoon.eroica.allergo.classloader;

import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;

public class AllergoContentUtilsTests {
	public static void main(String args[]) {
		System.out.println(AllergoURL.isBase64Content("/lib/a.jar"));
		System.out.println(AllergoURL.isBase64Content("/zip/a.jar"));
		System.out.println(AllergoURL.isBase64Content("/sar/a.jar"));
		System.out.println(AllergoURL.isBase64Content("zip/a.jar"));
		System.out.println(AllergoURL.isBase64Content("allergo:/zip/a.jar"));
		System.out.println(AllergoURL.isBase64Content("/abc/zip/a.jar"));
	}
}
