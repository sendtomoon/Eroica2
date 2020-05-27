package com.sendtomoon.eroica.common.beans.format;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;

import com.sendtomoon.eroica.common.beans.BeanTransformException;
import com.sendtomoon.eroica.common.beans.ParameterInfo;


public class NumberFieldFormatter implements FieldFormatter{
	
	private static NumberFormatAnnotationFormatterFactory formatterFactory=new NumberFormatAnnotationFormatterFactory();
	
	
	private Parser<Number> parser;
	
	private Printer<Number> printer;
	
	public  NumberFieldFormatter(ParameterInfo pf,NumberFormat annotation){
		parser= formatterFactory.getParser(annotation, pf.getBindClass());
		printer=formatterFactory.getPrinter(annotation,  pf.getBindClass());
	}
	
	

	@Override
	public Number parse(String value) {
		try {
			return parser.parse(value, Locale.getDefault());
		} catch (ParseException e) {
			throw new BeanTransformException("value["+value+"]format error:"+e.getMessage());
		}
	}

	@Override
	public String print(Object value) {
		return printer.print((Number)value, Locale.getDefault());
	}

}
