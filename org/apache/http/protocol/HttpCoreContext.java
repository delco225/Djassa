package org.apache.http.protocol;

import org.apache.http.HttpConnection;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@NotThreadSafe
public class HttpCoreContext
  implements HttpContext
{
  public static final String HTTP_CONNECTION = "http.connection";
  public static final String HTTP_REQUEST = "http.request";
  public static final String HTTP_REQ_SENT = "http.request_sent";
  public static final String HTTP_RESPONSE = "http.response";
  public static final String HTTP_TARGET_HOST = "http.target_host";
  private final HttpContext context;
  
  public HttpCoreContext()
  {
    this.context = new BasicHttpContext();
  }
  
  public HttpCoreContext(HttpContext paramHttpContext)
  {
    this.context = paramHttpContext;
  }
  
  public static HttpCoreContext adapt(HttpContext paramHttpContext)
  {
    Args.notNull(paramHttpContext, "HTTP context");
    if ((paramHttpContext instanceof HttpCoreContext)) {
      return (HttpCoreContext)paramHttpContext;
    }
    return new HttpCoreContext(paramHttpContext);
  }
  
  public static HttpCoreContext create()
  {
    return new HttpCoreContext(new BasicHttpContext());
  }
  
  public Object getAttribute(String paramString)
  {
    return this.context.getAttribute(paramString);
  }
  
  public <T> T getAttribute(String paramString, Class<T> paramClass)
  {
    Args.notNull(paramClass, "Attribute class");
    Object localObject = getAttribute(paramString);
    if (localObject == null) {
      return null;
    }
    return paramClass.cast(localObject);
  }
  
  public HttpConnection getConnection()
  {
    return (HttpConnection)getAttribute("http.connection", HttpConnection.class);
  }
  
  public <T extends HttpConnection> T getConnection(Class<T> paramClass)
  {
    return (HttpConnection)getAttribute("http.connection", paramClass);
  }
  
  public HttpRequest getRequest()
  {
    return (HttpRequest)getAttribute("http.request", HttpRequest.class);
  }
  
  public HttpResponse getResponse()
  {
    return (HttpResponse)getAttribute("http.response", HttpResponse.class);
  }
  
  public HttpHost getTargetHost()
  {
    return (HttpHost)getAttribute("http.target_host", HttpHost.class);
  }
  
  public boolean isRequestSent()
  {
    Boolean localBoolean = (Boolean)getAttribute("http.request_sent", Boolean.class);
    return (localBoolean != null) && (localBoolean.booleanValue());
  }
  
  public Object removeAttribute(String paramString)
  {
    return this.context.removeAttribute(paramString);
  }
  
  public void setAttribute(String paramString, Object paramObject)
  {
    this.context.setAttribute(paramString, paramObject);
  }
  
  public void setTargetHost(HttpHost paramHttpHost)
  {
    setAttribute("http.target_host", paramHttpHost);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.protocol.HttpCoreContext
 * JD-Core Version:    0.7.0.1
 */