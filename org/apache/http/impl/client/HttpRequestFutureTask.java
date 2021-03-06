package org.apache.http.impl.client;

import java.util.concurrent.FutureTask;
import org.apache.http.RequestLine;
import org.apache.http.client.methods.HttpUriRequest;

public class HttpRequestFutureTask<V>
  extends FutureTask<V>
{
  private final HttpRequestTaskCallable<V> callable;
  private final HttpUriRequest request;
  
  public HttpRequestFutureTask(HttpUriRequest paramHttpUriRequest, HttpRequestTaskCallable<V> paramHttpRequestTaskCallable)
  {
    super(paramHttpRequestTaskCallable);
    this.request = paramHttpUriRequest;
    this.callable = paramHttpRequestTaskCallable;
  }
  
  public boolean cancel(boolean paramBoolean)
  {
    this.callable.cancel();
    if (paramBoolean) {
      this.request.abort();
    }
    return super.cancel(paramBoolean);
  }
  
  public long endedTime()
  {
    if (isDone()) {
      return this.callable.getEnded();
    }
    throw new IllegalStateException("Task is not done yet");
  }
  
  public long requestDuration()
  {
    if (isDone()) {
      return endedTime() - startedTime();
    }
    throw new IllegalStateException("Task is not done yet");
  }
  
  public long scheduledTime()
  {
    return this.callable.getScheduled();
  }
  
  public long startedTime()
  {
    return this.callable.getStarted();
  }
  
  public long taskDuration()
  {
    if (isDone()) {
      return endedTime() - scheduledTime();
    }
    throw new IllegalStateException("Task is not done yet");
  }
  
  public String toString()
  {
    return this.request.getRequestLine().getUri();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.HttpRequestFutureTask
 * JD-Core Version:    0.7.0.1
 */