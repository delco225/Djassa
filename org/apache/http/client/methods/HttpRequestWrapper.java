package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.params.HttpParams;

@NotThreadSafe
public class HttpRequestWrapper
  extends AbstractHttpMessage
  implements HttpUriRequest
{
  private final String method;
  private final HttpRequest original;
  private URI uri;
  private ProtocolVersion version;
  
  private HttpRequestWrapper(HttpRequest paramHttpRequest)
  {
    this.original = paramHttpRequest;
    this.version = this.original.getRequestLine().getProtocolVersion();
    this.method = this.original.getRequestLine().getMethod();
    if ((paramHttpRequest instanceof HttpUriRequest)) {}
    for (this.uri = ((HttpUriRequest)paramHttpRequest).getURI();; this.uri = null)
    {
      setHeaders(paramHttpRequest.getAllHeaders());
      return;
    }
  }
  
  public static HttpRequestWrapper wrap(HttpRequest paramHttpRequest)
  {
    if (paramHttpRequest == null) {
      return null;
    }
    if ((paramHttpRequest instanceof HttpEntityEnclosingRequest)) {
      return new HttpEntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)paramHttpRequest);
    }
    return new HttpRequestWrapper(paramHttpRequest);
  }
  
  public void abort()
    throws UnsupportedOperationException
  {
    throw new UnsupportedOperationException();
  }
  
  public String getMethod()
  {
    return this.method;
  }
  
  public HttpRequest getOriginal()
  {
    return this.original;
  }
  
  @Deprecated
  public HttpParams getParams()
  {
    if (this.params == null) {
      this.params = this.original.getParams().copy();
    }
    return this.params;
  }
  
  public ProtocolVersion getProtocolVersion()
  {
    if (this.version != null) {
      return this.version;
    }
    return this.original.getProtocolVersion();
  }
  
  public RequestLine getRequestLine()
  {
    if (this.uri != null) {}
    for (String str = this.uri.toASCIIString();; str = this.original.getRequestLine().getUri())
    {
      if ((str == null) || (str.length() == 0)) {
        str = "/";
      }
      return new BasicRequestLine(this.method, str, getProtocolVersion());
    }
  }
  
  public URI getURI()
  {
    return this.uri;
  }
  
  public boolean isAborted()
  {
    return false;
  }
  
  public void setProtocolVersion(ProtocolVersion paramProtocolVersion)
  {
    this.version = paramProtocolVersion;
  }
  
  public void setURI(URI paramURI)
  {
    this.uri = paramURI;
  }
  
  public String toString()
  {
    return getRequestLine() + " " + this.headergroup;
  }
  
  static class HttpEntityEnclosingRequestWrapper
    extends HttpRequestWrapper
    implements HttpEntityEnclosingRequest
  {
    private HttpEntity entity;
    
    public HttpEntityEnclosingRequestWrapper(HttpEntityEnclosingRequest paramHttpEntityEnclosingRequest)
    {
      super(null);
      this.entity = paramHttpEntityEnclosingRequest.getEntity();
    }
    
    public boolean expectContinue()
    {
      Header localHeader = getFirstHeader("Expect");
      return (localHeader != null) && ("100-continue".equalsIgnoreCase(localHeader.getValue()));
    }
    
    public HttpEntity getEntity()
    {
      return this.entity;
    }
    
    public void setEntity(HttpEntity paramHttpEntity)
    {
      this.entity = paramHttpEntity;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.methods.HttpRequestWrapper
 * JD-Core Version:    0.7.0.1
 */