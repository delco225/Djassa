package org.apache.http.impl.client;

import java.io.Closeable;
import java.net.ProxySelector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.BackoffManager;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.client.protocol.RequestAuthCache;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.client.protocol.RequestDefaultHeaders;
import org.apache.http.client.protocol.RequestExpectContinue;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.client.protocol.ResponseProcessCookies;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.impl.cookie.IgnoreSpecFactory;
import org.apache.http.impl.cookie.NetscapeDraftSpecFactory;
import org.apache.http.impl.cookie.RFC2109SpecFactory;
import org.apache.http.impl.cookie.RFC2965SpecFactory;
import org.apache.http.impl.execchain.BackoffStrategyExec;
import org.apache.http.impl.execchain.ClientExecChain;
import org.apache.http.impl.execchain.MainClientExec;
import org.apache.http.impl.execchain.ProtocolExec;
import org.apache.http.impl.execchain.RedirectExec;
import org.apache.http.impl.execchain.RetryExec;
import org.apache.http.impl.execchain.ServiceUnavailableRetryExec;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.TextUtils;
import org.apache.http.util.VersionInfo;

@NotThreadSafe
public class HttpClientBuilder
{
  static final String DEFAULT_USER_AGENT;
  private boolean authCachingDisabled;
  private Lookup<AuthSchemeProvider> authSchemeRegistry;
  private boolean automaticRetriesDisabled;
  private BackoffManager backoffManager;
  private List<Closeable> closeables;
  private HttpClientConnectionManager connManager;
  private ConnectionBackoffStrategy connectionBackoffStrategy;
  private boolean connectionStateDisabled;
  private boolean contentCompressionDisabled;
  private boolean cookieManagementDisabled;
  private Lookup<CookieSpecProvider> cookieSpecRegistry;
  private CookieStore cookieStore;
  private CredentialsProvider credentialsProvider;
  private ConnectionConfig defaultConnectionConfig;
  private Collection<? extends Header> defaultHeaders;
  private RequestConfig defaultRequestConfig;
  private SocketConfig defaultSocketConfig;
  private X509HostnameVerifier hostnameVerifier;
  private HttpProcessor httpprocessor;
  private ConnectionKeepAliveStrategy keepAliveStrategy;
  private int maxConnPerRoute = 0;
  private int maxConnTotal = 0;
  private HttpHost proxy;
  private AuthenticationStrategy proxyAuthStrategy;
  private boolean redirectHandlingDisabled;
  private RedirectStrategy redirectStrategy;
  private HttpRequestExecutor requestExec;
  private LinkedList<HttpRequestInterceptor> requestFirst;
  private LinkedList<HttpRequestInterceptor> requestLast;
  private LinkedList<HttpResponseInterceptor> responseFirst;
  private LinkedList<HttpResponseInterceptor> responseLast;
  private HttpRequestRetryHandler retryHandler;
  private ConnectionReuseStrategy reuseStrategy;
  private HttpRoutePlanner routePlanner;
  private SchemePortResolver schemePortResolver;
  private ServiceUnavailableRetryStrategy serviceUnavailStrategy;
  private LayeredConnectionSocketFactory sslSocketFactory;
  private SSLContext sslcontext;
  private boolean systemProperties;
  private AuthenticationStrategy targetAuthStrategy;
  private String userAgent;
  private UserTokenHandler userTokenHandler;
  
  static
  {
    VersionInfo localVersionInfo = VersionInfo.loadVersionInfo("org.apache.http.client", HttpClientBuilder.class.getClassLoader());
    if (localVersionInfo != null) {}
    for (String str = localVersionInfo.getRelease();; str = "UNAVAILABLE")
    {
      DEFAULT_USER_AGENT = "Apache-HttpClient/" + str + " (java 1.5)";
      return;
    }
  }
  
  public static HttpClientBuilder create()
  {
    return new HttpClientBuilder();
  }
  
  private static String[] split(String paramString)
  {
    if (TextUtils.isBlank(paramString)) {
      return null;
    }
    return paramString.split(" *, *");
  }
  
  protected void addCloseable(Closeable paramCloseable)
  {
    if (paramCloseable == null) {
      return;
    }
    if (this.closeables == null) {
      this.closeables = new ArrayList();
    }
    this.closeables.add(paramCloseable);
  }
  
