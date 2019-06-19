package com.sendtomoon.eroica2.allergo;

import java.nio.charset.Charset;
import java.util.List;

import com.sendtomoon.eroica2.common.utils.URLUtils;

public interface AllergoManager {

	void setDefRootPath(String rootPath, boolean forceOverride);

	void initGlobalVariablesHandler(String path);

	Charset getCharset();

	List<String> listChildren(String parentPath, String likeChild, int beginIndex, int maxRecords);

	List<String> listChildren(String parentPath, String likeChild);

	List<String> listChildren(String parentPath);

	int countChildren(String parentPath);

	boolean del(String group, String key);

	boolean del(String path);

	boolean forceDel(String path);

	boolean createPath(String path);

	void set(String group, String key, String content);

	void set(String path, String content);

	void set(String path, String content, boolean ephemeral);

	boolean add(String group, String key, String content);

	boolean add(String path, String content);

	boolean add(String path, String content, boolean ephemeral);

	String get(String path);

	String get(String group, String key);

	boolean exists(String path);

	boolean exists(String group, String key);

	void setListener(String group, String key, AllergoListener listener);

	void setListener(String path, AllergoListener listener);

	void setPathListener(String parentPath, AllergoPathListener listener);

	boolean removeListener(String group, String key);

	boolean removeListener(String path);

	boolean removePathListener(String parentPath);

	void init(URLUtils configURL);

	void shutdown();

	URLUtils getConfigURL();

	/***
	 * @deprecated
	 * @param group
	 * @param key
	 * @param listener
	 * @see PizzaListener
	 */
	void setListener(String group, String key, ConfigChangedListener listener);

	boolean removeListener(String group, String key, AllergoListener listener);

	List<String> searchKeys(String parentPath, String likeChild, int beginIndex, int maxRecords);
}
