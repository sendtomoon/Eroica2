package com.sendtomoon.eroica.common.validator.impl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import com.sendtomoon.eroica.common.validator.IValidator;
import com.sendtomoon.eroica.common.validator.ValidateContext;
import com.sendtomoon.eroica.common.validator.ValidateException;
import com.sendtomoon.eroica.common.validator.annotation.VEnum;

/***
 * 枚举验证器(内置)
 */
public class EnumsValidator implements  IValidator {

	/**枚举项集合*/
	private List<String> options;
	
	
	private String message;
	
	public EnumsValidator(){}
	
	public EnumsValidator(List<String> options){
		this.options=options;
		afterPropertiesSet();
	}
	
	public EnumsValidator(VEnum vn){
		String temp[]=vn.value();
		List<String> tempList=new ArrayList<String>(temp.length);
		for(int i=0;i<temp.length;i++){
			tempList.add(temp[i]);
		}
		options=tempList;
		if(vn.message().length()>0){
			this.message=vn.message();
		}
		afterPropertiesSet();
	}
	
	
	public boolean validate(ValidateContext context, PropertyDescriptor feild,
			Object value, String nestedPath) {
		String vtemp=value.toString();
		//-------------------------------------------------------------
		for (int i = 0; i < options.size(); i++) {
			if (vtemp.equals(options.get(i))) {
				return true;
			}
		}
		return false;
	}

	public List<String> getOptions() {
		return options;
	}




	public RuleType getRuleType() {
		return RuleType.Enum;
	}

	@Override
	public String toString() {
		return "Enums@"+options+"";
	}

	public void afterPropertiesSet()  {
		//
		if (options == null || options.size() == 0){
			throw new ValidateException("enums limit - options count= 0");
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	

	
}
