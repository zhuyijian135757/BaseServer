package net.flyingfat.common.lang;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil
{
  private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
  public static final long SECOND = 1000L;
  public static final long MINUTE = 60000L;
  public static final long HOUR = 3600000L;
  public static final long DAY = 86400000L;
  public static final String TIME_BEGIN = " 00:00:00";
  public static final String TIME_END = " 23:59:59";
  public static final String MONTH_PATTERN = "yyyy-MM";
  public static final String DEFAULT_PATTERN = "yyyyMMdd";
  public static final String FULL_PATTERN = "yyyyMMddHHmmss";
  public static final String FULL_STANDARD_PATTERN = "yyyyMMdd HH:mm:ss";
  public static final String TRADITION_PATTERN = "yyyy-MM-dd";
  public static final String FULL_TRADITION_PATTERN = "yyyy-MM-dd HH:mm:ss";
  public static final String HH_MM_PATTERN = "HH:mm";
  
  public static String getShortNow()
  {
    return formatDate("yyyy-MM-dd");
  }
  
  public static String getTimeBykm()
  {
    return formatDate("H:mm");
  }
  
  public static String getMonth()
  {
    return formatDate("MM");
  }
  
  public static String getDay()
  {
    return formatDate("dd");
  }
  
  public static String formatDate(Date date)
  {
    return formatDate(date, "yyyyMMdd");
  }
  
  public static String formatDate(Date date, String format)
  {
    if ((null == date) || (StringUtil.isBlank(format))) {
      return null;
    }
    try
    {
      return new SimpleDateFormat(format).format(date);
    }
    catch (Exception e)
    {
      logger.error("", e);
    }
    return null;
  }
  
  public static String formatDate(String format)
  {
    return formatDate(new Date(), format);
  }
  
  public static Date parseDate(String date)
  {
    return parseDate(date, "yyyyMMdd", null);
  }
  
  public static Date parseDate(String date, String df)
  {
    return parseDate(date, df, null);
  }
  
  public static Date parseDate(String date, String df, Date defaultValue)
  {
    if ((date == null) || (StringUtil.isBlank(df))) {
      return defaultValue;
    }
    SimpleDateFormat formatter = new SimpleDateFormat(df);
    try
    {
      return formatter.parse(date);
    }
    catch (ParseException e)
    {
      logger.error("", e);
    }
    return defaultValue;
  }
  
  public static Date currentDate()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.set(10, 0);
    calendar.set(12, 0);
    calendar.set(13, 0);
    calendar.set(14, 0);
    return new Date(calendar.getTimeInMillis());
  }
  
  public static Date currentDate(Date date)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(10, 0);
    calendar.set(12, 0);
    calendar.set(13, 0);
    calendar.set(14, 0);
    return new Date(calendar.getTimeInMillis());
  }
  
  public static Date getStartOfDate(Date date)
  {
    if (date == null) {
      return null;
    }
    Calendar cal = GregorianCalendar.getInstance();
    cal.setTime(date);
    
    cal.set(11, 0);
    cal.set(12, 0);
    cal.set(13, 0);
    cal.set(14, 0);
    
    return new Date(cal.getTime().getTime());
  }
  
  public static Date getPreviousMonday()
  {
    Calendar cd = Calendar.getInstance();
    
    int dayOfWeek = cd.get(7) - 1;
    Date date;
    if (dayOfWeek == 1) {
      date = addDays(cd.getTime(), -7);
    } else {
      date = addDays(cd.getTime(), -6 - dayOfWeek);
    }
    return getStartOfDate(date);
  }
  
  public static Date getMondayBefore4Week()
  {
    Calendar cd = Calendar.getInstance();

    int dayOfWeek = cd.get(7) - 1;
    Date date;
    if (dayOfWeek == 1) {
      date = addDays(cd.getTime(), -28);
    } else {
      date = addDays(cd.getTime(), -27 - dayOfWeek);
    }
    return getStartOfDate(date);
  }
  
  public static Date getCurrentMonday()
  {
    Calendar cd = Calendar.getInstance();
    

    int dayOfWeek = cd.get(7) - 1;
    Date date;
    if (dayOfWeek == 1) {
      date = cd.getTime();
    } else {
      date = addDays(cd.getTime(), 1 - dayOfWeek);
    }
    return getStartOfDate(date);
  }
  
  public static Date getCurrentMonday(Calendar cd)
  {
    int dayOfWeek = cd.get(7) - 1;
    Date date;
    if (dayOfWeek == 1)
    {
      date = cd.getTime();
    }
    else
    {
      if (dayOfWeek == 0) {
        date = addDays(cd.getTime(), -6);
      } else {
        date = addDays(cd.getTime(), 1 - dayOfWeek);
      }
    }
    return getStartOfDate(date);
  }
  
  public static Date getCurrentSunday(Calendar cd)
  {
    Date curMonday = getCurrentMonday(cd);
    Date curSunday = addDays(curMonday, 6);
    return getStartOfDate(curSunday);
  }
  
  public static Date getPreviousMonday(Calendar cd)
  {
    Date curMonday = getCurrentMonday(cd);
    Date previousMondayDate = addDays(curMonday, -7);
    return getStartOfDate(previousMondayDate);
  }
  
  public static Date getPreviousSunday(Calendar cd)
  {
    Date curMonday = getCurrentMonday(cd);
    Date previousMondayDate = addDays(curMonday, -1);
    return getStartOfDate(previousMondayDate);
  }
  
  public static boolean beforeDay(Date date1, Date date2)
  {
    if (date1 == null) {
      return true;
    }
    return getStartOfDate(date1).before(getStartOfDate(date2));
  }
  
  public static boolean afterDay(Date date1, Date date2)
  {
    if (date1 == null) {
      return false;
    }
    return getStartOfDate(date1).after(getStartOfDate(date2));
  }
  
  public static Date addMonths(Date date, int months)
  {
    if (months == 0) {
      return date;
    }
    if (date == null) {
      return null;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(2, months);
    return cal.getTime();
  }
  
  public static Date addDays(Date date, int days)
  {
    if (days == 0) {
      return date;
    }
    if (date == null) {
      return null;
    }
    Calendar cal = GregorianCalendar.getInstance();
    cal.setTime(date);
    cal.add(5, days);
    
    return new Date(cal.getTime().getTime());
  }
  
  public static Date addMins(Date date, int mins)
  {
    if (mins == 0) {
      return date;
    }
    if (date == null) {
      return null;
    }
    Calendar cal = GregorianCalendar.getInstance();
    cal.setTime(date);
    cal.add(12, mins);
    
    return new Date(cal.getTime().getTime());
  }
  
  public static Date addSeconds(Date date, int secs)
  {
    if (secs == 0) {
      return date;
    }
    if (date == null) {
      return null;
    }
    Calendar cal = GregorianCalendar.getInstance();
    cal.setTime(date);
    cal.add(13, secs);
    
    return new Date(cal.getTime().getTime());
  }
  
  public static boolean isSameMonth(Date date1, Date date2)
  {
    if ((date1 == null) && (date2 == null)) {
      return true;
    }
    if ((date1 == null) || (date2 == null)) {
      return false;
    }
    Calendar cal1 = GregorianCalendar.getInstance();
    cal1.setTime(date1);
    Calendar cal2 = GregorianCalendar.getInstance();
    cal2.setTime(date2);
    return isSameMonth(cal1, cal2);
  }
  
  public static boolean isSameDay(Date date1, Date date2)
  {
    if ((date1 == null) && (date2 == null)) {
      return true;
    }
    if ((date1 == null) || (date2 == null)) {
      return false;
    }
    Calendar cal1 = GregorianCalendar.getInstance();
    cal1.setTime(date1);
    Calendar cal2 = GregorianCalendar.getInstance();
    cal2.setTime(date2);
    
    return (cal1.get(1) == cal2.get(1)) && (cal1.get(2) == cal2.get(2)) && (cal1.get(5) == cal2.get(5));
  }
  
  public static boolean isSameMonth(Calendar cal1, Calendar cal2)
  {
    if ((cal1 == null) && (cal2 == null)) {
      return true;
    }
    if ((cal1 == null) || (cal2 == null)) {
      return false;
    }
    return (cal1.get(1) == cal2.get(1)) && (cal1.get(2) == cal2.get(2));
  }
  
  public static Date getEndOfMonth(Date date)
  {
    if (date == null) {
      return null;
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    
    calendar.set(2, calendar.get(2) + 1);
    calendar.set(5, 0);
    
    calendar.set(11, 12);
    calendar.set(12, 0);
    calendar.set(13, 0);
    calendar.set(14, 0);
    return new Date(calendar.getTimeInMillis());
  }
  
  public static Date getFirstOfMonth(Date date)
  {
    Date lastMonth = addMonths(date, -1);
    lastMonth = getEndOfMonth(lastMonth);
    return addDays(lastMonth, 1);
  }
  
  public static boolean inFormat(String sourceDate, String format)
  {
    if ((sourceDate == null) || (StringUtil.isBlank(format))) {
      return false;
    }
    try
    {
      SimpleDateFormat dateFormat = new SimpleDateFormat(format);
      dateFormat.setLenient(false);
      dateFormat.parse(sourceDate);
      return true;
    }
    catch (Exception e) {}
    return false;
  }
  
  public static String now()
  {
    return formatDate(new Date(), "yyyyMMddHHmmss");
  }
  
  public static String formatShortDateC(Date gstrDate)
  {
    if (gstrDate == null) {
      return null;
    }
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy��MM��dd��");
    
    String pid = formatter.format(gstrDate);
    return pid;
  }
  
  public static String getNow()
  {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
    Date nowc = new Date();
    String pid = formatter.format(nowc);
    return pid;
  }
  
  public static String formatShort(String strDate)
  {
    String ret = "";
    if ((strDate != null) && (!"1900-01-01 00:00:00.0".equals(strDate)) && (strDate.indexOf("-") > 0))
    {
      ret = strDate;
      if (ret.indexOf(" ") > -1) {
        ret = ret.substring(0, ret.indexOf(" "));
      }
    }
    return ret;
  }
  
  public static int getNumberOfSecondsBetween(double d1, double d2)
  {
    if ((d1 == 0.0D) || (d2 == 0.0D)) {
      return -1;
    }
    return (int)(Math.abs(d1 - d2) / 1000.0D);
  }
  
  public static int getNumberOfMonthsBetween(Date before, Date end)
  {
    if ((before == null) || (end == null)) {
      return -1;
    }
    Calendar cal1 = Calendar.getInstance();
    cal1.setTime(before);
    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(end);
    return (cal2.get(1) - cal1.get(1)) * 12 + (cal2.get(2) - cal1.get(2));
  }
  
  public static long getNumberOfMinuteBetween(Date before, Date end)
  {
    if ((before == null) || (end == null)) {
      return -1L;
    }
    long millisec = end.getTime() - before.getTime();
    return millisec / 60000L;
  }
  
  public static long getNumberOfHoursBetween(Date before, Date end)
  {
    if ((before == null) || (end == null)) {
      return -1L;
    }
    long millisec = end.getTime() - before.getTime() + 1L;
    return millisec / 3600000L;
  }
  
  public static String formatMonthAndDay(Date srcDate)
  {
    return formatDate("MM��dd��");
  }
  
  public static long getNumberOfDaysBetween(Date before, Date end)
  {
    if ((before == null) || (end == null)) {
      return -1L;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(before);
    Calendar endCal = Calendar.getInstance();
    endCal.setTime(end);
    return getNumberOfDaysBetween(cal, endCal);
  }
  
  public static long getNumberOfDaysBetween(Calendar cal1, Calendar cal2)
  {
    if ((cal1 == null) || (cal2 == null)) {
      return -1L;
    }
    cal1.set(14, 0);
    cal1.set(13, 0);
    cal1.set(12, 0);
    cal1.set(11, 0);
    
    cal2.set(14, 0);
    cal2.set(13, 0);
    cal2.set(12, 0);
    cal2.set(11, 0);
    
    long elapsed = cal2.getTime().getTime() - cal1.getTime().getTime();
    return elapsed / 86400000L;
  }
  
  public static Calendar getCurrentCalendar()
  {
    return Calendar.getInstance();
  }
  
  public static Timestamp getCurrentDateTime()
  {
    return new Timestamp(System.currentTimeMillis());
  }
  
  public static Date getCurrentDate()
  {
    return new Date(System.currentTimeMillis());
  }
  
  public static final int getYear(Date date)
  {
    if (date == null) {
      return -1;
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(1);
  }
  
  public static final int getYear(long millis)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(millis);
    return calendar.get(1);
  }
  
  public static final int getMonth(Date date)
  {
    if (date == null) {
      return -1;
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(2) + 1;
  }
  
  public static final int getMonth(long millis)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(millis);
    return calendar.get(2) + 1;
  }
  
  public static final int getDate(Date date)
  {
    if (date == null) {
      return -1;
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(5);
  }
  
  public static final int getDate(long millis)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(millis);
    return calendar.get(5);
  }
  
  public static final int getHour(Date date)
  {
    if (date == null) {
      return -1;
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(11);
  }
  
  public static final int getHour(long millis)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(millis);
    return calendar.get(11);
  }
  
  public static Date getDateByUrl(String url)
  {
    try
    {
      URLConnection uc = new URL(url).openConnection();
      uc.connect();
      return new Date(uc.getDate());
    }
    catch (MalformedURLException e)
    {
      logger.error("", e);
    }
    catch (IOException e)
    {
      logger.error("", e);
    }
    return null;
  }
}
