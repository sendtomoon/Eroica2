package com.sendtomoon.eroica.common.utils;

import java.util.regex.Pattern;


public class ESAPatternUtils {

	private static final String ESA_NAME_PATTERN_STRING = "^[\\w\\-\\.]+$";
	
	
	private static final Pattern ESA_NAME_PATTERN = Pattern.compile(ESA_NAME_PATTERN_STRING);
	
	public static void check(String esaName) {
		if(esaName==null){
			throw new java.lang.IllegalArgumentException("ESA Name is null.");
		}
		if(!ESA_NAME_PATTERN.matcher(esaName).matches()){
			throw new IllegalArgumentException("ESA Name="+esaName+",Not matched by regex="+ESA_NAME_PATTERN_STRING+".");
		}
	}
}
