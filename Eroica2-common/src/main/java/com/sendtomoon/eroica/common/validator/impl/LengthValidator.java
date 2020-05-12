package com.sendtomoon.eroica.common.validator.impl;

import java.beans.PropertyDescriptor;

import com.sendtomoon.eroica.common.validator.IValidator;
import com.sendtomoon.eroica.common.validator.ValidateContext;
import com.sendtomoon.eroica.common.validator.annotation.VLength;
import com.sendtomoon.eroica.common.validator.annotation.VNumber;
import com.sendtomoon.eroica.common.validator.annotation.Valid;


/***
 * 长度验证器
 */
@Valid
public class LengthValidator implements IValidator {

	/**最小长度**/
	@VNumber(min=-1)
	private int min=-1;

	/**最大长度,小于等于0为不限定*/
	@VNumber(min=-1)
	private int max=-1;

	/**是否按字节计算长度,默认false,即按字符数统计*/
	private boolean byteUnit;
	
	private String message;
	
	public LengthValidator(){}

	public LengthValidator(int min,int max,boolean byteUnit){
		this.max=max;
		this.min=min;
		this.byteUnit=byteUnit;
	}
	
	public LengthValidator(VLength vn){
		this.max=vn.max();
		this.min=vn.min();
		this.byteUnit=vn.byteUnit();
		if(vn.message().length()>0){
			this.message=vn.message();
		}
	}
	
	
	public boolean validate(ValidateContext context, PropertyDescriptor feild,
			Object value, String nestedPath) {
		int len=-1;
		String t=value.toString();
		len=t.length();
		if(byteUnit){
			len=t.getBytes().length;
		}else{
			len=t.length();
		}
		if (min >= 0 && len < min) {// min
			return false;
		}
		if (max >= 0 && len > max) {// max
			return false;
		}
		return true;// 验证通过
	}

	public int getMin() {
		return min;
	}

	

	public int getMax() {
		return max;
	}
  
	

	

	

	public boolean isByteUnit() {
		return byteUnit;
	}

	


	public RuleType getRuleType() {
		return RuleType.Length;
	}

	public String toString() {
		return "Length@max="+max+",min="+min+(byteUnit?"Bytes":"Chars");
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public void setByteUnit(boolean byteUnit) {
		this.byteUnit = byteUnit;
	}

	

}
