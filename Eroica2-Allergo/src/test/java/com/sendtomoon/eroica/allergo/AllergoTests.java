package com.sendtomoon.eroica.allergo;

import java.util.regex.Pattern;

import org.junit.Test;

import com.sendtomoon.eroica2.allergo.Allergo;

public class AllergoTests {

	private static final String NAME_PATTERN_STRING = "^[\\w\\-]+\\.[\\w\\-\\.\\#]+$";

	private static final Pattern NAME_PATTERN = Pattern.compile(NAME_PATTERN_STRING);

	@Test
	public void test() {
		System.out.println(NAME_PATTERN.matcher("nauga.nan-gua_#").matches());
		// Allergo.getManager().set("resources","sar#test#com.nangua.abc.properties",
		// "abc");
		for (int i = 0; i < 10; i++)
			Allergo.getManager().set("resources", "sar#test#com.nangua.abc.properties", "abc");
		System.out.println(Allergo.get("resources", "sar#test#com.nangua.abc.properties"));
	}

	@Test
	public void test2() {
		for (int i = 0; i < 10; i++) {// 300
			Allergo.getManager().add("test", "abc", "aaa");
		}
		System.err.println("time=" + Allergo.getManager().get("test", "abc"));
	}
}
