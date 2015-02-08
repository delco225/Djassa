package org.apache.http.protocol;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.util.Args;

@ThreadSafe
public class UriPatternMatcher<T>
{
  @GuardedBy("this")
  private final Map<String, T> map = new HashMap();
  
  @Deprecated
  public Map<String, T> getObjects()
  {
    try
    {
      Map localMap = this.map;
      return localMap;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public T lookup(String paramString)
  {
    try
    {
      Args.notNull(paramString, "Request path");
      Object localObject2 = this.map.get(paramString);
      if (localObject2 == null)
      {
        Object localObject3 = null;
        Iterator localIterator = this.map.keySet().iterator();
        while (localIterator.hasNext())
        {
          String str = (String)localIterator.next();
          if ((matchUriRequestPattern(str, paramString)) && ((localObject3 == null) || (localObject3.length() < str.length()) || ((localObject3.length() == str.length()) && (str.endsWith("*")))))
          {
            Object localObject4 = this.map.get(str);
            localObject2 = localObject4;
            localObject3 = str;
          }
        }
      }
      return localObject2;
    }
    finally {}
  }
  
  protected boolean matchUriRequestPattern(String paramString1, String paramString2)
  {
    if (paramString1.equals("*")) {
      return true;
    }
    boolean bool2;
    if ((!paramString1.endsWith("*")) || (!paramString2.startsWith(paramString1.substring(0, -1 + paramString1.length()))))
    {
      boolean bool1 = paramString1.startsWith("*");
      bool2 = false;
      if (bool1)
      {
        boolean bool3 = paramString2.endsWith(paramString1.substring(1, paramString1.length()));
        bool2 = false;
        if (!bool3) {}
      }
    }
    else
    {
      bool2 = true;
    }
    return bool2;
  }
  
  public void register(String paramString, T paramT)
  {
    try
    {
      Args.notNull(paramString, "URI request pattern");
      this.map.put(paramString, paramT);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  @Deprecated
  public void setHandlers(Map<String, T> paramMap)
  {
    try
    {
      Args.notNull(paramMap, "Map of handlers");
      this.map.clear();
      this.map.putAll(paramMap);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  @Deprecated
  public void setObjects(Map<String, T> paramMap)
  {
    try
    {
      Args.notNull(paramMap, "Map of handlers");
      this.map.clear();
      this.map.putAll(paramMap);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public String toString()
  {
    return this.map.toString();
  }
  
  public void unregister(String paramString)
  {
    if (paramString == null) {}
    for (;;)
    {
      return;
      try
      {
        this.map.remove(paramString);
      }
      finally {}
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.protocol.UriPatternMatcher
 * JD-Core Version:    0.7.0.1
 */