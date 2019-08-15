package com.sendtomoon.eroica.allergo.context;

import com.sendtomoon.eroica2.allergo.Allergo;

public class RootContextTests {

	public static void main(String args[]) {
		System.setProperty("allergo.app.name", "test");
		System.setProperty("allergo.manager", "classpath:/allergo_resources");
		System.out.println(Allergo.getAllergoContext());
	}
}
