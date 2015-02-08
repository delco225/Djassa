package org.apache.http.conn.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.annotation.Immutable;

@Immutable
public class InetAddressUtils
{
  private static final char COLON_CHAR = ':';
  private static final String IPV4_BASIC_PATTERN_STRING = "(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])";
  private static final Pattern IPV4_MAPPED_IPV6_PATTERN;
  private static final Pattern IPV4_PATTERN = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
  private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile("^(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4}){0,5})?)::(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4}){0,5})?)$");
  private static final Pattern IPV6_STD_PATTERN;
  private static final int MAX_COLON_COUNT = 7;
  
  static
  {
    IPV4_MAPPED_IPV6_PATTERN = Pattern.compile("^::[fF]{4}:(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
    IPV6_STD_PATTERN = Pattern.compile("^[0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){7}$");
  }
  
  public static boolean isIPv4Address(String paramString)
  {
    return IPV4_PATTERN.matcher(paramString).matches();
  }
  
  public static boolean isIPv4MappedIPv64Address(String paramString)
  {
    return IPV4_MAPPED_IPV6_PATTERN.matcher(paramString).matches();
  }
  
  public static boolean isIPv6Address(String paramString)
  {
    return (isIPv6StdAddress(paramString)) || (isIPv6HexCompressedAddress(paramString));
  }
  
  public static boolean isIPv6HexCompressedAddress(String paramString)
  {
    int i = 0;
    for (int j = 0; j < paramString.length(); j++) {
      if (paramString.charAt(j) == ':') {
        i++;
      }
    }
    return (i <= 7) && (IPV6_HEX_COMPRESSED_PATTERN.matcher(paramString).matches());
  }
  
  public static boolean isIPv6StdAddress(String paramString)
  {
    return IPV6_STD_PATTERN.matcher(paramString).matches();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.util.InetAddressUtils
 * JD-Core Version:    0.7.0.1
 */