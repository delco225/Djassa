package org.apache.http.client;

import org.apache.http.HttpResponse;

public abstract interface ConnectionBackoffStrategy
{
  public abstract boolean shouldBackoff(Throwable paramThrowable);
  
  public abstract boolean shouldBackoff(HttpResponse paramHttpResponse);
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.ConnectionBackoffStrategy
 * JD-Core Version:    0.7.0.1
 */