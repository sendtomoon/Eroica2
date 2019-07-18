package com.sendtomoon.eroica2.allergo.classloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSONObject;
import com.sendtomoon.eroica2.allergo.AllergoException;

public class ClasspathResourceContent {

	private int size;

	private String base64datas;

	private ClasspathResourceContent() {
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getBase64datas() {
		return base64datas;
	}

	public void setBase64datas(String base64datas) {
		this.base64datas = base64datas;
	}

	public String toJSONString() {
		return this.toString();
	}

	public static ClasspathResourceContent fromJSONString(String jsonString) {
		return JSONObject.parseObject(jsonString, ClasspathResourceContent.class);
	}

	public static ClasspathResourceContent create(byte[] datas) {
		if (datas == null || datas.length == 0) {
			throw new AllergoException("Datas is null");
		}
		ClasspathResourceContent content = new ClasspathResourceContent();
		content.setSize(datas.length);
		content.setBase64datas(Base64.encodeBase64String(datas));
		return content;
	}

	public static ClasspathResourceContent create(InputStream inputStream) {
		if (inputStream == null) {
			throw new AllergoException("inputStream is null");
		}
		try {
			byte[] datas = IOUtils.toByteArray(inputStream);
			return create(datas);
		} catch (IOException e) {
			throw new AllergoException("Inputstream read error,cause:" + e.getMessage(), e);
		}

	}

	public static ClasspathResourceContent createByFile(File file) {
		byte[] fileContent = null;
		try {
			fileContent = FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			throw new AllergoException("file:" + file + " read error,cause:" + e.getMessage(), e);
		}
		if (fileContent == null || fileContent.length == 0) {
			throw new AllergoException("file:" + file + " be empty.");
		}
		return create(fileContent);
	}

	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
