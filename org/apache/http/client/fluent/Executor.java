package org.apache.http.client.fluent;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLInitializationException;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;

public class Executor
{
  static final HttpClient CLIENT;
  static final PoolingHttpClientConnectionManager CONNMGR;
  private final AuthCache authCache;
  private volatile CookieStore cookieStore;
  private final CredentialsProvider credentialsProvider;
  private final HttpClient httpclient;
  
  static
  {
    try
    {
      SSLConnectionSocketFactory localSSLConnectionSocketFactory2 = SSLConnectionSocketFactory.getSystemSocketFactory();
      localObject = localSSLConnectionSocketFactory2;
    }
    catch (SSLInitializationException localSSLInitializationException)
    {
      for (;;)
      {
        try
        {
          RegistryBuilder localRegistryBuilder;
          SSLContext localSSLContext = SSLContext.getInstance("TLS");
          localSSLContext.init(null, null, null);
          SSLConnectionSocketFactory localSSLConnectionSocketFactory1 = new SSLConnectionSocketFactory(localSSLContext);
          localObject = localSSLConnectionSocketFactory1;
          continue;
          localObject = SSLConnectionSocketFactory.getSocketFactory();
        }
        catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
        {
          localObject = null;
        }
        catch (KeyManagementException localKeyManagementException)
        {
          localObject = null;
        }
        catch (SecurityException localSecurityException)
        {
          Object localObject = null;
        }
      }
    }
    localRegistryBuilder = RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory());
    if (localObject != null)
    {
      CONNMGR = new PoolingHttpClientConnectionManager(localRegistryBuilder.register("https", localObject).build());
      CONNMGR.setDefaultMaxPerRoute(100);
      CONNMGR.setMaxTotal(200);
      CLIENT = HttpClientBuilder.create().setConnectionManager(CONNMGR).build();
      return;
    }
  }
  
  Executor(HttpClient paramHttpClient)
  {
    this.httpclient = paramHttpClient;
    this.credentialsProvider = new BasicCredentialsProvider();
    this.authCache = new BasicAuthCache();
  }
  
  public static Executor newInstance()
  {
    return new Executor(CLIENT);
  }
  
  public static Executor newInstance(HttpClient paramHttpClient)
  {
    if (paramHttpClient != null) {}
    for (;;)
    {
      return new Executor(paramHttpClient);
      paramHttpClient = CLIENT;
    }
  }
  
  @Deprecated
  public static void registerScheme(Scheme paramScheme) {}
  
  @Deprecated
  public static void unregisterScheme(String paramString) {}
  
  public Executor auth(String paramString1, String paramString2)
  {
    return auth(new UsernamePasswordCredentials(paramString1, paramString2));
  }
  
  public Executor auth(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    return auth(new NTCredentials(paramString1, paramString2, paramString3, paramString4));
  }
  
  public Executor auth(HttpHost paramHttpHost, String paramString1, String paramString2)
  {
    return auth(paramHttpHost, new UsernamePasswordCredentials(paramString1, paramString2));
  }
  
  public Executor auth(HttpHost paramHttpHost, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    return auth(paramHttpHost, new NTCredentials(paramString1, paramString2, paramString3, paramString4));
  }
  
  public Executor auth(HttpHost paramHttpHost, Credentials paramCredentials)
  {
    if (paramHttpHost != null) {}
    for (AuthScope localAuthScope = new AuthScope(paramHttpHost.getHostName(), paramHttpHost.getPort());; localAuthScope = AuthScope.ANY) {
      return auth(localAuthScope, paramCredentials);
    }
  }
  
  public Executor auth(AuthScope paramAuthScope, Credentials paramCredentials)
  {
    this.credentialsProvider.setCredentials(paramAuthScope, paramCredentials);
    return this;
  }
  
  public Executor auth(Credentials paramCredentials)
  {
    return auth(AuthScope.ANY, paramCredentials);
  }
  
  public Executor authPreemptive(HttpHost paramHttpHost)
  {
    BasicScheme localBasicScheme = new BasicScheme();
    try
    {
      localBasicScheme.processChallenge(new BasicHeader("WWW-Authenticate", "BASIC "));
      label23:
      this.authCache.put(paramHttpHost, localBasicScheme);
      return this;
    }
    catch (MalformedChallengeException localMalformedChallengeException)
    {
      break label23;
    }
  }
  
  public Executor authPreemptiveProxy(HttpHost paramHttpHost)
  {
    BasicScheme localBasicScheme = new BasicScheme();
    try
    {
      localBasicScheme.processChallenge(new BasicHeader("Proxy-Authenticate", "BASIC "));
      label23:
      this.authCache.put(paramHttpHost, localBasicScheme);
      return this;
    }
    catch (MalformedChallengeException localMalformedChallengeException)
    {
      break label23;
    }
  }
  
  public Executor clearAuth()
  {
    this.credentialsProvider.clear();
    return this;
  }
  
  public Executor clearCookies()
  {
    this.cookieStore.clear();
    return this;
  }
  
  public Executor cookieStore(CookieStore paramCookieStore)
  {
    this.cookieStore = paramCookieStore;
    return this;
  }
  
  public Response execute(Request paramRequest)
    throws ClientProtocolException, IOException
  {
    HttpClientContext localHttpClientContext = HttpClientContext.create();
    localHttpClientContext.setAttribute("http.auth.credentials-provider", this.credentialsProvider);
    localHttpClientContext.setAttribute("http.auth.auth-cache", this.authCache);
    localHttpClientContext.setAttribute("http.cookie-store", this.cookieStore);
    InternalHttpRequest localInternalHttpRequest = paramRequest.prepareRequest();
    return new Response(this.httpclient.execute(localInternalHttpRequest, localHttpClientContext));
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.fluent.Executor
 * JD-Core Version:    0.7.0.1
 */