package org.apache.http.impl.client;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.message.HeaderGroup;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.Args;

@Deprecated
@NotThreadSafe
public class RequestWrapper
  extends AbstractHttpMessage
  implements HttpUriRequest
{
  private int execCount;
  private String method;
  private final HttpRequest original;
  private URI uri;
  private ProtocolVersion version;
  
  public RequestWrapper(HttpRequest paramHttpRequest)
    throws ProtocolException
  {
    Args.notNull(paramHttpRequest, "HTTP request");
    this.original = paramHttpRequest;
    setParams(paramHttpRequest.getParams());
    setHeaders(paramHttpRequest.getAllHeaders());
    if ((paramHttpRequest instanceof HttpUriRequest))
    {
      this.uri = ((HttpUriRequest)paramHttpRequest).getURI();
      this.method = ((HttpUriRequest)paramHttpRequest).getMethod();
      this.version = null;
    }
    for (;;)
    {
      this.execCount = 0;
      return;
      RequestLine localRequestLine = paramHttpRequest.getRequestLine();
      try
      {
        this.uri = new URI(localRequestLine.getUri());
        this.method = localRequestLine.getMethod();
        this.version = paramHttpRequest.getProtocolVersion();
      }
      catch (URISyntaxException localURISyntaxException)
      {
        throw new ProtocolException("Invalid request URI: " + localRequestLine.getUri(), localURISyntaxException);
      }
    }
  }
  
  public void abort()
    throws UnsupportedOperationException
  {
    throw new UnsupportedOperationException();
  }
  
  public int getExecCount()
  {
    return this.execCount;
  }
  
  public String getMethod()
  {
    return this.method;
  }
  
  public HttpRequest getOriginal()
  {
    return this.original;
  }
  
  public ProtocolVersion getProtocolVersion()
  {
    if (this.version == null) {
      this.version = HttpProtocolParams.getVersion(getParams());
    }
    return this.version;
  }
  
  public RequestLine getRequestLine()
  {
    String str1 = getMethod();
    ProtocolVersion localProtocolVersion = getProtocolVersion();
    URI localURI = this.uri;
    String str2 = null;
    if (localURI != null) {
      str2 = this.uri.toASCIIString();
    }
    if ((str2 == null) || (str2.length() == 0)) {
      str2 = "/";
    }
    return new BasicRequestLine(str1, str2, localProtocolVersion);
  }
  
  public URI getURI()
  {
    return this.uri;
  }
  
  public void incrementExecCount()
  {
    this.execCount = (1 + this.execCount);
  }
  
  public boolean isAborted()
  {
    return false;
  }
  
  public boolean isRepeatable()
  {
    return true;
  }
  
  public void resetHeaders()
  {
    this.headergroup.clear();
    setHeaders(this.original.getAllHeaders());
  }
  
  public void setMethod(String paramString)
  {
    Args.notNull(paramString, "Method name");
    this.method = paramString;
  }
  
  public void setProtocolVersion(ProtocolVersion paramProtocolVersion)
  {
    this.version = paramProtocolVersion;
  }
  
  public void setURI(URI paramURI)
  {
    this.uri = paramURI;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.RequestWrapper
 * JD-Core Version:    0.7.0.1
 */