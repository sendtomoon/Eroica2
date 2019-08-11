package com.sendtomoon.eroica2.allergo.spring;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Vector;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.Ordered;

import com.sendtomoon.eroica2.allergo.EroicaContext;
import com.sendtomoon.eroica2.allergo.AllergoException;
import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;
import com.sendtomoon.eroica2.allergo.spring.annotation.AllergoResourceConfig;

public class AllergoResourceListenerRegisterBean extends ApplicationObjectSupport
		implements BeanPostProcessor, DisposableBean, Ordered {

	private EroicaContext eroicaContext;

	private volatile Vector<AllergoURL> allergoURLs;

	@Override
	public void destroy() throws Exception {
		if (allergoURLs != null && allergoURLs.size() > 0) {
			for (AllergoURL allergoURL : allergoURLs) {
				getAllergoContext().unregisterListener(allergoURL);
			}
		}
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof AllergoResourceListener) {
			AllergoResourceListener listener = (AllergoResourceListener) bean;
			AllergoURL allergoURL = listener.getAllergoURL();
			if (getAllergoContext().registerListener(listener)) {
				if (allergoURLs == null) {
					allergoURLs = new Vector<AllergoURL>(8);
				}
				allergoURLs.add(allergoURL);
			}
		} else if (bean instanceof AllergoResourceHandler) {
			AllergoResourceHandler hanlder = (AllergoResourceHandler) bean;
			Method method = null;
			try {
				method = bean.getClass().getMethod("handleAllergoResource", AllergoURL.class, InputStream.class);
			} catch (Exception e) {
				throw new FatalBeanException("Bean<" + beanName + "> defined error,cuase:" + e.getMessage(), e);
			}
			AllergoResourceConfig annotation = method.getAnnotation(AllergoResourceConfig.class);
			if (annotation == null) {
				throw new FatalBeanException("Bean<" + beanName + "> defined error:method<" + method.getName()
						+ "> not found annotation:" + AllergoResourceConfig.class.getName());
			}
			if (logger.isInfoEnabled()) {
				logger.info("Bean<" + beanName + ">Handle allergoConfig:" + annotation);
			}
			try {
				AllergoURL allergoURL = AllergoURL.valueOf(annotation.allergoURL());
				hanlder.handleAllergoResource(allergoURL, allergoURL.getInputStream());
				if (annotation.listenEnable()) {
					if (getAllergoContext().registerListener(new DefAllergoResourceListener(hanlder, allergoURL))) {
						if (allergoURLs == null) {
							allergoURLs = new Vector<AllergoURL>(8);
						}
						allergoURLs.add(allergoURL);
					}
				}
			} catch (Exception e) {
				throw new FatalBeanException(
						"Bean<" + beanName + "> allergo resource handle error,cuase:" + e.getMessage(), e);
			}
		}
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	public EroicaContext getAllergoContext() {
		if (eroicaContext == null) {
			eroicaContext = com.sendtomoon.eroica2.allergo.Allergo.getAllergoContext();
		}
		return eroicaContext;
	}

	public void setAllergoContext(EroicaContext eroicaContext) {
		this.eroicaContext = eroicaContext;
	}

}

class DefAllergoResourceListener implements AllergoResourceListener {

	private AllergoResourceHandler handler;

	private AllergoURL allergoURL;

	public DefAllergoResourceListener(AllergoResourceHandler handler, AllergoURL allergoURL) {
		this.handler = handler;
		this.allergoURL = allergoURL;
	}

	@Override
	public void onChanged(AllergoURL allergoURL, InputStream content) {
		try {
			handler.handleAllergoResource(allergoURL, content);
		} catch (Exception e) {
			throw new AllergoException(
					"Handle allergo resource<" + allergoURL.toString() + "> error,cause:" + e.getMessage(), e);
		}
	}

	@Override
	public AllergoURL getAllergoURL() {
		return allergoURL;
	}

	@Override
	public boolean isListenEnable() {
		return true;
	}

}
