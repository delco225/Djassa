package org.apache.http.impl;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
@NotThreadSafe
public class DefaultHttpClientConnection
  extends SocketHttpClientConnection
{
  public void bind(Socket paramSocket, HttpParams paramHttpParams)
    throws IOException
  {
    boolean bool = true;
    Args.notNull(paramSocket, "Socket");
    Args.notNull(paramHttpParams, "HTTP parameters");
    assertNotOpen();
    paramSocket.setTcpNoDelay(paramHttpParams.getBooleanParameter("http.tcp.nodelay", bool));
    paramSocket.setSoTimeout(paramHttpParams.getIntParameter("http.socket.timeout", 0));
    paramSocket.setKeepAlive(paramHttpParams.getBooleanParameter("http.socket.keepalive", false));
    int i = paramHttpParams.getIntParameter("http.socket.linger", -1);
    if (i >= 0) {
      if (i <= 0) {
        break label94;
      }
    }
    for (;;)
    {
      paramSocket.setSoLinger(bool, i);
      super.bind(paramSocket, paramHttpParams);
      return;
      label94:
      bool = false;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.DefaultHttpClientConnection
 * JD-Core Version:    0.7.0.1
 */