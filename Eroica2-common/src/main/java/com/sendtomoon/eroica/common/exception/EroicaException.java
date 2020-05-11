package com.sendtomoon.eroica.common.exception;

public class EroicaException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EroicaException(String msg) {
		
		super(msg);
	}

	public EroicaException(Throwable th) {
		super(th);
	}

	public EroicaException(String msg, Throwable th) {
		super(msg, th);
	}
}
