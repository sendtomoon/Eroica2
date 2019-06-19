package com.sendtomoon.eroica2.allergo.utils;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import com.sendtomoon.eroica2.allergo.AllergoException;
import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;

public class AllergoProperties extends Properties {

	private static final long serialVersionUID = 1L;

	public AllergoProperties(AllergoURL allergoURL, boolean requriedExists) {
		this.load(allergoURL, requriedExists);
	}

	public AllergoProperties(String allergoURL, boolean requriedExists) {
		this(AllergoURL.valueOf(allergoURL), requriedExists);
	}

	public AllergoProperties(String allergoURL) {
		this(AllergoURL.valueOf(allergoURL), false);
	}

	public AllergoProperties() {
	}

	public AllergoProperties(AllergoURL allergoURL) {
		this(allergoURL, false);
	}

	public void load(AllergoURL allergoURL) {
		this.load(allergoURL, false);
	}

	public void load(AllergoURL allergoURL, boolean requriedExists) {
		loadProperties(this, allergoURL, requriedExists);
	}

	public static Properties loadProperties(AllergoURL allergoURL) {
		return loadProperties(allergoURL, false);
	}

	public static Properties loadProperties(AllergoURL allergoURL, boolean requriedExists) {
		return loadProperties(null, allergoURL, requriedExists);
	}

	public static Properties loadProperties(Properties properties, AllergoURL allergoURL) {
		return loadProperties(properties, allergoURL, false);
	}

	public static Properties loadProperties(Properties properties, AllergoURL allergoURL, boolean requriedExists) {
		Reader reader = allergoURL.getReader(requriedExists);
		if (reader != null) {
			if (properties == null) {
				properties = new Properties();
			}
			try {
				properties.load(reader);
			} catch (IOException e) {
				throw new AllergoException(e.getMessage(), e);
			}
		}
		return properties;
	}

}
