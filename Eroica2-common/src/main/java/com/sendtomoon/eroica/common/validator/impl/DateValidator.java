package com.sendtomoon.eroica.common.validator.impl;

import java.beans.PropertyDescriptor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.sendtomoon.eroica.common.validator.IValidator;
import com.sendtomoon.eroica.common.validator.ValidateContext;
import com.sendtomoon.eroica.common.validator.ValidateException;
import com.sendtomoon.eroica.common.validator.annotation.VDate;


public class DateValidator implements   IValidator,InitializingBean {
	
	private static Log log=LogFactory.getLog(DateValidator.class);
	

	private String min;
	
	private String max;
	
	private String pattern;
	
	private String message;
	
	
	private DateParser _minParser;
	
	private DateParser _maxParser;
	
	private Date _minDate;
	
	private Date _maxDate;
	
	
	public DateValidator(){
		
	}
	
	public DateValidator(String min,String max,String pattern){
		if(StringUtils.hasText(min)){
			this.min=min;
		}
		if(StringUtils.hasText(max)){
			this.max=max;
		}
		if(StringUtils.hasText(pattern)){
			this.pattern=pattern;
		}
		afterPropertiesSet();
	}
	
	public DateValidator(VDate vn){
		if(vn.max().length()>0){
			max=vn.max();
		}
		if(vn.min().length()>0){
			min=vn.min();
		}
		if(vn.pattern().length()>0){
			pattern=vn.pattern();
		}
		if(vn.message().length()>0){
			this.message=vn.message();
		}
		afterPropertiesSet();
	}

	
	public String getMin() {
		return min;
	}



	public String getMax() {
		return max;
	}



	public String getPattern() {
		return pattern;
	}

	


	@Override
	public RuleType getRuleType() {
		return RuleType.Date;
	}


	@Override
	public boolean validate(ValidateContext context, PropertyDescriptor feild,
			Object value, String nestedPath) {
		long v=-1;
		if(value instanceof Date){
			v=((Date)value).getTime();
		}else {
			String temp=pattern;
			if(temp==null){
				temp="yyyy-MM-dd";
			}
			try {
				v = vdate(value,pattern);
			} catch (ParseException e) {
				if(log.isErrorEnabled()){
					log.error("["+context.getTarget().getClass().getName()+"."+feild.getName()+"] [value ="+value+",pattern="+pattern+"]"+e.getMessage());
				}
				return false;
			}
		}
		if(v!=-1){
			if(min!=null){
				Date minDate=null;
				if(_minParser!=null){
					minDate=_minParser.getCompareDate();
				}else{
					minDate= _minDate;
				}
				if(v<minDate.getTime()){
					return false;
				}
			}
			if(max!=null){
				Date maxDate=null;
				if(_maxParser!=null){
					maxDate=_maxParser.getCompareDate();
				}else{
					maxDate= _maxDate;
				}
				if(v>maxDate.getTime()){
					return false;
				}
			}
			return true;
		}else{
			return false;
		}
	}
	
	public long vdate(Object value,String pattern) throws ParseException {
		// 解决传入的日期参数包含字母、兼容 sdf.setLenient(false); 情况下的不严谨时间格式：如 YYYYMMDD等
		String regex = ".*[a-zA-Z]+.*";
		Matcher m = Pattern.compile(regex).matcher(value.toString());
		if (m.matches()) {
			throw new ParseException("Unparseable date: " + value.toString(), 0);
		}
		pattern = pattern.replace("YYYY", "yyyy").replace("YY", "yy").replace("DD", "dd").replace("SS", "ss");
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		sdf.setLenient(false);
		return sdf.parse(value.toString()).getTime();
	}
	
	public void setMin(String min) {
		this.min = min;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
	public void afterPropertiesSet() {
		try {
			if(min!=null){
				if(min.charAt(0)=='{'){
					_minParser=new DateParser(min);
				}else{
					Assert.notNull(pattern, "pattern  is null");
					SimpleDateFormat sdf=new SimpleDateFormat(pattern);
					_minDate= sdf.parse(min);
				}
			}
			if(max!=null){
				if(max.charAt(0)=='{'){
					_maxParser=new DateParser(max);
				}else{
					Assert.notNull(pattern, "pattern  is null");
					SimpleDateFormat sdf=new SimpleDateFormat(pattern);
					_maxDate= sdf.parse(max);
				}
			}
		} catch (ParseException e) {
			throw new ValidateException("Validator defined error[max="+max+",pattern="+pattern+"]"+e.getMessage());
		}
	}
	
	

	

	@Override
	public String toString() {
		return "Datetime@@min="+min+",max="+max+",pattern="+pattern;
	}
}
