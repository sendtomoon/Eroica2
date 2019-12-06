package com.sendtomoon.eroica.common.security;

import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.alibaba.dubbo.common.extension.ExtensionLoader;

import com.sendtomoon.eroica.common.utils.EroicaConfigUtils;

public class PasswordProviderFactory {

	protected static Log logger = LogFactory.getLog(PasswordProviderFactory.class);

	public static final String DEF_PROVIDER_KEY = "eroica.password.provider";

	private static volatile PasswordProvider defaultProvider;

	public static PasswordProvider getProvider(String name) {
		if (name == null || (name = name.trim()).length() == 0) {
			return getDefaultProvider();
		}
		ExtensionLoader<PasswordProvider> loader = ExtensionLoader.getExtensionLoader(PasswordProvider.class);
		return loader.getExtension(name);
	}

	private static PasswordProvider getDefaultProvider() {
		if (defaultProvider == null) {
			defaultProvider = getDefaultProvider(System.getProperties());
		}
		return defaultProvider;
	}

	public static void refreshAllProviderConfig(Properties configure) {
		ExtensionLoader<PasswordProvider> loader = ExtensionLoader.getExtensionLoader(PasswordProvider.class);
		Set<String> providers = loader.getSupportedExtensions();
		for (String p : providers) {
			loader.getExtension(p).refreshConfig(configure);
		}
		defaultProvider = getDefaultProvider(configure);
	}

	private static PasswordProvider getDefaultProvider(Properties configure) {
		String providerClassName = configure.getProperty(DEF_PROVIDER_KEY);
		if (providerClassName == null) {
			providerClassName = System.getProperty(DEF_PROVIDER_KEY);
		}
		return getDefaultProviderByClassName(providerClassName);
	}

	private static PasswordProvider getDefaultProviderByClassName(String providerClassName) {
		ExtensionLoader<PasswordProvider> loader = ExtensionLoader.getExtensionLoader(PasswordProvider.class);
		if (providerClassName == null || providerClassName.length() == 0) {
			return loader.getDefaultExtension();
		} else {
			Class<?> clazz = null;
			try {
				clazz = EroicaConfigUtils.getRootClassLoader().loadClass(providerClassName);
			} catch (ClassNotFoundException ex) {
				throw new PasswordProviderException("Not found  PasswordProvider class<" + providerClassName + ">", ex);
			}
			// 调用，初始化
			loader.getDefaultExtension();
			// ------------
			String name = loader.getExtensionName(clazz);
			if (name == null) {
				throw new PasswordProviderException("Not found  PasswordProvider class<" + providerClassName + ">");
			}
			// -----
			return loader.getExtension(name);
		}
	}

}
