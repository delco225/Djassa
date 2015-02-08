package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
@NotThreadSafe
class ManagedClientConnectionImpl
  implements ManagedClientConnection
{
  private volatile long duration;
  private final ClientConnectionManager manager;
  private final ClientConnectionOperator operator;
  private volatile HttpPoolEntry poolEntry;
  private volatile boolean reusable;
  
  ManagedClientConnectionImpl(ClientConnectionManager paramClientConnectionManager, ClientConnectionOperator paramClientConnectionOperator, HttpPoolEntry paramHttpPoolEntry)
  {
    Args.notNull(paramClientConnectionManager, "Connection manager");
    Args.notNull(paramClientConnectionOperator, "Connection operator");
    Args.notNull(paramHttpPoolEntry, "HTTP pool entry");
    this.manager = paramClientConnectionManager;
    this.operator = paramClientConnectionOperator;
    this.poolEntry = paramHttpPoolEntry;
    this.reusable = false;
    this.duration = 9223372036854775807L;
  }
  
  private OperatedClientConnection ensureConnection()
  {
    HttpPoolEntry localHttpPoolEntry = this.poolEntry;
    if (localHttpPoolEntry == null) {
      throw new ConnectionShutdownException();
    }
    return (OperatedClientConnection)localHttpPoolEntry.getConnection();
  }
  
  private HttpPoolEntry ensurePoolEntry()
  {
    HttpPoolEntry localHttpPoolEntry = this.poolEntry;
    if (localHttpPoolEntry == null) {
      throw new ConnectionShutdownException();
    }
    return localHttpPoolEntry;
  }
  
  private OperatedClientConnection getConnection()
  {
    HttpPoolEntry localHttpPoolEntry = this.poolEntry;
    if (localHttpPoolEntry == null) {
      return null;
    }
    return (OperatedClientConnection)localHttpPoolEntry.getConnection();
  }
  
  /* Error */
  public void abortConnection()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 41	org/apache/http/impl/conn/ManagedClientConnectionImpl:poolEntry	Lorg/apache/http/impl/conn/HttpPoolEntry;
    //   6: ifnonnull +6 -> 12
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: aload_0
    //   13: iconst_0
    //   14: putfield 43	org/apache/http/impl/conn/ManagedClientConnectionImpl:reusable	Z
    //   17: aload_0
    //   18: getfield 41	org/apache/http/impl/conn/ManagedClientConnectionImpl:poolEntry	Lorg/apache/http/impl/conn/HttpPoolEntry;
    //   21: invokevirtual 58	org/apache/http/impl/conn/HttpPoolEntry:getConnection	()Ljava/lang/Object;
    //   24: checkcast 60	org/apache/http/conn/OperatedClientConnection
    //   27: astore_2
    //   28: aload_2
    //   29: invokeinterface 68 1 0
    //   34: aload_0
    //   35: getfield 37	org/apache/http/impl/conn/ManagedClientConnectionImpl:manager	Lorg/apache/http/conn/ClientConnectionManager;
    //   38: aload_0
    //   39: aload_0
    //   40: getfield 47	org/apache/http/impl/conn/ManagedClientConnectionImpl:duration	J
    //   43: getstatic 74	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
    //   46: invokeinterface 80 5 0
    //   51: aload_0
    //   52: aconst_null
    //   53: putfield 41	org/apache/http/impl/conn/ManagedClientConnectionImpl:poolEntry	Lorg/apache/http/impl/conn/HttpPoolEntry;
    //   56: aload_0
    //   57: monitorexit
    //   58: return
    //   59: astore_1
    //   60: aload_0
    //   61: monitorexit
    //   62: aload_1
    //   63: athrow
    //   64: astore_3
    //   65: goto -31 -> 34
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	68	0	this	ManagedClientConnectionImpl
    //   59	4	1	localObject	Object
    //   27	2	2	localOperatedClientConnection	OperatedClientConnection
    //   64	1	3	localIOException	IOException
    // Exception table:
    //   from	to	target	type
    //   2	11	59	finally
    //   12	28	59	finally
    //   28	34	59	finally
    //   34	58	59	finally
    //   60	62	59	finally
    //   28	34	64	java/io/IOException
  }
  
  public void bind(Socket paramSocket)
    throws IOException
  {
    throw new UnsupportedOperationException();
  }
  
  public void close()
    throws IOException
  {
    HttpPoolEntry localHttpPoolEntry = this.poolEntry;
    if (localHttpPoolEntry != null)
    {
      OperatedClientConnection localOperatedClientConnection = (OperatedClientConnection)localHttpPoolEntry.getConnection();
      localHttpPoolEntry.getTracker().reset();
      localOperatedClientConnection.close();
    }
  }
  
  HttpPoolEntry detach()
  {
    HttpPoolEntry localHttpPoolEntry = this.poolEntry;
    this.poolEntry = null;
    return localHttpPoolEntry;
  }
  
  public void flush()
    throws IOException
  {
    ensureConnection().flush();
  }
  
  public Object getAttribute(String paramString)
  {
    OperatedClientConnection localOperatedClientConnection = ensureConnection();
    if ((localOperatedClientConnection instanceof HttpContext)) {
      return ((HttpContext)localOperatedClientConnection).getAttribute(paramString);
    }
    return null;
  }
  
  public String getId()
  {
    return null;
  }
  
  public InetAddress getLocalAddress()
  {
    return ensureConnection().getLocalAddress();
  }
  
  public int getLocalPort()
  {
    return ensureConnection().getLocalPort();
  }
  
  public ClientConnectionManager getManager()
  {
    return this.manager;
  }
  
  public HttpConnectionMetrics getMetrics()
  {
    return ensureConnection().getMetrics();
  }
  
  HttpPoolEntry getPoolEntry()
  {
    return this.poolEntry;
  }
  
  public InetAddress getRemoteAddress()
  {
    return ensureConnection().getRemoteAddress();
  }
  
  public int getRemotePort()
  {
    return ensureConnection().getRemotePort();
  }
  
  public HttpRoute getRoute()
  {
    return ensurePoolEntry().getEffectiveRoute();
  }
  
  public SSLSession getSSLSession()
  {
    Socket localSocket = ensureConnection().getSocket();
    boolean bool = localSocket instanceof SSLSocket;
    SSLSession localSSLSession = null;
    if (bool) {
      localSSLSession = ((SSLSocket)localSocket).getSession();
    }
    return localSSLSession;
  }
  
  public Socket getSocket()
  {
    return ensureConnection().getSocket();
  }
  
  public int getSocketTimeout()
  {
    return ensureConnection().getSocketTimeout();
  }
  
  public Object getState()
  {
    return ensurePoolEntry().getState();
  }
  
  public boolean isMarkedReusable()
  {
    return this.reusable;
  }
  
  public boolean isOpen()
  {
    OperatedClientConnection localOperatedClientConnection = getConnection();
    if (localOperatedClientConnection != null) {
      return localOperatedClientConnection.isOpen();
    }
    return false;
  }
  
  public boolean isResponseAvailable(int paramInt)
    throws IOException
  {
    return ensureConnection().isResponseAvailable(paramInt);
  }
  
  public boolean isSecure()
  {
    return ensureConnection().isSecure();
  }
  
  public boolean isStale()
  {
    OperatedClientConnection localOperatedClientConnection = getConnection();
    if (localOperatedClientConnection != null) {
      return localOperatedClientConnection.isStale();
    }
    return true;
  }
  
  public void layerProtocol(HttpContext paramHttpContext, HttpParams paramHttpParams)
    throws IOException
  {
    Args.notNull(paramHttpParams, "HTTP parameters");
    try
    {
      if (this.poolEntry == null) {
        throw new ConnectionShutdownException();
      }
    }
    finally {}
    RouteTracker localRouteTracker = this.poolEntry.getTracker();
    Asserts.notNull(localRouteTracker, "Route tracker");
    Asserts.check(localRouteTracker.isConnected(), "Connection not open");
    Asserts.check(localRouteTracker.isTunnelled(), "Protocol layering without a tunnel not supported");
    if (!localRouteTracker.isLayered()) {}
    OperatedClientConnection localOperatedClientConnection;
    for (boolean bool = true;; bool = false)
    {
      Asserts.check(bool, "Multiple protocol layering not supported");
      HttpHost localHttpHost = localRouteTracker.getTargetHost();
      localOperatedClientConnection = (OperatedClientConnection)this.poolEntry.getConnection();
      this.operator.updateSecureConnection(localOperatedClientConnection, localHttpHost, paramHttpContext, paramHttpParams);
      try
      {
        if (this.poolEntry != null) {
          break;
        }
        throw new InterruptedIOException();
      }
      finally {}
    }
    this.poolEntry.getTracker().layerProtocol(localOperatedClientConnection.isSecure());
  }
  
  public void markReusable()
  {
    this.reusable = true;
  }
  
  public void open(HttpRoute paramHttpRoute, HttpContext paramHttpContext, HttpParams paramHttpParams)
    throws IOException
  {
    Args.notNull(paramHttpRoute, "Route");
    Args.notNull(paramHttpParams, "HTTP parameters");
    try
    {
      if (this.poolEntry == null) {
        throw new ConnectionShutdownException();
      }
    }
    finally {}
    RouteTracker localRouteTracker1 = this.poolEntry.getTracker();
    Asserts.notNull(localRouteTracker1, "Route tracker");
    boolean bool;
    OperatedClientConnection localOperatedClientConnection;
    HttpHost localHttpHost1;
    ClientConnectionOperator localClientConnectionOperator;
    if (!localRouteTracker1.isConnected())
    {
      bool = true;
      Asserts.check(bool, "Connection already open");
      localOperatedClientConnection = (OperatedClientConnection)this.poolEntry.getConnection();
      localHttpHost1 = paramHttpRoute.getProxyHost();
      localClientConnectionOperator = this.operator;
      if (localHttpHost1 == null) {
        break label154;
      }
    }
    label154:
    for (HttpHost localHttpHost2 = localHttpHost1;; localHttpHost2 = paramHttpRoute.getTargetHost())
    {
      localClientConnectionOperator.openConnection(localOperatedClientConnection, localHttpHost2, paramHttpRoute.getLocalAddress(), paramHttpContext, paramHttpParams);
      try
      {
        if (this.poolEntry != null) {
          break label163;
        }
        throw new InterruptedIOException();
      }
      finally {}
      bool = false;
      break;
    }
    label163:
    RouteTracker localRouteTracker2 = this.poolEntry.getTracker();
    if (localHttpHost1 == null) {
      localRouteTracker2.connectTarget(localOperatedClientConnection.isSecure());
    }
    for (;;)
    {
      return;
      localRouteTracker2.connectProxy(localHttpHost1, localOperatedClientConnection.isSecure());
    }
  }
  
  public void receiveResponseEntity(HttpResponse paramHttpResponse)
    throws HttpException, IOException
  {
    ensureConnection().receiveResponseEntity(paramHttpResponse);
  }
  
  public HttpResponse receiveResponseHeader()
    throws HttpException, IOException
  {
    return ensureConnection().receiveResponseHeader();
  }
  
  public void releaseConnection()
  {
    try
    {
      if (this.poolEntry == null) {
        return;
      }
      this.manager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
      this.poolEntry = null;
      return;
    }
    finally {}
  }
  
  public Object removeAttribute(String paramString)
  {
    OperatedClientConnection localOperatedClientConnection = ensureConnection();
    if ((localOperatedClientConnection instanceof HttpContext)) {
      return ((HttpContext)localOperatedClientConnection).removeAttribute(paramString);
    }
    return null;
  }
  
  public void sendRequestEntity(HttpEntityEnclosingRequest paramHttpEntityEnclosingRequest)
    throws HttpException, IOException
  {
    ensureConnection().sendRequestEntity(paramHttpEntityEnclosingRequest);
  }
  
  public void sendRequestHeader(HttpRequest paramHttpRequest)
    throws HttpException, IOException
  {
    ensureConnection().sendRequestHeader(paramHttpRequest);
  }
  
  public void setAttribute(String paramString, Object paramObject)
  {
    OperatedClientConnection localOperatedClientConnection = ensureConnection();
    if ((localOperatedClientConnection instanceof HttpContext)) {
      ((HttpContext)localOperatedClientConnection).setAttribute(paramString, paramObject);
    }
  }
  
  public void setIdleDuration(long paramLong, TimeUnit paramTimeUnit)
  {
    if (paramLong > 0L)
    {
      this.duration = paramTimeUnit.toMillis(paramLong);
      return;
    }
    this.duration = -1L;
  }
  
  public void setSocketTimeout(int paramInt)
  {
    ensureConnection().setSocketTimeout(paramInt);
  }
  
  public void setState(Object paramObject)
  {
    ensurePoolEntry().setState(paramObject);
  }
  
  public void shutdown()
    throws IOException
  {
    HttpPoolEntry localHttpPoolEntry = this.poolEntry;
    if (localHttpPoolEntry != null)
    {
      OperatedClientConnection localOperatedClientConnection = (OperatedClientConnection)localHttpPoolEntry.getConnection();
      localHttpPoolEntry.getTracker().reset();
      localOperatedClientConnection.shutdown();
    }
  }
  
  public void tunnelProxy(HttpHost paramHttpHost, boolean paramBoolean, HttpParams paramHttpParams)
    throws IOException
  {
    Args.notNull(paramHttpHost, "Next proxy");
    Args.notNull(paramHttpParams, "HTTP parameters");
    try
    {
      if (this.poolEntry == null) {
        throw new ConnectionShutdownException();
      }
    }
    finally {}
    RouteTracker localRouteTracker = this.poolEntry.getTracker();
    Asserts.notNull(localRouteTracker, "Route tracker");
    Asserts.check(localRouteTracker.isConnected(), "Connection not open");
    OperatedClientConnection localOperatedClientConnection = (OperatedClientConnection)this.poolEntry.getConnection();
    localOperatedClientConnection.update(null, paramHttpHost, paramBoolean, paramHttpParams);
    try
    {
      if (this.poolEntry == null) {
        throw new InterruptedIOException();
      }
    }
    finally {}
    this.poolEntry.getTracker().tunnelProxy(paramHttpHost, paramBoolean);
  }
  
  public void tunnelTarget(boolean paramBoolean, HttpParams paramHttpParams)
    throws IOException
  {
    Args.notNull(paramHttpParams, "HTTP parameters");
    try
    {
      if (this.poolEntry == null) {
        throw new ConnectionShutdownException();
      }
    }
    finally {}
    RouteTracker localRouteTracker = this.poolEntry.getTracker();
    Asserts.notNull(localRouteTracker, "Route tracker");
    Asserts.check(localRouteTracker.isConnected(), "Connection not open");
    if (!localRouteTracker.isTunnelled()) {}
    for (boolean bool = true;; bool = false)
    {
      Asserts.check(bool, "Connection is already tunnelled");
      HttpHost localHttpHost = localRouteTracker.getTargetHost();
      OperatedClientConnection localOperatedClientConnection = (OperatedClientConnection)this.poolEntry.getConnection();
      localOperatedClientConnection.update(null, localHttpHost, paramBoolean, paramHttpParams);
      try
      {
        if (this.poolEntry != null) {
          break;
        }
        throw new InterruptedIOException();
      }
      finally {}
    }
    this.poolEntry.getTracker().tunnelTarget(paramBoolean);
  }
  
  public void unmarkReusable()
  {
    this.reusable = false;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.ManagedClientConnectionImpl
 * JD-Core Version:    0.7.0.1
 */