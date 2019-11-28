package com.sendtomoon.eroica.common.app.dto;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ui.ModelMap;

public class ServiceResponse extends EroicaDTO {
	/**
	 * @deprecated
	 */
	public static final String CODE_SUCCESS = "success";
	/***
	 * @deprecated
	 */
	public static final String CODE_FAIL = "fail";
	public static final String SERVICE_RESPONSE_RESULT = "SERVICE_RESPONSE_RESULT";

	private Map model = null;
	/***
	 * @deprecated
	 */
	private String responseCode;
	/***
	 * @deprecated
	 */
	private String responseMsg;

	static final long serialVersionUID = 732373890158203233L;

	/**
	 * @deprecated
	 */
	public ServiceResponse() {
		this.responseCode = CODE_SUCCESS;
	}

	public ServiceResponse(Map model) {
		this.setResponseCode("0");
		this.setModel(model);
	}

	/**
	 * @param model
	 * @param responseCode
	 */
	public ServiceResponse(Map model, String responseCode) {
		this.setModel(model);
		this.setResponseCode(responseCode);
	}

	public ServiceResponse(String responseCode, ModelMap model) {
		this.setModel(model);
		this.setResponseCode(responseCode);
	}

	public ServiceResponse(String responseCode, String responseMsg) {
		this.setResponseCode(responseCode);
		this.setResponseMsg(responseMsg);
	}

	public ServiceResponse(String responseCode) {
		this.setResponseCode(responseCode);
	}

	public ServiceResponse(ModelMap model) {
		this.setModel(model);
	}

	/**
	 * @return Returns the model.
	 */
	public Map getModel() {
		return model;
	}

	/**
	 *
	 * @param model The model to set.
	 */
	public void setModel(Map model) {
		if (this.model == null) {
			this.model = model;
		} else {
			if (model != null) {
				this.model.putAll(model);
			}
		}
	}

	/**
	 * @return Returns the responseCode.
	 */
	public String getResponseCode() {
		String code = null;
		if (model != null) {
			code = (String) model.get("responseCode");
		}
		if (code == null) {
			code = responseCode;
		}
		return code;
	}

	/**
	 *
	 * @param responseCode The responseCode to set.
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
		if (this.model == null) {
			this.model = new HashMap();
		}
		this.model.put("responseCode", responseCode);
	}

	/**
	 * @deprecated
	 * @param success The sucess flag to set.
	 */
	public void setSuccess(boolean success) {
		if (success) {
			this.responseCode = CODE_SUCCESS;
		} else {
			this.responseCode = CODE_FAIL;
		}
	}

	/**
	 * @return Returns the responseMsg.
	 */
	public String getResponseMsg() {
		String msg = null;
		if (model != null) {
			msg = (String) model.get("responseMsg");
		}
		if (msg == null) {
			msg = responseMsg;
		}
		return msg;
	}

	/**
	 * @param responseMsg The responseMsg to set.
	 */
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
		if (this.model == null) {
			this.model = new HashMap();
		}
		this.model.put("responseMsg", responseMsg);
	}
}
