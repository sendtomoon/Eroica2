package com.sendtomoon.eroica2.allergo;

import java.nio.charset.Charset;

import org.apache.commons.logging.Log;

import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;
import com.sendtomoon.eroica2.allergo.spring.AllergoResourceListener;

public interface AllergoContext {

	String getAppName();

	String getDomainId();

	public Charset getCharset();

	public Log getStartupLogger();

	public String get(String group, String key);

	public String get(String path);

	boolean exists(String group, String key);

	boolean exists(String path);

	public AllergoManager getDefaultManager();

	public AllergoManager createManager(String configURL);

	boolean registerListener(AllergoResourceListener listener);

	boolean unregisterListener(AllergoURL allergoURL);

}
