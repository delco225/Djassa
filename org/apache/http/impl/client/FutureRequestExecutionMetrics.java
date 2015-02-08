package org.apache.http.impl.client;

import java.util.concurrent.atomic.AtomicLong;

public final class FutureRequestExecutionMetrics
{
  private final AtomicLong activeConnections = new AtomicLong();
  private final DurationCounter failedConnections = new DurationCounter();
  private final DurationCounter requests = new DurationCounter();
  private final AtomicLong scheduledConnections = new AtomicLong();
  private final DurationCounter successfulConnections = new DurationCounter();
  private final DurationCounter tasks = new DurationCounter();
  
  public long getActiveConnectionCount()
  {
    return this.activeConnections.get();
  }
  
  AtomicLong getActiveConnections()
  {
    return this.activeConnections;
  }
  
  public long getFailedConnectionAverageDuration()
  {
    return this.failedConnections.averageDuration();
  }
  
  public long getFailedConnectionCount()
  {
    return this.failedConnections.count();
  }
  
  DurationCounter getFailedConnections()
  {
    return this.failedConnections;
  }
  
  public long getRequestAverageDuration()
  {
    return this.requests.averageDuration();
  }
  
  public long getRequestCount()
  {
    return this.requests.count();
  }
  
  DurationCounter getRequests()
  {
    return this.requests;
  }
  
  public long getScheduledConnectionCount()
  {
    return this.scheduledConnections.get();
  }
  
  AtomicLong getScheduledConnections()
  {
    return this.scheduledConnections;
  }
  
  public long getSuccessfulConnectionAverageDuration()
  {
    return this.successfulConnections.averageDuration();
  }
  
  public long getSuccessfulConnectionCount()
  {
    return this.successfulConnections.count();
  }
  
  DurationCounter getSuccessfulConnections()
  {
    return this.successfulConnections;
  }
  
  public long getTaskAverageDuration()
  {
    return this.tasks.averageDuration();
  }
  
  public long getTaskCount()
  {
    return this.tasks.count();
  }
  
  DurationCounter getTasks()
  {
    return this.tasks;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[activeConnections=").append(this.activeConnections).append(", scheduledConnections=").append(this.scheduledConnections).append(", successfulConnections=").append(this.successfulConnections).append(", failedConnections=").append(this.failedConnections).append(", requests=").append(this.requests).append(", tasks=").append(this.tasks).append("]");
    return localStringBuilder.toString();
  }
  
  static class DurationCounter
  {
    private final AtomicLong count = new AtomicLong(0L);
    private final AtomicLong cumulativeDuration = new AtomicLong(0L);
    
    public long averageDuration()
    {
      long l1 = 0L;
      long l2 = this.count.get();
      if (l2 > l1) {
        l1 = this.cumulativeDuration.get() / l2;
      }
      return l1;
    }
    
    public long count()
    {
      return this.count.get();
    }
    
    public void increment(long paramLong)
    {
      this.count.incrementAndGet();
      this.cumulativeDuration.addAndGet(System.currentTimeMillis() - paramLong);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("[count=").append(count()).append(", averageDuration=").append(averageDuration()).append("]");
      return localStringBuilder.toString();
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.FutureRequestExecutionMetrics
 * JD-Core Version:    0.7.0.1
 */