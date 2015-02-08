package org.apache.http;

import java.net.InetAddress;

public abstract interface HttpInetConnection
  extends HttpConnection
{
  public abstract InetAddress getLocalAddress();
  
  public abstract int getLocalPort();
  
  public abstract InetAddress getRemoteAddress();
  
  public abstract int getRemotePort();
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.HttpInetConnection
 * JD-Core Version:    0.7.0.1
 */