  public final HttpClientBuilder addInterceptorFirst(HttpRequestInterceptor paramHttpRequestInterceptor)
  {
    if (paramHttpRequestInterceptor == null) {
      return this;
    }
    if (this.requestFirst == null) {
      this.requestFirst = new LinkedList();
    }
    this.requestFirst.addFirst(paramHttpRequestInterceptor);
    return this;
  }
  
  public final HttpClientBuilder addInterceptorFirst(HttpResponseInterceptor paramHttpResponseInterceptor)
  {
    if (paramHttpResponseInterceptor == null) {
      return this;
    }
    if (this.responseFirst == null) {
      this.responseFirst = new LinkedList();
    }
    this.responseFirst.addFirst(paramHttpResponseInterceptor);
    return this;
  }
  
  public final HttpClientBuilder addInterceptorLast(HttpRequestInterceptor paramHttpRequestInterceptor)
  {
    if (paramHttpRequestInterceptor == null) {
      return this;
    }
    if (this.requestLast == null) {
      this.requestLast = new LinkedList();
    }
    this.requestLast.addLast(paramHttpRequestInterceptor);
    return this;
  }
  
  public final HttpClientBuilder addInterceptorLast(HttpResponseInterceptor paramHttpResponseInterceptor)
  {
    if (paramHttpResponseInterceptor == null) {
      return this;
    }
    if (this.responseLast == null) {
      this.responseLast = new LinkedList();
    }
    this.responseLast.addLast(paramHttpResponseInterceptor);
    return this;
  }
  
