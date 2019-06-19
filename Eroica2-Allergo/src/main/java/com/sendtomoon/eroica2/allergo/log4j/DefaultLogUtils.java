package com.sendtomoon.eroica2.allergo.log4j;

import java.io.FileNotFoundException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

public class DefaultLogUtils {

	private static final String LOG_NAME = "ALLERGO";

	private volatile static boolean isLog4j_2 = true;

	static {
		try {
			ClassUtils.forName("org.apache.logging.log4j.Logger", ClassUtils.getDefaultClassLoader());
		} catch (Exception e) {
			isLog4j_2 = false;
		}
		if (!defConfigFileEnable()) {
			if (isLog4j2()) {
				try {

					com.sendtomoon.eroica2.allergo.log4j.Log4j2Utils
							.reconfigure(getResourceURL("default-log4j2.xml").toURI());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					org.apache.log4j.PropertyConfigurator.configure(getResourceURL("default-log4j.properties"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected static boolean isLog4j2() {
		return isLog4j_2;
	}

	private static URL getResourceURL(String fileSimpleName) throws Exception {
		String fullName = "META-INF/eroica/log4j/" + fileSimpleName;
		return new ClassPathResource(fullName, DefaultLogUtils.class.getClassLoader()).getURL();
	}

	public static Log getLogger() {
		return LogFactory.getLog(LOG_NAME);
	}

	protected static boolean defConfigFileEnable() {
		if (isLog4j2()) {
			try {
				ResourceUtils.getFile("classpath:log4j2.xml");
				return true;
			} catch (FileNotFoundException e) {
			}
		} else {
			try {
				ResourceUtils.getFile("classpath:log4j.properties");
				return true;
			} catch (FileNotFoundException e) {
			}
			try {
				ResourceUtils.getFile("classpath:log4j.xml");
				return true;
			} catch (FileNotFoundException e) {
			}
		}

		return false;
	}

}
