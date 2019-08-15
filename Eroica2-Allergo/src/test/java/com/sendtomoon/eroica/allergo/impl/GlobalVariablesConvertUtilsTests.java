package com.sendtomoon.eroica.allergo.impl;

import com.sendtomoon.eroica2.allergo.impl.GlobalVariablesConvertUtils;

public class GlobalVariablesConvertUtilsTests {

	public static void main(String args[]) throws Exception {
		System.setProperty("test.ab", "nangua");
		System.setProperty("test.b", "nangua");
		// ----------------------------------------
		String originalValue = new String("abc=\tabc\n########=@{test1.b}\n");

		String result = GlobalVariablesConvertUtils.convert(originalValue);
		System.err.println("Result:" + result);
		// ----------------------------------------------------------------------------------

	}
}
