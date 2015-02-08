package org.apache.http.impl.conn;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
class HttpClientConnectionOperator
{
  static final String SOCKET_FACTORY_REGISTRY = "http.socket-factory-registry";
  private final DnsResolver dnsResolver;
  private final Log log = LogFactory.getLog(getClass());
  private final SchemePortResolver schemePortResolver;
  private final Lookup<ConnectionSocketFactory> socketFactoryRegistry;
  
  HttpClientConnectionOperator(Lookup<ConnectionSocketFactory> paramLookup, SchemePortResolver paramSchemePortResolver, DnsResolver paramDnsResolver)
  {
    Args.notNull(paramLookup, "Socket factory registry");
    this.socketFactoryRegistry = paramLookup;
    if (paramSchemePortResolver != null)
    {
      this.schemePortResolver = paramSchemePortResolver;
      if (paramDnsResolver == null) {
        break label53;
      }
    }
    for (;;)
    {
      this.dnsResolver = paramDnsResolver;
      return;
      paramSchemePortResolver = DefaultSchemePortResolver.INSTANCE;
      break;
      label53:
      paramDnsResolver = SystemDefaultDnsResolver.INSTANCE;
    }
  }
  
  private Lookup<ConnectionSocketFactory> getSocketFactoryRegistry(HttpContext paramHttpContext)
  {
    Lookup localLookup = (Lookup)paramHttpContext.getAttribute("http.socket-factory-registry");
    if (localLookup == null) {
      localLookup = this.socketFactoryRegistry;
    }
    return localLookup;
  }
  
  public void connect(ManagedHttpClientConnection paramManagedHttpClientConnection, HttpHost paramHttpHost, InetSocketAddress paramInetSocketAddress, int paramInt, SocketConfig paramSocketConfig, HttpContext paramHttpContext)
    throws IOException
  {
    ConnectionSocketFactory localConnectionSocketFactory = (ConnectionSocketFactory)getSocketFactoryRegistry(paramHttpContext).lookup(paramHttpHost.getSchemeName());
    if (localConnectionSocketFactory == null) {
      throw new UnsupportedSchemeException(paramHttpHost.getSchemeName() + " protocol is not supported");
    }
    InetAddress[] arrayOfInetAddress = this.dnsResolver.resolve(paramHttpHost.getHostName());
    int i = this.schemePortResolver.resolve(paramHttpHost);
    int j = 0;
    for (;;)
    {
      int k;
      Socket localSocket1;
      InetSocketAddress localInetSocketAddress;
      if (j < arrayOfInetAddress.length)
      {
        InetAddress localInetAddress = arrayOfInetAddress[j];
        if (j != -1 + arrayOfInetAddress.length) {
          break label323;
        }
        k = 1;
        localSocket1 = localConnectionSocketFactory.createSocket(paramHttpContext);
        localSocket1.setReuseAddress(paramSocketConfig.isSoReuseAddress());
        paramManagedHttpClientConnection.bind(localSocket1);
        localInetSocketAddress = new InetSocketAddress(localInetAddress, i);
        if (this.log.isDebugEnabled()) {
          this.log.debug("Connecting to " + localInetSocketAddress);
        }
      }
      try
      {
        localSocket1.setSoTimeout(paramSocketConfig.getSoTimeout());
        Socket localSocket2 = localConnectionSocketFactory.connectSocket(paramInt, localSocket1, paramHttpHost, localInetSocketAddress, paramInetSocketAddress, paramHttpContext);
        localSocket2.setTcpNoDelay(paramSocketConfig.isTcpNoDelay());
        localSocket2.setKeepAlive(paramSocketConfig.isSoKeepAlive());
        int m = paramSocketConfig.getSoLinger();
        if (m >= 0) {
          if (m <= 0) {
            break label329;
          }
        }
        label323:
        label329:
        for (boolean bool = true;; bool = false)
        {
          localSocket2.setSoLinger(bool, m);
          paramManagedHttpClientConnection.bind(localSocket2);
          if (this.log.isDebugEnabled()) {
            this.log.debug("Connection established " + paramManagedHttpClientConnection);
          }
          return;
          k = 0;
          break;
        }
      }
      catch (SocketTimeoutException localSocketTimeoutException)
      {
        if (k != 0) {
          throw new ConnectTimeoutException(localSocketTimeoutException, paramHttpHost, arrayOfInetAddress);
        }
      }
      catch (ConnectException localConnectException)
      {
        if (k != 0)
        {
          if ("Connection timed out".equals(localConnectException.getMessage())) {
            throw new ConnectTimeoutException(localConnectException, paramHttpHost, arrayOfInetAddress);
          }
          throw new HttpHostConnectException(localConnectException, paramHttpHost, arrayOfInetAddress);
        }
        if (this.log.isDebugEnabled()) {
          this.log.debug("Connect to " + localInetSocketAddress + " timed out. " + "Connection will be retried using another IP address");
        }
        j++;
      }
    }
  }
  
  public void upgrade(ManagedHttpClientConnection paramManagedHttpClientConnection, HttpHost paramHttpHost, HttpContext paramHttpContext)
    throws IOException
  {
    ConnectionSocketFactory localConnectionSocketFactory = (ConnectionSocketFactory)getSocketFactoryRegistry(HttpClientContext.adapt(paramHttpContext)).lookup(paramHttpHost.getSchemeName());
    if (localConnectionSocketFactory == null) {
      throw new UnsupportedSchemeException(paramHttpHost.getSchemeName() + " protocol is not supported");
    }
    if (!(localConnectionSocketFactory instanceof LayeredConnectionSocketFactory)) {
      throw new UnsupportedSchemeException(paramHttpHost.getSchemeName() + " protocol does not support connection upgrade");
    }
    LayeredConnectionSocketFactory localLayeredConnectionSocketFactory = (LayeredConnectionSocketFactory)localConnectionSocketFactory;
    Socket localSocket = paramManagedHttpClientConnection.getSocket();
    int i = this.schemePortResolver.resolve(paramHttpHost);
    paramManagedHttpClientConnection.bind(localLayeredConnectionSocketFactory.createLayeredSocket(localSocket, paramHttpHost.getHostName(), i, paramHttpContext));
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.HttpClientConnectionOperator
 * JD-Core Version:    0.7.0.1
 */