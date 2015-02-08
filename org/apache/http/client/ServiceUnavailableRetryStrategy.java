package org.apache.http.client;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

public abstract interface ServiceUnavailableRetryStrategy
{
  public abstract long getRetryInterval();
  
  public abstract boolean retryRequest(HttpResponse paramHttpResponse, int paramInt, HttpContext paramHttpContext);
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.ServiceUnavailableRetryStrategy
 * JD-Core Version:    0.7.0.1
 */