  public CloseableHttpClient build()
  {
    HttpRequestExecutor localHttpRequestExecutor = this.requestExec;
    if (localHttpRequestExecutor == null) {
      localHttpRequestExecutor = new HttpRequestExecutor();
    }
    Object localObject1 = this.connManager;
    Object localObject16;
    String[] arrayOfString1;
    String[] arrayOfString2;
    label71:
    X509HostnameVerifier localX509HostnameVerifier;
    label117:
    Object localObject2;
    label307:
    Object localObject3;
    Object localObject4;
    Object localObject5;
    if (localObject1 == null)
    {
      localObject16 = this.sslSocketFactory;
      if (localObject16 == null)
      {
        if (this.systemProperties)
        {
          arrayOfString1 = split(System.getProperty("https.protocols"));
          if (!this.systemProperties) {
            break label505;
          }
          arrayOfString2 = split(System.getProperty("https.cipherSuites"));
          localX509HostnameVerifier = this.hostnameVerifier;
          if (localX509HostnameVerifier == null) {
            localX509HostnameVerifier = SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
          }
          if (this.sslcontext == null) {
            break label511;
          }
          SSLContext localSSLContext2 = this.sslcontext;
          localObject16 = new SSLConnectionSocketFactory(localSSLContext2, arrayOfString1, arrayOfString2, localX509HostnameVerifier);
        }
      }
      else
      {
        PoolingHttpClientConnectionManager localPoolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", localObject16).build());
        if (this.defaultSocketConfig != null) {
          localPoolingHttpClientConnectionManager.setDefaultSocketConfig(this.defaultSocketConfig);
        }
        if (this.defaultConnectionConfig != null) {
          localPoolingHttpClientConnectionManager.setDefaultConnectionConfig(this.defaultConnectionConfig);
        }
        if ((this.systemProperties) && ("true".equalsIgnoreCase(System.getProperty("http.keepAlive", "true"))))
        {
          int i = Integer.parseInt(System.getProperty("http.maxConnections", "5"));
          localPoolingHttpClientConnectionManager.setDefaultMaxPerRoute(i);
          localPoolingHttpClientConnectionManager.setMaxTotal(i * 2);
        }
        if (this.maxConnTotal > 0) {
          localPoolingHttpClientConnectionManager.setMaxTotal(this.maxConnTotal);
        }
        if (this.maxConnPerRoute > 0) {
          localPoolingHttpClientConnectionManager.setDefaultMaxPerRoute(this.maxConnPerRoute);
        }
        localObject1 = localPoolingHttpClientConnectionManager;
      }
    }
    else
    {
      localObject2 = this.reuseStrategy;
      if (localObject2 == null)
      {
        if (!this.systemProperties) {
          break label574;
        }
        if (!"true".equalsIgnoreCase(System.getProperty("http.keepAlive", "true"))) {
          break label567;
        }
        localObject2 = DefaultConnectionReuseStrategy.INSTANCE;
      }
      localObject3 = this.keepAliveStrategy;
      if (localObject3 == null) {
        localObject3 = DefaultConnectionKeepAliveStrategy.INSTANCE;
      }
      localObject4 = this.targetAuthStrategy;
      if (localObject4 == null) {
        localObject4 = TargetAuthenticationStrategy.INSTANCE;
      }
      localObject5 = this.proxyAuthStrategy;
      if (localObject5 == null) {
        localObject5 = ProxyAuthenticationStrategy.INSTANCE;
      }
      localObject6 = this.userTokenHandler;
      if (localObject6 == null) {
        if (this.connectionStateDisabled) {
          break label581;
        }
      }
    }
    ClientExecChain localClientExecChain;
    String str;
    HttpProcessorBuilder localHttpProcessorBuilder;
    label567:
    label574:
    label581:
    for (Object localObject6 = DefaultUserTokenHandler.INSTANCE;; localObject6 = NoopUserTokenHandler.INSTANCE)
    {
      localClientExecChain = decorateMainExec(new MainClientExec(localHttpRequestExecutor, (HttpClientConnectionManager)localObject1, (ConnectionReuseStrategy)localObject2, (ConnectionKeepAliveStrategy)localObject3, (AuthenticationStrategy)localObject4, (AuthenticationStrategy)localObject5, (UserTokenHandler)localObject6));
      localHttpProcessor = this.httpprocessor;
      if (localHttpProcessor != null) {
        break label918;
      }
      str = this.userAgent;
      if (str == null)
      {
        if (this.systemProperties) {
          str = System.getProperty("http.agent");
        }
        if (str == null) {
          str = DEFAULT_USER_AGENT;
        }
      }
      localHttpProcessorBuilder = HttpProcessorBuilder.create();
      if (this.requestFirst == null) {
        break label589;
      }
      Iterator localIterator4 = this.requestFirst.iterator();
      while (localIterator4.hasNext()) {
        localHttpProcessorBuilder.addFirst((HttpRequestInterceptor)localIterator4.next());
      }
      arrayOfString1 = null;
      break;
      label505:
      arrayOfString2 = null;
      break label71;
      label511:
      if (this.systemProperties)
      {
        SSLSocketFactory localSSLSocketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
        localObject16 = new SSLConnectionSocketFactory(localSSLSocketFactory, arrayOfString1, arrayOfString2, localX509HostnameVerifier);
        break label117;
      }
      SSLContext localSSLContext1 = SSLContexts.createDefault();
      localObject16 = new SSLConnectionSocketFactory(localSSLContext1, localX509HostnameVerifier);
      break label117;
      localObject2 = NoConnectionReuseStrategy.INSTANCE;
      break label307;
      localObject2 = DefaultConnectionReuseStrategy.INSTANCE;
      break label307;
    }
    label589:
    if (this.responseFirst != null)
    {
      Iterator localIterator3 = this.responseFirst.iterator();
      while (localIterator3.hasNext()) {
        localHttpProcessorBuilder.addFirst((HttpResponseInterceptor)localIterator3.next());
      }
    }
    HttpRequestInterceptor[] arrayOfHttpRequestInterceptor = new HttpRequestInterceptor[6];
    arrayOfHttpRequestInterceptor[0] = new RequestDefaultHeaders(this.defaultHeaders);
    arrayOfHttpRequestInterceptor[1] = new RequestContent();
    arrayOfHttpRequestInterceptor[2] = new RequestTargetHost();
    arrayOfHttpRequestInterceptor[3] = new RequestClientConnControl();
    arrayOfHttpRequestInterceptor[4] = new RequestUserAgent(str);
    arrayOfHttpRequestInterceptor[5] = new RequestExpectContinue();
    localHttpProcessorBuilder.addAll(arrayOfHttpRequestInterceptor);
    if (!this.cookieManagementDisabled) {
      localHttpProcessorBuilder.add(new RequestAddCookies());
    }
    if (!this.contentCompressionDisabled) {
      localHttpProcessorBuilder.add(new RequestAcceptEncoding());
    }
    if (!this.authCachingDisabled) {
      localHttpProcessorBuilder.add(new RequestAuthCache());
    }
    if (!this.cookieManagementDisabled) {
      localHttpProcessorBuilder.add(new ResponseProcessCookies());
    }
    if (!this.contentCompressionDisabled) {
      localHttpProcessorBuilder.add(new ResponseContentEncoding());
    }
    if (this.requestLast != null)
    {
      Iterator localIterator2 = this.requestLast.iterator();
      while (localIterator2.hasNext()) {
        localHttpProcessorBuilder.addLast((HttpRequestInterceptor)localIterator2.next());
      }
    }
    if (this.responseLast != null)
    {
      Iterator localIterator1 = this.responseLast.iterator();
      while (localIterator1.hasNext()) {
        localHttpProcessorBuilder.addLast((HttpResponseInterceptor)localIterator1.next());
      }
    }
    HttpProcessor localHttpProcessor = localHttpProcessorBuilder.build();
    label918:
    ProtocolExec localProtocolExec = new ProtocolExec(localClientExecChain, localHttpProcessor);
    Object localObject7 = decorateProtocolExec(localProtocolExec);
    if (!this.automaticRetriesDisabled)
    {
      Object localObject15 = this.retryHandler;
      if (localObject15 == null) {
        localObject15 = DefaultHttpRequestRetryHandler.INSTANCE;
      }
      RetryExec localRetryExec = new RetryExec((ClientExecChain)localObject7, (HttpRequestRetryHandler)localObject15);
      localObject7 = localRetryExec;
    }
    Object localObject8 = this.routePlanner;
    Object localObject14;
    Object localObject9;
    Object localObject10;
    Object localObject11;
    Object localObject12;
    label1380:
    RequestConfig localRequestConfig;
    label1393:
    List localList;
    if (localObject8 == null)
    {
      localObject14 = this.schemePortResolver;
      if (localObject14 == null) {
        localObject14 = DefaultSchemePortResolver.INSTANCE;
      }
      if (this.proxy != null) {
        localObject8 = new DefaultProxyRoutePlanner(this.proxy, (SchemePortResolver)localObject14);
      }
    }
    else
    {
      if (!this.redirectHandlingDisabled)
      {
        Object localObject13 = this.redirectStrategy;
        if (localObject13 == null) {
          localObject13 = DefaultRedirectStrategy.INSTANCE;
        }
        RedirectExec localRedirectExec = new RedirectExec((ClientExecChain)localObject7, (HttpRoutePlanner)localObject8, (RedirectStrategy)localObject13);
        localObject7 = localRedirectExec;
      }
      ServiceUnavailableRetryStrategy localServiceUnavailableRetryStrategy = this.serviceUnavailStrategy;
      if (localServiceUnavailableRetryStrategy != null)
      {
        ServiceUnavailableRetryExec localServiceUnavailableRetryExec = new ServiceUnavailableRetryExec((ClientExecChain)localObject7, localServiceUnavailableRetryStrategy);
        localObject7 = localServiceUnavailableRetryExec;
      }
      BackoffManager localBackoffManager = this.backoffManager;
      ConnectionBackoffStrategy localConnectionBackoffStrategy = this.connectionBackoffStrategy;
      if ((localBackoffManager != null) && (localConnectionBackoffStrategy != null))
      {
        BackoffStrategyExec localBackoffStrategyExec = new BackoffStrategyExec((ClientExecChain)localObject7, localConnectionBackoffStrategy, localBackoffManager);
        localObject7 = localBackoffStrategyExec;
      }
      localObject9 = this.authSchemeRegistry;
      if (localObject9 == null) {
        localObject9 = RegistryBuilder.create().register("Basic", new BasicSchemeFactory()).register("Digest", new DigestSchemeFactory()).register("NTLM", new NTLMSchemeFactory()).register("negotiate", new SPNegoSchemeFactory()).register("Kerberos", new KerberosSchemeFactory()).build();
      }
      localObject10 = this.cookieSpecRegistry;
      if (localObject10 == null) {
        localObject10 = RegistryBuilder.create().register("best-match", new BestMatchSpecFactory()).register("standard", new RFC2965SpecFactory()).register("compatibility", new BrowserCompatSpecFactory()).register("netscape", new NetscapeDraftSpecFactory()).register("ignoreCookies", new IgnoreSpecFactory()).register("rfc2109", new RFC2109SpecFactory()).register("rfc2965", new RFC2965SpecFactory()).build();
      }
      localObject11 = this.cookieStore;
      if (localObject11 == null) {
        localObject11 = new BasicCookieStore();
      }
      localObject12 = this.credentialsProvider;
      if (localObject12 == null)
      {
        if (!this.systemProperties) {
          break label1484;
        }
        localObject12 = new SystemDefaultCredentialsProvider();
      }
      if (this.defaultRequestConfig == null) {
        break label1496;
      }
      localRequestConfig = this.defaultRequestConfig;
      if (this.closeables == null) {
        break label1504;
      }
      localList = this.closeables;
    }
    label1484:
    label1496:
    label1504:
    for (ArrayList localArrayList = new ArrayList(localList);; localArrayList = null)
    {
      return new InternalHttpClient((ClientExecChain)localObject7, (HttpClientConnectionManager)localObject1, (HttpRoutePlanner)localObject8, (Lookup)localObject10, (Lookup)localObject9, (CookieStore)localObject11, (CredentialsProvider)localObject12, localRequestConfig, localArrayList);
      if (this.systemProperties)
      {
        ProxySelector localProxySelector = ProxySelector.getDefault();
        localObject8 = new SystemDefaultRoutePlanner((SchemePortResolver)localObject14, localProxySelector);
        break;
      }
      localObject8 = new DefaultRoutePlanner((SchemePortResolver)localObject14);
      break;
      localObject12 = new BasicCredentialsProvider();
      break label1380;
      localRequestConfig = RequestConfig.DEFAULT;
      break label1393;
    }
  }
  
