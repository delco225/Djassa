package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.HttpRequest;

public abstract interface HttpUriRequest
  extends HttpRequest
{
  public abstract void abort()
    throws UnsupportedOperationException;
  
  public abstract String getMethod();
  
  public abstract URI getURI();
  
  public abstract boolean isAborted();
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.methods.HttpUriRequest
 * JD-Core Version:    0.7.0.1
 */