package org.apache.http.client.params;

import org.apache.http.annotation.Immutable;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
@Immutable
public class HttpClientParams
{
  public static long getConnectionManagerTimeout(HttpParams paramHttpParams)
  {
    Args.notNull(paramHttpParams, "HTTP parameters");
    Long localLong = (Long)paramHttpParams.getParameter("http.conn-manager.timeout");
    if (localLong != null) {
      return localLong.longValue();
    }
    return HttpConnectionParams.getConnectionTimeout(paramHttpParams);
  }
  
  public static String getCookiePolicy(HttpParams paramHttpParams)
  {
    Args.notNull(paramHttpParams, "HTTP parameters");
    String str = (String)paramHttpParams.getParameter("http.protocol.cookie-policy");
    if (str == null) {
      str = "best-match";
    }
    return str;
  }
  
  public static boolean isAuthenticating(HttpParams paramHttpParams)
  {
    Args.notNull(paramHttpParams, "HTTP parameters");
    return paramHttpParams.getBooleanParameter("http.protocol.handle-authentication", true);
  }
  
  public static boolean isRedirecting(HttpParams paramHttpParams)
  {
    Args.notNull(paramHttpParams, "HTTP parameters");
    return paramHttpParams.getBooleanParameter("http.protocol.handle-redirects", true);
  }
  
  public static void setAuthenticating(HttpParams paramHttpParams, boolean paramBoolean)
  {
    Args.notNull(paramHttpParams, "HTTP parameters");
    paramHttpParams.setBooleanParameter("http.protocol.handle-authentication", paramBoolean);
  }
  
  public static void setConnectionManagerTimeout(HttpParams paramHttpParams, long paramLong)
  {
    Args.notNull(paramHttpParams, "HTTP parameters");
    paramHttpParams.setLongParameter("http.conn-manager.timeout", paramLong);
  }
  
  public static void setCookiePolicy(HttpParams paramHttpParams, String paramString)
  {
    Args.notNull(paramHttpParams, "HTTP parameters");
    paramHttpParams.setParameter("http.protocol.cookie-policy", paramString);
  }
  
  public static void setRedirecting(HttpParams paramHttpParams, boolean paramBoolean)
  {
    Args.notNull(paramHttpParams, "HTTP parameters");
    paramHttpParams.setBooleanParameter("http.protocol.handle-redirects", paramBoolean);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.params.HttpClientParams
 * JD-Core Version:    0.7.0.1
 */