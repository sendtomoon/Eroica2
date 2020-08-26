package com.sendtomoon.eroica.common.utils;

import com.alibaba.dubbo.common.Version;

public class EroicaMeta {

	public static String VERSION = Version.getVersion(EroicaMeta.class, "1.0");

	public static String getVersion() {
		return VERSION;
	}

}
