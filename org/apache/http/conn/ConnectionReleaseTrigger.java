package org.apache.http.conn;

import java.io.IOException;

public abstract interface ConnectionReleaseTrigger
{
  public abstract void abortConnection()
    throws IOException;
  
  public abstract void releaseConnection()
    throws IOException;
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.ConnectionReleaseTrigger
 * JD-Core Version:    0.7.0.1
 */