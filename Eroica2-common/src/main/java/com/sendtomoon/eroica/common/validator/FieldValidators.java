package com.sendtomoon.eroica.common.validator;

import com.sendtomoon.eroica.common.validator.annotation.Valid;


@Valid
public class FieldValidators extends ValidatorsSet  {
	
	
	private String fieldName;
	
	
	
	public FieldValidators(){
		
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public void add(IValidator v) {
		super.add(v);
	}

	
}
