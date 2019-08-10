package com.sendtomoon.eroica2.allergo.utils;

import java.util.Enumeration;
import java.util.Properties;

import com.sendtomoon.eroica.common.utils.EroicaConfigUtils;

public class PropertiesUtil {

	protected static void bind(Properties properties, String suffix, Object target) {
		Properties map = populateMap(properties, suffix);
		if (map != null) {
			try {
				EroicaConfigUtils.bindBean(map, target);
			} catch (Throwable e) {
				throw new IllegalArgumentException("Bind properties" + properties + " to bean:" + target + "error:", e);
			}
		}
	}

	private static Properties populateMap(Properties properties, String suffix) {
		if (properties == null || properties.size() == 0) {
			return null;
		}
		Properties map = new Properties();
		Enumeration<?> names = properties.propertyNames();
		while (names.hasMoreElements()) {
			String _key = (String) names.nextElement();
			if (_key.startsWith(suffix)) {
				String shortKey = _key.substring(suffix.length());
				StringBuilder sb = new StringBuilder(shortKey.length());
				shortKey = shortKey.trim();
				if (shortKey.length() == 0) {
					continue;
				}
				for (int i = 0; i < shortKey.length(); i++) {
					char ch = shortKey.charAt(i);
					if (ch == '.') {
						i++;
						sb.append(Character.toUpperCase(shortKey.charAt(i)));
					} else {
						sb.append(shortKey.charAt(i));
					}
				}
				String value = (String) properties.get(_key);
				if (value != null && (value = value.trim()).length() > 0) {
					map.put(sb.toString(), value);
				}
			}
		}
		return map;
	}

}
