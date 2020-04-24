package com.sendtomoon.eroica.common.app.biz.ac;

import com.sendtomoon.eroica.common.app.dto.ServiceRequest;
import com.sendtomoon.eroica.common.app.dto.ServiceResponse;

public interface ApplicationControllerLocal extends ApplicationController {

	public ServiceResponse handleRequest(ServiceRequest request);
}
