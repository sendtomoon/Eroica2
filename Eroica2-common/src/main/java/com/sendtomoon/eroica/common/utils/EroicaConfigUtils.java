package com.sendtomoon.eroica.common.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.sendtomoon.eroica.common.beans.map.BeanMapUtils;
import com.sendtomoon.eroica.common.exception.EroicaException;

public class EroicaConfigUtils {

	public static Set<String> split(String text) {
		if (text != null && text.length() > 0) {
			Set<String> set = new LinkedHashSet<String>();
			String[] list = StringUtils.split(text, ",");
			for (String sid : list) {
				if (sid != null && (sid = sid.trim()).length() > 0) {
					set.add(sid);
				}
			}
			return set;
		}
		return null;
	}

	public static Properties toProperties(Properties props, String configValue) {
		try {
			props.load(new StringReader(configValue));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}

	public static Properties toProperties(String configValue) {
		Properties props = new Properties();
		return toProperties(props, configValue);
	}

	public static ClassLoader getRootClassLoader() {
		return EroicaConfigUtils.class.getClassLoader();
	}

	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
		}
		if (cl == null) {
			cl = EroicaConfigUtils.class.getClassLoader();
		}
		return cl;
	}

	public static void bindBean(Properties props, Object target) {
		BeanMapUtils utils = new BeanMapUtils();
		try {
			utils._toBean(target, props);
		} catch (Exception e) {
			throw new EroicaException(
					"Bind properties to class<" + target.getClass().getName() + "> error:" + e.getMessage(), e);
		}
	}

}
