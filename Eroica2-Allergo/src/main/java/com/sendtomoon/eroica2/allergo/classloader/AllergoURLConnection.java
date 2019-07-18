package com.sendtomoon.eroica2.allergo.classloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.sendtomoon.eroica2.allergo.AllergoManager;

public class AllergoURLConnection extends URLConnection {

	private AllergoManager manager;

	public AllergoURLConnection(URL url, AllergoManager manager) {
		super(url);
		this.manager = manager;
	}

	@Override
	public void connect() throws IOException {

	}

	public InputStream getInputStream() throws IOException {
		URL url = this.getURL();
		AllergoURL allergoURL = new AllergoURL(url.getFile());
		return AllergoContentUtils.toIputStream(manager, allergoURL);
	}

}
