package com.sendtomoon.eroica2.allergo.impl;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sendtomoon.eroica2.allergo.AllergoConstants;
import com.sendtomoon.eroica2.allergo.AllergoListener;
import com.sendtomoon.eroica2.allergo.AllergoManager;
import com.sendtomoon.eroica2.allergo.AllergoPathListener;
import com.sendtomoon.eroica2.allergo.ConfigChangedListener;
import com.sendtomoon.eroica2.allergo.classloader.AllergoURLException;
import com.sendtomoon.eroica2.common.utils.URLUtils;

public abstract class AbstractAllergoMananger implements AllergoManager {

	protected Log logger = LogFactory.getLog(this.getClass());

	private volatile LocalBackup localBackup;

	private volatile String defRootPath;

	private volatile Charset charset;

	protected URLUtils configURL;

	private volatile IGlobalVariablesHandler globalVariablesHandler;

	public AbstractAllergoMananger() {
	}

	@Override
	public final void init(URLUtils configURL) {
		this.configURL = configURL;
		String rootPath = configURL.getParameter("rootPath");
		boolean isLocalBackup = configURL.getParameter("localBackup", true);
		setDefRootPath(rootPath, true);
		charset = Charset.forName(configURL.getParameter("charset", "UTF-8"));
		// ------------------------------------
		this.doInit(configURL);
		if (isLocalBackup) {
			localBackup = new LocalBackup(configURL, charset);
		}
	}

	public final synchronized void initGlobalVariablesHandler(String path) {
		if (globalVariablesHandler != null) {
			return;
		}
		if (path != null && path.charAt(0) != '/') {
			path = getDefRootPath() + "/" + path;
		}
		globalVariablesHandler = new GlobalVariablesHandler();
		globalVariablesHandler.init(path, this);
	}

	public final synchronized void setDefRootPath(String rootPath, boolean forceOverride) {
		//
		if (rootPath == null) {
			return;
		}
		if (this.defRootPath != null && !forceOverride) {
			return;
		}
		rootPath = rootPath.trim();
		if (rootPath.length() > 0 && rootPath.charAt(0) != '/') {
			rootPath = "/" + rootPath;
		}
		if (rootPath.length() == 0 || rootPath.equals("/")) {
			rootPath = "";
		}
		this.defRootPath = rootPath;
	}

	private String getDefaultRootPath() {
		return AllergoConstants.DEF_ROOT_PATH;
	}

	protected abstract void doInit(URLUtils configURL);

	private static final String PIZZA_PATH_PATTERN_STRING = "^/[\\w\\-\\.]+(/[\\w\\-\\.\\#]+)*$";

	private static final Pattern PIZZA_PATH_PATTERN = Pattern.compile(PIZZA_PATH_PATTERN_STRING);

	protected final static void checkPizzaPath(String allergoPath) {
		if (allergoPath == null) {
			throw new AllergoURLException("allergoPath is null.");
		}
		if (!PIZZA_PATH_PATTERN.matcher(allergoPath).matches()) {
			throw new AllergoURLException("PizzaPath<" + allergoPath + "> format error,Not matched by regex="
					+ PIZZA_PATH_PATTERN_STRING + ".");
		}
	}

	@Override
	public boolean add(String group, String key, String content) {
		return add(group + "/" + key, content);
	}

	@Override
	public boolean add(String path, String content) {
		return add(path, content, false);
	}

	@Override
	public boolean add(String path, String content, boolean ephemeral) {
		if (path.charAt(0) != '/') {
			path = getDefRootPath() + "/" + path;
		}
		checkPizzaPath(path);
		if (logger.isInfoEnabled()) {
			logger.info("Add path<" + path + "> and ephemeral=" + ephemeral + ", content size="
					+ (content == null ? 0 : content.length()));
		}
		return doAdd(path, content, ephemeral);
	}

	protected abstract boolean doAdd(String path, String content, boolean ephemeral);

	@Override
	public boolean del(String path) {
		if (path.charAt(0) != '/') {
			path = getDefRootPath() + "/" + path;
		}
		checkPizzaPath(path);
		if (logger.isInfoEnabled()) {
			logger.info("Del path<" + path + ">.");
		}
		return doDel(path);
	}

	protected abstract boolean doDel(String path);

