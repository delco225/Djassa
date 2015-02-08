package org.apache.http.conn;

import java.io.IOException;
import java.io.InputStream;

public abstract interface EofSensorWatcher
{
  public abstract boolean eofDetected(InputStream paramInputStream)
    throws IOException;
  
  public abstract boolean streamAbort(InputStream paramInputStream)
    throws IOException;
  
  public abstract boolean streamClosed(InputStream paramInputStream)
    throws IOException;
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.EofSensorWatcher
 * JD-Core Version:    0.7.0.1
 */