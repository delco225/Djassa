package org.apache.http.impl.execchain;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.client.RedirectException;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

@ThreadSafe
public class RedirectExec
  implements ClientExecChain
{
  private final Log log = LogFactory.getLog(getClass());
  private final RedirectStrategy redirectStrategy;
  private final ClientExecChain requestExecutor;
  private final HttpRoutePlanner routePlanner;
  
  public RedirectExec(ClientExecChain paramClientExecChain, HttpRoutePlanner paramHttpRoutePlanner, RedirectStrategy paramRedirectStrategy)
  {
    Args.notNull(paramClientExecChain, "HTTP client request executor");
    Args.notNull(paramHttpRoutePlanner, "HTTP route planner");
    Args.notNull(paramRedirectStrategy, "HTTP redirect strategy");
    this.requestExecutor = paramClientExecChain;
    this.routePlanner = paramHttpRoutePlanner;
    this.redirectStrategy = paramRedirectStrategy;
  }
  
  public CloseableHttpResponse execute(HttpRoute paramHttpRoute, HttpRequestWrapper paramHttpRequestWrapper, HttpClientContext paramHttpClientContext, HttpExecutionAware paramHttpExecutionAware)
    throws IOException, HttpException
  {
    Args.notNull(paramHttpRoute, "HTTP route");
    Args.notNull(paramHttpRequestWrapper, "HTTP request");
    Args.notNull(paramHttpClientContext, "HTTP context");
    List localList = paramHttpClientContext.getRedirectLocations();
    if (localList != null) {
      localList.clear();
    }
    RequestConfig localRequestConfig = paramHttpClientContext.getRequestConfig();
    int i;
    if (localRequestConfig.getMaxRedirects() > 0) {
      i = localRequestConfig.getMaxRedirects();
    }
    CloseableHttpResponse localCloseableHttpResponse;
    for (;;)
    {
      HttpRoute localHttpRoute = paramHttpRoute;
      HttpRequestWrapper localHttpRequestWrapper = paramHttpRequestWrapper;
      int j = 0;
      localCloseableHttpResponse = this.requestExecutor.execute(localHttpRoute, localHttpRequestWrapper, paramHttpClientContext, paramHttpExecutionAware);
      try
      {
        if ((localRequestConfig.isRedirectsEnabled()) && (this.redirectStrategy.isRedirected(localHttpRequestWrapper, localCloseableHttpResponse, paramHttpClientContext))) {
          if (j >= i) {
            throw new RedirectException("Maximum redirects (" + i + ") exceeded");
          }
        }
      }
      catch (RuntimeException localRuntimeException)
      {
        localCloseableHttpResponse.close();
        throw localRuntimeException;
        i = 50;
        continue;
        j++;
        HttpUriRequest localHttpUriRequest = this.redirectStrategy.getRedirect(localHttpRequestWrapper, localCloseableHttpResponse, paramHttpClientContext);
        if (!localHttpUriRequest.headerIterator().hasNext()) {
          localHttpUriRequest.setHeaders(paramHttpRequestWrapper.getOriginal().getAllHeaders());
        }
        localHttpRequestWrapper = HttpRequestWrapper.wrap(localHttpUriRequest);
        if ((localHttpRequestWrapper instanceof HttpEntityEnclosingRequest)) {
          Proxies.enhanceEntity((HttpEntityEnclosingRequest)localHttpRequestWrapper);
        }
        localURI = localHttpRequestWrapper.getURI();
        localHttpHost = URIUtils.extractHost(localURI);
        if (localHttpHost == null) {
          throw new ProtocolException("Redirect URI does not specify a valid host name: " + localURI);
        }
      }
      catch (IOException localIOException2)
      {
        URI localURI;
        HttpHost localHttpHost;
        localCloseableHttpResponse.close();
        throw localIOException2;
        if (!localHttpRoute.getTargetHost().equals(localHttpHost))
        {
          AuthState localAuthState1 = paramHttpClientContext.getTargetAuthState();
          if (localAuthState1 != null)
          {
            this.log.debug("Resetting target auth state");
            localAuthState1.reset();
          }
          AuthState localAuthState2 = paramHttpClientContext.getProxyAuthState();
          if (localAuthState2 != null)
          {
            AuthScheme localAuthScheme = localAuthState2.getAuthScheme();
            if ((localAuthScheme != null) && (localAuthScheme.isConnectionBased()))
            {
              this.log.debug("Resetting proxy auth state");
              localAuthState2.reset();
            }
          }
        }
        localHttpRoute = this.routePlanner.determineRoute(localHttpHost, localHttpRequestWrapper, paramHttpClientContext);
        if (this.log.isDebugEnabled()) {
          this.log.debug("Redirecting to '" + localURI + "' via " + localHttpRoute);
        }
        EntityUtils.consume(localCloseableHttpResponse.getEntity());
        localCloseableHttpResponse.close();
      }
      catch (HttpException localHttpException)
      {
        try
        {
          EntityUtils.consume(localCloseableHttpResponse.getEntity());
        }
        catch (IOException localIOException1)
        {
          for (;;)
          {
            this.log.debug("I/O error while releasing connection", localIOException1);
            localCloseableHttpResponse.close();
          }
        }
        finally
        {
          localCloseableHttpResponse.close();
        }
        throw localHttpException;
      }
    }
    return localCloseableHttpResponse;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.execchain.RedirectExec
 * JD-Core Version:    0.7.0.1
 */