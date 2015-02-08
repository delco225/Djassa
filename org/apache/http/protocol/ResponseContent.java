package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public class ResponseContent
  implements HttpResponseInterceptor
{
  private final boolean overwrite;
  
  public ResponseContent()
  {
    this(false);
  }
  
  public ResponseContent(boolean paramBoolean)
  {
    this.overwrite = paramBoolean;
  }
  
  public void process(HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    Args.notNull(paramHttpResponse, "HTTP response");
    HttpEntity localHttpEntity;
    long l;
    if (this.overwrite)
    {
      paramHttpResponse.removeHeaders("Transfer-Encoding");
      paramHttpResponse.removeHeaders("Content-Length");
      ProtocolVersion localProtocolVersion = paramHttpResponse.getStatusLine().getProtocolVersion();
      localHttpEntity = paramHttpResponse.getEntity();
      if (localHttpEntity == null) {
        break label235;
      }
      l = localHttpEntity.getContentLength();
      if ((!localHttpEntity.isChunked()) || (localProtocolVersion.lessEquals(HttpVersion.HTTP_1_0))) {
        break label207;
      }
      paramHttpResponse.addHeader("Transfer-Encoding", "chunked");
      label96:
      if ((localHttpEntity.getContentType() != null) && (!paramHttpResponse.containsHeader("Content-Type"))) {
        paramHttpResponse.addHeader(localHttpEntity.getContentType());
      }
      if ((localHttpEntity.getContentEncoding() != null) && (!paramHttpResponse.containsHeader("Content-Encoding"))) {
        paramHttpResponse.addHeader(localHttpEntity.getContentEncoding());
      }
    }
    label207:
    label235:
    int i;
    do
    {
      return;
      if (paramHttpResponse.containsHeader("Transfer-Encoding")) {
        throw new ProtocolException("Transfer-encoding header already present");
      }
      if (!paramHttpResponse.containsHeader("Content-Length")) {
        break;
      }
      throw new ProtocolException("Content-Length header already present");
      if (l < 0L) {
        break label96;
      }
      paramHttpResponse.addHeader("Content-Length", Long.toString(localHttpEntity.getContentLength()));
      break label96;
      i = paramHttpResponse.getStatusLine().getStatusCode();
    } while ((i == 204) || (i == 304) || (i == 205));
    paramHttpResponse.addHeader("Content-Length", "0");
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.protocol.ResponseContent
 * JD-Core Version:    0.7.0.1
 */