package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
@ThreadSafe
public class SingleClientConnManager
  implements ClientConnectionManager
{
  public static final String MISUSE_MESSAGE = "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
  protected final boolean alwaysShutDown;
  protected final ClientConnectionOperator connOperator;
  @GuardedBy("this")
  protected volatile long connectionExpiresTime;
  protected volatile boolean isShutDown;
  @GuardedBy("this")
  protected volatile long lastReleaseTime;
  private final Log log = LogFactory.getLog(getClass());
  @GuardedBy("this")
  protected volatile ConnAdapter managedConn;
  protected final SchemeRegistry schemeRegistry;
  @GuardedBy("this")
  protected volatile PoolEntry uniquePoolEntry;
  
  public SingleClientConnManager()
  {
    this(SchemeRegistryFactory.createDefault());
  }
  
  public SingleClientConnManager(SchemeRegistry paramSchemeRegistry)
  {
    Args.notNull(paramSchemeRegistry, "Scheme registry");
    this.schemeRegistry = paramSchemeRegistry;
    this.connOperator = createConnectionOperator(paramSchemeRegistry);
    this.uniquePoolEntry = new PoolEntry();
    this.managedConn = null;
    this.lastReleaseTime = -1L;
    this.alwaysShutDown = false;
    this.isShutDown = false;
  }
  
  @Deprecated
  public SingleClientConnManager(HttpParams paramHttpParams, SchemeRegistry paramSchemeRegistry)
  {
    this(paramSchemeRegistry);
  }
  
  protected final void assertStillUp()
    throws IllegalStateException
  {
    if (!this.isShutDown) {}
    for (boolean bool = true;; bool = false)
    {
      Asserts.check(bool, "Manager is shut down");
      return;
    }
  }
  
  public void closeExpiredConnections()
  {
    long l = this.connectionExpiresTime;
    if (System.currentTimeMillis() >= l) {
      closeIdleConnections(0L, TimeUnit.MILLISECONDS);
    }
  }
  
  /* Error */
  public void closeIdleConnections(long paramLong, TimeUnit paramTimeUnit)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 124	org/apache/http/impl/conn/SingleClientConnManager:assertStillUp	()V
    //   4: aload_3
    //   5: ldc 126
    //   7: invokestatic 64	org/apache/http/util/Args:notNull	(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   10: pop
    //   11: aload_0
    //   12: monitorenter
    //   13: aload_0
    //   14: getfield 81	org/apache/http/impl/conn/SingleClientConnManager:managedConn	Lorg/apache/http/impl/conn/SingleClientConnManager$ConnAdapter;
    //   17: ifnonnull +50 -> 67
    //   20: aload_0
    //   21: getfield 79	org/apache/http/impl/conn/SingleClientConnManager:uniquePoolEntry	Lorg/apache/http/impl/conn/SingleClientConnManager$PoolEntry;
    //   24: getfield 130	org/apache/http/impl/conn/SingleClientConnManager$PoolEntry:connection	Lorg/apache/http/conn/OperatedClientConnection;
    //   27: invokeinterface 136 1 0
    //   32: ifeq +35 -> 67
    //   35: invokestatic 110	java/lang/System:currentTimeMillis	()J
    //   38: aload_3
    //   39: lload_1
    //   40: invokevirtual 140	java/util/concurrent/TimeUnit:toMillis	(J)J
    //   43: lsub
    //   44: lstore 6
    //   46: aload_0
    //   47: getfield 85	org/apache/http/impl/conn/SingleClientConnManager:lastReleaseTime	J
    //   50: lstore 8
    //   52: lload 8
    //   54: lload 6
    //   56: lcmp
    //   57: ifgt +10 -> 67
    //   60: aload_0
    //   61: getfield 79	org/apache/http/impl/conn/SingleClientConnManager:uniquePoolEntry	Lorg/apache/http/impl/conn/SingleClientConnManager$PoolEntry;
    //   64: invokevirtual 143	org/apache/http/impl/conn/SingleClientConnManager$PoolEntry:close	()V
    //   67: aload_0
    //   68: monitorexit
    //   69: return
    //   70: astore 10
    //   72: aload_0
    //   73: getfield 56	org/apache/http/impl/conn/SingleClientConnManager:log	Lorg/apache/commons/logging/Log;
    //   76: ldc 145
    //   78: aload 10
    //   80: invokeinterface 151 3 0
    //   85: goto -18 -> 67
    //   88: astore 5
    //   90: aload_0
    //   91: monitorexit
    //   92: aload 5
    //   94: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	95	0	this	SingleClientConnManager
    //   0	95	1	paramLong	long
    //   0	95	3	paramTimeUnit	TimeUnit
    //   88	5	5	localObject	Object
    //   44	11	6	l1	long
    //   50	3	8	l2	long
    //   70	9	10	localIOException	IOException
    // Exception table:
    //   from	to	target	type
    //   60	67	70	java/io/IOException
    //   13	52	88	finally
    //   60	67	88	finally
    //   67	69	88	finally
    //   72	85	88	finally
    //   90	92	88	finally
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
  
  public ManagedClientConnection getConnection(HttpRoute paramHttpRoute, Object paramObject)
  {
    Args.notNull(paramHttpRoute, "Route");
    assertStillUp();
    if (this.log.isDebugEnabled()) {
      this.log.debug("Get connection for route " + paramHttpRoute);
    }
    for (;;)
    {
      try
      {
        if (this.managedConn == null)
        {
          bool1 = true;
          Asserts.check(bool1, "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.");
          i = 0;
          closeExpiredConnections();
          if (!this.uniquePoolEntry.connection.isOpen()) {
            break label225;
          }
          RouteTracker localRouteTracker = this.uniquePoolEntry.tracker;
          if (localRouteTracker != null)
          {
            boolean bool2 = localRouteTracker.toRoute().equals(paramHttpRoute);
            if (bool2) {
              break label216;
            }
          }
          j = 1;
          if (j != 0) {
            i = 1;
          }
          try
          {
            this.uniquePoolEntry.shutdown();
            if (i != 0) {
              this.uniquePoolEntry = new PoolEntry();
            }
            this.managedConn = new ConnAdapter(this.uniquePoolEntry, paramHttpRoute);
            ConnAdapter localConnAdapter = this.managedConn;
            return localConnAdapter;
          }
          catch (IOException localIOException)
          {
            this.log.debug("Problem shutting down connection.", localIOException);
            continue;
          }
        }
        boolean bool1 = false;
      }
      finally {}
      continue;
      label216:
      int i = 0;
      int j = 0;
      continue;
      label225:
      i = 1;
      j = 0;
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
    //   1: instanceof 209
    //   4: ldc 219
    //   6: invokestatic 220	org/apache/http/util/Args:check	(ZLjava/lang/String;)V
    //   9: aload_0
    //   10: invokevirtual 124	org/apache/http/impl/conn/SingleClientConnManager:assertStillUp	()V
    //   13: aload_0
    //   14: getfield 56	org/apache/http/impl/conn/SingleClientConnManager:log	Lorg/apache/commons/logging/Log;
    //   17: invokeinterface 169 1 0
    //   22: ifeq +31 -> 53
    //   25: aload_0
    //   26: getfield 56	org/apache/http/impl/conn/SingleClientConnManager:log	Lorg/apache/commons/logging/Log;
    //   29: new 171	java/lang/StringBuilder
    //   32: dup
    //   33: invokespecial 172	java/lang/StringBuilder:<init>	()V
    //   36: ldc 222
    //   38: invokevirtual 178	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   41: aload_1
    //   42: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   45: invokevirtual 185	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   48: invokeinterface 188 2 0
    //   53: aload_1
    //   54: checkcast 209	org/apache/http/impl/conn/SingleClientConnManager$ConnAdapter
    //   57: astore 5
    //   59: aload 5
    //   61: monitorenter
    //   62: aload 5
    //   64: getfield 226	org/apache/http/impl/conn/SingleClientConnManager$ConnAdapter:poolEntry	Lorg/apache/http/impl/conn/AbstractPoolEntry;
    //   67: ifnonnull +7 -> 74
    //   70: aload 5
    //   72: monitorexit
    //   73: return
    //   74: aload 5
    //   76: invokevirtual 230	org/apache/http/impl/conn/SingleClientConnManager$ConnAdapter:getManager	()Lorg/apache/http/conn/ClientConnectionManager;
    //   79: aload_0
    //   80: if_acmpne +118 -> 198
    //   83: iconst_1
    //   84: istore 7
    //   86: iload 7
    //   88: ldc 232
    //   90: invokestatic 101	org/apache/http/util/Asserts:check	(ZLjava/lang/String;)V
    //   93: aload 5
    //   95: invokevirtual 233	org/apache/http/impl/conn/SingleClientConnManager$ConnAdapter:isOpen	()Z
    //   98: ifeq +46 -> 144
    //   101: aload_0
    //   102: getfield 87	org/apache/http/impl/conn/SingleClientConnManager:alwaysShutDown	Z
    //   105: ifne +11 -> 116
    //   108: aload 5
    //   110: invokevirtual 236	org/apache/http/impl/conn/SingleClientConnManager$ConnAdapter:isMarkedReusable	()Z
    //   113: ifne +31 -> 144
    //   116: aload_0
    //   117: getfield 56	org/apache/http/impl/conn/SingleClientConnManager:log	Lorg/apache/commons/logging/Log;
    //   120: invokeinterface 169 1 0
    //   125: ifeq +14 -> 139
    //   128: aload_0
    //   129: getfield 56	org/apache/http/impl/conn/SingleClientConnManager:log	Lorg/apache/commons/logging/Log;
    //   132: ldc 238
    //   134: invokeinterface 188 2 0
    //   139: aload 5
    //   141: invokevirtual 239	org/apache/http/impl/conn/SingleClientConnManager$ConnAdapter:shutdown	()V
    //   144: aload 5
    //   146: invokevirtual 242	org/apache/http/impl/conn/SingleClientConnManager$ConnAdapter:detach	()V
    //   149: aload_0
    //   150: monitorenter
    //   151: aload_0
    //   152: aconst_null
    //   153: putfield 81	org/apache/http/impl/conn/SingleClientConnManager:managedConn	Lorg/apache/http/impl/conn/SingleClientConnManager$ConnAdapter;
    //   156: aload_0
    //   157: invokestatic 110	java/lang/System:currentTimeMillis	()J
    //   160: putfield 85	org/apache/http/impl/conn/SingleClientConnManager:lastReleaseTime	J
    //   163: lload_2
    //   164: lconst_0
    //   165: lcmp
    //   166: ifle +38 -> 204
    //   169: aload_0
    //   170: aload 4
    //   172: lload_2
    //   173: invokevirtual 140	java/util/concurrent/TimeUnit:toMillis	(J)J
    //   176: aload_0
    //   177: getfield 85	org/apache/http/impl/conn/SingleClientConnManager:lastReleaseTime	J
    //   180: ladd
    //   181: putfield 104	org/apache/http/impl/conn/SingleClientConnManager:connectionExpiresTime	J
    //   184: aload_0
    //   185: monitorexit
    //   186: aload 5
    //   188: monitorexit
    //   189: return
    //   190: astore 6
    //   192: aload 5
    //   194: monitorexit
    //   195: aload 6
    //   197: athrow
    //   198: iconst_0
    //   199: istore 7
    //   201: goto -115 -> 86
    //   204: aload_0
    //   205: ldc2_w 243
    //   208: putfield 104	org/apache/http/impl/conn/SingleClientConnManager:connectionExpiresTime	J
    //   211: goto -27 -> 184
    //   214: astore 12
    //   216: aload_0
    //   217: monitorexit
    //   218: aload 12
    //   220: athrow
    //   221: astore 10
    //   223: aload_0
    //   224: getfield 56	org/apache/http/impl/conn/SingleClientConnManager:log	Lorg/apache/commons/logging/Log;
    //   227: invokeinterface 169 1 0
    //   232: ifeq +16 -> 248
    //   235: aload_0
    //   236: getfield 56	org/apache/http/impl/conn/SingleClientConnManager:log	Lorg/apache/commons/logging/Log;
    //   239: ldc 246
    //   241: aload 10
    //   243: invokeinterface 151 3 0
    //   248: aload 5
    //   250: invokevirtual 242	org/apache/http/impl/conn/SingleClientConnManager$ConnAdapter:detach	()V
    //   253: aload_0
    //   254: monitorenter
    //   255: aload_0
    //   256: aconst_null
    //   257: putfield 81	org/apache/http/impl/conn/SingleClientConnManager:managedConn	Lorg/apache/http/impl/conn/SingleClientConnManager$ConnAdapter;
    //   260: aload_0
    //   261: invokestatic 110	java/lang/System:currentTimeMillis	()J
    //   264: putfield 85	org/apache/http/impl/conn/SingleClientConnManager:lastReleaseTime	J
    //   267: lload_2
    //   268: lconst_0
    //   269: lcmp
    //   270: ifle +30 -> 300
    //   273: aload_0
    //   274: aload 4
    //   276: lload_2
    //   277: invokevirtual 140	java/util/concurrent/TimeUnit:toMillis	(J)J
    //   280: aload_0
    //   281: getfield 85	org/apache/http/impl/conn/SingleClientConnManager:lastReleaseTime	J
    //   284: ladd
    //   285: putfield 104	org/apache/http/impl/conn/SingleClientConnManager:connectionExpiresTime	J
    //   288: aload_0
    //   289: monitorexit
    //   290: goto -104 -> 186
    //   293: astore 11
    //   295: aload_0
    //   296: monitorexit
    //   297: aload 11
    //   299: athrow
    //   300: aload_0
    //   301: ldc2_w 243
    //   304: putfield 104	org/apache/http/impl/conn/SingleClientConnManager:connectionExpiresTime	J
    //   307: goto -19 -> 288
    //   310: astore 8
    //   312: aload 5
    //   314: invokevirtual 242	org/apache/http/impl/conn/SingleClientConnManager$ConnAdapter:detach	()V
    //   317: aload_0
    //   318: monitorenter
    //   319: aload_0
    //   320: aconst_null
    //   321: putfield 81	org/apache/http/impl/conn/SingleClientConnManager:managedConn	Lorg/apache/http/impl/conn/SingleClientConnManager$ConnAdapter;
    //   324: aload_0
    //   325: invokestatic 110	java/lang/System:currentTimeMillis	()J
    //   328: putfield 85	org/apache/http/impl/conn/SingleClientConnManager:lastReleaseTime	J
    //   331: lload_2
    //   332: lconst_0
    //   333: lcmp
    //   334: ifle +23 -> 357
    //   337: aload_0
    //   338: aload 4
    //   340: lload_2
    //   341: invokevirtual 140	java/util/concurrent/TimeUnit:toMillis	(J)J
    //   344: aload_0
    //   345: getfield 85	org/apache/http/impl/conn/SingleClientConnManager:lastReleaseTime	J
    //   348: ladd
    //   349: putfield 104	org/apache/http/impl/conn/SingleClientConnManager:connectionExpiresTime	J
    //   352: aload_0
    //   353: monitorexit
    //   354: aload 8
    //   356: athrow
    //   357: aload_0
    //   358: ldc2_w 243
    //   361: putfield 104	org/apache/http/impl/conn/SingleClientConnManager:connectionExpiresTime	J
    //   364: goto -12 -> 352
    //   367: astore 9
    //   369: aload_0
    //   370: monitorexit
    //   371: aload 9
    //   373: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	374	0	this	SingleClientConnManager
    //   0	374	1	paramManagedClientConnection	ManagedClientConnection
    //   0	374	2	paramLong	long
    //   0	374	4	paramTimeUnit	TimeUnit
    //   57	256	5	localConnAdapter	ConnAdapter
    //   190	6	6	localObject1	Object
    //   84	116	7	bool	boolean
    //   310	45	8	localObject2	Object
    //   367	5	9	localObject3	Object
    //   221	21	10	localIOException	IOException
    //   293	5	11	localObject4	Object
    //   214	5	12	localObject5	Object
    // Exception table:
    //   from	to	target	type
    //   62	73	190	finally
    //   74	83	190	finally
    //   86	93	190	finally
    //   144	151	190	finally
    //   186	189	190	finally
    //   192	195	190	finally
    //   218	221	190	finally
    //   248	255	190	finally
    //   297	300	190	finally
    //   312	319	190	finally
    //   354	357	190	finally
    //   371	374	190	finally
    //   151	163	214	finally
    //   169	184	214	finally
    //   184	186	214	finally
    //   204	211	214	finally
    //   216	218	214	finally
    //   93	116	221	java/io/IOException
    //   116	139	221	java/io/IOException
    //   139	144	221	java/io/IOException
    //   255	267	293	finally
    //   273	288	293	finally
    //   288	290	293	finally
    //   295	297	293	finally
    //   300	307	293	finally
    //   93	116	310	finally
    //   116	139	310	finally
    //   139	144	310	finally
    //   223	248	310	finally
    //   319	331	367	finally
    //   337	352	367	finally
    //   352	354	367	finally
    //   357	364	367	finally
    //   369	371	367	finally
  }
  
  public final ClientConnectionRequest requestConnection(final HttpRoute paramHttpRoute, final Object paramObject)
  {
    new ClientConnectionRequest()
    {
      public void abortRequest() {}
      
      public ManagedClientConnection getConnection(long paramAnonymousLong, TimeUnit paramAnonymousTimeUnit)
      {
        return SingleClientConnManager.this.getConnection(paramHttpRoute, paramObject);
      }
    };
  }
  
  /* Error */
  protected void revokeConnection()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 81	org/apache/http/impl/conn/SingleClientConnManager:managedConn	Lorg/apache/http/impl/conn/SingleClientConnManager$ConnAdapter;
    //   4: astore_1
    //   5: aload_1
    //   6: ifnonnull +4 -> 10
    //   9: return
    //   10: aload_1
    //   11: invokevirtual 242	org/apache/http/impl/conn/SingleClientConnManager$ConnAdapter:detach	()V
    //   14: aload_0
    //   15: monitorenter
    //   16: aload_0
    //   17: getfield 79	org/apache/http/impl/conn/SingleClientConnManager:uniquePoolEntry	Lorg/apache/http/impl/conn/SingleClientConnManager$PoolEntry;
    //   20: invokevirtual 207	org/apache/http/impl/conn/SingleClientConnManager$PoolEntry:shutdown	()V
    //   23: aload_0
    //   24: monitorexit
    //   25: return
    //   26: astore_3
    //   27: aload_0
    //   28: monitorexit
    //   29: aload_3
    //   30: athrow
    //   31: astore_2
    //   32: aload_0
    //   33: getfield 56	org/apache/http/impl/conn/SingleClientConnManager:log	Lorg/apache/commons/logging/Log;
    //   36: ldc_w 256
    //   39: aload_2
    //   40: invokeinterface 151 3 0
    //   45: goto -22 -> 23
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	48	0	this	SingleClientConnManager
    //   4	7	1	localConnAdapter	ConnAdapter
    //   31	9	2	localIOException	IOException
    //   26	4	3	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   16	23	26	finally
    //   23	25	26	finally
    //   27	29	26	finally
    //   32	45	26	finally
    //   16	23	31	java/io/IOException
  }
  
  /* Error */
  public void shutdown()
  {
    // Byte code:
    //   0: aload_0
    //   1: iconst_1
    //   2: putfield 89	org/apache/http/impl/conn/SingleClientConnManager:isShutDown	Z
    //   5: aload_0
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 79	org/apache/http/impl/conn/SingleClientConnManager:uniquePoolEntry	Lorg/apache/http/impl/conn/SingleClientConnManager$PoolEntry;
    //   11: ifnull +10 -> 21
    //   14: aload_0
    //   15: getfield 79	org/apache/http/impl/conn/SingleClientConnManager:uniquePoolEntry	Lorg/apache/http/impl/conn/SingleClientConnManager$PoolEntry;
    //   18: invokevirtual 207	org/apache/http/impl/conn/SingleClientConnManager$PoolEntry:shutdown	()V
    //   21: aload_0
    //   22: aconst_null
    //   23: putfield 79	org/apache/http/impl/conn/SingleClientConnManager:uniquePoolEntry	Lorg/apache/http/impl/conn/SingleClientConnManager$PoolEntry;
    //   26: aload_0
    //   27: aconst_null
    //   28: putfield 81	org/apache/http/impl/conn/SingleClientConnManager:managedConn	Lorg/apache/http/impl/conn/SingleClientConnManager$ConnAdapter;
    //   31: aload_0
    //   32: monitorexit
    //   33: return
    //   34: astore_3
    //   35: aload_0
    //   36: getfield 56	org/apache/http/impl/conn/SingleClientConnManager:log	Lorg/apache/commons/logging/Log;
    //   39: ldc_w 258
    //   42: aload_3
    //   43: invokeinterface 151 3 0
    //   48: aload_0
    //   49: aconst_null
    //   50: putfield 79	org/apache/http/impl/conn/SingleClientConnManager:uniquePoolEntry	Lorg/apache/http/impl/conn/SingleClientConnManager$PoolEntry;
    //   53: aload_0
    //   54: aconst_null
    //   55: putfield 81	org/apache/http/impl/conn/SingleClientConnManager:managedConn	Lorg/apache/http/impl/conn/SingleClientConnManager$ConnAdapter;
    //   58: goto -27 -> 31
    //   61: astore_2
    //   62: aload_0
    //   63: monitorexit
    //   64: aload_2
    //   65: athrow
    //   66: astore_1
    //   67: aload_0
    //   68: aconst_null
    //   69: putfield 79	org/apache/http/impl/conn/SingleClientConnManager:uniquePoolEntry	Lorg/apache/http/impl/conn/SingleClientConnManager$PoolEntry;
    //   72: aload_0
    //   73: aconst_null
    //   74: putfield 81	org/apache/http/impl/conn/SingleClientConnManager:managedConn	Lorg/apache/http/impl/conn/SingleClientConnManager$ConnAdapter;
    //   77: aload_1
    //   78: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	79	0	this	SingleClientConnManager
    //   66	12	1	localObject1	Object
    //   61	4	2	localObject2	Object
    //   34	9	3	localIOException	IOException
    // Exception table:
    //   from	to	target	type
    //   7	21	34	java/io/IOException
    //   21	31	61	finally
    //   31	33	61	finally
    //   48	58	61	finally
    //   62	64	61	finally
    //   67	79	61	finally
    //   7	21	66	finally
    //   35	48	66	finally
  }
  
  protected class ConnAdapter
    extends AbstractPooledConnAdapter
  {
    protected ConnAdapter(SingleClientConnManager.PoolEntry paramPoolEntry, HttpRoute paramHttpRoute)
    {
      super(paramPoolEntry);
      markReusable();
      paramPoolEntry.route = paramHttpRoute;
    }
  }
  
  protected class PoolEntry
    extends AbstractPoolEntry
  {
    protected PoolEntry()
    {
      super(null);
    }
    
    protected void close()
      throws IOException
    {
      shutdownEntry();
      if (this.connection.isOpen()) {
        this.connection.close();
      }
    }
    
    protected void shutdown()
      throws IOException
    {
      shutdownEntry();
      if (this.connection.isOpen()) {
        this.connection.shutdown();
      }
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.SingleClientConnManager
 * JD-Core Version:    0.7.0.1
 */