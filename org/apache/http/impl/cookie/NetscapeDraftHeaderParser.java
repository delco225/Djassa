package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.annotation.Immutable;
import org.apache.http.message.BasicHeaderElement;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.ParserCursor;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Immutable
public class NetscapeDraftHeaderParser
{
  public static final NetscapeDraftHeaderParser DEFAULT = new NetscapeDraftHeaderParser();
  
  private NameValuePair parseNameValuePair(CharArrayBuffer paramCharArrayBuffer, ParserCursor paramParserCursor)
  {
    int i = paramParserCursor.getPos();
    int j = paramParserCursor.getPos();
    int k = paramParserCursor.getUpperBound();
    int m = 0;
    int i2;
    label43:
    String str1;
    if (i < k)
    {
      i2 = paramCharArrayBuffer.charAt(i);
      m = 0;
      if (i2 != 61) {}
    }
    else
    {
      if (i != k) {
        break label102;
      }
      m = 1;
      str1 = paramCharArrayBuffer.substringTrimmed(j, k);
    }
    for (;;)
    {
      if (m == 0) {
        break label117;
      }
      paramParserCursor.updatePos(i);
      return new BasicNameValuePair(str1, null);
      if (i2 == 59)
      {
        m = 1;
        break label43;
      }
      i++;
      break;
      label102:
      str1 = paramCharArrayBuffer.substringTrimmed(j, i);
      i++;
    }
    label117:
    int n = i;
    int i1;
    for (;;)
    {
      if (i < k)
      {
        if (paramCharArrayBuffer.charAt(i) == ';') {
          m = 1;
        }
      }
      else
      {
        i1 = i;
        while ((n < i1) && (HTTP.isWhitespace(paramCharArrayBuffer.charAt(n)))) {
          n++;
        }
      }
      i++;
    }
    while ((i1 > n) && (HTTP.isWhitespace(paramCharArrayBuffer.charAt(i1 - 1)))) {
      i1--;
    }
    String str2 = paramCharArrayBuffer.substring(n, i1);
    if (m != 0) {
      i++;
    }
    paramParserCursor.updatePos(i);
    return new BasicNameValuePair(str1, str2);
  }
  
  public HeaderElement parseHeader(CharArrayBuffer paramCharArrayBuffer, ParserCursor paramParserCursor)
    throws ParseException
  {
    Args.notNull(paramCharArrayBuffer, "Char array buffer");
    Args.notNull(paramParserCursor, "Parser cursor");
    NameValuePair localNameValuePair = parseNameValuePair(paramCharArrayBuffer, paramParserCursor);
    ArrayList localArrayList = new ArrayList();
    while (!paramParserCursor.atEnd()) {
      localArrayList.add(parseNameValuePair(paramCharArrayBuffer, paramParserCursor));
    }
    return new BasicHeaderElement(localNameValuePair.getName(), localNameValuePair.getValue(), (NameValuePair[])localArrayList.toArray(new NameValuePair[localArrayList.size()]));
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.cookie.NetscapeDraftHeaderParser
 * JD-Core Version:    0.7.0.1
 */