package com.ericsson.api.codefactory.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author yaochong.chen
 *
 */
public class DateTimeUtil {

	public static final int YEAR = 1;

	public static final int MONTH = 2;

	public static final int DAY = 3;

	public static final int HOUR = 4;

	public static final int MINUTE = 5;

	public static final int SECOND = 6;

	/**
	 *
	 *
	 * @return current timestamp
	 */
	public static Timestamp getCurTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getFormatedDate(Date date, String pattern) {
		if ((date != null) && !StringUtil.isEmpty(pattern)) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		}
		return null;
	}

	public static String getCurrentDate(String pattern) {
		if (!StringUtil.isEmpty(pattern)) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(new Date(System.currentTimeMillis()));
		}
		return null;
	}

	/**
	 *
	 *
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	/**
	 *
	 *
	 * @param dateStr
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date getDateFromFormate(String dateStr, String pattern) throws ParseException {
		if (StringUtil.isNotEmpty(dateStr)) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.parse(dateStr);
		} else {
			return null;
		}
	}

	/**
	 *
	 *
	 * @param date
	 * @param field
	 * @param offset
	 * @return
	 */
	public static Date modifyDate(Date date, int field, int offset) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(field, offset);
		return c.getTime();
	}

	/**
	 *
	 *
	 * @param category
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int dateDiff(int category, Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		if (YEAR == category) {
			return Math.abs(c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR));
		} else if (MONTH == category) {
			return Math.abs((((c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) * 12) + c2.get(Calendar.MONTH))
					- c1.get(Calendar.MONTH));
		} else if (DAY == category) {
			return Integer.parseInt(Long.toString((date2.getTime() - date1.getTime()) / 86400000));
		} else if (HOUR == category) {
			return Integer.parseInt(Long.toString((date2.getTime() - date1.getTime()) / 3600000));
		} else if (MINUTE == category) {
			return Integer.parseInt(Long.toString((date2.getTime() - date1.getTime()) / 60000));
		} else if (SECOND == category) {
			return Integer.parseInt(Long.toString((date2.getTime() - date1.getTime()) / 1000));
		} else {
			return -1;
		}
	}

	public static void main(String[] args) {
		System.out.println(dateDiff(YEAR, new Date(), modifyDate(new Date(), Calendar.YEAR, 1)));
		System.out.println(dateDiff(HOUR, new Date(), modifyDate(new Date(), Calendar.HOUR, 1)));
		Date date = new Date();
		date = modifyDate(date, Calendar.YEAR, 1);
		date = modifyDate(date, Calendar.MONTH, 1);
		System.out.println(dateDiff(MONTH, date, new Date()));
	}
}
