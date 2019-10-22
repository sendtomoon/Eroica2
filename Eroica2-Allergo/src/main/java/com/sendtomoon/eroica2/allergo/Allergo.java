package com.sendtomoon.eroica2.allergo;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ResourceUtils;

import com.sendtomoon.eroica.common.Eroica;
import com.sendtomoon.eroica.common.utils.MDCUtil;
import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;
import com.sendtomoon.eroica2.allergo.context.RootContextBean;
import com.sendtomoon.eroica2.allergo.log4j.DefaultLogUtils;
import com.sendtomoon.eroica2.allergo.spring.AllergoResourceListener;

/**
 * 读取配置文件 配置allergo环境
 * 
 */
public class Allergo extends AllergoConstants {

	static volatile EroicaContext eroicaContext;

	private volatile static ConfigurableApplicationContext context = null;

	private volatile static Log startupLogger;

	static {
		long t1 = System.nanoTime();
		startupLogger = DefaultLogUtils.getLogger();
		try {
			// 日志设置
			MDCUtil.set();
			// 加载基本资源，app基础配置文件 eroica_base.properties
			loadConfigResource();
			// Spring Framework 框架开始介入集成
			context = new RootContextBean();
			eroicaContext = context.getBean(EroicaContext.class);
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				public void run() {
					System.out.println("Eroica<" + Allergo.getAppName() + "> shutdown hook now" + ",datetime="
							+ DateFormat.getDateTimeInstance().format(new Date()) + ".");
					if (context != null)
						context.close();
					com.alibaba.dubbo.config.ProtocolConfig.destroyAll();
				}
			}, "EroicaShutdownHook"));
		} catch (Exception ex) {
			String msg = "Allergo initialized error,cause:" + ex.getMessage();
			ex.printStackTrace(System.err);
			startupLogger.error(msg, ex);
		} finally {
			if (startupLogger.isInfoEnabled()) {
				startupLogger.info("Allergo Startuped,times=" + (System.nanoTime() - t1) / 1000 / 1000.0 + "ms.");
			}
		}
	}

	public static ConfigurableApplicationContext getSpringContext() {
		return context;
	}

	public static String getAppName() {
		return eroicaContext.getAppName();
	}

	public static String getDomainId() {
		return eroicaContext.getDomainId();
	}

	public static String get(String group, String key) {
		return eroicaContext.get(group, key);
	}

	public static boolean exists(String group, String key) {
		return eroicaContext.exists(group, key);
	}

	public static boolean registerListener(AllergoResourceListener listener) {
		return eroicaContext.registerListener(listener);
	}

	public static boolean unregisterListener(AllergoURL allergoURL) {
		return eroicaContext.unregisterListener(allergoURL);
	}

	public static Charset getCharset() {
		return eroicaContext.getCharset();
	}

	public static AllergoManager getManager() {
		return eroicaContext.getDefaultManager();
	}

	public static EroicaContext getAllergoContext() {
		return eroicaContext;
	}

	/**
	 * 加载 eroica_base.properties 配置文件
	 * @throws AllergoException
	 */
	private static synchronized void loadConfigResource() throws AllergoException {
		String configResource = null;
		try {
			configResource = System.getProperty(AllergoConstants.KEY_CONFIG_FILE);
			InputStream resourceInput = null;
			if (configResource == null || (configResource = configResource.trim()).length() == 0) {
				configResource = AllergoConstants.DEF_CONFIG_FILE;
				try {
					resourceInput = ResourceUtils.getURL(configResource).openStream();
				} catch (FileNotFoundException ex) {
					
				}
			} else {
				resourceInput = ResourceUtils.getURL(configResource).openStream();
				if (resourceInput == null) {
					throw new FileNotFoundException("Resource=" + configResource + " not found.");
				}
			}

			if (resourceInput == null) {
				startupLogger.info("Allergo configure resource not found.");
				loadSystemEnvs();
				return;
			}

			Properties properties = new Properties();
			startupLogger.info("Found Allergo configure resource=" + configResource);

			try {
				properties.load(new InputStreamReader(resourceInput, "UTF-8"));
			} finally {
				resourceInput.close();
			}

			for (Object key : properties.keySet()) {
				String value = properties.getProperty((String) key, "");
				if (value != null && value.length() > 0) {
					System.setProperty((String) key, value);
				}
			}

			startupLogger.info("Parsed Allergo configure resource=" + configResource + ",propertiesSize="
					+ properties.size() + (properties.size() > 0 ? ",properties:" + properties : ""));
		} catch (Throwable th) {
			throw new AllergoException("Loaded Allergo configure failure,cause:" + th.getMessage(), th);
		}
	}

	/**
	 * 从系统环境变量设置Eroica变量值
	 */
	private static void loadSystemEnvs() {
		String allergoManager = System.getenv(AllergoConstants.ENV_MANAGER);
		if (StringUtils.isNotBlank(allergoManager)) {
			System.setProperty(AllergoConstants.KEY_MANAGER, allergoManager);
		}
		String allergoDomainId = System.getenv(AllergoConstants.ENV_DOMAIN_ID);
		if (StringUtils.isNotBlank(allergoDomainId)) {
			System.setProperty(AllergoConstants.KEY_DOMAIN_ID, allergoDomainId);
		}
		String allergoAppName = System.getenv(AllergoConstants.ENV_APP_NAME);
		if (StringUtils.isNotBlank(allergoAppName)) {
			System.setProperty(AllergoConstants.KEY_APP_NAME, allergoAppName);
		}
		String eroicaLogHome = System.getenv(AllergoConstants.ENV_LOG_HOME);
		if (StringUtils.isNotBlank(eroicaLogHome)) {
			System.setProperty(Eroica.LOG_HOME_KEY, eroicaLogHome);
		}
	}

	/**
	 * @deprecated
	 * @param group
	 * @param key
	 * @param listener
	 */
	public static void setListener(String group, String key, ConfigChangedListener listener) {
		eroicaContext.getDefaultManager().setListener(group, key, listener);
	}

	/***
	 * @deprecated
	 * @param group
	 * @param key
	 * @param listener
	 */
	public static void removeListener(String group, String key, ConfigChangedListener listener) {
		eroicaContext.getDefaultManager().removeListener(group, key, listener);
	}

}
