package org.apache.http.impl.client;

import java.security.Principal;
import javax.net.ssl.SSLSession;
import org.apache.http.HttpConnection;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.protocol.HttpContext;

@Immutable
public class DefaultUserTokenHandler
  implements UserTokenHandler
{
  public static final DefaultUserTokenHandler INSTANCE = new DefaultUserTokenHandler();
  
  private static Principal getAuthPrincipal(AuthState paramAuthState)
  {
    AuthScheme localAuthScheme = paramAuthState.getAuthScheme();
    if ((localAuthScheme != null) && (localAuthScheme.isComplete()) && (localAuthScheme.isConnectionBased()))
    {
      Credentials localCredentials = paramAuthState.getCredentials();
      if (localCredentials != null) {
        return localCredentials.getUserPrincipal();
      }
    }
    return null;
  }
  
  public Object getUserToken(HttpContext paramHttpContext)
  {
    HttpClientContext localHttpClientContext = HttpClientContext.adapt(paramHttpContext);
    AuthState localAuthState = localHttpClientContext.getTargetAuthState();
    Principal localPrincipal = null;
    if (localAuthState != null)
    {
      localPrincipal = getAuthPrincipal(localAuthState);
      if (localPrincipal == null) {
        localPrincipal = getAuthPrincipal(localHttpClientContext.getProxyAuthState());
      }
    }
    if (localPrincipal == null)
    {
      HttpConnection localHttpConnection = localHttpClientContext.getConnection();
      if ((localHttpConnection.isOpen()) && ((localHttpConnection instanceof ManagedHttpClientConnection)))
      {
        SSLSession localSSLSession = ((ManagedHttpClientConnection)localHttpConnection).getSSLSession();
        if (localSSLSession != null) {
          localPrincipal = localSSLSession.getLocalPrincipal();
        }
      }
    }
    return localPrincipal;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.DefaultUserTokenHandler
 * JD-Core Version:    0.7.0.1
 */