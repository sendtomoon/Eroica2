package com.sendtomoon.eroica.common.exception;

import java.net.SocketTimeoutException;
import java.rmi.ConnectException;
import java.rmi.ConnectIOException;
import java.rmi.NoSuchObjectException;
import java.rmi.StubNotFoundException;
import java.rmi.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.alibaba.dubbo.remoting.TimeoutException;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.sendtomoon.eroica.common.validator.ValidateFailException;

public class ResponseCodeException extends RuntimeException {

	/** 系统错误 */
	public static String ERROR_UNKNOW = "900101";

	/** 系统繁忙 */
	public static String ERROR_SYSTEM_BUZY = "900102";

	/** 参数验证失败 */
	public static String ERROR_VALIDATE_FAIL = "900103";

	/** 请求超时 */
	public static String ERROR_REQUEST_TIMEOUT = "900104";

	/** 远程连接失败或超时 */
	public static String ERROR_CONNECT_FAIL = "900106";

	/** 远程调用错误 */
	public static String ERROR_REMOTE_EXCEPTION = "900107";

	/** 安全检查失败 */
	public static String ERROR_SECURITY_CHECK_FAIL = "900105";

	/** 未登陆或登陆超时 */
	public static String ERROR_NOT_LOGINED = "900106";

	/** ESA服务不存在 */
	public static String ERROR_ESA_NOT_EXISTS = "900900";

	/** SAR配置错误 */
	public static String ERROR_SAR_CONFIG_ERROR = "900200";

	/** Eroica配置错误 */
	public static String ERROR_APP_CONFIG_ERROR = "900201";

	private static final long serialVersionUID = 1L;

	private String responseCode;

	private String responseMsg;

	private Object[] args;

	public ResponseCodeException() {
		this.setResponseCode(null);
	}

	public ResponseCodeException(String responseCode) {
		this.setResponseCode(responseCode);
	}

	public ResponseCodeException(String responseCode, Throwable cause) {
		super(cause);
		this.setResponseCode(responseCode);
	}

	public ResponseCodeException(String responseCode, String responseMsg) {
		this.setResponseCode(responseCode);
		this.responseMsg = responseMsg;
	}

	public ResponseCodeException(String responseCode, String responseMsg, Throwable cause) {
		super(cause);
		this.setResponseCode(responseCode);
		this.responseMsg = responseMsg;
	}

	public ResponseCodeException(String responseCode, Object[] args, Throwable cause) {
		super(cause);
		this.setResponseCode(responseCode);
		this.args = args;
	}

	public ResponseCodeException(String responseCode, Object[] args) {
		this.setResponseCode(responseCode);
		this.args = args;
	}

	/*
	 * public ResponseCodeException(String responseCode,String responseMsg,Object[]
	 * args){ this.setResponseCode(responseCode); this.args=args;
	 * this.responseMsg=responseMsg; }
	 * 
	 * public ResponseCodeException(String responseCode,String responseMsg,Object[]
	 * args,Throwable cause){ super(cause); this.setResponseCode(responseCode);
	 * this.args=args; this.responseMsg=responseMsg; }
	 */

	protected String getDefaultResponseCode() {
		return ResponseCodeException.ERROR_UNKNOW;
	}

	public String resolveMessage(MessageSource messageSource) {
		return this.resolveMessage(messageSource);
	}

	public String resolveMessage(MessageSource messageSource, Locale locale) {
		String retMsg = this.getMessage();
		if (retMsg != null && retMsg.length() > 0) {
			return retMsg;
		}
		if (messageSource != null) {
			if (locale == null) {
				locale = Locale.CHINA;
			}
			retMsg = messageSource.getMessage(this.getResponseCode(), this.getArgs(), "", locale);
			return (retMsg == null || retMsg.length() == 0 ? null : retMsg);
		}
		return null;
	}

	@Override
	public String getMessage() {
		return this.getResponseMsg();
	}

