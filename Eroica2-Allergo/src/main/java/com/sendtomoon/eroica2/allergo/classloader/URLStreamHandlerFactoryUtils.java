package com.sendtomoon.eroica2.allergo.classloader;

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

import org.apache.commons.logging.LogFactory;

import com.sendtomoon.eroica2.allergo.AllergoManager;

public class URLStreamHandlerFactoryUtils {

	public static void registerAllergoURLStreamHandler(final AllergoManager allergoManager) {
		try {
			URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {

				@Override
				public URLStreamHandler createURLStreamHandler(String protocol) {
					if ("allergo".equals(protocol)) {
						return new AllergoURLStreamHandler(allergoManager);
					}
					return null;
				}

			});
		} catch (Error err) {
			LogFactory.getLog(URLStreamHandlerFactoryUtils.class)
					.warn("RegisterAllergoURLStreamHandler error,cause:" + err.getMessage());
			// err.printStackTrace();
		}
	}
}
