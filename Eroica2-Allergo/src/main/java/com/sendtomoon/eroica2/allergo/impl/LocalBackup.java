package com.sendtomoon.eroica2.allergo.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sendtomoon.eroica2.allergo.AllergoException;
import com.sendtomoon.eroica.common.utils.URLUtils;

public class LocalBackup {

	private Log logger = LogFactory.getLog(this.getClass());

	private int maxQueueSize = 1000;

	private PersistThread persistThread;

	private File rootDirectory;

	private final ConcurrentLinkedQueue<LocalBackupFile> queue = new ConcurrentLinkedQueue<LocalBackupFile>();

	public LocalBackup(URLUtils configURL, Charset charset) {
		init(configURL, charset);
	}

	protected void init(URLUtils configURL, Charset charset) {
		rootDirectory = resolveLocalRootFile();
		persistThread = new PersistThread(charset);
		persistThread.start();
	}

	public void pushItem(LocalBackupFile file) {
		if (queue.size() > maxQueueSize) {
			logger.warn("LocalBackupQueue size>" + maxQueueSize);
			return;
		}
		// 不带文件后缀的不备份
		if (file.getPath().indexOf('.') > 0) {
			queue.add(file);
		}
	}

	public synchronized void persist(LocalBackupFile item, Charset charset) {
		File file = new File(this.rootDirectory, item.getPath());
		if (logger.isDebugEnabled()) {
			logger.debug("Save file:" + file.getPath());
		}
		String content = item.getContent();
		if (content == null || content.length() == 0) {
			if (file.exists()) {
				if (!file.delete()) {
					throw new AllergoException("File:" + file + " delete failure,privliege error for user="
							+ System.getProperty("user.name"));
				}
			}
		} else {
			try {
				file.getParentFile().mkdirs();
				if (!file.exists()) {
					file.createNewFile();
				}
				if (item.isBase64()) {
					byte[] bytes = Base64.decodeBase64(content);
					FileUtils.writeByteArrayToFile(file, bytes, false);
				} else {
					FileUtils.write(file, content, charset.name(), false);
				}
			} catch (IOException e) {
				throw new AllergoException("File:" + file + " io error,privliege error for user="
						+ System.getProperty("user.name") + ",cause:" + e.getMessage(), e);
			}
		}
	}

	public File resolveLocalRootFile() {
		String rootDir = System.getProperty("eroica.log.home");
		if (rootDir == null || (rootDir = rootDir.trim()).length() == 0) {
			rootDir = System.getProperty("user.home");
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		rootDir = rootDir + "/allergobackup/" + format.format(new Date());
		//
		File rootDirectory = new File(rootDir);
		// ----------------------------------------------------------------
		rootDirectory.mkdirs();
		if (!rootDirectory.exists()) {
			throw new AllergoException(
					"Directory:" + rootDir + " create failed for user=" + System.getProperty("user.name"));
		}
		if (!rootDirectory.isDirectory()) {
			throw new AllergoException("Directory:" + rootDir + "  not be directory.");
		}
		if (logger.isInfoEnabled()) {
			logger.info("Allergo-LocalBackup directory=" + rootDirectory);
		}
		return rootDirectory;
	}

	public void shutdown() {
		if (this.persistThread != null) {
			PersistThread thread = this.persistThread;
			this.persistThread = null;
			thread.shutdown();
		}
		if (queue != null)
			queue.clear();
	}

	private class PersistThread extends Thread {

		private volatile Charset charset;

		private volatile boolean running = true;

		public PersistThread(Charset charset) {
			super.setDaemon(true);
			super.setName("t-allergo-local-backup");
			this.charset = charset;
		}

		@Override
		public void run() {
			while (running) {
				try {
					LocalBackupFile msg = null;
					while (running && (msg = queue.poll()) != null) {
						persist(msg, charset);
					}
				} catch (Throwable t) {
					logger.warn("Failed to persist, cause: " + t.getMessage());
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
			}
		}

		public void shutdown() {
			running = false;
		}

	}
}
