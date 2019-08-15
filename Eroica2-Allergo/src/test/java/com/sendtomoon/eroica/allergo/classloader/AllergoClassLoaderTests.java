package com.sendtomoon.eroica.allergo.classloader;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.sendtomoon.eroica2.allergo.Allergo;
import com.sendtomoon.eroica2.allergo.classloader.AllergoClassLoader;
import com.sendtomoon.eroica2.allergo.classloader.ClasspathResourceContent;
import com.sendtomoon.eroica2.allergo.classloader.ClasspathResourceKey;

public class AllergoClassLoaderTests {

	@Test
	public void testAllergoResource() throws Exception {
		ClasspathResourceContent content = ClasspathResourceContent
				.create(IOUtils.toByteArray(this.getClass().getResourceAsStream("merchant.xml")));
		String sarName = "eroapp5_sample_helloworld";
		ClasspathResourceKey key = ClasspathResourceKey.valueOf(sarName, "merchant.xml");
		Allergo.getManager().set(Allergo.GROUP_RESOURCES, key.toString(), content.toJSONString());
		File dir = new File(this.getClass().getResource("/").getFile());
		dir.mkdirs();
		AllergoClassLoader test = new AllergoClassLoader(sarName, dir.toURI().toURL());
		URL url = test.getResource("merchant.xml");
		System.err.println("URL=" + url);
		System.err.println("URL=" + url.getFile());
		File fileout = new File(dir, key.toString());
		FileUtils.writeStringToFile(fileout, content.toJSONString());
	}

}
