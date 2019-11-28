package com.sendtomoon.eroica.common.web;

import com.sendtomoon.eroica.common.exception.EroicaException;

public class WebException extends EroicaException {

	private static final long serialVersionUID = 1L;

	public WebException(String msg) {
		super(msg);
	}

	public WebException(Throwable th) {
		super(th.getMessage(), th);
	}

	public WebException(String msg, Throwable th) {
		super(msg, th);
	}

}
