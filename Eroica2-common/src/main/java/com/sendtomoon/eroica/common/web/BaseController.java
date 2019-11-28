package com.sendtomoon.eroica.common.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BaseController implements Controller {

	protected Log logger = LogFactory.getLog(this.getClass());

	protected final void _throwEx(String msg) {
		throw new WebException(msg);
	}

	protected final void _throwEx(Throwable th) {
		_throwEx(null, th);
	}

	protected final void _throwEx(String msg, Throwable th) {
		if (msg == null)
			msg = th.getMessage();
		throw new WebException(msg, th);
	}

	public Log getLogger() {
		return logger;
	}

	public void setLogger(Log logger) {
		this.logger = logger;
	}

}
