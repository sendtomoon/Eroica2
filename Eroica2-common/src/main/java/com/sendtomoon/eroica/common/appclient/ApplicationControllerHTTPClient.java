package com.sendtomoon.eroica.common.appclient;

import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.sendtomoon.eroica.common.app.biz.ac.ApplicationController;

public class ApplicationControllerHTTPClient extends HttpInvokerProxyFactoryBean {

	public ApplicationControllerHTTPClient() {
		setServiceInterface(ApplicationController.class);
	}

}
