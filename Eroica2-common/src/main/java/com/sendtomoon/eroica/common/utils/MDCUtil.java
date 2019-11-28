package com.sendtomoon.eroica.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.NDC;

import com.sendtomoon.eroica.common.app.dto.SessionDTO;

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

	public static SessionDTO peekSessionDTO() {
		return peekSessionDTO(null);
	}

	public static SessionDTO peekSessionDTO(SessionDTO s) {
		if (s == null) {
			s = new SessionDTO();
		}
		MDCData d = peek();
		if (d != null) {
			if (s.getTxnId() == null || s.getTxnId().length() == 0) {
				s.setTxnId(d.getRequestId());
			}
			if (s.getUserId() == null || s.getUserId().length() == 0) {
				s.setUserId(d.getUid());
			}
		}
		return s;
	}

	public static void setBySessionDTO(SessionDTO s) {
		if (s != null && s.getTxnId() != null) {
			MDCUtil.set(s.getTxnId(), s.getUserId());
		} else {
			MDCData data = MDCUtil.peek();
			if (data.getRequestId() == null) {
				data.setRequestId(generateRequestId());
			}
		}
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
