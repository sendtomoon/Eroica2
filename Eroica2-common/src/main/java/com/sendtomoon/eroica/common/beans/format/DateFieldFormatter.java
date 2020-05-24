package com.sendtomoon.eroica.common.beans.format;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.joda.JodaDateTimeFormatAnnotationFormatterFactory;

import com.sendtomoon.eroica.common.beans.BeanTransformException;
import com.sendtomoon.eroica.common.beans.ParameterInfo;

public class DateFieldFormatter implements FieldFormatter {

	private static JodaDateTimeFormatAnnotationFormatterFactory formatterFactory = new JodaDateTimeFormatAnnotationFormatterFactory();

	private Parser<? extends Object> parser;

	private Printer<Object> printer;

	@SuppressWarnings("unchecked")
	public DateFieldFormatter(ParameterInfo pf, DateTimeFormat annotation) {
		parser = formatterFactory.getParser(annotation, pf.getBindClass());
		printer = (Printer<Object>) formatterFactory.getPrinter(annotation, pf.getBindClass());
	}

	@Override
	public Date parse(String value) {
		try {
			DateTime _d = (DateTime) parser.parse(value, Locale.getDefault());
			return _d.toDate();
		} catch (ParseException e) {
			throw new BeanTransformException("value[" + value + "]format error:" + e.getMessage());
		}
	}

	@Override
	public String print(Object value) {
		Date d = (Date) value;
		return printer.print(d.getTime(), Locale.getDefault()).toString();
	}

}
