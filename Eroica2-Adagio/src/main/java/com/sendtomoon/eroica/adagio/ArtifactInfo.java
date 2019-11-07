package com.sendtomoon.eroica.adagio;

import java.util.regex.Pattern;

/**
 * 附件信息
 *
 */
public class ArtifactInfo implements java.io.Serializable {

	private static final Pattern PATTERN = Pattern.compile("^[\\w\\-\\.]+$");

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 所属组织 */
	private String organization;

	/** 所属模块 */
	private String module;

	/** 版本号 */
	private String version;

	private String fileName;

	public ArtifactInfo() {
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		if (organization != null && (organization = organization.trim()).length() > 0) {
			if (PATTERN.matcher(organization).matches()) {
				this.organization = organization;
			} else {
				throw new IllegalArgumentException("organization:" + organization + " format error.");
			}
		} else {
			throw new NullPointerException("organization is null.");
		}
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		if (module != null && (module = module.trim()).length() > 0) {
			if (PATTERN.matcher(module).matches()) {
				this.module = module;
			} else {
				throw new IllegalArgumentException("module:" + module + " format error.");
			}
		} else {
			throw new NullPointerException(" module is null.");
		}
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		if (version != null && (version = version.trim()).length() > 0) {
			if (PATTERN.matcher(version).matches()) {
				this.version = version;
			} else {
				throw new IllegalArgumentException("version:" + version + " format error.");
			}
		} else {
			throw new NullPointerException(" version is null.");
		}
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		if (fileName != null && (fileName = fileName.trim()).length() > 0) {
			if (PATTERN.matcher(fileName).matches()) {
				this.fileName = fileName;
			} else {
				throw new IllegalArgumentException("fileName:" + fileName + " format error.");
			}
		} else {
			throw new NullPointerException(" fileName is null.");
		}
	}

	public String toString() {
		return toGoordinate();
	}

	public String toGoordinate() {
		if (organization != null && organization.length() > 0) {
			StringBuilder str = new StringBuilder();
			str.append(organization).append(':').append(this.module).append(':').append(this.version);
			return str.toString();
		} else {
			return this.fileName;
		}
	}

	public String toAllergoKey() {
		if (organization != null && organization.length() > 0) {
			StringBuilder str = new StringBuilder();
			str.append(organization).append('#').append(this.module).append('#').append(this.version).append(".jar");
			return str.toString();
		} else {
			return this.fileName;
		}
	}

	public String toPathURI() {
		if (organization != null && organization.length() > 0) {
			StringBuilder str = new StringBuilder();
			char[] chars = organization.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				if (chars[i] == '.') {
					str.append('/');
				} else {
					str.append(chars[i]);
				}
			}
			str.append('/').append(this.module).append('/').append(this.version);
			if (fileName != null && fileName.length() > 0) {
				str.append('/').append(fileName);
			} else {
				str.append('/').append(module).append('-').append(version).append(".jar");
			}
			return str.toString();
		} else {
			return this.fileName;
		}
	}

	public boolean hasGoordinate() {
		return organization != null && organization.length() > 0;
	}

}
