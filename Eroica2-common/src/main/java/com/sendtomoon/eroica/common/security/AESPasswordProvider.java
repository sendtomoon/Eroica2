package com.sendtomoon.eroica.common.security;

import java.util.Properties;


public class AESPasswordProvider implements PasswordProvider{
	
	@Override
	public void refreshConfig(Properties properties) {
		
	}

	@Override
	public String getPassword(PasswordContext context) {
		String key=context.getKey();
		String password=null;
		if(key!=null && (key=key.trim()).length()>0){
			try{
				password=PasswordCodeUtils.decode(key);
			}catch(Exception ex){
				throw new PasswordProviderException("Decode key:"+key+" failure,cause:"+ex.getMessage(),ex);
			}
		}
		if(password==null){
			if(context.getDefaultPassword()!=null){
				return context.getDefaultPassword();
			}else if(context.isRequired()){
				throw new NullPointerException("read failure,password is null.");
			}
		}
		return password;
	}
	
	public static void main(String args[]){
		
	}

}
