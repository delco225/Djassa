package org.apache.http.impl.client;

import java.util.HashMap;
import org.apache.http.HttpHost;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthScheme;
import org.apache.http.client.AuthCache;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.util.Args;

@NotThreadSafe
public class BasicAuthCache
  implements AuthCache
{
  private final HashMap<HttpHost, AuthScheme> map = new HashMap();
  private final SchemePortResolver schemePortResolver;
  
  public BasicAuthCache()
  {
    this(null);
  }
  
  public BasicAuthCache(SchemePortResolver paramSchemePortResolver)
  {
    if (paramSchemePortResolver != null) {}
    for (;;)
    {
      this.schemePortResolver = paramSchemePortResolver;
      return;
      paramSchemePortResolver = DefaultSchemePortResolver.INSTANCE;
    }
  }
  
  public void clear()
  {
    this.map.clear();
  }
  
  public AuthScheme get(HttpHost paramHttpHost)
  {
    Args.notNull(paramHttpHost, "HTTP host");
    return (AuthScheme)this.map.get(getKey(paramHttpHost));
  }
  
  protected HttpHost getKey(HttpHost paramHttpHost)
  {
    if (paramHttpHost.getPort() <= 0) {}
    try
    {
      int i = this.schemePortResolver.resolve(paramHttpHost);
      paramHttpHost = new HttpHost(paramHttpHost.getHostName(), i, paramHttpHost.getSchemeName());
      return paramHttpHost;
    }
    catch (UnsupportedSchemeException localUnsupportedSchemeException) {}
    return paramHttpHost;
  }
  
  public void put(HttpHost paramHttpHost, AuthScheme paramAuthScheme)
  {
    Args.notNull(paramHttpHost, "HTTP host");
    this.map.put(getKey(paramHttpHost), paramAuthScheme);
  }
  
  public void remove(HttpHost paramHttpHost)
  {
    Args.notNull(paramHttpHost, "HTTP host");
    this.map.remove(getKey(paramHttpHost));
  }
  
  public String toString()
  {
    return this.map.toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.BasicAuthCache
 * JD-Core Version:    0.7.0.1
 */