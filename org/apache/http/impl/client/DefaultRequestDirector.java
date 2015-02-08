package org.apache.http.impl.client;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ProtocolException;
import org.apache.http.StatusLine;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.NonRepeatableRequestException;
import org.apache.http.client.RedirectException;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.RequestDirector;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.methods.AbortableHttpRequest;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.BasicManagedEntity;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.BasicRouteDirector;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRouteDirector;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.conn.ConnectionShutdownException;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

@Deprecated
@NotThreadSafe
public class DefaultRequestDirector
  implements RequestDirector
{
  private final HttpAuthenticator authenticator;
  protected final ClientConnectionManager connManager;
  private int execCount;
  protected final HttpProcessor httpProcessor;
  protected final ConnectionKeepAliveStrategy keepAliveStrategy;
  private final Log log;
  protected ManagedClientConnection managedConn;
  private final int maxRedirects;
  protected final HttpParams params;
  @Deprecated
  protected final AuthenticationHandler proxyAuthHandler;
  protected final AuthState proxyAuthState;
  protected final AuthenticationStrategy proxyAuthStrategy;
  private int redirectCount;
  @Deprecated
  protected final RedirectHandler redirectHandler;
  protected final RedirectStrategy redirectStrategy;
  protected final HttpRequestExecutor requestExec;
  protected final HttpRequestRetryHandler retryHandler;
  protected final ConnectionReuseStrategy reuseStrategy;
  protected final HttpRoutePlanner routePlanner;
  @Deprecated
  protected final AuthenticationHandler targetAuthHandler;
  protected final AuthState targetAuthState;
  protected final AuthenticationStrategy targetAuthStrategy;
  protected final UserTokenHandler userTokenHandler;
  private HttpHost virtualHost;
  
  @Deprecated
  public DefaultRequestDirector(Log paramLog, HttpRequestExecutor paramHttpRequestExecutor, ClientConnectionManager paramClientConnectionManager, ConnectionReuseStrategy paramConnectionReuseStrategy, ConnectionKeepAliveStrategy paramConnectionKeepAliveStrategy, HttpRoutePlanner paramHttpRoutePlanner, HttpProcessor paramHttpProcessor, HttpRequestRetryHandler paramHttpRequestRetryHandler, RedirectStrategy paramRedirectStrategy, AuthenticationHandler paramAuthenticationHandler1, AuthenticationHandler paramAuthenticationHandler2, UserTokenHandler paramUserTokenHandler, HttpParams paramHttpParams)
  {
    this(LogFactory.getLog(DefaultRequestDirector.class), paramHttpRequestExecutor, paramClientConnectionManager, paramConnectionReuseStrategy, paramConnectionKeepAliveStrategy, paramHttpRoutePlanner, paramHttpProcessor, paramHttpRequestRetryHandler, paramRedirectStrategy, new AuthenticationStrategyAdaptor(paramAuthenticationHandler1), new AuthenticationStrategyAdaptor(paramAuthenticationHandler2), paramUserTokenHandler, paramHttpParams);
  }
  
  public DefaultRequestDirector(Log paramLog, HttpRequestExecutor paramHttpRequestExecutor, ClientConnectionManager paramClientConnectionManager, ConnectionReuseStrategy paramConnectionReuseStrategy, ConnectionKeepAliveStrategy paramConnectionKeepAliveStrategy, HttpRoutePlanner paramHttpRoutePlanner, HttpProcessor paramHttpProcessor, HttpRequestRetryHandler paramHttpRequestRetryHandler, RedirectStrategy paramRedirectStrategy, AuthenticationStrategy paramAuthenticationStrategy1, AuthenticationStrategy paramAuthenticationStrategy2, UserTokenHandler paramUserTokenHandler, HttpParams paramHttpParams)
  {
    Args.notNull(paramLog, "Log");
    Args.notNull(paramHttpRequestExecutor, "Request executor");
    Args.notNull(paramClientConnectionManager, "Client connection manager");
    Args.notNull(paramConnectionReuseStrategy, "Connection reuse strategy");
    Args.notNull(paramConnectionKeepAliveStrategy, "Connection keep alive strategy");
    Args.notNull(paramHttpRoutePlanner, "Route planner");
    Args.notNull(paramHttpProcessor, "HTTP protocol processor");
    Args.notNull(paramHttpRequestRetryHandler, "HTTP request retry handler");
    Args.notNull(paramRedirectStrategy, "Redirect strategy");
    Args.notNull(paramAuthenticationStrategy1, "Target authentication strategy");
    Args.notNull(paramAuthenticationStrategy2, "Proxy authentication strategy");
    Args.notNull(paramUserTokenHandler, "User token handler");
    Args.notNull(paramHttpParams, "HTTP parameters");
    this.log = paramLog;
    this.authenticator = new HttpAuthenticator(paramLog);
    this.requestExec = paramHttpRequestExecutor;
    this.connManager = paramClientConnectionManager;
    this.reuseStrategy = paramConnectionReuseStrategy;
    this.keepAliveStrategy = paramConnectionKeepAliveStrategy;
    this.routePlanner = paramHttpRoutePlanner;
    this.httpProcessor = paramHttpProcessor;
    this.retryHandler = paramHttpRequestRetryHandler;
    this.redirectStrategy = paramRedirectStrategy;
    this.targetAuthStrategy = paramAuthenticationStrategy1;
    this.proxyAuthStrategy = paramAuthenticationStrategy2;
    this.userTokenHandler = paramUserTokenHandler;
    this.params = paramHttpParams;
    if ((paramRedirectStrategy instanceof DefaultRedirectStrategyAdaptor))
    {
      this.redirectHandler = ((DefaultRedirectStrategyAdaptor)paramRedirectStrategy).getHandler();
      if (!(paramAuthenticationStrategy1 instanceof AuthenticationStrategyAdaptor)) {
        break label315;
      }
      this.targetAuthHandler = ((AuthenticationStrategyAdaptor)paramAuthenticationStrategy1).getHandler();
      label232:
      if (!(paramAuthenticationStrategy2 instanceof AuthenticationStrategyAdaptor)) {
        break label323;
      }
    }
    label315:
    label323:
    for (this.proxyAuthHandler = ((AuthenticationStrategyAdaptor)paramAuthenticationStrategy2).getHandler();; this.proxyAuthHandler = null)
    {
      this.managedConn = null;
      this.execCount = 0;
      this.redirectCount = 0;
      this.targetAuthState = new AuthState();
      this.proxyAuthState = new AuthState();
      this.maxRedirects = this.params.getIntParameter("http.protocol.max-redirects", 100);
      return;
      this.redirectHandler = null;
      break;
      this.targetAuthHandler = null;
      break label232;
    }
  }
  
  @Deprecated
  public DefaultRequestDirector(HttpRequestExecutor paramHttpRequestExecutor, ClientConnectionManager paramClientConnectionManager, ConnectionReuseStrategy paramConnectionReuseStrategy, ConnectionKeepAliveStrategy paramConnectionKeepAliveStrategy, HttpRoutePlanner paramHttpRoutePlanner, HttpProcessor paramHttpProcessor, HttpRequestRetryHandler paramHttpRequestRetryHandler, RedirectHandler paramRedirectHandler, AuthenticationHandler paramAuthenticationHandler1, AuthenticationHandler paramAuthenticationHandler2, UserTokenHandler paramUserTokenHandler, HttpParams paramHttpParams)
  {
    this(LogFactory.getLog(DefaultRequestDirector.class), paramHttpRequestExecutor, paramClientConnectionManager, paramConnectionReuseStrategy, paramConnectionKeepAliveStrategy, paramHttpRoutePlanner, paramHttpProcessor, paramHttpRequestRetryHandler, new DefaultRedirectStrategyAdaptor(paramRedirectHandler), new AuthenticationStrategyAdaptor(paramAuthenticationHandler1), new AuthenticationStrategyAdaptor(paramAuthenticationHandler2), paramUserTokenHandler, paramHttpParams);
  }
  
  private void abortConnection()
  {
    ManagedClientConnection localManagedClientConnection = this.managedConn;
    if (localManagedClientConnection != null) {
      this.managedConn = null;
    }
    try
    {
      localManagedClientConnection.abortConnection();
    }
    catch (IOException localIOException1)
    {
      for (;;)
      {
        try
        {
          localManagedClientConnection.releaseConnection();
          return;
        }
        catch (IOException localIOException2)
        {
          this.log.debug("Error releasing connection", localIOException2);
        }
        localIOException1 = localIOException1;
        if (this.log.isDebugEnabled()) {
          this.log.debug(localIOException1.getMessage(), localIOException1);
        }
      }
    }
  }
  
  private void tryConnect(RoutedRequest paramRoutedRequest, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    localHttpRoute = paramRoutedRequest.getRoute();
    RequestWrapper localRequestWrapper = paramRoutedRequest.getRequest();
    i = 0;
    for (;;)
    {
      paramHttpContext.setAttribute("http.request", localRequestWrapper);
      i++;
      try
      {
        if (!this.managedConn.isOpen()) {
          this.managedConn.open(localHttpRoute, paramHttpContext, this.params);
        }
        for (;;)
        {
          establishRoute(localHttpRoute, paramHttpContext);
          return;
          this.managedConn.setSocketTimeout(HttpConnectionParams.getSoTimeout(this.params));
        }
        try
        {
          this.managedConn.close();
          if (this.retryHandler.retryRequest(localIOException1, i, paramHttpContext))
          {
            if (!this.log.isInfoEnabled()) {
              continue;
            }
            this.log.info("I/O exception (" + localIOException1.getClass().getName() + ") caught when connecting to " + localHttpRoute + ": " + localIOException1.getMessage());
            if (this.log.isDebugEnabled()) {
              this.log.debug(localIOException1.getMessage(), localIOException1);
            }
            this.log.info("Retrying connect to " + localHttpRoute);
            continue;
          }
          throw localIOException1;
        }
        catch (IOException localIOException2)
        {
          break label91;
        }
      }
      catch (IOException localIOException1) {}
    }
  }
  
  private HttpResponse tryExecute(RoutedRequest paramRoutedRequest, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    RequestWrapper localRequestWrapper = paramRoutedRequest.getRequest();
    HttpRoute localHttpRoute = paramRoutedRequest.getRoute();
    Object localObject = null;
    for (;;)
    {
      this.execCount = (1 + this.execCount);
      localRequestWrapper.incrementExecCount();
      if (!localRequestWrapper.isRepeatable())
      {
        this.log.debug("Cannot retry non-repeatable request");
        if (localObject != null) {
          throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.  The cause lists the reason the original request failed.", (Throwable)localObject);
        }
        throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.");
      }
      try
      {
        if (!this.managedConn.isOpen())
        {
          if (!localHttpRoute.isTunnelled())
          {
            this.log.debug("Reopening the direct connection.");
            this.managedConn.open(localHttpRoute, paramHttpContext, this.params);
          }
        }
        else
        {
          if (this.log.isDebugEnabled()) {
            this.log.debug("Attempt " + this.execCount + " to execute request");
          }
          return this.requestExec.execute(localRequestWrapper, this.managedConn, paramHttpContext);
        }
        this.log.debug("Proxied connection. Need to start over.");
        return null;
      }
      catch (IOException localIOException1)
      {
        this.log.debug("Closing the connection.");
      }
      try
      {
        this.managedConn.close();
        label225:
        if (this.retryHandler.retryRequest(localIOException1, localRequestWrapper.getExecCount(), paramHttpContext))
        {
          if (this.log.isInfoEnabled()) {
            this.log.info("I/O exception (" + localIOException1.getClass().getName() + ") caught when processing request to " + localHttpRoute + ": " + localIOException1.getMessage());
          }
          if (this.log.isDebugEnabled()) {
            this.log.debug(localIOException1.getMessage(), localIOException1);
          }
          if (this.log.isInfoEnabled()) {
            this.log.info("Retrying request to " + localHttpRoute);
          }
          localObject = localIOException1;
          continue;
        }
        if ((localIOException1 instanceof NoHttpResponseException))
        {
          NoHttpResponseException localNoHttpResponseException = new NoHttpResponseException(localHttpRoute.getTargetHost().toHostString() + " failed to respond");
          localNoHttpResponseException.setStackTrace(localIOException1.getStackTrace());
          throw localNoHttpResponseException;
        }
        throw localIOException1;
      }
      catch (IOException localIOException2)
      {
        break label225;
      }
    }
  }
  
  private RequestWrapper wrapRequest(HttpRequest paramHttpRequest)
    throws ProtocolException
  {
    if ((paramHttpRequest instanceof HttpEntityEnclosingRequest)) {
      return new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)paramHttpRequest);
    }
    return new RequestWrapper(paramHttpRequest);
  }
  
  protected HttpRequest createConnectRequest(HttpRoute paramHttpRoute, HttpContext paramHttpContext)
  {
    HttpHost localHttpHost = paramHttpRoute.getTargetHost();
    String str = localHttpHost.getHostName();
    int i = localHttpHost.getPort();
    if (i < 0) {
      i = this.connManager.getSchemeRegistry().getScheme(localHttpHost.getSchemeName()).getDefaultPort();
    }
    StringBuilder localStringBuilder = new StringBuilder(6 + str.length());
    localStringBuilder.append(str);
    localStringBuilder.append(':');
    localStringBuilder.append(Integer.toString(i));
    return new BasicHttpRequest("CONNECT", localStringBuilder.toString(), HttpProtocolParams.getVersion(this.params));
  }
  
  protected boolean createTunnelToProxy(HttpRoute paramHttpRoute, int paramInt, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    throw new HttpException("Proxy chains are not supported.");
  }
  
  protected boolean createTunnelToTarget(HttpRoute paramHttpRoute, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    HttpHost localHttpHost1 = paramHttpRoute.getProxyHost();
    HttpHost localHttpHost2 = paramHttpRoute.getTargetHost();
    HttpResponse localHttpResponse;
    for (;;)
    {
      if (!this.managedConn.isOpen()) {
        this.managedConn.open(paramHttpRoute, paramHttpContext, this.params);
      }
      HttpRequest localHttpRequest = createConnectRequest(paramHttpRoute, paramHttpContext);
      localHttpRequest.setParams(this.params);
      paramHttpContext.setAttribute("http.target_host", localHttpHost2);
      paramHttpContext.setAttribute("http.proxy_host", localHttpHost1);
      paramHttpContext.setAttribute("http.connection", this.managedConn);
      paramHttpContext.setAttribute("http.request", localHttpRequest);
      this.requestExec.preProcess(localHttpRequest, this.httpProcessor, paramHttpContext);
      localHttpResponse = this.requestExec.execute(localHttpRequest, this.managedConn, paramHttpContext);
      localHttpResponse.setParams(this.params);
      this.requestExec.postProcess(localHttpResponse, this.httpProcessor, paramHttpContext);
      if (localHttpResponse.getStatusLine().getStatusCode() < 200) {
        throw new HttpException("Unexpected response to CONNECT request: " + localHttpResponse.getStatusLine());
      }
      if (HttpClientParams.isAuthenticating(this.params))
      {
        if ((!this.authenticator.isAuthenticationRequested(localHttpHost1, localHttpResponse, this.proxyAuthStrategy, this.proxyAuthState, paramHttpContext)) || (!this.authenticator.authenticate(localHttpHost1, localHttpResponse, this.proxyAuthStrategy, this.proxyAuthState, paramHttpContext))) {
          break;
        }
        if (this.reuseStrategy.keepAlive(localHttpResponse, paramHttpContext))
        {
          this.log.debug("Connection kept alive");
          EntityUtils.consume(localHttpResponse.getEntity());
        }
        else
        {
          this.managedConn.close();
        }
      }
    }
    if (localHttpResponse.getStatusLine().getStatusCode() > 299)
    {
      HttpEntity localHttpEntity = localHttpResponse.getEntity();
      if (localHttpEntity != null) {
        localHttpResponse.setEntity(new BufferedHttpEntity(localHttpEntity));
      }
      this.managedConn.close();
      throw new TunnelRefusedException("CONNECT refused by proxy: " + localHttpResponse.getStatusLine(), localHttpResponse);
    }
    this.managedConn.markReusable();
    return false;
  }
  
  protected HttpRoute determineRoute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws HttpException
  {
    HttpRoutePlanner localHttpRoutePlanner = this.routePlanner;
    if (paramHttpHost != null) {}
    for (;;)
    {
      return localHttpRoutePlanner.determineRoute(paramHttpHost, paramHttpRequest, paramHttpContext);
      paramHttpHost = (HttpHost)paramHttpRequest.getParams().getParameter("http.default-host");
    }
  }
  
  protected void establishRoute(HttpRoute paramHttpRoute, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    BasicRouteDirector localBasicRouteDirector = new BasicRouteDirector();
    HttpRoute localHttpRoute = this.managedConn.getRoute();
    int i = localBasicRouteDirector.nextStep(paramHttpRoute, localHttpRoute);
    switch (i)
    {
    default: 
      throw new IllegalStateException("Unknown step indicator " + i + " from RouteDirector.");
    case 1: 
    case 2: 
      this.managedConn.open(paramHttpRoute, paramHttpContext, this.params);
    case 0: 
    case 3: 
    case 4: 
    case 5: 
      while (i <= 0)
      {
        return;
        boolean bool2 = createTunnelToTarget(paramHttpRoute, paramHttpContext);
        this.log.debug("Tunnel to target created.");
        this.managedConn.tunnelTarget(bool2, this.params);
        continue;
        int j = -1 + localHttpRoute.getHopCount();
        boolean bool1 = createTunnelToProxy(paramHttpRoute, j, paramHttpContext);
        this.log.debug("Tunnel to proxy created.");
        this.managedConn.tunnelProxy(paramHttpRoute.getHopTarget(j), bool1, this.params);
        continue;
        this.managedConn.layerProtocol(paramHttpContext, this.params);
      }
    }
    throw new HttpException("Unable to establish route: planned = " + paramHttpRoute + "; current = " + localHttpRoute);
  }
  
  public HttpResponse execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    paramHttpContext.setAttribute("http.auth.target-scope", this.targetAuthState);
    paramHttpContext.setAttribute("http.auth.proxy-scope", this.proxyAuthState);
    HttpHost localHttpHost1 = paramHttpHost;
    RequestWrapper localRequestWrapper1 = wrapRequest(paramHttpRequest);
    localRequestWrapper1.setParams(this.params);
    HttpRoute localHttpRoute1 = determineRoute(localHttpHost1, localRequestWrapper1, paramHttpContext);
    this.virtualHost = ((HttpHost)localRequestWrapper1.getParams().getParameter("http.virtual-host"));
    HttpHost localHttpHost2;
    if ((this.virtualHost != null) && (this.virtualHost.getPort() == -1))
    {
      if (localHttpHost1 == null) {
        break label775;
      }
      localHttpHost2 = localHttpHost1;
      int j = localHttpHost2.getPort();
      if (j != -1)
      {
        HttpHost localHttpHost3 = new HttpHost(this.virtualHost.getHostName(), j, this.virtualHost.getSchemeName());
        this.virtualHost = localHttpHost3;
      }
    }
    Object localObject1 = new RoutedRequest(localRequestWrapper1, localHttpRoute1);
    boolean bool = false;
    int i = 0;
    HttpResponse localHttpResponse = null;
    if (i == 0) {}
    for (;;)
    {
      RoutedRequest localRoutedRequest;
      try
      {
        localRequestWrapper2 = ((RoutedRequest)localObject1).getRequest();
        localHttpRoute2 = ((RoutedRequest)localObject1).getRoute();
        localObject2 = paramHttpContext.getAttribute("http.user-token");
        if (this.managedConn == null)
        {
          localClientConnectionRequest = this.connManager.requestConnection(localHttpRoute2, localObject2);
          if ((paramHttpRequest instanceof AbortableHttpRequest)) {
            ((AbortableHttpRequest)paramHttpRequest).setConnectionRequest(localClientConnectionRequest);
          }
          l2 = HttpClientParams.getConnectionManagerTimeout(this.params);
        }
      }
      catch (ConnectionShutdownException localConnectionShutdownException)
      {
        for (;;)
        {
          try
          {
            ClientConnectionRequest localClientConnectionRequest;
            long l2;
            this.managedConn = localClientConnectionRequest.getConnection(l2, TimeUnit.MILLISECONDS);
            if ((HttpConnectionParams.isStaleCheckingEnabled(this.params)) && (this.managedConn.isOpen()))
            {
              this.log.debug("Stale connection check");
              if (this.managedConn.isStale())
              {
                this.log.debug("Stale connection detected");
                this.managedConn.close();
              }
            }
            if ((paramHttpRequest instanceof AbortableHttpRequest)) {
              ((AbortableHttpRequest)paramHttpRequest).setReleaseTrigger(this.managedConn);
            }
          }
          catch (InterruptedException localInterruptedException)
          {
            HttpRoute localHttpRoute2;
            Object localObject2;
            String str1;
            HttpParams localHttpParams;
            HttpRequestExecutor localHttpRequestExecutor;
            HttpProcessor localHttpProcessor;
            InterruptedIOException localInterruptedIOException;
            Thread.currentThread().interrupt();
            throw new InterruptedIOException();
          }
          try
          {
            tryConnect((RoutedRequest)localObject1, paramHttpContext);
            str1 = localRequestWrapper2.getURI().getUserInfo();
            if (str1 != null)
            {
              AuthState localAuthState = this.targetAuthState;
              BasicScheme localBasicScheme = new BasicScheme();
              UsernamePasswordCredentials localUsernamePasswordCredentials = new UsernamePasswordCredentials(str1);
              localAuthState.update(localBasicScheme, localUsernamePasswordCredentials);
            }
            if (this.virtualHost == null) {
              break label896;
            }
            localHttpHost1 = this.virtualHost;
            if (localHttpHost1 == null) {
              localHttpHost1 = localHttpRoute2.getTargetHost();
            }
            localRequestWrapper2.resetHeaders();
            rewriteRequestURI(localRequestWrapper2, localHttpRoute2);
            paramHttpContext.setAttribute("http.target_host", localHttpHost1);
            paramHttpContext.setAttribute("http.route", localHttpRoute2);
            paramHttpContext.setAttribute("http.connection", this.managedConn);
            this.requestExec.preProcess(localRequestWrapper2, this.httpProcessor, paramHttpContext);
            localHttpResponse = tryExecute((RoutedRequest)localObject1, paramHttpContext);
            if (localHttpResponse == null) {
              break;
            }
            localHttpParams = this.params;
            localHttpResponse.setParams(localHttpParams);
            localHttpRequestExecutor = this.requestExec;
            localHttpProcessor = this.httpProcessor;
            localHttpRequestExecutor.postProcess(localHttpResponse, localHttpProcessor, paramHttpContext);
            bool = this.reuseStrategy.keepAlive(localHttpResponse, paramHttpContext);
            if (bool)
            {
              long l1 = this.keepAliveStrategy.getKeepAliveDuration(localHttpResponse, paramHttpContext);
              if (this.log.isDebugEnabled())
              {
                if (l1 <= 0L) {
                  break label1152;
                }
                str2 = "for " + l1 + " " + TimeUnit.MILLISECONDS;
                this.log.debug("Connection can be kept alive " + str2);
              }
              this.managedConn.setIdleDuration(l1, TimeUnit.MILLISECONDS);
            }
            localRoutedRequest = handleResponse((RoutedRequest)localObject1, localHttpResponse, paramHttpContext);
            if (localRoutedRequest != null) {
              break label921;
            }
            i = 1;
            if (this.managedConn == null) {
              break;
            }
            if (localObject2 == null)
            {
              localObject2 = this.userTokenHandler.getUserToken(paramHttpContext);
              paramHttpContext.setAttribute("http.user-token", localObject2);
            }
            if (localObject2 == null) {
              break;
            }
            this.managedConn.setState(localObject2);
          }
          catch (TunnelRefusedException localTunnelRefusedException)
          {
            if (!this.log.isDebugEnabled()) {
              break label838;
            }
            this.log.debug(localTunnelRefusedException.getMessage());
            localHttpResponse = localTunnelRefusedException.getResponse();
          }
        }
        localConnectionShutdownException = localConnectionShutdownException;
        localInterruptedIOException = new InterruptedIOException("Connection has been shut down");
        localInterruptedIOException.initCause(localConnectionShutdownException);
        throw localInterruptedIOException;
        localHttpHost2 = localHttpRoute1.getTargetHost();
      }
      catch (HttpException localHttpException)
      {
        RequestWrapper localRequestWrapper2;
        abortConnection();
        throw localHttpException;
        if ((localHttpResponse == null) || (localHttpResponse.getEntity() == null) || (!localHttpResponse.getEntity().isStreaming()))
        {
          if (bool) {
            this.managedConn.markReusable();
          }
          releaseConnection();
          return localHttpResponse;
          URI localURI = localRequestWrapper2.getURI();
          if (!localURI.isAbsolute()) {
            continue;
          }
          localHttpHost1 = URIUtils.extractHost(localURI);
          continue;
          if (bool)
          {
            EntityUtils.consume(localHttpResponse.getEntity());
            this.managedConn.markReusable();
            if (localRoutedRequest.getRoute().equals(((RoutedRequest)localObject1).getRoute())) {
              break label1160;
            }
            releaseConnection();
            break label1160;
          }
          this.managedConn.close();
          if ((this.proxyAuthState.getState().compareTo(AuthProtocolState.CHALLENGED) > 0) && (this.proxyAuthState.getAuthScheme() != null) && (this.proxyAuthState.getAuthScheme().isConnectionBased()))
          {
            this.log.debug("Resetting proxy auth state");
            this.proxyAuthState.reset();
          }
          if ((this.targetAuthState.getState().compareTo(AuthProtocolState.CHALLENGED) <= 0) || (this.targetAuthState.getAuthScheme() == null) || (!this.targetAuthState.getAuthScheme().isConnectionBased())) {
            continue;
          }
          this.log.debug("Resetting target auth state");
          this.targetAuthState.reset();
          continue;
        }
      }
      catch (IOException localIOException)
      {
        abortConnection();
        throw localIOException;
        BasicManagedEntity localBasicManagedEntity = new BasicManagedEntity(localHttpResponse.getEntity(), this.managedConn, bool);
        localHttpResponse.setEntity(localBasicManagedEntity);
        return localHttpResponse;
      }
      catch (RuntimeException localRuntimeException)
      {
        label775:
        label838:
        abortConnection();
        label896:
        label921:
        throw localRuntimeException;
      }
      label1152:
      String str2 = "indefinitely";
      continue;
      label1160:
      localObject1 = localRoutedRequest;
    }
  }
  
  protected RoutedRequest handleResponse(RoutedRequest paramRoutedRequest, HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    HttpRoute localHttpRoute1 = paramRoutedRequest.getRoute();
    RequestWrapper localRequestWrapper1 = paramRoutedRequest.getRequest();
    HttpParams localHttpParams = localRequestWrapper1.getParams();
    if (HttpClientParams.isAuthenticating(localHttpParams))
    {
      Object localObject = (HttpHost)paramHttpContext.getAttribute("http.target_host");
      if (localObject == null) {
        localObject = localHttpRoute1.getTargetHost();
      }
      if (((HttpHost)localObject).getPort() < 0)
      {
        Scheme localScheme = this.connManager.getSchemeRegistry().getScheme((HttpHost)localObject);
        HttpHost localHttpHost3 = new HttpHost(((HttpHost)localObject).getHostName(), localScheme.getDefaultPort(), ((HttpHost)localObject).getSchemeName());
        localObject = localHttpHost3;
      }
      boolean bool1 = this.authenticator.isAuthenticationRequested((HttpHost)localObject, paramHttpResponse, this.targetAuthStrategy, this.targetAuthState, paramHttpContext);
      HttpHost localHttpHost2 = localHttpRoute1.getProxyHost();
      if (localHttpHost2 == null) {
        localHttpHost2 = localHttpRoute1.getTargetHost();
      }
      boolean bool2 = this.authenticator.isAuthenticationRequested(localHttpHost2, paramHttpResponse, this.proxyAuthStrategy, this.proxyAuthState, paramHttpContext);
      if (bool1)
      {
        HttpAuthenticator localHttpAuthenticator = this.authenticator;
        AuthenticationStrategy localAuthenticationStrategy = this.targetAuthStrategy;
        AuthState localAuthState = this.targetAuthState;
        if (!localHttpAuthenticator.authenticate((HttpHost)localObject, paramHttpResponse, localAuthenticationStrategy, localAuthState, paramHttpContext)) {}
      }
      while ((bool2) && (this.authenticator.authenticate(localHttpHost2, paramHttpResponse, this.proxyAuthStrategy, this.proxyAuthState, paramHttpContext))) {
        return paramRoutedRequest;
      }
    }
    if ((HttpClientParams.isRedirecting(localHttpParams)) && (this.redirectStrategy.isRedirected(localRequestWrapper1, paramHttpResponse, paramHttpContext)))
    {
      if (this.redirectCount >= this.maxRedirects) {
        throw new RedirectException("Maximum redirects (" + this.maxRedirects + ") exceeded");
      }
      this.redirectCount = (1 + this.redirectCount);
      this.virtualHost = null;
      HttpUriRequest localHttpUriRequest = this.redirectStrategy.getRedirect(localRequestWrapper1, paramHttpResponse, paramHttpContext);
      localHttpUriRequest.setHeaders(localRequestWrapper1.getOriginal().getAllHeaders());
      URI localURI = localHttpUriRequest.getURI();
      HttpHost localHttpHost1 = URIUtils.extractHost(localURI);
      if (localHttpHost1 == null) {
        throw new ProtocolException("Redirect URI does not specify a valid host name: " + localURI);
      }
      if (!localHttpRoute1.getTargetHost().equals(localHttpHost1))
      {
        this.log.debug("Resetting target auth state");
        this.targetAuthState.reset();
        AuthScheme localAuthScheme = this.proxyAuthState.getAuthScheme();
        if ((localAuthScheme != null) && (localAuthScheme.isConnectionBased()))
        {
          this.log.debug("Resetting proxy auth state");
          this.proxyAuthState.reset();
        }
      }
      RequestWrapper localRequestWrapper2 = wrapRequest(localHttpUriRequest);
      localRequestWrapper2.setParams(localHttpParams);
      HttpRoute localHttpRoute2 = determineRoute(localHttpHost1, localRequestWrapper2, paramHttpContext);
      RoutedRequest localRoutedRequest = new RoutedRequest(localRequestWrapper2, localHttpRoute2);
      if (this.log.isDebugEnabled()) {
        this.log.debug("Redirecting to '" + localURI + "' via " + localHttpRoute2);
      }
      return localRoutedRequest;
    }
    return null;
  }
  
  protected void releaseConnection()
  {
    try
    {
      this.managedConn.releaseConnection();
      this.managedConn = null;
      return;
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        this.log.debug("IOException releasing connection", localIOException);
      }
    }
  }
  
  /* Error */
  protected void rewriteRequestURI(RequestWrapper paramRequestWrapper, HttpRoute paramHttpRoute)
    throws ProtocolException
  {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual 677	org/apache/http/impl/client/RequestWrapper:getURI	()Ljava/net/URI;
    //   4: astore 4
    //   6: aload_2
    //   7: invokevirtual 452	org/apache/http/conn/routing/HttpRoute:getProxyHost	()Lorg/apache/http/HttpHost;
    //   10: ifnull +47 -> 57
    //   13: aload_2
    //   14: invokevirtual 323	org/apache/http/conn/routing/HttpRoute:isTunnelled	()Z
    //   17: ifne +40 -> 57
    //   20: aload 4
    //   22: invokevirtual 768	java/net/URI:isAbsolute	()Z
    //   25: ifne +22 -> 47
    //   28: aload 4
    //   30: aload_2
    //   31: invokevirtual 356	org/apache/http/conn/routing/HttpRoute:getTargetHost	()Lorg/apache/http/HttpHost;
    //   34: iconst_1
    //   35: invokestatic 869	org/apache/http/client/utils/URIUtils:rewriteURI	(Ljava/net/URI;Lorg/apache/http/HttpHost;Z)Ljava/net/URI;
    //   38: astore 6
    //   40: aload_1
    //   41: aload 6
    //   43: invokevirtual 873	org/apache/http/impl/client/RequestWrapper:setURI	(Ljava/net/URI;)V
    //   46: return
    //   47: aload 4
    //   49: invokestatic 876	org/apache/http/client/utils/URIUtils:rewriteURI	(Ljava/net/URI;)Ljava/net/URI;
    //   52: astore 6
    //   54: goto -14 -> 40
    //   57: aload 4
    //   59: invokevirtual 768	java/net/URI:isAbsolute	()Z
    //   62: ifeq +15 -> 77
    //   65: aload 4
    //   67: aconst_null
    //   68: iconst_1
    //   69: invokestatic 869	org/apache/http/client/utils/URIUtils:rewriteURI	(Ljava/net/URI;Lorg/apache/http/HttpHost;Z)Ljava/net/URI;
    //   72: astore 6
    //   74: goto -34 -> 40
    //   77: aload 4
    //   79: invokestatic 876	org/apache/http/client/utils/URIUtils:rewriteURI	(Ljava/net/URI;)Ljava/net/URI;
    //   82: astore 5
    //   84: aload 5
    //   86: astore 6
    //   88: goto -48 -> 40
    //   91: astore_3
    //   92: new 376	org/apache/http/ProtocolException
    //   95: dup
    //   96: new 260	java/lang/StringBuilder
    //   99: dup
    //   100: invokespecial 261	java/lang/StringBuilder:<init>	()V
    //   103: ldc_w 878
    //   106: invokevirtual 267	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   109: aload_1
    //   110: invokevirtual 882	org/apache/http/impl/client/RequestWrapper:getRequestLine	()Lorg/apache/http/RequestLine;
    //   113: invokeinterface 887 1 0
    //   118: invokevirtual 267	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   121: invokevirtual 286	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   124: aload_3
    //   125: invokespecial 888	org/apache/http/ProtocolException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   128: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	129	0	this	DefaultRequestDirector
    //   0	129	1	paramRequestWrapper	RequestWrapper
    //   0	129	2	paramHttpRoute	HttpRoute
    //   91	34	3	localURISyntaxException	java.net.URISyntaxException
    //   4	74	4	localURI1	URI
    //   82	3	5	localURI2	URI
    //   38	49	6	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   0	40	91	java/net/URISyntaxException
    //   40	46	91	java/net/URISyntaxException
    //   47	54	91	java/net/URISyntaxException
    //   57	74	91	java/net/URISyntaxException
    //   77	84	91	java/net/URISyntaxException
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.DefaultRequestDirector
 * JD-Core Version:    0.7.0.1
 */