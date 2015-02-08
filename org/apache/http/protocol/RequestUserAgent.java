package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Immutable
public class RequestUserAgent
  implements HttpRequestInterceptor
{
  private final String userAgent;
  
  public RequestUserAgent()
  {
    this(null);
  }
  
  public RequestUserAgent(String paramString)
  {
    this.userAgent = paramString;
  }
  
  public void process(HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    Args.notNull(paramHttpRequest, "HTTP request");
    if (!paramHttpRequest.containsHeader("User-Agent"))
    {
      HttpParams localHttpParams = paramHttpRequest.getParams();
      String str = null;
      if (localHttpParams != null) {
        str = (String)localHttpParams.getParameter("http.useragent");
      }
      if (str == null) {
        str = this.userAgent;
      }
      if (str != null) {
        paramHttpRequest.addHeader("User-Agent", str);
      }
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.protocol.RequestUserAgent
 * JD-Core Version:    0.7.0.1
 */