package org.apache.http.impl.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.RequestLine;
import org.apache.http.StatusLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.TextUtils;

@Immutable
public class DefaultRedirectStrategy
  implements RedirectStrategy
{
  public static final DefaultRedirectStrategy INSTANCE = new DefaultRedirectStrategy();
  @Deprecated
  public static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";
  private static final String[] REDIRECT_METHODS = { "GET", "HEAD" };
  private final Log log = LogFactory.getLog(getClass());
  
  protected URI createLocationURI(String paramString)
    throws ProtocolException
  {
    try
    {
      URIBuilder localURIBuilder = new URIBuilder(new URI(paramString).normalize());
      String str = localURIBuilder.getHost();
      if (str != null) {
        localURIBuilder.setHost(str.toLowerCase(Locale.US));
      }
      if (TextUtils.isEmpty(localURIBuilder.getPath())) {
        localURIBuilder.setPath("/");
      }
      URI localURI = localURIBuilder.build();
      return localURI;
    }
    catch (URISyntaxException localURISyntaxException)
    {
      throw new ProtocolException("Invalid redirect URI: " + paramString, localURISyntaxException);
    }
  }
  
  public URI getLocationURI(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws ProtocolException
  {
    Args.notNull(paramHttpRequest, "HTTP request");
    Args.notNull(paramHttpResponse, "HTTP response");
    Args.notNull(paramHttpContext, "HTTP context");
    HttpClientContext localHttpClientContext = HttpClientContext.adapt(paramHttpContext);
    Header localHeader = paramHttpResponse.getFirstHeader("location");
    if (localHeader == null) {
      throw new ProtocolException("Received redirect response " + paramHttpResponse.getStatusLine() + " but no location header");
    }
    String str = localHeader.getValue();
    if (this.log.isDebugEnabled()) {
      this.log.debug("Redirect requested to location '" + str + "'");
    }
    RequestConfig localRequestConfig = localHttpClientContext.getRequestConfig();
    Object localObject = createLocationURI(str);
    try
    {
      if (((URI)localObject).isAbsolute()) {
        break label264;
      }
      if (!localRequestConfig.isRelativeRedirectsAllowed()) {
        throw new ProtocolException("Relative redirect location '" + localObject + "' not allowed");
      }
    }
    catch (URISyntaxException localURISyntaxException)
    {
      throw new ProtocolException(localURISyntaxException.getMessage(), localURISyntaxException);
    }
    HttpHost localHttpHost = localHttpClientContext.getTargetHost();
    Asserts.notNull(localHttpHost, "Target host");
    URI localURI = URIUtils.resolve(URIUtils.rewriteURI(new URI(paramHttpRequest.getRequestLine().getUri()), localHttpHost, false), (URI)localObject);
    localObject = localURI;
    label264:
    RedirectLocations localRedirectLocations = (RedirectLocations)localHttpClientContext.getAttribute("http.protocol.redirect-locations");
    if (localRedirectLocations == null)
    {
      localRedirectLocations = new RedirectLocations();
      paramHttpContext.setAttribute("http.protocol.redirect-locations", localRedirectLocations);
    }
    if ((!localRequestConfig.isCircularRedirectsAllowed()) && (localRedirectLocations.contains((URI)localObject))) {
      throw new CircularRedirectException("Circular redirect to '" + localObject + "'");
    }
    localRedirectLocations.add((URI)localObject);
    return localObject;
  }
  
  public HttpUriRequest getRedirect(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws ProtocolException
  {
    URI localURI = getLocationURI(paramHttpRequest, paramHttpResponse, paramHttpContext);
    String str = paramHttpRequest.getRequestLine().getMethod();
    if (str.equalsIgnoreCase("HEAD")) {
      return new HttpHead(localURI);
    }
    if (str.equalsIgnoreCase("GET")) {
      return new HttpGet(localURI);
    }
    if (paramHttpResponse.getStatusLine().getStatusCode() == 307) {
      return RequestBuilder.copy(paramHttpRequest).setUri(localURI).build();
    }
    return new HttpGet(localURI);
  }
  
  protected boolean isRedirectable(String paramString)
  {
    String[] arrayOfString = REDIRECT_METHODS;
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++) {
      if (arrayOfString[j].equalsIgnoreCase(paramString)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean isRedirected(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws ProtocolException
  {
    boolean bool = true;
    Args.notNull(paramHttpRequest, "HTTP request");
    Args.notNull(paramHttpResponse, "HTTP response");
    int i = paramHttpResponse.getStatusLine().getStatusCode();
    String str = paramHttpRequest.getRequestLine().getMethod();
    Header localHeader = paramHttpResponse.getFirstHeader("location");
    switch (i)
    {
    case 304: 
    case 305: 
    case 306: 
    default: 
      bool = false;
    case 303: 
    case 302: 
      do
      {
        return bool;
      } while ((isRedirectable(str)) && (localHeader != null));
      return false;
    }
    return isRedirectable(str);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.DefaultRedirectStrategy
 * JD-Core Version:    0.7.0.1
 */