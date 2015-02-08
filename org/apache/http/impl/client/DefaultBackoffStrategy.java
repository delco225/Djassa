package org.apache.http.impl.client;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ConnectionBackoffStrategy;

public class DefaultBackoffStrategy
  implements ConnectionBackoffStrategy
{
  public boolean shouldBackoff(Throwable paramThrowable)
  {
    return ((paramThrowable instanceof SocketTimeoutException)) || ((paramThrowable instanceof ConnectException));
  }
  
  public boolean shouldBackoff(HttpResponse paramHttpResponse)
  {
    return paramHttpResponse.getStatusLine().getStatusCode() == 503;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.DefaultBackoffStrategy
 * JD-Core Version:    0.7.0.1
 */