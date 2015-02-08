package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.client.AuthCache;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Deprecated
@Immutable
public class ResponseAuthCache
  implements HttpResponseInterceptor
{
  private final Log log = LogFactory.getLog(getClass());
  
  private void cache(AuthCache paramAuthCache, HttpHost paramHttpHost, AuthScheme paramAuthScheme)
  {
    if (this.log.isDebugEnabled()) {
      this.log.debug("Caching '" + paramAuthScheme.getSchemeName() + "' auth scheme for " + paramHttpHost);
    }
    paramAuthCache.put(paramHttpHost, paramAuthScheme);
  }
  
  private boolean isCachable(AuthState paramAuthState)
  {
    AuthScheme localAuthScheme = paramAuthState.getAuthScheme();
    if ((localAuthScheme == null) || (!localAuthScheme.isComplete())) {}
    String str;
    do
    {
      return false;
      str = localAuthScheme.getSchemeName();
    } while ((!str.equalsIgnoreCase("Basic")) && (!str.equalsIgnoreCase("Digest")));
    return true;
  }
  
  private void uncache(AuthCache paramAuthCache, HttpHost paramHttpHost, AuthScheme paramAuthScheme)
  {
    if (this.log.isDebugEnabled()) {
      this.log.debug("Removing from cache '" + paramAuthScheme.getSchemeName() + "' auth scheme for " + paramHttpHost);
    }
    paramAuthCache.remove(paramHttpHost);
  }
  
  public void process(HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    Args.notNull(paramHttpResponse, "HTTP request");
    Args.notNull(paramHttpContext, "HTTP context");
    Object localObject = (AuthCache)paramHttpContext.getAttribute("http.auth.auth-cache");
    HttpHost localHttpHost1 = (HttpHost)paramHttpContext.getAttribute("http.target_host");
    AuthState localAuthState1 = (AuthState)paramHttpContext.getAttribute("http.auth.target-scope");
    if ((localHttpHost1 != null) && (localAuthState1 != null))
    {
      if (this.log.isDebugEnabled()) {
        this.log.debug("Target auth state: " + localAuthState1.getState());
      }
      if (isCachable(localAuthState1))
      {
        SchemeRegistry localSchemeRegistry = (SchemeRegistry)paramHttpContext.getAttribute("http.scheme-registry");
        if (localHttpHost1.getPort() < 0)
        {
          Scheme localScheme = localSchemeRegistry.getScheme(localHttpHost1);
          localHttpHost1 = new HttpHost(localHttpHost1.getHostName(), localScheme.resolvePort(localHttpHost1.getPort()), localHttpHost1.getSchemeName());
        }
        if (localObject == null)
        {
          localObject = new BasicAuthCache();
          paramHttpContext.setAttribute("http.auth.auth-cache", localObject);
        }
        switch (1.$SwitchMap$org$apache$http$auth$AuthProtocolState[localAuthState1.getState().ordinal()])
        {
        }
      }
    }
    HttpHost localHttpHost2;
    AuthState localAuthState2;
    for (;;)
    {
      localHttpHost2 = (HttpHost)paramHttpContext.getAttribute("http.proxy_host");
      localAuthState2 = (AuthState)paramHttpContext.getAttribute("http.auth.proxy-scope");
      if ((localHttpHost2 != null) && (localAuthState2 != null))
      {
        if (this.log.isDebugEnabled()) {
          this.log.debug("Proxy auth state: " + localAuthState2.getState());
        }
        if (isCachable(localAuthState2)) {
          if (localObject == null)
          {
            localObject = new BasicAuthCache();
            paramHttpContext.setAttribute("http.auth.auth-cache", localObject);
          }
        }
      }
      switch (1.$SwitchMap$org$apache$http$auth$AuthProtocolState[localAuthState2.getState().ordinal()])
      {
      default: 
        return;
        cache((AuthCache)localObject, localHttpHost1, localAuthState1.getAuthScheme());
        continue;
        uncache((AuthCache)localObject, localHttpHost1, localAuthState1.getAuthScheme());
      }
    }
    cache((AuthCache)localObject, localHttpHost2, localAuthState2.getAuthScheme());
    return;
    uncache((AuthCache)localObject, localHttpHost2, localAuthState2.getAuthScheme());
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.protocol.ResponseAuthCache
 * JD-Core Version:    0.7.0.1
 */