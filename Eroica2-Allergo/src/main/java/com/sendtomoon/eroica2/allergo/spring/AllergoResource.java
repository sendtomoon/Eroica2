package com.sendtomoon.eroica2.allergo.spring;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.springframework.core.io.AbstractResource;

import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;

/**
 * Allergo资源，包装zookeeper上的资源
 */
public class AllergoResource extends AbstractResource {

	private final AllergoURL allergoURL;

	public AllergoResource(String allergoPath) {
		this(AllergoURL.valueOf(allergoPath));
	}

	public AllergoResource(AllergoURL allergoURL) {
		this.allergoURL = allergoURL;
	}

	public AllergoResource(String allergoGroup, String allergoKey) {
		this.allergoURL = new AllergoURL(allergoGroup, allergoKey);
	}

	/**
	 * 获取zk数据
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		return allergoURL.getInputStream();
	}

	@Override
	public boolean exists() {
		return allergoURL.exists();
	}

	@Override
	public long contentLength() throws IOException {
		InputStream is = allergoURL.getInputStream();
		return is == null ? -1 : is.available();
	}

	@Override
	public long lastModified() throws IOException {
		return -1;
	}

	@Override
	public URI getURI() throws IOException {
		return allergoURL.toJavaURI();
	}

	@Override
	public URL getURL() throws IOException {
		return allergoURL.toJavaURL();
	}

	@Override
	public String getFilename() {
		return allergoURL.getAllergoKey();
	}

	@Override
	public String getDescription() {
		return allergoURL.toString();
	}

	public AllergoURL getAllergoURL() {
		return allergoURL;
	}

}
