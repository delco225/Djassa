package org.apache.http.impl.pool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultBHttpClientConnectionFactory;
import org.apache.http.params.HttpParamConfig;
import org.apache.http.params.HttpParams;
import org.apache.http.pool.ConnFactory;
import org.apache.http.util.Args;

@Immutable
public class BasicConnFactory
  implements ConnFactory<HttpHost, HttpClientConnection>
{
  private final HttpConnectionFactory<? extends HttpClientConnection> connFactory;
  private final int connectTimeout;
  private final SocketFactory plainfactory;
  private final SocketConfig sconfig;
  private final SSLSocketFactory sslfactory;
  
  public BasicConnFactory()
  {
    this(null, null, 0, SocketConfig.DEFAULT, ConnectionConfig.DEFAULT);
  }
  
  public BasicConnFactory(int paramInt, SocketConfig paramSocketConfig, ConnectionConfig paramConnectionConfig)
  {
    this(null, null, paramInt, paramSocketConfig, paramConnectionConfig);
  }
  
  public BasicConnFactory(SocketFactory paramSocketFactory, SSLSocketFactory paramSSLSocketFactory, int paramInt, SocketConfig paramSocketConfig, ConnectionConfig paramConnectionConfig)
  {
    this.plainfactory = paramSocketFactory;
    this.sslfactory = paramSSLSocketFactory;
    this.connectTimeout = paramInt;
    if (paramSocketConfig != null)
    {
      this.sconfig = paramSocketConfig;
      if (paramConnectionConfig == null) {
        break label57;
      }
    }
    for (;;)
    {
      this.connFactory = new DefaultBHttpClientConnectionFactory(paramConnectionConfig);
      return;
      paramSocketConfig = SocketConfig.DEFAULT;
      break;
      label57:
      paramConnectionConfig = ConnectionConfig.DEFAULT;
    }
  }
  
  @Deprecated
  public BasicConnFactory(SSLSocketFactory paramSSLSocketFactory, HttpParams paramHttpParams)
  {
    Args.notNull(paramHttpParams, "HTTP params");
    this.plainfactory = null;
    this.sslfactory = paramSSLSocketFactory;
    this.connectTimeout = paramHttpParams.getIntParameter("http.connection.timeout", 0);
    this.sconfig = HttpParamConfig.getSocketConfig(paramHttpParams);
    this.connFactory = new DefaultBHttpClientConnectionFactory(HttpParamConfig.getConnectionConfig(paramHttpParams));
  }
  
  public BasicConnFactory(SocketConfig paramSocketConfig, ConnectionConfig paramConnectionConfig)
  {
    this(null, null, 0, paramSocketConfig, paramConnectionConfig);
  }
  
  @Deprecated
  public BasicConnFactory(HttpParams paramHttpParams)
  {
    this(null, paramHttpParams);
  }
  
  @Deprecated
  protected HttpClientConnection create(Socket paramSocket, HttpParams paramHttpParams)
    throws IOException
  {
    DefaultBHttpClientConnection localDefaultBHttpClientConnection = new DefaultBHttpClientConnection(paramHttpParams.getIntParameter("http.socket.buffer-size", 8192));
    localDefaultBHttpClientConnection.bind(paramSocket);
    return localDefaultBHttpClientConnection;
  }
  
  public HttpClientConnection create(HttpHost paramHttpHost)
    throws IOException
  {
    String str1 = paramHttpHost.getSchemeName();
    boolean bool1 = "http".equalsIgnoreCase(str1);
    Socket localSocket = null;
    if (bool1)
    {
      if (this.plainfactory != null) {
        localSocket = this.plainfactory.createSocket();
      }
    }
    else if ("https".equalsIgnoreCase(str1)) {
      if (this.sslfactory == null) {
        break label108;
      }
    }
    label108:
    for (Object localObject = this.sslfactory;; localObject = SSLSocketFactory.getDefault())
    {
      localSocket = ((SocketFactory)localObject).createSocket();
      if (localSocket != null) {
        break label116;
      }
      throw new IOException(str1 + " scheme is not supported");
      localSocket = new Socket();
      break;
    }
    label116:
    String str2 = paramHttpHost.getHostName();
    int i = paramHttpHost.getPort();
    int j;
    if (i == -1)
    {
      if (paramHttpHost.getSchemeName().equalsIgnoreCase("http")) {
        i = 80;
      }
    }
    else
    {
      localSocket.setSoTimeout(this.sconfig.getSoTimeout());
      localSocket.connect(new InetSocketAddress(str2, i), this.connectTimeout);
      localSocket.setTcpNoDelay(this.sconfig.isTcpNoDelay());
      j = this.sconfig.getSoLinger();
      if (j >= 0) {
        if (j <= 0) {
          break label272;
        }
      }
    }
    label272:
    for (boolean bool2 = true;; bool2 = false)
    {
      localSocket.setSoLinger(bool2, j);
      localSocket.setKeepAlive(this.sconfig.isSoKeepAlive());
      return (HttpClientConnection)this.connFactory.createConnection(localSocket);
      if (!paramHttpHost.getSchemeName().equalsIgnoreCase("https")) {
        break;
      }
      i = 443;
      break;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.pool.BasicConnFactory
 * JD-Core Version:    0.7.0.1
 */