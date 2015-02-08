package org.apache.http.impl.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.protocol.HttpContext;

@ThreadSafe
public class FutureRequestExecutionService
  implements Closeable
{
  private final AtomicBoolean closed = new AtomicBoolean(false);
  private final ExecutorService executorService;
  private final HttpClient httpclient;
  private final FutureRequestExecutionMetrics metrics = new FutureRequestExecutionMetrics();
  
  public FutureRequestExecutionService(HttpClient paramHttpClient, ExecutorService paramExecutorService)
  {
    this.httpclient = paramHttpClient;
    this.executorService = paramExecutorService;
  }
  
  public void close()
    throws IOException
  {
    this.closed.set(true);
    this.executorService.shutdownNow();
    if ((this.httpclient instanceof Closeable)) {
      ((Closeable)this.httpclient).close();
    }
  }
  
  public <T> HttpRequestFutureTask<T> execute(HttpUriRequest paramHttpUriRequest, HttpContext paramHttpContext, ResponseHandler<T> paramResponseHandler)
  {
    return execute(paramHttpUriRequest, paramHttpContext, paramResponseHandler, null);
  }
  
  public <T> HttpRequestFutureTask<T> execute(HttpUriRequest paramHttpUriRequest, HttpContext paramHttpContext, ResponseHandler<T> paramResponseHandler, FutureCallback<T> paramFutureCallback)
  {
    if (this.closed.get()) {
      throw new IllegalStateException("Close has been called on this httpclient instance.");
    }
    this.metrics.getScheduledConnections().incrementAndGet();
    HttpRequestFutureTask localHttpRequestFutureTask = new HttpRequestFutureTask(paramHttpUriRequest, new HttpRequestTaskCallable(this.httpclient, paramHttpUriRequest, paramHttpContext, paramResponseHandler, paramFutureCallback, this.metrics));
    this.executorService.execute(localHttpRequestFutureTask);
    return localHttpRequestFutureTask;
  }
  
  public FutureRequestExecutionMetrics metrics()
  {
    return this.metrics;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.FutureRequestExecutionService
 * JD-Core Version:    0.7.0.1
 */