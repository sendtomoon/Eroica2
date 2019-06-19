package com.sendtomoon.eroica2.allergo.classloader;

import com.sendtomoon.eroica2.allergo.AllergoException;

public class AllergoURLException extends AllergoException {

	private static final long serialVersionUID = -6144475420830258875L;

	public AllergoURLException(String msg) {
		super(msg);
	}

	public AllergoURLException(String msg, Throwable th) {
		super(msg, th);
	}
}