	@Override
	public boolean del(String group, String key) {
		return this.del(group + "/" + key);
	}

	@Override
	public boolean forceDel(String path) {
		if (path.charAt(0) != '/') {
			path = getDefRootPath() + "/" + path;
		}
		checkPizzaPath(path);
		if (logger.isInfoEnabled()) {
			logger.info("ForceDel path<" + path + ">.");
		}
		return doForceDel(path);
	}

	protected boolean doForceDel(String path) {
		List<String> children = this.listChildren(path);
		if (children != null && children.size() > 0) {
			for (String child : children) {
				doForceDel(path + "/" + child);
			}
		}
		return doDel(path);
	}

	@Override
	public boolean createPath(String path) {
		return createPath(path, false);
	}

	public boolean createPath(String path, boolean ephemeral) {
		if (path.charAt(0) != '/') {
			path = getDefRootPath() + "/" + path;
		}
		checkPizzaPath(path);
		if (logger.isInfoEnabled()) {
			logger.info("Create path<" + path + ">.");
		}
		return doCreatePath(path, ephemeral);
	}

	protected abstract boolean doCreatePath(String path, boolean ephemeral);

	@Override
	public String get(String path) {
		if (path.charAt(0) != '/') {
			path = getDefRootPath() + "/" + path;
		}
		String content = doGet(path);
		if (logger.isInfoEnabled()) {
			logger.info("ReadPizzaResource <" + path + ">, content size=" + (content == null ? 0 : content.length()));
		}
		if (logger.isDebugEnabled() && content != null && content.length() > 0 && content.length() < 10240) {
			logger.debug("ReadPizzaResource <" + path + ">, content:\n" + content + "");
		}
		if (globalVariablesHandler != null) {
			String lowerCasePath = path.toLowerCase();
			// 全局变量过滤
			if (lowerCasePath.endsWith(".properties")) {
				content = globalVariablesHandler.handle(path, content);
			}
		}
		if (localBackup != null) {
			localBackup.pushItem(new LocalBackupFile(path, content));
		}
		return content;
	}

	protected abstract String doGet(String path);

	@Override
	public String get(final String group, final String key) {
		return get(group + "/" + key);
	}

	@Override
	public boolean removeListener(String group, String key, AllergoListener listener) {
		return this.removeListener(group + "/" + key);
	}

	@Override
	public void setListener(String group, String key, ConfigChangedListener listener) {
		this.setListener(group, key, (AllergoListener) listener);
	}

	@Override
	public boolean removeListener(String path) {
		if (path.charAt(0) != '/') {
			path = getDefRootPath() + "/" + path;
		}
		if (logger.isInfoEnabled()) {
			logger.info("RemoveListener for allergo <" + path + ">.");
		}
		return doRemoveListener(path);
	}

	@Override
	public boolean removePathListener(String parentPath) {
		if (parentPath.charAt(0) != '/') {
			parentPath = getDefRootPath() + "/" + parentPath;
		}
		if (logger.isInfoEnabled()) {
			logger.info("RemovePathListener for allergo <" + parentPath + ">.");
		}
		return doRemovePathListener(parentPath);
	}

	protected abstract boolean doRemovePathListener(String parentPath);

	@Override
	public boolean removeListener(String group, String key) {
		return this.removeListener(group + "/" + key);
	}

	protected abstract boolean doRemoveListener(String path);

	@Override
	public int countChildren(String parentPath) {
		if (parentPath.charAt(0) != '/') {
			parentPath = getDefRootPath() + "/" + parentPath;
		}
		return doCountChildren(parentPath);
	}

	public abstract int doCountChildren(String parentPath);

	@Override
	public List<String> listChildren(String parentPath, String likeChild, int beginIndex, int maxRecords) {
		if (parentPath.charAt(0) != '/') {
			parentPath = getDefRootPath() + "/" + parentPath;
		}
		List<String> children = doListChildren(parentPath);
		if (children == null || children.size() == 0) {
			return children;
		}
		if (likeChild != null && (likeChild = likeChild.trim()).length() > 0) {
			List<String> selectedChildren = new ArrayList<String>();
			for (String child : children) {
				if (child.indexOf(likeChild) != -1) {
					selectedChildren.add(child);
				}
			}
			children = selectedChildren;
		}
		if (beginIndex < 0) {
			return children;
		}
		if (beginIndex >= children.size()) {
			return null;
		} else if (beginIndex == 0 && maxRecords >= children.size()) {
			return children;
		} else {
			int idx = beginIndex + maxRecords;
			if (idx > children.size()) {
				idx = children.size();
			}
			List<String> list = new ArrayList<String>();
			for (int i = beginIndex; i < idx; i++) {
				String child = children.get(i);
				list.add(child);
			}
			return list;
		}
	}

