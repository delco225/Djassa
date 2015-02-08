package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public class ResponseConnControl
  implements HttpResponseInterceptor
{
  public void process(HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    Args.notNull(paramHttpResponse, "HTTP response");
    HttpCoreContext localHttpCoreContext = HttpCoreContext.adapt(paramHttpContext);
    int i = paramHttpResponse.getStatusLine().getStatusCode();
    if ((i == 400) || (i == 408) || (i == 411) || (i == 413) || (i == 414) || (i == 503) || (i == 501)) {
      paramHttpResponse.setHeader("Connection", "Close");
    }
    HttpRequest localHttpRequest;
    do
    {
      do
      {
        Header localHeader1;
        do
        {
          return;
          localHeader1 = paramHttpResponse.getFirstHeader("Connection");
        } while ((localHeader1 != null) && ("Close".equalsIgnoreCase(localHeader1.getValue())));
        HttpEntity localHttpEntity = paramHttpResponse.getEntity();
        if (localHttpEntity != null)
        {
          ProtocolVersion localProtocolVersion = paramHttpResponse.getStatusLine().getProtocolVersion();
          if ((localHttpEntity.getContentLength() < 0L) && ((!localHttpEntity.isChunked()) || (localProtocolVersion.lessEquals(HttpVersion.HTTP_1_0))))
          {
            paramHttpResponse.setHeader("Connection", "Close");
            return;
          }
        }
        localHttpRequest = localHttpCoreContext.getRequest();
      } while (localHttpRequest == null);
      Header localHeader2 = localHttpRequest.getFirstHeader("Connection");
      if (localHeader2 != null)
      {
        paramHttpResponse.setHeader("Connection", localHeader2.getValue());
        return;
      }
    } while (!localHttpRequest.getProtocolVersion().lessEquals(HttpVersion.HTTP_1_0));
    paramHttpResponse.setHeader("Connection", "Close");
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.protocol.ResponseConnControl
 * JD-Core Version:    0.7.0.1
 */