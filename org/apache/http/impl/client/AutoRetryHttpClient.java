package org.apache.http.impl.client;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

@Deprecated
@ThreadSafe
public class AutoRetryHttpClient
  implements HttpClient
{
  private final HttpClient backend;
  private final Log log = LogFactory.getLog(getClass());
  private final ServiceUnavailableRetryStrategy retryStrategy;
  
  public AutoRetryHttpClient()
  {
    this(new DefaultHttpClient(), new DefaultServiceUnavailableRetryStrategy());
  }
  
  public AutoRetryHttpClient(HttpClient paramHttpClient)
  {
    this(paramHttpClient, new DefaultServiceUnavailableRetryStrategy());
  }
  
  public AutoRetryHttpClient(HttpClient paramHttpClient, ServiceUnavailableRetryStrategy paramServiceUnavailableRetryStrategy)
  {
    Args.notNull(paramHttpClient, "HttpClient");
    Args.notNull(paramServiceUnavailableRetryStrategy, "ServiceUnavailableRetryStrategy");
    this.backend = paramHttpClient;
    this.retryStrategy = paramServiceUnavailableRetryStrategy;
  }
  
  public AutoRetryHttpClient(ServiceUnavailableRetryStrategy paramServiceUnavailableRetryStrategy)
  {
    this(new DefaultHttpClient(), paramServiceUnavailableRetryStrategy);
  }
  
  public <T> T execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, ResponseHandler<? extends T> paramResponseHandler)
    throws IOException
  {
    return execute(paramHttpHost, paramHttpRequest, paramResponseHandler, null);
  }
  
  public <T> T execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, ResponseHandler<? extends T> paramResponseHandler, HttpContext paramHttpContext)
    throws IOException
  {
    return paramResponseHandler.handleResponse(execute(paramHttpHost, paramHttpRequest, paramHttpContext));
  }
  
  public <T> T execute(HttpUriRequest paramHttpUriRequest, ResponseHandler<? extends T> paramResponseHandler)
    throws IOException
  {
    return execute(paramHttpUriRequest, paramResponseHandler, null);
  }
  
  public <T> T execute(HttpUriRequest paramHttpUriRequest, ResponseHandler<? extends T> paramResponseHandler, HttpContext paramHttpContext)
    throws IOException
  {
    return paramResponseHandler.handleResponse(execute(paramHttpUriRequest, paramHttpContext));
  }
  
  public HttpResponse execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest)
    throws IOException
  {
    return execute(paramHttpHost, paramHttpRequest, null);
  }
  
  public HttpResponse execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws IOException
  {
    int i = 1;
    for (;;)
    {
      localHttpResponse = this.backend.execute(paramHttpHost, paramHttpRequest, paramHttpContext);
      try
      {
        if (this.retryStrategy.retryRequest(localHttpResponse, i, paramHttpContext))
        {
          EntityUtils.consume(localHttpResponse.getEntity());
          long l = this.retryStrategy.getRetryInterval();
          try
          {
            this.log.trace("Wait for " + l);
            Thread.sleep(l);
            i++;
          }
          catch (InterruptedException localInterruptedException)
          {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException();
          }
        }
        return localHttpResponse;
      }
      catch (RuntimeException localRuntimeException)
      {
        try
        {
          EntityUtils.consume(localHttpResponse.getEntity());
          throw localRuntimeException;
        }
        catch (IOException localIOException)
        {
          for (;;)
          {
            this.log.warn("I/O error consuming response content", localIOException);
          }
        }
      }
    }
  }
  
  public HttpResponse execute(HttpUriRequest paramHttpUriRequest)
    throws IOException
  {
    return execute(paramHttpUriRequest, null);
  }
  
  public HttpResponse execute(HttpUriRequest paramHttpUriRequest, HttpContext paramHttpContext)
    throws IOException
  {
    URI localURI = paramHttpUriRequest.getURI();
    return execute(new HttpHost(localURI.getHost(), localURI.getPort(), localURI.getScheme()), paramHttpUriRequest, paramHttpContext);
  }
  
  public ClientConnectionManager getConnectionManager()
  {
    return this.backend.getConnectionManager();
  }
  
  public HttpParams getParams()
  {
    return this.backend.getParams();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.AutoRetryHttpClient
 * JD-Core Version:    0.7.0.1
 */