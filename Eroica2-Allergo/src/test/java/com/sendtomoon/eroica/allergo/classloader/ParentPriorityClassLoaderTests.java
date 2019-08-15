package com.sendtomoon.eroica.allergo.classloader;

import java.net.URL;

import org.junit.Test;

import com.sendtomoon.eroica2.allergo.classloader.AllergoClassLoader;

public class ParentPriorityClassLoaderTests {

	@Test
	public void test() throws Exception {
		URL jarURL = this.getClass().getResource("test_jar").toURI().toURL();
		System.err.println("jarURL=" + jarURL);
		AllergoClassLoader parentCL = new AllergoClassLoader("parentCL", null);
		parentCL.addURL(jarURL);
		Class<?> classA = parentCL.loadClass("com.test.A");
		System.err.println("parentCL resourceRoot=" + parentCL.getResource(""));
		System.err.println("parentCL classA=" + classA.getName());
		System.err.println("parentCL classA=" + classA.getClassLoader());
		System.err.println("parentCL A=" + classA.getMethod("hello").invoke(classA.newInstance()));
		Class<?> classUUID = parentCL.loadClass("java.util.UUID");
		System.err.println("parentCL classUUID=" + classUUID.getName());
		System.err.println("parentCL classUUID=" + classUUID.getClassLoader());
		testChild(jarURL, parentCL);
	}

	public void testChild(URL jarURL, AllergoClassLoader parentCL) throws Exception {
		AllergoClassLoader childCL = new AllergoClassLoader("childCL", null, parentCL);
		childCL.addURL(jarURL);
		childCL.setParentPriority(false);
		Class<?> classA = childCL.loadClass("com.test.A");
		System.err.println("childCL classA=" + classA.getName());
		System.err.println("childCL classA=" + classA.getClassLoader());
		Class<?> classUUID = childCL.loadClass("java.util.UUID");
		System.err.println("childCL classUUID=" + classUUID.getName());
		System.err.println("childCL classUUID=" + classUUID.getClassLoader());
		childCL.close();
	}
}
