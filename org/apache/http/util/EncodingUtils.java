package org.apache.http.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.http.Consts;

public final class EncodingUtils
{
  public static byte[] getAsciiBytes(String paramString)
  {
    Args.notNull(paramString, "Input");
    try
    {
      byte[] arrayOfByte = paramString.getBytes(Consts.ASCII.name());
      return arrayOfByte;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new Error("ASCII not supported");
    }
  }
  
  public static String getAsciiString(byte[] paramArrayOfByte)
  {
    Args.notNull(paramArrayOfByte, "Input");
    return getAsciiString(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public static String getAsciiString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    Args.notNull(paramArrayOfByte, "Input");
    try
    {
      String str = new String(paramArrayOfByte, paramInt1, paramInt2, Consts.ASCII.name());
      return str;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new Error("ASCII not supported");
    }
  }
  
  public static byte[] getBytes(String paramString1, String paramString2)
  {
    Args.notNull(paramString1, "Input");
    Args.notEmpty(paramString2, "Charset");
    try
    {
      byte[] arrayOfByte = paramString1.getBytes(paramString2);
      return arrayOfByte;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
    return paramString1.getBytes();
  }
  
  public static String getString(byte[] paramArrayOfByte, int paramInt1, int paramInt2, String paramString)
  {
    Args.notNull(paramArrayOfByte, "Input");
    Args.notEmpty(paramString, "Charset");
    try
    {
      String str = new String(paramArrayOfByte, paramInt1, paramInt2, paramString);
      return str;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
    return new String(paramArrayOfByte, paramInt1, paramInt2);
  }
  
  public static String getString(byte[] paramArrayOfByte, String paramString)
  {
    Args.notNull(paramArrayOfByte, "Input");
    return getString(paramArrayOfByte, 0, paramArrayOfByte.length, paramString);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.util.EncodingUtils
 * JD-Core Version:    0.7.0.1
 */