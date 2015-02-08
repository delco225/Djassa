package org.apache.http.impl.client;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

@ThreadSafe
public abstract class CloseableHttpClient
  implements HttpClient, Closeable
{
  private final Log log = LogFactory.getLog(getClass());
  
  private static HttpHost determineTarget(HttpUriRequest paramHttpUriRequest)
    throws ClientProtocolException
  {
    URI localURI = paramHttpUriRequest.getURI();
    boolean bool = localURI.isAbsolute();
    HttpHost localHttpHost = null;
    if (bool)
    {
      localHttpHost = URIUtils.extractHost(localURI);
      if (localHttpHost == null) {
        throw new ClientProtocolException("URI does not specify a valid host name: " + localURI);
      }
    }
    return localHttpHost;
  }
  
  protected abstract CloseableHttpResponse doExecute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws IOException, ClientProtocolException;
  
  public <T> T execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, ResponseHandler<? extends T> paramResponseHandler)
    throws IOException, ClientProtocolException
  {
    return execute(paramHttpHost, paramHttpRequest, paramResponseHandler, null);
  }
  
  public <T> T execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, ResponseHandler<? extends T> paramResponseHandler, HttpContext paramHttpContext)
    throws IOException, ClientProtocolException
  {
    Args.notNull(paramResponseHandler, "Response handler");
    CloseableHttpResponse localCloseableHttpResponse = execute(paramHttpHost, paramHttpRequest, paramHttpContext);
    try
    {
      Object localObject = paramResponseHandler.handleResponse(localCloseableHttpResponse);
      EntityUtils.consume(localCloseableHttpResponse.getEntity());
      return localObject;
    }
    catch (Exception localException1)
    {
      HttpEntity localHttpEntity = localCloseableHttpResponse.getEntity();
      try
      {
        EntityUtils.consume(localHttpEntity);
        if ((localException1 instanceof RuntimeException)) {
          throw ((RuntimeException)localException1);
        }
      }
      catch (Exception localException2)
      {
        for (;;)
        {
          this.log.warn("Error consuming content after an exception.", localException2);
        }
        if ((localException1 instanceof IOException)) {
          throw ((IOException)localException1);
        }
        throw new UndeclaredThrowableException(localException1);
      }
    }
  }
  
  public <T> T execute(HttpUriRequest paramHttpUriRequest, ResponseHandler<? extends T> paramResponseHandler)
    throws IOException, ClientProtocolException
  {
    return execute(paramHttpUriRequest, paramResponseHandler, null);
  }
  
  public <T> T execute(HttpUriRequest paramHttpUriRequest, ResponseHandler<? extends T> paramResponseHandler, HttpContext paramHttpContext)
    throws IOException, ClientProtocolException
  {
    return execute(determineTarget(paramHttpUriRequest), paramHttpUriRequest, paramResponseHandler, paramHttpContext);
  }
  
  public CloseableHttpResponse execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest)
    throws IOException, ClientProtocolException
  {
    return doExecute(paramHttpHost, paramHttpRequest, (HttpContext)null);
  }
  
  public CloseableHttpResponse execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws IOException, ClientProtocolException
  {
    return doExecute(paramHttpHost, paramHttpRequest, paramHttpContext);
  }
  
  public CloseableHttpResponse execute(HttpUriRequest paramHttpUriRequest)
    throws IOException, ClientProtocolException
  {
    return execute(paramHttpUriRequest, (HttpContext)null);
  }
  
  public CloseableHttpResponse execute(HttpUriRequest paramHttpUriRequest, HttpContext paramHttpContext)
    throws IOException, ClientProtocolException
  {
    Args.notNull(paramHttpUriRequest, "HTTP request");
    return doExecute(determineTarget(paramHttpUriRequest), paramHttpUriRequest, paramHttpContext);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.CloseableHttpClient
 * JD-Core Version:    0.7.0.1
 */