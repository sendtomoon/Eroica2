package com.sendtomoon.eroica.common.beans.format;

import java.lang.reflect.Constructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.util.ClassUtils;

import com.sendtomoon.eroica.common.beans.ParameterInfo;
import com.sendtomoon.eroica.common.exception.EroicaException;

public class FieldFormatterFactory {

	private static Constructor<?> dateFieldFormatterConstructor;

	public static FieldFormatter getNumberFormatter(ParameterInfo bindField, NumberFormat nf) {
		return new NumberFieldFormatter(bindField, nf);
	}

	public static FieldFormatter getDateFormatter(ParameterInfo bindField, DateTimeFormat df) {
		try {
			if (dateFieldFormatterConstructor == null) {
				Class<?> clazz = ClassUtils.forName("com.sendtomoon.eroica.common.beans.format.DateFieldFormatter",
						ClassUtils.getDefaultClassLoader());
				dateFieldFormatterConstructor = clazz.getConstructor(ParameterInfo.class, DateTimeFormat.class);
			}
			return (FieldFormatter) dateFieldFormatterConstructor.newInstance(bindField, df);
		} catch (Exception e) {
			throw new EroicaException("DateFieldFormatter error:" + e.getMessage(), e);
		}
	}

}
