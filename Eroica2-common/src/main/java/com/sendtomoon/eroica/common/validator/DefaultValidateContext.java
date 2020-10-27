package com.sendtomoon.eroica.common.validator;

import org.springframework.validation.Errors;

public class DefaultValidateContext implements ValidateContext{
	
	private Object targetRoot;
	
	private Object target;
	
	private Errors errors;
	
	private String targetNestedPath;
	
	public DefaultValidateContext(Object target,String targetNestedPath,Object targetRoot,Errors errors){
		this.target=target;
		this.targetRoot=targetRoot;
		this.errors=errors;
		this.targetNestedPath=targetNestedPath;
	}

	
	public Object getTargetRoot() {
		return targetRoot;
	}


	public void setTargetRoot(Object targetRoot) {
		this.targetRoot = targetRoot;
	}


	public Object getTarget() {
		return target;
	}


	public void setTarget(Object target) {
		this.target = target;
	}


	


	public String getTargetNestedPath() {
		return targetNestedPath;
	}


	public void setTargetNestedPath(String targetNestedPath) {
		this.targetNestedPath = targetNestedPath;
	}


	public Errors getErrors() {
		return errors;
	}

	public void setErrors(Errors errors) {
		this.errors = errors;
	}
	
}
