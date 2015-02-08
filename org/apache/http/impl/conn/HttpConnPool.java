package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.Log;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.AbstractConnPool;
import org.apache.http.pool.ConnFactory;

@Deprecated
class HttpConnPool
  extends AbstractConnPool<HttpRoute, OperatedClientConnection, HttpPoolEntry>
{
  private static final AtomicLong COUNTER = new AtomicLong();
  private final Log log;
  private final long timeToLive;
  private final TimeUnit tunit;
  
  public HttpConnPool(Log paramLog, ClientConnectionOperator paramClientConnectionOperator, int paramInt1, int paramInt2, long paramLong, TimeUnit paramTimeUnit)
  {
    super(new InternalConnFactory(paramClientConnectionOperator), paramInt1, paramInt2);
    this.log = paramLog;
    this.timeToLive = paramLong;
    this.tunit = paramTimeUnit;
  }
  
  protected HttpPoolEntry createEntry(HttpRoute paramHttpRoute, OperatedClientConnection paramOperatedClientConnection)
  {
    String str = Long.toString(COUNTER.getAndIncrement());
    return new HttpPoolEntry(this.log, str, paramHttpRoute, paramOperatedClientConnection, this.timeToLive, this.tunit);
  }
  
  static class InternalConnFactory
    implements ConnFactory<HttpRoute, OperatedClientConnection>
  {
    private final ClientConnectionOperator connOperator;
    
    InternalConnFactory(ClientConnectionOperator paramClientConnectionOperator)
    {
      this.connOperator = paramClientConnectionOperator;
    }
    
    public OperatedClientConnection create(HttpRoute paramHttpRoute)
      throws IOException
    {
      return this.connOperator.createConnection();
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.HttpConnPool
 * JD-Core Version:    0.7.0.1
 */