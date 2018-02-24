package com.xincheng.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class DateUtils {
	public static final String PATTERN_FULL = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_YYMMDD = "yyyy-MM-dd";
	public static final String PATTERN_YYYYMM = "yyyy-MM";
	public static final String PATTERN_MIN = " 00:00:00";
	public static final String PATTERN_MAX = " 23:59:59";
	public static final DateFormat SIMPLE_DATA_FORMAT = new SimpleDateFormat(PATTERN_YYMMDD);

	/**
	 * 将时分秒字符串格式成当前日期
	 * 
	 * @param time
	 *            时-分-秒
	 * @param isMin
	 *            true 最小 false 最大
	 * @return
	 */
	public static Timestamp formatStringToTimestamp(String time, Boolean isMin) {
		if (StringUtils.isBlank(time)) {
			return new Timestamp(new Date().getTime());
		}
		String parseTime = time + (isMin ? PATTERN_MIN : PATTERN_MAX);
		try {
			Date parseDate = new SimpleDateFormat(PATTERN_FULL).parse(parseTime);
			return new Timestamp(parseDate.getTime());
		} catch (ParseException e) {
			return null;
		}
	}

	public static String formatDate(String format) {
		String returnstr = "";
		if (StringUtils.isBlank(format)) {
			returnstr = SIMPLE_DATA_FORMAT.format(new Date());
		} else {
			returnstr = new SimpleDateFormat(format).format(new Date());
		}
		return returnstr;
	}

	/**
	 * 转换Thu Jun 06 1985 00:00:00 GMT+0800 (中国标准时间)格式的时间
	 * 
	 * @param source
	 * @param format
	 * @return String
	 */
	@SuppressWarnings("deprecation")
	public static String formatGMTtimeToStr(String source, String format) {
		SimpleDateFormat dataFormat = new SimpleDateFormat(format);
		return dataFormat.format(Date.parse(source));
	}

	public static Date formatStrToDate(String date, String format) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		try {
			if (StringUtils.isBlank(format)) {
				return SIMPLE_DATA_FORMAT.parse(date);
			}
			return new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 取得指定日期所在周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return c.getTime();
	}

	/**
	 * 取得指定日期所在周的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		return c.getTime();
	}

	/**
	 * 取得指定日期所在月的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfMonth(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	/**
	 * 取得指定日期所在月的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.DAY_OF_MONTH, -1);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}

	/**
	 * 获取当月的最大天数
	 * 
	 * @param date
	 * @return int
	 */
	public static int getDayOfMonth(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		return c.getActualMaximum(Calendar.DATE);
	}

	/**
	 * 将日期型数据转化为字符串型
	 * 
	 * @param formatDate
	 * @param pattern
	 * @return
	 */
	public static String formatDateToStr(Date formatDate, String pattern) {
		if (null == formatDate) {
			return null;
		}
		if (pattern == null || pattern.equals("")) {
			pattern = PATTERN_FULL;
		}
		DateFormat format = null;
		format = new SimpleDateFormat(pattern);
		String dayBefore = format.format(formatDate.getTime());
		return dayBefore;
	}

	public static Date formatDateToDate(Date formatDate, boolean isMax) {
		if (null == formatDate) {
			return null;
		}
		Calendar c = new GregorianCalendar();
		c.setTime(formatDate);
		if (isMax) {
			c.set(Calendar.SECOND, 59);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.HOUR, 23);
			return c.getTime();
		}
		c.set(Calendar.SECOND, 00);
		c.set(Calendar.MINUTE, 00);
		c.set(Calendar.HOUR, 00);
		return c.getTime();
	}

	public static String getAssignedMonthWithPattern(Date date, String pattern, Integer step) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.set(Calendar.MONTH, c.get(Calendar.MONTH) - step);
		DateFormat format = new SimpleDateFormat(pattern);
		return format.format(c.getTime());
	}

	public static String getAssignedDayWithPattern(Date date, String pattern, Integer step) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.add(Calendar.DATE, step);
		DateFormat format = new SimpleDateFormat(pattern);
		return format.format(c.getTime());
	}

	/**
	 * 计算日历开始的时间和结束的时间日期
	 * 
	 * @param start
	 * @return Date[]
	 */
	public static Date[] buildStartAndEnd(Date start) {
		if (null == start) {
			return null;
		}
		Date[] returnDate = new Date[2];
		Calendar c = new GregorianCalendar();
		c.setTime(start);
		c.set(Calendar.DAY_OF_MONTH, 1);
		int weekDay = 1 - c.get(Calendar.DAY_OF_WEEK);
		if (weekDay > 0) {
			weekDay -= 7;
		}
		Calendar c2 = new GregorianCalendar();
		c2.setTime(start);
		c2.set(Calendar.DATE, c.get(Calendar.DATE) + weekDay);
		returnDate[0] = c2.getTime();
		c2.setTime(start);
		c2.set(Calendar.DATE, c.get(Calendar.DATE) + 37);
		c2.set(Calendar.SECOND, 59);
		c2.set(Calendar.MINUTE, 59);
		c2.set(Calendar.HOUR, 23);
		returnDate[1] = c2.getTime();
		return returnDate;
	}

	/**
	 * 取加、减N天后的日期
	 * 
	 * @param now
	 * @param addDay
	 * @return
	 */
	public static Date getDate(Date now, int addDay) {
		// long time = addDay * 24 * 60 * 60 * 1000;
		// Date date = new Date(now.getTime() + time);
		Calendar cd = Calendar.getInstance();
		cd.setTime(now);
		cd.add(Calendar.DATE, addDay);
		Date date = cd.getTime();
		return date;
	}

	/**
	 * 取加、减N月后的日期
	 * 
	 * @param now
	 * @param addMonth
	 * @return
	 */
	public static Date getDate_Month(Date now, int addMonth) {
		// long time = addDay * 24 * 60 * 60 * 1000;
		// Date date = new Date(now.getTime() + time);
		Calendar cd = Calendar.getInstance();
		cd.setTime(now);
		cd.add(Calendar.MONTH, addMonth);
		Date date = cd.getTime();
		return date;
	}

	/**
	 * 取年度
	 * 
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 取加、减N年后的日期
	 * 
	 * @param now
	 * @param addYear
	 * @return
	 */
	public static Date getDate_Year(Date now, int addYear) {
		// long time = addDay * 24 * 60 * 60 * 1000;
		// Date date = new Date(now.getTime() + time);
		Calendar cd = Calendar.getInstance();
		cd.setTime(now);
		cd.add(Calendar.YEAR, addYear);
		Date date = cd.getTime();
		return date;
	}

	/**
	 * 取第几月
	 * 
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * 取第几天
	 * 
	 * @param date
	 * @return
	 */
	public static int getDay(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 取小时
	 * 
	 * @param date
	 * @return
	 */
	public static int getHour(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 取分钟
	 * 
	 * @param date
	 * @return
	 */
	public static int getMinute(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.MINUTE);
	}

	/**
	 * 取星期几（数字）
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeek(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 取一年中的第几周（数字）
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 取星期几（中文）
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeekCaption(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		int week = cal.get(Calendar.DAY_OF_WEEK);
		String s = "日一二三四五六";
		return s.substring(week - 1, week);
	}

	/**
	 * 取秒数
	 * 
	 * @param date
	 * @return
	 */
	public static int getSecond(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.SECOND);
	}

	/**
	 * 格式化日期时间
	 * 
	 * @param year
	 * @param month
	 * @param type
	 *            0：取月初时间 1：取月末时间 2：取月初日期 3：取月末日期
	 * @param defaultDate
	 *            默认时间
	 * @return
	 */
	public static Date formatDateTime(int year, int month, int type, Date defaultDate) {
		SimpleDateFormat format1 = null;
		if (type == 0 || type == 1) {
			format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else {
			format1 = new SimpleDateFormat("yyyy-MM-dd");
		}
		String s = year + "-" + (month <= 9 ? "0" + Integer.toString(month) : Integer.toString(month));
		if (type == 0 || type == 2) {
			s += "-01";
		} else {
			switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				s += "-31";
				break;
			case 2:
				if ((year / 4) == 0) {
					s += "-29";
				} else {
					s += "-28";
				}
				break;
			default:
				s += "-30";
				break;
			}
		}
		if (type == 0) {
			s += " 00:00:00";
		} else if (type == 1) {
			s += " 23:59:59";
		}
		try {
			return format1.parse(s);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return defaultDate;
	}

	/**
	 * <获取指定日期的前N天的日期 >
	 * 
	 * @param date
	 * @param day
	 * @return Date
	 * @throws
	 */
	public static Date getDateBefore(Date date, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - day);
		return cal.getTime();
	}

	/**
	 * 得到一个月前的当前日期
	 */
	public static Date getDateBeforeMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
		return cal.getTime();
	}

	/**
	 * <获取前一天的日期 >
	 * 
	 * @param date
	 * @return Date
	 * @throws
	 */
	public static Date getDateBefore(Date date) {
		return getDateBefore(date, 1);
	}

	/**
	 * <获取指定日期的当月总天数 >
	 * 
	 * @param date
	 * @return int
	 * @throws
	 */
	public static int getTotalDaysByMonth(String date, String patten) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(formatStrToDate(date, patten));
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * <获取具体时间描述。如11190000转换成 3小时6分30秒
	 * 
	 * @param costtime
	 * @return
	 */
	public static String getCostTimeString(long costtime) {
		int second = 1000;
		int minute = 60 * second;
		int hour = 60 * minute;
		String cost = null;
		if (costtime < second) {// 少于1秒
			cost = costtime + "毫秒";
		} else if (costtime < minute) {// 少于1分钟
			cost = getCostTimeSecond(costtime);
		} else if (costtime < hour) {// 少于1小时
			cost = getCostTimeMinute(costtime) + getCostTimeSecond(costtime % minute);
		} else {// 大于1小时
			cost = (costtime / hour) + "小时" + getCostTimeMinute(costtime % hour) + getCostTimeSecond(costtime % hour % minute);
		}
		return cost;
	}

	/**
	 * <获取秒>
	 * 
	 * @param costtime
	 * @return
	 */
	private static String getCostTimeSecond(long costtime) {
		String second = "";
		if (costtime / 1000 > 0)
			second = costtime / 1000 + "秒";
		return second;
	}

	/**
	 * <获取分钟>
	 * 
	 * @param costtime
	 * @return
	 */
	private static String getCostTimeMinute(long costtime) {
		String minute = "";
		if (costtime / 60000 > 0)
			minute = costtime / 60000 + "分";
		return minute;
	}

	/**
	 * 获取两个日期之间月份集合
	 * 
	 * @param start
	 * @param end
	 * @return String[]
	 */
	public static String[] getAllMonths(String start, String end) {
		final String splitSign = "-";
		String regex = "\\d{4}" + splitSign + "(([0][1-9])|([1][012]))"; // 判断YYYY-MM时间格式的正则表达式
		if (!start.matches(regex) || !end.matches(regex))
			return new String[0];
		List<String> list = new ArrayList<String>();
		if (start.compareTo(end) > 0) {// start大于end日期时，互换
			String temp = start;
			start = end;
			end = temp;
		}
		String temp = start; // 从最小月份开始
		while (temp.compareTo(start) >= 0 && temp.compareTo(end) <= 0) {
			list.add(temp); // 首先加上最小月份,接着计算下一个月份
			String[] arr = temp.split(splitSign);
			int year = Integer.valueOf(arr[0]);
			int month = Integer.valueOf(arr[1]) + 1;
			if (month > 12) {
				month = 1;
				year++;
			}
			if (month < 10) {// 补0操作
				temp = year + splitSign + "0" + month;
			} else {
				temp = year + splitSign + month;
			}
		}
		int size = list.size();
		String[] result = new String[size];
		for (int i = 0; i < size; i++) {
			result[i] = list.get(i);
		}
		return result;
	}

	/**
	 * 
	 * <获取昨天的开始时间与结束时间 >
	 *
	 * @return String[] 数组第一个为开始时间，数组第二个为结束时间
	 * @see
	 * @throws
	 */
	public static String[] getYesterDayTime() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String times = SIMPLE_DATA_FORMAT.format(cal.getTime());
		return new String[] { times + PATTERN_MIN, times + PATTERN_MAX };
	}

	/**
	 * 
	 * <获取本周开始时间及本周结束时间，以周日为每周的第一天>
	 *
	 * @return String[] 数组第一个为开始时间，数组第二个为结束时间
	 * @see
	 * @throws
	 */
	public static String[] getThisWeekTime() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		String startTime = SIMPLE_DATA_FORMAT.format(cal.getTime()) + PATTERN_MIN;// 获得当前周第一天的日期

		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		String endTime = SIMPLE_DATA_FORMAT.format(cal.getTime()) + PATTERN_MAX;// 获取当前周第后一天的日期
		return new String[] { startTime, endTime };
	}

	/**
	 * 
	 * <获取上周开始时间及本周结束时间>
	 *
	 * @return String[] 数组第一个为开始时间，数组第二个为结束时间
	 * @see
	 * @throws
	 */
	public static String[] getUpWeekTime() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		String startTime = SIMPLE_DATA_FORMAT.format(cal.getTime()) + PATTERN_MIN;// 获取上周周日的日期

		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		String endTime = SIMPLE_DATA_FORMAT.format(cal.getTime()) + PATTERN_MAX;// 获取上周周六的日期
		return new String[] { startTime, endTime };
	}

	/**
	 * 
	 * <获取本月开始时间及结束时间 >
	 *
	 * @return String[] 数组第一个为开始时间，数组第二个为结束时间
	 * @see
	 * @throws
	 */
	public static String[] getThisMonthTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		String startTime = SIMPLE_DATA_FORMAT.format(cal.getTime()) + PATTERN_MIN;// 本月第一日
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		String endTime = SIMPLE_DATA_FORMAT.format(cal.getTime()) + PATTERN_MAX;// 本月最后一日
		return new String[] { startTime, endTime };
	}

	/**
	 * 
	 * <获取上月的开始时间与结束时间 >
	 *
	 * @return String[] 数组第一个为开始时间，数组第二个为结束时间
	 * @see
	 * @throws
	 */
	public static String[] getUpMonthTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, -1);
		String startTime = SIMPLE_DATA_FORMAT.format(cal.getTime()) + PATTERN_MIN;// 上月第一日

		cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.roll(Calendar.DAY_OF_MONTH, -1);
		String endTime = SIMPLE_DATA_FORMAT.format(cal.getTime()) + PATTERN_MAX;// 上月最后一日
		return new String[] { startTime, endTime };
	}

	public static Date startOfDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date endOfDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date getCurrentDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.getTime();
	}

	public static Date addYears(Date date, int years) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, years);
		return c.getTime();
	}

	public static Date addDays(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, days);
		return c.getTime();
	}

	public static Date addHours(Date date, int hours) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR_OF_DAY, hours);
		return c.getTime();
	}

	public static Date addMinutes(Date date, int minutes) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, minutes);
		return c.getTime();
	}

	public static Date addSeconds(Date date, int seconds) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.SECOND, seconds);
		return c.getTime();
	}

	/**
	 * 判断日期是否是今天
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isToday(Date date) {
		Date today = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 设置显示格式
		String nowTime = dateFormat.format(today);
		String sDate = dateFormat.format(date);
		if (sDate.equals(nowTime)) {
			return true;
		}

		return false;
	}

	/**
	 * 返回指定年度的所有周。List中包含的是String[2]对象<br>
	 * string[0]本周的开始日期,string[1]是本周的结束日期。<br>
	 * 日期的格式为yyyy-MM-dd。<br>
	 * 每年的第一个周，必须包含星期一且是完整的七天。<br>
	 * 例如：2009年的第一个周开始日期为2009-01-05，结束日期为2009-01-11。 <br>
	 * 星期一在哪一年，那么包含这个星期的周就是哪一年的周。<br>
	 * 例如：2008-12-29是星期一，2009-01-04是星期日，哪么这个周就是2008年度的最后一个周。<br>
	 * 
	 * @param year
	 *            格式 yyyy ，必须大于1900年度 小于9999年
	 * @return
	 */
	public static List<String[]> getWeeksByYear(final int year) {
		if (year < 1900 || year > 9999) {
			throw new NullPointerException("年度必须大于等于1900年小于等于9999年");
		}
		int weeks = getWeekNumByYear(year);
		List<String[]> result = new ArrayList<String[]>(weeks);
		for (int i = 0; i < weeks; i++) {
			String[] tempWeek = new String[2];
			tempWeek[0] = getYearWeekFirstDay(year, i);
			tempWeek[1] = getYearWeekEndDay(year, i);
			result.add(tempWeek);
		}
		return result;
	}

	/**
	 * 计算指定年度共有多少个周。
	 * 
	 * @param year
	 *            格式 yyyy ，必须大于1900年度 小于9999年
	 * @return
	 */
	public static int getWeekNumByYear(final int year) {
		if (year < 1900 || year > 9999) {
			throw new NullPointerException("年度必须大于等于1900年小于等于9999年");
		}
		int result = 52;// 每年至少有52个周 ，最多有53个周。
		String date = getYearWeekFirstDay(year, 53);
		if (date.substring(0, 4).equals(year + "")) { // 判断年度是否相符，如果相符说明有53个周。
			result = 53;
		}
		return result;
	}

	/**
	 * 计算某年某周的开始日期
	 * 
	 * @param yearNum
	 *            格式 yyyy ，必须大于1900年度 小于9999年
	 * @param weekNum
	 *            1到52或者53
	 * @return 日期，格式为yyyy-MM-dd
	 */
	public static String getYearWeekFirstDay(int yearNum, int weekNum) {
		if (yearNum < 1900 || yearNum > 9999) {
			throw new NullPointerException("年度必须大于等于1900年小于等于9999年");
		}
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 每周从周一开始
		// 上面两句代码配合，才能实现，每年度的第一个周，是包含第一个星期一的那个周。
		cal.setMinimalDaysInFirstWeek(7); // 设置每周最少为7天
		cal.set(Calendar.YEAR, yearNum);
		cal.set(Calendar.WEEK_OF_YEAR, weekNum);

		// 分别取得当前日期的年、月、日
		return getFormatDate(cal.getTime());
	}

	/**
	 * 计算某年某周的结束日期
	 * 
	 * @param yearNum
	 *            格式 yyyy ，必须大于1900年度 小于9999年
	 * @param weekNum
	 *            1到52或者53
	 * @return 日期，格式为yyyy-MM-dd
	 */
	public static String getYearWeekEndDay(int yearNum, int weekNum) {
		if (yearNum < 1900 || yearNum > 9999) {
			throw new NullPointerException("年度必须大于等于1900年小于等于9999年");
		}
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);// 每周从周一开始
		// 上面两句代码配合，才能实现，每年度的第一个周，是包含第一个星期一的那个周。
		cal.setMinimalDaysInFirstWeek(7); // 设置每周最少为7天
		cal.set(Calendar.YEAR, yearNum);
		cal.set(Calendar.WEEK_OF_YEAR, weekNum);

		return getFormatDate(cal.getTime());
	}

	private static String getFormatDate(Date time) {
		// TODO Auto-generated method stub
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}

	public static int getIntervalDays(Date startDate, Date endDate) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			startDate = sdf.parse(sdf.format(startDate));
			endDate = sdf.parse(sdf.format(endDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(endDate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));

	}
}
