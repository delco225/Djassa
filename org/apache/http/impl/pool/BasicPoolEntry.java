package org.apache.http.impl.pool;

import java.io.IOException;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.pool.PoolEntry;

@ThreadSafe
public class BasicPoolEntry
  extends PoolEntry<HttpHost, HttpClientConnection>
{
  public BasicPoolEntry(String paramString, HttpHost paramHttpHost, HttpClientConnection paramHttpClientConnection)
  {
    super(paramString, paramHttpHost, paramHttpClientConnection);
  }
  
  public void close()
  {
    try
    {
      ((HttpClientConnection)getConnection()).close();
      return;
    }
    catch (IOException localIOException) {}
  }
  
  public boolean isClosed()
  {
    return !((HttpClientConnection)getConnection()).isOpen();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.pool.BasicPoolEntry
 * JD-Core Version:    0.7.0.1
 */