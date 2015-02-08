package org.apache.http.conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;

@Immutable
public class ConnectTimeoutException
  extends InterruptedIOException
{
  private static final long serialVersionUID = -4816682903149535989L;
  private final HttpHost host = null;
  
  public ConnectTimeoutException() {}
  
  public ConnectTimeoutException(IOException paramIOException, HttpHost paramHttpHost, InetAddress... paramVarArgs) {}
  
  public ConnectTimeoutException(String paramString)
  {
    super(paramString);
  }
  
  public HttpHost getHost()
  {
    return this.host;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.ConnectTimeoutException
 * JD-Core Version:    0.7.0.1
 */