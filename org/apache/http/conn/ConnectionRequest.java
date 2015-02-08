package org.apache.http.conn;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpClientConnection;
import org.apache.http.concurrent.Cancellable;

public abstract interface ConnectionRequest
  extends Cancellable
{
  public abstract HttpClientConnection get(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException;
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.ConnectionRequest
 * JD-Core Version:    0.7.0.1
 */