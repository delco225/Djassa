package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public class ResponseServer
  implements HttpResponseInterceptor
{
  private final String originServer;
  
  public ResponseServer()
  {
    this(null);
  }
  
  public ResponseServer(String paramString)
  {
    this.originServer = paramString;
  }
  
  public void process(HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    Args.notNull(paramHttpResponse, "HTTP response");
    if ((!paramHttpResponse.containsHeader("Server")) && (this.originServer != null)) {
      paramHttpResponse.addHeader("Server", this.originServer);
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.protocol.ResponseServer
 * JD-Core Version:    0.7.0.1
 */