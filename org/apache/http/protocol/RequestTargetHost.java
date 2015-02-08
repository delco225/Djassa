package org.apache.http.protocol;

import java.io.IOException;
import java.net.InetAddress;
import org.apache.http.HttpConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpInetConnection;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public class RequestTargetHost
  implements HttpRequestInterceptor
{
  public void process(HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    Args.notNull(paramHttpRequest, "HTTP request");
    HttpCoreContext localHttpCoreContext = HttpCoreContext.adapt(paramHttpContext);
    ProtocolVersion localProtocolVersion = paramHttpRequest.getRequestLine().getProtocolVersion();
    if ((paramHttpRequest.getRequestLine().getMethod().equalsIgnoreCase("CONNECT")) && (localProtocolVersion.lessEquals(HttpVersion.HTTP_1_0))) {}
    HttpHost localHttpHost;
    do
    {
      do
      {
        return;
      } while (paramHttpRequest.containsHeader("Host"));
      localHttpHost = localHttpCoreContext.getTargetHost();
      if (localHttpHost != null) {
        break;
      }
      HttpConnection localHttpConnection = localHttpCoreContext.getConnection();
      if ((localHttpConnection instanceof HttpInetConnection))
      {
        InetAddress localInetAddress = ((HttpInetConnection)localHttpConnection).getRemoteAddress();
        int i = ((HttpInetConnection)localHttpConnection).getRemotePort();
        if (localInetAddress != null) {
          localHttpHost = new HttpHost(localInetAddress.getHostName(), i);
        }
      }
      if (localHttpHost != null) {
        break;
      }
    } while (localProtocolVersion.lessEquals(HttpVersion.HTTP_1_0));
    throw new ProtocolException("Target host missing");
    paramHttpRequest.addHeader("Host", localHttpHost.toHostString());
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.protocol.RequestTargetHost
 * JD-Core Version:    0.7.0.1
 */