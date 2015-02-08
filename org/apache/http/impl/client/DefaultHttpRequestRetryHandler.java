package org.apache.http.impl.client;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.net.ssl.SSLException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class DefaultHttpRequestRetryHandler
  implements HttpRequestRetryHandler
{
  public static final DefaultHttpRequestRetryHandler INSTANCE = new DefaultHttpRequestRetryHandler();
  private final Set<Class<? extends IOException>> nonRetriableClasses;
  private final boolean requestSentRetryEnabled;
  private final int retryCount;
  
  public DefaultHttpRequestRetryHandler()
  {
    this(3, false);
  }
  
  public DefaultHttpRequestRetryHandler(int paramInt, boolean paramBoolean)
  {
    this(paramInt, paramBoolean, Arrays.asList(new Class[] { InterruptedIOException.class, UnknownHostException.class, ConnectException.class, SSLException.class }));
  }
  
  protected DefaultHttpRequestRetryHandler(int paramInt, boolean paramBoolean, Collection<Class<? extends IOException>> paramCollection)
  {
    this.retryCount = paramInt;
    this.requestSentRetryEnabled = paramBoolean;
    this.nonRetriableClasses = new HashSet();
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      Class localClass = (Class)localIterator.next();
      this.nonRetriableClasses.add(localClass);
    }
  }
  
  public int getRetryCount()
  {
    return this.retryCount;
  }
  
  protected boolean handleAsIdempotent(HttpRequest paramHttpRequest)
  {
    return !(paramHttpRequest instanceof HttpEntityEnclosingRequest);
  }
  
  public boolean isRequestSentRetryEnabled()
  {
    return this.requestSentRetryEnabled;
  }
  
  @Deprecated
  protected boolean requestIsAborted(HttpRequest paramHttpRequest)
  {
    HttpRequest localHttpRequest = paramHttpRequest;
    if ((paramHttpRequest instanceof RequestWrapper)) {
      localHttpRequest = ((RequestWrapper)paramHttpRequest).getOriginal();
    }
    return ((localHttpRequest instanceof HttpUriRequest)) && (((HttpUriRequest)localHttpRequest).isAborted());
  }
  
  public boolean retryRequest(IOException paramIOException, int paramInt, HttpContext paramHttpContext)
  {
    Args.notNull(paramIOException, "Exception parameter");
    Args.notNull(paramHttpContext, "HTTP context");
    if (paramInt > this.retryCount) {}
    HttpClientContext localHttpClientContext;
    do
    {
      HttpRequest localHttpRequest;
      do
      {
        do
        {
          return false;
        } while (this.nonRetriableClasses.contains(paramIOException.getClass()));
        Iterator localIterator = this.nonRetriableClasses.iterator();
        while (localIterator.hasNext()) {
          if (((Class)localIterator.next()).isInstance(paramIOException)) {
            return false;
          }
        }
        localHttpClientContext = HttpClientContext.adapt(paramHttpContext);
        localHttpRequest = localHttpClientContext.getRequest();
      } while (requestIsAborted(localHttpRequest));
      if (handleAsIdempotent(localHttpRequest)) {
        return true;
      }
    } while ((localHttpClientContext.isRequestSent()) && (!this.requestSentRetryEnabled));
    return true;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.DefaultHttpRequestRetryHandler
 * JD-Core Version:    0.7.0.1
 */