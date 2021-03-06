package org.apache.http.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import org.apache.http.HttpInetConnection;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.impl.io.SocketInputBuffer;
import org.apache.http.impl.io.SocketOutputBuffer;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
@NotThreadSafe
public class SocketHttpClientConnection
  extends AbstractHttpClientConnection
  implements HttpInetConnection
{
  private volatile boolean open;
  private volatile Socket socket = null;
  
  private static void formatAddress(StringBuilder paramStringBuilder, SocketAddress paramSocketAddress)
  {
    if ((paramSocketAddress instanceof InetSocketAddress))
    {
      InetSocketAddress localInetSocketAddress = (InetSocketAddress)paramSocketAddress;
      if (localInetSocketAddress.getAddress() != null) {}
      for (Object localObject = localInetSocketAddress.getAddress().getHostAddress();; localObject = localInetSocketAddress.getAddress())
      {
        paramStringBuilder.append(localObject).append(':').append(localInetSocketAddress.getPort());
        return;
      }
    }
    paramStringBuilder.append(paramSocketAddress);
  }
  
  protected void assertNotOpen()
  {
    if (!this.open) {}
    for (boolean bool = true;; bool = false)
    {
      Asserts.check(bool, "Connection is already open");
      return;
    }
  }
  
  protected void assertOpen()
  {
    Asserts.check(this.open, "Connection is not open");
  }
  
  protected void bind(Socket paramSocket, HttpParams paramHttpParams)
    throws IOException
  {
    Args.notNull(paramSocket, "Socket");
    Args.notNull(paramHttpParams, "HTTP parameters");
    this.socket = paramSocket;
    int i = paramHttpParams.getIntParameter("http.socket.buffer-size", -1);
    init(createSessionInputBuffer(paramSocket, i, paramHttpParams), createSessionOutputBuffer(paramSocket, i, paramHttpParams), paramHttpParams);
    this.open = true;
  }
  
  /* Error */
  public void close()
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 51	org/apache/http/impl/SocketHttpClientConnection:open	Z
    //   4: ifne +4 -> 8
    //   7: return
    //   8: aload_0
    //   9: iconst_0
    //   10: putfield 51	org/apache/http/impl/SocketHttpClientConnection:open	Z
    //   13: aload_0
    //   14: getfield 18	org/apache/http/impl/SocketHttpClientConnection:socket	Ljava/net/Socket;
    //   17: astore_1
    //   18: aload_0
    //   19: invokevirtual 102	org/apache/http/impl/SocketHttpClientConnection:doFlush	()V
    //   22: aload_1
    //   23: invokevirtual 107	java/net/Socket:shutdownOutput	()V
    //   26: aload_1
    //   27: invokevirtual 110	java/net/Socket:shutdownInput	()V
    //   30: aload_1
    //   31: invokevirtual 112	java/net/Socket:close	()V
    //   34: return
    //   35: astore_2
    //   36: aload_1
    //   37: invokevirtual 112	java/net/Socket:close	()V
    //   40: aload_2
    //   41: athrow
    //   42: astore 4
    //   44: goto -18 -> 26
    //   47: astore 5
    //   49: goto -19 -> 30
    //   52: astore_3
    //   53: goto -23 -> 30
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	56	0	this	SocketHttpClientConnection
    //   17	20	1	localSocket	Socket
    //   35	6	2	localObject	Object
    //   52	1	3	localUnsupportedOperationException	java.lang.UnsupportedOperationException
    //   42	1	4	localIOException1	IOException
    //   47	1	5	localIOException2	IOException
    // Exception table:
    //   from	to	target	type
    //   18	22	35	finally
    //   22	26	35	finally
    //   26	30	35	finally
    //   22	26	42	java/io/IOException
    //   26	30	47	java/io/IOException
    //   22	26	52	java/lang/UnsupportedOperationException
    //   26	30	52	java/lang/UnsupportedOperationException
  }
  
  protected SessionInputBuffer createSessionInputBuffer(Socket paramSocket, int paramInt, HttpParams paramHttpParams)
    throws IOException
  {
    return new SocketInputBuffer(paramSocket, paramInt, paramHttpParams);
  }
  
  protected SessionOutputBuffer createSessionOutputBuffer(Socket paramSocket, int paramInt, HttpParams paramHttpParams)
    throws IOException
  {
    return new SocketOutputBuffer(paramSocket, paramInt, paramHttpParams);
  }
  
  public InetAddress getLocalAddress()
  {
    if (this.socket != null) {
      return this.socket.getLocalAddress();
    }
    return null;
  }
  
  public int getLocalPort()
  {
    if (this.socket != null) {
      return this.socket.getLocalPort();
    }
    return -1;
  }
  
  public InetAddress getRemoteAddress()
  {
    if (this.socket != null) {
      return this.socket.getInetAddress();
    }
    return null;
  }
  
  public int getRemotePort()
  {
    if (this.socket != null) {
      return this.socket.getPort();
    }
    return -1;
  }
  
  protected Socket getSocket()
  {
    return this.socket;
  }
  
  public int getSocketTimeout()
  {
    int i = -1;
    if (this.socket != null) {}
    try
    {
      int j = this.socket.getSoTimeout();
      i = j;
      return i;
    }
    catch (SocketException localSocketException) {}
    return i;
  }
  
  public boolean isOpen()
  {
    return this.open;
  }
  
  public void setSocketTimeout(int paramInt)
  {
    assertOpen();
    if (this.socket != null) {}
    try
    {
      this.socket.setSoTimeout(paramInt);
      return;
    }
    catch (SocketException localSocketException) {}
  }
  
  public void shutdown()
    throws IOException
  {
    this.open = false;
    Socket localSocket = this.socket;
    if (localSocket != null) {
      localSocket.close();
    }
  }
  
  public String toString()
  {
    if (this.socket != null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      SocketAddress localSocketAddress1 = this.socket.getRemoteSocketAddress();
      SocketAddress localSocketAddress2 = this.socket.getLocalSocketAddress();
      if ((localSocketAddress1 != null) && (localSocketAddress2 != null))
      {
        formatAddress(localStringBuilder, localSocketAddress2);
        localStringBuilder.append("<->");
        formatAddress(localStringBuilder, localSocketAddress1);
      }
      return localStringBuilder.toString();
    }
    return super.toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.SocketHttpClientConnection
 * JD-Core Version:    0.7.0.1
 */