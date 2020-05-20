package com.sendtomoon.eroica.common.beans;

import com.sendtomoon.eroica.common.exception.EroicaException;


public class BeanTransformException extends EroicaException {

	private static final long serialVersionUID = 1L;

	public BeanTransformException(String msg) {
		super(msg);
	}
	
	public BeanTransformException(String msg,Throwable th) {
		super(msg,th);
	}
	
	
}
