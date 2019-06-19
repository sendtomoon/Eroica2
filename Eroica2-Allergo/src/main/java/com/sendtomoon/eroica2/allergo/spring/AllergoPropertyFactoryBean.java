package com.sendtomoon.eroica2.allergo.spring;

import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;

import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;
import com.sendtomoon.eroica2.allergo.utils.AllergoProperties;

public class AllergoPropertyFactoryBean implements FactoryBean<Properties> {

	private Properties properties;

	private String allergoURL;

	@Override
	public Properties getObject() throws Exception {
		if (properties == null) {
			AllergoURL _allergoURL = AllergoURL.valueOf(allergoURL);
			properties = new AllergoProperties(_allergoURL);
		}
		return properties;
	}

	@Override
	public Class<?> getObjectType() {
		return Properties.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public String getAllergoURL() {
		return allergoURL;
	}

	public void setAllergoURL(String allergoURL) {
		this.allergoURL = allergoURL;
	}

}
