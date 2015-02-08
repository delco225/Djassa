package org.apache.http.impl.execchain;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolException;
import org.apache.http.RequestLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.util.Args;

@Immutable
public class ProtocolExec
  implements ClientExecChain
{
  private final HttpProcessor httpProcessor;
  private final Log log = LogFactory.getLog(getClass());
  private final ClientExecChain requestExecutor;
  
  public ProtocolExec(ClientExecChain paramClientExecChain, HttpProcessor paramHttpProcessor)
  {
    Args.notNull(paramClientExecChain, "HTTP client request executor");
    Args.notNull(paramHttpProcessor, "HTTP protocol processor");
    this.requestExecutor = paramClientExecChain;
    this.httpProcessor = paramHttpProcessor;
  }
  
  public CloseableHttpResponse execute(HttpRoute paramHttpRoute, HttpRequestWrapper paramHttpRequestWrapper, HttpClientContext paramHttpClientContext, HttpExecutionAware paramHttpExecutionAware)
    throws IOException, HttpException
  {
    Args.notNull(paramHttpRoute, "HTTP route");
    Args.notNull(paramHttpRequestWrapper, "HTTP request");
    Args.notNull(paramHttpClientContext, "HTTP context");
    HttpRequest localHttpRequest = paramHttpRequestWrapper.getOriginal();
    Object localObject1;
    Object localObject3;
    if ((localHttpRequest instanceof HttpUriRequest))
    {
      localObject1 = ((HttpUriRequest)localHttpRequest).getURI();
      paramHttpRequestWrapper.setURI((URI)localObject1);
      rewriteRequestURI(paramHttpRequestWrapper, paramHttpRoute);
      Object localObject2 = (HttpHost)paramHttpRequestWrapper.getParams().getParameter("http.virtual-host");
      if ((localObject2 != null) && (((HttpHost)localObject2).getPort() == -1))
      {
        int i = paramHttpRoute.getTargetHost().getPort();
        if (i != -1)
        {
          HttpHost localHttpHost = new HttpHost(((HttpHost)localObject2).getHostName(), i, ((HttpHost)localObject2).getSchemeName());
          localObject2 = localHttpHost;
        }
        if (this.log.isDebugEnabled()) {
          this.log.debug("Using virtual host" + localObject2);
        }
      }
      if (localObject2 == null) {
        break label434;
      }
      localObject3 = localObject2;
    }
    for (;;)
    {
      for (;;)
      {
        if (localObject3 == null) {
          localObject3 = paramHttpRoute.getTargetHost();
        }
        if (localObject1 != null)
        {
          String str3 = ((URI)localObject1).getUserInfo();
          if (str3 != null)
          {
            Object localObject4 = paramHttpClientContext.getCredentialsProvider();
            if (localObject4 == null)
            {
              localObject4 = new BasicCredentialsProvider();
              paramHttpClientContext.setCredentialsProvider((CredentialsProvider)localObject4);
            }
            AuthScope localAuthScope = new AuthScope((HttpHost)localObject3);
            UsernamePasswordCredentials localUsernamePasswordCredentials = new UsernamePasswordCredentials(str3);
            ((CredentialsProvider)localObject4).setCredentials(localAuthScope, localUsernamePasswordCredentials);
          }
        }
        paramHttpClientContext.setAttribute("http.target_host", localObject3);
        paramHttpClientContext.setAttribute("http.route", paramHttpRoute);
        paramHttpClientContext.setAttribute("http.request", paramHttpRequestWrapper);
        this.httpProcessor.process(paramHttpRequestWrapper, paramHttpClientContext);
        localCloseableHttpResponse = this.requestExecutor.execute(paramHttpRoute, paramHttpRequestWrapper, paramHttpClientContext, paramHttpExecutionAware);
        try
        {
          paramHttpClientContext.setAttribute("http.response", localCloseableHttpResponse);
          this.httpProcessor.process(localCloseableHttpResponse, paramHttpClientContext);
          return localCloseableHttpResponse;
        }
        catch (RuntimeException localRuntimeException)
        {
          String str1;
          boolean bool1;
          boolean bool2;
          String str2;
          localCloseableHttpResponse.close();
          throw localRuntimeException;
        }
        catch (IOException localIOException)
        {
          localCloseableHttpResponse.close();
          throw localIOException;
        }
        catch (HttpException localHttpException)
        {
          localCloseableHttpResponse.close();
          throw localHttpException;
        }
        str1 = localHttpRequest.getRequestLine().getUri();
        try
        {
          URI localURI = URI.create(str1);
          localObject1 = localURI;
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          bool1 = this.log.isDebugEnabled();
          localObject1 = null;
        }
      }
      if (!bool1) {
        break;
      }
      this.log.debug("Unable to parse '" + str1 + "' as a valid URI; " + "request URI and Host header may be inconsistent", localIllegalArgumentException);
      localObject1 = null;
      break;
      label434:
      localObject3 = null;
      if (localObject1 != null)
      {
        bool2 = ((URI)localObject1).isAbsolute();
        localObject3 = null;
        if (bool2)
        {
          str2 = ((URI)localObject1).getHost();
          localObject3 = null;
          if (str2 != null) {
            localObject3 = new HttpHost(((URI)localObject1).getHost(), ((URI)localObject1).getPort(), ((URI)localObject1).getScheme());
          }
        }
      }
    }
  }
  
  void rewriteRequestURI(HttpRequestWrapper paramHttpRequestWrapper, HttpRoute paramHttpRoute)
    throws ProtocolException
  {
    try
    {
      URI localURI1 = paramHttpRequestWrapper.getURI();
      if (localURI1 != null)
      {
        Object localObject;
        if ((paramHttpRoute.getProxyHost() != null) && (!paramHttpRoute.isTunnelled())) {
          if (!localURI1.isAbsolute()) {
            localObject = URIUtils.rewriteURI(localURI1, paramHttpRoute.getTargetHost(), true);
          }
        }
        for (;;)
        {
          paramHttpRequestWrapper.setURI((URI)localObject);
          return;
          localObject = URIUtils.rewriteURI(localURI1);
          continue;
          if (localURI1.isAbsolute())
          {
            localObject = URIUtils.rewriteURI(localURI1, null, true);
          }
          else
          {
            URI localURI2 = URIUtils.rewriteURI(localURI1);
            localObject = localURI2;
          }
        }
      }
      return;
    }
    catch (URISyntaxException localURISyntaxException)
    {
      throw new ProtocolException("Invalid URI: " + paramHttpRequestWrapper.getRequestLine().getUri(), localURISyntaxException);
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.execchain.ProtocolExec
 * JD-Core Version:    0.7.0.1
 */