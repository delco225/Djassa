package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpServerConnection;
import org.apache.http.HttpVersion;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.ProtocolException;
import org.apache.http.RequestLine;
import org.apache.http.StatusLine;
import org.apache.http.UnsupportedHttpVersionException;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;

@Immutable
public class HttpService
{
  private volatile ConnectionReuseStrategy connStrategy = null;
  private volatile HttpExpectationVerifier expectationVerifier = null;
  private volatile HttpRequestHandlerMapper handlerMapper = null;
  private volatile HttpParams params = null;
  private volatile HttpProcessor processor = null;
  private volatile HttpResponseFactory responseFactory = null;
  
  @Deprecated
  public HttpService(HttpProcessor paramHttpProcessor, ConnectionReuseStrategy paramConnectionReuseStrategy, HttpResponseFactory paramHttpResponseFactory)
  {
    setHttpProcessor(paramHttpProcessor);
    setConnReuseStrategy(paramConnectionReuseStrategy);
    setResponseFactory(paramHttpResponseFactory);
  }
  
  public HttpService(HttpProcessor paramHttpProcessor, ConnectionReuseStrategy paramConnectionReuseStrategy, HttpResponseFactory paramHttpResponseFactory, HttpRequestHandlerMapper paramHttpRequestHandlerMapper)
  {
    this(paramHttpProcessor, paramConnectionReuseStrategy, paramHttpResponseFactory, paramHttpRequestHandlerMapper, null);
  }
  
  public HttpService(HttpProcessor paramHttpProcessor, ConnectionReuseStrategy paramConnectionReuseStrategy, HttpResponseFactory paramHttpResponseFactory, HttpRequestHandlerMapper paramHttpRequestHandlerMapper, HttpExpectationVerifier paramHttpExpectationVerifier)
  {
    this.processor = ((HttpProcessor)Args.notNull(paramHttpProcessor, "HTTP processor"));
    if (paramConnectionReuseStrategy != null)
    {
      this.connStrategy = paramConnectionReuseStrategy;
      if (paramHttpResponseFactory == null) {
        break label85;
      }
    }
    for (;;)
    {
      this.responseFactory = paramHttpResponseFactory;
      this.handlerMapper = paramHttpRequestHandlerMapper;
      this.expectationVerifier = paramHttpExpectationVerifier;
      return;
      paramConnectionReuseStrategy = DefaultConnectionReuseStrategy.INSTANCE;
      break;
      label85:
      paramHttpResponseFactory = DefaultHttpResponseFactory.INSTANCE;
    }
  }
  
  @Deprecated
  public HttpService(HttpProcessor paramHttpProcessor, ConnectionReuseStrategy paramConnectionReuseStrategy, HttpResponseFactory paramHttpResponseFactory, HttpRequestHandlerResolver paramHttpRequestHandlerResolver, HttpParams paramHttpParams)
  {
    this(paramHttpProcessor, paramConnectionReuseStrategy, paramHttpResponseFactory, new HttpRequestHandlerResolverAdapter(paramHttpRequestHandlerResolver), null);
    this.params = paramHttpParams;
  }
  
  @Deprecated
  public HttpService(HttpProcessor paramHttpProcessor, ConnectionReuseStrategy paramConnectionReuseStrategy, HttpResponseFactory paramHttpResponseFactory, HttpRequestHandlerResolver paramHttpRequestHandlerResolver, HttpExpectationVerifier paramHttpExpectationVerifier, HttpParams paramHttpParams)
  {
    this(paramHttpProcessor, paramConnectionReuseStrategy, paramHttpResponseFactory, new HttpRequestHandlerResolverAdapter(paramHttpRequestHandlerResolver), paramHttpExpectationVerifier);
    this.params = paramHttpParams;
  }
  
  public HttpService(HttpProcessor paramHttpProcessor, HttpRequestHandlerMapper paramHttpRequestHandlerMapper)
  {
    this(paramHttpProcessor, null, null, paramHttpRequestHandlerMapper, null);
  }
  
  protected void doService(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    HttpRequestHandlerMapper localHttpRequestHandlerMapper = this.handlerMapper;
    HttpRequestHandler localHttpRequestHandler = null;
    if (localHttpRequestHandlerMapper != null) {
      localHttpRequestHandler = this.handlerMapper.lookup(paramHttpRequest);
    }
    if (localHttpRequestHandler != null)
    {
      localHttpRequestHandler.handle(paramHttpRequest, paramHttpResponse, paramHttpContext);
      return;
    }
    paramHttpResponse.setStatusCode(501);
  }
  
  @Deprecated
  public HttpParams getParams()
  {
    return this.params;
  }
  
  protected void handleException(HttpException paramHttpException, HttpResponse paramHttpResponse)
  {
    if ((paramHttpException instanceof MethodNotSupportedException)) {
      paramHttpResponse.setStatusCode(501);
    }
    for (;;)
    {
      String str = paramHttpException.getMessage();
      if (str == null) {
        str = paramHttpException.toString();
      }
      ByteArrayEntity localByteArrayEntity = new ByteArrayEntity(EncodingUtils.getAsciiBytes(str));
      localByteArrayEntity.setContentType("text/plain; charset=US-ASCII");
      paramHttpResponse.setEntity(localByteArrayEntity);
      return;
      if ((paramHttpException instanceof UnsupportedHttpVersionException)) {
        paramHttpResponse.setStatusCode(505);
      } else if ((paramHttpException instanceof ProtocolException)) {
        paramHttpResponse.setStatusCode(400);
      } else {
        paramHttpResponse.setStatusCode(500);
      }
    }
  }
  
