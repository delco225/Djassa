package org.apache.http.conn.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.protocol.HttpContext;

@Immutable
public class PlainConnectionSocketFactory
  implements ConnectionSocketFactory
{
  public static final PlainConnectionSocketFactory INSTANCE = new PlainConnectionSocketFactory();
  
  public static PlainConnectionSocketFactory getSocketFactory()
  {
    return INSTANCE;
  }
  
  public Socket connectSocket(int paramInt, Socket paramSocket, HttpHost paramHttpHost, InetSocketAddress paramInetSocketAddress1, InetSocketAddress paramInetSocketAddress2, HttpContext paramHttpContext)
    throws IOException
  {
    if (paramSocket != null) {}
    for (Socket localSocket = paramSocket;; localSocket = createSocket(paramHttpContext))
    {
      if (paramInetSocketAddress2 != null) {
        localSocket.bind(paramInetSocketAddress2);
      }
      try
      {
        localSocket.connect(paramInetSocketAddress1, paramInt);
        return localSocket;
      }
      catch (IOException localIOException1) {}
    }
    try
    {
      localSocket.close();
      label48:
      throw localIOException1;
    }
    catch (IOException localIOException2)
    {
      break label48;
    }
  }
  
  public Socket createSocket(HttpContext paramHttpContext)
    throws IOException
  {
    return new Socket();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.socket.PlainConnectionSocketFactory
 * JD-Core Version:    0.7.0.1
 */