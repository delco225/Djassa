package org.apache.http.impl.client;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.BackoffManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.util.Args;

public class AIMDBackoffManager
  implements BackoffManager
{
  private double backoffFactor = 0.5D;
  private int cap = 2;
  private final Clock clock;
  private final ConnPoolControl<HttpRoute> connPerRoute;
  private long coolDown = 5000L;
  private final Map<HttpRoute, Long> lastRouteBackoffs;
  private final Map<HttpRoute, Long> lastRouteProbes;
  
  public AIMDBackoffManager(ConnPoolControl<HttpRoute> paramConnPoolControl)
  {
    this(paramConnPoolControl, new SystemClock());
  }
  
  AIMDBackoffManager(ConnPoolControl<HttpRoute> paramConnPoolControl, Clock paramClock)
  {
    this.clock = paramClock;
    this.connPerRoute = paramConnPoolControl;
    this.lastRouteProbes = new HashMap();
    this.lastRouteBackoffs = new HashMap();
  }
  
  private int getBackedOffPoolSize(int paramInt)
  {
    if (paramInt <= 1) {
      return 1;
    }
    return (int)Math.floor(this.backoffFactor * paramInt);
  }
  
  private Long getLastUpdate(Map<HttpRoute, Long> paramMap, HttpRoute paramHttpRoute)
  {
    Long localLong = (Long)paramMap.get(paramHttpRoute);
    if (localLong == null) {
      localLong = Long.valueOf(0L);
    }
    return localLong;
  }
  
  public void backOff(HttpRoute paramHttpRoute)
  {
    synchronized (this.connPerRoute)
    {
      int i = this.connPerRoute.getMaxPerRoute(paramHttpRoute);
      Long localLong = getLastUpdate(this.lastRouteBackoffs, paramHttpRoute);
      long l = this.clock.getCurrentTime();
      if (l - localLong.longValue() < this.coolDown) {
        return;
      }
      this.connPerRoute.setMaxPerRoute(paramHttpRoute, getBackedOffPoolSize(i));
      this.lastRouteBackoffs.put(paramHttpRoute, Long.valueOf(l));
      return;
    }
  }
  
  public void probe(HttpRoute paramHttpRoute)
  {
    for (;;)
    {
      int i;
      synchronized (this.connPerRoute)
      {
        i = this.connPerRoute.getMaxPerRoute(paramHttpRoute);
        if (i >= this.cap)
        {
          j = this.cap;
          Long localLong1 = getLastUpdate(this.lastRouteProbes, paramHttpRoute);
          Long localLong2 = getLastUpdate(this.lastRouteBackoffs, paramHttpRoute);
          long l = this.clock.getCurrentTime();
          if ((l - localLong1.longValue() < this.coolDown) || (l - localLong2.longValue() < this.coolDown)) {
            return;
          }
          this.connPerRoute.setMaxPerRoute(paramHttpRoute, j);
          this.lastRouteProbes.put(paramHttpRoute, Long.valueOf(l));
          return;
        }
      }
      int j = i + 1;
    }
  }
  
  public void setBackoffFactor(double paramDouble)
  {
    if ((paramDouble > 0.0D) && (paramDouble < 1.0D)) {}
    for (boolean bool = true;; bool = false)
    {
      Args.check(bool, "Backoff factor must be 0.0 < f < 1.0");
      this.backoffFactor = paramDouble;
      return;
    }
  }
  
  public void setCooldownMillis(long paramLong)
  {
    Args.positive(this.coolDown, "Cool down");
    this.coolDown = paramLong;
  }
  
  public void setPerHostConnectionCap(int paramInt)
  {
    Args.positive(paramInt, "Per host connection cap");
    this.cap = paramInt;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.AIMDBackoffManager
 * JD-Core Version:    0.7.0.1
 */