package com.sendtomoon.eroica.common.web;

import com.sendtomoon.eroica.common.constants.ResponseConstants;

public class CommonVO {

	private String responseCode = ResponseConstants.DEF_SUCCESS_CODE;
	private String responseMsg;
	
	public CommonVO() {
		
	}
	
	public CommonVO(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public CommonVO(String responseCode, String responseMsg) {
		this.responseCode = responseCode;
		this.responseMsg = responseMsg;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
}
