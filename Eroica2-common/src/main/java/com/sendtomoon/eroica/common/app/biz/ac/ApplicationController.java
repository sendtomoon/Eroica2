/*
 * Created on 2004-7-2
 *
 * To change the template for this generated file go to
 *
 */
package com.sendtomoon.eroica.common.app.biz.ac;

import java.rmi.RemoteException;

import com.sendtomoon.eroica.common.app.dto.ServiceRequest;
import com.sendtomoon.eroica.common.app.dto.ServiceResponse;

/**
 * 
 *
 */
public interface ApplicationController {

	/*
	 * Command pattend
	 */
	public ServiceResponse handleRequest(ServiceRequest request)
		throws ApplicationControllerException, RemoteException;
}
