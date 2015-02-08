package org.apache.http.impl.conn;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.protocol.HttpContext;

@Immutable
public class SystemDefaultRoutePlanner
  extends DefaultRoutePlanner
{
  private final ProxySelector proxySelector;
  
  public SystemDefaultRoutePlanner(ProxySelector paramProxySelector)
  {
    this(null, paramProxySelector);
  }
  
  public SystemDefaultRoutePlanner(SchemePortResolver paramSchemePortResolver, ProxySelector paramProxySelector)
  {
    super(paramSchemePortResolver);
    if (paramProxySelector != null) {}
    for (;;)
    {
      this.proxySelector = paramProxySelector;
      return;
      paramProxySelector = ProxySelector.getDefault();
    }
  }
  
  private Proxy chooseProxy(List<Proxy> paramList)
  {
    Object localObject = null;
    int i = 0;
    if ((localObject == null) && (i < paramList.size()))
    {
      Proxy localProxy = (Proxy)paramList.get(i);
      switch (1.$SwitchMap$java$net$Proxy$Type[localProxy.type().ordinal()])
      {
      }
      for (;;)
      {
        i++;
        break;
        localObject = localProxy;
      }
    }
    if (localObject == null) {
      localObject = Proxy.NO_PROXY;
    }
    return localObject;
  }
  
  private String getHost(InetSocketAddress paramInetSocketAddress)
  {
    if (paramInetSocketAddress.isUnresolved()) {
      return paramInetSocketAddress.getHostName();
    }
    return paramInetSocketAddress.getAddress().getHostAddress();
  }
  
  protected HttpHost determineProxy(HttpHost paramHttpHost, HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws HttpException
  {
    Proxy localProxy;
    try
    {
      URI localURI = new URI(paramHttpHost.toURI());
      localProxy = chooseProxy(this.proxySelector.select(localURI));
      Proxy.Type localType1 = localProxy.type();
      Proxy.Type localType2 = Proxy.Type.HTTP;
      localHttpHost = null;
      if (localType1 != localType2) {
        break label153;
      }
      if (!(localProxy.address() instanceof InetSocketAddress)) {
        throw new HttpException("Unable to handle non-Inet proxy address: " + localProxy.address());
      }
    }
    catch (URISyntaxException localURISyntaxException)
    {
      throw new HttpException("Cannot convert host to URI: " + paramHttpHost, localURISyntaxException);
    }
    InetSocketAddress localInetSocketAddress = (InetSocketAddress)localProxy.address();
    HttpHost localHttpHost = new HttpHost(getHost(localInetSocketAddress), localInetSocketAddress.getPort());
    label153:
    return localHttpHost;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.SystemDefaultRoutePlanner
 * JD-Core Version:    0.7.0.1
 */