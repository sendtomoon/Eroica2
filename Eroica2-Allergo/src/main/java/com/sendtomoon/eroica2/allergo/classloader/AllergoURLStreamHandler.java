package com.sendtomoon.eroica2.allergo.classloader;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import com.sendtomoon.eroica2.allergo.AllergoManager;

public class AllergoURLStreamHandler extends URLStreamHandler {

	private AllergoManager manager;

	public AllergoURLStreamHandler() {
		manager = com.sendtomoon.eroica2.allergo.Allergo.getManager();
	}

	public AllergoURLStreamHandler(AllergoManager manager) {
		this.manager = manager;
	}

	@Override
	protected URLConnection openConnection(URL u) throws IOException {
		return new AllergoURLConnection(u, manager);
	}

}
