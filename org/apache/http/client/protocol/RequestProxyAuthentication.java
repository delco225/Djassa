package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthState;
import org.apache.http.conn.HttpRoutedConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Deprecated
@Immutable
public class RequestProxyAuthentication
  extends RequestAuthenticationBase
{
  public void process(HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    Args.notNull(paramHttpRequest, "HTTP request");
    Args.notNull(paramHttpContext, "HTTP context");
    if (paramHttpRequest.containsHeader("Proxy-Authorization")) {}
    HttpRoutedConnection localHttpRoutedConnection;
    do
    {
      return;
      localHttpRoutedConnection = (HttpRoutedConnection)paramHttpContext.getAttribute("http.connection");
      if (localHttpRoutedConnection == null)
      {
        this.log.debug("HTTP connection not set in the context");
        return;
      }
    } while (localHttpRoutedConnection.getRoute().isTunnelled());
    AuthState localAuthState = (AuthState)paramHttpContext.getAttribute("http.auth.proxy-scope");
    if (localAuthState == null)
    {
      this.log.debug("Proxy auth state not set in the context");
      return;
    }
    if (this.log.isDebugEnabled()) {
      this.log.debug("Proxy auth state: " + localAuthState.getState());
    }
    process(localAuthState, paramHttpRequest, paramHttpContext);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.protocol.RequestProxyAuthentication
 * JD-Core Version:    0.7.0.1
 */