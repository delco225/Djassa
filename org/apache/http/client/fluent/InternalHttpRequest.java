package org.apache.http.client.fluent;

import java.net.URI;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.Configurable;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.util.Args;

@NotThreadSafe
class InternalHttpRequest
  extends AbstractHttpMessage
  implements HttpUriRequest, Configurable
{
  private RequestConfig config;
  private final String method;
  private URI uri;
  private ProtocolVersion version;
  
  InternalHttpRequest(String paramString, URI paramURI)
  {
    Args.notBlank(paramString, "Method");
    Args.notNull(paramURI, "Request URI");
    this.method = paramString;
    this.uri = paramURI;
  }
  
  public void abort()
    throws UnsupportedOperationException
  {}
  
  public RequestConfig getConfig()
  {
    return this.config;
  }
  
  public String getMethod()
  {
    return this.method;
  }
  
  public ProtocolVersion getProtocolVersion()
  {
    if (this.version != null) {
      return this.version;
    }
    return HttpVersion.HTTP_1_1;
  }
  
  public RequestLine getRequestLine()
  {
    String str1 = getMethod();
    ProtocolVersion localProtocolVersion = getProtocolVersion();
    URI localURI = getURI();
    String str2 = null;
    if (localURI != null) {
      str2 = localURI.toASCIIString();
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
  
  public boolean isAborted()
  {
    return false;
  }
  
  public void setConfig(RequestConfig paramRequestConfig)
  {
    this.config = paramRequestConfig;
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
    return getMethod() + " " + getURI() + " " + getProtocolVersion();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.fluent.InternalHttpRequest
 * JD-Core Version:    0.7.0.1
 */