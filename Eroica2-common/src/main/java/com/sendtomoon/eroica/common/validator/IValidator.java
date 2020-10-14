package com.sendtomoon.eroica.common.validator;

import java.beans.PropertyDescriptor;

public interface IValidator {

	/***
	 * 验证
	 * 
	 * @return 验证是否通过
	 */
	boolean validate(ValidateContext context, PropertyDescriptor feild, Object value, String nestedPath)
			throws Throwable;

	/***
	 * 获得验证器的类型
	 */
	RuleType getRuleType();

	/**
	 * 提示信息
	 * 
	 * @return
	 */
	String getMessage();

	/** 验证规则类型:-1未知,0自定义规则,1长度验证,2数值验证,3枚举验证,4正则表达式验证,5非空验证,6日期验证 */
	public enum RuleType {
		Custom(0), Length(1), Number(2), Enum(3), Regex(4), Empty(5), Date(6);

		private int typeCode;

		private RuleType(int typeCode) {
			this.typeCode = typeCode;
		}

		public int getTypeCode() {
			return typeCode;
		}

	}
}