  public void handleRequest(HttpServerConnection paramHttpServerConnection, HttpContext paramHttpContext)
    throws IOException, HttpException
  {
    paramHttpContext.setAttribute("http.connection", paramHttpServerConnection);
    for (;;)
    {
      try
      {
        localHttpRequest = paramHttpServerConnection.receiveRequestHeader();
        boolean bool = localHttpRequest instanceof HttpEntityEnclosingRequest;
        localHttpResponse = null;
        if (bool)
        {
          if (!((HttpEntityEnclosingRequest)localHttpRequest).expectContinue()) {
            continue;
          }
          localHttpResponse = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_1, 100, paramHttpContext);
          HttpExpectationVerifier localHttpExpectationVerifier = this.expectationVerifier;
          if (localHttpExpectationVerifier == null) {}
        }
      }
      catch (HttpException localHttpException1)
      {
        HttpRequest localHttpRequest;
        HttpResponse localHttpResponse = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_0, 500, paramHttpContext);
        handleException(localHttpException1, localHttpResponse);
        continue;
        paramHttpServerConnection.receiveRequestEntity((HttpEntityEnclosingRequest)localHttpRequest);
        localHttpResponse = null;
        continue;
      }
      try
      {
        this.expectationVerifier.verify(localHttpRequest, localHttpResponse, paramHttpContext);
        if (localHttpResponse.getStatusLine().getStatusCode() < 200)
        {
          paramHttpServerConnection.sendResponseHeader(localHttpResponse);
          paramHttpServerConnection.flush();
          localHttpResponse = null;
          paramHttpServerConnection.receiveRequestEntity((HttpEntityEnclosingRequest)localHttpRequest);
        }
        paramHttpContext.setAttribute("http.request", localHttpRequest);
        if (localHttpResponse == null)
        {
          localHttpResponse = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_1, 200, paramHttpContext);
          this.processor.process(localHttpRequest, paramHttpContext);
          doService(localHttpRequest, localHttpResponse, paramHttpContext);
        }
        if ((localHttpRequest instanceof HttpEntityEnclosingRequest)) {
          EntityUtils.consume(((HttpEntityEnclosingRequest)localHttpRequest).getEntity());
        }
        paramHttpContext.setAttribute("http.response", localHttpResponse);
        this.processor.process(localHttpResponse, paramHttpContext);
        paramHttpServerConnection.sendResponseHeader(localHttpResponse);
        paramHttpServerConnection.sendResponseEntity(localHttpResponse);
        paramHttpServerConnection.flush();
        if (!this.connStrategy.keepAlive(localHttpResponse, paramHttpContext)) {
          paramHttpServerConnection.close();
        }
        return;
      }
      catch (HttpException localHttpException2)
      {
        localHttpResponse = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_0, 500, paramHttpContext);
        handleException(localHttpException2, localHttpResponse);
      }
    }
  }
  
  @Deprecated
  public void setConnReuseStrategy(ConnectionReuseStrategy paramConnectionReuseStrategy)
  {
    Args.notNull(paramConnectionReuseStrategy, "Connection reuse strategy");
    this.connStrategy = paramConnectionReuseStrategy;
  }
  
  @Deprecated
  public void setExpectationVerifier(HttpExpectationVerifier paramHttpExpectationVerifier)
  {
    this.expectationVerifier = paramHttpExpectationVerifier;
  }
  
  @Deprecated
  public void setHandlerResolver(HttpRequestHandlerResolver paramHttpRequestHandlerResolver)
  {
    this.handlerMapper = new HttpRequestHandlerResolverAdapter(paramHttpRequestHandlerResolver);
  }
  
  @Deprecated
  public void setHttpProcessor(HttpProcessor paramHttpProcessor)
  {
    Args.notNull(paramHttpProcessor, "HTTP processor");
    this.processor = paramHttpProcessor;
  }
  
  @Deprecated
  public void setParams(HttpParams paramHttpParams)
  {
    this.params = paramHttpParams;
  }
  
  @Deprecated
  public void setResponseFactory(HttpResponseFactory paramHttpResponseFactory)
  {
    Args.notNull(paramHttpResponseFactory, "Response factory");
    this.responseFactory = paramHttpResponseFactory;
  }
  
  @Deprecated
  private static class HttpRequestHandlerResolverAdapter
    implements HttpRequestHandlerMapper
  {
    private final HttpRequestHandlerResolver resolver;
    
    public HttpRequestHandlerResolverAdapter(HttpRequestHandlerResolver paramHttpRequestHandlerResolver)
    {
      this.resolver = paramHttpRequestHandlerResolver;
    }
    
    public HttpRequestHandler lookup(HttpRequest paramHttpRequest)
    {
      return this.resolver.lookup(paramHttpRequest.getRequestLine().getUri());
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.protocol.HttpService
 * JD-Core Version:    0.7.0.1
 */