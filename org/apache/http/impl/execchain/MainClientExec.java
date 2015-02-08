package org.apache.http.impl.execchain;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthState;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.BasicRouteDirector;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRouteDirector;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.auth.HttpAuthenticator;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

@Immutable
public class MainClientExec
  implements ClientExecChain
{
  private final HttpAuthenticator authenticator;
  private final HttpClientConnectionManager connManager;
  private final ConnectionKeepAliveStrategy keepAliveStrategy;
  private final Log log = LogFactory.getLog(getClass());
  private final AuthenticationStrategy proxyAuthStrategy;
  private final HttpProcessor proxyHttpProcessor;
  private final HttpRequestExecutor requestExecutor;
  private final ConnectionReuseStrategy reuseStrategy;
  private final HttpRouteDirector routeDirector;
  private final AuthenticationStrategy targetAuthStrategy;
  private final UserTokenHandler userTokenHandler;
  
  public MainClientExec(HttpRequestExecutor paramHttpRequestExecutor, HttpClientConnectionManager paramHttpClientConnectionManager, ConnectionReuseStrategy paramConnectionReuseStrategy, ConnectionKeepAliveStrategy paramConnectionKeepAliveStrategy, AuthenticationStrategy paramAuthenticationStrategy1, AuthenticationStrategy paramAuthenticationStrategy2, UserTokenHandler paramUserTokenHandler)
  {
    Args.notNull(paramHttpRequestExecutor, "HTTP request executor");
    Args.notNull(paramHttpClientConnectionManager, "Client connection manager");
    Args.notNull(paramConnectionReuseStrategy, "Connection reuse strategy");
    Args.notNull(paramConnectionKeepAliveStrategy, "Connection keep alive strategy");
    Args.notNull(paramAuthenticationStrategy1, "Target authentication strategy");
    Args.notNull(paramAuthenticationStrategy2, "Proxy authentication strategy");
    Args.notNull(paramUserTokenHandler, "User token handler");
    this.authenticator = new HttpAuthenticator();
    HttpRequestInterceptor[] arrayOfHttpRequestInterceptor = new HttpRequestInterceptor[2];
    arrayOfHttpRequestInterceptor[0] = new RequestTargetHost();
    arrayOfHttpRequestInterceptor[1] = new RequestClientConnControl();
    this.proxyHttpProcessor = new ImmutableHttpProcessor(arrayOfHttpRequestInterceptor);
    this.routeDirector = new BasicRouteDirector();
    this.requestExecutor = paramHttpRequestExecutor;
    this.connManager = paramHttpClientConnectionManager;
    this.reuseStrategy = paramConnectionReuseStrategy;
    this.keepAliveStrategy = paramConnectionKeepAliveStrategy;
    this.targetAuthStrategy = paramAuthenticationStrategy1;
    this.proxyAuthStrategy = paramAuthenticationStrategy2;
    this.userTokenHandler = paramUserTokenHandler;
  }
  
  private boolean createTunnelToProxy(HttpRoute paramHttpRoute, int paramInt, HttpClientContext paramHttpClientContext)
    throws HttpException
  {
    throw new HttpException("Proxy chains are not supported.");
  }
  
  private boolean createTunnelToTarget(AuthState paramAuthState, HttpClientConnection paramHttpClientConnection, HttpRoute paramHttpRoute, HttpRequest paramHttpRequest, HttpClientContext paramHttpClientContext)
    throws HttpException, IOException
  {
    RequestConfig localRequestConfig = paramHttpClientContext.getRequestConfig();
    int i = localRequestConfig.getConnectTimeout();
    HttpHost localHttpHost1 = paramHttpRoute.getTargetHost();
    HttpHost localHttpHost2 = paramHttpRoute.getProxyHost();
    BasicHttpRequest localBasicHttpRequest = new BasicHttpRequest("CONNECT", localHttpHost1.toHostString(), paramHttpRequest.getProtocolVersion());
    this.requestExecutor.preProcess(localBasicHttpRequest, this.proxyHttpProcessor, paramHttpClientContext);
    HttpResponse localHttpResponse;
    for (;;)
    {
      HttpClientConnectionManager localHttpClientConnectionManager;
      if (!paramHttpClientConnection.isOpen())
      {
        localHttpClientConnectionManager = this.connManager;
        if (i <= 0) {
          break label187;
        }
      }
      label187:
      for (int j = i;; j = 0)
      {
        localHttpClientConnectionManager.connect(paramHttpClientConnection, paramHttpRoute, j, paramHttpClientContext);
        localBasicHttpRequest.removeHeaders("Proxy-Authorization");
        this.authenticator.generateAuthResponse(localBasicHttpRequest, paramAuthState, paramHttpClientContext);
        localHttpResponse = this.requestExecutor.execute(localBasicHttpRequest, paramHttpClientConnection, paramHttpClientContext);
        if (localHttpResponse.getStatusLine().getStatusCode() >= 200) {
          break;
        }
        throw new HttpException("Unexpected response to CONNECT request: " + localHttpResponse.getStatusLine());
      }
      if (localRequestConfig.isAuthenticationEnabled())
      {
        if ((!this.authenticator.isAuthenticationRequested(localHttpHost2, localHttpResponse, this.proxyAuthStrategy, paramAuthState, paramHttpClientContext)) || (!this.authenticator.handleAuthChallenge(localHttpHost2, localHttpResponse, this.proxyAuthStrategy, paramAuthState, paramHttpClientContext))) {
          break;
        }
        if (this.reuseStrategy.keepAlive(localHttpResponse, paramHttpClientContext))
        {
          this.log.debug("Connection kept alive");
          EntityUtils.consume(localHttpResponse.getEntity());
        }
        else
        {
          paramHttpClientConnection.close();
        }
      }
    }
    if (localHttpResponse.getStatusLine().getStatusCode() > 299)
    {
      HttpEntity localHttpEntity = localHttpResponse.getEntity();
      if (localHttpEntity != null) {
        localHttpResponse.setEntity(new BufferedHttpEntity(localHttpEntity));
      }
      paramHttpClientConnection.close();
      throw new TunnelRefusedException("CONNECT refused by proxy: " + localHttpResponse.getStatusLine(), localHttpResponse);
    }
    return false;
  }
  
  private boolean needAuthentication(AuthState paramAuthState1, AuthState paramAuthState2, HttpRoute paramHttpRoute, HttpResponse paramHttpResponse, HttpClientContext paramHttpClientContext)
  {
    if (paramHttpClientContext.getRequestConfig().isAuthenticationEnabled())
    {
      HttpHost localHttpHost1 = paramHttpClientContext.getTargetHost();
      if (localHttpHost1 == null) {
        localHttpHost1 = paramHttpRoute.getTargetHost();
      }
      if (localHttpHost1.getPort() < 0) {
        localHttpHost1 = new HttpHost(localHttpHost1.getHostName(), paramHttpRoute.getTargetHost().getPort(), localHttpHost1.getSchemeName());
      }
      boolean bool1 = this.authenticator.isAuthenticationRequested(localHttpHost1, paramHttpResponse, this.targetAuthStrategy, paramAuthState1, paramHttpClientContext);
      HttpHost localHttpHost2 = paramHttpRoute.getProxyHost();
      if (localHttpHost2 == null) {
        localHttpHost2 = paramHttpRoute.getTargetHost();
      }
      boolean bool2 = this.authenticator.isAuthenticationRequested(localHttpHost2, paramHttpResponse, this.proxyAuthStrategy, paramAuthState2, paramHttpClientContext);
      if (bool1)
      {
        HttpAuthenticator localHttpAuthenticator = this.authenticator;
        AuthenticationStrategy localAuthenticationStrategy = this.targetAuthStrategy;
        return localHttpAuthenticator.handleAuthChallenge(localHttpHost1, paramHttpResponse, localAuthenticationStrategy, paramAuthState1, paramHttpClientContext);
      }
      if (bool2) {
        return this.authenticator.handleAuthChallenge(localHttpHost2, paramHttpResponse, this.proxyAuthStrategy, paramAuthState2, paramHttpClientContext);
      }
    }
    return false;
  }
  
  void establishRoute(AuthState paramAuthState, HttpClientConnection paramHttpClientConnection, HttpRoute paramHttpRoute, HttpRequest paramHttpRequest, HttpClientContext paramHttpClientContext)
    throws HttpException, IOException
  {
    int i = paramHttpClientContext.getRequestConfig().getConnectTimeout();
    RouteTracker localRouteTracker = new RouteTracker(paramHttpRoute);
    HttpRoute localHttpRoute = localRouteTracker.toRoute();
    int j = this.routeDirector.nextStep(paramHttpRoute, localHttpRoute);
    int n;
    switch (j)
    {
    default: 
      throw new IllegalStateException("Unknown step indicator " + j + " from RouteDirector.");
    case 1: 
      HttpClientConnectionManager localHttpClientConnectionManager2 = this.connManager;
      if (i > 0)
      {
        n = i;
        label134:
        localHttpClientConnectionManager2.connect(paramHttpClientConnection, paramHttpRoute, n, paramHttpClientContext);
        localRouteTracker.connectTarget(paramHttpRoute.isSecure());
      }
      break;
    }
    while (j <= 0)
    {
      return;
      n = 0;
      break label134;
      HttpClientConnectionManager localHttpClientConnectionManager1 = this.connManager;
      if (i > 0) {}
      for (int m = i;; m = 0)
      {
        localHttpClientConnectionManager1.connect(paramHttpClientConnection, paramHttpRoute, m, paramHttpClientContext);
        localRouteTracker.connectProxy(paramHttpRoute.getProxyHost(), false);
        break;
      }
      boolean bool2 = createTunnelToTarget(paramAuthState, paramHttpClientConnection, paramHttpRoute, paramHttpRequest, paramHttpClientContext);
      this.log.debug("Tunnel to target created.");
      localRouteTracker.tunnelTarget(bool2);
      continue;
      int k = -1 + localHttpRoute.getHopCount();
      boolean bool1 = createTunnelToProxy(paramHttpRoute, k, paramHttpClientContext);
      this.log.debug("Tunnel to proxy created.");
      localRouteTracker.tunnelProxy(paramHttpRoute.getHopTarget(k), bool1);
      continue;
      this.connManager.upgrade(paramHttpClientConnection, paramHttpRoute, paramHttpClientContext);
      localRouteTracker.layerProtocol(paramHttpRoute.isSecure());
      continue;
      throw new HttpException("Unable to establish route: planned = " + paramHttpRoute + "; current = " + localHttpRoute);
      this.connManager.routeComplete(paramHttpClientConnection, paramHttpRoute, paramHttpClientContext);
    }
  }
  
  /* Error */
  public org.apache.http.client.methods.CloseableHttpResponse execute(HttpRoute paramHttpRoute, org.apache.http.client.methods.HttpRequestWrapper paramHttpRequestWrapper, HttpClientContext paramHttpClientContext, org.apache.http.client.methods.HttpExecutionAware paramHttpExecutionAware)
    throws IOException, HttpException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc_w 364
    //   4: invokestatic 53	org/apache/http/util/Args:notNull	(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   7: pop
    //   8: aload_2
    //   9: ldc_w 366
    //   12: invokestatic 53	org/apache/http/util/Args:notNull	(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   15: pop
    //   16: aload_3
    //   17: ldc_w 368
    //   20: invokestatic 53	org/apache/http/util/Args:notNull	(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   23: pop
    //   24: aload_3
    //   25: invokevirtual 372	org/apache/http/client/protocol/HttpClientContext:getTargetAuthState	()Lorg/apache/http/auth/AuthState;
    //   28: astore 8
    //   30: aload 8
    //   32: ifnonnull +21 -> 53
    //   35: new 374	org/apache/http/auth/AuthState
    //   38: dup
    //   39: invokespecial 375	org/apache/http/auth/AuthState:<init>	()V
    //   42: astore 8
    //   44: aload_3
    //   45: ldc_w 377
    //   48: aload 8
    //   50: invokevirtual 381	org/apache/http/client/protocol/HttpClientContext:setAttribute	(Ljava/lang/String;Ljava/lang/Object;)V
    //   53: aload_3
    //   54: invokevirtual 384	org/apache/http/client/protocol/HttpClientContext:getProxyAuthState	()Lorg/apache/http/auth/AuthState;
    //   57: astore 9
    //   59: aload 9
    //   61: ifnonnull +21 -> 82
    //   64: new 374	org/apache/http/auth/AuthState
    //   67: dup
    //   68: invokespecial 375	org/apache/http/auth/AuthState:<init>	()V
    //   71: astore 9
    //   73: aload_3
    //   74: ldc_w 386
    //   77: aload 9
    //   79: invokevirtual 381	org/apache/http/client/protocol/HttpClientContext:setAttribute	(Ljava/lang/String;Ljava/lang/Object;)V
    //   82: aload_2
    //   83: instanceof 388
    //   86: ifeq +10 -> 96
    //   89: aload_2
    //   90: checkcast 388	org/apache/http/HttpEntityEnclosingRequest
    //   93: invokestatic 394	org/apache/http/impl/execchain/Proxies:enhanceEntity	(Lorg/apache/http/HttpEntityEnclosingRequest;)V
    //   96: aload_3
    //   97: invokevirtual 398	org/apache/http/client/protocol/HttpClientContext:getUserToken	()Ljava/lang/Object;
    //   100: astore 10
    //   102: aload_0
    //   103: getfield 94	org/apache/http/impl/execchain/MainClientExec:connManager	Lorg/apache/http/conn/HttpClientConnectionManager;
    //   106: aload_1
    //   107: aload 10
    //   109: invokeinterface 402 3 0
    //   114: astore 11
    //   116: aload 4
    //   118: ifnull +41 -> 159
    //   121: aload 4
    //   123: invokeinterface 407 1 0
    //   128: ifeq +22 -> 150
    //   131: aload 11
    //   133: invokeinterface 412 1 0
    //   138: pop
    //   139: new 414	org/apache/http/impl/execchain/RequestAbortedException
    //   142: dup
    //   143: ldc_w 416
    //   146: invokespecial 417	org/apache/http/impl/execchain/RequestAbortedException:<init>	(Ljava/lang/String;)V
    //   149: athrow
    //   150: aload 4
    //   152: aload 11
    //   154: invokeinterface 421 2 0
    //   159: aload_3
    //   160: invokevirtual 123	org/apache/http/client/protocol/HttpClientContext:getRequestConfig	()Lorg/apache/http/client/config/RequestConfig;
    //   163: astore 12
    //   165: aload 12
    //   167: invokevirtual 424	org/apache/http/client/config/RequestConfig:getConnectionRequestTimeout	()I
    //   170: istore 16
    //   172: iload 16
    //   174: ifle +175 -> 349
    //   177: iload 16
    //   179: i2l
    //   180: lstore 17
    //   182: aload 11
    //   184: lload 17
    //   186: getstatic 430	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
    //   189: invokeinterface 434 4 0
    //   194: astore 19
    //   196: aload_3
    //   197: ldc_w 436
    //   200: aload 19
    //   202: invokevirtual 381	org/apache/http/client/protocol/HttpClientContext:setAttribute	(Ljava/lang/String;Ljava/lang/Object;)V
    //   205: aload 12
    //   207: invokevirtual 439	org/apache/http/client/config/RequestConfig:isStaleConnectionCheckEnabled	()Z
    //   210: ifeq +54 -> 264
    //   213: aload 19
    //   215: invokeinterface 169 1 0
    //   220: ifeq +44 -> 264
    //   223: aload_0
    //   224: getfield 45	org/apache/http/impl/execchain/MainClientExec:log	Lorg/apache/commons/logging/Log;
    //   227: ldc_w 441
    //   230: invokeinterface 238 2 0
    //   235: aload 19
    //   237: invokeinterface 444 1 0
    //   242: ifeq +22 -> 264
    //   245: aload_0
    //   246: getfield 45	org/apache/http/impl/execchain/MainClientExec:log	Lorg/apache/commons/logging/Log;
    //   249: ldc_w 446
    //   252: invokeinterface 238 2 0
    //   257: aload 19
    //   259: invokeinterface 251 1 0
    //   264: new 448	org/apache/http/impl/execchain/ConnectionHolder
    //   267: dup
    //   268: aload_0
    //   269: getfield 45	org/apache/http/impl/execchain/MainClientExec:log	Lorg/apache/commons/logging/Log;
    //   272: aload_0
    //   273: getfield 94	org/apache/http/impl/execchain/MainClientExec:connManager	Lorg/apache/http/conn/HttpClientConnectionManager;
    //   276: aload 19
    //   278: invokespecial 451	org/apache/http/impl/execchain/ConnectionHolder:<init>	(Lorg/apache/commons/logging/Log;Lorg/apache/http/conn/HttpClientConnectionManager;Lorg/apache/http/HttpClientConnection;)V
    //   281: astore 20
    //   283: aload 4
    //   285: ifnull +919 -> 1204
    //   288: aload 4
    //   290: aload 20
    //   292: invokeinterface 421 2 0
    //   297: goto +907 -> 1204
    //   300: iload 21
    //   302: iconst_1
    //   303: if_icmple +104 -> 407
    //   306: aload_2
    //   307: invokestatic 455	org/apache/http/impl/execchain/Proxies:isRepeatable	(Lorg/apache/http/HttpRequest;)Z
    //   310: ifne +97 -> 407
    //   313: new 457	org/apache/http/client/NonRepeatableRequestException
    //   316: dup
    //   317: ldc_w 459
    //   320: invokespecial 460	org/apache/http/client/NonRepeatableRequestException:<init>	(Ljava/lang/String;)V
    //   323: athrow
    //   324: astore 25
    //   326: new 462	java/io/InterruptedIOException
    //   329: dup
    //   330: ldc_w 464
    //   333: invokespecial 465	java/io/InterruptedIOException:<init>	(Ljava/lang/String;)V
    //   336: astore 26
    //   338: aload 26
    //   340: aload 25
    //   342: invokevirtual 469	java/io/InterruptedIOException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   345: pop
    //   346: aload 26
    //   348: athrow
    //   349: lconst_0
    //   350: lstore 17
    //   352: goto -170 -> 182
    //   355: astore 15
    //   357: invokestatic 475	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   360: invokevirtual 478	java/lang/Thread:interrupt	()V
    //   363: new 414	org/apache/http/impl/execchain/RequestAbortedException
    //   366: dup
    //   367: ldc_w 416
    //   370: aload 15
    //   372: invokespecial 481	org/apache/http/impl/execchain/RequestAbortedException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   375: athrow
    //   376: astore 13
    //   378: aload 13
    //   380: invokevirtual 485	java/util/concurrent/ExecutionException:getCause	()Ljava/lang/Throwable;
    //   383: astore 14
    //   385: aload 14
    //   387: ifnonnull +7 -> 394
    //   390: aload 13
    //   392: astore 14
    //   394: new 414	org/apache/http/impl/execchain/RequestAbortedException
    //   397: dup
    //   398: ldc_w 487
    //   401: aload 14
    //   403: invokespecial 481	org/apache/http/impl/execchain/RequestAbortedException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   406: athrow
    //   407: aload 4
    //   409: ifnull +34 -> 443
    //   412: aload 4
    //   414: invokeinterface 407 1 0
    //   419: ifeq +24 -> 443
    //   422: new 414	org/apache/http/impl/execchain/RequestAbortedException
    //   425: dup
    //   426: ldc_w 416
    //   429: invokespecial 417	org/apache/http/impl/execchain/RequestAbortedException:<init>	(Ljava/lang/String;)V
    //   432: athrow
    //   433: astore 24
    //   435: aload 20
    //   437: invokevirtual 490	org/apache/http/impl/execchain/ConnectionHolder:abortConnection	()V
    //   440: aload 24
    //   442: athrow
    //   443: aload 19
    //   445: invokeinterface 169 1 0
    //   450: ifne +43 -> 493
    //   453: aload_0
    //   454: getfield 45	org/apache/http/impl/execchain/MainClientExec:log	Lorg/apache/commons/logging/Log;
    //   457: new 201	java/lang/StringBuilder
    //   460: dup
    //   461: invokespecial 202	java/lang/StringBuilder:<init>	()V
    //   464: ldc_w 492
    //   467: invokevirtual 208	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   470: aload_1
    //   471: invokevirtual 211	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   474: invokevirtual 214	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   477: invokeinterface 238 2 0
    //   482: aload_0
    //   483: aload 9
    //   485: aload 19
    //   487: aload_1
    //   488: aload_2
    //   489: aload_3
    //   490: invokevirtual 494	org/apache/http/impl/execchain/MainClientExec:establishRoute	(Lorg/apache/http/auth/AuthState;Lorg/apache/http/HttpClientConnection;Lorg/apache/http/conn/routing/HttpRoute;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/protocol/HttpClientContext;)V
    //   493: aload 12
    //   495: invokevirtual 497	org/apache/http/client/config/RequestConfig:getSocketTimeout	()I
    //   498: istore 28
    //   500: iload 28
    //   502: iflt +12 -> 514
    //   505: aload 19
    //   507: iload 28
    //   509: invokeinterface 501 2 0
    //   514: aload 4
    //   516: ifnull +143 -> 659
    //   519: aload 4
    //   521: invokeinterface 407 1 0
    //   526: ifeq +133 -> 659
    //   529: new 414	org/apache/http/impl/execchain/RequestAbortedException
    //   532: dup
    //   533: ldc_w 416
    //   536: invokespecial 417	org/apache/http/impl/execchain/RequestAbortedException:<init>	(Ljava/lang/String;)V
    //   539: athrow
    //   540: astore 23
    //   542: aload 20
    //   544: invokevirtual 490	org/apache/http/impl/execchain/ConnectionHolder:abortConnection	()V
    //   547: aload 23
    //   549: athrow
    //   550: astore 37
    //   552: aload_0
    //   553: getfield 45	org/apache/http/impl/execchain/MainClientExec:log	Lorg/apache/commons/logging/Log;
    //   556: invokeinterface 504 1 0
    //   561: ifeq +17 -> 578
    //   564: aload_0
    //   565: getfield 45	org/apache/http/impl/execchain/MainClientExec:log	Lorg/apache/commons/logging/Log;
    //   568: aload 37
    //   570: invokevirtual 507	org/apache/http/impl/execchain/TunnelRefusedException:getMessage	()Ljava/lang/String;
    //   573: invokeinterface 238 2 0
    //   578: aload 37
    //   580: invokevirtual 511	org/apache/http/impl/execchain/TunnelRefusedException:getResponse	()Lorg/apache/http/HttpResponse;
    //   583: astore 29
    //   585: aload 10
    //   587: ifnonnull +24 -> 611
    //   590: aload_0
    //   591: getfield 104	org/apache/http/impl/execchain/MainClientExec:userTokenHandler	Lorg/apache/http/client/UserTokenHandler;
    //   594: aload_3
    //   595: invokeinterface 516 2 0
    //   600: astore 10
    //   602: aload_3
    //   603: ldc_w 518
    //   606: aload 10
    //   608: invokevirtual 381	org/apache/http/client/protocol/HttpClientContext:setAttribute	(Ljava/lang/String;Ljava/lang/Object;)V
    //   611: aload 10
    //   613: ifnull +10 -> 623
    //   616: aload 20
    //   618: aload 10
    //   620: invokevirtual 521	org/apache/http/impl/execchain/ConnectionHolder:setState	(Ljava/lang/Object;)V
    //   623: aload 29
    //   625: invokeinterface 242 1 0
    //   630: astore 32
    //   632: aload 32
    //   634: ifnull +13 -> 647
    //   637: aload 32
    //   639: invokeinterface 526 1 0
    //   644: ifne +548 -> 1192
    //   647: aload 20
    //   649: invokevirtual 529	org/apache/http/impl/execchain/ConnectionHolder:releaseConnection	()V
    //   652: aload 29
    //   654: aconst_null
    //   655: invokestatic 533	org/apache/http/impl/execchain/Proxies:enhanceResponse	(Lorg/apache/http/HttpResponse;Lorg/apache/http/impl/execchain/ConnectionHolder;)Lorg/apache/http/client/methods/CloseableHttpResponse;
    //   658: areturn
    //   659: aload_0
    //   660: getfield 45	org/apache/http/impl/execchain/MainClientExec:log	Lorg/apache/commons/logging/Log;
    //   663: invokeinterface 504 1 0
    //   668: ifeq +35 -> 703
    //   671: aload_0
    //   672: getfield 45	org/apache/http/impl/execchain/MainClientExec:log	Lorg/apache/commons/logging/Log;
    //   675: new 201	java/lang/StringBuilder
    //   678: dup
    //   679: invokespecial 202	java/lang/StringBuilder:<init>	()V
    //   682: ldc_w 535
    //   685: invokevirtual 208	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   688: aload_2
    //   689: invokevirtual 541	org/apache/http/client/methods/HttpRequestWrapper:getRequestLine	()Lorg/apache/http/RequestLine;
    //   692: invokevirtual 211	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   695: invokevirtual 214	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   698: invokeinterface 238 2 0
    //   703: aload_2
    //   704: ldc_w 543
    //   707: invokevirtual 547	org/apache/http/client/methods/HttpRequestWrapper:containsHeader	(Ljava/lang/String;)Z
    //   710: ifne +59 -> 769
    //   713: aload_0
    //   714: getfield 45	org/apache/http/impl/execchain/MainClientExec:log	Lorg/apache/commons/logging/Log;
    //   717: invokeinterface 504 1 0
    //   722: ifeq +36 -> 758
    //   725: aload_0
    //   726: getfield 45	org/apache/http/impl/execchain/MainClientExec:log	Lorg/apache/commons/logging/Log;
    //   729: new 201	java/lang/StringBuilder
    //   732: dup
    //   733: invokespecial 202	java/lang/StringBuilder:<init>	()V
    //   736: ldc_w 549
    //   739: invokevirtual 208	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   742: aload 8
    //   744: invokevirtual 553	org/apache/http/auth/AuthState:getState	()Lorg/apache/http/auth/AuthProtocolState;
    //   747: invokevirtual 211	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   750: invokevirtual 214	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   753: invokeinterface 238 2 0
    //   758: aload_0
    //   759: getfield 70	org/apache/http/impl/execchain/MainClientExec:authenticator	Lorg/apache/http/impl/auth/HttpAuthenticator;
    //   762: aload_2
    //   763: aload 8
    //   765: aload_3
    //   766: invokevirtual 184	org/apache/http/impl/auth/HttpAuthenticator:generateAuthResponse	(Lorg/apache/http/HttpRequest;Lorg/apache/http/auth/AuthState;Lorg/apache/http/protocol/HttpContext;)V
    //   769: aload_2
    //   770: ldc 177
    //   772: invokevirtual 547	org/apache/http/client/methods/HttpRequestWrapper:containsHeader	(Ljava/lang/String;)Z
    //   775: ifne +66 -> 841
    //   778: aload_1
    //   779: invokevirtual 556	org/apache/http/conn/routing/HttpRoute:isTunnelled	()Z
    //   782: ifne +59 -> 841
    //   785: aload_0
    //   786: getfield 45	org/apache/http/impl/execchain/MainClientExec:log	Lorg/apache/commons/logging/Log;
    //   789: invokeinterface 504 1 0
    //   794: ifeq +36 -> 830
    //   797: aload_0
    //   798: getfield 45	org/apache/http/impl/execchain/MainClientExec:log	Lorg/apache/commons/logging/Log;
    //   801: new 201	java/lang/StringBuilder
    //   804: dup
    //   805: invokespecial 202	java/lang/StringBuilder:<init>	()V
    //   808: ldc_w 558
    //   811: invokevirtual 208	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   814: aload 9
    //   816: invokevirtual 553	org/apache/http/auth/AuthState:getState	()Lorg/apache/http/auth/AuthProtocolState;
    //   819: invokevirtual 211	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   822: invokevirtual 214	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   825: invokeinterface 238 2 0
    //   830: aload_0
    //   831: getfield 70	org/apache/http/impl/execchain/MainClientExec:authenticator	Lorg/apache/http/impl/auth/HttpAuthenticator;
    //   834: aload_2
    //   835: aload 9
    //   837: aload_3
    //   838: invokevirtual 184	org/apache/http/impl/auth/HttpAuthenticator:generateAuthResponse	(Lorg/apache/http/HttpRequest;Lorg/apache/http/auth/AuthState;Lorg/apache/http/protocol/HttpContext;)V
    //   841: aload_0
    //   842: getfield 92	org/apache/http/impl/execchain/MainClientExec:requestExecutor	Lorg/apache/http/protocol/HttpRequestExecutor;
    //   845: aload_2
    //   846: aload 19
    //   848: aload_3
    //   849: invokevirtual 188	org/apache/http/protocol/HttpRequestExecutor:execute	(Lorg/apache/http/HttpRequest;Lorg/apache/http/HttpClientConnection;Lorg/apache/http/protocol/HttpContext;)Lorg/apache/http/HttpResponse;
    //   852: astore 29
    //   854: aload_0
    //   855: getfield 96	org/apache/http/impl/execchain/MainClientExec:reuseStrategy	Lorg/apache/http/ConnectionReuseStrategy;
    //   858: aload 29
    //   860: aload_3
    //   861: invokeinterface 230 3 0
    //   866: ifeq +200 -> 1066
    //   869: aload_0
    //   870: getfield 98	org/apache/http/impl/execchain/MainClientExec:keepAliveStrategy	Lorg/apache/http/conn/ConnectionKeepAliveStrategy;
    //   873: aload 29
    //   875: aload_3
    //   876: invokeinterface 564 3 0
    //   881: lstore 34
    //   883: aload_0
    //   884: getfield 45	org/apache/http/impl/execchain/MainClientExec:log	Lorg/apache/commons/logging/Log;
    //   887: invokeinterface 504 1 0
    //   892: ifeq +75 -> 967
    //   895: lload 34
    //   897: lconst_0
    //   898: lcmp
    //   899: ifle +317 -> 1216
    //   902: new 201	java/lang/StringBuilder
    //   905: dup
    //   906: invokespecial 202	java/lang/StringBuilder:<init>	()V
    //   909: ldc_w 566
    //   912: invokevirtual 208	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   915: lload 34
    //   917: invokevirtual 569	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   920: ldc_w 571
    //   923: invokevirtual 208	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   926: getstatic 430	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
    //   929: invokevirtual 211	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   932: invokevirtual 214	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   935: astore 36
    //   937: aload_0
    //   938: getfield 45	org/apache/http/impl/execchain/MainClientExec:log	Lorg/apache/commons/logging/Log;
    //   941: new 201	java/lang/StringBuilder
    //   944: dup
    //   945: invokespecial 202	java/lang/StringBuilder:<init>	()V
    //   948: ldc_w 573
    //   951: invokevirtual 208	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   954: aload 36
    //   956: invokevirtual 208	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   959: invokevirtual 214	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   962: invokeinterface 238 2 0
    //   967: aload 20
    //   969: lload 34
    //   971: getstatic 430	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
    //   974: invokevirtual 577	org/apache/http/impl/execchain/ConnectionHolder:setValidFor	(JLjava/util/concurrent/TimeUnit;)V
    //   977: aload 20
    //   979: invokevirtual 580	org/apache/http/impl/execchain/ConnectionHolder:markReusable	()V
    //   982: aload_0
    //   983: aload 8
    //   985: aload 9
    //   987: aload_1
    //   988: aload 29
    //   990: aload_3
    //   991: invokespecial 582	org/apache/http/impl/execchain/MainClientExec:needAuthentication	(Lorg/apache/http/auth/AuthState;Lorg/apache/http/auth/AuthState;Lorg/apache/http/conn/routing/HttpRoute;Lorg/apache/http/HttpResponse;Lorg/apache/http/client/protocol/HttpClientContext;)Z
    //   994: ifeq -409 -> 585
    //   997: aload 29
    //   999: invokeinterface 242 1 0
    //   1004: astore 30
    //   1006: aload 20
    //   1008: invokevirtual 585	org/apache/http/impl/execchain/ConnectionHolder:isReusable	()Z
    //   1011: ifeq +73 -> 1084
    //   1014: aload 30
    //   1016: invokestatic 248	org/apache/http/util/EntityUtils:consume	(Lorg/apache/http/HttpEntity;)V
    //   1019: aload_2
    //   1020: invokevirtual 589	org/apache/http/client/methods/HttpRequestWrapper:getOriginal	()Lorg/apache/http/HttpRequest;
    //   1023: astore 31
    //   1025: aload 31
    //   1027: ldc_w 543
    //   1030: invokeinterface 590 2 0
    //   1035: ifne +10 -> 1045
    //   1038: aload_2
    //   1039: ldc_w 543
    //   1042: invokevirtual 591	org/apache/http/client/methods/HttpRequestWrapper:removeHeaders	(Ljava/lang/String;)V
    //   1045: aload 31
    //   1047: ldc 177
    //   1049: invokeinterface 590 2 0
    //   1054: ifne +156 -> 1210
    //   1057: aload_2
    //   1058: ldc 177
    //   1060: invokevirtual 591	org/apache/http/client/methods/HttpRequestWrapper:removeHeaders	(Ljava/lang/String;)V
    //   1063: goto +147 -> 1210
    //   1066: aload 20
    //   1068: invokevirtual 594	org/apache/http/impl/execchain/ConnectionHolder:markNonReusable	()V
    //   1071: goto -89 -> 982
    //   1074: astore 22
    //   1076: aload 20
    //   1078: invokevirtual 490	org/apache/http/impl/execchain/ConnectionHolder:abortConnection	()V
    //   1081: aload 22
    //   1083: athrow
    //   1084: aload 19
    //   1086: invokeinterface 251 1 0
    //   1091: aload 9
    //   1093: invokevirtual 553	org/apache/http/auth/AuthState:getState	()Lorg/apache/http/auth/AuthProtocolState;
    //   1096: getstatic 600	org/apache/http/auth/AuthProtocolState:SUCCESS	Lorg/apache/http/auth/AuthProtocolState;
    //   1099: if_acmpne +41 -> 1140
    //   1102: aload 9
    //   1104: invokevirtual 604	org/apache/http/auth/AuthState:getAuthScheme	()Lorg/apache/http/auth/AuthScheme;
    //   1107: ifnull +33 -> 1140
    //   1110: aload 9
    //   1112: invokevirtual 604	org/apache/http/auth/AuthState:getAuthScheme	()Lorg/apache/http/auth/AuthScheme;
    //   1115: invokeinterface 609 1 0
    //   1120: ifeq +20 -> 1140
    //   1123: aload_0
    //   1124: getfield 45	org/apache/http/impl/execchain/MainClientExec:log	Lorg/apache/commons/logging/Log;
    //   1127: ldc_w 611
    //   1130: invokeinterface 238 2 0
    //   1135: aload 9
    //   1137: invokevirtual 614	org/apache/http/auth/AuthState:reset	()V
    //   1140: aload 8
    //   1142: invokevirtual 553	org/apache/http/auth/AuthState:getState	()Lorg/apache/http/auth/AuthProtocolState;
    //   1145: getstatic 600	org/apache/http/auth/AuthProtocolState:SUCCESS	Lorg/apache/http/auth/AuthProtocolState;
    //   1148: if_acmpne -129 -> 1019
    //   1151: aload 8
    //   1153: invokevirtual 604	org/apache/http/auth/AuthState:getAuthScheme	()Lorg/apache/http/auth/AuthScheme;
    //   1156: ifnull -137 -> 1019
    //   1159: aload 8
    //   1161: invokevirtual 604	org/apache/http/auth/AuthState:getAuthScheme	()Lorg/apache/http/auth/AuthScheme;
    //   1164: invokeinterface 609 1 0
    //   1169: ifeq -150 -> 1019
    //   1172: aload_0
    //   1173: getfield 45	org/apache/http/impl/execchain/MainClientExec:log	Lorg/apache/commons/logging/Log;
    //   1176: ldc_w 616
    //   1179: invokeinterface 238 2 0
    //   1184: aload 8
    //   1186: invokevirtual 614	org/apache/http/auth/AuthState:reset	()V
    //   1189: goto -170 -> 1019
    //   1192: aload 29
    //   1194: aload 20
    //   1196: invokestatic 533	org/apache/http/impl/execchain/Proxies:enhanceResponse	(Lorg/apache/http/HttpResponse;Lorg/apache/http/impl/execchain/ConnectionHolder;)Lorg/apache/http/client/methods/CloseableHttpResponse;
    //   1199: astore 33
    //   1201: aload 33
    //   1203: areturn
    //   1204: iconst_1
    //   1205: istore 21
    //   1207: goto -907 -> 300
    //   1210: iinc 21 1
    //   1213: goto -913 -> 300
    //   1216: ldc_w 618
    //   1219: astore 36
    //   1221: goto -284 -> 937
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	1224	0	this	MainClientExec
    //   0	1224	1	paramHttpRoute	HttpRoute
    //   0	1224	2	paramHttpRequestWrapper	org.apache.http.client.methods.HttpRequestWrapper
    //   0	1224	3	paramHttpClientContext	HttpClientContext
    //   0	1224	4	paramHttpExecutionAware	org.apache.http.client.methods.HttpExecutionAware
    //   28	1157	8	localAuthState1	AuthState
    //   57	1079	9	localAuthState2	AuthState
    //   100	519	10	localObject1	Object
    //   114	69	11	localConnectionRequest	org.apache.http.conn.ConnectionRequest
    //   163	331	12	localRequestConfig	RequestConfig
    //   376	15	13	localExecutionException	java.util.concurrent.ExecutionException
    //   383	19	14	localObject2	Object
    //   355	16	15	localInterruptedException	java.lang.InterruptedException
    //   170	8	16	i	int
    //   180	171	17	l1	long
    //   194	891	19	localHttpClientConnection	HttpClientConnection
    //   281	914	20	localConnectionHolder	ConnectionHolder
    //   300	911	21	j	int
    //   1074	8	22	localRuntimeException	java.lang.RuntimeException
    //   540	8	23	localIOException	IOException
    //   433	8	24	localHttpException	HttpException
    //   324	17	25	localConnectionShutdownException	org.apache.http.impl.conn.ConnectionShutdownException
    //   336	11	26	localInterruptedIOException	java.io.InterruptedIOException
    //   498	10	28	k	int
    //   583	610	29	localHttpResponse	HttpResponse
    //   1004	11	30	localHttpEntity1	HttpEntity
    //   1023	23	31	localHttpRequest	HttpRequest
    //   630	8	32	localHttpEntity2	HttpEntity
    //   1199	3	33	localCloseableHttpResponse	org.apache.http.client.methods.CloseableHttpResponse
    //   881	89	34	l2	long
    //   935	285	36	str	java.lang.String
    //   550	29	37	localTunnelRefusedException	TunnelRefusedException
    // Exception table:
    //   from	to	target	type
    //   288	297	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   306	324	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   412	433	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   443	482	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   482	493	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   493	500	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   505	514	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   519	540	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   552	578	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   578	585	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   590	611	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   616	623	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   623	632	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   637	647	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   647	659	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   659	703	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   703	758	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   758	769	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   769	830	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   830	841	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   841	895	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   902	937	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   937	967	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   967	982	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   982	1019	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   1019	1045	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   1045	1063	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   1066	1071	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   1084	1140	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   1140	1189	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   1192	1201	324	org/apache/http/impl/conn/ConnectionShutdownException
    //   165	172	355	java/lang/InterruptedException
    //   182	196	355	java/lang/InterruptedException
    //   165	172	376	java/util/concurrent/ExecutionException
    //   182	196	376	java/util/concurrent/ExecutionException
    //   288	297	433	org/apache/http/HttpException
    //   306	324	433	org/apache/http/HttpException
    //   412	433	433	org/apache/http/HttpException
    //   443	482	433	org/apache/http/HttpException
    //   482	493	433	org/apache/http/HttpException
    //   493	500	433	org/apache/http/HttpException
    //   505	514	433	org/apache/http/HttpException
    //   519	540	433	org/apache/http/HttpException
    //   552	578	433	org/apache/http/HttpException
    //   578	585	433	org/apache/http/HttpException
    //   590	611	433	org/apache/http/HttpException
    //   616	623	433	org/apache/http/HttpException
    //   623	632	433	org/apache/http/HttpException
    //   637	647	433	org/apache/http/HttpException
    //   647	659	433	org/apache/http/HttpException
    //   659	703	433	org/apache/http/HttpException
    //   703	758	433	org/apache/http/HttpException
    //   758	769	433	org/apache/http/HttpException
    //   769	830	433	org/apache/http/HttpException
    //   830	841	433	org/apache/http/HttpException
    //   841	895	433	org/apache/http/HttpException
    //   902	937	433	org/apache/http/HttpException
    //   937	967	433	org/apache/http/HttpException
    //   967	982	433	org/apache/http/HttpException
    //   982	1019	433	org/apache/http/HttpException
    //   1019	1045	433	org/apache/http/HttpException
    //   1045	1063	433	org/apache/http/HttpException
    //   1066	1071	433	org/apache/http/HttpException
    //   1084	1140	433	org/apache/http/HttpException
    //   1140	1189	433	org/apache/http/HttpException
    //   1192	1201	433	org/apache/http/HttpException
    //   288	297	540	java/io/IOException
    //   306	324	540	java/io/IOException
    //   412	433	540	java/io/IOException
    //   443	482	540	java/io/IOException
    //   482	493	540	java/io/IOException
    //   493	500	540	java/io/IOException
    //   505	514	540	java/io/IOException
    //   519	540	540	java/io/IOException
    //   552	578	540	java/io/IOException
    //   578	585	540	java/io/IOException
    //   590	611	540	java/io/IOException
    //   616	623	540	java/io/IOException
    //   623	632	540	java/io/IOException
    //   637	647	540	java/io/IOException
    //   647	659	540	java/io/IOException
    //   659	703	540	java/io/IOException
    //   703	758	540	java/io/IOException
    //   758	769	540	java/io/IOException
    //   769	830	540	java/io/IOException
    //   830	841	540	java/io/IOException
    //   841	895	540	java/io/IOException
    //   902	937	540	java/io/IOException
    //   937	967	540	java/io/IOException
    //   967	982	540	java/io/IOException
    //   982	1019	540	java/io/IOException
    //   1019	1045	540	java/io/IOException
    //   1045	1063	540	java/io/IOException
    //   1066	1071	540	java/io/IOException
    //   1084	1140	540	java/io/IOException
    //   1140	1189	540	java/io/IOException
    //   1192	1201	540	java/io/IOException
    //   482	493	550	org/apache/http/impl/execchain/TunnelRefusedException
    //   288	297	1074	java/lang/RuntimeException
    //   306	324	1074	java/lang/RuntimeException
    //   412	433	1074	java/lang/RuntimeException
    //   443	482	1074	java/lang/RuntimeException
    //   482	493	1074	java/lang/RuntimeException
    //   493	500	1074	java/lang/RuntimeException
    //   505	514	1074	java/lang/RuntimeException
    //   519	540	1074	java/lang/RuntimeException
    //   552	578	1074	java/lang/RuntimeException
    //   578	585	1074	java/lang/RuntimeException
    //   590	611	1074	java/lang/RuntimeException
    //   616	623	1074	java/lang/RuntimeException
    //   623	632	1074	java/lang/RuntimeException
    //   637	647	1074	java/lang/RuntimeException
    //   647	659	1074	java/lang/RuntimeException
    //   659	703	1074	java/lang/RuntimeException
    //   703	758	1074	java/lang/RuntimeException
    //   758	769	1074	java/lang/RuntimeException
    //   769	830	1074	java/lang/RuntimeException
    //   830	841	1074	java/lang/RuntimeException
    //   841	895	1074	java/lang/RuntimeException
    //   902	937	1074	java/lang/RuntimeException
    //   937	967	1074	java/lang/RuntimeException
    //   967	982	1074	java/lang/RuntimeException
    //   982	1019	1074	java/lang/RuntimeException
    //   1019	1045	1074	java/lang/RuntimeException
    //   1045	1063	1074	java/lang/RuntimeException
    //   1066	1071	1074	java/lang/RuntimeException
    //   1084	1140	1074	java/lang/RuntimeException
    //   1140	1189	1074	java/lang/RuntimeException
    //   1192	1201	1074	java/lang/RuntimeException
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.execchain.MainClientExec
 * JD-Core Version:    0.7.0.1
 */