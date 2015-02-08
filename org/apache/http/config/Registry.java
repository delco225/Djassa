package org.apache.http.config;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
public final class Registry<I>
  implements Lookup<I>
{
  private final Map<String, I> map;
  
  Registry(Map<String, I> paramMap)
  {
    this.map = new ConcurrentHashMap(paramMap);
  }
  
  public I lookup(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    return this.map.get(paramString.toLowerCase(Locale.US));
  }
  
  public String toString()
  {
    return this.map.toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.config.Registry
 * JD-Core Version:    0.7.0.1
 */