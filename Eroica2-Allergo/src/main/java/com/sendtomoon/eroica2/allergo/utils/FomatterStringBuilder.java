package com.sendtomoon.eroica2.allergo.utils;

public class FomatterStringBuilder {
	
	private int keyLength=24;

	private StringBuilder builder=new StringBuilder(32);
	
	public FomatterStringBuilder(){
		
	}
	public FomatterStringBuilder(int keyLength){
		if(keyLength>0){
			this.keyLength=keyLength;
		}
	}
	
	public FomatterStringBuilder(String key,Object value){
		append(key,value);
	}
	
	public FomatterStringBuilder append(String key,Object value){
		builder.append("\n\t\t");
		builder.append(key);
		for(int i=0;i<keyLength-key.length();i++){
			builder.append(' ');
		}
		builder.append('=');
		builder.append('\t');
		builder.append(value);
		return this;
	}

	@Override
	public String toString() {
		return builder.toString();
	} 
	
	
}
