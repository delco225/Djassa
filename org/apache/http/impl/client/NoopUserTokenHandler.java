package org.apache.http.impl.client;

import org.apache.http.annotation.Immutable;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.protocol.HttpContext;

@Immutable
public class NoopUserTokenHandler
  implements UserTokenHandler
{
  public static final NoopUserTokenHandler INSTANCE = new NoopUserTokenHandler();
  
  public Object getUserToken(HttpContext paramHttpContext)
  {
    return null;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.NoopUserTokenHandler
 * JD-Core Version:    0.7.0.1
 */