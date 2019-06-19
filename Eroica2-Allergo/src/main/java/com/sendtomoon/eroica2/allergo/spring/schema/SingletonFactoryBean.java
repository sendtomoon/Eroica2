package com.sendtomoon.eroica2.allergo.spring.schema;

import org.springframework.beans.factory.FactoryBean;

public class SingletonFactoryBean implements FactoryBean<Object> {

	private Object bean;
	
	private Class<?> beanClass;
	
	@Override
	public Object getObject() throws Exception {
		return bean;
	}

	@Override
	public Class<?> getObjectType() {
		return beanClass;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

	public Class<?> getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}
	
	

}
