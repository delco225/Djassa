package org.apache.http.conn;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@Deprecated
@NotThreadSafe
public class BasicEofSensorWatcher
  implements EofSensorWatcher
{
  protected final boolean attemptReuse;
  protected final ManagedClientConnection managedConn;
  
  public BasicEofSensorWatcher(ManagedClientConnection paramManagedClientConnection, boolean paramBoolean)
  {
    Args.notNull(paramManagedClientConnection, "Connection");
    this.managedConn = paramManagedClientConnection;
    this.attemptReuse = paramBoolean;
  }
  
  public boolean eofDetected(InputStream paramInputStream)
    throws IOException
  {
    try
    {
      if (this.attemptReuse)
      {
        paramInputStream.close();
        this.managedConn.markReusable();
      }
      return false;
    }
    finally
    {
      this.managedConn.releaseConnection();
    }
  }
  
  public boolean streamAbort(InputStream paramInputStream)
    throws IOException
  {
    this.managedConn.abortConnection();
    return false;
  }
  
  public boolean streamClosed(InputStream paramInputStream)
    throws IOException
  {
    try
    {
      if (this.attemptReuse)
      {
        paramInputStream.close();
        this.managedConn.markReusable();
      }
      return false;
    }
    finally
    {
      this.managedConn.releaseConnection();
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.BasicEofSensorWatcher
 * JD-Core Version:    0.7.0.1
 */