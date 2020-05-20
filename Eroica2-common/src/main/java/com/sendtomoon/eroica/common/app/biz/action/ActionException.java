package com.sendtomoon.eroica.common.app.biz.action;

import com.sendtomoon.eroica.common.exception.EroicaException;

public class ActionException extends EroicaException {

	private static final long serialVersionUID = 1L;

	public ActionException(String msg) {
		super(msg);
	}

	public ActionException(Throwable th) {
		super(th.getMessage(), th);
	}

	public ActionException(String msg, Throwable th) {
		super(msg, th);
	}

}
