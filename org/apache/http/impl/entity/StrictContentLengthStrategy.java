package org.apache.http.impl.entity;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.util.Args;

@Immutable
public class StrictContentLengthStrategy
  implements ContentLengthStrategy
{
  public static final StrictContentLengthStrategy INSTANCE = new StrictContentLengthStrategy();
  private final int implicitLen;
  
  public StrictContentLengthStrategy()
  {
    this(-1);
  }
  
  public StrictContentLengthStrategy(int paramInt)
  {
    this.implicitLen = paramInt;
  }
  
  public long determineLength(HttpMessage paramHttpMessage)
    throws HttpException
  {
    Args.notNull(paramHttpMessage, "HTTP message");
    Header localHeader1 = paramHttpMessage.getFirstHeader("Transfer-Encoding");
    String str2;
    long l;
    if (localHeader1 != null)
    {
      str2 = localHeader1.getValue();
      if ("chunked".equalsIgnoreCase(str2))
      {
        if (paramHttpMessage.getProtocolVersion().lessEquals(HttpVersion.HTTP_1_0)) {
          throw new ProtocolException("Chunked transfer encoding not allowed for " + paramHttpMessage.getProtocolVersion());
        }
        l = -2L;
      }
    }
    for (;;)
    {
      return l;
      if ("identity".equalsIgnoreCase(str2)) {
        return -1L;
      }
      throw new ProtocolException("Unsupported transfer encoding: " + str2);
      Header localHeader2 = paramHttpMessage.getFirstHeader("Content-Length");
      if (localHeader2 != null)
      {
        String str1 = localHeader2.getValue();
        try
        {
          l = Long.parseLong(str1);
          if (l < 0L) {
            throw new ProtocolException("Negative content length: " + str1);
          }
        }
        catch (NumberFormatException localNumberFormatException)
        {
          throw new ProtocolException("Invalid content length: " + str1);
        }
      }
    }
    return this.implicitLen;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.entity.StrictContentLengthStrategy
 * JD-Core Version:    0.7.0.1
 */