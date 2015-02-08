package org.apache.http.client.methods;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.HttpRequest;
import org.apache.http.client.utils.CloneUtils;
import org.apache.http.concurrent.Cancellable;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionReleaseTrigger;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.HeaderGroup;
import org.apache.http.params.HttpParams;

public abstract class AbstractExecutionAwareRequest
  extends AbstractHttpMessage
  implements HttpExecutionAware, AbortableHttpRequest, Cloneable, HttpRequest
{
  private Lock abortLock = new ReentrantLock();
  private volatile boolean aborted;
  private volatile Cancellable cancellable;
  
  private void cancelExecution()
  {
    if (this.cancellable != null)
    {
      this.cancellable.cancel();
      this.cancellable = null;
    }
  }
  
  public void abort()
  {
    if (this.aborted) {
      return;
    }
    this.abortLock.lock();
    try
    {
      this.aborted = true;
      cancelExecution();
      return;
    }
    finally
    {
      this.abortLock.unlock();
    }
  }
  
  public Object clone()
    throws CloneNotSupportedException
  {
    AbstractExecutionAwareRequest localAbstractExecutionAwareRequest = (AbstractExecutionAwareRequest)super.clone();
    localAbstractExecutionAwareRequest.headergroup = ((HeaderGroup)CloneUtils.cloneObject(this.headergroup));
    localAbstractExecutionAwareRequest.params = ((HttpParams)CloneUtils.cloneObject(this.params));
    localAbstractExecutionAwareRequest.abortLock = new ReentrantLock();
    localAbstractExecutionAwareRequest.cancellable = null;
    localAbstractExecutionAwareRequest.aborted = false;
    return localAbstractExecutionAwareRequest;
  }
  
  public void completed()
  {
    this.abortLock.lock();
    try
    {
      this.cancellable = null;
      return;
    }
    finally
    {
      this.abortLock.unlock();
    }
  }
  
  public boolean isAborted()
  {
    return this.aborted;
  }
  
  public void reset()
  {
    this.abortLock.lock();
    try
    {
      cancelExecution();
      this.aborted = false;
      return;
    }
    finally
    {
      this.abortLock.unlock();
    }
  }
  
  public void setCancellable(Cancellable paramCancellable)
  {
    if (this.aborted) {
      return;
    }
    this.abortLock.lock();
    try
    {
      this.cancellable = paramCancellable;
      return;
    }
    finally
    {
      this.abortLock.unlock();
    }
  }
  
  @Deprecated
  public void setConnectionRequest(final ClientConnectionRequest paramClientConnectionRequest)
  {
    if (this.aborted) {
      return;
    }
    this.abortLock.lock();
    try
    {
      this.cancellable = new Cancellable()
      {
        public boolean cancel()
        {
          paramClientConnectionRequest.abortRequest();
          return true;
        }
      };
      return;
    }
    finally
    {
      this.abortLock.unlock();
    }
  }
  
  @Deprecated
  public void setReleaseTrigger(final ConnectionReleaseTrigger paramConnectionReleaseTrigger)
  {
    if (this.aborted) {
      return;
    }
    this.abortLock.lock();
    try
    {
      this.cancellable = new Cancellable()
      {
        public boolean cancel()
        {
          try
          {
            paramConnectionReleaseTrigger.abortConnection();
            return true;
          }
          catch (IOException localIOException) {}
          return false;
        }
      };
      return;
    }
    finally
    {
      this.abortLock.unlock();
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.methods.AbstractExecutionAwareRequest
 * JD-Core Version:    0.7.0.1
 */