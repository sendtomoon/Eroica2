package com.sendtomoon.eroica.common.beans.format;

public interface FieldFormatter {
	
	public Object parse(String value);
	
	public String print(Object value);
	
}
