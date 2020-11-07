package com.sendtomoon.eroica.common.validator;

import org.springframework.validation.Errors;

public interface ValidateContext {
	
	

	
	public Object getTargetRoot();



	public Object getTarget() ;


	


	public String getTargetNestedPath();


	

	public Errors getErrors() ;
	
}
