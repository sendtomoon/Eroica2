package com.sendtomoon.eroica2.allergo.impl;

import org.I0Itec.zkclient.IZkDataListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sendtomoon.eroica2.allergo.AllergoListener;

public class AllergoZkDataListener implements IZkDataListener {

	protected static Log log = LogFactory.getLog(AllergoZkDataListener.class);

	private AllergoListener listener;

	public AllergoZkDataListener(AllergoListener listener) {
		this.listener = listener;
	}

	@Override
	public void handleDataChange(String path, Object value) throws Exception {
		if (log.isInfoEnabled()) {
			log.info("PizzaResource<" + path + "> changed.");
		}
		String content = (String) value;
		if (content != null && content.length() > 0) {
			listener.handleConfigChange(content);
		}
	}

	@Override
	public void handleDataDeleted(String path) throws Exception {
		if (log.isWarnEnabled()) {
			log.warn("PizzaResource<" + path + "> be deleted.");
		}
	}

	public AllergoListener getAllergoListener() {
		return listener;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof AllergoZkDataListener)) {
			return false;
		}
		AllergoZkDataListener obj1 = (AllergoZkDataListener) obj;
		return listener.equals(obj1.listener);
	}

}
