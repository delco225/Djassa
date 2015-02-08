package org.apache.http.conn;

import org.apache.http.annotation.Immutable;

@Immutable
public class ConnectionPoolTimeoutException
  extends ConnectTimeoutException
{
  private static final long serialVersionUID = -7898874842020245128L;
  
  public ConnectionPoolTimeoutException() {}
  
  public ConnectionPoolTimeoutException(String paramString)
  {
    super(paramString);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.ConnectionPoolTimeoutException
 * JD-Core Version:    0.7.0.1
 */