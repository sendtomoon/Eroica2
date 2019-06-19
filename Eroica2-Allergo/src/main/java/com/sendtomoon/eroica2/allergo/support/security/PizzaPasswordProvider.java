package com.sendtomoon.eroica2.allergo.support.security;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sendtomoon.eroica2.allergo.AllergoConstants;
import com.sendtomoon.eroica2.allergo.AllergoException;
import com.sendtomoon.eroica2.common.security.PasswordCodeUtils;
import com.sendtomoon.eroica2.common.security.PasswordContext;
import com.sendtomoon.eroica2.common.security.PasswordProvider;

public class PizzaPasswordProvider implements PasswordProvider {

	protected Log logger = LogFactory.getLog(PizzaPasswordProvider.class);

	public static final String KEY_NAMESPACE = "allergo.password.provider.namespace";

	private static final String PIZZA_GROUP = "passwords";

	public PizzaPasswordProvider() {
		refreshConfig(System.getProperties());
	}

	@Override
	public void refreshConfig(Properties properties) {
		String namespace = properties.getProperty(KEY_NAMESPACE);
		if (namespace == null || namespace.length() == 0) {
			namespace = properties.getProperty(AllergoConstants.KEY_DOMAIN_ID);
		}
		if (namespace != null && namespace.length() > 0) {
			System.setProperty(KEY_NAMESPACE, namespace);
			if (logger.isDebugEnabled()) {
				logger.debug(KEY_NAMESPACE + "=" + namespace);
			}
		}
	}

	@Override
	public String getPassword(PasswordContext context) {
		String passwordKey = context.getKey();
		try {
			String password = null;
			if (passwordKey == null || (passwordKey = passwordKey.trim()).length() == 0) {
				throw new NullPointerException("Password key  is null.");
			}
			String namespace = System.getProperty(KEY_NAMESPACE);
			if (namespace == null || namespace.length() == 0) {
				throw new NullPointerException("Not initialized,namespace  is null.");
			}
			String key = namespace + "." + passwordKey;
			if (logger.isInfoEnabled()) {
				logger.info("Read password by key:" + key);
			}
			String value = com.sendtomoon.eroica2.allergo.Allergo.getManager().get(PIZZA_GROUP, key);
			if (value != null && (value = value.trim()).length() > 0) {
				password = PasswordCodeUtils.decode(value);
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("Read password failure by key:" + key);
				}
			}
			if (password == null) {
				if (context.getDefaultPassword() != null) {
					return context.getDefaultPassword();
				} else if (context.isRequired()) {
					throw new NullPointerException("read failure,password is null.");
				}
			}
			return password;
		} catch (Throwable th) {
			throw new AllergoException(
					"ReadPassword error for key:" + passwordKey + " by allergoManager,cause:" + th.getMessage(), th);
		}
	}

}
