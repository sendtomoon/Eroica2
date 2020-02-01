package com.sendtomoon.eroica.common.web.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
/***
 * 车险异常日志记录器
 */
public class ExceptionLogUtils {

	/**记录异常日志*/
	@SuppressWarnings("unchecked")
	public  void log(Throwable ex,HttpServletRequest request,Log logger){
		if(logger.isErrorEnabled()){
			//加“#”号，用于快速定位错误日志
			logger.error("####---------------------------------------###");
			if(request!=null){//记录发生错误时，请求中的信息
				HttpSession session=request.getSession(false);
				//-----------------------------
				String qs=request.getQueryString();
				logger.error("URI="+request.getRequestURI()+(qs==null?"":("?"+qs)));
				logger.error("IP="+request.getRemoteAddr());
				if(session!=null){
					logger.error("SessionId="+session.getId());
				}
				Enumeration headerNames=request.getHeaderNames();
				while(headerNames.hasMoreElements()){
					String hn=(String)headerNames.nextElement();
					logger.error(hn+"="+request.getHeader(hn));
				}
			}
			//记录异常信息
			Throwable th=ex;
			//th=getCause(ex);
			logger.error(th.getMessage(),th);
		}
	}
	
	@SuppressWarnings("unused")
	protected  Throwable getCause(Throwable th){
		if(th!=null && th.getCause()!=null){
			return getCause(th.getCause());
		}
		return th;
	}
	
}
