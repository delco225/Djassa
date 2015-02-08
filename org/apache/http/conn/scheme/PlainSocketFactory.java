package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
@Immutable
public class PlainSocketFactory
  implements SocketFactory, SchemeSocketFactory
{
  private final HostNameResolver nameResolver;
  
  public PlainSocketFactory()
  {
    this.nameResolver = null;
  }
  
  @Deprecated
  public PlainSocketFactory(HostNameResolver paramHostNameResolver)
  {
    this.nameResolver = paramHostNameResolver;
  }
  
  public static PlainSocketFactory getSocketFactory()
  {
    return new PlainSocketFactory();
  }
  
  @Deprecated
  public Socket connectSocket(Socket paramSocket, String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2, HttpParams paramHttpParams)
    throws IOException, UnknownHostException, ConnectTimeoutException
  {
    InetSocketAddress localInetSocketAddress;
    if (paramInetAddress == null)
    {
      localInetSocketAddress = null;
      if (paramInt2 <= 0) {}
    }
    else
    {
      if (paramInt2 <= 0) {
        break label70;
      }
      localInetSocketAddress = new InetSocketAddress(paramInetAddress, paramInt2);
    }
    if (this.nameResolver != null) {}
    for (InetAddress localInetAddress = this.nameResolver.resolve(paramString);; localInetAddress = InetAddress.getByName(paramString))
    {
      return connectSocket(paramSocket, new InetSocketAddress(localInetAddress, paramInt1), localInetSocketAddress, paramHttpParams);
      label70:
      paramInt2 = 0;
      break;
    }
  }
  
  public Socket connectSocket(Socket paramSocket, InetSocketAddress paramInetSocketAddress1, InetSocketAddress paramInetSocketAddress2, HttpParams paramHttpParams)
    throws IOException, ConnectTimeoutException
  {
    Args.notNull(paramInetSocketAddress1, "Remote address");
    Args.notNull(paramHttpParams, "HTTP parameters");
    Socket localSocket = paramSocket;
    if (localSocket == null) {
      localSocket = createSocket();
    }
    if (paramInetSocketAddress2 != null)
    {
      localSocket.setReuseAddress(HttpConnectionParams.getSoReuseaddr(paramHttpParams));
      localSocket.bind(paramInetSocketAddress2);
    }
    int i = HttpConnectionParams.getConnectionTimeout(paramHttpParams);
    int j = HttpConnectionParams.getSoTimeout(paramHttpParams);
    try
    {
      localSocket.setSoTimeout(j);
      localSocket.connect(paramInetSocketAddress1, i);
      return localSocket;
    }
    catch (SocketTimeoutException localSocketTimeoutException)
    {
      throw new ConnectTimeoutException("Connect to " + paramInetSocketAddress1 + " timed out");
    }
  }
  
  public Socket createSocket()
  {
    return new Socket();
  }
  
  public Socket createSocket(HttpParams paramHttpParams)
  {
    return new Socket();
  }
  
  public final boolean isSecure(Socket paramSocket)
  {
    return false;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.scheme.PlainSocketFactory
 * JD-Core Version:    0.7.0.1
 */