  protected ClientExecChain decorateMainExec(ClientExecChain paramClientExecChain)
  {
    return paramClientExecChain;
  }
  
  protected ClientExecChain decorateProtocolExec(ClientExecChain paramClientExecChain)
  {
    return paramClientExecChain;
  }
  
  public final HttpClientBuilder disableAuthCaching()
  {
    this.authCachingDisabled = true;
    return this;
  }
  
  public final HttpClientBuilder disableAutomaticRetries()
  {
    this.automaticRetriesDisabled = true;
    return this;
  }
  
  public final HttpClientBuilder disableConnectionState()
  {
    this.connectionStateDisabled = true;
    return this;
  }
  
  public final HttpClientBuilder disableContentCompression()
  {
    this.contentCompressionDisabled = true;
    return this;
  }
  
  public final HttpClientBuilder disableCookieManagement()
  {
    this.cookieManagementDisabled = true;
    return this;
  }
  
  public final HttpClientBuilder disableRedirectHandling()
  {
    this.redirectHandlingDisabled = true;
    return this;
  }
  
  public final HttpClientBuilder setBackoffManager(BackoffManager paramBackoffManager)
  {
    this.backoffManager = paramBackoffManager;
    return this;
  }
  
  public final HttpClientBuilder setConnectionBackoffStrategy(ConnectionBackoffStrategy paramConnectionBackoffStrategy)
  {
    this.connectionBackoffStrategy = paramConnectionBackoffStrategy;
    return this;
  }
  
