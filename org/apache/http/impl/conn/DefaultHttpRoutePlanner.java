package org.apache.http.impl.conn;

import java.net.InetAddress;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
@ThreadSafe
public class DefaultHttpRoutePlanner
  implements HttpRoutePlanner
{
  protected final SchemeRegistry schemeRegistry;
  
  public DefaultHttpRoutePlanner(SchemeRegistry paramSchemeRegistry)
  {
    Args.notNull(paramSchemeRegistry, "Scheme registry");
    this.schemeRegistry = paramSchemeRegistry;
  }
  
  public HttpRoute determineRoute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws HttpException
  {
    Args.notNull(paramHttpRequest, "HTTP request");
    HttpRoute localHttpRoute1 = ConnRouteParams.getForcedRoute(paramHttpRequest.getParams());
    if (localHttpRoute1 != null) {
      return localHttpRoute1;
    }
    Asserts.notNull(paramHttpHost, "Target host");
    InetAddress localInetAddress = ConnRouteParams.getLocalAddress(paramHttpRequest.getParams());
    HttpHost localHttpHost = ConnRouteParams.getDefaultProxy(paramHttpRequest.getParams());
    for (;;)
    {
      boolean bool;
      try
      {
        Scheme localScheme = this.schemeRegistry.getScheme(paramHttpHost.getSchemeName());
        bool = localScheme.isLayered();
        if (localHttpHost == null)
        {
          localHttpRoute2 = new HttpRoute(paramHttpHost, localInetAddress, bool);
          return localHttpRoute2;
        }
      }
      catch (IllegalStateException localIllegalStateException)
      {
        throw new HttpException(localIllegalStateException.getMessage());
      }
      HttpRoute localHttpRoute2 = new HttpRoute(paramHttpHost, localInetAddress, localHttpHost, bool);
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.DefaultHttpRoutePlanner
 * JD-Core Version:    0.7.0.1
 */