package org.apache.http;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Locale;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

@Immutable
public final class HttpHost
  implements Cloneable, Serializable
{
  public static final String DEFAULT_SCHEME_NAME = "http";
  private static final long serialVersionUID = -7529410654042457626L;
  protected final InetAddress address;
  protected final String hostname;
  protected final String lcHostname;
  protected final int port;
  protected final String schemeName;
  
  public HttpHost(String paramString)
  {
    this(paramString, -1, null);
  }
  
  public HttpHost(String paramString, int paramInt)
  {
    this(paramString, paramInt, null);
  }
  
  public HttpHost(String paramString1, int paramInt, String paramString2)
  {
    this.hostname = ((String)Args.notBlank(paramString1, "Host name"));
    this.lcHostname = paramString1.toLowerCase(Locale.ENGLISH);
    if (paramString2 != null) {}
    for (this.schemeName = paramString2.toLowerCase(Locale.ENGLISH);; this.schemeName = "http")
    {
      this.port = paramInt;
      this.address = null;
      return;
    }
  }
  
  public HttpHost(InetAddress paramInetAddress)
  {
    this(paramInetAddress, -1, null);
  }
  
  public HttpHost(InetAddress paramInetAddress, int paramInt)
  {
    this(paramInetAddress, paramInt, null);
  }
  
  public HttpHost(InetAddress paramInetAddress, int paramInt, String paramString)
  {
    this.address = ((InetAddress)Args.notNull(paramInetAddress, "Inet address"));
    this.hostname = paramInetAddress.getHostAddress();
    this.lcHostname = this.hostname.toLowerCase(Locale.ENGLISH);
    if (paramString != null) {}
    for (this.schemeName = paramString.toLowerCase(Locale.ENGLISH);; this.schemeName = "http")
    {
      this.port = paramInt;
      return;
    }
  }
  
  public HttpHost(HttpHost paramHttpHost)
  {
    Args.notNull(paramHttpHost, "HTTP host");
    this.hostname = paramHttpHost.hostname;
    this.lcHostname = paramHttpHost.lcHostname;
    this.schemeName = paramHttpHost.schemeName;
    this.port = paramHttpHost.port;
    this.address = paramHttpHost.address;
  }
  
  public Object clone()
    throws CloneNotSupportedException
  {
    return super.clone();
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {}
    HttpHost localHttpHost;
    do
    {
      return true;
      if (!(paramObject instanceof HttpHost)) {
        break;
      }
      localHttpHost = (HttpHost)paramObject;
    } while ((this.lcHostname.equals(localHttpHost.lcHostname)) && (this.port == localHttpHost.port) && (this.schemeName.equals(localHttpHost.schemeName)));
    return false;
    return false;
  }
  
  public InetAddress getAddress()
  {
    return this.address;
  }
  
  public String getHostName()
  {
    return this.hostname;
  }
  
  public int getPort()
  {
    return this.port;
  }
  
  public String getSchemeName()
  {
    return this.schemeName;
  }
  
  public int hashCode()
  {
    return LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(17, this.lcHostname), this.port), this.schemeName);
  }
  
  public String toHostString()
  {
    if (this.port != -1)
    {
      StringBuilder localStringBuilder = new StringBuilder(6 + this.hostname.length());
      localStringBuilder.append(this.hostname);
      localStringBuilder.append(":");
      localStringBuilder.append(Integer.toString(this.port));
      return localStringBuilder.toString();
    }
    return this.hostname;
  }
  
  public String toString()
  {
    return toURI();
  }
  
  public String toURI()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(this.schemeName);
    localStringBuilder.append("://");
    localStringBuilder.append(this.hostname);
    if (this.port != -1)
    {
      localStringBuilder.append(':');
      localStringBuilder.append(Integer.toString(this.port));
    }
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.HttpHost
 * JD-Core Version:    0.7.0.1
 */