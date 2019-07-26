package com.sendtomoon.eroica2.allergo.log4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.sendtomoon.eroica2.allergo.AllergoConstants;
import com.sendtomoon.eroica2.allergo.EroicaContext;
import com.sendtomoon.eroica2.allergo.AllergoException;
import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;

public class Log4j2InitializationBean implements Log4jInitialization {

	private static final String KEY_SUFFIX = ".log4j.xml";

	private EroicaContext eroicaContext;

	public Log4j2InitializationBean() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		AllergoURL allergoURL = this.getAllergoURL();
		allergoURL.setManager(eroicaContext.getDefaultManager());
		processConfig(this.getAllergoURL(), allergoURL.getInputStream(false));
	}

	@Override
	public void setAllergoContext(EroicaContext eroicaContext) {
		this.eroicaContext = eroicaContext;
	}

	protected void processConfig(AllergoURL allergoURL, InputStream content) {
		if (content != null) {
			File localFile = Log4j2Utils.resolveLocalConfigureFile();
			try {
				FileUtils.writeByteArrayToFile(localFile, IOUtils.toByteArray(content), false);
			} catch (IOException e) {
				throw new AllergoException("Write file:" + localFile + " error,cause:" + e.getMessage(), e);
			}
			try {
				Log4j2Utils.reconfigure(localFile.toURI());
			} catch (Exception ex) {
				throw new AllergoException("Reconfigure log4j error,cause:" + ex.getMessage(), ex);
			}
		}
	}

	@Override
	public void onChanged(AllergoURL allergoURL, InputStream content) {
		processConfig(allergoURL, content);
	}

	@Override
	public AllergoURL getAllergoURL() {
		return AllergoURL.valueOf("/" + AllergoConstants.GROUP_EOAPP + "/" + eroicaContext.getAppName() + KEY_SUFFIX);
	}

	@Override
	public boolean isListenEnable() {
		return true;
	}

}
