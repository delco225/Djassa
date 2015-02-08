package org.apache.http.client.protocol;

import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.auth.Credentials;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Asserts;

@Deprecated
abstract class RequestAuthenticationBase
  implements HttpRequestInterceptor
{
  final Log log = LogFactory.getLog(getClass());
  
  private Header authenticate(AuthScheme paramAuthScheme, Credentials paramCredentials, HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws AuthenticationException
  {
    Asserts.notNull(paramAuthScheme, "Auth scheme");
    if ((paramAuthScheme instanceof ContextAwareAuthScheme)) {
      return ((ContextAwareAuthScheme)paramAuthScheme).authenticate(paramCredentials, paramHttpRequest, paramHttpContext);
    }
    return paramAuthScheme.authenticate(paramCredentials, paramHttpRequest);
  }
  
  private void ensureAuthScheme(AuthScheme paramAuthScheme)
  {
    Asserts.notNull(paramAuthScheme, "Auth scheme");
  }
  
  void process(AuthState paramAuthState, HttpRequest paramHttpRequest, HttpContext paramHttpContext)
  {
    AuthScheme localAuthScheme1 = paramAuthState.getAuthScheme();
    Credentials localCredentials1 = paramAuthState.getCredentials();
    switch (1.$SwitchMap$org$apache$http$auth$AuthProtocolState[paramAuthState.getState().ordinal()])
    {
    }
    for (;;)
    {
      if (localAuthScheme1 != null) {}
      try
      {
        paramHttpRequest.addHeader(authenticate(localAuthScheme1, localCredentials1, paramHttpRequest, paramHttpContext));
        for (;;)
        {
          return;
          ensureAuthScheme(localAuthScheme1);
          if (!localAuthScheme1.isConnectionBased()) {
            break;
          }
          return;
          Queue localQueue = paramAuthState.getAuthOptions();
          if (localQueue == null) {
            break label264;
          }
          while (!localQueue.isEmpty())
          {
            AuthOption localAuthOption = (AuthOption)localQueue.remove();
            AuthScheme localAuthScheme2 = localAuthOption.getAuthScheme();
            Credentials localCredentials2 = localAuthOption.getCredentials();
            paramAuthState.update(localAuthScheme2, localCredentials2);
            if (this.log.isDebugEnabled()) {
              this.log.debug("Generating response to an authentication challenge using " + localAuthScheme2.getSchemeName() + " scheme");
            }
            try
            {
              paramHttpRequest.addHeader(authenticate(localAuthScheme2, localCredentials2, paramHttpRequest, paramHttpContext));
              return;
            }
            catch (AuthenticationException localAuthenticationException2) {}
            if (this.log.isWarnEnabled()) {
              this.log.warn(localAuthScheme2 + " authentication error: " + localAuthenticationException2.getMessage());
            }
          }
        }
        label264:
        ensureAuthScheme(localAuthScheme1);
      }
      catch (AuthenticationException localAuthenticationException1)
      {
        while (!this.log.isErrorEnabled()) {}
        this.log.error(localAuthScheme1 + " authentication error: " + localAuthenticationException1.getMessage());
      }
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.protocol.RequestAuthenticationBase
 * JD-Core Version:    0.7.0.1
 */