package com.sendtomoon.eroica.common.validator.impl;

import java.beans.PropertyDescriptor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.InitializingBean;

import com.sendtomoon.eroica.common.validator.IValidator;
import com.sendtomoon.eroica.common.validator.ValidateContext;
import com.sendtomoon.eroica.common.validator.ValidateException;
import com.sendtomoon.eroica.common.validator.annotation.VNotEmpty;
import com.sendtomoon.eroica.common.validator.annotation.VRegex;
import com.sendtomoon.eroica.common.validator.annotation.Valid;


/*******************************************************************************
 * 正则表式检查器
 * 
 */
@Valid
public class RegexValidator implements IValidator,InitializingBean {
	
	/**正则表达式字符串*/
	@VNotEmpty
	private String pattern;
	
	private Pattern _p;
	
	private String message;
	
	public RegexValidator(){
		
	}
 
	public RegexValidator(String pattern) {
		this.pattern = pattern; 
		afterPropertiesSet();
	}
	
	public RegexValidator(VRegex vn){
		this.pattern=vn.value();
		if(this.pattern!=null){
			this.pattern=this.pattern.trim();
		}
		if(this.pattern==null || this.pattern.length()==0){
			this.pattern=vn.defined().getRegex();
		}
		if(vn.message().length()>0){
			this.message=vn.message();
		}
		afterPropertiesSet();
	}

	public boolean validate(ValidateContext context, PropertyDescriptor feild,
			Object value, String nestedPath) {
		Matcher m = _p.matcher(value.toString());
        if(m.matches()){
        
			return true;
		}
		return false;
	}

	

	public String getPattern() {
		return pattern;
	}

	public RuleType getRuleType() {
		return RuleType.Regex;
	}
	
	public String toString() {
		return "{Regex@pattern="+pattern+"}";
	}

	
	public void afterPropertiesSet()  {
		if(this.pattern!=null){
			this.pattern=this.pattern.trim();
		}
		if(pattern==null || pattern.length()==0){
			throw new ValidateException("pattern is null");
		}
		_p = Pattern.compile(pattern);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
}
