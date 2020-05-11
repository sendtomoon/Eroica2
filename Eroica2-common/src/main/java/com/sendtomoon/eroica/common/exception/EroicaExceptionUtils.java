package com.sendtomoon.eroica.common.exception;

public class EroicaExceptionUtils {

	public static ResponseCodeException toResponseCodeException(Throwable ex) {
		return ResponseCodeException.toResponseCodeException(ex);
	}

}
