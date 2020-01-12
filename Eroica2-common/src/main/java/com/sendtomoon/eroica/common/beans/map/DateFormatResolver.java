package com.sendtomoon.eroica.common.beans.map;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sendtomoon.eroica.common.beans.format.FieldFormatter;

class DateFormatResolver {

	private static DateFormatResolver instance = new DateFormatResolver();

	public static DateFormatResolver getInstance() {
		return instance;
	}

	public Date toDate(Object value) {
		return _toDate(value, null, null);
	}

	public Date toDate(Object value, FieldFormatter formatter) {
		return _toDate(value, null, formatter);
	}

	public Date toDate(Object value, String pattern) {
		return _toDate(value, pattern, null);
	}

	private Date _toDate(Object value, String pattern, FieldFormatter formatter) {
		if (value instanceof Date) {
			return (Date) value;
		} else if (value instanceof Number) {
			return new Date(((Number) value).longValue());
		} else {
			String temp = value.toString();
			if (formatter != null) {
				return (Date) formatter.parse(temp);
			} else {
				try {
					long ts = new Long(temp);
					return new Date(ts);
				} catch (NumberFormatException ex) {
					if (pattern == null) {
						pattern = "yyyy-MM-dd";
					}
					Date tempVal = null;
					try {
						tempVal = new SimpleDateFormat(pattern).parse(temp);
					} catch (ParseException e) {
						throw new java.lang.IllegalArgumentException("date format error ,pattern=[" + pattern + "].");
					}
					return tempVal;
				}
			}

		}
	}

}
