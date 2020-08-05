package com.sendtomoon.eroica.common.biz.services;

import com.sendtomoon.eroica.common.exception.EroicaException;

public class ServicesException extends EroicaException {

	private static final long serialVersionUID = 1L;

	public ServicesException(String msg) {
		super(msg);
	}

	public ServicesException(Throwable th) {
		super(th.getMessage(), th);
	}

	public ServicesException(String msg, Throwable th) {
		super(msg, th);
	}

}
