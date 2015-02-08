package org.apache.http.client.config;

import java.net.InetAddress;
import java.util.Collection;
import org.apache.http.HttpHost;

public class RequestConfig
  implements Cloneable
{
  public static final RequestConfig DEFAULT = new Builder().build();
  private final boolean authenticationEnabled;
  private final boolean circularRedirectsAllowed;
  private final int connectTimeout;
  private final int connectionRequestTimeout;
  private final String cookieSpec;
  private final boolean expectContinueEnabled;
  private final InetAddress localAddress;
  private final int maxRedirects;
  private final HttpHost proxy;
  private final Collection<String> proxyPreferredAuthSchemes;
  private final boolean redirectsEnabled;
  private final boolean relativeRedirectsAllowed;
  private final int socketTimeout;
  private final boolean staleConnectionCheckEnabled;
  private final Collection<String> targetPreferredAuthSchemes;
  
  RequestConfig(boolean paramBoolean1, HttpHost paramHttpHost, InetAddress paramInetAddress, boolean paramBoolean2, String paramString, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, int paramInt1, boolean paramBoolean6, Collection<String> paramCollection1, Collection<String> paramCollection2, int paramInt2, int paramInt3, int paramInt4)
  {
    this.expectContinueEnabled = paramBoolean1;
    this.proxy = paramHttpHost;
    this.localAddress = paramInetAddress;
    this.staleConnectionCheckEnabled = paramBoolean2;
    this.cookieSpec = paramString;
    this.redirectsEnabled = paramBoolean3;
    this.relativeRedirectsAllowed = paramBoolean4;
    this.circularRedirectsAllowed = paramBoolean5;
    this.maxRedirects = paramInt1;
    this.authenticationEnabled = paramBoolean6;
    this.targetPreferredAuthSchemes = paramCollection1;
    this.proxyPreferredAuthSchemes = paramCollection2;
    this.connectionRequestTimeout = paramInt2;
    this.connectTimeout = paramInt3;
    this.socketTimeout = paramInt4;
  }
  
  public static Builder copy(RequestConfig paramRequestConfig)
  {
    return new Builder().setExpectContinueEnabled(paramRequestConfig.isExpectContinueEnabled()).setProxy(paramRequestConfig.getProxy()).setLocalAddress(paramRequestConfig.getLocalAddress()).setStaleConnectionCheckEnabled(paramRequestConfig.isStaleConnectionCheckEnabled()).setCookieSpec(paramRequestConfig.getCookieSpec()).setRedirectsEnabled(paramRequestConfig.isRedirectsEnabled()).setRelativeRedirectsAllowed(paramRequestConfig.isRelativeRedirectsAllowed()).setCircularRedirectsAllowed(paramRequestConfig.isCircularRedirectsAllowed()).setMaxRedirects(paramRequestConfig.getMaxRedirects()).setAuthenticationEnabled(paramRequestConfig.isAuthenticationEnabled()).setTargetPreferredAuthSchemes(paramRequestConfig.getTargetPreferredAuthSchemes()).setProxyPreferredAuthSchemes(paramRequestConfig.getProxyPreferredAuthSchemes()).setConnectionRequestTimeout(paramRequestConfig.getConnectionRequestTimeout()).setConnectTimeout(paramRequestConfig.getConnectTimeout()).setSocketTimeout(paramRequestConfig.getSocketTimeout());
  }
  
  public static Builder custom()
  {
    return new Builder();
  }
  
  protected RequestConfig clone()
    throws CloneNotSupportedException
  {
    return (RequestConfig)super.clone();
  }
  
  public int getConnectTimeout()
  {
    return this.connectTimeout;
  }
  
  public int getConnectionRequestTimeout()
  {
    return this.connectionRequestTimeout;
  }
  
  public String getCookieSpec()
  {
    return this.cookieSpec;
  }
  
  public InetAddress getLocalAddress()
  {
    return this.localAddress;
  }
  
  public int getMaxRedirects()
  {
    return this.maxRedirects;
  }
  
  public HttpHost getProxy()
  {
    return this.proxy;
  }
  
  public Collection<String> getProxyPreferredAuthSchemes()
  {
    return this.proxyPreferredAuthSchemes;
  }
  
  public int getSocketTimeout()
  {
    return this.socketTimeout;
  }
  
  public Collection<String> getTargetPreferredAuthSchemes()
  {
    return this.targetPreferredAuthSchemes;
  }
  
  public boolean isAuthenticationEnabled()
  {
    return this.authenticationEnabled;
  }
  
  public boolean isCircularRedirectsAllowed()
  {
    return this.circularRedirectsAllowed;
  }
  
  public boolean isExpectContinueEnabled()
  {
    return this.expectContinueEnabled;
  }
  
  public boolean isRedirectsEnabled()
  {
    return this.redirectsEnabled;
  }
  
  public boolean isRelativeRedirectsAllowed()
  {
    return this.relativeRedirectsAllowed;
  }
  
  public boolean isStaleConnectionCheckEnabled()
  {
    return this.staleConnectionCheckEnabled;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(", expectContinueEnabled=").append(this.expectContinueEnabled);
    localStringBuilder.append(", proxy=").append(this.proxy);
    localStringBuilder.append(", localAddress=").append(this.localAddress);
    localStringBuilder.append(", staleConnectionCheckEnabled=").append(this.staleConnectionCheckEnabled);
    localStringBuilder.append(", cookieSpec=").append(this.cookieSpec);
    localStringBuilder.append(", redirectsEnabled=").append(this.redirectsEnabled);
    localStringBuilder.append(", relativeRedirectsAllowed=").append(this.relativeRedirectsAllowed);
    localStringBuilder.append(", maxRedirects=").append(this.maxRedirects);
    localStringBuilder.append(", circularRedirectsAllowed=").append(this.circularRedirectsAllowed);
    localStringBuilder.append(", authenticationEnabled=").append(this.authenticationEnabled);
    localStringBuilder.append(", targetPreferredAuthSchemes=").append(this.targetPreferredAuthSchemes);
    localStringBuilder.append(", proxyPreferredAuthSchemes=").append(this.proxyPreferredAuthSchemes);
    localStringBuilder.append(", connectionRequestTimeout=").append(this.connectionRequestTimeout);
    localStringBuilder.append(", connectTimeout=").append(this.connectTimeout);
    localStringBuilder.append(", socketTimeout=").append(this.socketTimeout);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public static class Builder
  {
    private boolean authenticationEnabled = true;
    private boolean circularRedirectsAllowed;
    private int connectTimeout = -1;
    private int connectionRequestTimeout = -1;
    private String cookieSpec;
    private boolean expectContinueEnabled;
    private InetAddress localAddress;
    private int maxRedirects = 50;
    private HttpHost proxy;
    private Collection<String> proxyPreferredAuthSchemes;
    private boolean redirectsEnabled = true;
    private boolean relativeRedirectsAllowed = true;
    private int socketTimeout = -1;
    private boolean staleConnectionCheckEnabled = true;
    private Collection<String> targetPreferredAuthSchemes;
    
    public RequestConfig build()
    {
      return new RequestConfig(this.expectContinueEnabled, this.proxy, this.localAddress, this.staleConnectionCheckEnabled, this.cookieSpec, this.redirectsEnabled, this.relativeRedirectsAllowed, this.circularRedirectsAllowed, this.maxRedirects, this.authenticationEnabled, this.targetPreferredAuthSchemes, this.proxyPreferredAuthSchemes, this.connectionRequestTimeout, this.connectTimeout, this.socketTimeout);
    }
    
    public Builder setAuthenticationEnabled(boolean paramBoolean)
    {
      this.authenticationEnabled = paramBoolean;
      return this;
    }
    
    public Builder setCircularRedirectsAllowed(boolean paramBoolean)
    {
      this.circularRedirectsAllowed = paramBoolean;
      return this;
    }
    
    public Builder setConnectTimeout(int paramInt)
    {
      this.connectTimeout = paramInt;
      return this;
    }
    
    public Builder setConnectionRequestTimeout(int paramInt)
    {
      this.connectionRequestTimeout = paramInt;
      return this;
    }
    
    public Builder setCookieSpec(String paramString)
    {
      this.cookieSpec = paramString;
      return this;
    }
    
    public Builder setExpectContinueEnabled(boolean paramBoolean)
    {
      this.expectContinueEnabled = paramBoolean;
      return this;
    }
    
    public Builder setLocalAddress(InetAddress paramInetAddress)
    {
      this.localAddress = paramInetAddress;
      return this;
    }
    
    public Builder setMaxRedirects(int paramInt)
    {
      this.maxRedirects = paramInt;
      return this;
    }
    
    public Builder setProxy(HttpHost paramHttpHost)
    {
      this.proxy = paramHttpHost;
      return this;
    }
    
    public Builder setProxyPreferredAuthSchemes(Collection<String> paramCollection)
    {
      this.proxyPreferredAuthSchemes = paramCollection;
      return this;
    }
    
    public Builder setRedirectsEnabled(boolean paramBoolean)
    {
      this.redirectsEnabled = paramBoolean;
      return this;
    }
    
    public Builder setRelativeRedirectsAllowed(boolean paramBoolean)
    {
      this.relativeRedirectsAllowed = paramBoolean;
      return this;
    }
    
    public Builder setSocketTimeout(int paramInt)
    {
      this.socketTimeout = paramInt;
      return this;
    }
    
    public Builder setStaleConnectionCheckEnabled(boolean paramBoolean)
    {
      this.staleConnectionCheckEnabled = paramBoolean;
      return this;
    }
    
    public Builder setTargetPreferredAuthSchemes(Collection<String> paramCollection)
    {
      this.targetPreferredAuthSchemes = paramCollection;
      return this;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.config.RequestConfig
 * JD-Core Version:    0.7.0.1
 */