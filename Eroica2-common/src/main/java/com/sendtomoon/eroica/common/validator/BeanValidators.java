package com.sendtomoon.eroica.common.validator;

import java.util.List;

import com.sendtomoon.eroica.common.validator.annotation.VNotEmpty;
import com.sendtomoon.eroica.common.validator.annotation.Valid;
@Valid
public class BeanValidators {
	
	@VNotEmpty
	private Class<?> targetClass;
	
	private List<FieldValidators>  fields;

	
	public Class<?> getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public List<FieldValidators> getFields() {
		return fields;
	}

	public void setFields(List<FieldValidators> fields) {
		this.fields = fields;
	}
	
	
}
