package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class RequestAuthCache
  implements HttpRequestInterceptor
{
  private final Log log = LogFactory.getLog(getClass());
  
  private void doPreemptiveAuth(HttpHost paramHttpHost, AuthScheme paramAuthScheme, AuthState paramAuthState, CredentialsProvider paramCredentialsProvider)
  {
    String str = paramAuthScheme.getSchemeName();
    if (this.log.isDebugEnabled()) {
      this.log.debug("Re-using cached '" + str + "' auth scheme for " + paramHttpHost);
    }
    Credentials localCredentials = paramCredentialsProvider.getCredentials(new AuthScope(paramHttpHost, AuthScope.ANY_REALM, str));
    if (localCredentials != null)
    {
      if ("BASIC".equalsIgnoreCase(paramAuthScheme.getSchemeName())) {
        paramAuthState.setState(AuthProtocolState.CHALLENGED);
      }
      for (;;)
      {
        paramAuthState.update(paramAuthScheme, localCredentials);
        return;
        paramAuthState.setState(AuthProtocolState.SUCCESS);
      }
    }
    this.log.debug("No credentials for preemptive authentication");
  }
  
  public void process(HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    Args.notNull(paramHttpRequest, "HTTP request");
    Args.notNull(paramHttpContext, "HTTP context");
    HttpClientContext localHttpClientContext = HttpClientContext.adapt(paramHttpContext);
    AuthCache localAuthCache = localHttpClientContext.getAuthCache();
    if (localAuthCache == null) {
      this.log.debug("Auth cache not set in the context");
    }
    CredentialsProvider localCredentialsProvider;
    HttpHost localHttpHost2;
    AuthState localAuthState2;
    AuthScheme localAuthScheme1;
    do
    {
      do
      {
        return;
        localCredentialsProvider = localHttpClientContext.getCredentialsProvider();
        if (localCredentialsProvider == null)
        {
          this.log.debug("Credentials provider not set in the context");
          return;
        }
        RouteInfo localRouteInfo = localHttpClientContext.getHttpRoute();
        HttpHost localHttpHost1 = localHttpClientContext.getTargetHost();
        if (localHttpHost1.getPort() < 0) {
          localHttpHost1 = new HttpHost(localHttpHost1.getHostName(), localRouteInfo.getTargetHost().getPort(), localHttpHost1.getSchemeName());
        }
        AuthState localAuthState1 = localHttpClientContext.getTargetAuthState();
        if ((localAuthState1 != null) && (localAuthState1.getState() == AuthProtocolState.UNCHALLENGED))
        {
          AuthScheme localAuthScheme2 = localAuthCache.get(localHttpHost1);
          if (localAuthScheme2 != null) {
            doPreemptiveAuth(localHttpHost1, localAuthScheme2, localAuthState1, localCredentialsProvider);
          }
        }
        localHttpHost2 = localRouteInfo.getProxyHost();
        localAuthState2 = localHttpClientContext.getProxyAuthState();
      } while ((localHttpHost2 == null) || (localAuthState2 == null) || (localAuthState2.getState() != AuthProtocolState.UNCHALLENGED));
      localAuthScheme1 = localAuthCache.get(localHttpHost2);
    } while (localAuthScheme1 == null);
    doPreemptiveAuth(localHttpHost2, localAuthScheme1, localAuthState2, localCredentialsProvider);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.protocol.RequestAuthCache
 * JD-Core Version:    0.7.0.1
 */