  public final HttpClientBuilder setConnectionManager(HttpClientConnectionManager paramHttpClientConnectionManager)
  {
    this.connManager = paramHttpClientConnectionManager;
    return this;
  }
  
  public final HttpClientBuilder setConnectionReuseStrategy(ConnectionReuseStrategy paramConnectionReuseStrategy)
  {
    this.reuseStrategy = paramConnectionReuseStrategy;
    return this;
  }
  
  public final HttpClientBuilder setDefaultAuthSchemeRegistry(Lookup<AuthSchemeProvider> paramLookup)
  {
    this.authSchemeRegistry = paramLookup;
    return this;
  }
  
  public final HttpClientBuilder setDefaultConnectionConfig(ConnectionConfig paramConnectionConfig)
  {
    this.defaultConnectionConfig = paramConnectionConfig;
    return this;
  }
  
  public final HttpClientBuilder setDefaultCookieSpecRegistry(Lookup<CookieSpecProvider> paramLookup)
  {
    this.cookieSpecRegistry = paramLookup;
    return this;
  }
  
  public final HttpClientBuilder setDefaultCookieStore(CookieStore paramCookieStore)
  {
    this.cookieStore = paramCookieStore;
    return this;
  }
  
  public final HttpClientBuilder setDefaultCredentialsProvider(CredentialsProvider paramCredentialsProvider)
  {
    this.credentialsProvider = paramCredentialsProvider;
    return this;
  }
  
