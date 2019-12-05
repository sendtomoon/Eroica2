package com.sendtomoon.eroica.common.seri;

import java.nio.charset.Charset;

import com.alibaba.fastjson.JSONObject;

public class FastJsonSerialization implements Serialization {
	private Charset charset;

	public FastJsonSerialization() {
		charset = Charset.forName("UTF-8");
	}

	public FastJsonSerialization(Charset charset) {
		this.charset = charset;
	}

	@Override
	public Object deserialize(Class<?> clazz, byte[] bytes) {
		String data = new String(bytes, charset);
		return JSONObject.parseObject(data, clazz);
	}

	@Override
	public byte[] serialize(Object value) {
		return JSONObject.toJSONString(value).getBytes(charset);
	}

	public void setCharset(String charset) {
		this.charset = Charset.forName(charset);
	}
}
