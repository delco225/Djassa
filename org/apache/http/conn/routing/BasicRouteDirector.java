package org.apache.http.conn.routing;

import java.net.InetAddress;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public class BasicRouteDirector
  implements HttpRouteDirector
{
  protected int directStep(RouteInfo paramRouteInfo1, RouteInfo paramRouteInfo2)
  {
    if (paramRouteInfo2.getHopCount() > 1) {}
    while ((!paramRouteInfo1.getTargetHost().equals(paramRouteInfo2.getTargetHost())) || (paramRouteInfo1.isSecure() != paramRouteInfo2.isSecure()) || ((paramRouteInfo1.getLocalAddress() != null) && (!paramRouteInfo1.getLocalAddress().equals(paramRouteInfo2.getLocalAddress())))) {
      return -1;
    }
    return 0;
  }
  
  protected int firstStep(RouteInfo paramRouteInfo)
  {
    int i = 1;
    if (paramRouteInfo.getHopCount() > i) {
      i = 2;
    }
    return i;
  }
  
  public int nextStep(RouteInfo paramRouteInfo1, RouteInfo paramRouteInfo2)
  {
    Args.notNull(paramRouteInfo1, "Planned route");
    if ((paramRouteInfo2 == null) || (paramRouteInfo2.getHopCount() < 1)) {
      return firstStep(paramRouteInfo1);
    }
    if (paramRouteInfo1.getHopCount() > 1) {
      return proxiedStep(paramRouteInfo1, paramRouteInfo2);
    }
    return directStep(paramRouteInfo1, paramRouteInfo2);
  }
  
  protected int proxiedStep(RouteInfo paramRouteInfo1, RouteInfo paramRouteInfo2)
  {
    if (paramRouteInfo2.getHopCount() <= 1) {}
    label91:
    do
    {
      do
      {
        int i;
        int j;
        do
        {
          do
          {
            return -1;
          } while (!paramRouteInfo1.getTargetHost().equals(paramRouteInfo2.getTargetHost()));
          i = paramRouteInfo1.getHopCount();
          j = paramRouteInfo2.getHopCount();
        } while (i < j);
        for (int k = 0;; k++)
        {
          if (k >= j - 1) {
            break label91;
          }
          if (!paramRouteInfo1.getHopTarget(k).equals(paramRouteInfo2.getHopTarget(k))) {
            break;
          }
        }
        if (i > j) {
          return 4;
        }
      } while (((paramRouteInfo2.isTunnelled()) && (!paramRouteInfo1.isTunnelled())) || ((paramRouteInfo2.isLayered()) && (!paramRouteInfo1.isLayered())));
      if ((paramRouteInfo1.isTunnelled()) && (!paramRouteInfo2.isTunnelled())) {
        return 3;
      }
      if ((paramRouteInfo1.isLayered()) && (!paramRouteInfo2.isLayered())) {
        return 5;
      }
    } while (paramRouteInfo1.isSecure() != paramRouteInfo2.isSecure());
    return 0;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.routing.BasicRouteDirector
 * JD-Core Version:    0.7.0.1
 */