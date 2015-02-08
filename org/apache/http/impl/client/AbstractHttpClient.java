package org.apache.http.impl.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.BackoffManager;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.RequestDirector;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionManagerFactory;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.CookieSpecRegistry;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.impl.conn.DefaultHttpRoutePlanner;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.impl.cookie.IgnoreSpecFactory;
import org.apache.http.impl.cookie.NetscapeDraftSpecFactory;
import org.apache.http.impl.cookie.RFC2109SpecFactory;
import org.apache.http.impl.cookie.RFC2965SpecFactory;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;

@Deprecated
@ThreadSafe
public abstract class AbstractHttpClient
  extends CloseableHttpClient
{
  @GuardedBy("this")
  private BackoffManager backoffManager;
  @GuardedBy("this")
  private ClientConnectionManager connManager;
  @GuardedBy("this")
  private ConnectionBackoffStrategy connectionBackoffStrategy;
  @GuardedBy("this")
  private CookieStore cookieStore;
  @GuardedBy("this")
  private CredentialsProvider credsProvider;
  @GuardedBy("this")
  private HttpParams defaultParams;
  @GuardedBy("this")
  private ConnectionKeepAliveStrategy keepAliveStrategy;
  private final Log log = LogFactory.getLog(getClass());
  @GuardedBy("this")
  private BasicHttpProcessor mutableProcessor;
  @GuardedBy("this")
  private ImmutableHttpProcessor protocolProcessor;
  @GuardedBy("this")
  private AuthenticationStrategy proxyAuthStrategy;
  @GuardedBy("this")
  private RedirectStrategy redirectStrategy;
  @GuardedBy("this")
  private HttpRequestExecutor requestExec;
  @GuardedBy("this")
  private HttpRequestRetryHandler retryHandler;
  @GuardedBy("this")
  private ConnectionReuseStrategy reuseStrategy;
  @GuardedBy("this")
  private HttpRoutePlanner routePlanner;
  @GuardedBy("this")
  private AuthSchemeRegistry supportedAuthSchemes;
  @GuardedBy("this")
  private CookieSpecRegistry supportedCookieSpecs;
  @GuardedBy("this")
  private AuthenticationStrategy targetAuthStrategy;
  @GuardedBy("this")
  private UserTokenHandler userTokenHandler;
  
  protected AbstractHttpClient(ClientConnectionManager paramClientConnectionManager, HttpParams paramHttpParams)
  {
    this.defaultParams = paramHttpParams;
    this.connManager = paramClientConnectionManager;
  }
  
  private HttpProcessor getProtocolProcessor()
  {
    try
    {
      if (this.protocolProcessor == null)
      {
        BasicHttpProcessor localBasicHttpProcessor = getHttpProcessor();
        int i = localBasicHttpProcessor.getRequestInterceptorCount();
        HttpRequestInterceptor[] arrayOfHttpRequestInterceptor = new HttpRequestInterceptor[i];
        for (int j = 0; j < i; j++) {
          arrayOfHttpRequestInterceptor[j] = localBasicHttpProcessor.getRequestInterceptor(j);
        }
        int k = localBasicHttpProcessor.getResponseInterceptorCount();
        HttpResponseInterceptor[] arrayOfHttpResponseInterceptor = new HttpResponseInterceptor[k];
        for (int m = 0; m < k; m++) {
          arrayOfHttpResponseInterceptor[m] = localBasicHttpProcessor.getResponseInterceptor(m);
        }
        this.protocolProcessor = new ImmutableHttpProcessor(arrayOfHttpRequestInterceptor, arrayOfHttpResponseInterceptor);
      }
      ImmutableHttpProcessor localImmutableHttpProcessor = this.protocolProcessor;
      return localImmutableHttpProcessor;
    }
    finally {}
  }
  
  public void addRequestInterceptor(HttpRequestInterceptor paramHttpRequestInterceptor)
  {
    try
    {
      getHttpProcessor().addInterceptor(paramHttpRequestInterceptor);
      this.protocolProcessor = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void addRequestInterceptor(HttpRequestInterceptor paramHttpRequestInterceptor, int paramInt)
  {
    try
    {
      getHttpProcessor().addInterceptor(paramHttpRequestInterceptor, paramInt);
      this.protocolProcessor = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void addResponseInterceptor(HttpResponseInterceptor paramHttpResponseInterceptor)
  {
    try
    {
      getHttpProcessor().addInterceptor(paramHttpResponseInterceptor);
      this.protocolProcessor = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void addResponseInterceptor(HttpResponseInterceptor paramHttpResponseInterceptor, int paramInt)
  {
    try
    {
      getHttpProcessor().addInterceptor(paramHttpResponseInterceptor, paramInt);
      this.protocolProcessor = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void clearRequestInterceptors()
  {
    try
    {
      getHttpProcessor().clearRequestInterceptors();
      this.protocolProcessor = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void clearResponseInterceptors()
  {
    try
    {
      getHttpProcessor().clearResponseInterceptors();
      this.protocolProcessor = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void close()
  {
    getConnectionManager().shutdown();
  }
  
  protected AuthSchemeRegistry createAuthSchemeRegistry()
  {
    AuthSchemeRegistry localAuthSchemeRegistry = new AuthSchemeRegistry();
    localAuthSchemeRegistry.register("Basic", new BasicSchemeFactory());
    localAuthSchemeRegistry.register("Digest", new DigestSchemeFactory());
    localAuthSchemeRegistry.register("NTLM", new NTLMSchemeFactory());
    localAuthSchemeRegistry.register("negotiate", new SPNegoSchemeFactory());
    localAuthSchemeRegistry.register("Kerberos", new KerberosSchemeFactory());
    return localAuthSchemeRegistry;
  }
  
  protected ClientConnectionManager createClientConnectionManager()
  {
    SchemeRegistry localSchemeRegistry = SchemeRegistryFactory.createDefault();
    HttpParams localHttpParams = getParams();
    String str = (String)localHttpParams.getParameter("http.connection-manager.factory-class-name");
    ClientConnectionManagerFactory localClientConnectionManagerFactory = null;
    if (str != null) {}
    try
    {
      localClientConnectionManagerFactory = (ClientConnectionManagerFactory)Class.forName(str).newInstance();
      if (localClientConnectionManagerFactory != null) {
        return localClientConnectionManagerFactory.newInstance(localHttpParams, localSchemeRegistry);
      }
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      throw new IllegalStateException("Invalid class name: " + str);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      throw new IllegalAccessError(localIllegalAccessException.getMessage());
    }
    catch (InstantiationException localInstantiationException)
    {
      throw new InstantiationError(localInstantiationException.getMessage());
    }
    return new BasicClientConnectionManager(localSchemeRegistry);
  }
  
  @Deprecated
  protected RequestDirector createClientRequestDirector(HttpRequestExecutor paramHttpRequestExecutor, ClientConnectionManager paramClientConnectionManager, ConnectionReuseStrategy paramConnectionReuseStrategy, ConnectionKeepAliveStrategy paramConnectionKeepAliveStrategy, HttpRoutePlanner paramHttpRoutePlanner, HttpProcessor paramHttpProcessor, HttpRequestRetryHandler paramHttpRequestRetryHandler, RedirectHandler paramRedirectHandler, AuthenticationHandler paramAuthenticationHandler1, AuthenticationHandler paramAuthenticationHandler2, UserTokenHandler paramUserTokenHandler, HttpParams paramHttpParams)
  {
    return new DefaultRequestDirector(paramHttpRequestExecutor, paramClientConnectionManager, paramConnectionReuseStrategy, paramConnectionKeepAliveStrategy, paramHttpRoutePlanner, paramHttpProcessor, paramHttpRequestRetryHandler, paramRedirectHandler, paramAuthenticationHandler1, paramAuthenticationHandler2, paramUserTokenHandler, paramHttpParams);
  }
  
  @Deprecated
  protected RequestDirector createClientRequestDirector(HttpRequestExecutor paramHttpRequestExecutor, ClientConnectionManager paramClientConnectionManager, ConnectionReuseStrategy paramConnectionReuseStrategy, ConnectionKeepAliveStrategy paramConnectionKeepAliveStrategy, HttpRoutePlanner paramHttpRoutePlanner, HttpProcessor paramHttpProcessor, HttpRequestRetryHandler paramHttpRequestRetryHandler, RedirectStrategy paramRedirectStrategy, AuthenticationHandler paramAuthenticationHandler1, AuthenticationHandler paramAuthenticationHandler2, UserTokenHandler paramUserTokenHandler, HttpParams paramHttpParams)
  {
    return new DefaultRequestDirector(this.log, paramHttpRequestExecutor, paramClientConnectionManager, paramConnectionReuseStrategy, paramConnectionKeepAliveStrategy, paramHttpRoutePlanner, paramHttpProcessor, paramHttpRequestRetryHandler, paramRedirectStrategy, paramAuthenticationHandler1, paramAuthenticationHandler2, paramUserTokenHandler, paramHttpParams);
  }
  
  protected RequestDirector createClientRequestDirector(HttpRequestExecutor paramHttpRequestExecutor, ClientConnectionManager paramClientConnectionManager, ConnectionReuseStrategy paramConnectionReuseStrategy, ConnectionKeepAliveStrategy paramConnectionKeepAliveStrategy, HttpRoutePlanner paramHttpRoutePlanner, HttpProcessor paramHttpProcessor, HttpRequestRetryHandler paramHttpRequestRetryHandler, RedirectStrategy paramRedirectStrategy, AuthenticationStrategy paramAuthenticationStrategy1, AuthenticationStrategy paramAuthenticationStrategy2, UserTokenHandler paramUserTokenHandler, HttpParams paramHttpParams)
  {
    return new DefaultRequestDirector(this.log, paramHttpRequestExecutor, paramClientConnectionManager, paramConnectionReuseStrategy, paramConnectionKeepAliveStrategy, paramHttpRoutePlanner, paramHttpProcessor, paramHttpRequestRetryHandler, paramRedirectStrategy, paramAuthenticationStrategy1, paramAuthenticationStrategy2, paramUserTokenHandler, paramHttpParams);
  }
  
  protected ConnectionKeepAliveStrategy createConnectionKeepAliveStrategy()
  {
    return new DefaultConnectionKeepAliveStrategy();
  }
  
  protected ConnectionReuseStrategy createConnectionReuseStrategy()
  {
    return new DefaultConnectionReuseStrategy();
  }
  
  protected CookieSpecRegistry createCookieSpecRegistry()
  {
    CookieSpecRegistry localCookieSpecRegistry = new CookieSpecRegistry();
    localCookieSpecRegistry.register("best-match", new BestMatchSpecFactory());
    localCookieSpecRegistry.register("compatibility", new BrowserCompatSpecFactory());
    localCookieSpecRegistry.register("netscape", new NetscapeDraftSpecFactory());
    localCookieSpecRegistry.register("rfc2109", new RFC2109SpecFactory());
    localCookieSpecRegistry.register("rfc2965", new RFC2965SpecFactory());
    localCookieSpecRegistry.register("ignoreCookies", new IgnoreSpecFactory());
    return localCookieSpecRegistry;
  }
  
  protected CookieStore createCookieStore()
  {
    return new BasicCookieStore();
  }
  
  protected CredentialsProvider createCredentialsProvider()
  {
    return new BasicCredentialsProvider();
  }
  
  protected HttpContext createHttpContext()
  {
    BasicHttpContext localBasicHttpContext = new BasicHttpContext();
    localBasicHttpContext.setAttribute("http.scheme-registry", getConnectionManager().getSchemeRegistry());
    localBasicHttpContext.setAttribute("http.authscheme-registry", getAuthSchemes());
    localBasicHttpContext.setAttribute("http.cookiespec-registry", getCookieSpecs());
    localBasicHttpContext.setAttribute("http.cookie-store", getCookieStore());
    localBasicHttpContext.setAttribute("http.auth.credentials-provider", getCredentialsProvider());
    return localBasicHttpContext;
  }
  
  protected abstract HttpParams createHttpParams();
  
  protected abstract BasicHttpProcessor createHttpProcessor();
  
  protected HttpRequestRetryHandler createHttpRequestRetryHandler()
  {
    return new DefaultHttpRequestRetryHandler();
  }
  
  protected HttpRoutePlanner createHttpRoutePlanner()
  {
    return new DefaultHttpRoutePlanner(getConnectionManager().getSchemeRegistry());
  }
  
  @Deprecated
  protected AuthenticationHandler createProxyAuthenticationHandler()
  {
    return new DefaultProxyAuthenticationHandler();
  }
  
  protected AuthenticationStrategy createProxyAuthenticationStrategy()
  {
    return new ProxyAuthenticationStrategy();
  }
  
  @Deprecated
  protected RedirectHandler createRedirectHandler()
  {
    return new DefaultRedirectHandler();
  }
  
  protected HttpRequestExecutor createRequestExecutor()
  {
    return new HttpRequestExecutor();
  }
  
  @Deprecated
  protected AuthenticationHandler createTargetAuthenticationHandler()
  {
    return new DefaultTargetAuthenticationHandler();
  }
  
  protected AuthenticationStrategy createTargetAuthenticationStrategy()
  {
    return new TargetAuthenticationStrategy();
  }
  
  protected UserTokenHandler createUserTokenHandler()
  {
    return new DefaultUserTokenHandler();
  }
  
  protected HttpParams determineParams(HttpRequest paramHttpRequest)
  {
    return new ClientParamsStack(null, getParams(), paramHttpRequest.getParams(), null);
  }
  
  /* Error */
  protected final org.apache.http.client.methods.CloseableHttpResponse doExecute(org.apache.http.HttpHost paramHttpHost, HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws java.io.IOException, org.apache.http.client.ClientProtocolException
  {
    // Byte code:
    //   0: aload_2
    //   1: ldc_w 423
    //   4: invokestatic 429	org/apache/http/util/Args:notNull	(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   7: pop
    //   8: aload_0
    //   9: monitorenter
    //   10: aload_0
    //   11: invokevirtual 431	org/apache/http/impl/client/AbstractHttpClient:createHttpContext	()Lorg/apache/http/protocol/HttpContext;
    //   14: astore 6
    //   16: aload_3
    //   17: ifnonnull +176 -> 193
    //   20: aload 6
    //   22: astore 8
    //   24: aload_0
    //   25: aload_2
    //   26: invokevirtual 433	org/apache/http/impl/client/AbstractHttpClient:determineParams	(Lorg/apache/http/HttpRequest;)Lorg/apache/http/params/HttpParams;
    //   29: astore 9
    //   31: aload 9
    //   33: invokestatic 439	org/apache/http/client/params/HttpClientParamConfig:getRequestConfig	(Lorg/apache/http/params/HttpParams;)Lorg/apache/http/client/config/RequestConfig;
    //   36: astore 10
    //   38: aload 8
    //   40: ldc_w 441
    //   43: aload 10
    //   45: invokeinterface 334 3 0
    //   50: aload_0
    //   51: aload_0
    //   52: invokevirtual 444	org/apache/http/impl/client/AbstractHttpClient:getRequestExecutor	()Lorg/apache/http/protocol/HttpRequestExecutor;
    //   55: aload_0
    //   56: invokevirtual 131	org/apache/http/impl/client/AbstractHttpClient:getConnectionManager	()Lorg/apache/http/conn/ClientConnectionManager;
    //   59: aload_0
    //   60: invokevirtual 447	org/apache/http/impl/client/AbstractHttpClient:getConnectionReuseStrategy	()Lorg/apache/http/ConnectionReuseStrategy;
    //   63: aload_0
    //   64: invokevirtual 450	org/apache/http/impl/client/AbstractHttpClient:getConnectionKeepAliveStrategy	()Lorg/apache/http/conn/ConnectionKeepAliveStrategy;
    //   67: aload_0
    //   68: invokevirtual 453	org/apache/http/impl/client/AbstractHttpClient:getRoutePlanner	()Lorg/apache/http/conn/routing/HttpRoutePlanner;
    //   71: aload_0
    //   72: invokespecial 455	org/apache/http/impl/client/AbstractHttpClient:getProtocolProcessor	()Lorg/apache/http/protocol/HttpProcessor;
    //   75: aload_0
    //   76: invokevirtual 458	org/apache/http/impl/client/AbstractHttpClient:getHttpRequestRetryHandler	()Lorg/apache/http/client/HttpRequestRetryHandler;
    //   79: aload_0
    //   80: invokevirtual 462	org/apache/http/impl/client/AbstractHttpClient:getRedirectStrategy	()Lorg/apache/http/client/RedirectStrategy;
    //   83: aload_0
    //   84: invokevirtual 465	org/apache/http/impl/client/AbstractHttpClient:getTargetAuthenticationStrategy	()Lorg/apache/http/client/AuthenticationStrategy;
    //   87: aload_0
    //   88: invokevirtual 468	org/apache/http/impl/client/AbstractHttpClient:getProxyAuthenticationStrategy	()Lorg/apache/http/client/AuthenticationStrategy;
    //   91: aload_0
    //   92: invokevirtual 471	org/apache/http/impl/client/AbstractHttpClient:getUserTokenHandler	()Lorg/apache/http/client/UserTokenHandler;
    //   95: aload 9
    //   97: invokevirtual 473	org/apache/http/impl/client/AbstractHttpClient:createClientRequestDirector	(Lorg/apache/http/protocol/HttpRequestExecutor;Lorg/apache/http/conn/ClientConnectionManager;Lorg/apache/http/ConnectionReuseStrategy;Lorg/apache/http/conn/ConnectionKeepAliveStrategy;Lorg/apache/http/conn/routing/HttpRoutePlanner;Lorg/apache/http/protocol/HttpProcessor;Lorg/apache/http/client/HttpRequestRetryHandler;Lorg/apache/http/client/RedirectStrategy;Lorg/apache/http/client/AuthenticationStrategy;Lorg/apache/http/client/AuthenticationStrategy;Lorg/apache/http/client/UserTokenHandler;Lorg/apache/http/params/HttpParams;)Lorg/apache/http/client/RequestDirector;
    //   100: astore 11
    //   102: aload_0
    //   103: invokevirtual 453	org/apache/http/impl/client/AbstractHttpClient:getRoutePlanner	()Lorg/apache/http/conn/routing/HttpRoutePlanner;
    //   106: astore 12
    //   108: aload_0
    //   109: invokevirtual 477	org/apache/http/impl/client/AbstractHttpClient:getConnectionBackoffStrategy	()Lorg/apache/http/client/ConnectionBackoffStrategy;
    //   112: astore 13
    //   114: aload_0
    //   115: invokevirtual 481	org/apache/http/impl/client/AbstractHttpClient:getBackoffManager	()Lorg/apache/http/client/BackoffManager;
    //   118: astore 14
    //   120: aload_0
    //   121: monitorexit
    //   122: aload 13
    //   124: ifnull +227 -> 351
    //   127: aload 14
    //   129: ifnull +222 -> 351
    //   132: aload_1
    //   133: ifnull +86 -> 219
    //   136: aload_1
    //   137: astore 17
    //   139: aload 12
    //   141: aload 17
    //   143: aload_2
    //   144: aload 8
    //   146: invokeinterface 487 4 0
    //   151: astore 18
    //   153: aload 11
    //   155: aload_1
    //   156: aload_2
    //   157: aload 8
    //   159: invokeinterface 493 4 0
    //   164: invokestatic 499	org/apache/http/impl/client/CloseableHttpResponseProxy:newProxy	(Lorg/apache/http/HttpResponse;)Lorg/apache/http/client/methods/CloseableHttpResponse;
    //   167: astore 21
    //   169: aload 13
    //   171: aload 21
    //   173: invokeinterface 505 2 0
    //   178: ifeq +161 -> 339
    //   181: aload 14
    //   183: aload 18
    //   185: invokeinterface 511 2 0
    //   190: aload 21
    //   192: areturn
    //   193: new 513	org/apache/http/protocol/DefaultedHttpContext
    //   196: dup
    //   197: aload_3
    //   198: aload 6
    //   200: invokespecial 516	org/apache/http/protocol/DefaultedHttpContext:<init>	(Lorg/apache/http/protocol/HttpContext;Lorg/apache/http/protocol/HttpContext;)V
    //   203: astore 7
    //   205: aload 7
    //   207: astore 8
    //   209: goto -185 -> 24
    //   212: astore 5
    //   214: aload_0
    //   215: monitorexit
    //   216: aload 5
    //   218: athrow
    //   219: aload_0
    //   220: aload_2
    //   221: invokevirtual 433	org/apache/http/impl/client/AbstractHttpClient:determineParams	(Lorg/apache/http/HttpRequest;)Lorg/apache/http/params/HttpParams;
    //   224: ldc_w 518
    //   227: invokeinterface 195 2 0
    //   232: checkcast 520	org/apache/http/HttpHost
    //   235: astore 17
    //   237: goto -98 -> 139
    //   240: astore 20
    //   242: aload 13
    //   244: aload 20
    //   246: invokeinterface 523 2 0
    //   251: ifeq +12 -> 263
    //   254: aload 14
    //   256: aload 18
    //   258: invokeinterface 511 2 0
    //   263: aload 20
    //   265: athrow
    //   266: astore 16
    //   268: new 415	org/apache/http/client/ClientProtocolException
    //   271: dup
    //   272: aload 16
    //   274: invokespecial 526	org/apache/http/client/ClientProtocolException:<init>	(Ljava/lang/Throwable;)V
    //   277: athrow
    //   278: astore 19
    //   280: aload 13
    //   282: aload 19
    //   284: invokeinterface 523 2 0
    //   289: ifeq +12 -> 301
    //   292: aload 14
    //   294: aload 18
    //   296: invokeinterface 511 2 0
    //   301: aload 19
    //   303: instanceof 417
    //   306: ifeq +9 -> 315
    //   309: aload 19
    //   311: checkcast 417	org/apache/http/HttpException
    //   314: athrow
    //   315: aload 19
    //   317: instanceof 413
    //   320: ifeq +9 -> 329
    //   323: aload 19
    //   325: checkcast 413	java/io/IOException
    //   328: athrow
    //   329: new 528	java/lang/reflect/UndeclaredThrowableException
    //   332: dup
    //   333: aload 19
    //   335: invokespecial 529	java/lang/reflect/UndeclaredThrowableException:<init>	(Ljava/lang/Throwable;)V
    //   338: athrow
    //   339: aload 14
    //   341: aload 18
    //   343: invokeinterface 532 2 0
    //   348: aload 21
    //   350: areturn
    //   351: aload 11
    //   353: aload_1
    //   354: aload_2
    //   355: aload 8
    //   357: invokeinterface 493 4 0
    //   362: invokestatic 499	org/apache/http/impl/client/CloseableHttpResponseProxy:newProxy	(Lorg/apache/http/HttpResponse;)Lorg/apache/http/client/methods/CloseableHttpResponse;
    //   365: astore 15
    //   367: aload 15
    //   369: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	370	0	this	AbstractHttpClient
    //   0	370	1	paramHttpHost	org.apache.http.HttpHost
    //   0	370	2	paramHttpRequest	HttpRequest
    //   0	370	3	paramHttpContext	HttpContext
    //   212	5	5	localObject1	Object
    //   14	185	6	localHttpContext	HttpContext
    //   203	3	7	localDefaultedHttpContext	org.apache.http.protocol.DefaultedHttpContext
    //   22	334	8	localObject2	Object
    //   29	67	9	localHttpParams	HttpParams
    //   36	8	10	localRequestConfig	org.apache.http.client.config.RequestConfig
    //   100	252	11	localRequestDirector	RequestDirector
    //   106	34	12	localHttpRoutePlanner	HttpRoutePlanner
    //   112	169	13	localConnectionBackoffStrategy	ConnectionBackoffStrategy
    //   118	222	14	localBackoffManager	BackoffManager
    //   365	3	15	localCloseableHttpResponse1	org.apache.http.client.methods.CloseableHttpResponse
    //   266	7	16	localHttpException	org.apache.http.HttpException
    //   137	99	17	localHttpHost	org.apache.http.HttpHost
    //   151	191	18	localHttpRoute	org.apache.http.conn.routing.HttpRoute
    //   278	56	19	localException	java.lang.Exception
    //   240	24	20	localRuntimeException	java.lang.RuntimeException
    //   167	182	21	localCloseableHttpResponse2	org.apache.http.client.methods.CloseableHttpResponse
    // Exception table:
    //   from	to	target	type
    //   10	16	212	finally
    //   24	122	212	finally
    //   193	205	212	finally
    //   214	216	212	finally
    //   153	169	240	java/lang/RuntimeException
    //   139	153	266	org/apache/http/HttpException
    //   153	169	266	org/apache/http/HttpException
    //   169	190	266	org/apache/http/HttpException
    //   219	237	266	org/apache/http/HttpException
    //   242	263	266	org/apache/http/HttpException
    //   263	266	266	org/apache/http/HttpException
    //   280	301	266	org/apache/http/HttpException
    //   301	315	266	org/apache/http/HttpException
    //   315	329	266	org/apache/http/HttpException
    //   329	339	266	org/apache/http/HttpException
    //   339	348	266	org/apache/http/HttpException
    //   351	367	266	org/apache/http/HttpException
    //   153	169	278	java/lang/Exception
  }
  
  public final AuthSchemeRegistry getAuthSchemes()
  {
    try
    {
      if (this.supportedAuthSchemes == null) {
        this.supportedAuthSchemes = createAuthSchemeRegistry();
      }
      AuthSchemeRegistry localAuthSchemeRegistry = this.supportedAuthSchemes;
      return localAuthSchemeRegistry;
    }
    finally {}
  }
  
  public final BackoffManager getBackoffManager()
  {
    try
    {
      BackoffManager localBackoffManager = this.backoffManager;
      return localBackoffManager;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public final ConnectionBackoffStrategy getConnectionBackoffStrategy()
  {
    try
    {
      ConnectionBackoffStrategy localConnectionBackoffStrategy = this.connectionBackoffStrategy;
      return localConnectionBackoffStrategy;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public final ConnectionKeepAliveStrategy getConnectionKeepAliveStrategy()
  {
    try
    {
      if (this.keepAliveStrategy == null) {
        this.keepAliveStrategy = createConnectionKeepAliveStrategy();
      }
      ConnectionKeepAliveStrategy localConnectionKeepAliveStrategy = this.keepAliveStrategy;
      return localConnectionKeepAliveStrategy;
    }
    finally {}
  }
  
  public final ClientConnectionManager getConnectionManager()
  {
    try
    {
      if (this.connManager == null) {
        this.connManager = createClientConnectionManager();
      }
      ClientConnectionManager localClientConnectionManager = this.connManager;
      return localClientConnectionManager;
    }
    finally {}
  }
  
  public final ConnectionReuseStrategy getConnectionReuseStrategy()
  {
    try
    {
      if (this.reuseStrategy == null) {
        this.reuseStrategy = createConnectionReuseStrategy();
      }
      ConnectionReuseStrategy localConnectionReuseStrategy = this.reuseStrategy;
      return localConnectionReuseStrategy;
    }
    finally {}
  }
  
  public final CookieSpecRegistry getCookieSpecs()
  {
    try
    {
      if (this.supportedCookieSpecs == null) {
        this.supportedCookieSpecs = createCookieSpecRegistry();
      }
      CookieSpecRegistry localCookieSpecRegistry = this.supportedCookieSpecs;
      return localCookieSpecRegistry;
    }
    finally {}
  }
  
  public final CookieStore getCookieStore()
  {
    try
    {
      if (this.cookieStore == null) {
        this.cookieStore = createCookieStore();
      }
      CookieStore localCookieStore = this.cookieStore;
      return localCookieStore;
    }
    finally {}
  }
  
  public final CredentialsProvider getCredentialsProvider()
  {
    try
    {
      if (this.credsProvider == null) {
        this.credsProvider = createCredentialsProvider();
      }
      CredentialsProvider localCredentialsProvider = this.credsProvider;
      return localCredentialsProvider;
    }
    finally {}
  }
  
  protected final BasicHttpProcessor getHttpProcessor()
  {
    try
    {
      if (this.mutableProcessor == null) {
        this.mutableProcessor = createHttpProcessor();
      }
      BasicHttpProcessor localBasicHttpProcessor = this.mutableProcessor;
      return localBasicHttpProcessor;
    }
    finally {}
  }
  
  public final HttpRequestRetryHandler getHttpRequestRetryHandler()
  {
    try
    {
      if (this.retryHandler == null) {
        this.retryHandler = createHttpRequestRetryHandler();
      }
      HttpRequestRetryHandler localHttpRequestRetryHandler = this.retryHandler;
      return localHttpRequestRetryHandler;
    }
    finally {}
  }
  
  public final HttpParams getParams()
  {
    try
    {
      if (this.defaultParams == null) {
        this.defaultParams = createHttpParams();
      }
      HttpParams localHttpParams = this.defaultParams;
      return localHttpParams;
    }
    finally {}
  }
  
  @Deprecated
  public final AuthenticationHandler getProxyAuthenticationHandler()
  {
    try
    {
      AuthenticationHandler localAuthenticationHandler = createProxyAuthenticationHandler();
      return localAuthenticationHandler;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public final AuthenticationStrategy getProxyAuthenticationStrategy()
  {
    try
    {
      if (this.proxyAuthStrategy == null) {
        this.proxyAuthStrategy = createProxyAuthenticationStrategy();
      }
      AuthenticationStrategy localAuthenticationStrategy = this.proxyAuthStrategy;
      return localAuthenticationStrategy;
    }
    finally {}
  }
  
  @Deprecated
  public final RedirectHandler getRedirectHandler()
  {
    try
    {
      RedirectHandler localRedirectHandler = createRedirectHandler();
      return localRedirectHandler;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public final RedirectStrategy getRedirectStrategy()
  {
    try
    {
      if (this.redirectStrategy == null) {
        this.redirectStrategy = new DefaultRedirectStrategy();
      }
      RedirectStrategy localRedirectStrategy = this.redirectStrategy;
      return localRedirectStrategy;
    }
    finally {}
  }
  
  public final HttpRequestExecutor getRequestExecutor()
  {
    try
    {
      if (this.requestExec == null) {
        this.requestExec = createRequestExecutor();
      }
      HttpRequestExecutor localHttpRequestExecutor = this.requestExec;
      return localHttpRequestExecutor;
    }
    finally {}
  }
  
  public HttpRequestInterceptor getRequestInterceptor(int paramInt)
  {
    try
    {
      HttpRequestInterceptor localHttpRequestInterceptor = getHttpProcessor().getRequestInterceptor(paramInt);
      return localHttpRequestInterceptor;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public int getRequestInterceptorCount()
  {
    try
    {
      int i = getHttpProcessor().getRequestInterceptorCount();
      return i;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public HttpResponseInterceptor getResponseInterceptor(int paramInt)
  {
    try
    {
      HttpResponseInterceptor localHttpResponseInterceptor = getHttpProcessor().getResponseInterceptor(paramInt);
      return localHttpResponseInterceptor;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public int getResponseInterceptorCount()
  {
    try
    {
      int i = getHttpProcessor().getResponseInterceptorCount();
      return i;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public final HttpRoutePlanner getRoutePlanner()
  {
    try
    {
      if (this.routePlanner == null) {
        this.routePlanner = createHttpRoutePlanner();
      }
      HttpRoutePlanner localHttpRoutePlanner = this.routePlanner;
      return localHttpRoutePlanner;
    }
    finally {}
  }
  
  @Deprecated
  public final AuthenticationHandler getTargetAuthenticationHandler()
  {
    try
    {
      AuthenticationHandler localAuthenticationHandler = createTargetAuthenticationHandler();
      return localAuthenticationHandler;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public final AuthenticationStrategy getTargetAuthenticationStrategy()
  {
    try
    {
      if (this.targetAuthStrategy == null) {
        this.targetAuthStrategy = createTargetAuthenticationStrategy();
      }
      AuthenticationStrategy localAuthenticationStrategy = this.targetAuthStrategy;
      return localAuthenticationStrategy;
    }
    finally {}
  }
  
  public final UserTokenHandler getUserTokenHandler()
  {
    try
    {
      if (this.userTokenHandler == null) {
        this.userTokenHandler = createUserTokenHandler();
      }
      UserTokenHandler localUserTokenHandler = this.userTokenHandler;
      return localUserTokenHandler;
    }
    finally {}
  }
  
  public void removeRequestInterceptorByClass(Class<? extends HttpRequestInterceptor> paramClass)
  {
    try
    {
      getHttpProcessor().removeRequestInterceptorByClass(paramClass);
      this.protocolProcessor = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void removeResponseInterceptorByClass(Class<? extends HttpResponseInterceptor> paramClass)
  {
    try
    {
      getHttpProcessor().removeResponseInterceptorByClass(paramClass);
      this.protocolProcessor = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setAuthSchemes(AuthSchemeRegistry paramAuthSchemeRegistry)
  {
    try
    {
      this.supportedAuthSchemes = paramAuthSchemeRegistry;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setBackoffManager(BackoffManager paramBackoffManager)
  {
    try
    {
      this.backoffManager = paramBackoffManager;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setConnectionBackoffStrategy(ConnectionBackoffStrategy paramConnectionBackoffStrategy)
  {
    try
    {
      this.connectionBackoffStrategy = paramConnectionBackoffStrategy;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setCookieSpecs(CookieSpecRegistry paramCookieSpecRegistry)
  {
    try
    {
      this.supportedCookieSpecs = paramCookieSpecRegistry;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setCookieStore(CookieStore paramCookieStore)
  {
    try
    {
      this.cookieStore = paramCookieStore;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setCredentialsProvider(CredentialsProvider paramCredentialsProvider)
  {
    try
    {
      this.credsProvider = paramCredentialsProvider;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setHttpRequestRetryHandler(HttpRequestRetryHandler paramHttpRequestRetryHandler)
  {
    try
    {
      this.retryHandler = paramHttpRequestRetryHandler;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setKeepAliveStrategy(ConnectionKeepAliveStrategy paramConnectionKeepAliveStrategy)
  {
    try
    {
      this.keepAliveStrategy = paramConnectionKeepAliveStrategy;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setParams(HttpParams paramHttpParams)
  {
    try
    {
      this.defaultParams = paramHttpParams;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  @Deprecated
  public void setProxyAuthenticationHandler(AuthenticationHandler paramAuthenticationHandler)
  {
    try
    {
      this.proxyAuthStrategy = new AuthenticationStrategyAdaptor(paramAuthenticationHandler);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setProxyAuthenticationStrategy(AuthenticationStrategy paramAuthenticationStrategy)
  {
    try
    {
      this.proxyAuthStrategy = paramAuthenticationStrategy;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  @Deprecated
  public void setRedirectHandler(RedirectHandler paramRedirectHandler)
  {
    try
    {
      this.redirectStrategy = new DefaultRedirectStrategyAdaptor(paramRedirectHandler);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setRedirectStrategy(RedirectStrategy paramRedirectStrategy)
  {
    try
    {
      this.redirectStrategy = paramRedirectStrategy;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setReuseStrategy(ConnectionReuseStrategy paramConnectionReuseStrategy)
  {
    try
    {
      this.reuseStrategy = paramConnectionReuseStrategy;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setRoutePlanner(HttpRoutePlanner paramHttpRoutePlanner)
  {
    try
    {
      this.routePlanner = paramHttpRoutePlanner;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  @Deprecated
  public void setTargetAuthenticationHandler(AuthenticationHandler paramAuthenticationHandler)
  {
    try
    {
      this.targetAuthStrategy = new AuthenticationStrategyAdaptor(paramAuthenticationHandler);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setTargetAuthenticationStrategy(AuthenticationStrategy paramAuthenticationStrategy)
  {
    try
    {
      this.targetAuthStrategy = paramAuthenticationStrategy;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setUserTokenHandler(UserTokenHandler paramUserTokenHandler)
  {
    try
    {
      this.userTokenHandler = paramUserTokenHandler;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.AbstractHttpClient
 * JD-Core Version:    0.7.0.1
 */