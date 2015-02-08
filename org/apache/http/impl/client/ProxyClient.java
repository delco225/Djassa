package org.apache.http.impl.client;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.params.HttpClientParamConfig;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteInfo.LayerType;
import org.apache.http.conn.routing.RouteInfo.TunnelType;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.HttpAuthenticator;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.execchain.TunnelRefusedException;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParamConfig;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

public class ProxyClient
{
  private final AuthSchemeRegistry authSchemeRegistry;
  private final HttpAuthenticator authenticator;
  private final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory;
  private final ConnectionConfig connectionConfig;
  private final HttpProcessor httpProcessor;
  private final AuthState proxyAuthState;
  private final ProxyAuthenticationStrategy proxyAuthStrategy;
  private final RequestConfig requestConfig;
  private final HttpRequestExecutor requestExec;
  private final ConnectionReuseStrategy reuseStrategy;
  
  public ProxyClient()
  {
    this(null, null, null);
  }
  
  public ProxyClient(RequestConfig paramRequestConfig)
  {
    this(null, null, paramRequestConfig);
  }
  
  public ProxyClient(HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> paramHttpConnectionFactory, ConnectionConfig paramConnectionConfig, RequestConfig paramRequestConfig)
  {
    if (paramHttpConnectionFactory != null)
    {
      this.connFactory = paramHttpConnectionFactory;
      if (paramConnectionConfig == null) {
        break label237;
      }
      label17:
      this.connectionConfig = paramConnectionConfig;
      if (paramRequestConfig == null) {
        break label244;
      }
    }
    for (;;)
    {
      this.requestConfig = paramRequestConfig;
      HttpRequestInterceptor[] arrayOfHttpRequestInterceptor = new HttpRequestInterceptor[3];
      arrayOfHttpRequestInterceptor[0] = new RequestTargetHost();
      arrayOfHttpRequestInterceptor[1] = new RequestClientConnControl();
      arrayOfHttpRequestInterceptor[2] = new RequestUserAgent();
      this.httpProcessor = new ImmutableHttpProcessor(arrayOfHttpRequestInterceptor);
      this.requestExec = new HttpRequestExecutor();
      this.proxyAuthStrategy = new ProxyAuthenticationStrategy();
      this.authenticator = new HttpAuthenticator();
      this.proxyAuthState = new AuthState();
      this.authSchemeRegistry = new AuthSchemeRegistry();
      this.authSchemeRegistry.register("Basic", new BasicSchemeFactory());
      this.authSchemeRegistry.register("Digest", new DigestSchemeFactory());
      this.authSchemeRegistry.register("NTLM", new NTLMSchemeFactory());
      this.authSchemeRegistry.register("negotiate", new SPNegoSchemeFactory());
      this.authSchemeRegistry.register("Kerberos", new KerberosSchemeFactory());
      this.reuseStrategy = new DefaultConnectionReuseStrategy();
      return;
      paramHttpConnectionFactory = ManagedHttpClientConnectionFactory.INSTANCE;
      break;
      label237:
      paramConnectionConfig = ConnectionConfig.DEFAULT;
      break label17;
      label244:
      paramRequestConfig = RequestConfig.DEFAULT;
    }
  }
  
  @Deprecated
  public ProxyClient(HttpParams paramHttpParams)
  {
    this(null, HttpParamConfig.getConnectionConfig(paramHttpParams), HttpClientParamConfig.getRequestConfig(paramHttpParams));
  }
  
  @Deprecated
  public AuthSchemeRegistry getAuthSchemeRegistry()
  {
    return this.authSchemeRegistry;
  }
  
  @Deprecated
  public HttpParams getParams()
  {
    return new BasicHttpParams();
  }
  
