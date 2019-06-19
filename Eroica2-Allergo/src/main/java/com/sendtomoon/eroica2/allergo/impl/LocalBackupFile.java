package com.sendtomoon.eroica2.allergo.impl;

import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;

 public class LocalBackupFile {

	private String path;
	
	private String content;
	
	private boolean base64;
	
	public LocalBackupFile(String path,String content){
		this.path=path;
		this.content=content;
		this.base64=AllergoURL.isBase64Content(path);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isBase64() {
		return base64;
	}

	public void setBase64(boolean base64) {
		this.base64 = base64;
	}
	
	
	
}
