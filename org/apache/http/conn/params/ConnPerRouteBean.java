package org.apache.http.conn.params;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;

@Deprecated
@ThreadSafe
public final class ConnPerRouteBean
  implements ConnPerRoute
{
  public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 2;
  private volatile int defaultMax;
  private final ConcurrentHashMap<HttpRoute, Integer> maxPerHostMap = new ConcurrentHashMap();
  
  public ConnPerRouteBean()
  {
    this(2);
  }
  
  public ConnPerRouteBean(int paramInt)
  {
    setDefaultMaxPerRoute(paramInt);
  }
  
  public int getDefaultMax()
  {
    return this.defaultMax;
  }
  
  public int getDefaultMaxPerRoute()
  {
    return this.defaultMax;
  }
  
  public int getMaxForRoute(HttpRoute paramHttpRoute)
  {
    Args.notNull(paramHttpRoute, "HTTP route");
    Integer localInteger = (Integer)this.maxPerHostMap.get(paramHttpRoute);
    if (localInteger != null) {
      return localInteger.intValue();
    }
    return this.defaultMax;
  }
  
  public void setDefaultMaxPerRoute(int paramInt)
  {
    Args.positive(paramInt, "Defautl max per route");
    this.defaultMax = paramInt;
  }
  
  public void setMaxForRoute(HttpRoute paramHttpRoute, int paramInt)
  {
    Args.notNull(paramHttpRoute, "HTTP route");
    Args.positive(paramInt, "Max per route");
    this.maxPerHostMap.put(paramHttpRoute, Integer.valueOf(paramInt));
  }
  
  public void setMaxForRoutes(Map<HttpRoute, Integer> paramMap)
  {
    if (paramMap == null) {
      return;
    }
    this.maxPerHostMap.clear();
    this.maxPerHostMap.putAll(paramMap);
  }
  
  public String toString()
  {
    return this.maxPerHostMap.toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.params.ConnPerRouteBean
 * JD-Core Version:    0.7.0.1
 */