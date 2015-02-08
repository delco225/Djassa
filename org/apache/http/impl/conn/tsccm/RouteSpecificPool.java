package org.apache.http.impl.conn.tsccm;

import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.LangUtils;

@Deprecated
public class RouteSpecificPool
{
  protected final ConnPerRoute connPerRoute;
  protected final LinkedList<BasicPoolEntry> freeEntries;
  private final Log log = LogFactory.getLog(getClass());
  protected final int maxEntries;
  protected int numEntries;
  protected final HttpRoute route;
  protected final Queue<WaitingThread> waitingThreads;
  
  @Deprecated
  public RouteSpecificPool(HttpRoute paramHttpRoute, int paramInt)
  {
    this.route = paramHttpRoute;
    this.maxEntries = paramInt;
    this.connPerRoute = new ConnPerRoute()
    {
      public int getMaxForRoute(HttpRoute paramAnonymousHttpRoute)
      {
        return RouteSpecificPool.this.maxEntries;
      }
    };
    this.freeEntries = new LinkedList();
    this.waitingThreads = new LinkedList();
    this.numEntries = 0;
  }
  
  public RouteSpecificPool(HttpRoute paramHttpRoute, ConnPerRoute paramConnPerRoute)
  {
    this.route = paramHttpRoute;
    this.connPerRoute = paramConnPerRoute;
    this.maxEntries = paramConnPerRoute.getMaxForRoute(paramHttpRoute);
    this.freeEntries = new LinkedList();
    this.waitingThreads = new LinkedList();
    this.numEntries = 0;
  }
  
  public BasicPoolEntry allocEntry(Object paramObject)
  {
    if (!this.freeEntries.isEmpty())
    {
      ListIterator localListIterator = this.freeEntries.listIterator(this.freeEntries.size());
      while (localListIterator.hasPrevious())
      {
        BasicPoolEntry localBasicPoolEntry2 = (BasicPoolEntry)localListIterator.previous();
        if ((localBasicPoolEntry2.getState() == null) || (LangUtils.equals(paramObject, localBasicPoolEntry2.getState())))
        {
          localListIterator.remove();
          return localBasicPoolEntry2;
        }
      }
    }
    if ((getCapacity() == 0) && (!this.freeEntries.isEmpty()))
    {
      BasicPoolEntry localBasicPoolEntry1 = (BasicPoolEntry)this.freeEntries.remove();
      localBasicPoolEntry1.shutdownEntry();
      OperatedClientConnection localOperatedClientConnection = localBasicPoolEntry1.getConnection();
      try
      {
        localOperatedClientConnection.close();
        return localBasicPoolEntry1;
      }
      catch (IOException localIOException)
      {
        this.log.debug("I/O error closing connection", localIOException);
        return localBasicPoolEntry1;
      }
    }
    return null;
  }
  
  public void createdEntry(BasicPoolEntry paramBasicPoolEntry)
  {
    Args.check(this.route.equals(paramBasicPoolEntry.getPlannedRoute()), "Entry not planned for this pool");
    this.numEntries = (1 + this.numEntries);
  }
  
  public boolean deleteEntry(BasicPoolEntry paramBasicPoolEntry)
  {
    boolean bool = this.freeEntries.remove(paramBasicPoolEntry);
    if (bool) {
      this.numEntries = (-1 + this.numEntries);
    }
    return bool;
  }
  
  public void dropEntry()
  {
    if (this.numEntries > 0) {}
    for (boolean bool = true;; bool = false)
    {
      Asserts.check(bool, "There is no entry that could be dropped");
      this.numEntries = (-1 + this.numEntries);
      return;
    }
  }
  
  public void freeEntry(BasicPoolEntry paramBasicPoolEntry)
  {
    if (this.numEntries < 1) {
      throw new IllegalStateException("No entry created for this pool. " + this.route);
    }
    if (this.numEntries <= this.freeEntries.size()) {
      throw new IllegalStateException("No entry allocated from this pool. " + this.route);
    }
    this.freeEntries.add(paramBasicPoolEntry);
  }
  
  public int getCapacity()
  {
    return this.connPerRoute.getMaxForRoute(this.route) - this.numEntries;
  }
  
  public final int getEntryCount()
  {
    return this.numEntries;
  }
  
  public final int getMaxEntries()
  {
    return this.maxEntries;
  }
  
  public final HttpRoute getRoute()
  {
    return this.route;
  }
  
  public boolean hasThread()
  {
    return !this.waitingThreads.isEmpty();
  }
  
  public boolean isUnused()
  {
    return (this.numEntries < 1) && (this.waitingThreads.isEmpty());
  }
  
  public WaitingThread nextThread()
  {
    return (WaitingThread)this.waitingThreads.peek();
  }
  
  public void queueThread(WaitingThread paramWaitingThread)
  {
    Args.notNull(paramWaitingThread, "Waiting thread");
    this.waitingThreads.add(paramWaitingThread);
  }
  
  public void removeThread(WaitingThread paramWaitingThread)
  {
    if (paramWaitingThread == null) {
      return;
    }
    this.waitingThreads.remove(paramWaitingThread);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.tsccm.RouteSpecificPool
 * JD-Core Version:    0.7.0.1
 */