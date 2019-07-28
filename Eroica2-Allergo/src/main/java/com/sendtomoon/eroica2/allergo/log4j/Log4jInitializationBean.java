package com.sendtomoon.eroica2.allergo.log4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import com.sendtomoon.eroica2.allergo.AllergoConstants;
import com.sendtomoon.eroica2.allergo.EroicaContext;
import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;


public class Log4jInitializationBean implements Log4jInitialization {
	
	private static final String KEY_SUFFIX=".log4j.properties";
	
	private EroicaContext eroicaContext;
	
	
	@Override
	public void onChanged(AllergoURL allergoURL, InputStream content) {
		if(content==null)return ;
		Properties properties=new Properties();
		try {
			properties.load(content);
		} catch (IOException e) {
		}
		PropertyConfigurator.configure(properties);
	}

	@Override
	public AllergoURL getAllergoURL() {
		return AllergoURL.valueOf("/"+AllergoConstants.GROUP_EOAPP+"/"+eroicaContext.getAppName()+KEY_SUFFIX);
	}

	@Override
	public boolean isListenEnable() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		AllergoURL allergoURL=this.getAllergoURL();
		allergoURL.setManager(eroicaContext.getDefaultManager());
		InputStream input=allergoURL.getInputStream(false);
		if(input!=null){
			Properties properties=new Properties();
			try {
				properties.load(input);
			} catch (IOException e) {
			}
			PropertyConfigurator.configure(properties);
		}
	}
	
	@Override
	public void setAllergoContext(EroicaContext eroicaContext) {
		this.eroicaContext=eroicaContext;
	}
}
