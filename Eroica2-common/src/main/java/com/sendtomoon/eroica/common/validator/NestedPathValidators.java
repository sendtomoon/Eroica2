package com.sendtomoon.eroica.common.validator;

import com.sendtomoon.eroica.common.validator.annotation.VNotEmpty;
import com.sendtomoon.eroica.common.validator.annotation.Valid;

@Valid
public class NestedPathValidators extends ValidatorsSet {
	@VNotEmpty
	private String path;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	


	@Override
	public void add(IValidator v) {
		super.add(v);
	}


	
	
	
}
