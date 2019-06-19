package com.sendtomoon.eroica2.allergo.classloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import com.sendtomoon.eroica2.allergo.AllergoListener;
import com.sendtomoon.eroica2.allergo.AllergoManager;

public class AllergoURL {

	private static String ALLERGO_URL_PREFIX = "allergo:";

	private static final String ALLERGO_PATH_PATTERN_STRING = "^[\\w\\-\\.]+(/[\\w\\-\\.\\#]+)+$";

	private static final Pattern ALERGO_PATH_PATTERN = Pattern.compile(ALLERGO_PATH_PATTERN_STRING);

	public static boolean isAllergoURL(String path) {
		return path != null && path.toLowerCase().startsWith(ALLERGO_URL_PREFIX);
	}

	private static void checkAllergoPath(String allergoPath) {
		if (allergoPath == null) {
			throw new AllergoURLException("allergoPath is null.");
		}
		if (!ALERGO_PATH_PATTERN.matcher(allergoPath).matches()) {
			throw new AllergoURLException("AllergoPath=" + allergoPath + " format error,Not matched by regex="
					+ ALLERGO_PATH_PATTERN_STRING + ".");
		}
	}

	public static AllergoURL valueOf(String path) {
		return new AllergoURL(path);
	}

	public static AllergoURL valueOf(String allergoGroup, String allergoKey) {
		return new AllergoURL(allergoGroup, allergoKey);
	}

	private String allergoKey;

	private String allergoGroup;

	private String rootPath;

	private AllergoManager manager;

	public AllergoURL(String path) {
		if (path == null || (path = path.trim()).length() == 0) {
			throw new AllergoURLException("allergoPath is null");
		}
		if (path.toLowerCase().startsWith(ALLERGO_URL_PREFIX)) {
			path = path.substring(ALLERGO_URL_PREFIX.length());
		}
		if (path.charAt(0) == '/') {
			path = path.substring(1);
		}
		checkAllergoPath(path);
		int idx = path.lastIndexOf('/');
		this.allergoKey = path.substring(idx + 1);
		String parent = path.substring(0, idx);
		idx = parent.lastIndexOf('/');
		if (idx != -1) {
			this.allergoGroup = parent.substring(idx + 1);
			this.rootPath = parent.substring(0, idx);
		} else {
			this.allergoGroup = parent;
		}
	}

	public boolean isBase64Content() {
		String key = this.allergoKey;
		return isBase64Content(key);
	}

	public static boolean isBase64Content(String path) {
		if (path == null)
			return false;
		path = path.toLowerCase();
		return (path.endsWith(".jar") || path.endsWith(".zip") || path.endsWith(".data"));
	}

	public AllergoURL(String allergoGroup, String allergoKey) {
		if (allergoKey == null || (allergoKey = allergoKey.trim()).length() == 0) {
			throw new AllergoURLException("allergoKey is null");
		}
		if (allergoGroup == null || (allergoGroup = allergoGroup.trim()).length() == 0) {
			throw new AllergoURLException("allergoGroup is null");
		}
		String allergoPath = allergoGroup + "/" + allergoKey;
		checkAllergoPath(allergoPath);
		this.allergoGroup = allergoGroup;
		this.allergoKey = allergoKey;
	}

	public URL toJavaURL() {
		try {
			return new URL("allergo", null, -1, toURI(), getURLStreamHandler());
		} catch (MalformedURLException e) {
			throw new AllergoURLException(e.getMessage(), e);
		}
	}

	public String getTextContent(boolean requriedExists) {
		return AllergoContentUtils.getTextContent(getManager(), this, requriedExists);
	}

	public String getTextContent() {
		return AllergoContentUtils.getTextContent(getManager(), this, true);
	}

	public InputStream getInputStream() {
		return AllergoContentUtils.toIputStream(getManager(), this, true);
	}

	public InputStream getInputStream(boolean requriedExists) {
		return AllergoContentUtils.toIputStream(getManager(), this, requriedExists);
	}

	public Reader getReader() {
		return getReader(true);
	}

	public Reader getReader(boolean requriedExists) {
		InputStream is = getInputStream(requriedExists);
		if (is != null) {
			return new InputStreamReader(is);
		}
		return null;
	}

	public long contentLength() throws IOException {
		InputStream is = this.getInputStream();
		return is == null ? -1 : is.available();
	}

	public URI toJavaURI() {
		try {
			return this.toJavaURL().toURI();
		} catch (URISyntaxException e) {
			throw new AllergoURLException(e.getMessage(), e);
		}
	}

	public boolean exists() {
		return getManager().exists(toAllergoPath());
	}

	public String toAllergoPath() {
		return (this.rootPath == null ? "" : "/" + this.rootPath + "/") + allergoGroup + "/" + allergoKey;
	}

	public String toURI() {
		return (this.rootPath == null ? "/" : "/" + this.rootPath + "/") + allergoGroup + "/" + allergoKey;
	}

	@Override
	public String toString() {
		return ALLERGO_URL_PREFIX + toAllergoPath();
	}

	@Override
	public int hashCode() {
		return toAllergoPath().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof AllergoURL) {
			return obj.toString().equals(this.toString());
		}
		return false;
	}

	private volatile URLStreamHandler URLStreamHandler;

	private URLStreamHandler getURLStreamHandler() {
		if (URLStreamHandler == null) {
			synchronized (this) {
				URLStreamHandler = new AllergoURLStreamHandler(this.getManager());
			}
		}
		return URLStreamHandler;
	}

	private AllergoManager getManager() {
		if (manager == null) {
			manager = com.sendtomoon.eroica2.allergo.Allergo.getManager();
		}
		if (manager == null) {
			throw new NullPointerException("manager required.");
		}
		return manager;
	}

	public void setManager(AllergoManager manager) {
		this.manager = manager;
	}

	public String getRootPath() {
		return rootPath;
	}

	public Charset getCharset() {
		return this.getManager().getCharset();
	}

	public void setListener(AllergoListener allergoListener) {
		this.getManager().setListener(this.toAllergoPath(), allergoListener);
	}

	public String getAllergoKey() {
		return allergoKey;
	}

	public void setAllergoKey(String allergoKey) {
		this.allergoKey = allergoKey;
	}

	public String getAllergoGroup() {
		return allergoGroup;
	}

	public void setAllergoGroup(String allergoGroup) {
		this.allergoGroup = allergoGroup;
	}

}
