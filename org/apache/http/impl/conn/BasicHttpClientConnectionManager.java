package org.apache.http.impl.conn;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
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
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@ThreadSafe
public class BasicHttpClientConnectionManager
  implements HttpClientConnectionManager, Closeable
{
  @GuardedBy("this")
  private ManagedHttpClientConnection conn;
  @GuardedBy("this")
  private ConnectionConfig connConfig;
  private final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory;
  private final HttpClientConnectionOperator connectionOperator;
  @GuardedBy("this")
  private long expiry;
  @GuardedBy("this")
  private boolean leased;
  private final Log log = LogFactory.getLog(getClass());
  @GuardedBy("this")
  private HttpRoute route;
  @GuardedBy("this")
  private volatile boolean shutdown;
  @GuardedBy("this")
  private SocketConfig socketConfig;
  @GuardedBy("this")
  private Object state;
  @GuardedBy("this")
  private long updated;
  
  public BasicHttpClientConnectionManager()
  {
    this(getDefaultRegistry(), null, null, null);
  }
  
  public BasicHttpClientConnectionManager(Lookup<ConnectionSocketFactory> paramLookup)
  {
    this(paramLookup, null, null, null);
  }
  
  public BasicHttpClientConnectionManager(Lookup<ConnectionSocketFactory> paramLookup, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> paramHttpConnectionFactory)
  {
    this(paramLookup, paramHttpConnectionFactory, null, null);
  }
  
  public BasicHttpClientConnectionManager(Lookup<ConnectionSocketFactory> paramLookup, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> paramHttpConnectionFactory, SchemePortResolver paramSchemePortResolver, DnsResolver paramDnsResolver)
  {
    this.connectionOperator = new HttpClientConnectionOperator(paramLookup, paramSchemePortResolver, paramDnsResolver);
    if (paramHttpConnectionFactory != null) {}
    for (;;)
    {
      this.connFactory = paramHttpConnectionFactory;
      this.expiry = 9223372036854775807L;
      this.socketConfig = SocketConfig.DEFAULT;
      this.connConfig = ConnectionConfig.DEFAULT;
      return;
      paramHttpConnectionFactory = ManagedHttpClientConnectionFactory.INSTANCE;
    }
  }
  
  private void checkExpiry()
  {
    if ((this.conn != null) && (System.currentTimeMillis() >= this.expiry))
    {
      if (this.log.isDebugEnabled()) {
        this.log.debug("Connection expired @ " + new Date(this.expiry));
      }
      closeConnection();
    }
  }
  
  private void closeConnection()
  {
    if (this.conn != null) {
      this.log.debug("Closing connection");
    }
    try
    {
      this.conn.close();
      this.conn = null;
      return;
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        if (this.log.isDebugEnabled()) {
          this.log.debug("I/O exception closing connection", localIOException);
        }
      }
    }
  }
  
  private static Registry<ConnectionSocketFactory> getDefaultRegistry()
  {
    return RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
  }
  
  private void shutdownConnection()
  {
    if (this.conn != null) {
      this.log.debug("Shutting down connection");
    }
    try
    {
      this.conn.shutdown();
      this.conn = null;
      return;
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        if (this.log.isDebugEnabled()) {
          this.log.debug("I/O exception shutting down connection", localIOException);
        }
      }
    }
  }
  
  public void close()
  {
    shutdown();
  }
  
  /* Error */
  public void closeExpiredConnections()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 188	org/apache/http/impl/conn/BasicHttpClientConnectionManager:shutdown	Z
    //   6: istore_2
    //   7: iload_2
    //   8: ifeq +6 -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: aload_0
    //   15: getfield 190	org/apache/http/impl/conn/BasicHttpClientConnectionManager:leased	Z
    //   18: ifne -7 -> 11
    //   21: aload_0
    //   22: invokespecial 192	org/apache/http/impl/conn/BasicHttpClientConnectionManager:checkExpiry	()V
    //   25: goto -14 -> 11
    //   28: astore_1
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_1
    //   32: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	33	0	this	BasicHttpClientConnectionManager
    //   28	4	1	localObject	Object
    //   6	2	2	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   2	7	28	finally
    //   14	25	28	finally
  }
  
  /* Error */
  public void closeIdleConnections(long paramLong, TimeUnit paramTimeUnit)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_3
    //   3: ldc 196
    //   5: invokestatic 202	org/apache/http/util/Args:notNull	(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   8: pop
    //   9: aload_0
    //   10: getfield 188	org/apache/http/impl/conn/BasicHttpClientConnectionManager:shutdown	Z
    //   13: istore 6
    //   15: iload 6
    //   17: ifeq +6 -> 23
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: aload_0
    //   24: getfield 190	org/apache/http/impl/conn/BasicHttpClientConnectionManager:leased	Z
    //   27: ifne -7 -> 20
    //   30: aload_3
    //   31: lload_1
    //   32: invokevirtual 208	java/util/concurrent/TimeUnit:toMillis	(J)J
    //   35: lstore 7
    //   37: lload 7
    //   39: lconst_0
    //   40: lcmp
    //   41: ifge +6 -> 47
    //   44: lconst_0
    //   45: lstore 7
    //   47: invokestatic 101	java/lang/System:currentTimeMillis	()J
    //   50: lload 7
    //   52: lsub
    //   53: lstore 9
    //   55: aload_0
    //   56: getfield 210	org/apache/http/impl/conn/BasicHttpClientConnectionManager:updated	J
    //   59: lload 9
    //   61: lcmp
    //   62: ifgt -42 -> 20
    //   65: aload_0
    //   66: invokespecial 135	org/apache/http/impl/conn/BasicHttpClientConnectionManager:closeConnection	()V
    //   69: goto -49 -> 20
    //   72: astore 4
    //   74: aload_0
    //   75: monitorexit
    //   76: aload 4
    //   78: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	79	0	this	BasicHttpClientConnectionManager
    //   0	79	1	paramLong	long
    //   0	79	3	paramTimeUnit	TimeUnit
    //   72	5	4	localObject	Object
    //   13	3	6	bool	boolean
    //   35	16	7	l1	long
    //   53	7	9	l2	long
    // Exception table:
    //   from	to	target	type
    //   2	15	72	finally
    //   23	37	72	finally
    //   47	69	72	finally
  }
  
  public void connect(HttpClientConnection paramHttpClientConnection, HttpRoute paramHttpRoute, int paramInt, HttpContext paramHttpContext)
    throws IOException
  {
    Args.notNull(paramHttpClientConnection, "Connection");
    Args.notNull(paramHttpRoute, "HTTP route");
    boolean bool;
    if (paramHttpClientConnection == this.conn)
    {
      bool = true;
      Asserts.check(bool, "Connection not obtained from this manager");
      if (paramHttpRoute.getProxyHost() == null) {
        break label80;
      }
    }
    label80:
    for (HttpHost localHttpHost = paramHttpRoute.getProxyHost();; localHttpHost = paramHttpRoute.getTargetHost())
    {
      InetSocketAddress localInetSocketAddress = paramHttpRoute.getLocalSocketAddress();
      this.connectionOperator.connect(this.conn, localHttpHost, localInetSocketAddress, paramInt, this.socketConfig, paramHttpContext);
      return;
      bool = false;
      break;
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
  
  /* Error */
  HttpClientConnection getConnection(HttpRoute paramHttpRoute, Object paramObject)
  {
    // Byte code:
    //   0: iconst_1
    //   1: istore_3
    //   2: aload_0
    //   3: monitorenter
    //   4: aload_0
    //   5: getfield 188	org/apache/http/impl/conn/BasicHttpClientConnectionManager:shutdown	Z
    //   8: ifne +150 -> 158
    //   11: iload_3
    //   12: istore 5
    //   14: iload 5
    //   16: ldc 249
    //   18: invokestatic 224	org/apache/http/util/Asserts:check	(ZLjava/lang/String;)V
    //   21: aload_0
    //   22: getfield 60	org/apache/http/impl/conn/BasicHttpClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   25: invokeinterface 107 1 0
    //   30: ifeq +31 -> 61
    //   33: aload_0
    //   34: getfield 60	org/apache/http/impl/conn/BasicHttpClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   37: new 109	java/lang/StringBuilder
    //   40: dup
    //   41: invokespecial 110	java/lang/StringBuilder:<init>	()V
    //   44: ldc 251
    //   46: invokevirtual 116	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   49: aload_1
    //   50: invokevirtual 124	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   53: invokevirtual 128	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   56: invokeinterface 132 2 0
    //   61: aload_0
    //   62: getfield 190	org/apache/http/impl/conn/BasicHttpClientConnectionManager:leased	Z
    //   65: ifne +99 -> 164
    //   68: iload_3
    //   69: ldc 253
    //   71: invokestatic 224	org/apache/http/util/Asserts:check	(ZLjava/lang/String;)V
    //   74: aload_0
    //   75: getfield 255	org/apache/http/impl/conn/BasicHttpClientConnectionManager:route	Lorg/apache/http/conn/routing/HttpRoute;
    //   78: aload_1
    //   79: invokestatic 261	org/apache/http/util/LangUtils:equals	(Ljava/lang/Object;Ljava/lang/Object;)Z
    //   82: ifeq +14 -> 96
    //   85: aload_0
    //   86: getfield 263	org/apache/http/impl/conn/BasicHttpClientConnectionManager:state	Ljava/lang/Object;
    //   89: aload_2
    //   90: invokestatic 261	org/apache/http/util/LangUtils:equals	(Ljava/lang/Object;Ljava/lang/Object;)Z
    //   93: ifne +7 -> 100
    //   96: aload_0
    //   97: invokespecial 135	org/apache/http/impl/conn/BasicHttpClientConnectionManager:closeConnection	()V
    //   100: aload_0
    //   101: aload_1
    //   102: putfield 255	org/apache/http/impl/conn/BasicHttpClientConnectionManager:route	Lorg/apache/http/conn/routing/HttpRoute;
    //   105: aload_0
    //   106: aload_2
    //   107: putfield 263	org/apache/http/impl/conn/BasicHttpClientConnectionManager:state	Ljava/lang/Object;
    //   110: aload_0
    //   111: invokespecial 192	org/apache/http/impl/conn/BasicHttpClientConnectionManager:checkExpiry	()V
    //   114: aload_0
    //   115: getfield 95	org/apache/http/impl/conn/BasicHttpClientConnectionManager:conn	Lorg/apache/http/conn/ManagedHttpClientConnection;
    //   118: ifnonnull +24 -> 142
    //   121: aload_0
    //   122: aload_0
    //   123: getfield 69	org/apache/http/impl/conn/BasicHttpClientConnectionManager:connFactory	Lorg/apache/http/conn/HttpConnectionFactory;
    //   126: aload_1
    //   127: aload_0
    //   128: getfield 86	org/apache/http/impl/conn/BasicHttpClientConnectionManager:connConfig	Lorg/apache/http/config/ConnectionConfig;
    //   131: invokeinterface 268 3 0
    //   136: checkcast 141	org/apache/http/conn/ManagedHttpClientConnection
    //   139: putfield 95	org/apache/http/impl/conn/BasicHttpClientConnectionManager:conn	Lorg/apache/http/conn/ManagedHttpClientConnection;
    //   142: aload_0
    //   143: iconst_1
    //   144: putfield 190	org/apache/http/impl/conn/BasicHttpClientConnectionManager:leased	Z
    //   147: aload_0
    //   148: getfield 95	org/apache/http/impl/conn/BasicHttpClientConnectionManager:conn	Lorg/apache/http/conn/ManagedHttpClientConnection;
    //   151: astore 6
    //   153: aload_0
    //   154: monitorexit
    //   155: aload 6
    //   157: areturn
    //   158: iconst_0
    //   159: istore 5
    //   161: goto -147 -> 14
    //   164: iconst_0
    //   165: istore_3
    //   166: goto -98 -> 68
    //   169: astore 4
    //   171: aload_0
    //   172: monitorexit
    //   173: aload 4
    //   175: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	176	0	this	BasicHttpClientConnectionManager
    //   0	176	1	paramHttpRoute	HttpRoute
    //   0	176	2	paramObject	Object
    //   1	165	3	bool1	boolean
    //   169	5	4	localObject	Object
    //   12	148	5	bool2	boolean
    //   151	5	6	localManagedHttpClientConnection	ManagedHttpClientConnection
    // Exception table:
    //   from	to	target	type
    //   4	11	169	finally
    //   14	61	169	finally
    //   61	68	169	finally
    //   68	96	169	finally
    //   96	100	169	finally
    //   100	142	169	finally
    //   142	153	169	finally
  }
  
  public ConnectionConfig getConnectionConfig()
  {
    try
    {
      ConnectionConfig localConnectionConfig = this.connConfig;
      return localConnectionConfig;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  HttpRoute getRoute()
  {
    return this.route;
  }
  
  public SocketConfig getSocketConfig()
  {
    try
    {
      SocketConfig localSocketConfig = this.socketConfig;
      return localSocketConfig;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  Object getState()
  {
    return this.state;
  }
  
  /* Error */
  public void releaseConnection(HttpClientConnection paramHttpClientConnection, Object paramObject, long paramLong, TimeUnit paramTimeUnit)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ldc 214
    //   5: invokestatic 202	org/apache/http/util/Args:notNull	(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   8: pop
    //   9: aload_0
    //   10: getfield 95	org/apache/http/impl/conn/BasicHttpClientConnectionManager:conn	Lorg/apache/http/conn/ManagedHttpClientConnection;
    //   13: astore 8
    //   15: iconst_0
    //   16: istore 9
    //   18: aload_1
    //   19: aload 8
    //   21: if_acmpne +6 -> 27
    //   24: iconst_1
    //   25: istore 9
    //   27: iload 9
    //   29: ldc 218
    //   31: invokestatic 224	org/apache/http/util/Asserts:check	(ZLjava/lang/String;)V
    //   34: aload_0
    //   35: getfield 60	org/apache/http/impl/conn/BasicHttpClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   38: invokeinterface 107 1 0
    //   43: ifeq +32 -> 75
    //   46: aload_0
    //   47: getfield 60	org/apache/http/impl/conn/BasicHttpClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   50: new 109	java/lang/StringBuilder
    //   53: dup
    //   54: invokespecial 110	java/lang/StringBuilder:<init>	()V
    //   57: ldc_w 280
    //   60: invokevirtual 116	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: aload_1
    //   64: invokevirtual 124	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   67: invokevirtual 128	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   70: invokeinterface 132 2 0
    //   75: aload_0
    //   76: getfield 188	org/apache/http/impl/conn/BasicHttpClientConnectionManager:shutdown	Z
    //   79: ifeq +10 -> 89
    //   82: aload_0
    //   83: invokespecial 282	org/apache/http/impl/conn/BasicHttpClientConnectionManager:shutdownConnection	()V
    //   86: aload_0
    //   87: monitorexit
    //   88: return
    //   89: aload_0
    //   90: invokestatic 101	java/lang/System:currentTimeMillis	()J
    //   93: putfield 210	org/apache/http/impl/conn/BasicHttpClientConnectionManager:updated	J
    //   96: aload_0
    //   97: getfield 95	org/apache/http/impl/conn/BasicHttpClientConnectionManager:conn	Lorg/apache/http/conn/ManagedHttpClientConnection;
    //   100: invokeinterface 285 1 0
    //   105: ifne +40 -> 145
    //   108: aload_0
    //   109: aconst_null
    //   110: putfield 95	org/apache/http/impl/conn/BasicHttpClientConnectionManager:conn	Lorg/apache/http/conn/ManagedHttpClientConnection;
    //   113: aload_0
    //   114: aconst_null
    //   115: putfield 255	org/apache/http/impl/conn/BasicHttpClientConnectionManager:route	Lorg/apache/http/conn/routing/HttpRoute;
    //   118: aload_0
    //   119: aconst_null
    //   120: putfield 95	org/apache/http/impl/conn/BasicHttpClientConnectionManager:conn	Lorg/apache/http/conn/ManagedHttpClientConnection;
    //   123: aload_0
    //   124: ldc2_w 70
    //   127: putfield 73	org/apache/http/impl/conn/BasicHttpClientConnectionManager:expiry	J
    //   130: aload_0
    //   131: iconst_0
    //   132: putfield 190	org/apache/http/impl/conn/BasicHttpClientConnectionManager:leased	Z
    //   135: goto -49 -> 86
    //   138: astore 6
    //   140: aload_0
    //   141: monitorexit
    //   142: aload 6
    //   144: athrow
    //   145: aload_0
    //   146: aload_2
    //   147: putfield 263	org/apache/http/impl/conn/BasicHttpClientConnectionManager:state	Ljava/lang/Object;
    //   150: aload_0
    //   151: getfield 60	org/apache/http/impl/conn/BasicHttpClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   154: invokeinterface 107 1 0
    //   159: ifeq +72 -> 231
    //   162: lload_3
    //   163: lconst_0
    //   164: lcmp
    //   165: ifle +100 -> 265
    //   168: new 109	java/lang/StringBuilder
    //   171: dup
    //   172: invokespecial 110	java/lang/StringBuilder:<init>	()V
    //   175: ldc_w 287
    //   178: invokevirtual 116	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   181: lload_3
    //   182: invokevirtual 290	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   185: ldc_w 292
    //   188: invokevirtual 116	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   191: aload 5
    //   193: invokevirtual 124	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   196: invokevirtual 128	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   199: astore 11
    //   201: aload_0
    //   202: getfield 60	org/apache/http/impl/conn/BasicHttpClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   205: new 109	java/lang/StringBuilder
    //   208: dup
    //   209: invokespecial 110	java/lang/StringBuilder:<init>	()V
    //   212: ldc_w 294
    //   215: invokevirtual 116	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   218: aload 11
    //   220: invokevirtual 116	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   223: invokevirtual 128	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   226: invokeinterface 132 2 0
    //   231: lload_3
    //   232: lconst_0
    //   233: lcmp
    //   234: ifle +39 -> 273
    //   237: aload_0
    //   238: aload_0
    //   239: getfield 210	org/apache/http/impl/conn/BasicHttpClientConnectionManager:updated	J
    //   242: aload 5
    //   244: lload_3
    //   245: invokevirtual 208	java/util/concurrent/TimeUnit:toMillis	(J)J
    //   248: ladd
    //   249: putfield 73	org/apache/http/impl/conn/BasicHttpClientConnectionManager:expiry	J
    //   252: goto -122 -> 130
    //   255: astore 10
    //   257: aload_0
    //   258: iconst_0
    //   259: putfield 190	org/apache/http/impl/conn/BasicHttpClientConnectionManager:leased	Z
    //   262: aload 10
    //   264: athrow
    //   265: ldc_w 296
    //   268: astore 11
    //   270: goto -69 -> 201
    //   273: aload_0
    //   274: ldc2_w 70
    //   277: putfield 73	org/apache/http/impl/conn/BasicHttpClientConnectionManager:expiry	J
    //   280: goto -150 -> 130
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	283	0	this	BasicHttpClientConnectionManager
    //   0	283	1	paramHttpClientConnection	HttpClientConnection
    //   0	283	2	paramObject	Object
    //   0	283	3	paramLong	long
    //   0	283	5	paramTimeUnit	TimeUnit
    //   138	5	6	localObject1	Object
    //   13	7	8	localManagedHttpClientConnection	ManagedHttpClientConnection
    //   16	12	9	bool	boolean
    //   255	8	10	localObject2	Object
    //   199	70	11	str	java.lang.String
    // Exception table:
    //   from	to	target	type
    //   2	15	138	finally
    //   27	75	138	finally
    //   75	86	138	finally
    //   130	135	138	finally
    //   257	265	138	finally
    //   89	130	255	finally
    //   145	162	255	finally
    //   168	201	255	finally
    //   201	231	255	finally
    //   237	252	255	finally
    //   273	280	255	finally
  }
  
  public final ConnectionRequest requestConnection(final HttpRoute paramHttpRoute, final Object paramObject)
  {
    Args.notNull(paramHttpRoute, "Route");
    new ConnectionRequest()
    {
      public boolean cancel()
      {
        return false;
      }
      
      public HttpClientConnection get(long paramAnonymousLong, TimeUnit paramAnonymousTimeUnit)
      {
        return BasicHttpClientConnectionManager.this.getConnection(paramHttpRoute, paramObject);
      }
    };
  }
  
  public void routeComplete(HttpClientConnection paramHttpClientConnection, HttpRoute paramHttpRoute, HttpContext paramHttpContext)
    throws IOException
  {}
  
  public void setConnectionConfig(ConnectionConfig paramConnectionConfig)
  {
    if (paramConnectionConfig != null) {}
    for (;;)
    {
      try
      {
        this.connConfig = paramConnectionConfig;
        return;
      }
      finally {}
      paramConnectionConfig = ConnectionConfig.DEFAULT;
    }
  }
  
  public void setSocketConfig(SocketConfig paramSocketConfig)
  {
    if (paramSocketConfig != null) {}
    for (;;)
    {
      try
      {
        this.socketConfig = paramSocketConfig;
        return;
      }
      finally {}
      paramSocketConfig = SocketConfig.DEFAULT;
    }
  }
  
  /* Error */
  public void shutdown()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 188	org/apache/http/impl/conn/BasicHttpClientConnectionManager:shutdown	Z
    //   6: istore_2
    //   7: iload_2
    //   8: ifeq +6 -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: aload_0
    //   15: iconst_1
    //   16: putfield 188	org/apache/http/impl/conn/BasicHttpClientConnectionManager:shutdown	Z
    //   19: aload_0
    //   20: invokespecial 282	org/apache/http/impl/conn/BasicHttpClientConnectionManager:shutdownConnection	()V
    //   23: goto -12 -> 11
    //   26: astore_1
    //   27: aload_0
    //   28: monitorexit
    //   29: aload_1
    //   30: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	31	0	this	BasicHttpClientConnectionManager
    //   26	4	1	localObject	Object
    //   6	2	2	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   2	7	26	finally
    //   14	23	26	finally
  }
  
  public void upgrade(HttpClientConnection paramHttpClientConnection, HttpRoute paramHttpRoute, HttpContext paramHttpContext)
    throws IOException
  {
    Args.notNull(paramHttpClientConnection, "Connection");
    Args.notNull(paramHttpRoute, "HTTP route");
    if (paramHttpClientConnection == this.conn) {}
    for (boolean bool = true;; bool = false)
    {
      Asserts.check(bool, "Connection not obtained from this manager");
      this.connectionOperator.upgrade(this.conn, paramHttpRoute.getTargetHost(), paramHttpContext);
      return;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.BasicHttpClientConnectionManager
 * JD-Core Version:    0.7.0.1
 */