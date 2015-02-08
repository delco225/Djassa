package org.apache.http.impl.client;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class DefaultConnectionKeepAliveStrategy
  implements ConnectionKeepAliveStrategy
{
  public static final DefaultConnectionKeepAliveStrategy INSTANCE = new DefaultConnectionKeepAliveStrategy();
  
  public long getKeepAliveDuration(HttpResponse paramHttpResponse, HttpContext paramHttpContext)
  {
    Args.notNull(paramHttpResponse, "HTTP response");
    BasicHeaderElementIterator localBasicHeaderElementIterator = new BasicHeaderElementIterator(paramHttpResponse.headerIterator("Keep-Alive"));
    for (;;)
    {
      String str2;
      if (localBasicHeaderElementIterator.hasNext())
      {
        HeaderElement localHeaderElement = localBasicHeaderElementIterator.nextElement();
        String str1 = localHeaderElement.getName();
        str2 = localHeaderElement.getValue();
        if ((str2 == null) || (!str1.equalsIgnoreCase("timeout"))) {}
      }
      else
      {
        try
        {
          long l = Long.parseLong(str2);
          return l * 1000L;
        }
        catch (NumberFormatException localNumberFormatException) {}
        return -1L;
      }
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy
 * JD-Core Version:    0.7.0.1
 */