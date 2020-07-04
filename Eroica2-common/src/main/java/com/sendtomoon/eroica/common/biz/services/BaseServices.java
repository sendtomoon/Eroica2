package com.sendtomoon.eroica.common.biz.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BaseServices implements Services {

	protected Log logger = LogFactory.getLog(this.getClass());

	protected Asserter asserter = Asserter.instance;

	protected final void _throwEx(String msg) {
		throw new ServicesException(msg);
	}

	protected final void _throwEx(Throwable th) {
		_throwEx(null, th);
	}

	protected final void _throwEx(String msg, Throwable th) {
		if (msg == null)
			msg = th.getMessage();
		throw new ServicesException(msg, th);
	}

}
