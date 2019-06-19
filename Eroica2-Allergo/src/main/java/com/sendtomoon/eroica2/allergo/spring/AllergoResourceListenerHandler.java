package com.sendtomoon.eroica2.allergo.spring;

import java.io.InputStream;

import com.sendtomoon.eroica2.allergo.AllergoListener;
import com.sendtomoon.eroica2.allergo.classloader.AllergoContentUtils;
import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;

public class AllergoResourceListenerHandler implements AllergoListener {

	private AllergoResourceListener listener;

	private AllergoURL allergoURL;

	public AllergoResourceListenerHandler(AllergoURL allergoURL, AllergoResourceListener listener) {
		this.listener = listener;
		this.allergoURL = allergoURL;
	}

	@Override
	public void handleConfigChange(String allergoContent) {
		if (allergoContent != null) {
			InputStream stream = AllergoContentUtils.toIputStream(allergoURL, allergoContent);
			listener.onChanged(allergoURL, stream);
		}
	}

}
