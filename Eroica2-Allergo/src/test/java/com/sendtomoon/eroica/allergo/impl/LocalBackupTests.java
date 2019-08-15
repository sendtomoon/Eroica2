package com.sendtomoon.eroica.allergo.impl;

import java.nio.charset.Charset;

import com.sendtomoon.eroica.common.utils.URLUtils;
import com.sendtomoon.eroica2.allergo.impl.LocalBackup;
import com.sendtomoon.eroica2.allergo.impl.LocalBackupFile;

public class LocalBackupTests {

	public static void main(String args[]) throws Exception {
		LocalBackup lb = new LocalBackup(new URLUtils("http", "localhost", 0), Charset.forName("UTF8"));
		lb.pushItem(new LocalBackupFile("/papp/test.properties", "abc"));
		System.in.read();
	}
}
