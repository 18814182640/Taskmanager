package com.TMC.Unit;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
/**
*@author 170711185
 */
public class DateUnit {
	
    private static String week[] = {"周日","周一","周二","周三","周四","周五","周六"};
	/**
	*获取本周的第一天
	 */
	public static java.util.Date getThisWeekStartDate() {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(new Date());
		int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DATE, -dayofweek+1);
		return calendar.getTime();
	}
	
	
	/**
	*获取本月的第一天
	 */
	public static java.util.Date getThisMonthStartDate() {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(new Date());
		int day= calendar.get(Calendar.DAY_OF_MONTH);
		calendar.add(Calendar.DATE, -day+1);
		return calendar.getTime();
	}
	
	/**
	*获取当前月份
	 */
	public static int getNowMonth() {
		return getMonth(new Date());
	}
	/**
	*获取当前年份
	 */
	public static int getNowYear() {
		return getYear(new Date());
	}
	
	/**
	*获取周
	 */
	public static int getWeekOnYear(Date date) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	/**
	*获取周数
	 */
	public static int getWeekCountOnMonth(Date date) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		return calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
	}
	
	/**
	*获取天数
	 */
	public static int getDayOnMonth(Date date) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	*获取星期
	 */
	public static int getDayOnWeek(Date date) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK)-1;
	}
	
	
	/**
	*获取星期
	 */
	public static String getDayOnWeek(Date date,int day) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(date);
		calendar.add(Calendar.DATE,day);
		return week[calendar.get(Calendar.DAY_OF_WEEK)-1];
	}
	
	/**
	*获取周的天数
	 */
	public static int getDayCountOnThisWeek(Date date,int week) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(date);
	    int startDay =  calendar.get(Calendar.DAY_OF_WEEK);
	    int dayCount = getDayOnMonth(date);
	    int weekCount  = getWeekCountOnMonth(date);
	    if (week==0) {
			return 7-startDay+1;
		}else if(week==weekCount-1){
			return dayCount-(weekCount-2)*7-(7-startDay+1);
		}else{
			return 7;
		}
	}
	
    
	/**
	*获取年份
	 */
	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(date);
	    return calendar.get(Calendar.YEAR);
	}
	
	/**
	*获取月份
	 */
	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(date);
	    return calendar.get(Calendar.MONTH)+1;
	}
	
	
	/**
	*获取天数
	 */
	public static int getDay(Date date) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(date);
	    return calendar.get(Calendar.DATE)+1;
	}
	

}
