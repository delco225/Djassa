package org.apache.http.conn;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
@Immutable
public final class MultihomePlainSocketFactory
  implements SocketFactory
{
  private static final MultihomePlainSocketFactory DEFAULT_FACTORY = new MultihomePlainSocketFactory();
  
  public static MultihomePlainSocketFactory getSocketFactory()
  {
    return DEFAULT_FACTORY;
  }
  
  public Socket connectSocket(Socket paramSocket, String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2, HttpParams paramHttpParams)
    throws IOException
  {
    Args.notNull(paramString, "Target host");
    Args.notNull(paramHttpParams, "HTTP parameters");
    Socket localSocket = paramSocket;
    if (localSocket == null) {
      localSocket = createSocket();
    }
    int i;
    Object localObject;
    Iterator localIterator;
    if ((paramInetAddress != null) || (paramInt2 > 0))
    {
      if (paramInt2 > 0) {
        localSocket.bind(new InetSocketAddress(paramInetAddress, paramInt2));
      }
    }
    else
    {
      i = HttpConnectionParams.getConnectionTimeout(paramHttpParams);
      InetAddress[] arrayOfInetAddress = InetAddress.getAllByName(paramString);
      ArrayList localArrayList = new ArrayList(arrayOfInetAddress.length);
      localArrayList.addAll(Arrays.asList(arrayOfInetAddress));
      Collections.shuffle(localArrayList);
      localObject = null;
      localIterator = localArrayList.iterator();
    }
    InetAddress localInetAddress;
    for (;;)
    {
      if (localIterator.hasNext()) {
        localInetAddress = (InetAddress)localIterator.next();
      }
      try
      {
        localSocket.connect(new InetSocketAddress(localInetAddress, paramInt1), i);
        if (localObject == null) {
          break label221;
        }
        throw localObject;
      }
      catch (SocketTimeoutException localSocketTimeoutException)
      {
        throw new ConnectTimeoutException("Connect to " + localInetAddress + " timed out");
      }
      catch (IOException localIOException)
      {
        localSocket = new Socket();
        localObject = localIOException;
      }
      paramInt2 = 0;
      break;
    }
    label221:
    return localSocket;
  }
  
  public Socket createSocket()
  {
    return new Socket();
  }
  
  public final boolean isSecure(Socket paramSocket)
    throws IllegalArgumentException
  {
    Args.notNull(paramSocket, "Socket");
    if (!paramSocket.isClosed()) {}
    for (boolean bool = true;; bool = false)
    {
      Asserts.check(bool, "Socket is closed");
      return false;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.MultihomePlainSocketFactory
 * JD-Core Version:    0.7.0.1
 */