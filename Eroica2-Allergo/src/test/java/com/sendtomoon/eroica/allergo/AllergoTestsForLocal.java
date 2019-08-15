package com.sendtomoon.eroica.allergo;

import java.io.File;
import java.util.regex.Pattern;

import org.junit.Test;

import com.sendtomoon.eroica2.allergo.Allergo;

public class AllergoTestsForLocal {

	private static final String NAME_PATTERN_STRING = "^[\\w\\-]+\\.[\\w\\-\\.\\#]+$";

	private static final Pattern NAME_PATTERN = Pattern.compile(NAME_PATTERN_STRING);

	@Test
	public void test() throws Exception {
		System.out.println("t1=" + NAME_PATTERN.matcher("nauga.nan-gua_#").matches());
		File file = new File("D:/temp");
		System.out.println(file.toURI().toURL());
		System.out.println("t2=" + Allergo.get("resources", "sar#test#com.nangua.abc.properties"));
	}

}
