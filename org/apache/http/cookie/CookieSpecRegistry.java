package org.apache.http.cookie;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.config.Lookup;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Deprecated
@ThreadSafe
public final class CookieSpecRegistry
  implements Lookup<CookieSpecProvider>
{
  private final ConcurrentHashMap<String, CookieSpecFactory> registeredSpecs = new ConcurrentHashMap();
  
  public CookieSpec getCookieSpec(String paramString)
    throws IllegalStateException
  {
    return getCookieSpec(paramString, null);
  }
  
  public CookieSpec getCookieSpec(String paramString, HttpParams paramHttpParams)
    throws IllegalStateException
  {
    Args.notNull(paramString, "Name");
    CookieSpecFactory localCookieSpecFactory = (CookieSpecFactory)this.registeredSpecs.get(paramString.toLowerCase(Locale.ENGLISH));
    if (localCookieSpecFactory != null) {
      return localCookieSpecFactory.newInstance(paramHttpParams);
    }
    throw new IllegalStateException("Unsupported cookie spec: " + paramString);
  }
  
  public List<String> getSpecNames()
  {
    return new ArrayList(this.registeredSpecs.keySet());
  }
  
  public CookieSpecProvider lookup(final String paramString)
  {
    new CookieSpecProvider()
    {
      public CookieSpec create(HttpContext paramAnonymousHttpContext)
      {
        HttpRequest localHttpRequest = (HttpRequest)paramAnonymousHttpContext.getAttribute("http.request");
        return CookieSpecRegistry.this.getCookieSpec(paramString, localHttpRequest.getParams());
      }
    };
  }
  
  public void register(String paramString, CookieSpecFactory paramCookieSpecFactory)
  {
    Args.notNull(paramString, "Name");
    Args.notNull(paramCookieSpecFactory, "Cookie spec factory");
    this.registeredSpecs.put(paramString.toLowerCase(Locale.ENGLISH), paramCookieSpecFactory);
  }
  
  public void setItems(Map<String, CookieSpecFactory> paramMap)
  {
    if (paramMap == null) {
      return;
    }
    this.registeredSpecs.clear();
    this.registeredSpecs.putAll(paramMap);
  }
  
  public void unregister(String paramString)
  {
    Args.notNull(paramString, "Id");
    this.registeredSpecs.remove(paramString.toLowerCase(Locale.ENGLISH));
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.cookie.CookieSpecRegistry
 * JD-Core Version:    0.7.0.1
 */