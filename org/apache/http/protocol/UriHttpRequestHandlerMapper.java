package org.apache.http.protocol;

import org.apache.http.HttpRequest;
import org.apache.http.RequestLine;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.util.Args;

@ThreadSafe
public class UriHttpRequestHandlerMapper
  implements HttpRequestHandlerMapper
{
  private final UriPatternMatcher<HttpRequestHandler> matcher;
  
  public UriHttpRequestHandlerMapper()
  {
    this(new UriPatternMatcher());
  }
  
  protected UriHttpRequestHandlerMapper(UriPatternMatcher<HttpRequestHandler> paramUriPatternMatcher)
  {
    this.matcher = ((UriPatternMatcher)Args.notNull(paramUriPatternMatcher, "Pattern matcher"));
  }
  
  protected String getRequestPath(HttpRequest paramHttpRequest)
  {
    String str = paramHttpRequest.getRequestLine().getUri();
    int i = str.indexOf("?");
    if (i != -1) {
      str = str.substring(0, i);
    }
    int j;
    do
    {
      return str;
      j = str.indexOf("#");
    } while (j == -1);
    return str.substring(0, j);
  }
  
  public HttpRequestHandler lookup(HttpRequest paramHttpRequest)
  {
    Args.notNull(paramHttpRequest, "HTTP request");
    return (HttpRequestHandler)this.matcher.lookup(getRequestPath(paramHttpRequest));
  }
  
  public void register(String paramString, HttpRequestHandler paramHttpRequestHandler)
  {
    Args.notNull(paramString, "Pattern");
    Args.notNull(paramHttpRequestHandler, "Handler");
    this.matcher.register(paramString, paramHttpRequestHandler);
  }
  
  public void unregister(String paramString)
  {
    this.matcher.unregister(paramString);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.protocol.UriHttpRequestHandlerMapper
 * JD-Core Version:    0.7.0.1
 */