package org.apache.http.impl.auth;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Asserts;

public class HttpAuthenticator
{
  private final Log log;
  
  public HttpAuthenticator()
  {
    this(null);
  }
  
  public HttpAuthenticator(Log paramLog)
  {
    if (paramLog != null) {}
    for (;;)
    {
      this.log = paramLog;
      return;
      paramLog = LogFactory.getLog(getClass());
    }
  }
  
  private Header doAuth(AuthScheme paramAuthScheme, Credentials paramCredentials, HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws AuthenticationException
  {
    if ((paramAuthScheme instanceof ContextAwareAuthScheme)) {
      return ((ContextAwareAuthScheme)paramAuthScheme).authenticate(paramCredentials, paramHttpRequest, paramHttpContext);
    }
    return paramAuthScheme.authenticate(paramCredentials, paramHttpRequest);
  }
  
  private void ensureAuthScheme(AuthScheme paramAuthScheme)
  {
    Asserts.notNull(paramAuthScheme, "Auth scheme");
  }
  
  public void generateAuthResponse(HttpRequest paramHttpRequest, AuthState paramAuthState, HttpContext paramHttpContext)
    throws HttpException, IOException
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
        paramHttpRequest.addHeader(doAuth(localAuthScheme1, localCredentials1, paramHttpRequest, paramHttpContext));
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
            break label268;
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
              paramHttpRequest.addHeader(doAuth(localAuthScheme2, localCredentials2, paramHttpRequest, paramHttpContext));
              return;
            }
            catch (AuthenticationException localAuthenticationException2) {}
            if (this.log.isWarnEnabled()) {
              this.log.warn(localAuthScheme2 + " authentication error: " + localAuthenticationException2.getMessage());
            }
          }
        }
        label268:
        ensureAuthScheme(localAuthScheme1);
      }
      catch (AuthenticationException localAuthenticationException1)
      {
        while (!this.log.isErrorEnabled()) {}
        this.log.error(localAuthScheme1 + " authentication error: " + localAuthenticationException1.getMessage());
      }
    }
  }
  
  public boolean handleAuthChallenge(HttpHost paramHttpHost, HttpResponse paramHttpResponse, AuthenticationStrategy paramAuthenticationStrategy, AuthState paramAuthState, HttpContext paramHttpContext)
  {
    for (;;)
    {
      Map localMap;
      AuthScheme localAuthScheme;
      try
      {
        if (this.log.isDebugEnabled()) {
          this.log.debug(paramHttpHost.toHostString() + " requested authentication");
        }
        localMap = paramAuthenticationStrategy.getChallenges(paramHttpHost, paramHttpResponse, paramHttpContext);
        if (localMap.isEmpty())
        {
          this.log.debug("Response contains no authentication challenges");
          return false;
        }
        localAuthScheme = paramAuthState.getAuthScheme();
        switch (1.$SwitchMap$org$apache$http$auth$AuthProtocolState[paramAuthState.getState().ordinal()])
        {
        default: 
          Queue localQueue = paramAuthenticationStrategy.select(localMap, paramHttpHost, paramHttpResponse, paramHttpContext);
          if ((localQueue == null) || (localQueue.isEmpty())) {
            break label444;
          }
          if (this.log.isDebugEnabled()) {
            this.log.debug("Selected authentication options: " + localQueue);
          }
          paramAuthState.setState(AuthProtocolState.CHALLENGED);
          paramAuthState.update(localQueue);
          return true;
        }
      }
      catch (MalformedChallengeException localMalformedChallengeException)
      {
        if (!this.log.isWarnEnabled()) {
          continue;
        }
        this.log.warn("Malformed challenge: " + localMalformedChallengeException.getMessage());
        paramAuthState.reset();
        return false;
      }
      paramAuthState.reset();
      continue;
      if (localAuthScheme == null)
      {
        this.log.debug("Auth scheme is null");
        paramAuthenticationStrategy.authFailed(paramHttpHost, null, paramHttpContext);
        paramAuthState.reset();
        paramAuthState.setState(AuthProtocolState.FAILURE);
        return false;
      }
      if (localAuthScheme != null)
      {
        Header localHeader = (Header)localMap.get(localAuthScheme.getSchemeName().toLowerCase(Locale.US));
        if (localHeader != null)
        {
          this.log.debug("Authorization challenge processed");
          localAuthScheme.processChallenge(localHeader);
          if (localAuthScheme.isComplete())
          {
            this.log.debug("Authentication failed");
            paramAuthenticationStrategy.authFailed(paramHttpHost, paramAuthState.getAuthScheme(), paramHttpContext);
            paramAuthState.reset();
            paramAuthState.setState(AuthProtocolState.FAILURE);
            return false;
          }
          paramAuthState.setState(AuthProtocolState.HANDSHAKE);
          return true;
        }
        paramAuthState.reset();
      }
    }
    label444:
    return false;
    return false;
  }
  
  public boolean isAuthenticationRequested(HttpHost paramHttpHost, HttpResponse paramHttpResponse, AuthenticationStrategy paramAuthenticationStrategy, AuthState paramAuthState, HttpContext paramHttpContext)
  {
    if (paramAuthenticationStrategy.isAuthenticationRequested(paramHttpHost, paramHttpResponse, paramHttpContext))
    {
      this.log.debug("Authentication required");
      if (paramAuthState.getState() == AuthProtocolState.SUCCESS) {
        paramAuthenticationStrategy.authFailed(paramHttpHost, paramAuthState.getAuthScheme(), paramHttpContext);
      }
      return true;
    }
    switch (1.$SwitchMap$org$apache$http$auth$AuthProtocolState[paramAuthState.getState().ordinal()])
    {
    default: 
      paramAuthState.setState(AuthProtocolState.UNCHALLENGED);
    }
    for (;;)
    {
      return false;
      this.log.debug("Authentication succeeded");
      paramAuthState.setState(AuthProtocolState.SUCCESS);
      paramAuthenticationStrategy.authSucceeded(paramHttpHost, paramAuthState.getAuthScheme(), paramHttpContext);
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.auth.HttpAuthenticator
 * JD-Core Version:    0.7.0.1
 */