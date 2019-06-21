package com.sendtomoon.eroica2.allergo;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import com.sendtomoon.eroica.common.utils.EroicaConfigUtils;

/***
 * @deprecated
 *
 */
public abstract class BaseConfigLoader {

	protected Log logger = LogFactory.getLog(this.getClass());

	private String configKey;

	private String configGroup;

	private boolean configRequiredExists = true;

	protected String getConfigValue() {
		Assert.hasText(this.getConfigKey(), "configKey required.");
		Assert.hasText(this.getConfigGroup(), "configGroup required.");
		String configValue = Allergo.get(this.getConfigGroup(), this.getConfigKey());
		if (configValue == null || configValue.length() == 0) {
			if (this.isConfigRequiredExists()) {
				throw new AllergoException("Not found config<" + getFullName() + ">...");
			}
		}
		return configValue;
	}

	protected Properties getConfigProperties() {
		String c = getConfigValue();
		if (c == null || c.length() == 0) {
			return null;
		} else {
			return EroicaConfigUtils.toProperties(c);
		}

	}

	protected String getFullName() {
		return "/" + this.getConfigGroup() + "/" + this.getConfigKey();
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getConfigGroup() {
		return configGroup;
	}

	public void setConfigGroup(String configGroup) {
		this.configGroup = configGroup;
	}

	public boolean isConfigRequiredExists() {
		return configRequiredExists;
	}

	public void setConfigRequiredExists(boolean configRequiredExists) {
		this.configRequiredExists = configRequiredExists;
	}

}
