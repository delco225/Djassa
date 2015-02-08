package org.apache.http.pool;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@ThreadSafe
public abstract class AbstractConnPool<T, C, E extends PoolEntry<T, C>>
  implements ConnPool<T, E>, ConnPoolControl<T>
{
  private final LinkedList<E> available;
  private final ConnFactory<T, C> connFactory;
  private volatile int defaultMaxPerRoute;
  private volatile boolean isShutDown;
  private final Set<E> leased;
  private final Lock lock;
  private final Map<T, Integer> maxPerRoute;
  private volatile int maxTotal;
  private final LinkedList<PoolEntryFuture<E>> pending;
  private final Map<T, RouteSpecificPool<T, C, E>> routeToPool;
  
  public AbstractConnPool(ConnFactory<T, C> paramConnFactory, int paramInt1, int paramInt2)
  {
    this.connFactory = ((ConnFactory)Args.notNull(paramConnFactory, "Connection factory"));
    this.defaultMaxPerRoute = Args.notNegative(paramInt1, "Max per route value");
    this.maxTotal = Args.notNegative(paramInt2, "Max total value");
    this.lock = new ReentrantLock();
    this.routeToPool = new HashMap();
    this.leased = new HashSet();
    this.available = new LinkedList();
    this.pending = new LinkedList();
    this.maxPerRoute = new HashMap();
  }
  
  private int getMax(T paramT)
  {
    Integer localInteger = (Integer)this.maxPerRoute.get(paramT);
    if (localInteger != null) {
      return localInteger.intValue();
    }
    return this.defaultMaxPerRoute;
  }
  
  private RouteSpecificPool<T, C, E> getPool(final T paramT)
  {
    Object localObject = (RouteSpecificPool)this.routeToPool.get(paramT);
    if (localObject == null)
    {
      localObject = new RouteSpecificPool(paramT)
      {
        protected E createEntry(C paramAnonymousC)
        {
          return AbstractConnPool.this.createEntry(paramT, paramAnonymousC);
        }
      };
      this.routeToPool.put(paramT, localObject);
    }
    return localObject;
  }
  
  private E getPoolEntryBlocking(T paramT, Object paramObject, long paramLong, TimeUnit paramTimeUnit, PoolEntryFuture<E> paramPoolEntryFuture)
    throws IOException, InterruptedException, TimeoutException
  {
    boolean bool1 = paramLong < 0L;
    Date localDate = null;
    if (bool1) {
      localDate = new Date(System.currentTimeMillis() + paramTimeUnit.toMillis(paramLong));
    }
    this.lock.lock();
    for (;;)
    {
      RouteSpecificPool localRouteSpecificPool;
      try
      {
        localRouteSpecificPool = getPool(paramT);
        PoolEntry localPoolEntry1 = null;
        if (localPoolEntry1 != null) {
          break label470;
        }
        boolean bool2;
        if (!this.isShutDown)
        {
          bool2 = true;
          Asserts.check(bool2, "Connection pool shut down");
          localPoolEntry1 = localRouteSpecificPool.getFree(paramObject);
          if (localPoolEntry1 == null)
          {
            if (localPoolEntry1 == null) {
              break label190;
            }
            this.available.remove(localPoolEntry1);
            this.leased.add(localPoolEntry1);
            return localPoolEntry1;
          }
        }
        else
        {
          bool2 = false;
          continue;
        }
        if ((!localPoolEntry1.isClosed()) && (!localPoolEntry1.isExpired(System.currentTimeMillis()))) {
          continue;
        }
        localPoolEntry1.close();
        this.available.remove(localPoolEntry1);
        localRouteSpecificPool.free(localPoolEntry1, false);
        continue;
        i = getMax(paramT);
      }
      finally
      {
        this.lock.unlock();
      }
      label190:
      int i;
      int j = Math.max(0, 1 + localRouteSpecificPool.getAllocatedCount() - i);
      if (j > 0) {}
      for (int n = 0;; n++)
      {
        PoolEntry localPoolEntry4;
        if (n < j)
        {
          localPoolEntry4 = localRouteSpecificPool.getLastUsed();
          if (localPoolEntry4 != null) {}
        }
        else
        {
          if (localRouteSpecificPool.getAllocatedCount() >= i) {
            break;
          }
          int k = this.leased.size();
          int m = Math.max(this.maxTotal - k, 0);
          if (m <= 0) {
            break;
          }
          if ((this.available.size() > m - 1) && (!this.available.isEmpty()))
          {
            PoolEntry localPoolEntry3 = (PoolEntry)this.available.removeLast();
            localPoolEntry3.close();
            getPool(localPoolEntry3.getRoute()).remove(localPoolEntry3);
          }
          PoolEntry localPoolEntry2 = localRouteSpecificPool.add(this.connFactory.create(paramT));
          this.leased.add(localPoolEntry2);
          this.lock.unlock();
          return localPoolEntry2;
        }
        localPoolEntry4.close();
        this.available.remove(localPoolEntry4);
        localRouteSpecificPool.remove(localPoolEntry4);
      }
      try
      {
        localRouteSpecificPool.queue(paramPoolEntryFuture);
        this.pending.add(paramPoolEntryFuture);
        boolean bool3 = paramPoolEntryFuture.await(localDate);
        localRouteSpecificPool.unqueue(paramPoolEntryFuture);
        this.pending.remove(paramPoolEntryFuture);
        if ((bool3) || (localDate == null) || (localDate.getTime() > System.currentTimeMillis())) {
          continue;
        }
        label470:
        throw new TimeoutException("Timeout waiting for connection");
      }
      finally
      {
        localRouteSpecificPool.unqueue(paramPoolEntryFuture);
        this.pending.remove(paramPoolEntryFuture);
      }
    }
  }
  
  private void purgePoolMap()
  {
    Iterator localIterator = this.routeToPool.entrySet().iterator();
    while (localIterator.hasNext())
    {
      RouteSpecificPool localRouteSpecificPool = (RouteSpecificPool)((Map.Entry)localIterator.next()).getValue();
      if (localRouteSpecificPool.getPendingCount() + localRouteSpecificPool.getAllocatedCount() == 0) {
        localIterator.remove();
      }
    }
  }
  
  public void closeExpired()
  {
    enumAvailable(new PoolEntryCallback()
    {
      public void process(PoolEntry<T, C> paramAnonymousPoolEntry)
      {
        if (paramAnonymousPoolEntry.isExpired(this.val$now)) {
          paramAnonymousPoolEntry.close();
        }
      }
    });
  }
  
  public void closeIdle(long paramLong, TimeUnit paramTimeUnit)
  {
    Args.notNull(paramTimeUnit, "Time unit");
    long l = paramTimeUnit.toMillis(paramLong);
    if (l < 0L) {
      l = 0L;
    }
    enumAvailable(new PoolEntryCallback()
    {
      public void process(PoolEntry<T, C> paramAnonymousPoolEntry)
      {
        if (paramAnonymousPoolEntry.getUpdated() <= this.val$deadline) {
          paramAnonymousPoolEntry.close();
        }
      }
    });
  }
  
  protected abstract E createEntry(T paramT, C paramC);
  
  protected void enumAvailable(PoolEntryCallback<T, C> paramPoolEntryCallback)
  {
    this.lock.lock();
    try
    {
      Iterator localIterator = this.available.iterator();
      while (localIterator.hasNext())
      {
        PoolEntry localPoolEntry = (PoolEntry)localIterator.next();
        paramPoolEntryCallback.process(localPoolEntry);
        if (localPoolEntry.isClosed())
        {
          getPool(localPoolEntry.getRoute()).remove(localPoolEntry);
          localIterator.remove();
        }
      }
      purgePoolMap();
    }
    finally
    {
      this.lock.unlock();
    }
    this.lock.unlock();
  }
  
  protected void enumLeased(PoolEntryCallback<T, C> paramPoolEntryCallback)
  {
    this.lock.lock();
    try
    {
      Iterator localIterator = this.leased.iterator();
      while (localIterator.hasNext()) {
        paramPoolEntryCallback.process((PoolEntry)localIterator.next());
      }
    }
    finally
    {
      this.lock.unlock();
    }
  }
  
  public int getDefaultMaxPerRoute()
  {
    this.lock.lock();
    try
    {
      int i = this.defaultMaxPerRoute;
      return i;
    }
    finally
    {
      this.lock.unlock();
    }
  }
  
  public int getMaxPerRoute(T paramT)
  {
    Args.notNull(paramT, "Route");
    this.lock.lock();
    try
    {
      int i = getMax(paramT);
      return i;
    }
    finally
    {
      this.lock.unlock();
    }
  }
  
  public int getMaxTotal()
  {
    this.lock.lock();
    try
    {
      int i = this.maxTotal;
      return i;
    }
    finally
    {
      this.lock.unlock();
    }
  }
  
  public PoolStats getStats(T paramT)
  {
    Args.notNull(paramT, "Route");
    this.lock.lock();
    try
    {
      RouteSpecificPool localRouteSpecificPool = getPool(paramT);
      PoolStats localPoolStats = new PoolStats(localRouteSpecificPool.getLeasedCount(), localRouteSpecificPool.getPendingCount(), localRouteSpecificPool.getAvailableCount(), getMax(paramT));
      return localPoolStats;
    }
    finally
    {
      this.lock.unlock();
    }
  }
  
  public PoolStats getTotalStats()
  {
    this.lock.lock();
    try
    {
      PoolStats localPoolStats = new PoolStats(this.leased.size(), this.pending.size(), this.available.size(), this.maxTotal);
      return localPoolStats;
    }
    finally
    {
      this.lock.unlock();
    }
  }
  
  public boolean isShutdown()
  {
    return this.isShutDown;
  }
  
  public Future<E> lease(T paramT, Object paramObject)
  {
    return lease(paramT, paramObject, null);
  }
  
  public Future<E> lease(final T paramT, final Object paramObject, FutureCallback<E> paramFutureCallback)
  {
    Args.notNull(paramT, "Route");
    if (!this.isShutDown) {}
    for (boolean bool = true;; bool = false)
    {
      Asserts.check(bool, "Connection pool shut down");
      new PoolEntryFuture(this.lock, paramFutureCallback)
      {
        public E getPoolEntry(long paramAnonymousLong, TimeUnit paramAnonymousTimeUnit)
          throws InterruptedException, TimeoutException, IOException
        {
          PoolEntry localPoolEntry = AbstractConnPool.this.getPoolEntryBlocking(paramT, paramObject, paramAnonymousLong, paramAnonymousTimeUnit, this);
          AbstractConnPool.this.onLease(localPoolEntry);
          return localPoolEntry;
        }
      };
    }
  }
  
  protected void onLease(E paramE) {}
  
  protected void onRelease(E paramE) {}
  
  public void release(E paramE, boolean paramBoolean)
  {
    this.lock.lock();
    label124:
    for (;;)
    {
      try
      {
        if (this.leased.remove(paramE))
        {
          RouteSpecificPool localRouteSpecificPool = getPool(paramE.getRoute());
          localRouteSpecificPool.free(paramE, paramBoolean);
          if ((!paramBoolean) || (this.isShutDown)) {
            continue;
          }
          this.available.addFirst(paramE);
          onRelease(paramE);
          localPoolEntryFuture = localRouteSpecificPool.nextPending();
          if (localPoolEntryFuture == null) {
            break label124;
          }
          this.pending.remove(localPoolEntryFuture);
          if (localPoolEntryFuture != null) {
            localPoolEntryFuture.wakeup();
          }
        }
        return;
        paramE.close();
        continue;
        PoolEntryFuture localPoolEntryFuture = (PoolEntryFuture)this.pending.poll();
      }
      finally
      {
        this.lock.unlock();
      }
    }
  }
  
  public void setDefaultMaxPerRoute(int paramInt)
  {
    Args.notNegative(paramInt, "Max per route value");
    this.lock.lock();
    try
    {
      this.defaultMaxPerRoute = paramInt;
      return;
    }
    finally
    {
      this.lock.unlock();
    }
  }
  
  public void setMaxPerRoute(T paramT, int paramInt)
  {
    Args.notNull(paramT, "Route");
    Args.notNegative(paramInt, "Max per route value");
    this.lock.lock();
    try
    {
      this.maxPerRoute.put(paramT, Integer.valueOf(paramInt));
      return;
    }
    finally
    {
      this.lock.unlock();
    }
  }
  
  public void setMaxTotal(int paramInt)
  {
    Args.notNegative(paramInt, "Max value");
    this.lock.lock();
    try
    {
      this.maxTotal = paramInt;
      return;
    }
    finally
    {
      this.lock.unlock();
    }
  }
  
  public void shutdown()
    throws IOException
  {
    if (this.isShutDown) {
      return;
    }
    this.isShutDown = true;
    this.lock.lock();
    try
    {
      Iterator localIterator1 = this.available.iterator();
      while (localIterator1.hasNext()) {
        ((PoolEntry)localIterator1.next()).close();
      }
      localIterator2 = this.leased.iterator();
    }
    finally
    {
      this.lock.unlock();
    }
    Iterator localIterator2;
    while (localIterator2.hasNext()) {
      ((PoolEntry)localIterator2.next()).close();
    }
    Iterator localIterator3 = this.routeToPool.values().iterator();
    while (localIterator3.hasNext()) {
      ((RouteSpecificPool)localIterator3.next()).shutdown();
    }
    this.routeToPool.clear();
    this.leased.clear();
    this.available.clear();
    this.lock.unlock();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[leased: ");
    localStringBuilder.append(this.leased);
    localStringBuilder.append("][available: ");
    localStringBuilder.append(this.available);
    localStringBuilder.append("][pending: ");
    localStringBuilder.append(this.pending);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.pool.AbstractConnPool
 * JD-Core Version:    0.7.0.1
 */