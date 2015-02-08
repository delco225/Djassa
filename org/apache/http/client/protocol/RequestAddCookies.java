package org.apache.http.client.protocol;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.RequestLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Lookup;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.SetCookie2;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

@Immutable
public class RequestAddCookies
  implements HttpRequestInterceptor
{
  private final Log log = LogFactory.getLog(getClass());
  
  public void process(HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    Args.notNull(paramHttpRequest, "HTTP request");
    Args.notNull(paramHttpContext, "HTTP context");
    if (paramHttpRequest.getRequestLine().getMethod().equalsIgnoreCase("CONNECT")) {
      return;
    }
    HttpClientContext localHttpClientContext = HttpClientContext.adapt(paramHttpContext);
    CookieStore localCookieStore = localHttpClientContext.getCookieStore();
    if (localCookieStore == null)
    {
      this.log.debug("Cookie store not specified in HTTP context");
      return;
    }
    Lookup localLookup = localHttpClientContext.getCookieSpecRegistry();
    if (localLookup == null)
    {
      this.log.debug("CookieSpec registry not specified in HTTP context");
      return;
    }
    HttpHost localHttpHost = localHttpClientContext.getTargetHost();
    if (localHttpHost == null)
    {
      this.log.debug("Target host not set in the context");
      return;
    }
    RouteInfo localRouteInfo = localHttpClientContext.getHttpRoute();
    if (localRouteInfo == null)
    {
      this.log.debug("Connection route not set in the context");
      return;
    }
    String str1 = localHttpClientContext.getRequestConfig().getCookieSpec();
    if (str1 == null) {
      str1 = "best-match";
    }
    if (this.log.isDebugEnabled()) {
      this.log.debug("CookieSpec selected: " + str1);
    }
    Object localObject;
    if ((paramHttpRequest instanceof HttpUriRequest)) {
      localObject = ((HttpUriRequest)paramHttpRequest).getURI();
    }
    for (;;)
    {
      String str2;
      label226:
      String str3;
      int i;
      if (localObject != null)
      {
        str2 = ((URI)localObject).getPath();
        str3 = localHttpHost.getHostName();
        i = localHttpHost.getPort();
        if (i < 0) {
          i = localRouteInfo.getTargetHost().getPort();
        }
        if (i < 0) {
          break label376;
        }
        label262:
        if (TextUtils.isEmpty(str2)) {
          break label382;
        }
      }
      for (;;)
      {
        for (;;)
        {
          boolean bool = localRouteInfo.isSecure();
          CookieOrigin localCookieOrigin = new CookieOrigin(str3, i, str2, bool);
          CookieSpecProvider localCookieSpecProvider = (CookieSpecProvider)localLookup.lookup(str1);
          if (localCookieSpecProvider != null) {
            break label389;
          }
          throw new HttpException("Unsupported cookie policy: " + str1);
          try
          {
            URI localURI = new URI(paramHttpRequest.getRequestLine().getUri());
            localObject = localURI;
          }
          catch (URISyntaxException localURISyntaxException)
          {
            label376:
            label382:
            CookieSpec localCookieSpec;
            ArrayList localArrayList1;
            ArrayList localArrayList2;
            Date localDate;
            Iterator localIterator1;
            Iterator localIterator3;
            int j;
            int k;
            Iterator localIterator2;
            Header localHeader;
            localObject = null;
          }
        }
        str2 = null;
        break label226;
        i = 0;
        break label262;
        str2 = "/";
      }
      label389:
      localCookieSpec = localCookieSpecProvider.create(localHttpClientContext);
      localArrayList1 = new ArrayList(localCookieStore.getCookies());
      localArrayList2 = new ArrayList();
      localDate = new Date();
      localIterator1 = localArrayList1.iterator();
      while (localIterator1.hasNext())
      {
        Cookie localCookie2 = (Cookie)localIterator1.next();
        if (!localCookie2.isExpired(localDate))
        {
          if (localCookieSpec.match(localCookie2, localCookieOrigin))
          {
            if (this.log.isDebugEnabled()) {
              this.log.debug("Cookie " + localCookie2 + " match " + localCookieOrigin);
            }
            localArrayList2.add(localCookie2);
          }
        }
        else if (this.log.isDebugEnabled()) {
          this.log.debug("Cookie " + localCookie2 + " expired");
        }
      }
      if (!localArrayList2.isEmpty())
      {
        localIterator3 = localCookieSpec.formatCookies(localArrayList2).iterator();
        while (localIterator3.hasNext()) {
          paramHttpRequest.addHeader((Header)localIterator3.next());
        }
      }
      j = localCookieSpec.getVersion();
      if (j > 0)
      {
        k = 0;
        localIterator2 = localArrayList2.iterator();
        while (localIterator2.hasNext())
        {
          Cookie localCookie1 = (Cookie)localIterator2.next();
          if ((j != localCookie1.getVersion()) || (!(localCookie1 instanceof SetCookie2))) {
            k = 1;
          }
        }
        if (k != 0)
        {
          localHeader = localCookieSpec.getVersionHeader();
          if (localHeader != null) {
            paramHttpRequest.addHeader(localHeader);
          }
        }
      }
      paramHttpContext.setAttribute("http.cookie-spec", localCookieSpec);
      paramHttpContext.setAttribute("http.cookie-origin", localCookieOrigin);
      return;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.protocol.RequestAddCookies
 * JD-Core Version:    0.7.0.1
 */