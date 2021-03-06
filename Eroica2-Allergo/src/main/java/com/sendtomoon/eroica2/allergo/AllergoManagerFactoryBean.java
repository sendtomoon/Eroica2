package com.sendtomoon.eroica2.allergo;

import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.sendtomoon.eroica.common.utils.URLUtils;
import com.sendtomoon.eroica2.allergo.impl.ClassPathAllergoManagerFactory;
import com.sendtomoon.eroica2.allergo.impl.LocalAllergoManagerFactory;
import com.sendtomoon.eroica2.allergo.impl.ZookeeperAllergoManagerFactory;

/**
 *  配置文件管理工厂类，4种配置文件管理模式，推荐使用zookeeper
 *
 */
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
		String name = _managerURL.getProtocol();
		if (name.equals("zookeeper")) {
			manager = new ZookeeperAllergoManagerFactory().create(_managerURL);
		} else if (name.equals("file")) {
			manager = new LocalAllergoManagerFactory().create(_managerURL);
		} else if (name.equals("classpath")) {
			manager = new ClassPathAllergoManagerFactory().create(_managerURL);
		} else if (name.equals("local")) {
			manager = new LocalAllergoManagerFactory().create(_managerURL);
		}
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
