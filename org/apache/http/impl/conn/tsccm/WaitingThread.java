package org.apache.http.impl.conn.tsccm;

import java.util.Date;
import java.util.concurrent.locks.Condition;
import org.apache.http.util.Args;

@Deprecated
public class WaitingThread
{
  private boolean aborted;
  private final Condition cond;
  private final RouteSpecificPool pool;
  private Thread waiter;
  
  public WaitingThread(Condition paramCondition, RouteSpecificPool paramRouteSpecificPool)
  {
    Args.notNull(paramCondition, "Condition");
    this.cond = paramCondition;
    this.pool = paramRouteSpecificPool;
  }
  
  public boolean await(Date paramDate)
    throws InterruptedException
  {
    if (this.waiter != null) {
      throw new IllegalStateException("A thread is already waiting on this object.\ncaller: " + Thread.currentThread() + "\nwaiter: " + this.waiter);
    }
    if (this.aborted) {
      throw new InterruptedException("Operation interrupted");
    }
    this.waiter = Thread.currentThread();
    if (paramDate != null) {}
    boolean bool;
    for (;;)
    {
      try
      {
        bool = this.cond.awaitUntil(paramDate);
        if (!this.aborted) {
          break;
        }
        throw new InterruptedException("Operation interrupted");
      }
      finally
      {
        this.waiter = null;
      }
      this.cond.await();
      bool = true;
    }
    this.waiter = null;
    return bool;
  }
  
  public final Condition getCondition()
  {
    return this.cond;
  }
  
  public final RouteSpecificPool getPool()
  {
    return this.pool;
  }
  
  public final Thread getThread()
  {
    return this.waiter;
  }
  
  public void interrupt()
  {
    this.aborted = true;
    this.cond.signalAll();
  }
  
  public void wakeup()
  {
    if (this.waiter == null) {
      throw new IllegalStateException("Nobody waiting on this object.");
    }
    this.cond.signalAll();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.tsccm.WaitingThread
 * JD-Core Version:    0.7.0.1
 */