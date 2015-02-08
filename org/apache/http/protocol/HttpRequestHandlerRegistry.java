package org.apache.http.protocol;

import java.util.Map;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.util.Args;

@Deprecated
@ThreadSafe
public class HttpRequestHandlerRegistry
  implements HttpRequestHandlerResolver
{
  private final UriPatternMatcher<HttpRequestHandler> matcher = new UriPatternMatcher();
  
  public Map<String, HttpRequestHandler> getHandlers()
  {
    return this.matcher.getObjects();
  }
  
  public HttpRequestHandler lookup(String paramString)
  {
    return (HttpRequestHandler)this.matcher.lookup(paramString);
  }
  
  public void register(String paramString, HttpRequestHandler paramHttpRequestHandler)
  {
    Args.notNull(paramString, "URI request pattern");
    Args.notNull(paramHttpRequestHandler, "Request handler");
    this.matcher.register(paramString, paramHttpRequestHandler);
  }
  
  public void setHandlers(Map<String, HttpRequestHandler> paramMap)
  {
    this.matcher.setObjects(paramMap);
  }
  
  public void unregister(String paramString)
  {
    this.matcher.unregister(paramString);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.protocol.HttpRequestHandlerRegistry
 * JD-Core Version:    0.7.0.1
 */