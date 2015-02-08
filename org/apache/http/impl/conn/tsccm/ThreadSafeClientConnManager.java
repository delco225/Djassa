package org.apache.http.impl.conn.tsccm;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.conn.DefaultClientConnectionOperator;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
@ThreadSafe
public class ThreadSafeClientConnManager
  implements ClientConnectionManager
{
  protected final ClientConnectionOperator connOperator;
  protected final ConnPerRouteBean connPerRoute;
  protected final AbstractConnPool connectionPool;
  private final Log log;
  protected final ConnPoolByRoute pool;
  protected final SchemeRegistry schemeRegistry;
  
  public ThreadSafeClientConnManager()
  {
    this(SchemeRegistryFactory.createDefault());
  }
  
  public ThreadSafeClientConnManager(SchemeRegistry paramSchemeRegistry)
  {
    this(paramSchemeRegistry, -1L, TimeUnit.MILLISECONDS);
  }
  
  public ThreadSafeClientConnManager(SchemeRegistry paramSchemeRegistry, long paramLong, TimeUnit paramTimeUnit)
  {
    this(paramSchemeRegistry, paramLong, paramTimeUnit, new ConnPerRouteBean());
  }
  
  public ThreadSafeClientConnManager(SchemeRegistry paramSchemeRegistry, long paramLong, TimeUnit paramTimeUnit, ConnPerRouteBean paramConnPerRouteBean)
  {
    Args.notNull(paramSchemeRegistry, "Scheme registry");
    this.log = LogFactory.getLog(getClass());
    this.schemeRegistry = paramSchemeRegistry;
    this.connPerRoute = paramConnPerRouteBean;
    this.connOperator = createConnectionOperator(paramSchemeRegistry);
    this.pool = createConnectionPool(paramLong, paramTimeUnit);
    this.connectionPool = this.pool;
  }
  
  @Deprecated
  public ThreadSafeClientConnManager(HttpParams paramHttpParams, SchemeRegistry paramSchemeRegistry)
  {
    Args.notNull(paramSchemeRegistry, "Scheme registry");
    this.log = LogFactory.getLog(getClass());
    this.schemeRegistry = paramSchemeRegistry;
    this.connPerRoute = new ConnPerRouteBean();
    this.connOperator = createConnectionOperator(paramSchemeRegistry);
    this.pool = ((ConnPoolByRoute)createConnectionPool(paramHttpParams));
    this.connectionPool = this.pool;
  }
  
  public void closeExpiredConnections()
  {
    this.log.debug("Closing expired connections");
    this.pool.closeExpiredConnections();
  }
  
  public void closeIdleConnections(long paramLong, TimeUnit paramTimeUnit)
  {
    if (this.log.isDebugEnabled()) {
      this.log.debug("Closing connections idle longer than " + paramLong + " " + paramTimeUnit);
    }
    this.pool.closeIdleConnections(paramLong, paramTimeUnit);
  }
  
  protected ClientConnectionOperator createConnectionOperator(SchemeRegistry paramSchemeRegistry)
  {
    return new DefaultClientConnectionOperator(paramSchemeRegistry);
  }
  
  @Deprecated
  protected AbstractConnPool createConnectionPool(HttpParams paramHttpParams)
  {
    return new ConnPoolByRoute(this.connOperator, paramHttpParams);
  }
  
  protected ConnPoolByRoute createConnectionPool(long paramLong, TimeUnit paramTimeUnit)
  {
    return new ConnPoolByRoute(this.connOperator, this.connPerRoute, 20, paramLong, paramTimeUnit);
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      shutdown();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public int getConnectionsInPool()
  {
    return this.pool.getConnectionsInPool();
  }
  
  public int getConnectionsInPool(HttpRoute paramHttpRoute)
  {
    return this.pool.getConnectionsInPool(paramHttpRoute);
  }
  
  public int getDefaultMaxPerRoute()
  {
    return this.connPerRoute.getDefaultMaxPerRoute();
  }
  
  public int getMaxForRoute(HttpRoute paramHttpRoute)
  {
    return this.connPerRoute.getMaxForRoute(paramHttpRoute);
  }
  
  public int getMaxTotal()
  {
    return this.pool.getMaxTotalConnections();
  }
  
  public SchemeRegistry getSchemeRegistry()
  {
    return this.schemeRegistry;
  }
  
  public void releaseConnection(ManagedClientConnection paramManagedClientConnection, long paramLong, TimeUnit paramTimeUnit)
  {
    Args.check(paramManagedClientConnection instanceof BasicPooledConnAdapter, "Connection class mismatch, connection not obtained from this manager");
    BasicPooledConnAdapter localBasicPooledConnAdapter = (BasicPooledConnAdapter)paramManagedClientConnection;
    boolean bool4;
    if (localBasicPooledConnAdapter.getPoolEntry() != null)
    {
      if (localBasicPooledConnAdapter.getManager() != this) {
        break label64;
      }
      bool4 = true;
      Asserts.check(bool4, "Connection not obtained from this manager");
    }
    BasicPoolEntry localBasicPoolEntry;
    for (;;)
    {
      label64:
      boolean bool3;
      try
      {
        localBasicPoolEntry = (BasicPoolEntry)localBasicPooledConnAdapter.getPoolEntry();
        if (localBasicPoolEntry == null)
        {
          return;
          bool4 = false;
          break;
        }
      }
      finally {}
      try
      {
        if ((localBasicPooledConnAdapter.isOpen()) && (!localBasicPooledConnAdapter.isMarkedReusable())) {
          localBasicPooledConnAdapter.shutdown();
        }
        bool3 = localBasicPooledConnAdapter.isMarkedReusable();
        if (this.log.isDebugEnabled())
        {
          if (!bool3) {
            break label157;
          }
          this.log.debug("Released connection is reusable.");
        }
        localBasicPooledConnAdapter.detach();
        this.pool.freeEntry(localBasicPoolEntry, bool3, paramLong, paramTimeUnit);
      }
      catch (IOException localIOException)
      {
        label157:
        if (!this.log.isDebugEnabled()) {
          break label198;
        }
        this.log.debug("Exception shutting down released connection.", localIOException);
        bool2 = localBasicPooledConnAdapter.isMarkedReusable();
        if (!this.log.isDebugEnabled()) {
          break label233;
        }
        if (!bool2) {
          break label255;
        }
        this.log.debug("Released connection is reusable.");
        for (;;)
        {
          localBasicPooledConnAdapter.detach();
          this.pool.freeEntry(localBasicPoolEntry, bool2, paramLong, paramTimeUnit);
          break;
          this.log.debug("Released connection is not reusable.");
        }
      }
      finally
      {
        bool1 = localBasicPooledConnAdapter.isMarkedReusable();
        if (!this.log.isDebugEnabled()) {
          break label306;
        }
        if (!bool1) {
          break label328;
        }
        this.log.debug("Released connection is reusable.");
      }
      return;
      this.log.debug("Released connection is not reusable.");
    }
    for (;;)
    {
      label198:
      boolean bool2;
      label233:
      label255:
      boolean bool1;
      label306:
      localBasicPooledConnAdapter.detach();
      this.pool.freeEntry(localBasicPoolEntry, bool1, paramLong, paramTimeUnit);
      throw localObject2;
      label328:
      this.log.debug("Released connection is not reusable.");
    }
  }
  
  public ClientConnectionRequest requestConnection(final HttpRoute paramHttpRoute, Object paramObject)
  {
    new ClientConnectionRequest()
    {
      public void abortRequest()
      {
        this.val$poolRequest.abortRequest();
      }
      
      public ManagedClientConnection getConnection(long paramAnonymousLong, TimeUnit paramAnonymousTimeUnit)
        throws InterruptedException, ConnectionPoolTimeoutException
      {
        Args.notNull(paramHttpRoute, "Route");
        if (ThreadSafeClientConnManager.this.log.isDebugEnabled()) {
          ThreadSafeClientConnManager.this.log.debug("Get connection: " + paramHttpRoute + ", timeout = " + paramAnonymousLong);
        }
        BasicPoolEntry localBasicPoolEntry = this.val$poolRequest.getPoolEntry(paramAnonymousLong, paramAnonymousTimeUnit);
        return new BasicPooledConnAdapter(ThreadSafeClientConnManager.this, localBasicPoolEntry);
      }
    };
  }
  
  public void setDefaultMaxPerRoute(int paramInt)
  {
    this.connPerRoute.setDefaultMaxPerRoute(paramInt);
  }
  
  public void setMaxForRoute(HttpRoute paramHttpRoute, int paramInt)
  {
    this.connPerRoute.setMaxForRoute(paramHttpRoute, paramInt);
  }
  
  public void setMaxTotal(int paramInt)
  {
    this.pool.setMaxTotalConnections(paramInt);
  }
  
  public void shutdown()
  {
    this.log.debug("Shutting down");
    this.pool.shutdown();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager
 * JD-Core Version:    0.7.0.1
 */