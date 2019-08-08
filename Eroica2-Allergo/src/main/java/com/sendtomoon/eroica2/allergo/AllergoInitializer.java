package com.sendtomoon.eroica2.allergo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.sendtomoon.eroica2.allergo.log4j.DefaultLogUtils;
import com.sendtomoon.eroica2.allergo.utils.FomatterStringBuilder;
import com.sendtomoon.eroica.common.Eroica;
import com.sendtomoon.eroica.common.utils.EroicaMeta;
import com.sendtomoon.eroica.common.utils.InstanceSystemPropertyUtils;
import com.sendtomoon.eroica.common.utils.NetUtils;

/**
 * 屏幕打印Logo，初始化主机配置参数
 *
 */
public class AllergoInitializer extends PropertyPlaceholderConfigurer implements InitializingBean {

	private Log startupLogger;

	private AllergoManager defaultManager;

	public AllergoInitializer() {
		startupLogger = DefaultLogUtils.getLogger();
		this.setSystemPropertiesMode(SYSTEM_PROPERTIES_MODE_OVERRIDE);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			printLogo();
			doInit();
		} catch (AllergoException ex) {
			startupLogger.error(ex.getMessage(), ex);
			throw ex;
		} catch (Throwable th) {
			throw new AllergoException("Allergo startup failure ,cause:" + th.getMessage(), th);
		}
	}

	/**
	 * 读取主机配置文件，初始化一些实例参数
	 */
	protected synchronized void doInit() {
		String instanceIp = NetUtils.getLocalHost();
//		AllergoManager defaultManager = this.defaultManager;
//		// 读取主机配置文件
//		if (instanceIp != null) {
//			loadInstanceProperties(defaultManager, instanceIp);
//		}
		FomatterStringBuilder info = new FomatterStringBuilder();
		info.append(AllergoConstants.KEY_APP_NAME, System.getProperty(AllergoConstants.KEY_APP_NAME));
		info.append(AllergoConstants.KEY_DOMAIN_ID, System.getProperty(AllergoConstants.KEY_DOMAIN_ID));
		info.append(AllergoConstants.KEY_MANAGER, System.getProperty(AllergoConstants.KEY_MANAGER));
		info.append(Eroica.LOG_HOME_KEY, System.getProperty(Eroica.LOG_HOME_KEY));
		info.append(InstanceSystemPropertyUtils.KEY_INSTANCE_IP, instanceIp);
		info.append(InstanceSystemPropertyUtils.KEY_INSTANCE_NAME, InstanceSystemPropertyUtils.getInstanceName());
		startupLogger.info("Allergo configure initialized,primary properties:" + info);
	}

	protected void loadInstanceProperties(AllergoManager allergoManager, String instanceIp) {
		String content = allergoManager.get("/allergo/instances/" + instanceIp);
		if (content != null && content.length() > 1) {
			Properties config = new Properties();
			try {
				config.load(new StringReader(content));
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			if (startupLogger.isInfoEnabled()) {
				startupLogger.info("Instance<" + instanceIp + ">allergo properties:" + config);
			}
			Set<Object> keyset = config.keySet();
			for (Object key : keyset) {
				System.setProperty((String) key, config.getProperty((String) key));
			}
		} else {
			if (startupLogger.isInfoEnabled()) {
				startupLogger.info("Instance<" + instanceIp + ">allergo properties:null");
			}
		}
	}

	/**
	 * Logo打印在屏幕
	 */
	protected void printLogo() {
		InputStream input = this.getClass().getResourceAsStream("/META-INF/eroica/logo.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		StringBuilder str = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				str.append("		" + line).append('\n');
			}
			str.append("		" + " :: V" + EroicaMeta.VERSION + "  :: "
					+ DateFormat.getDateTimeInstance().format(new Date()) + "\n");

			startupLogger.info(":: Eroica :: \n" + str.toString());
		} catch (Exception ex) {

		} finally {
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
	}

	public AllergoManager getDefaultManager() {
		return defaultManager;
	}

	public void setDefaultManager(AllergoManager defaultManager) {
		this.defaultManager = defaultManager;
	}

}
