package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.RequestLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class RequestClientConnControl
  implements HttpRequestInterceptor
{
  private static final String PROXY_CONN_DIRECTIVE = "Proxy-Connection";
  private final Log log = LogFactory.getLog(getClass());
  
  public void process(HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    Args.notNull(paramHttpRequest, "HTTP request");
    if (paramHttpRequest.getRequestLine().getMethod().equalsIgnoreCase("CONNECT")) {
      paramHttpRequest.setHeader("Proxy-Connection", "Keep-Alive");
    }
    RouteInfo localRouteInfo;
    do
    {
      return;
      localRouteInfo = HttpClientContext.adapt(paramHttpContext).getHttpRoute();
      if (localRouteInfo == null)
      {
        this.log.debug("Connection route not set in the context");
        return;
      }
      if (((localRouteInfo.getHopCount() == 1) || (localRouteInfo.isTunnelled())) && (!paramHttpRequest.containsHeader("Connection"))) {
        paramHttpRequest.addHeader("Connection", "Keep-Alive");
      }
    } while ((localRouteInfo.getHopCount() != 2) || (localRouteInfo.isTunnelled()) || (paramHttpRequest.containsHeader("Proxy-Connection")));
    paramHttpRequest.addHeader("Proxy-Connection", "Keep-Alive");
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.protocol.RequestClientConnControl
 * JD-Core Version:    0.7.0.1
 */