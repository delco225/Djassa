package org.apache.http.client.protocol;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class ResponseProcessCookies
  implements HttpResponseInterceptor
{
  private final Log log = LogFactory.getLog(getClass());
  
  private static String formatCooke(Cookie paramCookie)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramCookie.getName());
    localStringBuilder.append("=\"");
    String str = paramCookie.getValue();
    if (str.length() > 100) {
      str = str.substring(0, 100) + "...";
    }
    localStringBuilder.append(str);
    localStringBuilder.append("\"");
    localStringBuilder.append(", version:");
    localStringBuilder.append(Integer.toString(paramCookie.getVersion()));
    localStringBuilder.append(", domain:");
    localStringBuilder.append(paramCookie.getDomain());
    localStringBuilder.append(", path:");
    localStringBuilder.append(paramCookie.getPath());
    localStringBuilder.append(", expiry:");
    localStringBuilder.append(paramCookie.getExpiryDate());
    return localStringBuilder.toString();
  }
  
  private void processCookies(HeaderIterator paramHeaderIterator, CookieSpec paramCookieSpec, CookieOrigin paramCookieOrigin, CookieStore paramCookieStore)
  {
    while (paramHeaderIterator.hasNext())
    {
      Header localHeader = paramHeaderIterator.nextHeader();
      try
      {
        Iterator localIterator = paramCookieSpec.parse(localHeader, paramCookieOrigin).iterator();
        while (localIterator.hasNext())
        {
          Cookie localCookie = (Cookie)localIterator.next();
          try
          {
            paramCookieSpec.validate(localCookie, paramCookieOrigin);
            paramCookieStore.addCookie(localCookie);
            if (!this.log.isDebugEnabled()) {
              continue;
            }
            this.log.debug("Cookie accepted [" + formatCooke(localCookie) + "]");
          }
          catch (MalformedCookieException localMalformedCookieException2) {}
          if (this.log.isWarnEnabled()) {
            this.log.warn("Cookie rejected [" + formatCooke(localCookie) + "] " + localMalformedCookieException2.getMessage());
          }
        }
        if (!this.log.isWarnEnabled()) {
          continue;
        }
      }
      catch (MalformedCookieException localMalformedCookieException1) {}
      this.log.warn("Invalid cookie header: \"" + localHeader + "\". " + localMalformedCookieException1.getMessage());
    }
  }
  
  public void process(HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    Args.notNull(paramHttpResponse, "HTTP request");
    Args.notNull(paramHttpContext, "HTTP context");
    HttpClientContext localHttpClientContext = HttpClientContext.adapt(paramHttpContext);
    CookieSpec localCookieSpec = localHttpClientContext.getCookieSpec();
    if (localCookieSpec == null) {
      this.log.debug("Cookie spec not specified in HTTP context");
    }
    CookieStore localCookieStore;
    CookieOrigin localCookieOrigin;
    do
    {
      return;
      localCookieStore = localHttpClientContext.getCookieStore();
      if (localCookieStore == null)
      {
        this.log.debug("Cookie store not specified in HTTP context");
        return;
      }
      localCookieOrigin = localHttpClientContext.getCookieOrigin();
      if (localCookieOrigin == null)
      {
        this.log.debug("Cookie origin not specified in HTTP context");
        return;
      }
      processCookies(paramHttpResponse.headerIterator("Set-Cookie"), localCookieSpec, localCookieOrigin, localCookieStore);
    } while (localCookieSpec.getVersion() <= 0);
    processCookies(paramHttpResponse.headerIterator("Set-Cookie2"), localCookieSpec, localCookieOrigin, localCookieStore);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.protocol.ResponseProcessCookies
 * JD-Core Version:    0.7.0.1
 */