package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.pool.PoolStats;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
@ThreadSafe
public class PoolingClientConnectionManager
  implements ClientConnectionManager, ConnPoolControl<HttpRoute>
{
  private final DnsResolver dnsResolver;
  private final Log log = LogFactory.getLog(getClass());
  private final ClientConnectionOperator operator;
  private final HttpConnPool pool;
  private final SchemeRegistry schemeRegistry;
  
  public PoolingClientConnectionManager()
  {
    this(SchemeRegistryFactory.createDefault());
  }
  
  public PoolingClientConnectionManager(SchemeRegistry paramSchemeRegistry)
  {
    this(paramSchemeRegistry, -1L, TimeUnit.MILLISECONDS);
  }
  
  public PoolingClientConnectionManager(SchemeRegistry paramSchemeRegistry, long paramLong, TimeUnit paramTimeUnit)
  {
    this(paramSchemeRegistry, paramLong, paramTimeUnit, new SystemDefaultDnsResolver());
  }
  
  public PoolingClientConnectionManager(SchemeRegistry paramSchemeRegistry, long paramLong, TimeUnit paramTimeUnit, DnsResolver paramDnsResolver)
  {
    Args.notNull(paramSchemeRegistry, "Scheme registry");
    Args.notNull(paramDnsResolver, "DNS resolver");
    this.schemeRegistry = paramSchemeRegistry;
    this.dnsResolver = paramDnsResolver;
    this.operator = createConnectionOperator(paramSchemeRegistry);
    this.pool = new HttpConnPool(this.log, this.operator, 2, 20, paramLong, paramTimeUnit);
  }
  
  public PoolingClientConnectionManager(SchemeRegistry paramSchemeRegistry, DnsResolver paramDnsResolver)
  {
    this(paramSchemeRegistry, -1L, TimeUnit.MILLISECONDS, paramDnsResolver);
  }
  
  private String format(HttpRoute paramHttpRoute, Object paramObject)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[route: ").append(paramHttpRoute).append("]");
    if (paramObject != null) {
      localStringBuilder.append("[state: ").append(paramObject).append("]");
    }
    return localStringBuilder.toString();
  }
  
  private String format(HttpPoolEntry paramHttpPoolEntry)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[id: ").append(paramHttpPoolEntry.getId()).append("]");
    localStringBuilder.append("[route: ").append(paramHttpPoolEntry.getRoute()).append("]");
    Object localObject = paramHttpPoolEntry.getState();
    if (localObject != null) {
      localStringBuilder.append("[state: ").append(localObject).append("]");
    }
    return localStringBuilder.toString();
  }
  
  private String formatStats(HttpRoute paramHttpRoute)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    PoolStats localPoolStats1 = this.pool.getTotalStats();
    PoolStats localPoolStats2 = this.pool.getStats(paramHttpRoute);
    localStringBuilder.append("[total kept alive: ").append(localPoolStats1.getAvailable()).append("; ");
    localStringBuilder.append("route allocated: ").append(localPoolStats2.getLeased() + localPoolStats2.getAvailable());
    localStringBuilder.append(" of ").append(localPoolStats2.getMax()).append("; ");
    localStringBuilder.append("total allocated: ").append(localPoolStats1.getLeased() + localPoolStats1.getAvailable());
    localStringBuilder.append(" of ").append(localPoolStats1.getMax()).append("]");
    return localStringBuilder.toString();
  }
  
  public void closeExpiredConnections()
  {
    this.log.debug("Closing expired connections");
    this.pool.closeExpired();
  }
  
  public void closeIdleConnections(long paramLong, TimeUnit paramTimeUnit)
  {
    if (this.log.isDebugEnabled()) {
      this.log.debug("Closing connections idle longer than " + paramLong + " " + paramTimeUnit);
    }
    this.pool.closeIdle(paramLong, paramTimeUnit);
  }
  
  protected ClientConnectionOperator createConnectionOperator(SchemeRegistry paramSchemeRegistry)
  {
    return new DefaultClientConnectionOperator(paramSchemeRegistry, this.dnsResolver);
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
  
  public int getDefaultMaxPerRoute()
  {
    return this.pool.getDefaultMaxPerRoute();
  }
  
  public int getMaxPerRoute(HttpRoute paramHttpRoute)
  {
    return this.pool.getMaxPerRoute(paramHttpRoute);
  }
  
  public int getMaxTotal()
  {
    return this.pool.getMaxTotal();
  }
  
  public SchemeRegistry getSchemeRegistry()
  {
    return this.schemeRegistry;
  }
  
  public PoolStats getStats(HttpRoute paramHttpRoute)
  {
    return this.pool.getStats(paramHttpRoute);
  }
  
  public PoolStats getTotalStats()
  {
    return this.pool.getTotalStats();
  }
  
  ManagedClientConnection leaseConnection(Future<HttpPoolEntry> paramFuture, long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException, ConnectionPoolTimeoutException
  {
    try
    {
      localHttpPoolEntry = (HttpPoolEntry)paramFuture.get(paramLong, paramTimeUnit);
      if ((localHttpPoolEntry == null) || (paramFuture.isCancelled())) {
        throw new InterruptedException();
      }
    }
    catch (ExecutionException localExecutionException)
    {
      HttpPoolEntry localHttpPoolEntry;
      Object localObject = localExecutionException.getCause();
      if (localObject == null) {
        localObject = localExecutionException;
      }
      this.log.error("Unexpected exception leasing connection from pool", (Throwable)localObject);
      throw new InterruptedException();
      if (localHttpPoolEntry.getConnection() != null) {}
      for (boolean bool = true;; bool = false)
      {
        Asserts.check(bool, "Pool entry with no connection");
        if (this.log.isDebugEnabled()) {
          this.log.debug("Connection leased: " + format(localHttpPoolEntry) + formatStats((HttpRoute)localHttpPoolEntry.getRoute()));
        }
        ManagedClientConnectionImpl localManagedClientConnectionImpl = new ManagedClientConnectionImpl(this, this.operator, localHttpPoolEntry);
        return localManagedClientConnectionImpl;
      }
    }
    catch (TimeoutException localTimeoutException)
    {
      throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
    }
  }
  
  /* Error */
  public void releaseConnection(ManagedClientConnection paramManagedClientConnection, long paramLong, TimeUnit paramTimeUnit)
  {
    // Byte code:
    //   0: aload_1
    //   1: instanceof 271
    //   4: ldc_w 285
    //   7: invokestatic 286	org/apache/http/util/Args:check	(ZLjava/lang/String;)V
    //   10: aload_1
    //   11: checkcast 271	org/apache/http/impl/conn/ManagedClientConnectionImpl
    //   14: astore 5
    //   16: aload 5
    //   18: invokevirtual 290	org/apache/http/impl/conn/ManagedClientConnectionImpl:getManager	()Lorg/apache/http/conn/ClientConnectionManager;
    //   21: aload_0
    //   22: if_acmpne +33 -> 55
    //   25: iconst_1
    //   26: istore 6
    //   28: iload 6
    //   30: ldc_w 292
    //   33: invokestatic 263	org/apache/http/util/Asserts:check	(ZLjava/lang/String;)V
    //   36: aload 5
    //   38: monitorenter
    //   39: aload 5
    //   41: invokevirtual 296	org/apache/http/impl/conn/ManagedClientConnectionImpl:detach	()Lorg/apache/http/impl/conn/HttpPoolEntry;
    //   44: astore 8
    //   46: aload 8
    //   48: ifnonnull +13 -> 61
    //   51: aload 5
    //   53: monitorexit
    //   54: return
    //   55: iconst_0
    //   56: istore 6
    //   58: goto -30 -> 28
    //   61: aload 5
    //   63: invokevirtual 299	org/apache/http/impl/conn/ManagedClientConnectionImpl:isOpen	()Z
    //   66: ifeq +20 -> 86
    //   69: aload 5
    //   71: invokevirtual 302	org/apache/http/impl/conn/ManagedClientConnectionImpl:isMarkedReusable	()Z
    //   74: istore 12
    //   76: iload 12
    //   78: ifne +8 -> 86
    //   81: aload 5
    //   83: invokevirtual 303	org/apache/http/impl/conn/ManagedClientConnectionImpl:shutdown	()V
    //   86: aload 5
    //   88: invokevirtual 302	org/apache/http/impl/conn/ManagedClientConnectionImpl:isMarkedReusable	()Z
    //   91: ifeq +115 -> 206
    //   94: aload 4
    //   96: ifnull +247 -> 343
    //   99: aload 4
    //   101: astore 10
    //   103: aload 8
    //   105: lload_2
    //   106: aload 10
    //   108: invokevirtual 306	org/apache/http/impl/conn/HttpPoolEntry:updateExpiry	(JLjava/util/concurrent/TimeUnit;)V
    //   111: aload_0
    //   112: getfield 63	org/apache/http/impl/conn/PoolingClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   115: invokeinterface 181 1 0
    //   120: ifeq +86 -> 206
    //   123: lload_2
    //   124: lconst_0
    //   125: lcmp
    //   126: ifle +225 -> 351
    //   129: new 95	java/lang/StringBuilder
    //   132: dup
    //   133: invokespecial 96	java/lang/StringBuilder:<init>	()V
    //   136: ldc_w 308
    //   139: invokevirtual 102	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   142: lload_2
    //   143: invokevirtual 186	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   146: ldc 188
    //   148: invokevirtual 102	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   151: aload 4
    //   153: invokevirtual 105	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   156: invokevirtual 113	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   159: astore 11
    //   161: aload_0
    //   162: getfield 63	org/apache/http/impl/conn/PoolingClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   165: new 95	java/lang/StringBuilder
    //   168: dup
    //   169: invokespecial 96	java/lang/StringBuilder:<init>	()V
    //   172: ldc_w 310
    //   175: invokevirtual 102	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   178: aload_0
    //   179: aload 8
    //   181: invokespecial 267	org/apache/http/impl/conn/PoolingClientConnectionManager:format	(Lorg/apache/http/impl/conn/HttpPoolEntry;)Ljava/lang/String;
    //   184: invokevirtual 102	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   187: ldc_w 312
    //   190: invokevirtual 102	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   193: aload 11
    //   195: invokevirtual 102	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   198: invokevirtual 113	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   201: invokeinterface 172 2 0
    //   206: aload_0
    //   207: getfield 90	org/apache/http/impl/conn/PoolingClientConnectionManager:pool	Lorg/apache/http/impl/conn/HttpConnPool;
    //   210: aload 8
    //   212: aload 5
    //   214: invokevirtual 302	org/apache/http/impl/conn/ManagedClientConnectionImpl:isMarkedReusable	()Z
    //   217: invokevirtual 316	org/apache/http/impl/conn/HttpConnPool:release	(Lorg/apache/http/pool/PoolEntry;Z)V
    //   220: aload_0
    //   221: getfield 63	org/apache/http/impl/conn/PoolingClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   224: invokeinterface 181 1 0
    //   229: ifeq +52 -> 281
    //   232: aload_0
    //   233: getfield 63	org/apache/http/impl/conn/PoolingClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   236: new 95	java/lang/StringBuilder
    //   239: dup
    //   240: invokespecial 96	java/lang/StringBuilder:<init>	()V
    //   243: ldc_w 318
    //   246: invokevirtual 102	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   249: aload_0
    //   250: aload 8
    //   252: invokespecial 267	org/apache/http/impl/conn/PoolingClientConnectionManager:format	(Lorg/apache/http/impl/conn/HttpPoolEntry;)Ljava/lang/String;
    //   255: invokevirtual 102	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   258: aload_0
    //   259: aload 8
    //   261: invokevirtual 125	org/apache/http/impl/conn/HttpPoolEntry:getRoute	()Ljava/lang/Object;
    //   264: checkcast 210	org/apache/http/conn/routing/HttpRoute
    //   267: invokespecial 269	org/apache/http/impl/conn/PoolingClientConnectionManager:formatStats	(Lorg/apache/http/conn/routing/HttpRoute;)Ljava/lang/String;
    //   270: invokevirtual 102	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   273: invokevirtual 113	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   276: invokeinterface 172 2 0
    //   281: aload 5
    //   283: monitorexit
    //   284: return
    //   285: astore 7
    //   287: aload 5
    //   289: monitorexit
    //   290: aload 7
    //   292: athrow
    //   293: astore 13
    //   295: aload_0
    //   296: getfield 63	org/apache/http/impl/conn/PoolingClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   299: invokeinterface 181 1 0
    //   304: ifeq -218 -> 86
    //   307: aload_0
    //   308: getfield 63	org/apache/http/impl/conn/PoolingClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   311: ldc_w 320
    //   314: aload 13
    //   316: invokeinterface 322 3 0
    //   321: goto -235 -> 86
    //   324: astore 9
    //   326: aload_0
    //   327: getfield 90	org/apache/http/impl/conn/PoolingClientConnectionManager:pool	Lorg/apache/http/impl/conn/HttpConnPool;
    //   330: aload 8
    //   332: aload 5
    //   334: invokevirtual 302	org/apache/http/impl/conn/ManagedClientConnectionImpl:isMarkedReusable	()Z
    //   337: invokevirtual 316	org/apache/http/impl/conn/HttpConnPool:release	(Lorg/apache/http/pool/PoolEntry;Z)V
    //   340: aload 9
    //   342: athrow
    //   343: getstatic 40	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
    //   346: astore 10
    //   348: goto -245 -> 103
    //   351: ldc_w 324
    //   354: astore 11
    //   356: goto -195 -> 161
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	359	0	this	PoolingClientConnectionManager
    //   0	359	1	paramManagedClientConnection	ManagedClientConnection
    //   0	359	2	paramLong	long
    //   0	359	4	paramTimeUnit	TimeUnit
    //   14	319	5	localManagedClientConnectionImpl	ManagedClientConnectionImpl
    //   26	31	6	bool1	boolean
    //   285	6	7	localObject1	Object
    //   44	287	8	localHttpPoolEntry	HttpPoolEntry
    //   324	17	9	localObject2	Object
    //   101	246	10	localTimeUnit	TimeUnit
    //   159	196	11	str	String
    //   74	3	12	bool2	boolean
    //   293	22	13	localIOException	IOException
    // Exception table:
    //   from	to	target	type
    //   39	46	285	finally
    //   51	54	285	finally
    //   206	281	285	finally
    //   281	284	285	finally
    //   287	290	285	finally
    //   326	343	285	finally
    //   81	86	293	java/io/IOException
    //   61	76	324	finally
    //   81	86	324	finally
    //   86	94	324	finally
    //   103	123	324	finally
    //   129	161	324	finally
    //   161	206	324	finally
    //   295	321	324	finally
    //   343	348	324	finally
  }
  
  public ClientConnectionRequest requestConnection(HttpRoute paramHttpRoute, Object paramObject)
  {
    Args.notNull(paramHttpRoute, "HTTP route");
    if (this.log.isDebugEnabled()) {
      this.log.debug("Connection request: " + format(paramHttpRoute, paramObject) + formatStats(paramHttpRoute));
    }
    new ClientConnectionRequest()
    {
      public void abortRequest()
      {
        this.val$future.cancel(true);
      }
      
      public ManagedClientConnection getConnection(long paramAnonymousLong, TimeUnit paramAnonymousTimeUnit)
        throws InterruptedException, ConnectionPoolTimeoutException
      {
        return PoolingClientConnectionManager.this.leaseConnection(this.val$future, paramAnonymousLong, paramAnonymousTimeUnit);
      }
    };
  }
  
  public void setDefaultMaxPerRoute(int paramInt)
  {
    this.pool.setDefaultMaxPerRoute(paramInt);
  }
  
  public void setMaxPerRoute(HttpRoute paramHttpRoute, int paramInt)
  {
    this.pool.setMaxPerRoute(paramHttpRoute, paramInt);
  }
  
  public void setMaxTotal(int paramInt)
  {
    this.pool.setMaxTotal(paramInt);
  }
  
  public void shutdown()
  {
    this.log.debug("Connection manager is shutting down");
    try
    {
      this.pool.shutdown();
      this.log.debug("Connection manager shut down");
      return;
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        this.log.debug("I/O exception shutting down connection manager", localIOException);
      }
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.PoolingClientConnectionManager
 * JD-Core Version:    0.7.0.1
 */