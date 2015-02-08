package org.apache.http.client.fluent;

import java.util.concurrent.Future;
import org.apache.http.client.ResponseHandler;
import org.apache.http.concurrent.BasicFuture;
import org.apache.http.concurrent.FutureCallback;

public class Async
{
  private java.util.concurrent.Executor concurrentExec;
  private Executor executor;
  
  public static Async newInstance()
  {
    return new Async();
  }
  
  public Future<Content> execute(Request paramRequest)
  {
    return execute(paramRequest, new ContentResponseHandler(), null);
  }
  
  public <T> Future<T> execute(Request paramRequest, ResponseHandler<T> paramResponseHandler)
  {
    return execute(paramRequest, paramResponseHandler, null);
  }
  
  public <T> Future<T> execute(Request paramRequest, ResponseHandler<T> paramResponseHandler, FutureCallback<T> paramFutureCallback)
  {
    BasicFuture localBasicFuture = new BasicFuture(paramFutureCallback);
    if (this.executor != null) {}
    ExecRunnable localExecRunnable;
    for (Executor localExecutor = this.executor;; localExecutor = Executor.newInstance())
    {
      localExecRunnable = new ExecRunnable(localBasicFuture, paramRequest, localExecutor, paramResponseHandler);
      if (this.concurrentExec == null) {
        break;
      }
      this.concurrentExec.execute(localExecRunnable);
      return localBasicFuture;
    }
    Thread localThread = new Thread(localExecRunnable);
    localThread.setDaemon(true);
    localThread.start();
    return localBasicFuture;
  }
  
  public Future<Content> execute(Request paramRequest, FutureCallback<Content> paramFutureCallback)
  {
    return execute(paramRequest, new ContentResponseHandler(), paramFutureCallback);
  }
  
  public Async use(java.util.concurrent.Executor paramExecutor)
  {
    this.concurrentExec = paramExecutor;
    return this;
  }
  
  public Async use(Executor paramExecutor)
  {
    this.executor = paramExecutor;
    return this;
  }
  
  static class ExecRunnable<T>
    implements Runnable
  {
    private final Executor executor;
    private final BasicFuture<T> future;
    private final ResponseHandler<T> handler;
    private final Request request;
    
    ExecRunnable(BasicFuture<T> paramBasicFuture, Request paramRequest, Executor paramExecutor, ResponseHandler<T> paramResponseHandler)
    {
      this.future = paramBasicFuture;
      this.request = paramRequest;
      this.executor = paramExecutor;
      this.handler = paramResponseHandler;
    }
    
    public void run()
    {
      try
      {
        Object localObject = this.executor.execute(this.request).handleResponse(this.handler);
        this.future.completed(localObject);
        return;
      }
      catch (Exception localException)
      {
        this.future.failed(localException);
      }
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.fluent.Async
 * JD-Core Version:    0.7.0.1
 */