package com.sendtomoon.eroica.common.app.dto;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ui.ModelMap;

public class ServiceRequest extends EroicaDTO {

	private static final String REQUESTED_SERVICE_ID = "REQUESTED_SERVICE_ID";

	private static final String REQUESTED_SERVICE_GROUP = "REQUESTED_SERVICE_GROUP";

	public static final String SESSION_DTO = "SESSION_DTO";

	private Map parameters;

	private static final long serialVersionUID = 4490490957886951839L;

	public ServiceRequest(String actionName, Map parameters) {
		if (parameters != null) {
			this.parameters = parameters;
		} else {
			this.parameters = new HashMap();
		}
		this.setRequestedServiceID(actionName);
	}

	public ServiceRequest(String actionName, ModelMap model) {
		this.parameters = new HashMap();
		this.setRequestedServiceID(actionName);
		if (model != null) {
			parameters.putAll(model);
		}
	}

	/**
	 * @param parameterKey
	 */
	public Object getParameter(String parameterKey) {
		return parameters.get(parameterKey);
	}

	/**
	 * @param parameterKey
	 * @param parameter
	 */
	public void setParameter(String parameterKey, Object parameter) {
		parameters.put(parameterKey, parameter);
	}

	public void setActionName(String actionName) {
		this.setRequestedServiceID(actionName);
	}

	/**
	 * @param serviceID
	 */
	public void setRequestedServiceID(String serviceID) {
		parameters.put(REQUESTED_SERVICE_ID, serviceID);
	}

	/**
	 * 
	 * @return the service request ID
	 */
	public String getRequestedServiceID() {
		return (String) parameters.get(REQUESTED_SERVICE_ID);
	}

	public String getActionName() {
		return getRequestedServiceID();
	}

	public String getGroup() {
		return (String) parameters.get(REQUESTED_SERVICE_GROUP);
	}

	public void setGroup(String group) {
		parameters.put(REQUESTED_SERVICE_GROUP, group);
	}

	/**
	 * @return Returns the parameters.
	 */
	public Map getParameters() {
		return parameters;
	}

	/**
	 * @param parameters The parameters to set.
	 */
	public void setParameters(Map parameters) {
		if (this.parameters == null) {
			this.parameters = parameters;
		} else {
			this.parameters.putAll(parameters);
		}
	}

}