package com.sendtomoon.eroica.common.validator.impl;

import java.beans.PropertyDescriptor;

import com.sendtomoon.eroica.common.validator.IValidator;
import com.sendtomoon.eroica.common.validator.ValidateContext;
import com.sendtomoon.eroica.common.validator.annotation.VNumber;
import com.sendtomoon.eroica.common.validator.annotation.Valid;

/***
 * 数值验证器
 */
@Valid
public class NumberValidator implements IValidator {

	private Double max;

	private Double min;

	private String message;

	/** 精度,-1为不限定,0表示整数 */
	@VNumber(min = -1)
	private int precision = -1;

	public NumberValidator() {

	}

	public NumberValidator(VNumber vn) {
		if (vn.max() != Double.MAX_VALUE) {
			this.max = vn.max();
		}
		if (vn.min() != Double.MIN_VALUE) {
			this.min = vn.min();
		}
		this.precision = vn.precision();
		if (vn.message().length() > 0) {
			this.message = vn.message();
		}
	}

	public boolean validate(ValidateContext context, PropertyDescriptor feild, Object value, String nestedPath) {
		double v = 0;
		try {
			v = Double.valueOf(value.toString()).doubleValue();
		} catch (java.lang.Exception ex) {
			return false;
		}
		if (max != null && v > max) {
			return false;
		}
		if (min != null && v < min) {
			return false;
		}
		// 验证精度
		if (precision >= 0) {
			String temp = String.valueOf(value);
			int idx = temp.lastIndexOf('.');
			int ps = 0;// 实际精度,0表示整数
			if (idx != -1 && idx != temp.length() - 1) {
				ps = temp.substring(idx + 1).length();// 取得实际精度
			}
			if (ps > precision) {// 实际精度大于限定精度为失败
				return false;
			}
		}
		return true;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public RuleType getRuleType() {
		return RuleType.Number;
	}

	public String toString() {
		return "Number@max=" + (min == null ? "*" : min) + ",min=" + (min == null ? "*" : min) + ",precision="
				+ precision;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
