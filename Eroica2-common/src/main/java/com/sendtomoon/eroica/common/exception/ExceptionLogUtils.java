package com.sendtomoon.eroica.common.exception;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;

public class ExceptionLogUtils {

	/** 记录异常日志 */
	public void logResponseCodeException(ResponseCodeException codeEx, HttpServletRequest request, Log logger) {
		if (logger.isErrorEnabled()) {
			if (request != null) {// 记录发生错误时，请求中的信息
				HttpSession session = request.getSession(false);
				// -----------------------------
				String qs = request.getQueryString();
				logger.error("URI=" + request.getRequestURI() + (qs == null ? "" : ("?" + qs)));
				logger.error("IP=" + request.getRemoteAddr());
				if (session != null) {
					logger.error("SessionId=" + session.getId());
				}
				Enumeration headerNames = request.getHeaderNames();
				while (headerNames.hasMoreElements()) {
					String hn = (String) headerNames.nextElement();
					logger.error(hn + "=" + request.getHeader(hn));
				}
			}
			// 记录异常信息
			Throwable causeEx = codeEx.getCause();
			String msg = codeEx.getMessage();
			String code = codeEx.getResponseCode();
			logger.error("" + (code != null ? "ErrorResponse：[" + code + "]" : "") + (msg != null ? msg : ""),
					causeEx == null ? codeEx : causeEx);
		}
	}

}
