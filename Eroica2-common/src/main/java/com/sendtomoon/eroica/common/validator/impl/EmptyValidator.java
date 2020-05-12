package com.sendtomoon.eroica.common.validator.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import com.sendtomoon.eroica.common.validator.IValidator;
import com.sendtomoon.eroica.common.validator.ValidateContext;
import com.sendtomoon.eroica.common.validator.annotation.VEmpty;
import com.sendtomoon.eroica.common.validator.annotation.VNotEmpty;

public class EmptyValidator implements IValidator {

	private boolean notEmpty = true;

	private String message;

	public EmptyValidator() {

	}

	public EmptyValidator(VNotEmpty vn) {
		if (vn.message().length() > 0) {
			this.message = vn.message();
		}
	}

	public EmptyValidator(VEmpty vn) {
		if (vn.message().length() > 0) {
			this.message = vn.message();
		}
		notEmpty = false;
	}

	@Override
	public RuleType getRuleType() {
		return RuleType.Empty;
	}

	public static void main(String args[]) {
		System.out.println(RuleType.Empty);
	}

	@Override
	public boolean validate(ValidateContext context, PropertyDescriptor feild, Object value, String nestedPath) {
		if (notEmpty) {
			if (value == null)
				return false;
			if (value instanceof String) {
				return ((String) value).length() != 0;
			} else if (value instanceof Number || value instanceof Boolean) {
				return true;
			} else if (feild.getPropertyType().isArray()) {
				return Array.getLength(value) != 0;
			} else if (value instanceof Collection) {
				return ((Collection) value).size() != 0;
			} else if (value instanceof Map) {
				return ((Map) value).size() != 0;
			}
			return true;
		} else {
			if (value == null)
				return true;
			if (value instanceof String || value instanceof Number || value instanceof Boolean) {
				return false;
			} else if (feild.getPropertyType().isArray()) {
				return Array.getLength(value) == 0;
			} else if (value instanceof Collection) {
				return ((Collection) value).size() == 0;
			} else if (value instanceof Map) {
				return ((Map) value).size() == 0;
			}
			return false;
		}
	}

	@Override
	public String toString() {
		return notEmpty ? "NotEmpty" : "RequiredEmpty";
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isNotEmpty() {
		return notEmpty;
	}

	public void setNotEmpty(boolean notEmpty) {
		this.notEmpty = notEmpty;
	}

	public boolean isRequiredEmpty() {
		return !notEmpty;
	}

	public void setRequiredEmpty(boolean requiredEmpty) {
		this.notEmpty = !requiredEmpty;
	}

}
