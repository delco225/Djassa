package org.apache.http.impl.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.AuthCache;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Immutable
abstract class AuthenticationStrategyImpl
  implements AuthenticationStrategy
{
  private static final List<String> DEFAULT_SCHEME_PRIORITY = Collections.unmodifiableList(Arrays.asList(new String[] { "negotiate", "Kerberos", "NTLM", "Digest", "Basic" }));
  private final int challengeCode;
  private final String headerName;
  private final Log log = LogFactory.getLog(getClass());
  
  AuthenticationStrategyImpl(int paramInt, String paramString)
  {
    this.challengeCode = paramInt;
    this.headerName = paramString;
  }
  
  public void authFailed(HttpHost paramHttpHost, AuthScheme paramAuthScheme, HttpContext paramHttpContext)
  {
    Args.notNull(paramHttpHost, "Host");
    Args.notNull(paramHttpContext, "HTTP context");
    AuthCache localAuthCache = HttpClientContext.adapt(paramHttpContext).getAuthCache();
    if (localAuthCache != null)
    {
      if (this.log.isDebugEnabled()) {
        this.log.debug("Clearing cached auth scheme for " + paramHttpHost);
      }
      localAuthCache.remove(paramHttpHost);
    }
  }
  
  public void authSucceeded(HttpHost paramHttpHost, AuthScheme paramAuthScheme, HttpContext paramHttpContext)
  {
    Args.notNull(paramHttpHost, "Host");
    Args.notNull(paramAuthScheme, "Auth scheme");
    Args.notNull(paramHttpContext, "HTTP context");
    HttpClientContext localHttpClientContext = HttpClientContext.adapt(paramHttpContext);
    if (isCachable(paramAuthScheme))
    {
      Object localObject = localHttpClientContext.getAuthCache();
      if (localObject == null)
      {
        localObject = new BasicAuthCache();
        localHttpClientContext.setAuthCache((AuthCache)localObject);
      }
      if (this.log.isDebugEnabled()) {
        this.log.debug("Caching '" + paramAuthScheme.getSchemeName() + "' auth scheme for " + paramHttpHost);
      }
      ((AuthCache)localObject).put(paramHttpHost, paramAuthScheme);
    }
  }
  
  public Map<String, Header> getChallenges(HttpHost paramHttpHost, HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws MalformedChallengeException
  {
    Args.notNull(paramHttpResponse, "HTTP response");
    Header[] arrayOfHeader = paramHttpResponse.getHeaders(this.headerName);
    HashMap localHashMap = new HashMap(arrayOfHeader.length);
    int i = arrayOfHeader.length;
    for (int j = 0; j < i; j++)
    {
      Header localHeader = arrayOfHeader[j];
      CharArrayBuffer localCharArrayBuffer;
      int k;
      if ((localHeader instanceof FormattedHeader))
      {
        localCharArrayBuffer = ((FormattedHeader)localHeader).getBuffer();
        k = ((FormattedHeader)localHeader).getValuePos();
      }
      while ((k < localCharArrayBuffer.length()) && (HTTP.isWhitespace(localCharArrayBuffer.charAt(k))))
      {
        k++;
        continue;
        String str = localHeader.getValue();
        if (str == null) {
          throw new MalformedChallengeException("Header value is null");
        }
        localCharArrayBuffer = new CharArrayBuffer(str.length());
        localCharArrayBuffer.append(str);
        k = 0;
      }
      int m = k;
      while ((k < localCharArrayBuffer.length()) && (!HTTP.isWhitespace(localCharArrayBuffer.charAt(k)))) {
        k++;
      }
      localHashMap.put(localCharArrayBuffer.substring(m, k).toLowerCase(Locale.US), localHeader);
    }
    return localHashMap;
  }
  
  abstract Collection<String> getPreferredAuthSchemes(RequestConfig paramRequestConfig);
  
  public boolean isAuthenticationRequested(HttpHost paramHttpHost, HttpResponse paramHttpResponse, HttpContext paramHttpContext)
  {
    Args.notNull(paramHttpResponse, "HTTP response");
    return paramHttpResponse.getStatusLine().getStatusCode() == this.challengeCode;
  }
  
  protected boolean isCachable(AuthScheme paramAuthScheme)
  {
    if ((paramAuthScheme == null) || (!paramAuthScheme.isComplete())) {}
    String str;
    do
    {
      return false;
      str = paramAuthScheme.getSchemeName();
    } while ((!str.equalsIgnoreCase("Basic")) && (!str.equalsIgnoreCase("Digest")));
    return true;
  }
  
  public Queue<AuthOption> select(Map<String, Header> paramMap, HttpHost paramHttpHost, HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws MalformedChallengeException
  {
    Args.notNull(paramMap, "Map of auth challenges");
    Args.notNull(paramHttpHost, "Host");
    Args.notNull(paramHttpResponse, "HTTP response");
    Args.notNull(paramHttpContext, "HTTP context");
    HttpClientContext localHttpClientContext = HttpClientContext.adapt(paramHttpContext);
    LinkedList localLinkedList = new LinkedList();
    Lookup localLookup = localHttpClientContext.getAuthSchemeRegistry();
    if (localLookup == null) {
      this.log.debug("Auth scheme registry not set in the context");
    }
    for (;;)
    {
      return localLinkedList;
      CredentialsProvider localCredentialsProvider = localHttpClientContext.getCredentialsProvider();
      if (localCredentialsProvider == null)
      {
        this.log.debug("Credentials provider not set in the context");
        return localLinkedList;
      }
      Object localObject = getPreferredAuthSchemes(localHttpClientContext.getRequestConfig());
      if (localObject == null) {
        localObject = DEFAULT_SCHEME_PRIORITY;
      }
      if (this.log.isDebugEnabled()) {
        this.log.debug("Authentication schemes in the order of preference: " + localObject);
      }
      Iterator localIterator = ((Collection)localObject).iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        Header localHeader = (Header)paramMap.get(str.toLowerCase(Locale.US));
        if (localHeader != null)
        {
          AuthSchemeProvider localAuthSchemeProvider = (AuthSchemeProvider)localLookup.lookup(str);
          if (localAuthSchemeProvider == null)
          {
            if (this.log.isWarnEnabled()) {
              this.log.warn("Authentication scheme " + str + " not supported");
            }
          }
          else
          {
            AuthScheme localAuthScheme = localAuthSchemeProvider.create(paramHttpContext);
            localAuthScheme.processChallenge(localHeader);
            Credentials localCredentials = localCredentialsProvider.getCredentials(new AuthScope(paramHttpHost.getHostName(), paramHttpHost.getPort(), localAuthScheme.getRealm(), localAuthScheme.getSchemeName()));
            if (localCredentials != null)
            {
              AuthOption localAuthOption = new AuthOption(localAuthScheme, localCredentials);
              localLinkedList.add(localAuthOption);
            }
          }
        }
        else if (this.log.isDebugEnabled())
        {
          this.log.debug("Challenge for " + str + " authentication scheme not available");
        }
      }
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.AuthenticationStrategyImpl
 * JD-Core Version:    0.7.0.1
 */