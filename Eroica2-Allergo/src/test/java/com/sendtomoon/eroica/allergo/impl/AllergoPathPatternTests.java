package com.sendtomoon.eroica.allergo.impl;

import java.util.regex.Pattern;

import com.sendtomoon.eroica2.allergo.classloader.AllergoURLException;

public class AllergoPathPatternTests {

	private static final String ALLERGO_PATH_PATTERN_STRING = "^/[\\w\\-]+(/[\\w\\-\\.\\#]+)+$";

	private static final Pattern ALLERGO_PATH_PATTERN = Pattern.compile(ALLERGO_PATH_PATTERN_STRING);

	protected final static void checkAllergoPath(String allergoPath) {
		if (allergoPath == null) {
			throw new AllergoURLException("allergoPath is null.");
		}
		if (!ALLERGO_PATH_PATTERN.matcher(allergoPath).matches()) {
			throw new AllergoURLException("AllergoPath<" + allergoPath + "> format error,Not matched by regex="
					+ ALLERGO_PATH_PATTERN_STRING + ".");
		}
	}

	public static void main(String args[]) {
		checkAllergoPath("/abc");
		checkAllergoPath("/abc/abc");
		checkAllergoPath("/abc/abc.txt");
		System.out.println("-----------");
	}
}
