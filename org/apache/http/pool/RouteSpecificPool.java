package org.apache.http.pool;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@NotThreadSafe
abstract class RouteSpecificPool<T, C, E extends PoolEntry<T, C>>
{
  private final LinkedList<E> available;
  private final Set<E> leased;
  private final LinkedList<PoolEntryFuture<E>> pending;
  private final T route;
  
  RouteSpecificPool(T paramT)
  {
    this.route = paramT;
    this.leased = new HashSet();
    this.available = new LinkedList();
    this.pending = new LinkedList();
  }
  
  public E add(C paramC)
  {
    PoolEntry localPoolEntry = createEntry(paramC);
    this.leased.add(localPoolEntry);
    return localPoolEntry;
  }
  
  protected abstract E createEntry(C paramC);
  
  public void free(E paramE, boolean paramBoolean)
  {
    Args.notNull(paramE, "Pool entry");
    Asserts.check(this.leased.remove(paramE), "Entry %s has not been leased from this pool", new Object[] { paramE });
    if (paramBoolean) {
      this.available.addFirst(paramE);
    }
  }
  
  public int getAllocatedCount()
  {
    return this.available.size() + this.leased.size();
  }
  
  public int getAvailableCount()
  {
    return this.available.size();
  }
  
  public E getFree(Object paramObject)
  {
    if (!this.available.isEmpty())
    {
      if (paramObject != null)
      {
        Iterator localIterator2 = this.available.iterator();
        while (localIterator2.hasNext())
        {
          PoolEntry localPoolEntry2 = (PoolEntry)localIterator2.next();
          if (paramObject.equals(localPoolEntry2.getState()))
          {
            localIterator2.remove();
            this.leased.add(localPoolEntry2);
            return localPoolEntry2;
          }
        }
      }
      Iterator localIterator1 = this.available.iterator();
      while (localIterator1.hasNext())
      {
        PoolEntry localPoolEntry1 = (PoolEntry)localIterator1.next();
        if (localPoolEntry1.getState() == null)
        {
          localIterator1.remove();
          this.leased.add(localPoolEntry1);
          return localPoolEntry1;
        }
      }
    }
    return null;
  }
  
  public E getLastUsed()
  {
    if (!this.available.isEmpty()) {
      return (PoolEntry)this.available.getLast();
    }
    return null;
  }
  
  public int getLeasedCount()
  {
    return this.leased.size();
  }
  
  public int getPendingCount()
  {
    return this.pending.size();
  }
  
  public final T getRoute()
  {
    return this.route;
  }
  
  public PoolEntryFuture<E> nextPending()
  {
    return (PoolEntryFuture)this.pending.poll();
  }
  
  public void queue(PoolEntryFuture<E> paramPoolEntryFuture)
  {
    if (paramPoolEntryFuture == null) {
      return;
    }
    this.pending.add(paramPoolEntryFuture);
  }
  
  public boolean remove(E paramE)
  {
    Args.notNull(paramE, "Pool entry");
    return (this.available.remove(paramE)) || (this.leased.remove(paramE));
  }
  
  public void shutdown()
  {
    Iterator localIterator1 = this.pending.iterator();
    while (localIterator1.hasNext()) {
      ((PoolEntryFuture)localIterator1.next()).cancel(true);
    }
    this.pending.clear();
    Iterator localIterator2 = this.available.iterator();
    while (localIterator2.hasNext()) {
      ((PoolEntry)localIterator2.next()).close();
    }
    this.available.clear();
    Iterator localIterator3 = this.leased.iterator();
    while (localIterator3.hasNext()) {
      ((PoolEntry)localIterator3.next()).close();
    }
    this.leased.clear();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[route: ");
    localStringBuilder.append(this.route);
    localStringBuilder.append("][leased: ");
    localStringBuilder.append(this.leased.size());
    localStringBuilder.append("][available: ");
    localStringBuilder.append(this.available.size());
    localStringBuilder.append("][pending: ");
    localStringBuilder.append(this.pending.size());
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void unqueue(PoolEntryFuture<E> paramPoolEntryFuture)
  {
    if (paramPoolEntryFuture == null) {
      return;
    }
    this.pending.remove(paramPoolEntryFuture);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.pool.RouteSpecificPool
 * JD-Core Version:    0.7.0.1
 */