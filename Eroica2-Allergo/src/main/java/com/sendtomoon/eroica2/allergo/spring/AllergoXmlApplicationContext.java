package com.sendtomoon.eroica2.allergo.spring;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.ServletContextAwareProcessor;

public class AllergoXmlApplicationContext extends AbstractXmlApplicationContext implements WebApplicationContext {

	private Resource[] configResources;

	public static String ALLERGO_URL_PREFIX = "allergo:";

	private String[] basePackages;

	private ServletContext servletContext;

	@Override
	public Resource getResource(String location) {
		Assert.notNull(location, "Location must not be null");
		if (location.startsWith(ALLERGO_URL_PREFIX)) {
			String path = location.substring(ALLERGO_URL_PREFIX.length());
			return new AllergoResource(path);
		} else {
			return super.getResource(location);
		}
	}

	public AllergoXmlApplicationContext(String displayName) {
		this.setDisplayName(displayName);
	}

	public AllergoXmlApplicationContext(String displayName, Resource[] configResources) {
		this.setDisplayName(displayName);
		this.configResources = configResources;
	}

	@Override
	protected Resource[] getConfigResources() {
		return configResources;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public ServletContext getServletContext() {
		return this.servletContext;
	}

	protected void onRefresh() {
		String[] basePackages = this.getBasePackages();
		if (basePackages != null) {
			ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(
					(BeanDefinitionRegistry) this.getBeanFactory());
			scanner.scan(basePackages);
		}
	}

	@Override
	protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		ServletContext servletContext = this.getServletContext();
		if (servletContext != null) {
			beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(servletContext, null));
			beanFactory.ignoreDependencyInterface(ServletContextAware.class);
		}
	}

	public String[] getBasePackages() {
		return basePackages;
	}

	public void setBasePackages(String[] basePackages) {
		this.basePackages = basePackages;
	}

}
