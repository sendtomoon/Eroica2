package com.sendtomoon.eroica.common.web.util;

import java.io.IOException;
import java.io.Writer;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sendtomoon.eroica.common.web.WebException;

public class JsonpSupportInterceptor extends HandlerInterceptorAdapter
		implements InitializingBean {

	private final Log logger = LogFactory.getLog(this.getClass());

	private String callbackParameterName = "callback";

	private boolean enable = true;

	private String defaultPattern = "^\\w+$";

	private Pattern pattern = null;

	public void setDefaultPattern(String defaultPattern) {
		this.defaultPattern = defaultPattern;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.pattern = Pattern.compile(defaultPattern);
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String params = null;
		if (enable && (params=request.getParameter(callbackParameterName)) != null 
				&& (params = params.trim()).length() > 0) {
			// 防止跨网站脚本的攻击
			if (pattern.matcher(params).matches()) {
				Writer out = response.getWriter();
				out.write(outputfilter(params));
				out.write('(');
			} else {
				logger
						.error("*****************jsonp callback function name must be matched by [^\\w+$]********************");
			}
		}
		return true;
	}
	
	public static String outputfilter(String value) {
		if (value == null) {
			return null;
		}
		StringBuffer result = new StringBuffer(value.length());
		for (int i = 0; i < value.length(); ++i) {
			switch (value.charAt(i)) {
			case '<':
				result.append("&lt;");
				break;
			case '>':
				result.append("&gt;");
				break;
			case '"':
				result.append("&quot;");
				break;
			case '\'':
				result.append("&#39;");
				break;
			case '%':
				result.append("&#37;");
				break;
			case ';':
				result.append("&#59;");
				break;
			case '(':
				result.append("&#40;");
				break;
			case ')':
				result.append("&#41;");
				break;
			case '&':
				result.append("&amp;");
				break;
			case '+':
				result.append("&#43;");
				break;
			default:
				result.append(value.charAt(i));
				break;
			}
		}
		return result.toString();
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		String params = null;
		if (enable && (params=request.getParameter(callbackParameterName)) != null  && (params = params.trim()).length() > 0) {
			try {
				Writer out = response.getWriter();
				out.write(");");
				out.flush();
			} catch (IOException e) {
				throw new WebException(e.getMessage(), e);
			}
		}
	}

	public String getCallbackParameterName() {
		return callbackParameterName;
	}

	public void setCallbackParameterName(String callbackParameterName) {
		this.callbackParameterName = callbackParameterName;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

}
