package com.sendtomoon.eroica.common.biz.sao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BaseSAO implements SAO {

	protected Log logger = LogFactory.getLog(this.getClass());

	protected final void _throwEx(String msg) {
		throw new SAOException(msg);
	}

	protected final void _throwEx(Throwable th) {
		_throwEx(null, th);
	}

	protected final void _throwEx(String msg, Throwable th) {
		if (th instanceof RuntimeException) {
			throw (RuntimeException) th;
		} else {
			if (th.getMessage() == null && th.getCause() != null) {
				th = th.getCause();
			}
			if (msg == null)
				msg = th.getMessage();
			throw new SAOException(msg, th);
		}
	}

	public Log getLogger() {
		return logger;
	}

	public void setLogger(Log logger) {
		this.logger = logger;
	}

}
