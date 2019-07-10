package com.sendtomoon.eroica2.allergo.spring.schema;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.sendtomoon.eroica2.allergo.Allergo;
import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;
import com.sendtomoon.eroica.common.utils.URLUtils;

public class ShareBeanDefinitionParser implements BeanDefinitionParser {

	protected Log logger = LogFactory.getLog(this.getClass());

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String beanName = element.getAttribute("name");
		String shareBeanName = element.getAttribute("shareName");
		String configURLString = element.getAttribute("configURL");
		String scope = element.getAttribute("scope");
		String type = element.getAttribute("type");
		if (logger.isInfoEnabled()) {
			logger.info(" configURL=" + configURLString + ", and scope=" + scope + ",and type=" + type
					+ ",and shareName=" + shareBeanName + ",and name=" + beanName + ".");
		}
		if (configURLString == null || (configURLString = configURLString.trim()).length() == 0) {
			throw new BeanDefinitionValidationException("configURL requried.");
		}
		//
		URLUtils configURL = null;
		AllergoURL allergoURL = null;
		try {
			configURL = URLUtils.valueOf(configURLString);
			allergoURL = AllergoURL.valueOf(configURL.toIdentityString());
		} catch (Exception ex) {
			throw new BeanDefinitionValidationException(
					"configURL:" + configURL + " format error,cause:" + ex.getMessage());
		}
		if (type == null || (type = type.trim()).length() == 0) {
			type = allergoURL.getAllergoGroup();
		}
		if (shareBeanName == null || (shareBeanName = shareBeanName.trim()).length() == 0) {
			shareBeanName = allergoURL.toURI();
		}
		// ----------------------
		BeanDefinitionRegistry selfRegistry = parserContext.getRegistry();
		try {
			if ("global".equalsIgnoreCase(scope)) {
				ConfigurableListableBeanFactory globalBeanFactory = (ConfigurableListableBeanFactory) Allergo
						.getSpringContext().getBeanFactory();
				BeanDefinitionRegistry globalRegistry = (BeanDefinitionRegistry) globalBeanFactory;
				if (!globalRegistry.containsBeanDefinition(shareBeanName)) {
					globalRegistry.registerBeanDefinition(shareBeanName,
							loadBeanDefinition(type, allergoURL, configURL));
				} else {
					if (logger.isInfoEnabled()) {
						logger.info("GolbalShareBean:" + shareBeanName + " be defined.");
					}
				}
				Object bean = globalBeanFactory.getBean(shareBeanName);
				if (beanName != null && (beanName = beanName.trim()).length() > 0) {
					if (selfRegistry.containsBeanDefinition(beanName)) {
						throw new BeanDefinitionValidationException("Duplicate spring bean id " + beanName);
					}
					selfRegistry.registerBeanDefinition(beanName, createGlobalProxyBeanDefinition(bean));
				}
			} else if ("self".equalsIgnoreCase(scope)) {
				if (beanName == null || beanName.length() == 0) {
					beanName = shareBeanName;
				}
				if (selfRegistry.containsBeanDefinition(beanName)) {
					throw new BeanDefinitionValidationException("Duplicate spring bean id " + beanName);
				}
				selfRegistry.registerBeanDefinition(beanName, loadBeanDefinition(type, allergoURL, configURL));
			} else {
				BeanDefinitionRegistry parentRegistry = getParentRegistry(selfRegistry);
				if (parentRegistry != null) {
					if (!parentRegistry.containsBeanDefinition(shareBeanName)) {
						parentRegistry.registerBeanDefinition(shareBeanName,
								loadBeanDefinition(type, allergoURL, configURL));
					} else {
						if (logger.isInfoEnabled()) {
							logger.info("ParentShareBean:" + shareBeanName + " be defined.");
						}
					}
					Object bean = ((BeanFactory) parentRegistry).getBean(shareBeanName);
					if (beanName != null && (beanName = beanName.trim()).length() > 0) {
						if (selfRegistry.containsBeanDefinition(beanName)) {
							throw new BeanDefinitionValidationException("Duplicate spring bean id " + beanName);
						}
						selfRegistry.registerBeanDefinition(beanName, createGlobalProxyBeanDefinition(bean));
					}
				} else {
					if (beanName == null || beanName.length() == 0) {
						beanName = shareBeanName;
					}
					if (selfRegistry.containsBeanDefinition(beanName)) {
						throw new BeanDefinitionValidationException("Duplicate spring bean id " + beanName);
					}
					selfRegistry.registerBeanDefinition(beanName, loadBeanDefinition(type, allergoURL, configURL));
				}
			}
		} catch (Throwable ex) {
			throw new BeanDefinitionValidationException(
					"AllergoBean register  error by config:" + configURLString + ",cause:" + ex.getMessage(), ex);
		}
		// ---------------------------------
		return null;
	}

	protected BeanDefinitionRegistry getParentRegistry(BeanDefinitionRegistry selfRegistry) {
		if (selfRegistry instanceof ConfigurableBeanFactory) {
			BeanFactory parentBeanFactory = ((ConfigurableBeanFactory) selfRegistry).getParentBeanFactory();
			if (parentBeanFactory != null && parentBeanFactory instanceof BeanDefinitionRegistry) {
				return (BeanDefinitionRegistry) parentBeanFactory;
			}
		}
		return null;
	}

	protected RootBeanDefinition createGlobalProxyBeanDefinition(Object bean) {
		RootBeanDefinition proxy = new RootBeanDefinition();
		proxy.setLazyInit(false);
		proxy.setBeanClass(SingletonFactoryBean.class);
		MutablePropertyValues pvs = proxy.getPropertyValues();
		pvs.addPropertyValue("bean", bean);
		pvs.addPropertyValue("beanClass", bean.getClass());
		return proxy;
	}

	protected BeanDefinition loadBeanDefinition(String type, AllergoURL allergoURL, URLUtils configURL)
			throws FatalBeanException {
		// ------------------------------------------------------------------------
		BeanDefinition beanDefinition = null;
		try {
			ExtensionLoader<AllergoShareBean> loader = ExtensionLoader.getExtensionLoader(AllergoShareBean.class);
			AllergoShareBean factory = loader.getExtension(type);
			beanDefinition = factory.create(allergoURL, configURL);
			return beanDefinition;
			// -----------------------------------------------------------------------
		} catch (Throwable ex) {
			throw new BeanDefinitionValidationException("Create bean definition error ,cause:" + ex.getMessage(), ex);
		}
	}

}
