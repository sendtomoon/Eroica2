package com.sendtomoon.eroica2.allergo;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.sendtomoon.eroica2.common.utils.URLUtils;

public class AllergoManagerFactoryBean implements FactoryBean<AllergoManager>, InitializingBean, DisposableBean {

	private volatile AllergoManager manager;

	@Override
	public void afterPropertiesSet() throws Exception {
		String managerURL = System.getProperty(AllergoConstants.KEY_MANAGER);
		if (managerURL == null || (managerURL = managerURL.trim()).length() == 0) {
			throw new FatalBeanException("缺少zookeeper管理地址");
		}
		URLUtils _managerURL = null;
		try {
			_managerURL = URLUtils.valueOf(managerURL);
		} catch (Exception ex) {
			throw new AllergoException("Allergo manager URL <" + managerURL + ">  format error ," + ex.getMessage(),
					ex);
		}
		ExtensionLoader<AllergoManagerFactory> factory = ExtensionLoader
				.getExtensionLoader(AllergoManagerFactory.class);
		String name = _managerURL.getProtocol();
		manager = factory.getExtension(name).create(_managerURL);
	}

	@Override
	public AllergoManager getObject() throws Exception {
		return manager;
	}

	@Override
	public Class<?> getObjectType() {
		return AllergoManager.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void destroy() throws Exception {
		AllergoManager manager = this.manager;
		this.manager = null;
		if (manager != null) {
			manager.shutdown();
		}
	}

}
