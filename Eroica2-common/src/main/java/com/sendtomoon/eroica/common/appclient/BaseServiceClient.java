package com.sendtomoon.eroica.common.appclient;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.sendtomoon.eroica.common.Eroica;
import com.sendtomoon.eroica.common.app.dto.ServiceRequest;
import com.sendtomoon.eroica.common.beans.map.BeanMapUtils;

public abstract class BaseServiceClient implements IServiceClient, InitializingBean, ApplicationContextAware {

	protected Log logger = LogFactory.getLog(this.getClass());

	private Class<?> handleClazz;

	protected BeanMapUtils beanMapUtils = new BeanMapUtils();

	protected ApplicationContext applicationContext;

	public BaseServiceClient() {
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public <T> T invoke(Object paramsModel, Class<T> resultDTOClass, String resultKey) {
		ServiceResults results = invoke(paramsModel);
		if (results != null) {
			return results.toDTO(resultKey, resultDTOClass);
		} else {
			return null;
		}
	}

	public ServiceResults invoke() {
		return invoke((Object) null);
	}

	public <T> T invoke(Class<T> resultBindClass, String resultKey) {
		return invoke((Object) null, resultBindClass, resultKey);
	}

	public <T> T invoke(Class<T> resultBindClass) {
		return invoke((Object) null, resultBindClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T invoke(Object paramsModel, Class<T> resultDTOClass) {
		ServiceResults results = invoke(paramsModel);
		if (results == null)
			return null;
		Map<?, ?> temp = results;
		T t = (T) beanMapUtils._toBean(resultDTOClass, temp);
		return t;
	}

	protected Map processParams(Object paramsModel) {
		Map params = null;
		if (paramsModel == null) {
			params = new HashMap<Object, Object>(4);
		} else {
			if (paramsModel instanceof Map) {
				if (paramsModel instanceof ServiceParams) {
					paramsModel = ((ServiceParams) paramsModel).getMap();
				}
			} else {
				if (paramsModel instanceof ServiceRequest) {
					paramsModel = ((ServiceRequest) paramsModel).getParameters();
				}
			}
			params = (Map) beanMapUtils._toMap(paramsModel);
		}
		params.put(Eroica.PARAM_NAME_DATA_TYPE, Eroica.DATA_TYPE_MAP);
		return params;
	}

	@SuppressWarnings("unchecked")
	protected ServiceResults processResult(Map model) {
		ServiceResults result = null;
		if (model == null) {
			model = new HashMap(0);
		}
		result = new ServiceResults(model, this.beanMapUtils);
		return result;
	}

	protected abstract String getServiceName();

	public Class<?> getHandleClazz() {
		return handleClazz;
	}

	public void setHandleClazz(Class<?> handleClazz) {
		this.handleClazz = handleClazz;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

}
