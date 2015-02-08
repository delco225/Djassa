package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpParams;

@Deprecated
class SchemeSocketFactoryAdaptor
  implements SchemeSocketFactory
{
  private final SocketFactory factory;
  
  SchemeSocketFactoryAdaptor(SocketFactory paramSocketFactory)
  {
    this.factory = paramSocketFactory;
  }
  
  public Socket connectSocket(Socket paramSocket, InetSocketAddress paramInetSocketAddress1, InetSocketAddress paramInetSocketAddress2, HttpParams paramHttpParams)
    throws IOException, UnknownHostException, ConnectTimeoutException
  {
    String str = paramInetSocketAddress1.getHostName();
    int i = paramInetSocketAddress1.getPort();
    InetAddress localInetAddress = null;
    int j = 0;
    if (paramInetSocketAddress2 != null)
    {
      localInetAddress = paramInetSocketAddress2.getAddress();
      j = paramInetSocketAddress2.getPort();
    }
    return this.factory.connectSocket(paramSocket, str, i, localInetAddress, j, paramHttpParams);
  }
  
  public Socket createSocket(HttpParams paramHttpParams)
    throws IOException
  {
    return this.factory.createSocket();
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == null) {
      return false;
    }
    if (this == paramObject) {
      return true;
    }
    if ((paramObject instanceof SchemeSocketFactoryAdaptor)) {
      return this.factory.equals(((SchemeSocketFactoryAdaptor)paramObject).factory);
    }
    return this.factory.equals(paramObject);
  }
  
  public SocketFactory getFactory()
  {
    return this.factory;
  }
  
  public int hashCode()
  {
    return this.factory.hashCode();
  }
  
  public boolean isSecure(Socket paramSocket)
    throws IllegalArgumentException
  {
    return this.factory.isSecure(paramSocket);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.scheme.SchemeSocketFactoryAdaptor
 * JD-Core Version:    0.7.0.1
 */