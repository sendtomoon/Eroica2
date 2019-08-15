package com.sendtomoon.eroica.allergo.impl;

import java.io.StringWriter;
import java.util.Properties;

public class ABC {

	public static void main(String args[]) throws Exception{
		Properties properties=new Properties();
		properties.put("a","b");
		properties.put("c","中国人");
		
		StringWriter out=new StringWriter();
		properties.store(out,"aaaa");
		System.out.println(out.toString());
	}
}
