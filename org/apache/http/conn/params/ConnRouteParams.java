package org.apache.http.conn.params;

import java.net.InetAddress;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
@Immutable
public class ConnRouteParams
  implements ConnRoutePNames
{
  public static final HttpHost NO_HOST = new HttpHost("127.0.0.255", 0, "no-host");
  public static final HttpRoute NO_ROUTE = new HttpRoute(NO_HOST);
  
  public static HttpHost getDefaultProxy(HttpParams paramHttpParams)
  {
    Args.notNull(paramHttpParams, "Parameters");
    HttpHost localHttpHost = (HttpHost)paramHttpParams.getParameter("http.route.default-proxy");
    if ((localHttpHost != null) && (NO_HOST.equals(localHttpHost))) {
      localHttpHost = null;
    }
    return localHttpHost;
  }
  
  public static HttpRoute getForcedRoute(HttpParams paramHttpParams)
  {
    Args.notNull(paramHttpParams, "Parameters");
    HttpRoute localHttpRoute = (HttpRoute)paramHttpParams.getParameter("http.route.forced-route");
    if ((localHttpRoute != null) && (NO_ROUTE.equals(localHttpRoute))) {
      localHttpRoute = null;
    }
    return localHttpRoute;
  }
  
  public static InetAddress getLocalAddress(HttpParams paramHttpParams)
  {
    Args.notNull(paramHttpParams, "Parameters");
    return (InetAddress)paramHttpParams.getParameter("http.route.local-address");
  }
  
  public static void setDefaultProxy(HttpParams paramHttpParams, HttpHost paramHttpHost)
  {
    Args.notNull(paramHttpParams, "Parameters");
    paramHttpParams.setParameter("http.route.default-proxy", paramHttpHost);
  }
  
  public static void setForcedRoute(HttpParams paramHttpParams, HttpRoute paramHttpRoute)
  {
    Args.notNull(paramHttpParams, "Parameters");
    paramHttpParams.setParameter("http.route.forced-route", paramHttpRoute);
  }
  
  public static void setLocalAddress(HttpParams paramHttpParams, InetAddress paramInetAddress)
  {
    Args.notNull(paramHttpParams, "Parameters");
    paramHttpParams.setParameter("http.route.local-address", paramInetAddress);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.params.ConnRouteParams
 * JD-Core Version:    0.7.0.1
 */