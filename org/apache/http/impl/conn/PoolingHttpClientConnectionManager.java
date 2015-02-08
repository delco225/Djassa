package org.apache.http.impl.conn;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.pool.ConnFactory;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.pool.PoolStats;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@ThreadSafe
public class PoolingHttpClientConnectionManager
  implements HttpClientConnectionManager, ConnPoolControl<HttpRoute>, Closeable
{
  private final ConfigData configData = new ConfigData();
  private final HttpClientConnectionOperator connectionOperator;
  private final Log log = LogFactory.getLog(getClass());
  private final CPool pool;
  
  public PoolingHttpClientConnectionManager()
  {
    this(getDefaultRegistry());
  }
  
  public PoolingHttpClientConnectionManager(long paramLong, TimeUnit paramTimeUnit)
  {
    this(getDefaultRegistry(), null, null, null, paramLong, paramTimeUnit);
  }
  
  public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> paramRegistry)
  {
    this(paramRegistry, null, null);
  }
  
  public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> paramRegistry, DnsResolver paramDnsResolver)
  {
    this(paramRegistry, null, paramDnsResolver);
  }
  
  public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> paramRegistry, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> paramHttpConnectionFactory)
  {
    this(paramRegistry, paramHttpConnectionFactory, null);
  }
  
  public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> paramRegistry, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> paramHttpConnectionFactory, DnsResolver paramDnsResolver)
  {
    this(paramRegistry, paramHttpConnectionFactory, null, paramDnsResolver, -1L, TimeUnit.MILLISECONDS);
  }
  
  public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> paramRegistry, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> paramHttpConnectionFactory, SchemePortResolver paramSchemePortResolver, DnsResolver paramDnsResolver, long paramLong, TimeUnit paramTimeUnit)
  {
    this.pool = new CPool(new InternalConnectionFactory(this.configData, paramHttpConnectionFactory), 2, 20, paramLong, paramTimeUnit);
    this.connectionOperator = new HttpClientConnectionOperator(paramRegistry, paramSchemePortResolver, paramDnsResolver);
  }
  
  public PoolingHttpClientConnectionManager(HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> paramHttpConnectionFactory)
  {
    this(getDefaultRegistry(), paramHttpConnectionFactory, null);
  }
  
  PoolingHttpClientConnectionManager(CPool paramCPool, Lookup<ConnectionSocketFactory> paramLookup, SchemePortResolver paramSchemePortResolver, DnsResolver paramDnsResolver)
  {
    this.pool = paramCPool;
    this.connectionOperator = new HttpClientConnectionOperator(paramLookup, paramSchemePortResolver, paramDnsResolver);
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
  
  private String format(CPoolEntry paramCPoolEntry)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[id: ").append(paramCPoolEntry.getId()).append("]");
    localStringBuilder.append("[route: ").append(paramCPoolEntry.getRoute()).append("]");
    Object localObject = paramCPoolEntry.getState();
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
  
  private static Registry<ConnectionSocketFactory> getDefaultRegistry()
  {
    return RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
  }
  
  public void close()
  {
    shutdown();
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
  
  public void connect(HttpClientConnection paramHttpClientConnection, HttpRoute paramHttpRoute, int paramInt, HttpContext paramHttpContext)
    throws IOException
  {
    Args.notNull(paramHttpClientConnection, "Managed Connection");
    Args.notNull(paramHttpRoute, "HTTP route");
    for (;;)
    {
      try
      {
        ManagedHttpClientConnection localManagedHttpClientConnection = (ManagedHttpClientConnection)CPoolProxy.getPoolEntry(paramHttpClientConnection).getConnection();
        if (paramHttpRoute.getProxyHost() != null)
        {
          localHttpHost = paramHttpRoute.getProxyHost();
          InetSocketAddress localInetSocketAddress = paramHttpRoute.getLocalSocketAddress();
          SocketConfig localSocketConfig = this.configData.getSocketConfig(localHttpHost);
          if (localSocketConfig == null) {
            localSocketConfig = this.configData.getDefaultSocketConfig();
          }
          if (localSocketConfig == null) {
            localSocketConfig = SocketConfig.DEFAULT;
          }
          this.connectionOperator.connect(localManagedHttpClientConnection, localHttpHost, localInetSocketAddress, paramInt, localSocketConfig, paramHttpContext);
          return;
        }
      }
      finally {}
      HttpHost localHttpHost = paramHttpRoute.getTargetHost();
    }
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
  
  public ConnectionConfig getConnectionConfig(HttpHost paramHttpHost)
  {
    return this.configData.getConnectionConfig(paramHttpHost);
  }
  
  public ConnectionConfig getDefaultConnectionConfig()
  {
    return this.configData.getDefaultConnectionConfig();
  }
  
  public int getDefaultMaxPerRoute()
  {
    return this.pool.getDefaultMaxPerRoute();
  }
  
  public SocketConfig getDefaultSocketConfig()
  {
    return this.configData.getDefaultSocketConfig();
  }
  
  public int getMaxPerRoute(HttpRoute paramHttpRoute)
  {
    return this.pool.getMaxPerRoute(paramHttpRoute);
  }
  
  public int getMaxTotal()
  {
    return this.pool.getMaxTotal();
  }
  
  public SocketConfig getSocketConfig(HttpHost paramHttpHost)
  {
    return this.configData.getSocketConfig(paramHttpHost);
  }
  
  public PoolStats getStats(HttpRoute paramHttpRoute)
  {
    return this.pool.getStats(paramHttpRoute);
  }
  
  public PoolStats getTotalStats()
  {
    return this.pool.getTotalStats();
  }
  
  protected HttpClientConnection leaseConnection(Future<CPoolEntry> paramFuture, long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException
  {
    CPoolEntry localCPoolEntry;
    try
    {
      localCPoolEntry = (CPoolEntry)paramFuture.get(paramLong, paramTimeUnit);
      if ((localCPoolEntry == null) || (paramFuture.isCancelled())) {
        throw new InterruptedException();
      }
    }
    catch (TimeoutException localTimeoutException)
    {
      throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
    }
    if (localCPoolEntry.getConnection() != null) {}
    for (boolean bool = true;; bool = false)
    {
      Asserts.check(bool, "Pool entry with no connection");
      if (this.log.isDebugEnabled()) {
        this.log.debug("Connection leased: " + format(localCPoolEntry) + formatStats((HttpRoute)localCPoolEntry.getRoute()));
      }
      HttpClientConnection localHttpClientConnection = CPoolProxy.newProxy(localCPoolEntry);
      return localHttpClientConnection;
    }
  }
  
  /* Error */
  public void releaseConnection(HttpClientConnection paramHttpClientConnection, Object paramObject, long paramLong, TimeUnit paramTimeUnit)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc_w 348
    //   4: invokestatic 229	org/apache/http/util/Args:notNull	(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   7: pop
    //   8: aload_1
    //   9: monitorenter
    //   10: aload_1
    //   11: invokestatic 351	org/apache/http/impl/conn/CPoolProxy:detach	(Lorg/apache/http/HttpClientConnection;)Lorg/apache/http/impl/conn/CPoolEntry;
    //   14: astore 8
    //   16: aload 8
    //   18: ifnonnull +6 -> 24
    //   21: aload_1
    //   22: monitorexit
    //   23: return
    //   24: aload 8
    //   26: invokevirtual 240	org/apache/http/impl/conn/CPoolEntry:getConnection	()Ljava/lang/Object;
    //   29: checkcast 242	org/apache/http/conn/ManagedHttpClientConnection
    //   32: astore 9
    //   34: aload 9
    //   36: invokeinterface 354 1 0
    //   41: ifeq +118 -> 159
    //   44: aload 8
    //   46: aload_2
    //   47: invokevirtual 357	org/apache/http/impl/conn/CPoolEntry:setState	(Ljava/lang/Object;)V
    //   50: aload 5
    //   52: ifnull +214 -> 266
    //   55: aload 8
    //   57: lload_3
    //   58: aload 5
    //   60: invokevirtual 360	org/apache/http/impl/conn/CPoolEntry:updateExpiry	(JLjava/util/concurrent/TimeUnit;)V
    //   63: aload_0
    //   64: getfield 60	org/apache/http/impl/conn/PoolingHttpClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   67: invokeinterface 207 1 0
    //   72: ifeq +87 -> 159
    //   75: lload_3
    //   76: lconst_0
    //   77: lcmp
    //   78: ifle +196 -> 274
    //   81: new 90	java/lang/StringBuilder
    //   84: dup
    //   85: invokespecial 91	java/lang/StringBuilder:<init>	()V
    //   88: ldc_w 362
    //   91: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   94: lload_3
    //   95: l2d
    //   96: ldc2_w 363
    //   99: ddiv
    //   100: invokevirtual 367	java/lang/StringBuilder:append	(D)Ljava/lang/StringBuilder;
    //   103: ldc_w 369
    //   106: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   109: invokevirtual 108	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   112: astore 15
    //   114: aload_0
    //   115: getfield 60	org/apache/http/impl/conn/PoolingHttpClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   118: new 90	java/lang/StringBuilder
    //   121: dup
    //   122: invokespecial 91	java/lang/StringBuilder:<init>	()V
    //   125: ldc_w 371
    //   128: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: aload_0
    //   132: aload 8
    //   134: invokespecial 338	org/apache/http/impl/conn/PoolingHttpClientConnectionManager:format	(Lorg/apache/http/impl/conn/CPoolEntry;)Ljava/lang/String;
    //   137: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   140: ldc_w 373
    //   143: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   146: aload 15
    //   148: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   151: invokevirtual 108	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   154: invokeinterface 199 2 0
    //   159: aload_0
    //   160: getfield 77	org/apache/http/impl/conn/PoolingHttpClientConnectionManager:pool	Lorg/apache/http/impl/conn/CPool;
    //   163: astore 13
    //   165: aload 9
    //   167: invokeinterface 354 1 0
    //   172: ifeq +110 -> 282
    //   175: aload 8
    //   177: invokevirtual 376	org/apache/http/impl/conn/CPoolEntry:isRouteComplete	()Z
    //   180: ifeq +102 -> 282
    //   183: iconst_1
    //   184: istore 14
    //   186: aload 13
    //   188: aload 8
    //   190: iload 14
    //   192: invokevirtual 380	org/apache/http/impl/conn/CPool:release	(Lorg/apache/http/pool/PoolEntry;Z)V
    //   195: aload_0
    //   196: getfield 60	org/apache/http/impl/conn/PoolingHttpClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   199: invokeinterface 207 1 0
    //   204: ifeq +52 -> 256
    //   207: aload_0
    //   208: getfield 60	org/apache/http/impl/conn/PoolingHttpClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   211: new 90	java/lang/StringBuilder
    //   214: dup
    //   215: invokespecial 91	java/lang/StringBuilder:<init>	()V
    //   218: ldc_w 382
    //   221: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   224: aload_0
    //   225: aload 8
    //   227: invokespecial 338	org/apache/http/impl/conn/PoolingHttpClientConnectionManager:format	(Lorg/apache/http/impl/conn/CPoolEntry;)Ljava/lang/String;
    //   230: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   233: aload_0
    //   234: aload 8
    //   236: invokevirtual 120	org/apache/http/impl/conn/CPoolEntry:getRoute	()Ljava/lang/Object;
    //   239: checkcast 244	org/apache/http/conn/routing/HttpRoute
    //   242: invokespecial 340	org/apache/http/impl/conn/PoolingHttpClientConnectionManager:formatStats	(Lorg/apache/http/conn/routing/HttpRoute;)Ljava/lang/String;
    //   245: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   248: invokevirtual 108	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   251: invokeinterface 199 2 0
    //   256: aload_1
    //   257: monitorexit
    //   258: return
    //   259: astore 7
    //   261: aload_1
    //   262: monitorexit
    //   263: aload 7
    //   265: athrow
    //   266: getstatic 46	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
    //   269: astore 5
    //   271: goto -216 -> 55
    //   274: ldc_w 384
    //   277: astore 15
    //   279: goto -165 -> 114
    //   282: iconst_0
    //   283: istore 14
    //   285: goto -99 -> 186
    //   288: astore 10
    //   290: aload_0
    //   291: getfield 77	org/apache/http/impl/conn/PoolingHttpClientConnectionManager:pool	Lorg/apache/http/impl/conn/CPool;
    //   294: astore 11
    //   296: aload 9
    //   298: invokeinterface 354 1 0
    //   303: ifeq +87 -> 390
    //   306: aload 8
    //   308: invokevirtual 376	org/apache/http/impl/conn/CPoolEntry:isRouteComplete	()Z
    //   311: ifeq +79 -> 390
    //   314: iconst_1
    //   315: istore 12
    //   317: aload 11
    //   319: aload 8
    //   321: iload 12
    //   323: invokevirtual 380	org/apache/http/impl/conn/CPool:release	(Lorg/apache/http/pool/PoolEntry;Z)V
    //   326: aload_0
    //   327: getfield 60	org/apache/http/impl/conn/PoolingHttpClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   330: invokeinterface 207 1 0
    //   335: ifeq +52 -> 387
    //   338: aload_0
    //   339: getfield 60	org/apache/http/impl/conn/PoolingHttpClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   342: new 90	java/lang/StringBuilder
    //   345: dup
    //   346: invokespecial 91	java/lang/StringBuilder:<init>	()V
    //   349: ldc_w 382
    //   352: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   355: aload_0
    //   356: aload 8
    //   358: invokespecial 338	org/apache/http/impl/conn/PoolingHttpClientConnectionManager:format	(Lorg/apache/http/impl/conn/CPoolEntry;)Ljava/lang/String;
    //   361: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   364: aload_0
    //   365: aload 8
    //   367: invokevirtual 120	org/apache/http/impl/conn/CPoolEntry:getRoute	()Ljava/lang/Object;
    //   370: checkcast 244	org/apache/http/conn/routing/HttpRoute
    //   373: invokespecial 340	org/apache/http/impl/conn/PoolingHttpClientConnectionManager:formatStats	(Lorg/apache/http/conn/routing/HttpRoute;)Ljava/lang/String;
    //   376: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   379: invokevirtual 108	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   382: invokeinterface 199 2 0
    //   387: aload 10
    //   389: athrow
    //   390: iconst_0
    //   391: istore 12
    //   393: goto -76 -> 317
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	396	0	this	PoolingHttpClientConnectionManager
    //   0	396	1	paramHttpClientConnection	HttpClientConnection
    //   0	396	2	paramObject	Object
    //   0	396	3	paramLong	long
    //   0	396	5	paramTimeUnit	TimeUnit
    //   259	5	7	localObject1	Object
    //   14	352	8	localCPoolEntry	CPoolEntry
    //   32	265	9	localManagedHttpClientConnection	ManagedHttpClientConnection
    //   288	100	10	localObject2	Object
    //   294	24	11	localCPool1	CPool
    //   315	77	12	bool1	boolean
    //   163	24	13	localCPool2	CPool
    //   184	100	14	bool2	boolean
    //   112	166	15	str	String
    // Exception table:
    //   from	to	target	type
    //   10	16	259	finally
    //   21	23	259	finally
    //   24	34	259	finally
    //   159	183	259	finally
    //   186	256	259	finally
    //   256	258	259	finally
    //   261	263	259	finally
    //   290	314	259	finally
    //   317	387	259	finally
    //   387	390	259	finally
    //   34	50	288	finally
    //   55	75	288	finally
    //   81	114	288	finally
    //   114	159	288	finally
    //   266	271	288	finally
  }
  
  public ConnectionRequest requestConnection(HttpRoute paramHttpRoute, Object paramObject)
  {
    Args.notNull(paramHttpRoute, "HTTP route");
    if (this.log.isDebugEnabled()) {
      this.log.debug("Connection request: " + format(paramHttpRoute, paramObject) + formatStats(paramHttpRoute));
    }
    new ConnectionRequest()
    {
      public boolean cancel()
      {
        return this.val$future.cancel(true);
      }
      
      public HttpClientConnection get(long paramAnonymousLong, TimeUnit paramAnonymousTimeUnit)
        throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException
      {
        return PoolingHttpClientConnectionManager.this.leaseConnection(this.val$future, paramAnonymousLong, paramAnonymousTimeUnit);
      }
    };
  }
  
  public void routeComplete(HttpClientConnection paramHttpClientConnection, HttpRoute paramHttpRoute, HttpContext paramHttpContext)
    throws IOException
  {
    Args.notNull(paramHttpClientConnection, "Managed Connection");
    Args.notNull(paramHttpRoute, "HTTP route");
    try
    {
      CPoolProxy.getPoolEntry(paramHttpClientConnection).markRouteComplete();
      return;
    }
    finally {}
  }
  
  public void setConnectionConfig(HttpHost paramHttpHost, ConnectionConfig paramConnectionConfig)
  {
    this.configData.setConnectionConfig(paramHttpHost, paramConnectionConfig);
  }
  
  public void setDefaultConnectionConfig(ConnectionConfig paramConnectionConfig)
  {
    this.configData.setDefaultConnectionConfig(paramConnectionConfig);
  }
  
  public void setDefaultMaxPerRoute(int paramInt)
  {
    this.pool.setDefaultMaxPerRoute(paramInt);
  }
  
  public void setDefaultSocketConfig(SocketConfig paramSocketConfig)
  {
    this.configData.setDefaultSocketConfig(paramSocketConfig);
  }
  
  public void setMaxPerRoute(HttpRoute paramHttpRoute, int paramInt)
  {
    this.pool.setMaxPerRoute(paramHttpRoute, paramInt);
  }
  
  public void setMaxTotal(int paramInt)
  {
    this.pool.setMaxTotal(paramInt);
  }
  
  public void setSocketConfig(HttpHost paramHttpHost, SocketConfig paramSocketConfig)
  {
    this.configData.setSocketConfig(paramHttpHost, paramSocketConfig);
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
  
  public void upgrade(HttpClientConnection paramHttpClientConnection, HttpRoute paramHttpRoute, HttpContext paramHttpContext)
    throws IOException
  {
    Args.notNull(paramHttpClientConnection, "Managed Connection");
    Args.notNull(paramHttpRoute, "HTTP route");
    try
    {
      ManagedHttpClientConnection localManagedHttpClientConnection = (ManagedHttpClientConnection)CPoolProxy.getPoolEntry(paramHttpClientConnection).getConnection();
      this.connectionOperator.upgrade(localManagedHttpClientConnection, paramHttpRoute.getTargetHost(), paramHttpContext);
      return;
    }
    finally {}
  }
  
  static class ConfigData
  {
    private final Map<HttpHost, ConnectionConfig> connectionConfigMap = new ConcurrentHashMap();
    private volatile ConnectionConfig defaultConnectionConfig;
    private volatile SocketConfig defaultSocketConfig;
    private final Map<HttpHost, SocketConfig> socketConfigMap = new ConcurrentHashMap();
    
    public ConnectionConfig getConnectionConfig(HttpHost paramHttpHost)
    {
      return (ConnectionConfig)this.connectionConfigMap.get(paramHttpHost);
    }
    
    public ConnectionConfig getDefaultConnectionConfig()
    {
      return this.defaultConnectionConfig;
    }
    
    public SocketConfig getDefaultSocketConfig()
    {
      return this.defaultSocketConfig;
    }
    
    public SocketConfig getSocketConfig(HttpHost paramHttpHost)
    {
      return (SocketConfig)this.socketConfigMap.get(paramHttpHost);
    }
    
    public void setConnectionConfig(HttpHost paramHttpHost, ConnectionConfig paramConnectionConfig)
    {
      this.connectionConfigMap.put(paramHttpHost, paramConnectionConfig);
    }
    
    public void setDefaultConnectionConfig(ConnectionConfig paramConnectionConfig)
    {
      this.defaultConnectionConfig = paramConnectionConfig;
    }
    
    public void setDefaultSocketConfig(SocketConfig paramSocketConfig)
    {
      this.defaultSocketConfig = paramSocketConfig;
    }
    
    public void setSocketConfig(HttpHost paramHttpHost, SocketConfig paramSocketConfig)
    {
      this.socketConfigMap.put(paramHttpHost, paramSocketConfig);
    }
  }
  
  static class InternalConnectionFactory
    implements ConnFactory<HttpRoute, ManagedHttpClientConnection>
  {
    private final PoolingHttpClientConnectionManager.ConfigData configData;
    private final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory;
    
    InternalConnectionFactory(PoolingHttpClientConnectionManager.ConfigData paramConfigData, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> paramHttpConnectionFactory)
    {
      if (paramConfigData != null)
      {
        this.configData = paramConfigData;
        if (paramHttpConnectionFactory == null) {
          break label34;
        }
      }
      for (;;)
      {
        this.connFactory = paramHttpConnectionFactory;
        return;
        paramConfigData = new PoolingHttpClientConnectionManager.ConfigData();
        break;
        label34:
        paramHttpConnectionFactory = ManagedHttpClientConnectionFactory.INSTANCE;
      }
    }
    
    public ManagedHttpClientConnection create(HttpRoute paramHttpRoute)
      throws IOException
    {
      HttpHost localHttpHost = paramHttpRoute.getProxyHost();
      ConnectionConfig localConnectionConfig = null;
      if (localHttpHost != null) {
        localConnectionConfig = this.configData.getConnectionConfig(paramHttpRoute.getProxyHost());
      }
      if (localConnectionConfig == null) {
        localConnectionConfig = this.configData.getConnectionConfig(paramHttpRoute.getTargetHost());
      }
      if (localConnectionConfig == null) {
        localConnectionConfig = this.configData.getDefaultConnectionConfig();
      }
      if (localConnectionConfig == null) {
        localConnectionConfig = ConnectionConfig.DEFAULT;
      }
      return (ManagedHttpClientConnection)this.connFactory.create(paramHttpRoute, localConnectionConfig);
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.PoolingHttpClientConnectionManager
 * JD-Core Version:    0.7.0.1
 */