package org.apache.http.client;

import org.apache.http.conn.routing.HttpRoute;

public abstract interface BackoffManager
{
  public abstract void backOff(HttpRoute paramHttpRoute);
  
  public abstract void probe(HttpRoute paramHttpRoute);
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.BackoffManager
 * JD-Core Version:    0.7.0.1
 */