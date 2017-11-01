package cn.account.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccountDateUtil {
	/**
	 * 字符串转日期
	 * @param dateString
	 * @param dateFormat
	 * @return
	 * @throws ParseException
	 */
	public static Date StringToDate(String dateString,String dateFormat) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);  
	    Date date = sdf.parse(dateString);  
	    return date;
	}
	/**
	 * 日期转字符串
	 * @param date
	 * @param dateFormat
	 * @return
	 * @throws ParseException
	 */
	public static String DateToString(Date date,String dateFormat) throws ParseException{
		String dateStr;  
		dateStr = (new SimpleDateFormat(dateFormat)).format(date);  
	    return dateStr;
	}
	/**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1,Date date2){
        int days = (int) ((date1.getTime() - date2.getTime()) / (1000*3600*24));
        return days;
    }
    
    public static void main(String[] args) throws ParseException {
    	
    	String dateFormat = "yyyy-MM-dd";
		Date date1 = StringToDate("2017-08-04", dateFormat);
		Date date2 = StringToDate("2017-08-01", dateFormat);
		
		System.out.println(differentDaysByMillisecond(date1, date2));
	}
}
