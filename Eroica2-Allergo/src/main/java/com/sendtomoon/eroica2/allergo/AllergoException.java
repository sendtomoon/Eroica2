package com.sendtomoon.eroica2.allergo;


public class AllergoException extends RuntimeException{

	private static final long serialVersionUID = -6144475420830258875L;

	public AllergoException(String msg){
		super(msg);
	}
	
	public AllergoException(String msg,Throwable th){
		super(msg,th);
	}
	


}
