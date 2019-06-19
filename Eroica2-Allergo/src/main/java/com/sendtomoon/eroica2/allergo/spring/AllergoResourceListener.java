package com.sendtomoon.eroica2.allergo.spring;

import java.io.InputStream;

import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;

public interface AllergoResourceListener {

	void onChanged(AllergoURL allergoURL,InputStream content);
	
	AllergoURL getAllergoURL();
	
	boolean isListenEnable();
}
