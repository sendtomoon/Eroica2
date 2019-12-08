package com.sendtomoon.eroica.common.web.util;

import java.nio.charset.Charset;

import javax.servlet.ServletRequest;

import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

public class ContentTypeUtil {

	public MediaType getMediaType(ServletRequest request) {
		String contentType = request.getContentType();
		if (!StringUtils.hasText(contentType)) {
			contentType = "*";
		}
		if (StringUtils.hasText(contentType)) {
			return MediaType.parseMediaType(contentType);
		}
		return null;
	}

	public String getCharset(ServletRequest request, String defaultCharset) {
		MediaType mediaType = getMediaType(request);
		if (mediaType != null) {
			Charset charSet = mediaType.getCharset();
			if (charSet != null) {
				return charSet.displayName();
			}
		}
		return defaultCharset;
	}
}
