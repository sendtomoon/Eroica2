package com.sendtomoon.eroica2.allergo.impl;

import com.sendtomoon.eroica2.allergo.AllergoManager;

public interface IGlobalVariablesHandler {

	String handle(String path,String originalContent);
	
	void init(String path,AllergoManager allergoManager);
	
	void shutdown();
	
}
