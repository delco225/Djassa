package org.apache.http.message;

import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Immutable
public class BasicHeaderValueFormatter
  implements HeaderValueFormatter
{
  @Deprecated
  public static final BasicHeaderValueFormatter DEFAULT = new BasicHeaderValueFormatter();
  public static final BasicHeaderValueFormatter INSTANCE = new BasicHeaderValueFormatter();
  public static final String SEPARATORS = " ;,:@()<>\\\"/[]?={}\t";
  public static final String UNSAFE_CHARS = "\"\\";
  
  public static String formatElements(HeaderElement[] paramArrayOfHeaderElement, boolean paramBoolean, HeaderValueFormatter paramHeaderValueFormatter)
  {
    if (paramHeaderValueFormatter != null) {}
    for (;;)
    {
      return paramHeaderValueFormatter.formatElements(null, paramArrayOfHeaderElement, paramBoolean).toString();
      paramHeaderValueFormatter = INSTANCE;
    }
  }
  
  public static String formatHeaderElement(HeaderElement paramHeaderElement, boolean paramBoolean, HeaderValueFormatter paramHeaderValueFormatter)
  {
    if (paramHeaderValueFormatter != null) {}
    for (;;)
    {
      return paramHeaderValueFormatter.formatHeaderElement(null, paramHeaderElement, paramBoolean).toString();
      paramHeaderValueFormatter = INSTANCE;
    }
  }
  
  public static String formatNameValuePair(NameValuePair paramNameValuePair, boolean paramBoolean, HeaderValueFormatter paramHeaderValueFormatter)
  {
    if (paramHeaderValueFormatter != null) {}
    for (;;)
    {
      return paramHeaderValueFormatter.formatNameValuePair(null, paramNameValuePair, paramBoolean).toString();
      paramHeaderValueFormatter = INSTANCE;
    }
  }
  
  public static String formatParameters(NameValuePair[] paramArrayOfNameValuePair, boolean paramBoolean, HeaderValueFormatter paramHeaderValueFormatter)
  {
    if (paramHeaderValueFormatter != null) {}
    for (;;)
    {
      return paramHeaderValueFormatter.formatParameters(null, paramArrayOfNameValuePair, paramBoolean).toString();
      paramHeaderValueFormatter = INSTANCE;
    }
  }
  
  protected void doFormatValue(CharArrayBuffer paramCharArrayBuffer, String paramString, boolean paramBoolean)
  {
    boolean bool = paramBoolean;
    if (!bool) {
      for (int j = 0; (j < paramString.length()) && (!bool); j++) {
        bool = isSeparator(paramString.charAt(j));
      }
    }
    if (bool) {
      paramCharArrayBuffer.append('"');
    }
    for (int i = 0; i < paramString.length(); i++)
    {
      char c = paramString.charAt(i);
      if (isUnsafe(c)) {
        paramCharArrayBuffer.append('\\');
      }
      paramCharArrayBuffer.append(c);
    }
    if (bool) {
      paramCharArrayBuffer.append('"');
    }
  }
  
  protected int estimateElementsLen(HeaderElement[] paramArrayOfHeaderElement)
  {
    int i;
    if ((paramArrayOfHeaderElement == null) || (paramArrayOfHeaderElement.length < 1)) {
      i = 0;
    }
    for (;;)
    {
      return i;
      i = 2 * (-1 + paramArrayOfHeaderElement.length);
      int j = paramArrayOfHeaderElement.length;
      for (int k = 0; k < j; k++) {
        i += estimateHeaderElementLen(paramArrayOfHeaderElement[k]);
      }
    }
  }
  
  protected int estimateHeaderElementLen(HeaderElement paramHeaderElement)
  {
    int i;
    if (paramHeaderElement == null) {
      i = 0;
    }
    for (;;)
    {
      return i;
      i = paramHeaderElement.getName().length();
      String str = paramHeaderElement.getValue();
      if (str != null) {
        i += 3 + str.length();
      }
      int j = paramHeaderElement.getParameterCount();
      if (j > 0) {
        for (int k = 0; k < j; k++) {
          i += 2 + estimateNameValuePairLen(paramHeaderElement.getParameter(k));
        }
      }
    }
  }
  
  protected int estimateNameValuePairLen(NameValuePair paramNameValuePair)
  {
    int i;
    if (paramNameValuePair == null) {
      i = 0;
    }
    String str;
    do
    {
      return i;
      i = paramNameValuePair.getName().length();
      str = paramNameValuePair.getValue();
    } while (str == null);
    return i + (3 + str.length());
  }
  
  protected int estimateParametersLen(NameValuePair[] paramArrayOfNameValuePair)
  {
    int i;
    if ((paramArrayOfNameValuePair == null) || (paramArrayOfNameValuePair.length < 1)) {
      i = 0;
    }
    for (;;)
    {
      return i;
      i = 2 * (-1 + paramArrayOfNameValuePair.length);
      int j = paramArrayOfNameValuePair.length;
      for (int k = 0; k < j; k++) {
        i += estimateNameValuePairLen(paramArrayOfNameValuePair[k]);
      }
    }
  }
  
  public CharArrayBuffer formatElements(CharArrayBuffer paramCharArrayBuffer, HeaderElement[] paramArrayOfHeaderElement, boolean paramBoolean)
  {
    Args.notNull(paramArrayOfHeaderElement, "Header element array");
    int i = estimateElementsLen(paramArrayOfHeaderElement);
    CharArrayBuffer localCharArrayBuffer = paramCharArrayBuffer;
    if (localCharArrayBuffer == null) {
      localCharArrayBuffer = new CharArrayBuffer(i);
    }
    for (;;)
    {
      for (int j = 0; j < paramArrayOfHeaderElement.length; j++)
      {
        if (j > 0) {
          localCharArrayBuffer.append(", ");
        }
        formatHeaderElement(localCharArrayBuffer, paramArrayOfHeaderElement[j], paramBoolean);
      }
      localCharArrayBuffer.ensureCapacity(i);
    }
    return localCharArrayBuffer;
  }
  
  public CharArrayBuffer formatHeaderElement(CharArrayBuffer paramCharArrayBuffer, HeaderElement paramHeaderElement, boolean paramBoolean)
  {
    Args.notNull(paramHeaderElement, "Header element");
    int i = estimateHeaderElementLen(paramHeaderElement);
    CharArrayBuffer localCharArrayBuffer = paramCharArrayBuffer;
    if (localCharArrayBuffer == null) {
      localCharArrayBuffer = new CharArrayBuffer(i);
    }
    for (;;)
    {
      localCharArrayBuffer.append(paramHeaderElement.getName());
      String str = paramHeaderElement.getValue();
      if (str != null)
      {
        localCharArrayBuffer.append('=');
        doFormatValue(localCharArrayBuffer, str, paramBoolean);
      }
      int j = paramHeaderElement.getParameterCount();
      if (j <= 0) {
        break;
      }
      for (int k = 0; k < j; k++)
      {
        localCharArrayBuffer.append("; ");
        formatNameValuePair(localCharArrayBuffer, paramHeaderElement.getParameter(k), paramBoolean);
      }
      localCharArrayBuffer.ensureCapacity(i);
    }
    return localCharArrayBuffer;
  }
  
  public CharArrayBuffer formatNameValuePair(CharArrayBuffer paramCharArrayBuffer, NameValuePair paramNameValuePair, boolean paramBoolean)
  {
    Args.notNull(paramNameValuePair, "Name / value pair");
    int i = estimateNameValuePairLen(paramNameValuePair);
    CharArrayBuffer localCharArrayBuffer = paramCharArrayBuffer;
    if (localCharArrayBuffer == null) {
      localCharArrayBuffer = new CharArrayBuffer(i);
    }
    for (;;)
    {
      localCharArrayBuffer.append(paramNameValuePair.getName());
      String str = paramNameValuePair.getValue();
      if (str != null)
      {
        localCharArrayBuffer.append('=');
        doFormatValue(localCharArrayBuffer, str, paramBoolean);
      }
      return localCharArrayBuffer;
      localCharArrayBuffer.ensureCapacity(i);
    }
  }
  
  public CharArrayBuffer formatParameters(CharArrayBuffer paramCharArrayBuffer, NameValuePair[] paramArrayOfNameValuePair, boolean paramBoolean)
  {
    Args.notNull(paramArrayOfNameValuePair, "Header parameter array");
    int i = estimateParametersLen(paramArrayOfNameValuePair);
    CharArrayBuffer localCharArrayBuffer = paramCharArrayBuffer;
    if (localCharArrayBuffer == null) {
      localCharArrayBuffer = new CharArrayBuffer(i);
    }
    for (;;)
    {
      for (int j = 0; j < paramArrayOfNameValuePair.length; j++)
      {
        if (j > 0) {
          localCharArrayBuffer.append("; ");
        }
        formatNameValuePair(localCharArrayBuffer, paramArrayOfNameValuePair[j], paramBoolean);
      }
      localCharArrayBuffer.ensureCapacity(i);
    }
    return localCharArrayBuffer;
  }
  
  protected boolean isSeparator(char paramChar)
  {
    return " ;,:@()<>\\\"/[]?={}\t".indexOf(paramChar) >= 0;
  }
  
  protected boolean isUnsafe(char paramChar)
  {
    return "\"\\".indexOf(paramChar) >= 0;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.message.BasicHeaderValueFormatter
 * JD-Core Version:    0.7.0.1
 */