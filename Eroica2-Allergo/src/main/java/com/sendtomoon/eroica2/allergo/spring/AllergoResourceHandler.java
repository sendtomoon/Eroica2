package com.sendtomoon.eroica2.allergo.spring;

import java.io.InputStream;

import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;

public interface AllergoResourceHandler {

	void handleAllergoResource(AllergoURL allergoURL,InputStream content)throws Exception;
	
}
