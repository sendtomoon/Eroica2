package com.sendtomoon.eroica.common.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

	/******* 日期常用格式化 ***********/

	/** 长日期: yyyy-MM-dd HH:mm:ss */
	public static String DATE_FMT_LONG = "yyyy-MM-dd HH:mm:ss";

	public static String DATE_FMT_LONG_EN = "yyyyMMddHHmmss";

	/** 中文长日期: yyyy年MM月dd日 HH:mm:ss */
	public static String DATE_FMT_LONG_CN = "yyyy年MM月dd日 HH:mm:ss";

	/** 中文普通日期: yyyy年MM月dd日 HH:mm */
	public static String DATE_FMT_NORMAL_CN = "yyyy年MM月dd日 HH:mm";
	/** 中文普通日期: yyyy年M月d日 HH:mm */
	public static String DATE_FMT_NORMAL_CN_1 = "yyyy年M月d日 HH:mm";

	public static String DATE_FMT_LONG_HMSS = "yyyyMMddHHmmssSSS";

	/** 短日期: yyyy-MM-dd */
	public static String DATE_FMT_SHORT = "yyyy-MM-dd";

	/** 短日期: yyyyMMdd */
	public static String DATE_FMT_SHORT_8 = "yyyyMMdd";

	/** 中文短日期: yyyy年MM月dd日 */
	public static String DATE_FMT_SHORT_CN = "yyyy年MM月dd日";
	/** 中文短日期: yyyy.MM.dd */
	public static String DATE_FMT_SHORTD_CN = "yyyy.MM.dd";
	/** 中文短日期 : yyyy年MM月 */
	public static String DATE_FMT_MONTH_CN = "yyyy年MM月";

	public static String DATE_FMT_HM = "HHmm";

	public static boolean nowIsBetween(String sTime, String eTime, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String now = formatter.format(new Date());
		return now.compareTo(sTime) > 0 && now.compareTo(eTime) < 0;
	}

	/**
	 * Date转字符串
	 * 
	 * @param date   , 默认今天
	 * @param format
	 * @return String
	 */
	public static String date2Str(Date date, String format) {
		if (date == null) {
			date = new Date();
		}
		if (format == null || "".equals(format)) {
			format = DATE_FMT_LONG;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = formatter.format(date);
		return dateString;
	}

	/**
	 * 字符串转Date
	 * 
	 * @param strDate
	 * @param format
	 * @return Date
	 */
	public static Date str2Date(String strDate, String format) {
		if (strDate == null) {
			return null;
		} else {
			if (format == null || "".equals(format)) {
				format = DATE_FMT_LONG;
			}
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			ParsePosition pos = new ParsePosition(0);
			Date strtodate = formatter.parse(strDate, pos);
			return strtodate;
		}

	}

	public static Date getLastDate(Date date, long day) {
		long date_3_hm = date.getTime() - 3600000 * 24 * day;
		Date date_3_hm_date = new Date(date_3_hm);
		return date_3_hm_date;
	}

	public static Date getDelayTime(Date lastTime, String delaySencod) {
		Date date = null;
		if (lastTime == null) {
			lastTime = new Date();
		}
		if (delaySencod == null || "".equals(delaySencod) || delaySencod.startsWith("0")) {
			delaySencod = "60";
		}
		try {
			long Time = (lastTime.getTime() / 1000) + Integer.parseInt(delaySencod);
			lastTime.setTime(Time * 1000);
			date = lastTime;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date getDelayDay(Date lastTime) {
		return getDelayDay(lastTime, null);
	}

	public static Date getDelayDay(Date lastTime, String delay) {
		if (lastTime == null) {
			lastTime = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(lastTime); // 设置时间
		Date currentDate = c.getTime();
		Date date = null;

		if (delay == null || "".equals(delay) || delay.startsWith("0")) {
			delay = "1";
		}
		try {
			long Time = (currentDate.getTime() / 1000) + Integer.parseInt(delay) * 24 * 60 * 60;
			currentDate.setTime(Time * 1000);
			date = currentDate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String getNextDay(String nowdate, String delay) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(DATE_FMT_SHORT);
			String mdate = "";
			Date d = str2Date(nowdate, DATE_FMT_SHORT);
			long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24 * 60 * 60;
			d.setTime(myTime * 1000);
			mdate = format.format(d);
			return mdate;
		} catch (Exception e) {
			return "";
		}
	}

	public static String getDay(String nowdate, String delay, String formatStr) {
		try {
			if (formatStr == null || "".equals(formatStr)) {
				formatStr = DATE_FMT_SHORT;
			}
			SimpleDateFormat format = new SimpleDateFormat(formatStr);
			String mdate = "";
			Date d = null;
			if (nowdate == null || "".equals(nowdate)) {
				d = new Date();
			} else {
				d = str2Date(nowdate, formatStr);
			}
			long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24 * 60 * 60;
			d.setTime(myTime * 1000);
			mdate = format.format(d);
			return mdate;
		} catch (Exception e) {
			return "";
		}
	}

	public static Date getDate(String date, String format, int days) {

		Date _date = str2Date(date, format);
		Calendar c = Calendar.getInstance();
		c.setTime(_date); // 设置日期
		c.add(Calendar.DATE, days); // 日期分钟加1,Calendar.DATE(天),Calendar.HOUR(小时)
		return c.getTime(); // 结果
	}

	public static String getWeek(String strDate, String num) {

		Date dd = str2Date(strDate, DATE_FMT_SHORT);
		Calendar c = Calendar.getInstance();
		c.setTime(dd);
		if (num.equals("1"))
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		else if (num.equals("2"))
			c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		else if (num.equals("3"))
			c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		else if (num.equals("4"))
			c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		else if (num.equals("5"))
			c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		else if (num.equals("6"))
			c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		else if (num.equals("0"))
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return new SimpleDateFormat(DATE_FMT_SHORT).format(c.getTime());
	}

	public static String getWeekDay() {
		String weekDay = null;
		Calendar c = Calendar.getInstance();
		switch (c.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			weekDay = "星期日";
			break;
		case Calendar.MONDAY:
			weekDay = "星期一";
			break;
		case Calendar.TUESDAY:
			weekDay = "星期二";
			break;
		case Calendar.WEDNESDAY:
			weekDay = "星期三";
			break;
		case Calendar.THURSDAY:
			weekDay = "星期四";
			break;
		case Calendar.FRIDAY:
			weekDay = "星期五";
			break;
		case Calendar.SATURDAY:
			weekDay = "星期六";
			break;
		}
		return weekDay;
	}

	public static String getWeek(String sdate) {

		Date date = str2Date(sdate, DATE_FMT_SHORT);
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return new SimpleDateFormat("EEEE").format(c.getTime());
	}

	public static int compare(String format, Date date, Date todate) {
		String datestr = date2Str(date, format);
		String todatestr = date2Str(todate, format);
		date = str2Date(datestr, format);
		todate = str2Date(todatestr, format);
		return date.compareTo(todate);
	}

	public static String getNowTimes() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FMT_LONG);
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getNowTimes(String format) {
		if (format == null || "".equals(format)) {
			format = DATE_FMT_LONG;
		}
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static Date parseDate(String date) {
		SimpleDateFormat df = new SimpleDateFormat();
		Date rtnDate = null;
		if (date == null || date.trim().equals("") || date.trim().equals("null")) {
			return rtnDate;
		}
		try {
			date = date.trim();
			int length = date.length();
			if (date.indexOf("-") != -1) {
				if (length == 5) {
					if (date.indexOf("-") == length - 1) {// 2015-
						df.applyPattern("yyyy");
						date = date.substring(0, 4);
						rtnDate = df.parse(date);
					} else {
						df.applyPattern("yyyy-MM");// 2015-01
						rtnDate = df.parse(date);
					}
				} else if (length >= 6 && length <= 7) {// 2015-1 -- 2015-01
					df.applyPattern("yyyy-MM");
					rtnDate = df.parse(date);
				} else if (length >= 8 && length <= 9) {
					if (date.lastIndexOf("-") == length - 1) { // 2015-12-
						df.applyPattern("yyyy-MM");
						date = date.substring(0, length - 1);
						rtnDate = df.parse(date);
					} else {
						df.applyPattern("yyyy-MM-dd");// 2015-1-1 --
														// 2015-01-01
						rtnDate = df.parse(date);
					}
				} else if (length >= 10 && length <= 11) {
					if (date.indexOf(" ") > -1 && date.indexOf(" ") < length - 1) {
						df.applyPattern("yyyy-MM-dd HH");// 2015-1-1 1 --
															// 2015-1-1 11 中间有空格
						rtnDate = df.parse(date);
					} else {
						df.applyPattern("yyyy-MM-dd");// "2015-01-01"中间无空格
						rtnDate = df.parse(date);
					}
				} else if (length >= 12 && length <= 13) {
					if (date.indexOf(":") > -1 && date.indexOf(":") < length - 1) {
						df.applyPattern("yyyy-MM-dd HH:mm");// 2015-1-1 1:1 --
															// 2015-1-1 1:01
															// 中间有冒号
						rtnDate = df.parse(date);
					} else {
						df.applyPattern("yyyy-MM-dd HH");// 2015-01-01 01
															// 中间有空格
						rtnDate = df.parse(date);
					}
				} else if (length >= 14 && length <= 16) {
					int lastIndex = date.lastIndexOf(":");
					if (date.indexOf(":") > -1 && lastIndex < length - 1 && date.indexOf(":") != lastIndex) {
						df.applyPattern("yyyy-MM-dd HH:mm:ss");// 2015-1-1
																// 1:1:1 --
																// 2015-01-01
																// 1:1:1 中间有两个冒号
						if (lastIndex < length - 1 - 2) {
							date = date.substring(0, lastIndex + 3);
						}
						rtnDate = df.parse(date);
					} else if (date.indexOf(":") > -1 && lastIndex < length - 1 && date.indexOf(":") == lastIndex) {
						df.applyPattern("yyyy-MM-dd HH:mm");// 2015-01-01 1:1 --
															// 2015-01-01
															// 01:01中间只有一个冒号
						rtnDate = df.parse(date);
					} else if (date.indexOf(":") > -1 && lastIndex == length - 1 && date.indexOf(":") == lastIndex) {
						df.applyPattern("yyyy-MM-dd HH");// 2015-01-01 01:
															// 只有一个冒号在末尾
						date = date.substring(0, length - 1);
						rtnDate = df.parse(date);
					}
				} else if (length == 17) {
					int lastIndex = date.lastIndexOf(":");
					if (lastIndex < length - 1) {
						df.applyPattern("yyyy-MM-dd HH:mm:ss");// 2015-1-1
																// 1:1:1 --
																// 2015-01-01
																// 1:1:1 中间有两个冒号
						if (lastIndex < length - 1 - 2) {
							date = date.substring(0, lastIndex + 3);
						}
						rtnDate = df.parse(date);
					} else if (lastIndex == length - 1) {
						df.applyPattern("yyyy-MM-dd HH:mm");// 2015-01-01 1:1 --
															// 2015-01-01
															// 01:01中间只有一个冒号
						date = date.substring(0, length - 1);
						rtnDate = df.parse(date);
					}
				} else if (length >= 18) {
					df.applyPattern("yyyy-MM-dd HH:mm:ss");// 2015-1-1 1:1:1 --
															// 2015-01-01
															// 01:01:01 有两个冒号
					int lastIndex = date.lastIndexOf(":");
					if (lastIndex < length - 1 - 2) {
						date = date.substring(0, lastIndex + 3);
					}
					rtnDate = df.parse(date);
				}
			} else if (length == 4) {
				df.applyPattern("yyyy");
				rtnDate = df.parse(date);
			} else if (length >= 5 && length <= 6) {
				df.applyPattern("yyyyMM");
				rtnDate = df.parse(date);
			} else if (length >= 7 && length <= 8) {
				df.applyPattern("yyyyMMdd");
				rtnDate = df.parse(date);
			} else if (length >= 9 && length <= 10) {
				df.applyPattern("yyyyMMddHH");
				rtnDate = df.parse(date);
			} else if (length >= 11 && length <= 12) {
				df.applyPattern("yyyyMMddHHmm");
				rtnDate = df.parse(date);
			} else if (length >= 13 && length <= 14) {
				df.applyPattern("yyyyMMddHHmmss");
				rtnDate = df.parse(date);
			} else if (length >= 15) {
				df.applyPattern("yyyyMMddHHmmss");
				date = date.substring(0, 14);
				rtnDate = df.parse(date);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return rtnDate;

	}

	/**
	 * date日期之前day天的日期 Description:
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date getPreDate(Date date, int day) {
		if (date == null) {
			return null;
		}
		long time = date.getTime();
		time -= 86400000L * day;
		return new Date(time);
	}

	public static Date addMinutes(Date date, int amount) {
		return add(date, 12, amount);
	}

	public static Date add(Date date, int calendarField, int amount) {
		if (date == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(calendarField, amount);
		return c.getTime();
	}

	/**
	 * 描述：日期格式化
	 * 
	 * @param date    日期
	 * @param pattern 格式化类型
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}

	public static String formatDate(long timestamp, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(timestamp);
	}

	public static void main(String[] args) {
		String vrCheckTime = "0800,2004";
		boolean b = DateHelper.nowIsBetween(vrCheckTime.split(",")[0], vrCheckTime.split(",")[1], DATE_FMT_HM);
		System.out.println(b);
	}

}
