package com.sendtomoon.eroica2.allergo.classloader;

import com.sendtomoon.eroica2.allergo.Allergo;

public class ClasspathResourceKey {

	private static final String SPER = "#";

	private String namespace;

	private String resourceName;

	private ClasspathResourceKey(String namespace, String resourceName) {
		this.namespace = namespace;
		this.resourceName = resourceName;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public static ClasspathResourceKey valueOf(String namespace, String resourceName) {
		if (namespace == null || (namespace = namespace.trim()).length() == 0) {
			throw new AllergoURLException("namespace is null.");
		}
		if (resourceName == null || (resourceName = resourceName.trim()).length() == 0) {
			throw new AllergoURLException("resourceName is null.");
		}
		char[] namechars = resourceName.toCharArray();
		for (int i = 0; i < namechars.length; i++) {
			if (namechars[i] == '/' || namechars[i] == '\\') {
				namechars[i] = '.';
			}
		}
		return new ClasspathResourceKey(namespace, new String(namechars));
	}

	public String toString() {
		return this.getNamespace() + SPER + this.getResourceName();
	}

	public AllergoURL toAllergoURL() {
		return new AllergoURL(Allergo.GROUP_RESOURCES, this.toString());
	}

}
