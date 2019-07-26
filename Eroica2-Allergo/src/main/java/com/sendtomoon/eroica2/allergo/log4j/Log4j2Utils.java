package com.sendtomoon.eroica2.allergo.log4j;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import com.sendtomoon.eroica2.allergo.Allergo;
import com.sendtomoon.eroica2.allergo.AllergoException;
import com.sendtomoon.eroica.common.utils.NetUtils;

public class Log4j2Utils {

	private static final String FQCN = org.apache.log4j.Logger.class.getName();

	public static org.apache.logging.log4j.spi.LoggerContext _getContext() {
		return org.apache.logging.log4j.LogManager.getFactory().getContext(FQCN,
				org.apache.log4j.Logger.class.getClassLoader(), null, false);
	}

	public static void reconfigure(final URI configLocation) {
		final org.apache.logging.log4j.core.LoggerContext ctx = (org.apache.logging.log4j.core.LoggerContext) _getContext();
		ctx.setConfigLocation(configLocation);

	}

	public static File resolveLocalConfigureFile() {
		String config_dir = System.getProperty("eroica.log.home");
		if (config_dir == null || (config_dir = config_dir.trim()).length() == 0) {
			config_dir = System.getProperty("user.home");
			config_dir = config_dir + "/" + Allergo.getAppName() + "/" + NetUtils.getLocalHost();
		}
		config_dir = config_dir + "/log4j";
		//
		File configDirectory = new File(config_dir);
		// ----------------------------------------------------------------
		configDirectory.mkdirs();
		if (!configDirectory.exists()) {
			throw new AllergoException("Directory:" + config_dir + " created failure,privliege error for user="
					+ System.getProperty("user.name"));
		}
		if (!configDirectory.isDirectory()) {
			throw new AllergoException("Directory:" + config_dir + "  not be directory.");
		}
		File configFile = new File(configDirectory, "log4j.xml");
		if (configFile.exists()) {
			if (!configFile.delete()) {
				throw new AllergoException("File:" + configFile + " delete failure,privliege error for user="
						+ System.getProperty("user.name"));
			}
		}
		try {
			if (!configFile.createNewFile()) {
				throw new AllergoException("File:" + configFile + " create failure,privliege error for user="
						+ System.getProperty("user.name"));
			}
		} catch (IOException e) {
			throw new AllergoException("File:" + configFile + " create failure,privliege error for user="
					+ System.getProperty("user.name"));
		}
		return configFile;
	}
}
