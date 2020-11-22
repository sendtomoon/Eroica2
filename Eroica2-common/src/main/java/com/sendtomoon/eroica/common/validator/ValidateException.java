package com.sendtomoon.eroica.common.validator;

import com.sendtomoon.eroica.common.exception.EroicaException;

public class ValidateException extends EroicaException {

	private static final long serialVersionUID = 1L;

	public ValidateException(String msg) {
		super(msg);
	}

	public ValidateException(String msg, Throwable th) {
		super(msg, th);
	}

}
