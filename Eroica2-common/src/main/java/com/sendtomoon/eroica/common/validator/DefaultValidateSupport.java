package com.sendtomoon.eroica.common.validator;

import java.util.Map;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.sendtomoon.eroica.common.validator.annotation.ConfigLoader;

/***
 * 验证上下文 验证框架的核心类
 *
 *
 */
public class DefaultValidateSupport implements ValidateSupport {

	private ConfigLoader configLoader = new ConfigLoader();

	public ConfigLoader getConfigLoader() {
		return configLoader;
	}

	public void setConfigLoader(ConfigLoader configLoader) {
		this.configLoader = configLoader;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		if (clazz.getName().startsWith("java") || clazz.isPrimitive())
			return false;
		return true;
	}

	@Override
	public Map<String, NestedPathValidators> getPathValidators(Class<?> beanClazz) {
		return configLoader.getNestedPathValidators(beanClazz);
	}

	public void validate(Object validateObject, Errors errors) {
		ValidatorExecutor executor = new ValidatorExecutor(validateObject, errors, this);
		executor.validate();
	}

	public void validateException(Object validateObject) {
		Errors errors = new BeanPropertyBindingResult(validateObject, validateObject.getClass().getName());
		validate(validateObject, errors);
		if (errors.getErrorCount() > 0) {
			throw new ValidateFailException(validateObject.getClass(), errors);
		}
	}

	@Override
	public void validate(Object validateObject) {
		validateException(validateObject);
	}

}
