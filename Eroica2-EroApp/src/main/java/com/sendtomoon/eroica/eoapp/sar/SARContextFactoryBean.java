package com.sendtomoon.eroica.eoapp.sar;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ServletContextAware;

import com.sendtomoon.eroica.adagio.Pola;
import com.sendtomoon.eroica.eoapp.sar.lib.SARBootClassResolver;
import com.sendtomoon.eroica.eoapp.sar.lib.SARClassLoaderFactory;
import com.sendtomoon.eroica.eoapp.sar.lib.SARClassLoaderFactoryBean;
import com.sendtomoon.eroica2.allergo.AllergoConstants;
import com.sendtomoon.eroica2.allergo.classloader.AllergoClassLoader;
import com.sendtomoon.eroica2.allergo.utils.AllergoProperties;

public class SARContextFactoryBean implements SARContextFactory, ApplicationContextAware, ServletContextAware {

	private static final String KEY_PREFIX = ".properties";

	private Properties eoappProperties;

	private Pola pola;

	private SARBootClassResolver bootClassResolver = new SARBootClassResolver();

	private ConfigurableApplicationContext eoappSpringContext;

	private Log logger = LogFactory.getLog(this.getClass());

	@Override
	public SARContext create(String sarName) {
		try {
			String allergoURL = AllergoConstants.GROUP_SAR + "/" + sarName + KEY_PREFIX;
			// 组件配置文件必须存在
			boolean requriedExists = true;
			//
			AllergoProperties properties = new AllergoProperties(allergoURL, requriedExists);
			if (logger.isInfoEnabled()) {
				logger.info("SAR:" + sarName + " allergo properties=" + properties);
			}
			properties.put(SARAttrs.KEY_SAR_NAME, sarName);
			SARAttrs attrs = new SARAttrs(sarName, properties);
			SARClassLoaderFactory classloaderFactory = new SARClassLoaderFactoryBean(attrs, this.getPola());
			//
			ConfigurableApplicationContext eoappSpringContext = this.eoappSpringContext;
			if (eoappSpringContext == null) {
				throw new FatalBeanException("eoappSpringContext required.");
			}
			AllergoClassLoader classLoader = classloaderFactory.createClassLoader(eoappSpringContext.getClassLoader(),
					this.servletContext);
//			Class<?> bootClass = bootClassResolver.resolve(attrs, classLoader);
			SARContextAttrs contextAttrs = createSARContextAttrs(sarName, properties);
			checkBasePackage(contextAttrs, classLoader);
			return new SARContextBean(contextAttrs, classLoader, eoappSpringContext, this.servletContext);
		} catch (Exception e) {
			logger.error(e, e);
			return null;
		}
	}

	/**
	 * 读取sar包配置文件，并解析，传入到对应的处理类
	 * @param sarName
	 * @param properties
	 * @return
	 */
	protected SARContextAttrs createSARContextAttrs(String sarName, AllergoProperties properties) {
		Properties mergedProperties = new Properties();
		Set<String> plugins = new HashSet<String>();

		if(properties.getProperty("sar.web.enabled").equals("true")) {
			plugins.add("web");
		}
		if(org.apache.commons.lang3.StringUtils.isNoneBlank(properties.getProperty("sar.deafult"))) {
			mergedProperties.setProperty(SARContextAttrs.KEY_DEF_CHARSET, properties.getProperty("sar.deafult"));
		}
		mergedProperties.setProperty(SARContextAttrs.KEY_ORDER, properties.getProperty("sar.web.order"));
		Properties eoappProperties = this.getEoappProperties();
		if (properties != null && properties.size() > 0) {
			mergedProperties.putAll(properties);
		}
		if (logger.isDebugEnabled()) {
			logger.info("SAR<" + sarName + "> merged properties=" + mergedProperties);
		}
		SARContextAttrs contextAttrs = new SARContextAttrs(sarName, mergedProperties, plugins,
				eoappProperties);
		if (logger.isInfoEnabled()) {
			logger.info("SAR<" + sarName + "> properties=" + contextAttrs.toString());
			logger.info("SAR<" + sarName + "> basePackages=" + Arrays.toString(contextAttrs.getBasePackages()));
			logger.info("SAR<" + sarName + "> webPatterns=" + Arrays.toString(contextAttrs.getWebPatterns()));
		}
		return contextAttrs;
	}

	protected void forAnnotation(Annotation annotation, String pluginName, Properties mergedProperties) {
		Class<?> annotationClass = annotation.annotationType();
		Method[] methods = annotationClass.getMethods();
		for (Method method : methods) {
			if (method.getDeclaringClass() != Object.class && method.getReturnType() != void.class
					&& method.getParameterTypes().length == 0 && Modifier.isPublic(method.getModifiers())
					&& !Modifier.isStatic(method.getModifiers())) {
				try {
					String methodName = method.getName();
					if ("toString".equals(methodName)) {
						continue;
					}
					StringBuilder property = new StringBuilder();
					property.append("sar.").append(pluginName);
					if (!"value".equals(methodName)) {
						property.append('.');
						for (int i = 0; i < methodName.length(); i++) {
							char ch = methodName.charAt(i);
							if (i == 0) {
								property.append(Character.toLowerCase(ch));
							} else if (Character.isUpperCase(ch)) {
								property.append('.');
								property.append(Character.toLowerCase(ch));
							} else if (ch == '_') {
								property.append('.');
							} else {
								property.append(ch);
							}
						}
					}
					Object value = method.invoke(annotation, new Object[0]);
					if (value != null && !value.equals(method.getDefaultValue())) {
						if (value.getClass().isArray()) {
							Object[] array = (Object[]) value;
							if (array.length == 0) {
								continue;
							}
							value = StringUtils.arrayToDelimitedString(array, ",");
						}
						mergedProperties.setProperty(property.toString(), value.toString());
					}
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	protected void checkBasePackage(SARContextAttrs contextAttrs, ClassLoader classLoader) {
		String[] bps = contextAttrs.getBasePackages();
		if (bps == null) {
			return;
		}
		for (String bp : bps) {
			String path = bp.replaceAll("\\.", "/");
			// File dir=null;
			URL url = classLoader.getResource(path);
			if (url != null) {
				if (logger.isInfoEnabled()) {
					logger.info("BasePackage<" + bp + "> resourceURL=" + url);
				}
				/*
				 * try { dir=new File(url.toURI()); } catch (Exception e) { throw new
				 * SARException("BasePackage<"+url+"> error,cause:"+e.getMessage(),e); }
				 */
			}
			/*
			 * if(dir==null || !dir.exists()){ throw new
			 * SARException("BasePackage<"+bp+"> not exists."); } if(!dir.isDirectory()){
			 * throw new SARException("BasePackage<"+bp+"> not be directory,directory="+dir.
			 * getAbsolutePath()); }
			 */

		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.eoappSpringContext = (ConfigurableApplicationContext) applicationContext;
	}

	private ServletContext servletContext;

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public Properties getEoappProperties() {
		return eoappProperties;
	}

	public void setEoappProperties(Properties eoappProperties) {
		this.eoappProperties = eoappProperties;
	}

	public Pola getPola() {
		return pola;
	}

	public void setPola(Pola pola) {
		this.pola = pola;
	}

	public SARBootClassResolver getBootClassResolver() {
		return bootClassResolver;
	}

	public void setBootClassResolver(SARBootClassResolver bootClassResolver) {
		this.bootClassResolver = bootClassResolver;
	}

}
