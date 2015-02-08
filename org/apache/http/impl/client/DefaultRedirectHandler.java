package org.apache.http.impl.client;

import java.net.URI;
import java.net.URISyntaxException;
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
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
@Immutable
public class DefaultRedirectHandler
  implements RedirectHandler
{
  private static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";
  private final Log log = LogFactory.getLog(getClass());
  
  public URI getLocationURI(HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws ProtocolException
  {
    Args.notNull(paramHttpResponse, "HTTP response");
    Header localHeader = paramHttpResponse.getFirstHeader("location");
    if (localHeader == null) {
      throw new ProtocolException("Received redirect response " + paramHttpResponse.getStatusLine() + " but no location header");
    }
    String str = localHeader.getValue();
    if (this.log.isDebugEnabled()) {
      this.log.debug("Redirect requested to location '" + str + "'");
    }
    Object localObject1;
    HttpParams localHttpParams;
    try
    {
      localObject1 = new URI(str);
      localHttpParams = paramHttpResponse.getParams();
      if (((URI)localObject1).isAbsolute()) {
        break label287;
      }
      if (localHttpParams.isParameterTrue("http.protocol.reject-relative-redirect")) {
        throw new ProtocolException("Relative redirect location '" + localObject1 + "' not allowed");
      }
    }
    catch (URISyntaxException localURISyntaxException3)
    {
      throw new ProtocolException("Invalid redirect URI: " + str, localURISyntaxException3);
    }
    HttpHost localHttpHost = (HttpHost)paramHttpContext.getAttribute("http.target_host");
    Asserts.notNull(localHttpHost, "Target host");
    HttpRequest localHttpRequest = (HttpRequest)paramHttpContext.getAttribute("http.request");
    label287:
    RedirectLocations localRedirectLocations;
    Object localObject2;
    for (;;)
    {
      try
      {
        URI localURI2 = URIUtils.resolve(URIUtils.rewriteURI(new URI(localHttpRequest.getRequestLine().getUri()), localHttpHost, true), (URI)localObject1);
        localObject1 = localURI2;
        if (!localHttpParams.isParameterFalse("http.protocol.allow-circular-redirects")) {
          break label469;
        }
        localRedirectLocations = (RedirectLocations)paramHttpContext.getAttribute("http.protocol.redirect-locations");
        if (localRedirectLocations == null)
        {
          localRedirectLocations = new RedirectLocations();
          paramHttpContext.setAttribute("http.protocol.redirect-locations", localRedirectLocations);
        }
        if (((URI)localObject1).getFragment() != null) {}
        URI localURI1;
        localObject2 = localObject1;
      }
      catch (URISyntaxException localURISyntaxException2)
      {
        try
        {
          localURI1 = URIUtils.rewriteURI((URI)localObject1, new HttpHost(((URI)localObject1).getHost(), ((URI)localObject1).getPort(), ((URI)localObject1).getScheme()), true);
          localObject2 = localURI1;
          if (!localRedirectLocations.contains((URI)localObject2)) {
            break;
          }
          throw new CircularRedirectException("Circular redirect to '" + localObject2 + "'");
        }
        catch (URISyntaxException localURISyntaxException1)
        {
          throw new ProtocolException(localURISyntaxException1.getMessage(), localURISyntaxException1);
        }
        localURISyntaxException2 = localURISyntaxException2;
        throw new ProtocolException(localURISyntaxException2.getMessage(), localURISyntaxException2);
      }
    }
    localRedirectLocations.add((URI)localObject2);
    label469:
    return localObject1;
  }
  
  public boolean isRedirectRequested(HttpResponse paramHttpResponse, HttpContext paramHttpContext)
  {
    Args.notNull(paramHttpResponse, "HTTP response");
    switch (paramHttpResponse.getStatusLine().getStatusCode())
    {
    case 304: 
    case 305: 
    case 306: 
    default: 
    case 301: 
    case 302: 
    case 307: 
      String str;
      do
      {
        return false;
        str = ((HttpRequest)paramHttpContext.getAttribute("http.request")).getRequestLine().getMethod();
      } while ((!str.equalsIgnoreCase("GET")) && (!str.equalsIgnoreCase("HEAD")));
      return true;
    }
    return true;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.DefaultRedirectHandler
 * JD-Core Version:    0.7.0.1
 */