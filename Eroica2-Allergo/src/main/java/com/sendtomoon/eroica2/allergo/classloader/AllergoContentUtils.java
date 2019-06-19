package com.sendtomoon.eroica2.allergo.classloader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;

import com.sendtomoon.eroica2.allergo.AllergoConstants;
import com.sendtomoon.eroica2.allergo.AllergoException;
import com.sendtomoon.eroica2.allergo.AllergoManager;

public class AllergoContentUtils {

	static InputStream toIputStream(AllergoManager mananger, AllergoURL allergoURL) {
		return toIputStream(mananger, allergoURL, true);
	}

	static String getTextContent(AllergoManager mananger, AllergoURL allergoURL, boolean requriedExists) {
		if (allergoURL.isBase64Content()) {
			throw new AllergoException("Allergo Resource [" + allergoURL + "] not be text content.");
		}
		String content = mananger.get(allergoURL.toAllergoPath());
		if (content == null) {
			if (requriedExists) {
				throw new AllergoException(
						"Allergo resource [" + allergoURL.toString() + "] cannot be opened because it does not exist");
			} else {
				return null;
			}
		}

		return content;
	}

	static InputStream toIputStream(AllergoManager mananger, AllergoURL allergoURL, boolean requriedExists) {
		String content = mananger.get(allergoURL.toAllergoPath());
		if (content == null) {
			if (requriedExists) {
				throw new AllergoException(
						"Allergo resource [" + allergoURL.toString() + "] cannot be opened because it does not exist");
			} else {
				return null;
			}
		}
		return toIputStream(allergoURL, content);
	}

	public static InputStream toIputStream(AllergoURL allergoURL, final String allergoContent) {
		if (allergoContent == null) {
			throw new AllergoException("Allergo resource [" + allergoURL.toString() + "] error,content be empty.");
		}
		if (AllergoConstants.GROUP_RESOURCES.equals(allergoURL.getAllergoGroup())) {
			try {
				ClasspathResourceContent content = ClasspathResourceContent.fromJSONString(allergoContent);
				byte[] fileContent = content.getBase64datas().getBytes();
				fileContent = Base64.decodeBase64(fileContent);
				return new ByteArrayInputStream(fileContent);
			} catch (Throwable th) {
				throw new AllergoException(
						"AllergoResource@" + allergoURL + " content format error,cause:" + th.getMessage(), th);
			}
		} else if (allergoURL.isBase64Content()) {
			try {
				byte[] fileContent = allergoContent.getBytes();
				fileContent = Base64.decodeBase64(fileContent);
				return new ByteArrayInputStream(fileContent);
			} catch (Throwable th) {
				throw new AllergoException(
						"AllergoResource@" + allergoURL + " decodeBase64 failure,cause:" + th.getMessage(), th);
			}
		} else {
			return new ByteArrayInputStream(allergoContent.getBytes(allergoURL.getCharset()));
		}
	}
}