	@Override
	public List<String> searchKeys(String parentPath, String likeChild, int beginIndex, int maxRecords) {
		return listChildren(parentPath, likeChild, beginIndex, maxRecords);
	}

	@Override
	public List<String> listChildren(String parentPath, String likeChild) {
		return listChildren(parentPath, likeChild, -1, -1);
	}

	@Override
	public List<String> listChildren(String parentPath) {
		return listChildren(parentPath, null, -1, -1);
	}

	protected abstract List<String> doListChildren(String parentPath);

	@Override
	public void set(String group, String key, String content) {
		set(group + "/" + key, content);
	}

	@Override
	public void set(String path, String content) {
		set(path, content, false);
	}

	@Override
	public void set(String path, String content, boolean ephemeral) {
		if (path.charAt(0) != '/') {
			path = getDefRootPath() + "/" + path;
		}
		checkPizzaPath(path);
		if (logger.isInfoEnabled()) {
			logger.info("Set path<" + path + "> and ephemeral=" + ephemeral + ", content size="
					+ (content == null ? 0 : content.length()));
		}
		doSet(path, content, ephemeral);
	}

	protected abstract void doSet(String path, String content, boolean ephemeral);

	@Override
	public void setListener(String group, String key, AllergoListener listener) {
		setListener(group + "/" + key, listener);
	}

	@Override
	public void setPathListener(String path, AllergoPathListener listener) {
		if (path.charAt(0) != '/') {
			path = getDefRootPath() + "/" + path;
		}
		checkPizzaPath(path);
		if (logger.isInfoEnabled()) {
			logger.info("RegisterPathListener for path <" + path + ">.");
		}
		doSetPathListener(path, listener);
	}

	protected abstract void doSetPathListener(String path, AllergoPathListener listener);

	@Override
	public void setListener(String path, final AllergoListener listener) {
		if (path.charAt(0) != '/') {
			path = getDefRootPath() + "/" + path;
		}
		final String fullPath = path;
		checkPizzaPath(path);
		if (logger.isInfoEnabled()) {
			logger.info("RegisterListener for allergo <" + path + ">.");
		}
		if (globalVariablesHandler != null && path.endsWith(".properties")) {
			AllergoListener newListener = new AllergoListener() {

				@Override
				public void handleConfigChange(String allergoContent) {
					allergoContent = globalVariablesHandler.handle(fullPath, allergoContent);
					if (localBackup != null && allergoContent != null && allergoContent.length() > 0) {
						localBackup.pushItem(new LocalBackupFile(fullPath, allergoContent));
					}
					listener.handleConfigChange(allergoContent);
				}
			};
			doSetListener(path, newListener);
		} else {
			doSetListener(path, listener);
		}
	}

	public abstract void doSetListener(String path, AllergoListener listener);

	@Override
	public boolean exists(String group, String key) {
		return exists(group + "/" + key);
	}

	@Override
	public boolean exists(String path) {
		if (path.charAt(0) != '/') {
			path = getDefRootPath() + "/" + path;
		}
		checkPizzaPath(path);
		boolean exists = doExists(path);
		if (logger.isInfoEnabled()) {
			logger.info("ExistsCheck <" + path + "> = " + exists);
		}
		return exists;
	}

	protected abstract boolean doExists(String path);

	@Override
	public synchronized void shutdown() {
		if (globalVariablesHandler != null) {
			IGlobalVariablesHandler i = this.globalVariablesHandler;
			this.globalVariablesHandler = null;
			i.shutdown();
		}
		doShutdown();
	}

	protected String getDefRootPath() {
		return defRootPath == null ? this.getDefaultRootPath() : defRootPath;
	}

	protected abstract void doShutdown();

	public URLUtils getConfigURL() {
		return configURL;
	}

	public Charset getCharset() {
		return charset;
	}

}
