package org.apache.http.impl.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.ConnectionBackoffStrategy;

public class NullBackoffStrategy
  implements ConnectionBackoffStrategy
{
  public boolean shouldBackoff(Throwable paramThrowable)
  {
    return false;
  }
  
  public boolean shouldBackoff(HttpResponse paramHttpResponse)
  {
    return false;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.NullBackoffStrategy
 * JD-Core Version:    0.7.0.1
 */