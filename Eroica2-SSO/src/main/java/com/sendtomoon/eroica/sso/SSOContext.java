package com.sendtomoon.eroica.sso;

import org.springframework.beans.FatalBeanException;

public class SSOContext {

	protected static SSO SSO;

	public static SSO get() {
		if (SSO == null) {
			throw new FatalBeanException("SSO is null.");
		}
		return SSO;
	}

}
