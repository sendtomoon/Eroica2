package com.sendtomoon.eroica.common.security;


public class PasswordProviderException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6144475420830258875L;

	public PasswordProviderException(String msg){
		super(msg);
	}
	
	public PasswordProviderException(String msg,Throwable th){
		super(msg,th);
	}
	


}
