package org.apache.http.impl.client;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.routing.HttpRoute;

@Deprecated
@NotThreadSafe
public class RoutedRequest
{
  protected final RequestWrapper request;
  protected final HttpRoute route;
  
  public RoutedRequest(RequestWrapper paramRequestWrapper, HttpRoute paramHttpRoute)
  {
    this.request = paramRequestWrapper;
    this.route = paramHttpRoute;
  }
  
  public final RequestWrapper getRequest()
  {
    return this.request;
  }
  
  public final HttpRoute getRoute()
  {
    return this.route;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.RoutedRequest
 * JD-Core Version:    0.7.0.1
 */