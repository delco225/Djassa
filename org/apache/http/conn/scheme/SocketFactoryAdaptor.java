package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

@Deprecated
class SocketFactoryAdaptor
  implements SocketFactory
{
  private final SchemeSocketFactory factory;
  
  SocketFactoryAdaptor(SchemeSocketFactory paramSchemeSocketFactory)
  {
    this.factory = paramSchemeSocketFactory;
  }
  
  public Socket connectSocket(Socket paramSocket, String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2, HttpParams paramHttpParams)
    throws IOException, UnknownHostException, ConnectTimeoutException
  {
    InetSocketAddress localInetSocketAddress1;
    if (paramInetAddress == null)
    {
      localInetSocketAddress1 = null;
      if (paramInt2 <= 0) {}
    }
    else
    {
      if (paramInt2 <= 0) {
        break label62;
      }
    }
    for (;;)
    {
      localInetSocketAddress1 = new InetSocketAddress(paramInetAddress, paramInt2);
      InetSocketAddress localInetSocketAddress2 = new InetSocketAddress(InetAddress.getByName(paramString), paramInt1);
      return this.factory.connectSocket(paramSocket, localInetSocketAddress2, localInetSocketAddress1, paramHttpParams);
      label62:
      paramInt2 = 0;
    }
  }
  
  public Socket createSocket()
    throws IOException
  {
    BasicHttpParams localBasicHttpParams = new BasicHttpParams();
    return this.factory.createSocket(localBasicHttpParams);
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == null) {
      return false;
    }
    if (this == paramObject) {
      return true;
    }
    if ((paramObject instanceof SocketFactoryAdaptor)) {
      return this.factory.equals(((SocketFactoryAdaptor)paramObject).factory);
    }
    return this.factory.equals(paramObject);
  }
  
  public SchemeSocketFactory getFactory()
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
 * Qualified Name:     org.apache.http.conn.scheme.SocketFactoryAdaptor
 * JD-Core Version:    0.7.0.1
 */