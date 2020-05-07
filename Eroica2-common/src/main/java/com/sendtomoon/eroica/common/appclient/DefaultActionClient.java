package com.sendtomoon.eroica.common.appclient;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.FatalBeanException;

import com.alibaba.fastjson.JSONObject;
import com.sendtomoon.eroica.common.app.biz.ac.ApplicationController;
import com.sendtomoon.eroica.common.app.biz.ac.ApplicationControllerException;
import com.sendtomoon.eroica.common.app.dto.ServiceRequest;
import com.sendtomoon.eroica.common.app.dto.ServiceResponse;

public class DefaultActionClient extends BaseServiceClient {

	private String name;

	private String group;

	private ApplicationController eroicaAcBean;

	private String eroicaAc;

	public DefaultActionClient() {
	}

	public DefaultActionClient(String name, String eroicaAc) {
		this.name = name;
		this.eroicaAc = eroicaAc;
	}

	@Override
	protected String getServiceName() {
		return name;
	}

	public ServiceResults invoke(Object paramsModel) {
		Map<?, ?> params = processParams(paramsModel);
		ServiceRequest request = new ServiceRequest(this.name, params);
		if (group != null) {
			request.setGroup(group);
		}
		ServiceResponse resp = performRequest(request);
		Map<?, ?> model = null;
		if (resp != null) {
			model = resp.getModel();
		}
		return processResult(model);
	}

	private ApplicationController getEroicaAcBean() {
		if (this.eroicaAcBean == null) {
			if (!this.applicationContext.containsBean(eroicaAc)) {
				throw new FatalBeanException("Not define bean  for eroicaAc <" + eroicaAc + "> ");
			}
			Object bean = this.applicationContext.getBean(eroicaAc);
			if (!(bean instanceof ApplicationController)) {
				throw new FatalBeanException("Bean of eroicaAc<" + eroicaAc + "> not instanceof <"
						+ ApplicationController.class.getName() + ">");
			}
			this.eroicaAcBean = (ApplicationController) bean;
		}
		return this.eroicaAcBean;
	}

	private ServiceResponse performRequest(ServiceRequest request) {
		long t1 = System.nanoTime();
		if (logger.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append("Invoking<" + name + "> by <" + eroicaAc + ">");
			sb.append(",params=" + JSONObject.toJSONString(request.getParameters()));
			sb.append(".");
			logger.info(sb);
		}
		ServiceResponse resp = null;
		try {
			resp = this.getEroicaAcBean().handleRequest(request);
			ApplicationController ac = this.getEroicaAcBean();
			resp = ac.handleRequest(request);
			if (resp != null) {
				processResponse(resp);
			}
			if (logger.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();
				sb.append("#" + (System.nanoTime() - t1) / 1000 / 1000.0 + "ms#Invoked<" + name + "> by <" + eroicaAc
						+ ">");
				if (logger.isDebugEnabled()) {
					sb.append(",result=");
					Object m = (resp == null ? null : resp.getModel());
					sb.append(m == null ? "null" : JSONObject.toJSONString(m));
				}
				sb.append(".");
				logger.info(sb);
			}
		} catch (Throwable ex) {
			if (ex instanceof ApplicationControllerException && ex.getCause() != null) {
				ex = ex.getCause();
			}
			StringBuilder sb = new StringBuilder();
			String param = JSONObject.toJSONString(request.getParameters());
			Class<?> handleClazz = this.getHandleClazz();
			sb.append("#" + (System.nanoTime() - t1) / 1000 / 1000.0 + "ms#Invoked<" + name + "> by <" + eroicaAc + "><"
					+ (handleClazz == null ? "unknown" : handleClazz.getName()) + ">");
			sb.append(",params=" + param);
			sb.append(".");

			if (logger.isErrorEnabled()) {
				logger.error(sb);
				logger.error(ex.getMessage(), ex);
			}
			throw new AppClientException(sb.toString(), ex);
		}
		return resp;
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private void processResponse(ServiceResponse resp) {
		Map model = resp.getModel();
		if (model == null) {
			model = new HashMap(2);
			model.put("responseCode", resp.getResponseCode());
			if (resp.getResponseMsg() != null) {
				model.put("responseMsg", resp.getResponseMsg());
			}
			resp.setModel(model);
		} else {
			String code = resp.getResponseCode();
			if (!model.containsKey("responseCode")) {
				model.put("responseCode", resp.getResponseCode());
			}
			if (resp.getResponseMsg() != null && !model.containsKey("responseMsg")) {
				model.put("responseMsg", resp.getResponseMsg());
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEroicaAc() {
		return eroicaAc;
	}

	public void setEroicaAc(String eroicaAc) {
		this.eroicaAc = eroicaAc;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		if (this.eroicaAc == null) {
			throw new FatalBeanException("eroicaAc is null");
		}
		if (this.name == null) {
			throw new FatalBeanException("name is null");
		}
		getEroicaAcBean();
	}

	public void setEroicaAcBean(ApplicationController eroicaAcBean) {
		this.eroicaAcBean = eroicaAcBean;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

}
