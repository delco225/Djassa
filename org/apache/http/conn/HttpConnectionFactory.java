package org.apache.http.conn;

import org.apache.http.HttpConnection;
import org.apache.http.config.ConnectionConfig;

public abstract interface HttpConnectionFactory<T, C extends HttpConnection>
{
  public abstract C create(T paramT, ConnectionConfig paramConnectionConfig);
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.HttpConnectionFactory
 * JD-Core Version:    0.7.0.1
 */