  public final HttpClientBuilder setDefaultHeaders(Collection<? extends Header> paramCollection)
  {
    this.defaultHeaders = paramCollection;
    return this;
  }
  
  public final HttpClientBuilder setDefaultRequestConfig(RequestConfig paramRequestConfig)
  {
    this.defaultRequestConfig = paramRequestConfig;
    return this;
  }
  
  public final HttpClientBuilder setDefaultSocketConfig(SocketConfig paramSocketConfig)
  {
    this.defaultSocketConfig = paramSocketConfig;
    return this;
  }
  
  public final HttpClientBuilder setHostnameVerifier(X509HostnameVerifier paramX509HostnameVerifier)
  {
    this.hostnameVerifier = paramX509HostnameVerifier;
    return this;
  }
  
  public final HttpClientBuilder setHttpProcessor(HttpProcessor paramHttpProcessor)
  {
    this.httpprocessor = paramHttpProcessor;
    return this;
  }
  
  public final HttpClientBuilder setKeepAliveStrategy(ConnectionKeepAliveStrategy paramConnectionKeepAliveStrategy)
  {
    this.keepAliveStrategy = paramConnectionKeepAliveStrategy;
    return this;
  }
  
  public final HttpClientBuilder setMaxConnPerRoute(int paramInt)
  {
    this.maxConnPerRoute = paramInt;
    return this;
  }
  
  public final HttpClientBuilder setMaxConnTotal(int paramInt)
  {
    this.maxConnTotal = paramInt;
    return this;
  }
  
  public final HttpClientBuilder setProxy(HttpHost paramHttpHost)
  {
    this.proxy = paramHttpHost;
    return this;
  }
  
  public final HttpClientBuilder setProxyAuthenticationStrategy(AuthenticationStrategy paramAuthenticationStrategy)
  {
    this.proxyAuthStrategy = paramAuthenticationStrategy;
    return this;
  }
  
  public final HttpClientBuilder setRedirectStrategy(RedirectStrategy paramRedirectStrategy)
  {
    this.redirectStrategy = paramRedirectStrategy;
    return this;
  }
  
  public final HttpClientBuilder setRequestExecutor(HttpRequestExecutor paramHttpRequestExecutor)
  {
    this.requestExec = paramHttpRequestExecutor;
    return this;
  }
  
  public final HttpClientBuilder setRetryHandler(HttpRequestRetryHandler paramHttpRequestRetryHandler)
  {
    this.retryHandler = paramHttpRequestRetryHandler;
    return this;
  }
  
  public final HttpClientBuilder setRoutePlanner(HttpRoutePlanner paramHttpRoutePlanner)
  {
    this.routePlanner = paramHttpRoutePlanner;
    return this;
  }
  
  public final HttpClientBuilder setSSLSocketFactory(LayeredConnectionSocketFactory paramLayeredConnectionSocketFactory)
  {
    this.sslSocketFactory = paramLayeredConnectionSocketFactory;
    return this;
  }
  
  public final HttpClientBuilder setSchemePortResolver(SchemePortResolver paramSchemePortResolver)
  {
    this.schemePortResolver = paramSchemePortResolver;
    return this;
  }
  
  public final HttpClientBuilder setServiceUnavailableRetryStrategy(ServiceUnavailableRetryStrategy paramServiceUnavailableRetryStrategy)
  {
    this.serviceUnavailStrategy = paramServiceUnavailableRetryStrategy;
    return this;
  }
  
  public final HttpClientBuilder setSslcontext(SSLContext paramSSLContext)
  {
    this.sslcontext = paramSSLContext;
    return this;
  }
  
  public final HttpClientBuilder setTargetAuthenticationStrategy(AuthenticationStrategy paramAuthenticationStrategy)
  {
    this.targetAuthStrategy = paramAuthenticationStrategy;
    return this;
  }
  
  public final HttpClientBuilder setUserAgent(String paramString)
  {
    this.userAgent = paramString;
    return this;
  }
  
  public final HttpClientBuilder setUserTokenHandler(UserTokenHandler paramUserTokenHandler)
  {
    this.userTokenHandler = paramUserTokenHandler;
    return this;
  }
  
  public final HttpClientBuilder useSystemProperties()
  {
    this.systemProperties = true;
    return this;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.HttpClientBuilder
 * JD-Core Version:    0.7.0.1
 */