package org.apache.http.impl.conn.tsccm;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.conn.AbstractPoolEntry;
import org.apache.http.impl.conn.AbstractPooledConnAdapter;

@Deprecated
public class BasicPooledConnAdapter
  extends AbstractPooledConnAdapter
{
  protected BasicPooledConnAdapter(ThreadSafeClientConnManager paramThreadSafeClientConnManager, AbstractPoolEntry paramAbstractPoolEntry)
  {
    super(paramThreadSafeClientConnManager, paramAbstractPoolEntry);
    markReusable();
  }
  
  protected void detach()
  {
    super.detach();
  }
  
  protected ClientConnectionManager getManager()
  {
    return super.getManager();
  }
  
  protected AbstractPoolEntry getPoolEntry()
  {
    return super.getPoolEntry();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.tsccm.BasicPooledConnAdapter
 * JD-Core Version:    0.7.0.1
 */