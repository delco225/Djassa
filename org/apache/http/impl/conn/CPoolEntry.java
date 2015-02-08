package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.http.HttpClientConnection;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.PoolEntry;

@ThreadSafe
class CPoolEntry
  extends PoolEntry<HttpRoute, ManagedHttpClientConnection>
{
  private final Log log;
  private volatile boolean routeComplete;
  
  public CPoolEntry(Log paramLog, String paramString, HttpRoute paramHttpRoute, ManagedHttpClientConnection paramManagedHttpClientConnection, long paramLong, TimeUnit paramTimeUnit)
  {
    super(paramString, paramHttpRoute, paramManagedHttpClientConnection, paramLong, paramTimeUnit);
    this.log = paramLog;
  }
  
  public void close()
  {
    try
    {
      closeConnection();
      return;
    }
    catch (IOException localIOException)
    {
      this.log.debug("I/O error closing connection", localIOException);
    }
  }
  
  public void closeConnection()
    throws IOException
  {
    ((HttpClientConnection)getConnection()).close();
  }
  
  public boolean isClosed()
  {
    return !((HttpClientConnection)getConnection()).isOpen();
  }
  
  public boolean isExpired(long paramLong)
  {
    boolean bool = super.isExpired(paramLong);
    if ((bool) && (this.log.isDebugEnabled())) {
      this.log.debug("Connection " + this + " expired @ " + new Date(getExpiry()));
    }
    return bool;
  }
  
  public boolean isRouteComplete()
  {
    return this.routeComplete;
  }
  
  public void markRouteComplete()
  {
    this.routeComplete = true;
  }
  
  public void shutdownConnection()
    throws IOException
  {
    ((HttpClientConnection)getConnection()).shutdown();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.CPoolEntry
 * JD-Core Version:    0.7.0.1
 */