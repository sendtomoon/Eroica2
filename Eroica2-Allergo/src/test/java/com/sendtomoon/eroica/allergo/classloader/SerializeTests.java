package com.sendtomoon.eroica.allergo.classloader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.serialize.ObjectInput;
import com.alibaba.dubbo.common.serialize.ObjectOutput;
import com.alibaba.dubbo.common.serialize.Serialization;
import org.junit.Test;

import com.sendtomoon.eroica2.allergo.classloader.AllergoClassLoader;

public class SerializeTests {

	private static final URL url = URL.valueOf("empty://");

	//
	@Test
	public void test() throws Exception {
		java.net.URL jarURL = this.getClass().getResource("test_jar").toURI().toURL();
		System.err.println("jarURL=" + jarURL);
		AllergoClassLoader cl = new AllergoClassLoader("test", null);
		cl.addURL(jarURL);
		//
		Class<?> classA = cl.loadClass("com.test.DTO");
		Object obj1 = classA.newInstance();
		// hessian2 compactedjava fastjson
		Thread.currentThread().setContextClassLoader(cl);
		Serialization s = getSerialization(null);
		byte[] datas = this.serialize(s, obj1);
		System.err.println("datas size=" + datas.length);
		Object obj2 = this.deserialize(s, classA, datas);
		System.err.println(obj2);
		cl.close();
	}

	protected Serialization getSerialization(String type) {
		if (type == null || (type = type.trim()).length() == 0) {
			return null;
		}
		ExtensionLoader<Serialization> loader = ExtensionLoader.getExtensionLoader(Serialization.class);
		return loader.getDefaultExtension();
	}

	protected byte[] serialize(Serialization serialization, Object value) {
		if (value == null) {
			return null;
		}
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutput output = serialization.serialize(url, baos);
			output.writeObject(value);
			output.flushBuffer();
			//
			return baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("Serialize error,cause:" + e.getMessage(), e);
		}
	}

	protected Object deserialize(Serialization serialization, Class<?> elementClass, byte[] datas) {
		if (datas == null)
			return null;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(datas);
			ObjectInput input = serialization.deserialize(url, bais);
			if (elementClass == null || elementClass.equals(Object.class)) {
				return (Object) input.readObject();
			} else {
				return (Object) input.readObject(elementClass);
			}
		} catch (Exception e) {
			throw new RuntimeException("Deserialize error,cause:" + e.getMessage(), e);
		}
	}
}