	@Override
	public String getLocalizedMessage() {
		return this.getResponseMsg();
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode == null ? this.getDefaultResponseCode() : responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	/// --------------------------------------------------------------

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("responseCode:");
		sb.append(this.responseCode);
		if (this.responseMsg != null) {
			sb.append(",responseMsg:");
			sb.append(this.responseMsg);
		}
		sb.append(",args:");
		sb.append(this.args == null ? "null" : Arrays.asList(this.args));
		sb.append("\n").append(super.toString());
		return sb.toString();
	}

	public static ResponseCodeException toResponseCodeException(Throwable ex) {
		Throwable temp = ex;
		Throwable finalCause = ex;
		ResponseCodeException result = null;
		while (temp != null) {
			if (temp instanceof ResponseCodeException) {
				result = (ResponseCodeException) temp;
				break;
			} else {
				finalCause = temp;
				temp = temp.getCause();
			}
		}
		if (result == null) {
			if (finalCause instanceof java.rmi.RemoteException) {
				if (isConnectFailure((java.rmi.RemoteException) finalCause)) {
					result = new ResponseCodeException(ResponseCodeException.ERROR_CONNECT_FAIL, finalCause);
				} else {
					result = new ResponseCodeException(ResponseCodeException.ERROR_REMOTE_EXCEPTION, finalCause);
				}
			} else if (finalCause instanceof java.net.ConnectException) {
				result = new ResponseCodeException(ResponseCodeException.ERROR_CONNECT_FAIL, finalCause);
			} else if (isTimeoutException(finalCause)) {
				result = new ResponseCodeException(ResponseCodeException.ERROR_REQUEST_TIMEOUT, finalCause);
			} else if (finalCause instanceof ValidateFailException) {
				result = forValidateFailException(((ValidateFailException) finalCause).getErrors(), finalCause);
			} else if (finalCause instanceof BindException) {
				result = forValidateFailException(((BindException) finalCause).getBindingResult(), finalCause);
			} else {
				result = new ResponseCodeException(ResponseCodeException.ERROR_UNKNOW, finalCause);
			}
		}

		return result;
	}

	private final static Map<String, String> VALIDATE_FAILURE_CODE_MAP = new HashMap<String, String>();

	/***
	 * 900500={0}不符合校验规则 900501={0}不符合长度规则 900502={0}不符合数值规则 900503={0}不符合枚举规则
	 * 900504={0}不能为空 900505={0}不符合日期规则
	 */
	static {
		VALIDATE_FAILURE_CODE_MAP.put("Regex", "900500");
		VALIDATE_FAILURE_CODE_MAP.put("Length", "900501");
		VALIDATE_FAILURE_CODE_MAP.put("Number", "900502");
		VALIDATE_FAILURE_CODE_MAP.put("Enum", "900503");
		VALIDATE_FAILURE_CODE_MAP.put("Empty", "900504");
		VALIDATE_FAILURE_CODE_MAP.put("Date", "900505");
	}

	protected static ResponseCodeException forValidateFailException(Errors errors, Throwable ex) {

		List<FieldError> list = errors.getFieldErrors();
		if (list != null && list.size() != 0) {
			FieldError fe = list.get(0);
			String code = VALIDATE_FAILURE_CODE_MAP.get(fe.getCode());
			if (code == null || code.length() == 0) {
				code = ResponseCodeException.ERROR_VALIDATE_FAIL;
			}
			String msg = fe.getDefaultMessage();
			if (msg == null || msg.length() == 0) {
				List<Object> argArray = new ArrayList<Object>();
				argArray.add(fe.getField());
				argArray.add(fe.getRejectedValue());
				return new ResponseCodeException(code, argArray.toArray(), ex);
			} else {
				return new ResponseCodeException(code, msg, ex);
			}
		}
		return new ResponseCodeException(ResponseCodeException.ERROR_VALIDATE_FAIL, ex);
	}

	protected static boolean isTimeoutException(Throwable temp) {
		return temp instanceof SocketTimeoutException || temp instanceof TimeoutException;
	}

	protected static boolean isConnectFailure(Throwable temp) {
		return temp instanceof ConnectException || temp instanceof ConnectIOException
				|| temp instanceof UnknownHostException || temp instanceof NoSuchObjectException
				|| temp instanceof StubNotFoundException;
	}

}
