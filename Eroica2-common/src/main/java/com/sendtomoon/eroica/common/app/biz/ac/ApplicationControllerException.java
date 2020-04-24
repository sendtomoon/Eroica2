package com.sendtomoon.eroica.common.app.biz.ac;

public class ApplicationControllerException extends Exception {

	
  private static final long serialVersionUID = -6814081746628061319L;

  /**
	 * 
	 */
	public ApplicationControllerException() {
		super();
		
	}


	/**
	 * @param cause
	 */
	public ApplicationControllerException(Throwable cause) {
		super(cause);
		
	}



	/**
	 * @param message
	 */
	public ApplicationControllerException(String message) {
		super(message);
		
	}


	/**
	 * @param message
	 * @param cause
	 */
	public ApplicationControllerException(String message, Throwable cause) {
		super(message, cause);
		
	}



}
