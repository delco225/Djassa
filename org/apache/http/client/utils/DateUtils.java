package org.apache.http.client.utils;

import java.lang.ref.SoftReference;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public final class DateUtils
{
  private static final String[] DEFAULT_PATTERNS = { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy" };
  private static final Date DEFAULT_TWO_DIGIT_YEAR_START;
  public static final TimeZone GMT = TimeZone.getTimeZone("GMT");
  public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";
  public static final String PATTERN_RFC1036 = "EEE, dd-MMM-yy HH:mm:ss zzz";
  public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
  
  static
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeZone(GMT);
    localCalendar.set(2000, 0, 1, 0, 0, 0);
    localCalendar.set(14, 0);
    DEFAULT_TWO_DIGIT_YEAR_START = localCalendar.getTime();
  }
  
  public static void clearThreadLocal() {}
  
  public static String formatDate(Date paramDate)
  {
    return formatDate(paramDate, "EEE, dd MMM yyyy HH:mm:ss zzz");
  }
  
  public static String formatDate(Date paramDate, String paramString)
  {
    Args.notNull(paramDate, "Date");
    Args.notNull(paramString, "Pattern");
    return DateFormatHolder.formatFor(paramString).format(paramDate);
  }
  
  public static Date parseDate(String paramString)
  {
    return parseDate(paramString, null, null);
  }
  
  public static Date parseDate(String paramString, String[] paramArrayOfString)
  {
    return parseDate(paramString, paramArrayOfString, null);
  }
  
  public static Date parseDate(String paramString, String[] paramArrayOfString, Date paramDate)
  {
    Args.notNull(paramString, "Date value");
    String[] arrayOfString1;
    Date localDate1;
    label21:
    String str;
    String[] arrayOfString2;
    int i;
    if (paramArrayOfString != null)
    {
      arrayOfString1 = paramArrayOfString;
      if (paramDate == null) {
        break label144;
      }
      localDate1 = paramDate;
      str = paramString;
      if ((str.length() > 1) && (str.startsWith("'")) && (str.endsWith("'"))) {
        str = str.substring(1, -1 + str.length());
      }
      arrayOfString2 = arrayOfString1;
      i = arrayOfString2.length;
    }
    for (int j = 0;; j++)
    {
      if (j >= i) {
        break label158;
      }
      SimpleDateFormat localSimpleDateFormat = DateFormatHolder.formatFor(arrayOfString2[j]);
      localSimpleDateFormat.set2DigitYearStart(localDate1);
      ParsePosition localParsePosition = new ParsePosition(0);
      Date localDate2 = localSimpleDateFormat.parse(str, localParsePosition);
      if (localParsePosition.getIndex() != 0)
      {
        return localDate2;
        arrayOfString1 = DEFAULT_PATTERNS;
        break;
        label144:
        localDate1 = DEFAULT_TWO_DIGIT_YEAR_START;
        break label21;
      }
    }
    label158:
    return null;
  }
  
  static final class DateFormatHolder
  {
    private static final ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>> THREADLOCAL_FORMATS = new ThreadLocal()
    {
      protected SoftReference<Map<String, SimpleDateFormat>> initialValue()
      {
        return new SoftReference(new HashMap());
      }
    };
    
    public static void clearThreadLocal()
    {
      THREADLOCAL_FORMATS.remove();
    }
    
    public static SimpleDateFormat formatFor(String paramString)
    {
      Object localObject = (Map)((SoftReference)THREADLOCAL_FORMATS.get()).get();
      if (localObject == null)
      {
        localObject = new HashMap();
        THREADLOCAL_FORMATS.set(new SoftReference(localObject));
      }
      SimpleDateFormat localSimpleDateFormat = (SimpleDateFormat)((Map)localObject).get(paramString);
      if (localSimpleDateFormat == null)
      {
        localSimpleDateFormat = new SimpleDateFormat(paramString, Locale.US);
        localSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        ((Map)localObject).put(paramString, localSimpleDateFormat);
      }
      return localSimpleDateFormat;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.utils.DateUtils
 * JD-Core Version:    0.7.0.1
 */