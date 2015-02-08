package org.apache.http.impl.io;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.EofSensor;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
@NotThreadSafe
public class SocketInputBuffer
  extends AbstractSessionInputBuffer
  implements EofSensor
{
  private boolean eof;
  private final Socket socket;
  
  public SocketInputBuffer(Socket paramSocket, int paramInt, HttpParams paramHttpParams)
    throws IOException
  {
    Args.notNull(paramSocket, "Socket");
    this.socket = paramSocket;
    this.eof = false;
    int i = paramInt;
    if (i < 0) {
      i = paramSocket.getReceiveBufferSize();
    }
    if (i < 1024) {
      i = 1024;
    }
    init(paramSocket.getInputStream(), i, paramHttpParams);
  }
  
  protected int fillBuffer()
    throws IOException
  {
    int i = super.fillBuffer();
    if (i == -1) {}
    for (boolean bool = true;; bool = false)
    {
      this.eof = bool;
      return i;
    }
  }
  
  public boolean isDataAvailable(int paramInt)
    throws IOException
  {
    boolean bool1 = hasBufferedData();
    int i;
    if (!bool1) {
      i = this.socket.getSoTimeout();
    }
    try
    {
      this.socket.setSoTimeout(paramInt);
      fillBuffer();
      boolean bool2 = hasBufferedData();
      bool1 = bool2;
      return bool1;
    }
    finally
    {
      this.socket.setSoTimeout(i);
    }
  }
  
  public boolean isEof()
  {
    return this.eof;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.io.SocketInputBuffer
 * JD-Core Version:    0.7.0.1
 */