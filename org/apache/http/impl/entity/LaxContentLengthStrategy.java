package org.apache.http.impl.entity;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.ParseException;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.util.Args;

@Immutable
public class LaxContentLengthStrategy
  implements ContentLengthStrategy
{
  public static final LaxContentLengthStrategy INSTANCE = new LaxContentLengthStrategy();
  private final int implicitLen;
  
  public LaxContentLengthStrategy()
  {
    this(-1);
  }
  
  public LaxContentLengthStrategy(int paramInt)
  {
    this.implicitLen = paramInt;
  }
  
  public long determineLength(HttpMessage paramHttpMessage)
    throws HttpException
  {
    Args.notNull(paramHttpMessage, "HTTP message");
    Header localHeader1 = paramHttpMessage.getFirstHeader("Transfer-Encoding");
    long l1;
    if (localHeader1 != null)
    {
      HeaderElement[] arrayOfHeaderElement;
      int j;
      try
      {
        arrayOfHeaderElement = localHeader1.getElements();
        j = arrayOfHeaderElement.length;
        if ("identity".equalsIgnoreCase(localHeader1.getValue()))
        {
          l1 = -1L;
          return l1;
        }
      }
      catch (ParseException localParseException)
      {
        throw new ProtocolException("Invalid Transfer-Encoding header value: " + localHeader1, localParseException);
      }
      if ((j > 0) && ("chunked".equalsIgnoreCase(arrayOfHeaderElement[(j - 1)].getName()))) {
        return -2L;
      }
      return -1L;
    }
    if (paramHttpMessage.getFirstHeader("Content-Length") != null)
    {
      l1 = -1L;
      Header[] arrayOfHeader = paramHttpMessage.getHeaders("Content-Length");
      int i = -1 + arrayOfHeader.length;
      for (;;)
      {
        for (;;)
        {
          Header localHeader2;
          if (i >= 0) {
            localHeader2 = arrayOfHeader[i];
          }
          try
          {
            long l2 = Long.parseLong(localHeader2.getValue());
            l1 = l2;
            if (l1 >= 0L) {
              break;
            }
            return -1L;
          }
          catch (NumberFormatException localNumberFormatException)
          {
            i--;
          }
        }
      }
    }
    return this.implicitLen;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.entity.LaxContentLengthStrategy
 * JD-Core Version:    0.7.0.1
 */