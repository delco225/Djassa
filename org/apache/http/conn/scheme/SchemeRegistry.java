package org.apache.http.conn.scheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.util.Args;

@Deprecated
@ThreadSafe
public final class SchemeRegistry
{
  private final ConcurrentHashMap<String, Scheme> registeredSchemes = new ConcurrentHashMap();
  
  public final Scheme get(String paramString)
  {
    Args.notNull(paramString, "Scheme name");
    return (Scheme)this.registeredSchemes.get(paramString);
  }
  
  public final Scheme getScheme(String paramString)
  {
    Scheme localScheme = get(paramString);
    if (localScheme == null) {
      throw new IllegalStateException("Scheme '" + paramString + "' not registered.");
    }
    return localScheme;
  }
  
  public final Scheme getScheme(HttpHost paramHttpHost)
  {
    Args.notNull(paramHttpHost, "Host");
    return getScheme(paramHttpHost.getSchemeName());
  }
  
  public final List<String> getSchemeNames()
  {
    return new ArrayList(this.registeredSchemes.keySet());
  }
  
  public final Scheme register(Scheme paramScheme)
  {
    Args.notNull(paramScheme, "Scheme");
    return (Scheme)this.registeredSchemes.put(paramScheme.getName(), paramScheme);
  }
  
  public void setItems(Map<String, Scheme> paramMap)
  {
    if (paramMap == null) {
      return;
    }
    this.registeredSchemes.clear();
    this.registeredSchemes.putAll(paramMap);
  }
  
  public final Scheme unregister(String paramString)
  {
    Args.notNull(paramString, "Scheme name");
    return (Scheme)this.registeredSchemes.remove(paramString);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.scheme.SchemeRegistry
 * JD-Core Version:    0.7.0.1
 */