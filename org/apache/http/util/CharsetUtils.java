package org.apache.http.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

public class CharsetUtils
{
  public static Charset get(String paramString)
    throws UnsupportedEncodingException
  {
    if (paramString == null) {
      return null;
    }
    try
    {
      Charset localCharset = Charset.forName(paramString);
      return localCharset;
    }
    catch (UnsupportedCharsetException localUnsupportedCharsetException)
    {
      throw new UnsupportedEncodingException(paramString);
    }
  }
  
  public static Charset lookup(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    try
    {
      Charset localCharset = Charset.forName(paramString);
      return localCharset;
    }
    catch (UnsupportedCharsetException localUnsupportedCharsetException) {}
    return null;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.util.CharsetUtils
 * JD-Core Version:    0.7.0.1
 */