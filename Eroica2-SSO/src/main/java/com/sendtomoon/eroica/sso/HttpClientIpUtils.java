package com.sendtomoon.eroica.sso;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;

public class HttpClientIpUtils {

	/**
	 * 获取客户端IP
	 * 
	 * @param request
	 * @return 客户端IP地址
	 * 
	 */
	public static String getIp(HttpServletRequest request, Log log) {
		// 打印出所有header内容帮助判断
		Enumeration<String> headerNames = request.getHeaderNames();
		Map<String, String> headerMap = new HashMap<String, String>();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			headerMap.put(key, value);
		}
		log.info("*****************HEADER*****************");
		log.info("requestHeader：" + headerMap.toString());
		log.info("*****************HEADER*****************");
		String ip = request.getHeader("x-forwarded-for");
		if (ip != null && ip.length() != 0 && log.isInfoEnabled()) {
			log.info("Got ip=" + ip + " from header[x-forwarded-for]");
		}
		// 通过http_X-Forwarded-For获取用户真实ip
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("http_X-Forwarded-For");
			if (ip != null && ip.length() != 0 && log.isInfoEnabled()) {
				log.info("Got ip=" + ip + " from header[http_X-Forwarded-For]");
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			if (ip != null && ip.length() != 0 && log.isInfoEnabled()) {
				log.info("Got ip=" + ip + " from header[Proxy-Client-IP]");
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			if (ip != null && ip.length() != 0 && log.isInfoEnabled()) {
				log.info("Got ip=" + ip + " from header[WL-Proxy-Client-IP]");
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if (log.isInfoEnabled()) {
				log.info("Got ip=" + ip);
			}
		}
		ip = getIp(ip);
		return ip;
	}

	private static String getIp(String ip) {
		// 多层反向代理，有可能取到的ip类似这样：27.106.131.246, 127.0.0.1, 61.135.165.46
		if (ip != null && ip.indexOf(',') > 0) {
			String[] ipArray = ip.split(",");
			// log.info("ipArray=" + ip); // 多层反向代理ip地址,取最后一个ip返回
			ip = ipArray[ipArray.length - 1].trim();
		}
		return ip;
	}

}
