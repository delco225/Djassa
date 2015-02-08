package org.apache.http.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import org.apache.http.HttpInetConnection;
import org.apache.http.impl.io.SocketInputBuffer;
import org.apache.http.impl.io.SocketOutputBuffer;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
public class SocketHttpServerConnection
  extends AbstractHttpServerConnection
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
    //   1: getfield 50	org/apache/http/impl/SocketHttpServerConnection:open	Z
    //   4: ifne +4 -> 8
    //   7: return
    //   8: aload_0
    //   9: iconst_0
    //   10: putfield 50	org/apache/http/impl/SocketHttpServerConnection:open	Z
    //   13: aload_0
    //   14: iconst_0
    //   15: putfield 50	org/apache/http/impl/SocketHttpServerConnection:open	Z
    //   18: aload_0
    //   19: getfield 17	org/apache/http/impl/SocketHttpServerConnection:socket	Ljava/net/Socket;
    //   22: astore_1
    //   23: aload_0
    //   24: invokevirtual 101	org/apache/http/impl/SocketHttpServerConnection:doFlush	()V
    //   27: aload_1
    //   28: invokevirtual 106	java/net/Socket:shutdownOutput	()V
    //   31: aload_1
    //   32: invokevirtual 109	java/net/Socket:shutdownInput	()V
    //   35: aload_1
    //   36: invokevirtual 111	java/net/Socket:close	()V
    //   39: return
    //   40: astore_2
    //   41: aload_1
    //   42: invokevirtual 111	java/net/Socket:close	()V
    //   45: aload_2
    //   46: athrow
    //   47: astore 4
    //   49: goto -18 -> 31
    //   52: astore 5
    //   54: goto -19 -> 35
    //   57: astore_3
    //   58: goto -23 -> 35
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	61	0	this	SocketHttpServerConnection
    //   22	20	1	localSocket	Socket
    //   40	6	2	localObject	Object
    //   57	1	3	localUnsupportedOperationException	java.lang.UnsupportedOperationException
    //   47	1	4	localIOException1	IOException
    //   52	1	5	localIOException2	IOException
    // Exception table:
    //   from	to	target	type
    //   23	27	40	finally
    //   27	31	40	finally
    //   31	35	40	finally
    //   27	31	47	java/io/IOException
    //   31	35	52	java/io/IOException
    //   27	31	57	java/lang/UnsupportedOperationException
    //   31	35	57	java/lang/UnsupportedOperationException
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
 * Qualified Name:     org.apache.http.impl.SocketHttpServerConnection
 * JD-Core Version:    0.7.0.1
 */