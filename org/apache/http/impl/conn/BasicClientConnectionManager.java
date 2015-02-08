package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpClientConnection;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
@ThreadSafe
public class BasicClientConnectionManager
  implements ClientConnectionManager
{
  private static final AtomicLong COUNTER = new AtomicLong();
  public static final String MISUSE_MESSAGE = "Invalid use of BasicClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
  @GuardedBy("this")
  private ManagedClientConnectionImpl conn;
  private final ClientConnectionOperator connOperator;
  private final Log log = LogFactory.getLog(getClass());
  @GuardedBy("this")
  private HttpPoolEntry poolEntry;
  private final SchemeRegistry schemeRegistry;
  @GuardedBy("this")
  private volatile boolean shutdown;
  
  public BasicClientConnectionManager()
  {
    this(SchemeRegistryFactory.createDefault());
  }
  
  public BasicClientConnectionManager(SchemeRegistry paramSchemeRegistry)
  {
    Args.notNull(paramSchemeRegistry, "Scheme registry");
    this.schemeRegistry = paramSchemeRegistry;
    this.connOperator = createConnectionOperator(paramSchemeRegistry);
  }
  
  private void assertNotShutdown()
  {
    if (!this.shutdown) {}
    for (boolean bool = true;; bool = false)
    {
      Asserts.check(bool, "Connection manager has been shut down");
      return;
    }
  }
  
  private void shutdownConnection(HttpClientConnection paramHttpClientConnection)
  {
    try
    {
      paramHttpClientConnection.shutdown();
      return;
    }
    catch (IOException localIOException)
    {
      while (!this.log.isDebugEnabled()) {}
      this.log.debug("I/O exception shutting down connection", localIOException);
    }
  }
  
  public void closeExpiredConnections()
  {
    try
    {
      assertNotShutdown();
      long l = System.currentTimeMillis();
      if ((this.poolEntry != null) && (this.poolEntry.isExpired(l)))
      {
        this.poolEntry.close();
        this.poolEntry.getTracker().reset();
      }
      return;
    }
    finally {}
  }
  
  public void closeIdleConnections(long paramLong, TimeUnit paramTimeUnit)
  {
    Args.notNull(paramTimeUnit, "Time unit");
    try
    {
      assertNotShutdown();
      long l1 = paramTimeUnit.toMillis(paramLong);
      if (l1 < 0L) {
        l1 = 0L;
      }
      long l2 = System.currentTimeMillis() - l1;
      if ((this.poolEntry != null) && (this.poolEntry.getUpdated() <= l2))
      {
        this.poolEntry.close();
        this.poolEntry.getTracker().reset();
      }
      return;
    }
    finally {}
  }
  
  protected ClientConnectionOperator createConnectionOperator(SchemeRegistry paramSchemeRegistry)
  {
    return new DefaultClientConnectionOperator(paramSchemeRegistry);
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
  
  ManagedClientConnection getConnection(HttpRoute paramHttpRoute, Object paramObject)
  {
    Args.notNull(paramHttpRoute, "Route");
    for (;;)
    {
      try
      {
        assertNotShutdown();
        if (this.log.isDebugEnabled()) {
          this.log.debug("Get connection for route " + paramHttpRoute);
        }
        if (this.conn == null)
        {
          bool = true;
          Asserts.check(bool, "Invalid use of BasicClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.");
          if ((this.poolEntry != null) && (!this.poolEntry.getPlannedRoute().equals(paramHttpRoute)))
          {
            this.poolEntry.close();
            this.poolEntry = null;
          }
          if (this.poolEntry == null)
          {
            String str = Long.toString(COUNTER.getAndIncrement());
            OperatedClientConnection localOperatedClientConnection = this.connOperator.createConnection();
            this.poolEntry = new HttpPoolEntry(this.log, str, paramHttpRoute, localOperatedClientConnection, 0L, TimeUnit.MILLISECONDS);
          }
          long l = System.currentTimeMillis();
          if (this.poolEntry.isExpired(l))
          {
            this.poolEntry.close();
            this.poolEntry.getTracker().reset();
          }
          this.conn = new ManagedClientConnectionImpl(this, this.connOperator, this.poolEntry);
          ManagedClientConnectionImpl localManagedClientConnectionImpl = this.conn;
          return localManagedClientConnectionImpl;
        }
      }
      finally {}
      boolean bool = false;
    }
  }
  
  public SchemeRegistry getSchemeRegistry()
  {
    return this.schemeRegistry;
  }
  
  /* Error */
  public void releaseConnection(ManagedClientConnection paramManagedClientConnection, long paramLong, TimeUnit paramTimeUnit)
  {
    // Byte code:
    //   0: aload_1
    //   1: instanceof 216
    //   4: ldc 224
    //   6: invokestatic 225	org/apache/http/util/Args:check	(ZLjava/lang/String;)V
    //   9: aload_1
    //   10: checkcast 216	org/apache/http/impl/conn/ManagedClientConnectionImpl
    //   13: astore 5
    //   15: aload 5
    //   17: monitorenter
    //   18: aload_0
    //   19: getfield 60	org/apache/http/impl/conn/BasicClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   22: invokeinterface 101 1 0
    //   27: ifeq +31 -> 58
    //   30: aload_0
    //   31: getfield 60	org/apache/http/impl/conn/BasicClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   34: new 164	java/lang/StringBuilder
    //   37: dup
    //   38: invokespecial 165	java/lang/StringBuilder:<init>	()V
    //   41: ldc 227
    //   43: invokevirtual 171	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   46: aload_1
    //   47: invokevirtual 174	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   50: invokevirtual 178	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   53: invokeinterface 181 2 0
    //   58: aload 5
    //   60: invokevirtual 231	org/apache/http/impl/conn/ManagedClientConnectionImpl:getPoolEntry	()Lorg/apache/http/impl/conn/HttpPoolEntry;
    //   63: ifnonnull +7 -> 70
    //   66: aload 5
    //   68: monitorexit
    //   69: return
    //   70: aload 5
    //   72: invokevirtual 235	org/apache/http/impl/conn/ManagedClientConnectionImpl:getManager	()Lorg/apache/http/conn/ClientConnectionManager;
    //   75: aload_0
    //   76: if_acmpne +42 -> 118
    //   79: iconst_1
    //   80: istore 7
    //   82: iload 7
    //   84: ldc 237
    //   86: invokestatic 87	org/apache/http/util/Asserts:check	(ZLjava/lang/String;)V
    //   89: aload_0
    //   90: monitorenter
    //   91: aload_0
    //   92: getfield 79	org/apache/http/impl/conn/BasicClientConnectionManager:shutdown	Z
    //   95: ifeq +29 -> 124
    //   98: aload_0
    //   99: aload 5
    //   101: invokespecial 239	org/apache/http/impl/conn/BasicClientConnectionManager:shutdownConnection	(Lorg/apache/http/HttpClientConnection;)V
    //   104: aload_0
    //   105: monitorexit
    //   106: aload 5
    //   108: monitorexit
    //   109: return
    //   110: astore 6
    //   112: aload 5
    //   114: monitorexit
    //   115: aload 6
    //   117: athrow
    //   118: iconst_0
    //   119: istore 7
    //   121: goto -39 -> 82
    //   124: aload 5
    //   126: invokevirtual 242	org/apache/http/impl/conn/ManagedClientConnectionImpl:isOpen	()Z
    //   129: ifeq +17 -> 146
    //   132: aload 5
    //   134: invokevirtual 245	org/apache/http/impl/conn/ManagedClientConnectionImpl:isMarkedReusable	()Z
    //   137: ifne +9 -> 146
    //   140: aload_0
    //   141: aload 5
    //   143: invokespecial 239	org/apache/http/impl/conn/BasicClientConnectionManager:shutdownConnection	(Lorg/apache/http/HttpClientConnection;)V
    //   146: aload 5
    //   148: invokevirtual 245	org/apache/http/impl/conn/ManagedClientConnectionImpl:isMarkedReusable	()Z
    //   151: ifeq +105 -> 256
    //   154: aload_0
    //   155: getfield 118	org/apache/http/impl/conn/BasicClientConnectionManager:poolEntry	Lorg/apache/http/impl/conn/HttpPoolEntry;
    //   158: astore 12
    //   160: aload 4
    //   162: ifnull +126 -> 288
    //   165: aload 4
    //   167: astore 13
    //   169: aload 12
    //   171: lload_2
    //   172: aload 13
    //   174: invokevirtual 248	org/apache/http/impl/conn/HttpPoolEntry:updateExpiry	(JLjava/util/concurrent/TimeUnit;)V
    //   177: aload_0
    //   178: getfield 60	org/apache/http/impl/conn/BasicClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   181: invokeinterface 101 1 0
    //   186: ifeq +70 -> 256
    //   189: lload_2
    //   190: lconst_0
    //   191: lcmp
    //   192: ifle +104 -> 296
    //   195: new 164	java/lang/StringBuilder
    //   198: dup
    //   199: invokespecial 165	java/lang/StringBuilder:<init>	()V
    //   202: ldc 250
    //   204: invokevirtual 171	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   207: lload_2
    //   208: invokevirtual 253	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   211: ldc 255
    //   213: invokevirtual 171	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   216: aload 4
    //   218: invokevirtual 174	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   221: invokevirtual 178	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   224: astore 14
    //   226: aload_0
    //   227: getfield 60	org/apache/http/impl/conn/BasicClientConnectionManager:log	Lorg/apache/commons/logging/Log;
    //   230: new 164	java/lang/StringBuilder
    //   233: dup
    //   234: invokespecial 165	java/lang/StringBuilder:<init>	()V
    //   237: ldc_w 257
    //   240: invokevirtual 171	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   243: aload 14
    //   245: invokevirtual 171	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   248: invokevirtual 178	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   251: invokeinterface 181 2 0
    //   256: aload 5
    //   258: invokevirtual 260	org/apache/http/impl/conn/ManagedClientConnectionImpl:detach	()Lorg/apache/http/impl/conn/HttpPoolEntry;
    //   261: pop
    //   262: aload_0
    //   263: aconst_null
    //   264: putfield 183	org/apache/http/impl/conn/BasicClientConnectionManager:conn	Lorg/apache/http/impl/conn/ManagedClientConnectionImpl;
    //   267: aload_0
    //   268: getfield 118	org/apache/http/impl/conn/BasicClientConnectionManager:poolEntry	Lorg/apache/http/impl/conn/HttpPoolEntry;
    //   271: invokevirtual 263	org/apache/http/impl/conn/HttpPoolEntry:isClosed	()Z
    //   274: ifeq +8 -> 282
    //   277: aload_0
    //   278: aconst_null
    //   279: putfield 118	org/apache/http/impl/conn/BasicClientConnectionManager:poolEntry	Lorg/apache/http/impl/conn/HttpPoolEntry;
    //   282: aload_0
    //   283: monitorexit
    //   284: aload 5
    //   286: monitorexit
    //   287: return
    //   288: getstatic 211	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
    //   291: astore 13
    //   293: goto -124 -> 169
    //   296: ldc_w 265
    //   299: astore 14
    //   301: goto -75 -> 226
    //   304: astore 9
    //   306: aload 5
    //   308: invokevirtual 260	org/apache/http/impl/conn/ManagedClientConnectionImpl:detach	()Lorg/apache/http/impl/conn/HttpPoolEntry;
    //   311: pop
    //   312: aload_0
    //   313: aconst_null
    //   314: putfield 183	org/apache/http/impl/conn/BasicClientConnectionManager:conn	Lorg/apache/http/impl/conn/ManagedClientConnectionImpl;
    //   317: aload_0
    //   318: getfield 118	org/apache/http/impl/conn/BasicClientConnectionManager:poolEntry	Lorg/apache/http/impl/conn/HttpPoolEntry;
    //   321: invokevirtual 263	org/apache/http/impl/conn/HttpPoolEntry:isClosed	()Z
    //   324: ifeq +8 -> 332
    //   327: aload_0
    //   328: aconst_null
    //   329: putfield 118	org/apache/http/impl/conn/BasicClientConnectionManager:poolEntry	Lorg/apache/http/impl/conn/HttpPoolEntry;
    //   332: aload 9
    //   334: athrow
    //   335: astore 8
    //   337: aload_0
    //   338: monitorexit
    //   339: aload 8
    //   341: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	342	0	this	BasicClientConnectionManager
    //   0	342	1	paramManagedClientConnection	ManagedClientConnection
    //   0	342	2	paramLong	long
    //   0	342	4	paramTimeUnit	TimeUnit
    //   13	294	5	localManagedClientConnectionImpl	ManagedClientConnectionImpl
    //   110	6	6	localObject1	Object
    //   80	40	7	bool	boolean
    //   335	5	8	localObject2	Object
    //   304	29	9	localObject3	Object
    //   158	12	12	localHttpPoolEntry	HttpPoolEntry
    //   167	125	13	localTimeUnit	TimeUnit
    //   224	76	14	str	String
    // Exception table:
    //   from	to	target	type
    //   18	58	110	finally
    //   58	69	110	finally
    //   70	79	110	finally
    //   82	91	110	finally
    //   106	109	110	finally
    //   112	115	110	finally
    //   284	287	110	finally
    //   339	342	110	finally
    //   124	146	304	finally
    //   146	160	304	finally
    //   169	189	304	finally
    //   195	226	304	finally
    //   226	256	304	finally
    //   288	293	304	finally
    //   91	106	335	finally
    //   256	282	335	finally
    //   282	284	335	finally
    //   306	332	335	finally
    //   332	335	335	finally
    //   337	339	335	finally
  }
  
  public final ClientConnectionRequest requestConnection(final HttpRoute paramHttpRoute, final Object paramObject)
  {
    new ClientConnectionRequest()
    {
      public void abortRequest() {}
      
      public ManagedClientConnection getConnection(long paramAnonymousLong, TimeUnit paramAnonymousTimeUnit)
      {
        return BasicClientConnectionManager.this.getConnection(paramHttpRoute, paramObject);
      }
    };
  }
  
  /* Error */
  public void shutdown()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_1
    //   4: putfield 79	org/apache/http/impl/conn/BasicClientConnectionManager:shutdown	Z
    //   7: aload_0
    //   8: getfield 118	org/apache/http/impl/conn/BasicClientConnectionManager:poolEntry	Lorg/apache/http/impl/conn/HttpPoolEntry;
    //   11: ifnull +10 -> 21
    //   14: aload_0
    //   15: getfield 118	org/apache/http/impl/conn/BasicClientConnectionManager:poolEntry	Lorg/apache/http/impl/conn/HttpPoolEntry;
    //   18: invokevirtual 127	org/apache/http/impl/conn/HttpPoolEntry:close	()V
    //   21: aload_0
    //   22: aconst_null
    //   23: putfield 118	org/apache/http/impl/conn/BasicClientConnectionManager:poolEntry	Lorg/apache/http/impl/conn/HttpPoolEntry;
    //   26: aload_0
    //   27: aconst_null
    //   28: putfield 183	org/apache/http/impl/conn/BasicClientConnectionManager:conn	Lorg/apache/http/impl/conn/ManagedClientConnectionImpl;
    //   31: aload_0
    //   32: monitorexit
    //   33: return
    //   34: astore_2
    //   35: aload_0
    //   36: aconst_null
    //   37: putfield 118	org/apache/http/impl/conn/BasicClientConnectionManager:poolEntry	Lorg/apache/http/impl/conn/HttpPoolEntry;
    //   40: aload_0
    //   41: aconst_null
    //   42: putfield 183	org/apache/http/impl/conn/BasicClientConnectionManager:conn	Lorg/apache/http/impl/conn/ManagedClientConnectionImpl;
    //   45: aload_2
    //   46: athrow
    //   47: astore_1
    //   48: aload_0
    //   49: monitorexit
    //   50: aload_1
    //   51: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	52	0	this	BasicClientConnectionManager
    //   47	4	1	localObject1	Object
    //   34	12	2	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   7	21	34	finally
    //   2	7	47	finally
    //   21	33	47	finally
    //   35	47	47	finally
    //   48	50	47	finally
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.BasicClientConnectionManager
 * JD-Core Version:    0.7.0.1
 */