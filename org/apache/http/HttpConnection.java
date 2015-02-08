package org.apache.http;

import java.io.Closeable;
import java.io.IOException;

public abstract interface HttpConnection
  extends Closeable
{
  public abstract void close()
    throws IOException;
  
  public abstract HttpConnectionMetrics getMetrics();
  
  public abstract int getSocketTimeout();
  
  public abstract boolean isOpen();
  
  public abstract boolean isStale();
  
  public abstract void setSocketTimeout(int paramInt);
  
  public abstract void shutdown()
    throws IOException;
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.HttpConnection
 * JD-Core Version:    0.7.0.1
 */