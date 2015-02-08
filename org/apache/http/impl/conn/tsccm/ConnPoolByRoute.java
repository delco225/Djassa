package org.apache.http.impl.conn.tsccm;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
public class ConnPoolByRoute
  extends AbstractConnPool
{
  protected final ConnPerRoute connPerRoute;
  private final long connTTL;
  private final TimeUnit connTTLTimeUnit;
  protected final Queue<BasicPoolEntry> freeConnections;
  protected final Set<BasicPoolEntry> leasedConnections;
  private final Log log = LogFactory.getLog(getClass());
  protected volatile int maxTotalConnections;
  protected volatile int numConnections;
  protected final ClientConnectionOperator operator;
  private final Lock poolLock;
  protected final Map<HttpRoute, RouteSpecificPool> routeToPool;
  protected volatile boolean shutdown;
  protected final Queue<WaitingThread> waitingThreads;
  
  public ConnPoolByRoute(ClientConnectionOperator paramClientConnectionOperator, ConnPerRoute paramConnPerRoute, int paramInt)
  {
    this(paramClientConnectionOperator, paramConnPerRoute, paramInt, -1L, TimeUnit.MILLISECONDS);
  }
  
  public ConnPoolByRoute(ClientConnectionOperator paramClientConnectionOperator, ConnPerRoute paramConnPerRoute, int paramInt, long paramLong, TimeUnit paramTimeUnit)
  {
    Args.notNull(paramClientConnectionOperator, "Connection operator");
    Args.notNull(paramConnPerRoute, "Connections per route");
    this.poolLock = this.poolLock;
    this.leasedConnections = this.leasedConnections;
    this.operator = paramClientConnectionOperator;
    this.connPerRoute = paramConnPerRoute;
    this.maxTotalConnections = paramInt;
    this.freeConnections = createFreeConnQueue();
    this.waitingThreads = createWaitingThreadQueue();
    this.routeToPool = createRouteToPoolMap();
    this.connTTL = paramLong;
    this.connTTLTimeUnit = paramTimeUnit;
  }
  
  @Deprecated
  public ConnPoolByRoute(ClientConnectionOperator paramClientConnectionOperator, HttpParams paramHttpParams)
  {
    this(paramClientConnectionOperator, ConnManagerParams.getMaxConnectionsPerRoute(paramHttpParams), ConnManagerParams.getMaxTotalConnections(paramHttpParams));
  }
  
  private void closeConnection(BasicPoolEntry paramBasicPoolEntry)
  {
    OperatedClientConnection localOperatedClientConnection = paramBasicPoolEntry.getConnection();
    if (localOperatedClientConnection != null) {}
    try
    {
      localOperatedClientConnection.close();
      return;
    }
    catch (IOException localIOException)
    {
      this.log.debug("I/O error closing connection", localIOException);
    }
  }
  
  public void closeExpiredConnections()
  {
    this.log.debug("Closing expired connections");
    long l = System.currentTimeMillis();
    this.poolLock.lock();
    try
    {
      Iterator localIterator = this.freeConnections.iterator();
      while (localIterator.hasNext())
      {
        BasicPoolEntry localBasicPoolEntry = (BasicPoolEntry)localIterator.next();
        if (localBasicPoolEntry.isExpired(l))
        {
          if (this.log.isDebugEnabled()) {
            this.log.debug("Closing connection expired @ " + new Date(localBasicPoolEntry.getExpiry()));
          }
          localIterator.remove();
          deleteEntry(localBasicPoolEntry);
        }
      }
    }
    finally
    {
      this.poolLock.unlock();
    }
  }
  
  public void closeIdleConnections(long paramLong, TimeUnit paramTimeUnit)
  {
    long l1 = 0L;
    Args.notNull(paramTimeUnit, "Time unit");
    if (paramLong > l1) {
      l1 = paramLong;
    }
    if (this.log.isDebugEnabled()) {
      this.log.debug("Closing connections idle longer than " + l1 + " " + paramTimeUnit);
    }
    long l2 = System.currentTimeMillis() - paramTimeUnit.toMillis(l1);
    this.poolLock.lock();
    try
    {
      Iterator localIterator = this.freeConnections.iterator();
      while (localIterator.hasNext())
      {
        BasicPoolEntry localBasicPoolEntry = (BasicPoolEntry)localIterator.next();
        if (localBasicPoolEntry.getUpdated() <= l2)
        {
          if (this.log.isDebugEnabled()) {
            this.log.debug("Closing connection last used @ " + new Date(localBasicPoolEntry.getUpdated()));
          }
          localIterator.remove();
          deleteEntry(localBasicPoolEntry);
        }
      }
    }
    finally
    {
      this.poolLock.unlock();
    }
  }
  
  protected BasicPoolEntry createEntry(RouteSpecificPool paramRouteSpecificPool, ClientConnectionOperator paramClientConnectionOperator)
  {
    if (this.log.isDebugEnabled()) {
      this.log.debug("Creating new connection [" + paramRouteSpecificPool.getRoute() + "]");
    }
    BasicPoolEntry localBasicPoolEntry = new BasicPoolEntry(paramClientConnectionOperator, paramRouteSpecificPool.getRoute(), this.connTTL, this.connTTLTimeUnit);
    this.poolLock.lock();
    try
    {
      paramRouteSpecificPool.createdEntry(localBasicPoolEntry);
      this.numConnections = (1 + this.numConnections);
      this.leasedConnections.add(localBasicPoolEntry);
      return localBasicPoolEntry;
    }
    finally
    {
      this.poolLock.unlock();
    }
  }
  
  protected Queue<BasicPoolEntry> createFreeConnQueue()
  {
    return new LinkedList();
  }
  
  protected Map<HttpRoute, RouteSpecificPool> createRouteToPoolMap()
  {
    return new HashMap();
  }
  
  protected Queue<WaitingThread> createWaitingThreadQueue()
  {
    return new LinkedList();
  }
  
  public void deleteClosedConnections()
  {
    this.poolLock.lock();
    try
    {
      Iterator localIterator = this.freeConnections.iterator();
      while (localIterator.hasNext())
      {
        BasicPoolEntry localBasicPoolEntry = (BasicPoolEntry)localIterator.next();
        if (!localBasicPoolEntry.getConnection().isOpen())
        {
          localIterator.remove();
          deleteEntry(localBasicPoolEntry);
        }
      }
    }
    finally
    {
      this.poolLock.unlock();
    }
  }
  
  protected void deleteEntry(BasicPoolEntry paramBasicPoolEntry)
  {
    HttpRoute localHttpRoute = paramBasicPoolEntry.getPlannedRoute();
    if (this.log.isDebugEnabled()) {
      this.log.debug("Deleting connection [" + localHttpRoute + "][" + paramBasicPoolEntry.getState() + "]");
    }
    this.poolLock.lock();
    try
    {
      closeConnection(paramBasicPoolEntry);
      RouteSpecificPool localRouteSpecificPool = getRoutePool(localHttpRoute, true);
      localRouteSpecificPool.deleteEntry(paramBasicPoolEntry);
      this.numConnections = (-1 + this.numConnections);
      if (localRouteSpecificPool.isUnused()) {
        this.routeToPool.remove(localHttpRoute);
      }
      return;
    }
    finally
    {
      this.poolLock.unlock();
    }
  }
  
  /* Error */
  protected void deleteLeastUsedEntry()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 75	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:poolLock	Ljava/util/concurrent/locks/Lock;
    //   4: invokeinterface 160 1 0
    //   9: aload_0
    //   10: getfield 90	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:freeConnections	Ljava/util/Queue;
    //   13: invokeinterface 302 1 0
    //   18: checkcast 126	org/apache/http/impl/conn/tsccm/BasicPoolEntry
    //   21: astore_2
    //   22: aload_2
    //   23: ifnull +18 -> 41
    //   26: aload_0
    //   27: aload_2
    //   28: invokevirtual 213	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:deleteEntry	(Lorg/apache/http/impl/conn/tsccm/BasicPoolEntry;)V
    //   31: aload_0
    //   32: getfield 75	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:poolLock	Ljava/util/concurrent/locks/Lock;
    //   35: invokeinterface 216 1 0
    //   40: return
    //   41: aload_0
    //   42: getfield 62	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:log	Lorg/apache/commons/logging/Log;
    //   45: invokeinterface 183 1 0
    //   50: ifeq -19 -> 31
    //   53: aload_0
    //   54: getfield 62	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:log	Lorg/apache/commons/logging/Log;
    //   57: ldc_w 304
    //   60: invokeinterface 149 2 0
    //   65: goto -34 -> 31
    //   68: astore_1
    //   69: aload_0
    //   70: getfield 75	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:poolLock	Ljava/util/concurrent/locks/Lock;
    //   73: invokeinterface 216 1 0
    //   78: aload_1
    //   79: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	80	0	this	ConnPoolByRoute
    //   68	11	1	localObject	Object
    //   21	7	2	localBasicPoolEntry	BasicPoolEntry
    // Exception table:
    //   from	to	target	type
    //   9	22	68	finally
    //   26	31	68	finally
    //   41	65	68	finally
  }
  
  /* Error */
  public void freeEntry(BasicPoolEntry paramBasicPoolEntry, boolean paramBoolean, long paramLong, TimeUnit paramTimeUnit)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual 275	org/apache/http/impl/conn/tsccm/BasicPoolEntry:getPlannedRoute	()Lorg/apache/http/conn/routing/HttpRoute;
    //   4: astore 6
    //   6: aload_0
    //   7: getfield 62	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:log	Lorg/apache/commons/logging/Log;
    //   10: invokeinterface 183 1 0
    //   15: ifeq +51 -> 66
    //   18: aload_0
    //   19: getfield 62	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:log	Lorg/apache/commons/logging/Log;
    //   22: new 185	java/lang/StringBuilder
    //   25: dup
    //   26: invokespecial 186	java/lang/StringBuilder:<init>	()V
    //   29: ldc_w 308
    //   32: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   35: aload 6
    //   37: invokevirtual 203	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   40: ldc_w 279
    //   43: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   46: aload_1
    //   47: invokevirtual 282	org/apache/http/impl/conn/tsccm/BasicPoolEntry:getState	()Ljava/lang/Object;
    //   50: invokevirtual 203	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   53: ldc 248
    //   55: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   58: invokevirtual 207	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   61: invokeinterface 149 2 0
    //   66: aload_0
    //   67: getfield 75	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:poolLock	Ljava/util/concurrent/locks/Lock;
    //   70: invokeinterface 160 1 0
    //   75: aload_0
    //   76: getfield 310	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:shutdown	Z
    //   79: ifeq +18 -> 97
    //   82: aload_0
    //   83: aload_1
    //   84: invokespecial 284	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:closeConnection	(Lorg/apache/http/impl/conn/tsccm/BasicPoolEntry;)V
    //   87: aload_0
    //   88: getfield 75	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:poolLock	Ljava/util/concurrent/locks/Lock;
    //   91: invokeinterface 216 1 0
    //   96: return
    //   97: aload_0
    //   98: getfield 78	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:leasedConnections	Ljava/util/Set;
    //   101: aload_1
    //   102: invokeinterface 312 2 0
    //   107: pop
    //   108: aload_0
    //   109: aload 6
    //   111: iconst_1
    //   112: invokevirtual 288	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:getRoutePool	(Lorg/apache/http/conn/routing/HttpRoute;Z)Lorg/apache/http/impl/conn/tsccm/RouteSpecificPool;
    //   115: astore 9
    //   117: iload_2
    //   118: ifeq +163 -> 281
    //   121: aload 9
    //   123: invokevirtual 316	org/apache/http/impl/conn/tsccm/RouteSpecificPool:getCapacity	()I
    //   126: iflt +155 -> 281
    //   129: aload_0
    //   130: getfield 62	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:log	Lorg/apache/commons/logging/Log;
    //   133: invokeinterface 183 1 0
    //   138: ifeq +95 -> 233
    //   141: lload_3
    //   142: lconst_0
    //   143: lcmp
    //   144: ifle +129 -> 273
    //   147: new 185	java/lang/StringBuilder
    //   150: dup
    //   151: invokespecial 186	java/lang/StringBuilder:<init>	()V
    //   154: ldc_w 318
    //   157: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   160: lload_3
    //   161: invokevirtual 225	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   164: ldc 227
    //   166: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   169: aload 5
    //   171: invokevirtual 203	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   174: invokevirtual 207	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   177: astore 10
    //   179: aload_0
    //   180: getfield 62	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:log	Lorg/apache/commons/logging/Log;
    //   183: new 185	java/lang/StringBuilder
    //   186: dup
    //   187: invokespecial 186	java/lang/StringBuilder:<init>	()V
    //   190: ldc_w 320
    //   193: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   196: aload 6
    //   198: invokevirtual 203	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   201: ldc_w 279
    //   204: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   207: aload_1
    //   208: invokevirtual 282	org/apache/http/impl/conn/tsccm/BasicPoolEntry:getState	()Ljava/lang/Object;
    //   211: invokevirtual 203	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   214: ldc_w 322
    //   217: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   220: aload 10
    //   222: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   225: invokevirtual 207	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   228: invokeinterface 149 2 0
    //   233: aload 9
    //   235: aload_1
    //   236: invokevirtual 324	org/apache/http/impl/conn/tsccm/RouteSpecificPool:freeEntry	(Lorg/apache/http/impl/conn/tsccm/BasicPoolEntry;)V
    //   239: aload_1
    //   240: lload_3
    //   241: aload 5
    //   243: invokevirtual 327	org/apache/http/impl/conn/tsccm/BasicPoolEntry:updateExpiry	(JLjava/util/concurrent/TimeUnit;)V
    //   246: aload_0
    //   247: getfield 90	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:freeConnections	Ljava/util/Queue;
    //   250: aload_1
    //   251: invokeinterface 328 2 0
    //   256: pop
    //   257: aload_0
    //   258: aload 9
    //   260: invokevirtual 332	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:notifyWaitingThread	(Lorg/apache/http/impl/conn/tsccm/RouteSpecificPool;)V
    //   263: aload_0
    //   264: getfield 75	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:poolLock	Ljava/util/concurrent/locks/Lock;
    //   267: invokeinterface 216 1 0
    //   272: return
    //   273: ldc_w 334
    //   276: astore 10
    //   278: goto -99 -> 179
    //   281: aload_0
    //   282: aload_1
    //   283: invokespecial 284	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:closeConnection	(Lorg/apache/http/impl/conn/tsccm/BasicPoolEntry;)V
    //   286: aload 9
    //   288: invokevirtual 337	org/apache/http/impl/conn/tsccm/RouteSpecificPool:dropEntry	()V
    //   291: aload_0
    //   292: iconst_m1
    //   293: aload_0
    //   294: getfield 256	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:numConnections	I
    //   297: iadd
    //   298: putfield 256	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:numConnections	I
    //   301: goto -44 -> 257
    //   304: astore 7
    //   306: aload_0
    //   307: getfield 75	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:poolLock	Ljava/util/concurrent/locks/Lock;
    //   310: invokeinterface 216 1 0
    //   315: aload 7
    //   317: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	318	0	this	ConnPoolByRoute
    //   0	318	1	paramBasicPoolEntry	BasicPoolEntry
    //   0	318	2	paramBoolean	boolean
    //   0	318	3	paramLong	long
    //   0	318	5	paramTimeUnit	TimeUnit
    //   4	193	6	localHttpRoute	HttpRoute
    //   304	12	7	localObject	Object
    //   115	172	9	localRouteSpecificPool	RouteSpecificPool
    //   177	100	10	str	java.lang.String
    // Exception table:
    //   from	to	target	type
    //   75	87	304	finally
    //   97	117	304	finally
    //   121	141	304	finally
    //   147	179	304	finally
    //   179	233	304	finally
    //   233	257	304	finally
    //   257	263	304	finally
    //   281	301	304	finally
  }
  
  public int getConnectionsInPool()
  {
    this.poolLock.lock();
    try
    {
      int i = this.numConnections;
      return i;
    }
    finally
    {
      this.poolLock.unlock();
    }
  }
  
  public int getConnectionsInPool(HttpRoute paramHttpRoute)
  {
    this.poolLock.lock();
    try
    {
      RouteSpecificPool localRouteSpecificPool = getRoutePool(paramHttpRoute, false);
      int i = 0;
      if (localRouteSpecificPool != null)
      {
        int j = localRouteSpecificPool.getEntryCount();
        i = j;
      }
      return i;
    }
    finally
    {
      this.poolLock.unlock();
    }
  }
  
  /* Error */
  protected BasicPoolEntry getEntryBlocking(HttpRoute paramHttpRoute, Object paramObject, long paramLong, TimeUnit paramTimeUnit, WaitingThreadAborter paramWaitingThreadAborter)
    throws ConnectionPoolTimeoutException, InterruptedException
  {
    // Byte code:
    //   0: lload_3
    //   1: lconst_0
    //   2: lcmp
    //   3: istore 7
    //   5: aconst_null
    //   6: astore 8
    //   8: iload 7
    //   10: ifle +22 -> 32
    //   13: new 194	java/util/Date
    //   16: dup
    //   17: invokestatic 155	java/lang/System:currentTimeMillis	()J
    //   20: aload 5
    //   22: lload_3
    //   23: invokevirtual 231	java/util/concurrent/TimeUnit:toMillis	(J)J
    //   26: ladd
    //   27: invokespecial 200	java/util/Date:<init>	(J)V
    //   30: astore 8
    //   32: aconst_null
    //   33: astore 9
    //   35: aload_0
    //   36: getfield 75	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:poolLock	Ljava/util/concurrent/locks/Lock;
    //   39: invokeinterface 160 1 0
    //   44: aload_0
    //   45: aload_1
    //   46: iconst_1
    //   47: invokevirtual 288	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:getRoutePool	(Lorg/apache/http/conn/routing/HttpRoute;Z)Lorg/apache/http/impl/conn/tsccm/RouteSpecificPool;
    //   50: astore 11
    //   52: aconst_null
    //   53: astore 12
    //   55: aload 9
    //   57: ifnonnull +142 -> 199
    //   60: aload_0
    //   61: getfield 310	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:shutdown	Z
    //   64: ifne +147 -> 211
    //   67: iconst_1
    //   68: istore 13
    //   70: iload 13
    //   72: ldc_w 350
    //   75: invokestatic 356	org/apache/http/util/Asserts:check	(ZLjava/lang/String;)V
    //   78: aload_0
    //   79: getfield 62	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:log	Lorg/apache/commons/logging/Log;
    //   82: invokeinterface 183 1 0
    //   87: ifeq +94 -> 181
    //   90: aload_0
    //   91: getfield 62	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:log	Lorg/apache/commons/logging/Log;
    //   94: new 185	java/lang/StringBuilder
    //   97: dup
    //   98: invokespecial 186	java/lang/StringBuilder:<init>	()V
    //   101: ldc_w 358
    //   104: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   107: aload_1
    //   108: invokevirtual 203	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   111: ldc_w 360
    //   114: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   117: aload_0
    //   118: getfield 90	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:freeConnections	Ljava/util/Queue;
    //   121: invokeinterface 363 1 0
    //   126: invokevirtual 366	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   129: ldc_w 368
    //   132: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   135: aload_0
    //   136: getfield 78	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:leasedConnections	Ljava/util/Set;
    //   139: invokeinterface 369 1 0
    //   144: invokevirtual 366	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   147: ldc_w 371
    //   150: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   153: aload_0
    //   154: getfield 256	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:numConnections	I
    //   157: invokevirtual 366	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   160: ldc_w 373
    //   163: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   166: aload_0
    //   167: getfield 84	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:maxTotalConnections	I
    //   170: invokevirtual 366	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   173: invokevirtual 207	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   176: invokeinterface 149 2 0
    //   181: aload_0
    //   182: aload 11
    //   184: aload_2
    //   185: invokevirtual 377	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:getFreeEntry	(Lorg/apache/http/impl/conn/tsccm/RouteSpecificPool;Ljava/lang/Object;)Lorg/apache/http/impl/conn/tsccm/BasicPoolEntry;
    //   188: astore 14
    //   190: aload 14
    //   192: astore 9
    //   194: aload 9
    //   196: ifnull +21 -> 217
    //   199: aload_0
    //   200: getfield 75	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:poolLock	Ljava/util/concurrent/locks/Lock;
    //   203: invokeinterface 216 1 0
    //   208: aload 9
    //   210: areturn
    //   211: iconst_0
    //   212: istore 13
    //   214: goto -144 -> 70
    //   217: aload 11
    //   219: invokevirtual 316	org/apache/http/impl/conn/tsccm/RouteSpecificPool:getCapacity	()I
    //   222: ifle +368 -> 590
    //   225: iconst_1
    //   226: istore 15
    //   228: aload_0
    //   229: getfield 62	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:log	Lorg/apache/commons/logging/Log;
    //   232: invokeinterface 183 1 0
    //   237: ifeq +75 -> 312
    //   240: aload_0
    //   241: getfield 62	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:log	Lorg/apache/commons/logging/Log;
    //   244: new 185	java/lang/StringBuilder
    //   247: dup
    //   248: invokespecial 186	java/lang/StringBuilder:<init>	()V
    //   251: ldc_w 379
    //   254: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   257: aload 11
    //   259: invokevirtual 316	org/apache/http/impl/conn/tsccm/RouteSpecificPool:getCapacity	()I
    //   262: invokevirtual 366	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   265: ldc_w 373
    //   268: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   271: aload 11
    //   273: invokevirtual 382	org/apache/http/impl/conn/tsccm/RouteSpecificPool:getMaxEntries	()I
    //   276: invokevirtual 366	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   279: ldc_w 384
    //   282: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   285: aload_1
    //   286: invokevirtual 203	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   289: ldc_w 279
    //   292: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   295: aload_2
    //   296: invokevirtual 203	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   299: ldc 248
    //   301: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   304: invokevirtual 207	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   307: invokeinterface 149 2 0
    //   312: iload 15
    //   314: ifeq +29 -> 343
    //   317: aload_0
    //   318: getfield 256	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:numConnections	I
    //   321: aload_0
    //   322: getfield 84	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:maxTotalConnections	I
    //   325: if_icmpge +18 -> 343
    //   328: aload_0
    //   329: aload 11
    //   331: aload_0
    //   332: getfield 80	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:operator	Lorg/apache/http/conn/ClientConnectionOperator;
    //   335: invokevirtual 386	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:createEntry	(Lorg/apache/http/impl/conn/tsccm/RouteSpecificPool;Lorg/apache/http/conn/ClientConnectionOperator;)Lorg/apache/http/impl/conn/tsccm/BasicPoolEntry;
    //   338: astore 9
    //   340: goto -285 -> 55
    //   343: iload 15
    //   345: ifeq +42 -> 387
    //   348: aload_0
    //   349: getfield 90	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:freeConnections	Ljava/util/Queue;
    //   352: invokeinterface 389 1 0
    //   357: ifne +30 -> 387
    //   360: aload_0
    //   361: invokevirtual 391	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:deleteLeastUsedEntry	()V
    //   364: aload_0
    //   365: aload_1
    //   366: iconst_1
    //   367: invokevirtual 288	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:getRoutePool	(Lorg/apache/http/conn/routing/HttpRoute;Z)Lorg/apache/http/impl/conn/tsccm/RouteSpecificPool;
    //   370: astore 11
    //   372: aload_0
    //   373: aload 11
    //   375: aload_0
    //   376: getfield 80	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:operator	Lorg/apache/http/conn/ClientConnectionOperator;
    //   379: invokevirtual 386	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:createEntry	(Lorg/apache/http/impl/conn/tsccm/RouteSpecificPool;Lorg/apache/http/conn/ClientConnectionOperator;)Lorg/apache/http/impl/conn/tsccm/BasicPoolEntry;
    //   382: astore 9
    //   384: goto -329 -> 55
    //   387: aload_0
    //   388: getfield 62	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:log	Lorg/apache/commons/logging/Log;
    //   391: invokeinterface 183 1 0
    //   396: ifeq +47 -> 443
    //   399: aload_0
    //   400: getfield 62	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:log	Lorg/apache/commons/logging/Log;
    //   403: new 185	java/lang/StringBuilder
    //   406: dup
    //   407: invokespecial 186	java/lang/StringBuilder:<init>	()V
    //   410: ldc_w 393
    //   413: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   416: aload_1
    //   417: invokevirtual 203	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   420: ldc_w 279
    //   423: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   426: aload_2
    //   427: invokevirtual 203	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   430: ldc 248
    //   432: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   435: invokevirtual 207	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   438: invokeinterface 149 2 0
    //   443: aload 12
    //   445: ifnonnull +27 -> 472
    //   448: aload_0
    //   449: aload_0
    //   450: getfield 75	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:poolLock	Ljava/util/concurrent/locks/Lock;
    //   453: invokeinterface 397 1 0
    //   458: aload 11
    //   460: invokevirtual 401	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:newWaitingThread	(Ljava/util/concurrent/locks/Condition;Lorg/apache/http/impl/conn/tsccm/RouteSpecificPool;)Lorg/apache/http/impl/conn/tsccm/WaitingThread;
    //   463: astore 12
    //   465: aload 6
    //   467: aload 12
    //   469: invokevirtual 407	org/apache/http/impl/conn/tsccm/WaitingThreadAborter:setWaitingThread	(Lorg/apache/http/impl/conn/tsccm/WaitingThread;)V
    //   472: aload 11
    //   474: aload 12
    //   476: invokevirtual 410	org/apache/http/impl/conn/tsccm/RouteSpecificPool:queueThread	(Lorg/apache/http/impl/conn/tsccm/WaitingThread;)V
    //   479: aload_0
    //   480: getfield 95	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:waitingThreads	Ljava/util/Queue;
    //   483: aload 12
    //   485: invokeinterface 328 2 0
    //   490: pop
    //   491: aload 12
    //   493: aload 8
    //   495: invokevirtual 416	org/apache/http/impl/conn/tsccm/WaitingThread:await	(Ljava/util/Date;)Z
    //   498: istore 19
    //   500: aload 11
    //   502: aload 12
    //   504: invokevirtual 419	org/apache/http/impl/conn/tsccm/RouteSpecificPool:removeThread	(Lorg/apache/http/impl/conn/tsccm/WaitingThread;)V
    //   507: aload_0
    //   508: getfield 95	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:waitingThreads	Ljava/util/Queue;
    //   511: aload 12
    //   513: invokeinterface 420 2 0
    //   518: pop
    //   519: iload 19
    //   521: ifne -466 -> 55
    //   524: aload 8
    //   526: ifnull -471 -> 55
    //   529: aload 8
    //   531: invokevirtual 423	java/util/Date:getTime	()J
    //   534: invokestatic 155	java/lang/System:currentTimeMillis	()J
    //   537: lcmp
    //   538: ifgt -483 -> 55
    //   541: new 346	org/apache/http/conn/ConnectionPoolTimeoutException
    //   544: dup
    //   545: ldc_w 425
    //   548: invokespecial 428	org/apache/http/conn/ConnectionPoolTimeoutException:<init>	(Ljava/lang/String;)V
    //   551: athrow
    //   552: astore 10
    //   554: aload_0
    //   555: getfield 75	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:poolLock	Ljava/util/concurrent/locks/Lock;
    //   558: invokeinterface 216 1 0
    //   563: aload 10
    //   565: athrow
    //   566: astore 16
    //   568: aload 11
    //   570: aload 12
    //   572: invokevirtual 419	org/apache/http/impl/conn/tsccm/RouteSpecificPool:removeThread	(Lorg/apache/http/impl/conn/tsccm/WaitingThread;)V
    //   575: aload_0
    //   576: getfield 95	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:waitingThreads	Ljava/util/Queue;
    //   579: aload 12
    //   581: invokeinterface 420 2 0
    //   586: pop
    //   587: aload 16
    //   589: athrow
    //   590: iconst_0
    //   591: istore 15
    //   593: goto -365 -> 228
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	596	0	this	ConnPoolByRoute
    //   0	596	1	paramHttpRoute	HttpRoute
    //   0	596	2	paramObject	Object
    //   0	596	3	paramLong	long
    //   0	596	5	paramTimeUnit	TimeUnit
    //   0	596	6	paramWaitingThreadAborter	WaitingThreadAborter
    //   3	6	7	bool1	boolean
    //   6	524	8	localDate	Date
    //   33	350	9	localObject1	Object
    //   552	12	10	localObject2	Object
    //   50	519	11	localRouteSpecificPool	RouteSpecificPool
    //   53	527	12	localWaitingThread	WaitingThread
    //   68	145	13	bool2	boolean
    //   188	3	14	localBasicPoolEntry	BasicPoolEntry
    //   226	366	15	i	int
    //   566	22	16	localObject3	Object
    //   498	22	19	bool3	boolean
    // Exception table:
    //   from	to	target	type
    //   44	52	552	finally
    //   60	67	552	finally
    //   70	181	552	finally
    //   181	190	552	finally
    //   217	225	552	finally
    //   228	312	552	finally
    //   317	340	552	finally
    //   348	384	552	finally
    //   387	443	552	finally
    //   448	472	552	finally
    //   500	519	552	finally
    //   529	552	552	finally
    //   568	590	552	finally
    //   472	500	566	finally
  }
  
  protected BasicPoolEntry getFreeEntry(RouteSpecificPool paramRouteSpecificPool, Object paramObject)
  {
    BasicPoolEntry localBasicPoolEntry = null;
    this.poolLock.lock();
    int i = 0;
    while (i == 0)
    {
      try
      {
        localBasicPoolEntry = paramRouteSpecificPool.allocEntry(paramObject);
        if (localBasicPoolEntry == null) {
          break label221;
        }
        if (this.log.isDebugEnabled()) {
          this.log.debug("Getting free connection [" + paramRouteSpecificPool.getRoute() + "][" + paramObject + "]");
        }
        this.freeConnections.remove(localBasicPoolEntry);
        if (localBasicPoolEntry.isExpired(System.currentTimeMillis()))
        {
          if (this.log.isDebugEnabled()) {
            this.log.debug("Closing expired free connection [" + paramRouteSpecificPool.getRoute() + "][" + paramObject + "]");
          }
          closeConnection(localBasicPoolEntry);
          paramRouteSpecificPool.dropEntry();
          this.numConnections = (-1 + this.numConnections);
          continue;
        }
      }
      finally
      {
        this.poolLock.unlock();
      }
      this.leasedConnections.add(localBasicPoolEntry);
      i = 1;
      continue;
      label221:
      i = 1;
      if (this.log.isDebugEnabled()) {
        this.log.debug("No free connections [" + paramRouteSpecificPool.getRoute() + "][" + paramObject + "]");
      }
    }
    this.poolLock.unlock();
    return localBasicPoolEntry;
  }
  
  protected Lock getLock()
  {
    return this.poolLock;
  }
  
  public int getMaxTotalConnections()
  {
    return this.maxTotalConnections;
  }
  
  protected RouteSpecificPool getRoutePool(HttpRoute paramHttpRoute, boolean paramBoolean)
  {
    this.poolLock.lock();
    try
    {
      RouteSpecificPool localRouteSpecificPool = (RouteSpecificPool)this.routeToPool.get(paramHttpRoute);
      if ((localRouteSpecificPool == null) && (paramBoolean))
      {
        localRouteSpecificPool = newRouteSpecificPool(paramHttpRoute);
        this.routeToPool.put(paramHttpRoute, localRouteSpecificPool);
      }
      return localRouteSpecificPool;
    }
    finally
    {
      this.poolLock.unlock();
    }
  }
  
  protected void handleLostEntry(HttpRoute paramHttpRoute)
  {
    this.poolLock.lock();
    try
    {
      RouteSpecificPool localRouteSpecificPool = getRoutePool(paramHttpRoute, true);
      localRouteSpecificPool.dropEntry();
      if (localRouteSpecificPool.isUnused()) {
        this.routeToPool.remove(paramHttpRoute);
      }
      this.numConnections = (-1 + this.numConnections);
      notifyWaitingThread(localRouteSpecificPool);
      return;
    }
    finally
    {
      this.poolLock.unlock();
    }
  }
  
  protected RouteSpecificPool newRouteSpecificPool(HttpRoute paramHttpRoute)
  {
    return new RouteSpecificPool(paramHttpRoute, this.connPerRoute);
  }
  
  protected WaitingThread newWaitingThread(Condition paramCondition, RouteSpecificPool paramRouteSpecificPool)
  {
    return new WaitingThread(paramCondition, paramRouteSpecificPool);
  }
  
  /* Error */
  protected void notifyWaitingThread(RouteSpecificPool paramRouteSpecificPool)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 75	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:poolLock	Ljava/util/concurrent/locks/Lock;
    //   4: invokeinterface 160 1 0
    //   9: aload_1
    //   10: ifnull +85 -> 95
    //   13: aload_1
    //   14: invokevirtual 462	org/apache/http/impl/conn/tsccm/RouteSpecificPool:hasThread	()Z
    //   17: ifeq +78 -> 95
    //   20: aload_0
    //   21: getfield 62	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:log	Lorg/apache/commons/logging/Log;
    //   24: invokeinterface 183 1 0
    //   29: ifeq +40 -> 69
    //   32: aload_0
    //   33: getfield 62	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:log	Lorg/apache/commons/logging/Log;
    //   36: new 185	java/lang/StringBuilder
    //   39: dup
    //   40: invokespecial 186	java/lang/StringBuilder:<init>	()V
    //   43: ldc_w 464
    //   46: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   49: aload_1
    //   50: invokevirtual 246	org/apache/http/impl/conn/tsccm/RouteSpecificPool:getRoute	()Lorg/apache/http/conn/routing/HttpRoute;
    //   53: invokevirtual 203	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   56: ldc 248
    //   58: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: invokevirtual 207	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   64: invokeinterface 149 2 0
    //   69: aload_1
    //   70: invokevirtual 468	org/apache/http/impl/conn/tsccm/RouteSpecificPool:nextThread	()Lorg/apache/http/impl/conn/tsccm/WaitingThread;
    //   73: astore 4
    //   75: aload 4
    //   77: ifnull +8 -> 85
    //   80: aload 4
    //   82: invokevirtual 471	org/apache/http/impl/conn/tsccm/WaitingThread:wakeup	()V
    //   85: aload_0
    //   86: getfield 75	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:poolLock	Ljava/util/concurrent/locks/Lock;
    //   89: invokeinterface 216 1 0
    //   94: return
    //   95: aload_0
    //   96: getfield 95	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:waitingThreads	Ljava/util/Queue;
    //   99: invokeinterface 389 1 0
    //   104: ifne +44 -> 148
    //   107: aload_0
    //   108: getfield 62	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:log	Lorg/apache/commons/logging/Log;
    //   111: invokeinterface 183 1 0
    //   116: ifeq +15 -> 131
    //   119: aload_0
    //   120: getfield 62	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:log	Lorg/apache/commons/logging/Log;
    //   123: ldc_w 473
    //   126: invokeinterface 149 2 0
    //   131: aload_0
    //   132: getfield 95	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:waitingThreads	Ljava/util/Queue;
    //   135: invokeinterface 302 1 0
    //   140: checkcast 412	org/apache/http/impl/conn/tsccm/WaitingThread
    //   143: astore 4
    //   145: goto -70 -> 75
    //   148: aload_0
    //   149: getfield 62	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:log	Lorg/apache/commons/logging/Log;
    //   152: invokeinterface 183 1 0
    //   157: istore_3
    //   158: aconst_null
    //   159: astore 4
    //   161: iload_3
    //   162: ifeq -87 -> 75
    //   165: aload_0
    //   166: getfield 62	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:log	Lorg/apache/commons/logging/Log;
    //   169: ldc_w 475
    //   172: invokeinterface 149 2 0
    //   177: aconst_null
    //   178: astore 4
    //   180: goto -105 -> 75
    //   183: astore_2
    //   184: aload_0
    //   185: getfield 75	org/apache/http/impl/conn/tsccm/ConnPoolByRoute:poolLock	Ljava/util/concurrent/locks/Lock;
    //   188: invokeinterface 216 1 0
    //   193: aload_2
    //   194: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	195	0	this	ConnPoolByRoute
    //   0	195	1	paramRouteSpecificPool	RouteSpecificPool
    //   183	11	2	localObject	Object
    //   157	5	3	bool	boolean
    //   73	106	4	localWaitingThread	WaitingThread
    // Exception table:
    //   from	to	target	type
    //   13	69	183	finally
    //   69	75	183	finally
    //   80	85	183	finally
    //   95	131	183	finally
    //   131	145	183	finally
    //   148	158	183	finally
    //   165	177	183	finally
  }
  
  public PoolEntryRequest requestPoolEntry(final HttpRoute paramHttpRoute, final Object paramObject)
  {
    new PoolEntryRequest()
    {
      public void abortRequest()
      {
        ConnPoolByRoute.this.poolLock.lock();
        try
        {
          this.val$aborter.abort();
          return;
        }
        finally
        {
          ConnPoolByRoute.this.poolLock.unlock();
        }
      }
      
      public BasicPoolEntry getPoolEntry(long paramAnonymousLong, TimeUnit paramAnonymousTimeUnit)
        throws InterruptedException, ConnectionPoolTimeoutException
      {
        return ConnPoolByRoute.this.getEntryBlocking(paramHttpRoute, paramObject, paramAnonymousLong, paramAnonymousTimeUnit, this.val$aborter);
      }
    };
  }
  
  public void setMaxTotalConnections(int paramInt)
  {
    this.poolLock.lock();
    try
    {
      this.maxTotalConnections = paramInt;
      return;
    }
    finally
    {
      this.poolLock.unlock();
    }
  }
  
  public void shutdown()
  {
    this.poolLock.lock();
    try
    {
      boolean bool = this.shutdown;
      if (bool) {
        return;
      }
      this.shutdown = true;
      Iterator localIterator1 = this.leasedConnections.iterator();
      while (localIterator1.hasNext())
      {
        BasicPoolEntry localBasicPoolEntry2 = (BasicPoolEntry)localIterator1.next();
        localIterator1.remove();
        closeConnection(localBasicPoolEntry2);
      }
      localIterator2 = this.freeConnections.iterator();
    }
    finally
    {
      this.poolLock.unlock();
    }
    Iterator localIterator2;
    while (localIterator2.hasNext())
    {
      BasicPoolEntry localBasicPoolEntry1 = (BasicPoolEntry)localIterator2.next();
      localIterator2.remove();
      if (this.log.isDebugEnabled()) {
        this.log.debug("Closing connection [" + localBasicPoolEntry1.getPlannedRoute() + "][" + localBasicPoolEntry1.getState() + "]");
      }
      closeConnection(localBasicPoolEntry1);
    }
    Iterator localIterator3 = this.waitingThreads.iterator();
    while (localIterator3.hasNext())
    {
      WaitingThread localWaitingThread = (WaitingThread)localIterator3.next();
      localIterator3.remove();
      localWaitingThread.wakeup();
    }
    this.routeToPool.clear();
    this.poolLock.unlock();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.tsccm.ConnPoolByRoute
 * JD-Core Version:    0.7.0.1
 */