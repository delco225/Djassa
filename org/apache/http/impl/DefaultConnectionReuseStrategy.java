package org.apache.http.impl;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.TokenIterator;
import org.apache.http.annotation.Immutable;
import org.apache.http.message.BasicTokenIterator;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class DefaultConnectionReuseStrategy
  implements ConnectionReuseStrategy
{
  public static final DefaultConnectionReuseStrategy INSTANCE = new DefaultConnectionReuseStrategy();
  
  private boolean canResponseHaveBody(HttpResponse paramHttpResponse)
  {
    int i = paramHttpResponse.getStatusLine().getStatusCode();
    return (i >= 200) && (i != 204) && (i != 304) && (i != 205);
  }
  
  protected TokenIterator createTokenIterator(HeaderIterator paramHeaderIterator)
  {
    return new BasicTokenIterator(paramHeaderIterator);
  }
  
  public boolean keepAlive(HttpResponse paramHttpResponse, HttpContext paramHttpContext)
  {
    Args.notNull(paramHttpResponse, "HTTP response");
    Args.notNull(paramHttpContext, "HTTP context");
    ProtocolVersion localProtocolVersion = paramHttpResponse.getStatusLine().getProtocolVersion();
    Header localHeader1 = paramHttpResponse.getFirstHeader("Transfer-Encoding");
    if (localHeader1 != null)
    {
      if (!"chunked".equalsIgnoreCase(localHeader1.getValue())) {
        return false;
      }
    }
    else if (canResponseHaveBody(paramHttpResponse))
    {
      Header[] arrayOfHeader = paramHttpResponse.getHeaders("Content-Length");
      if (arrayOfHeader.length == 1)
      {
        Header localHeader2 = arrayOfHeader[0];
        try
        {
          int j = Integer.parseInt(localHeader2.getValue());
          if (j >= 0) {
            break label115;
          }
          return false;
        }
        catch (NumberFormatException localNumberFormatException)
        {
          return false;
        }
      }
      else
      {
        return false;
      }
    }
    label115:
    HeaderIterator localHeaderIterator = paramHttpResponse.headerIterator("Connection");
    if (!localHeaderIterator.hasNext()) {
      localHeaderIterator = paramHttpResponse.headerIterator("Proxy-Connection");
    }
    if (localHeaderIterator.hasNext()) {
      try
      {
        TokenIterator localTokenIterator = createTokenIterator(localHeaderIterator);
        int i = 0;
        while (localTokenIterator.hasNext())
        {
          String str = localTokenIterator.nextToken();
          if ("Close".equalsIgnoreCase(str)) {
            return false;
          }
          boolean bool = "Keep-Alive".equalsIgnoreCase(str);
          if (bool) {
            i = 1;
          }
        }
        if (i != 0) {
          return true;
        }
      }
      catch (ParseException localParseException)
      {
        return false;
      }
    }
    return !localProtocolVersion.lessEquals(HttpVersion.HTTP_1_0);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.DefaultConnectionReuseStrategy
 * JD-Core Version:    0.7.0.1
 */