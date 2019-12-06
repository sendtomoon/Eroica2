package com.sendtomoon.eroica.common.security;

public class PasswordContext {

	private String key;
	
	
	private String defaultPassword;
	
	private boolean required=true;
	
	public PasswordContext(){
		
	}
	
	public PasswordContext(String key){
		this.key=key;
	}
	
	
	/*public PasswordContext(String key,String defPassword){
		this.key=key;
		this.defaultPassword=defPassword;
	}*/

	public String getKey() {
		return key;
	}

	public PasswordContext setUser(String key) {
		this.key = key;
		return this;
	}

	
	public String getDefaultPassword() {
		return defaultPassword;
	}

	public PasswordContext setDefaultPassword(String defaultPassword) {
		this.defaultPassword = defaultPassword;
		return this;
	}

	public boolean isRequired() {
		return required;
	}

	public PasswordContext setRequired(boolean required) {
		this.required = required;
		return this;
	}
	
	
}
