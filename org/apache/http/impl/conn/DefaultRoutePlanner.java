package org.apache.http.impl.conn;

import java.net.InetAddress;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class DefaultRoutePlanner
  implements HttpRoutePlanner
{
  private final SchemePortResolver schemePortResolver;
  
  public DefaultRoutePlanner(SchemePortResolver paramSchemePortResolver)
  {
    if (paramSchemePortResolver != null) {}
    for (;;)
    {
      this.schemePortResolver = paramSchemePortResolver;
      return;
      paramSchemePortResolver = DefaultSchemePortResolver.INSTANCE;
    }
  }
  
  protected HttpHost determineProxy(HttpHost paramHttpHost, HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws HttpException
  {
    return null;
  }
  
  public HttpRoute determineRoute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws HttpException
  {
    Args.notNull(paramHttpHost, "Target host");
    Args.notNull(paramHttpRequest, "Request");
    RequestConfig localRequestConfig = HttpClientContext.adapt(paramHttpContext).getRequestConfig();
    InetAddress localInetAddress = localRequestConfig.getLocalAddress();
    HttpHost localHttpHost1 = localRequestConfig.getProxy();
    if (localHttpHost1 == null) {
      localHttpHost1 = determineProxy(paramHttpHost, paramHttpRequest, paramHttpContext);
    }
    if (paramHttpHost.getPort() <= 0) {}
    HttpHost localHttpHost2;
    boolean bool;
    for (;;)
    {
      try
      {
        localHttpHost2 = new HttpHost(paramHttpHost.getHostName(), this.schemePortResolver.resolve(paramHttpHost), paramHttpHost.getSchemeName());
        bool = localHttpHost2.getSchemeName().equalsIgnoreCase("https");
        if (localHttpHost1 != null) {
          break;
        }
        return new HttpRoute(localHttpHost2, localInetAddress, bool);
      }
      catch (UnsupportedSchemeException localUnsupportedSchemeException)
      {
        throw new HttpException(localUnsupportedSchemeException.getMessage());
      }
      localHttpHost2 = paramHttpHost;
    }
    return new HttpRoute(localHttpHost2, localInetAddress, localHttpHost1, bool);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.DefaultRoutePlanner
 * JD-Core Version:    0.7.0.1
 */