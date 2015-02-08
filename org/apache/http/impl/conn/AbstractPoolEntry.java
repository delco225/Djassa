package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import org.apache.http.HttpHost;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
public abstract class AbstractPoolEntry
{
  protected final ClientConnectionOperator connOperator;
  protected final OperatedClientConnection connection;
  protected volatile HttpRoute route;
  protected volatile Object state;
  protected volatile RouteTracker tracker;
  
  protected AbstractPoolEntry(ClientConnectionOperator paramClientConnectionOperator, HttpRoute paramHttpRoute)
  {
    Args.notNull(paramClientConnectionOperator, "Connection operator");
    this.connOperator = paramClientConnectionOperator;
    this.connection = paramClientConnectionOperator.createConnection();
    this.route = paramHttpRoute;
    this.tracker = null;
  }
  
  public Object getState()
  {
    return this.state;
  }
  
  public void layerProtocol(HttpContext paramHttpContext, HttpParams paramHttpParams)
    throws IOException
  {
    Args.notNull(paramHttpParams, "HTTP parameters");
    Asserts.notNull(this.tracker, "Route tracker");
    Asserts.check(this.tracker.isConnected(), "Connection not open");
    Asserts.check(this.tracker.isTunnelled(), "Protocol layering without a tunnel not supported");
    if (!this.tracker.isLayered()) {}
    for (boolean bool = true;; bool = false)
    {
      Asserts.check(bool, "Multiple protocol layering not supported");
      HttpHost localHttpHost = this.tracker.getTargetHost();
      this.connOperator.updateSecureConnection(this.connection, localHttpHost, paramHttpContext, paramHttpParams);
      this.tracker.layerProtocol(this.connection.isSecure());
      return;
    }
  }
  
  public void open(HttpRoute paramHttpRoute, HttpContext paramHttpContext, HttpParams paramHttpParams)
    throws IOException
  {
    Args.notNull(paramHttpRoute, "Route");
    Args.notNull(paramHttpParams, "HTTP parameters");
    boolean bool;
    HttpHost localHttpHost1;
    ClientConnectionOperator localClientConnectionOperator;
    OperatedClientConnection localOperatedClientConnection;
    if (this.tracker != null)
    {
      if (!this.tracker.isConnected())
      {
        bool = true;
        Asserts.check(bool, "Connection already open");
      }
    }
    else
    {
      this.tracker = new RouteTracker(paramHttpRoute);
      localHttpHost1 = paramHttpRoute.getProxyHost();
      localClientConnectionOperator = this.connOperator;
      localOperatedClientConnection = this.connection;
      if (localHttpHost1 == null) {
        break label124;
      }
    }
    RouteTracker localRouteTracker;
    label124:
    for (HttpHost localHttpHost2 = localHttpHost1;; localHttpHost2 = paramHttpRoute.getTargetHost())
    {
      localClientConnectionOperator.openConnection(localOperatedClientConnection, localHttpHost2, paramHttpRoute.getLocalAddress(), paramHttpContext, paramHttpParams);
      localRouteTracker = this.tracker;
      if (localRouteTracker != null) {
        break label133;
      }
      throw new InterruptedIOException("Request aborted");
      bool = false;
      break;
    }
    label133:
    if (localHttpHost1 == null)
    {
      localRouteTracker.connectTarget(this.connection.isSecure());
      return;
    }
    localRouteTracker.connectProxy(localHttpHost1, this.connection.isSecure());
  }
  
  public void setState(Object paramObject)
  {
    this.state = paramObject;
  }
  
  protected void shutdownEntry()
  {
    this.tracker = null;
    this.state = null;
  }
  
  public void tunnelProxy(HttpHost paramHttpHost, boolean paramBoolean, HttpParams paramHttpParams)
    throws IOException
  {
    Args.notNull(paramHttpHost, "Next proxy");
    Args.notNull(paramHttpParams, "Parameters");
    Asserts.notNull(this.tracker, "Route tracker");
    Asserts.check(this.tracker.isConnected(), "Connection not open");
    this.connection.update(null, paramHttpHost, paramBoolean, paramHttpParams);
    this.tracker.tunnelProxy(paramHttpHost, paramBoolean);
  }
  
  public void tunnelTarget(boolean paramBoolean, HttpParams paramHttpParams)
    throws IOException
  {
    Args.notNull(paramHttpParams, "HTTP parameters");
    Asserts.notNull(this.tracker, "Route tracker");
    Asserts.check(this.tracker.isConnected(), "Connection not open");
    if (!this.tracker.isTunnelled()) {}
    for (boolean bool = true;; bool = false)
    {
      Asserts.check(bool, "Connection is already tunnelled");
      this.connection.update(null, this.tracker.getTargetHost(), paramBoolean, paramHttpParams);
      this.tracker.tunnelTarget(paramBoolean);
      return;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.AbstractPoolEntry
 * JD-Core Version:    0.7.0.1
 */