  public Socket tunnel(HttpHost paramHttpHost1, HttpHost paramHttpHost2, Credentials paramCredentials)
    throws IOException, HttpException
  {
    Args.notNull(paramHttpHost1, "Proxy host");
    Args.notNull(paramHttpHost2, "Target host");
    Args.notNull(paramCredentials, "Credentials");
    HttpHost localHttpHost = paramHttpHost2;
    if (localHttpHost.getPort() <= 0) {
      localHttpHost = new HttpHost(localHttpHost.getHostName(), 80, localHttpHost.getSchemeName());
    }
    HttpRoute localHttpRoute = new HttpRoute(localHttpHost, this.requestConfig.getLocalAddress(), paramHttpHost1, false, RouteInfo.TunnelType.TUNNELLED, RouteInfo.LayerType.PLAIN);
    ManagedHttpClientConnection localManagedHttpClientConnection = (ManagedHttpClientConnection)this.connFactory.create(localHttpRoute, this.connectionConfig);
    BasicHttpContext localBasicHttpContext = new BasicHttpContext();
    BasicHttpRequest localBasicHttpRequest = new BasicHttpRequest("CONNECT", localHttpHost.toHostString(), HttpVersion.HTTP_1_1);
    BasicCredentialsProvider localBasicCredentialsProvider = new BasicCredentialsProvider();
    localBasicCredentialsProvider.setCredentials(new AuthScope(paramHttpHost1), paramCredentials);
    localBasicHttpContext.setAttribute("http.target_host", paramHttpHost2);
    localBasicHttpContext.setAttribute("http.connection", localManagedHttpClientConnection);
    localBasicHttpContext.setAttribute("http.request", localBasicHttpRequest);
    localBasicHttpContext.setAttribute("http.route", localHttpRoute);
    localBasicHttpContext.setAttribute("http.auth.proxy-scope", this.proxyAuthState);
    localBasicHttpContext.setAttribute("http.auth.credentials-provider", localBasicCredentialsProvider);
    localBasicHttpContext.setAttribute("http.authscheme-registry", this.authSchemeRegistry);
    localBasicHttpContext.setAttribute("http.request-config", this.requestConfig);
    this.requestExec.preProcess(localBasicHttpRequest, this.httpProcessor, localBasicHttpContext);
    if (!localManagedHttpClientConnection.isOpen()) {
      localManagedHttpClientConnection.bind(new Socket(paramHttpHost1.getHostName(), paramHttpHost1.getPort()));
    }
    this.authenticator.generateAuthResponse(localBasicHttpRequest, this.proxyAuthState, localBasicHttpContext);
    HttpResponse localHttpResponse = this.requestExec.execute(localBasicHttpRequest, localManagedHttpClientConnection, localBasicHttpContext);
    if (localHttpResponse.getStatusLine().getStatusCode() < 200) {
      throw new HttpException("Unexpected response to CONNECT request: " + localHttpResponse.getStatusLine());
    }
    if ((this.authenticator.isAuthenticationRequested(paramHttpHost1, localHttpResponse, this.proxyAuthStrategy, this.proxyAuthState, localBasicHttpContext)) && (this.authenticator.handleAuthChallenge(paramHttpHost1, localHttpResponse, this.proxyAuthStrategy, this.proxyAuthState, localBasicHttpContext)))
    {
      if (this.reuseStrategy.keepAlive(localHttpResponse, localBasicHttpContext)) {
        EntityUtils.consume(localHttpResponse.getEntity());
      }
      for (;;)
      {
        localBasicHttpRequest.removeHeaders("Proxy-Authorization");
        break;
        localManagedHttpClientConnection.close();
      }
    }
    if (localHttpResponse.getStatusLine().getStatusCode() > 299)
    {
      HttpEntity localHttpEntity = localHttpResponse.getEntity();
      if (localHttpEntity != null) {
        localHttpResponse.setEntity(new BufferedHttpEntity(localHttpEntity));
      }
      localManagedHttpClientConnection.close();
      throw new TunnelRefusedException("CONNECT refused by proxy: " + localHttpResponse.getStatusLine(), localHttpResponse);
    }
    return localManagedHttpClientConnection.getSocket();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.ProxyClient
 * JD-Core Version:    0.7.0.1
 */