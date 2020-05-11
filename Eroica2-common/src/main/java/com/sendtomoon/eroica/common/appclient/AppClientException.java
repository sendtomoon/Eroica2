package com.sendtomoon.eroica.common.appclient;

import com.sendtomoon.eroica.common.exception.EroicaException;

public class AppClientException extends EroicaException {

	private static final long serialVersionUID = 1L;

	private String errorCode;

	public AppClientException(String msg) {
		super(msg);
	}

	public AppClientException(String errorCode, String msg) {

		super("[" + errorCode + "]" + msg);
		this.errorCode = errorCode;
	}

	public AppClientException(Throwable th) {
		super(th.getMessage(), th);
	}

	public AppClientException(String msg, Throwable th) {
		super(msg, th);
	}

	public AppClientException(String errorCode, String msg, Throwable th) {
		super("[" + errorCode + "]" + msg, th);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
