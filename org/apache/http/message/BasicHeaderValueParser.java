package org.apache.http.message;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.annotation.Immutable;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Immutable
public class BasicHeaderValueParser
  implements HeaderValueParser
{
  private static final char[] ALL_DELIMITERS = { 59, 44 };
  @Deprecated
  public static final BasicHeaderValueParser DEFAULT = new BasicHeaderValueParser();
  private static final char ELEM_DELIMITER = ',';
  public static final BasicHeaderValueParser INSTANCE = new BasicHeaderValueParser();
  private static final char PARAM_DELIMITER = ';';
  
  private static boolean isOneOf(char paramChar, char[] paramArrayOfChar)
  {
    if (paramArrayOfChar != null)
    {
      int i = paramArrayOfChar.length;
      for (int j = 0; j < i; j++) {
        if (paramChar == paramArrayOfChar[j]) {
          return true;
        }
      }
    }
    return false;
  }
  
  public static HeaderElement[] parseElements(String paramString, HeaderValueParser paramHeaderValueParser)
    throws ParseException
  {
    Args.notNull(paramString, "Value");
    CharArrayBuffer localCharArrayBuffer = new CharArrayBuffer(paramString.length());
    localCharArrayBuffer.append(paramString);
    ParserCursor localParserCursor = new ParserCursor(0, paramString.length());
    if (paramHeaderValueParser != null) {}
    for (;;)
    {
      return paramHeaderValueParser.parseElements(localCharArrayBuffer, localParserCursor);
      paramHeaderValueParser = INSTANCE;
    }
  }
  
  public static HeaderElement parseHeaderElement(String paramString, HeaderValueParser paramHeaderValueParser)
    throws ParseException
  {
    Args.notNull(paramString, "Value");
    CharArrayBuffer localCharArrayBuffer = new CharArrayBuffer(paramString.length());
    localCharArrayBuffer.append(paramString);
    ParserCursor localParserCursor = new ParserCursor(0, paramString.length());
    if (paramHeaderValueParser != null) {}
    for (;;)
    {
      return paramHeaderValueParser.parseHeaderElement(localCharArrayBuffer, localParserCursor);
      paramHeaderValueParser = INSTANCE;
    }
  }
  
  public static NameValuePair parseNameValuePair(String paramString, HeaderValueParser paramHeaderValueParser)
    throws ParseException
  {
    Args.notNull(paramString, "Value");
    CharArrayBuffer localCharArrayBuffer = new CharArrayBuffer(paramString.length());
    localCharArrayBuffer.append(paramString);
    ParserCursor localParserCursor = new ParserCursor(0, paramString.length());
    if (paramHeaderValueParser != null) {}
    for (;;)
    {
      return paramHeaderValueParser.parseNameValuePair(localCharArrayBuffer, localParserCursor);
      paramHeaderValueParser = INSTANCE;
    }
  }
  
  public static NameValuePair[] parseParameters(String paramString, HeaderValueParser paramHeaderValueParser)
    throws ParseException
  {
    Args.notNull(paramString, "Value");
    CharArrayBuffer localCharArrayBuffer = new CharArrayBuffer(paramString.length());
    localCharArrayBuffer.append(paramString);
    ParserCursor localParserCursor = new ParserCursor(0, paramString.length());
    if (paramHeaderValueParser != null) {}
    for (;;)
    {
      return paramHeaderValueParser.parseParameters(localCharArrayBuffer, localParserCursor);
      paramHeaderValueParser = INSTANCE;
    }
  }
  
  protected HeaderElement createHeaderElement(String paramString1, String paramString2, NameValuePair[] paramArrayOfNameValuePair)
  {
    return new BasicHeaderElement(paramString1, paramString2, paramArrayOfNameValuePair);
  }
  
  protected NameValuePair createNameValuePair(String paramString1, String paramString2)
  {
    return new BasicNameValuePair(paramString1, paramString2);
  }
  
  public HeaderElement[] parseElements(CharArrayBuffer paramCharArrayBuffer, ParserCursor paramParserCursor)
  {
    Args.notNull(paramCharArrayBuffer, "Char array buffer");
    Args.notNull(paramParserCursor, "Parser cursor");
    ArrayList localArrayList = new ArrayList();
    while (!paramParserCursor.atEnd())
    {
      HeaderElement localHeaderElement = parseHeaderElement(paramCharArrayBuffer, paramParserCursor);
      if ((localHeaderElement.getName().length() != 0) || (localHeaderElement.getValue() != null)) {
        localArrayList.add(localHeaderElement);
      }
    }
    return (HeaderElement[])localArrayList.toArray(new HeaderElement[localArrayList.size()]);
  }
  
  public HeaderElement parseHeaderElement(CharArrayBuffer paramCharArrayBuffer, ParserCursor paramParserCursor)
  {
    Args.notNull(paramCharArrayBuffer, "Char array buffer");
    Args.notNull(paramParserCursor, "Parser cursor");
    NameValuePair localNameValuePair = parseNameValuePair(paramCharArrayBuffer, paramParserCursor);
    boolean bool = paramParserCursor.atEnd();
    NameValuePair[] arrayOfNameValuePair = null;
    if (!bool)
    {
      int i = paramCharArrayBuffer.charAt(-1 + paramParserCursor.getPos());
      arrayOfNameValuePair = null;
      if (i != 44) {
        arrayOfNameValuePair = parseParameters(paramCharArrayBuffer, paramParserCursor);
      }
    }
    return createHeaderElement(localNameValuePair.getName(), localNameValuePair.getValue(), arrayOfNameValuePair);
  }
  
  public NameValuePair parseNameValuePair(CharArrayBuffer paramCharArrayBuffer, ParserCursor paramParserCursor)
  {
    return parseNameValuePair(paramCharArrayBuffer, paramParserCursor, ALL_DELIMITERS);
  }
  
  public NameValuePair parseNameValuePair(CharArrayBuffer paramCharArrayBuffer, ParserCursor paramParserCursor, char[] paramArrayOfChar)
  {
    Args.notNull(paramCharArrayBuffer, "Char array buffer");
    Args.notNull(paramParserCursor, "Parser cursor");
    int i = paramParserCursor.getPos();
    int j = paramParserCursor.getPos();
    int k = paramParserCursor.getUpperBound();
    int m = 0;
    char c2;
    label60:
    String str1;
    if (i < k)
    {
      c2 = paramCharArrayBuffer.charAt(i);
      m = 0;
      if (c2 != '=') {}
    }
    else
    {
      if (i != k) {
        break label120;
      }
      m = 1;
      str1 = paramCharArrayBuffer.substringTrimmed(j, k);
    }
    for (;;)
    {
      if (m == 0) {
        break label136;
      }
      paramParserCursor.updatePos(i);
      return createNameValuePair(str1, null);
      if (isOneOf(c2, paramArrayOfChar))
      {
        m = 1;
        break label60;
      }
      i++;
      break;
      label120:
      str1 = paramCharArrayBuffer.substringTrimmed(j, i);
      i++;
    }
    label136:
    int n = i;
    int i1 = 0;
    int i2 = 0;
    char c1;
    int i3;
    for (;;)
    {
      if (i < k)
      {
        c1 = paramCharArrayBuffer.charAt(i);
        if ((c1 == '"') && (i2 == 0)) {
          if (i1 != 0) {
            break label232;
          }
        }
      }
      label232:
      for (i1 = 1; (i1 == 0) && (i2 == 0) && (isOneOf(c1, paramArrayOfChar)); i1 = 0)
      {
        m = 1;
        i3 = i;
        while ((n < i3) && (HTTP.isWhitespace(paramCharArrayBuffer.charAt(n)))) {
          n++;
        }
      }
      if (i2 == 0) {
        break;
      }
      i2 = 0;
      i++;
    }
    if ((i1 != 0) && (c1 == '\\')) {}
    for (i2 = 1;; i2 = 0) {
      break;
    }
    while ((i3 > n) && (HTTP.isWhitespace(paramCharArrayBuffer.charAt(i3 - 1)))) {
      i3--;
    }
    if ((i3 - n >= 2) && (paramCharArrayBuffer.charAt(n) == '"') && (paramCharArrayBuffer.charAt(i3 - 1) == '"'))
    {
      n++;
      i3--;
    }
    String str2 = paramCharArrayBuffer.substring(n, i3);
    if (m != 0) {
      i++;
    }
    paramParserCursor.updatePos(i);
    return createNameValuePair(str1, str2);
  }
  
  public NameValuePair[] parseParameters(CharArrayBuffer paramCharArrayBuffer, ParserCursor paramParserCursor)
  {
    Args.notNull(paramCharArrayBuffer, "Char array buffer");
    Args.notNull(paramParserCursor, "Parser cursor");
    int i = paramParserCursor.getPos();
    int j = paramParserCursor.getUpperBound();
    while ((i < j) && (HTTP.isWhitespace(paramCharArrayBuffer.charAt(i)))) {
      i++;
    }
    paramParserCursor.updatePos(i);
    if (paramParserCursor.atEnd()) {
      return new NameValuePair[0];
    }
    ArrayList localArrayList = new ArrayList();
    do
    {
      if (paramParserCursor.atEnd()) {
        break;
      }
      localArrayList.add(parseNameValuePair(paramCharArrayBuffer, paramParserCursor));
    } while (paramCharArrayBuffer.charAt(-1 + paramParserCursor.getPos()) != ',');
    return (NameValuePair[])localArrayList.toArray(new NameValuePair[localArrayList.size()]);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.message.BasicHeaderValueParser
 * JD-Core Version:    0.7.0.1
 */