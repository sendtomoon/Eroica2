package com.sendtomoon.eroica2.allergo.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.util.ResourceUtils;

import com.sendtomoon.eroica2.allergo.AllergoConstants;
import com.sendtomoon.eroica2.allergo.AllergoException;
import com.sendtomoon.eroica2.allergo.AllergoListener;
import com.sendtomoon.eroica2.allergo.AllergoPathListener;
import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;
import com.sendtomoon.eroica2.common.utils.URLUtils;

public class LocalAllergoManager extends AbstractAllergoMananger {

	private File rootFile;

	@Override
	protected void doInit(URLUtils configURL) {
		String rootLocation = configURL.getPath();
		String host = configURL.getHost();
		if ("file".equals(configURL.getProtocol())) {

		} else {
			String rootName = AllergoConstants.DEF_ROOT_PATH.substring(1);
			if ((host == null && rootLocation == null) || "classpath:".equals(host)) {
				if (rootLocation == null) {
					rootLocation = "classpath:" + rootName;
				} else {
					if (!(rootLocation.endsWith(rootName) || rootLocation.endsWith(rootName + "/"))) {
						rootLocation = "classpath:" + rootLocation + "/" + rootName;
					} else {
						rootLocation = "classpath:" + rootLocation;
					}
				}
			} else {
				host = (host == null ? "/" : host + "/");
				if (rootLocation == null || (rootLocation = rootLocation.trim()).length() == 0) {
					rootLocation = host + rootName;
				} else {
					if (!(rootLocation.endsWith(rootName) || rootLocation.endsWith(rootName + "/"))) {
						rootLocation = host + rootLocation + "/" + rootName;
					}
				}
			}
		}
		try {
			System.out.println("Allergo root location=" + rootLocation);
			rootFile = ResourceUtils.getFile(rootLocation);
			System.out.println("Allergo root location AbsolutePath=" + rootFile.getAbsolutePath());
			if (!(rootFile.exists() && rootFile.isDirectory())) {
				throw new FileNotFoundException(
						"File<" + rootFile.getAbsolutePath() + "> not be directory or not exists.");
			}
		} catch (FileNotFoundException e) {
			throw new AllergoException(
					"Local allergo manager configURL error<" + configURL + "> error:" + e.getMessage(), e);
		}
	}

	@Override
	protected boolean doAdd(String path, String content, boolean ephemeral) {
		return true;
	}

	@Override
	protected boolean doDel(String path) {
		return true;
	}

	@Override
	protected String doGet(String path) {
		if (AllergoURL.isBase64Content(path)) {
			byte[] bytes = null;
			File file = new File(rootFile, path);
			if (file.exists() && file.isFile()) {
				try {
					bytes = FileUtils.readFileToByteArray(file);
				} catch (IOException e) {
					throw new AllergoException("Read file<" + file.getAbsolutePath() + "> error for allergo<" + path
							+ "> :" + e.getMessage(), e);
				}
			}
			if (bytes == null) {
				return null;
			} else {
				return Base64.encodeBase64String(bytes);
			}
		} else {
			String configValue = null;
			File file = new File(rootFile, path);
			if (file.exists() && file.isFile()) {
				try {
					configValue = FileUtils.readFileToString(file, this.getCharset().name());
				} catch (IOException e) {
					throw new AllergoException("Read file<" + file.getAbsolutePath() + "> error for allergo<" + path
							+ "> :" + e.getMessage(), e);
				}
			}
			return configValue;
		}
	}

	@Override
	protected boolean doRemoveListener(String path) {
		return true;
	}

	@Override
	protected List<String> doListChildren(String parentPath) {
		return null;
	}

	@Override
	public int doCountChildren(String parentPath) {
		return 0;
	}

	@Override
	protected boolean doCreatePath(String path, boolean ephemeral) {
		return true;
	}

	@Override
	protected void doSet(String path, String content, boolean ephemeral) {

	}

	@Override
	public void doSetListener(String path, AllergoListener listener) {

	}

	@Override
	protected boolean doRemovePathListener(String parentPath) {
		return true;
	}

	@Override
	protected void doSetPathListener(String path, AllergoPathListener listener) {

	}

	@Override
	protected boolean doExists(String path) {
		File file = new File(rootFile, path);
		return file.exists() && file.isFile();
	}

	@Override
	protected void doShutdown() {
	}

	@Override
	public String toString() {
		try {
			return this.rootFile.toURI().toString();
		} catch (Exception e) {
			return "local://";
		}
	}

}
