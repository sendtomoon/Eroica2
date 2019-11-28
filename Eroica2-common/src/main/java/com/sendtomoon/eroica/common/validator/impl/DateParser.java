package com.sendtomoon.eroica.common.validator.impl;

import java.util.Calendar;
import java.util.Date;

 class DateParser {
	
	private int offerset=0;
	
	private char unit;
	
	
	public DateParser(String dateStr){
		String num=dateStr.substring(1,dateStr.length()-2);
		if(num.charAt(0)=='+'){
			num=num.substring(1);
		}
		offerset=Integer.parseInt(num);
		unit=dateStr.charAt(dateStr.length()-2);
	}
	
	public  Date getCompareDate(){
		Calendar c=Calendar.getInstance();
		switch(unit){
			case 'y':c.add(Calendar.YEAR, offerset);break;
			case 'M':c.add(Calendar.MONTH, offerset);break;
			case 'd':c.add(Calendar.DAY_OF_YEAR, offerset);break;
			case 'H':c.add(Calendar.HOUR_OF_DAY, offerset);break;
			case 'm':c.add(Calendar.MINUTE,offerset);break;
			case 's':c.add(Calendar.SECOND, offerset);break;
			
		}
		return c.getTime();
	}
	

	public static void main(String args[]) throws Exception{
		System.out.println(new DateParser("{0y}").getCompareDate());
	}
}
