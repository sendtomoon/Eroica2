package com.sendtomoon.eroica.common.biz.sao;

import com.sendtomoon.eroica.common.exception.EroicaException;

public class SAOException extends EroicaException {

	private static final long serialVersionUID = 1L;

	public SAOException(String msg) {
		super(msg);
	}

	public SAOException(String msg, Throwable th) {
		super(msg, th);
	}

	public SAOException(Throwable th) {
		super(th.getMessage(), th);
	}
}
