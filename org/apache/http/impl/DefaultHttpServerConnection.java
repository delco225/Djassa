package org.apache.http.impl;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
@NotThreadSafe
public class DefaultHttpServerConnection
  extends SocketHttpServerConnection
{
  public void bind(Socket paramSocket, HttpParams paramHttpParams)
    throws IOException
  {
    boolean bool1 = true;
    Args.notNull(paramSocket, "Socket");
    Args.notNull(paramHttpParams, "HTTP parameters");
    assertNotOpen();
    paramSocket.setTcpNoDelay(paramHttpParams.getBooleanParameter("http.tcp.nodelay", bool1));
    paramSocket.setSoTimeout(paramHttpParams.getIntParameter("http.socket.timeout", 0));
    paramSocket.setKeepAlive(paramHttpParams.getBooleanParameter("http.socket.keepalive", false));
    int i = paramHttpParams.getIntParameter("http.socket.linger", -1);
    boolean bool2;
    if (i >= 0)
    {
      if (i > 0)
      {
        bool2 = bool1;
        paramSocket.setSoLinger(bool2, i);
      }
    }
    else if (i >= 0) {
      if (i <= 0) {
        break label121;
      }
    }
    for (;;)
    {
      paramSocket.setSoLinger(bool1, i);
      super.bind(paramSocket, paramHttpParams);
      return;
      bool2 = false;
      break;
      label121:
      bool1 = false;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.DefaultHttpServerConnection
 * JD-Core Version:    0.7.0.1
 */