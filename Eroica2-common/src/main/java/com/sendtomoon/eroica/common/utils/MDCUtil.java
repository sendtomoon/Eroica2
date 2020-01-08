package com.sendtomoon.eroica.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.NDC;

/**
 * MDC Util
 */
public class MDCUtil {

	@SuppressWarnings("rawtypes")
	public static Map peekMap() {
		return peek().getDatas();
	}

	public static MDCData getDatas() {
		return peek();
	}

	public static MDCData peek() {
		return new MDCData(NDC.peek());
	}

	@SuppressWarnings("rawtypes")
	public static void set(Map mapDatas) {
		if (mapDatas != null) {
			new MDCData(mapDatas);
		} else {
			clear();
		}
	}

	public static void set() {
		set(null, null);
	}

	public static void set(String requestId) {
		set(requestId, null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void set(String requestId, String uid) {
		Map mapDatas = new HashMap();
		if (requestId == null) {
			requestId = generateRequestId();
		}
		mapDatas.put(MDCData.KEY_REQUEST_ID, requestId);
		if (uid != null) {
			mapDatas.put(MDCData.KEY_UID, uid);
		}
		new MDCData(mapDatas);
	}

	public static String generateRequestId() {
		return UUID.randomUUID().toString();
	}

	public static void clear() {
		NDC.clear();
	}

}
