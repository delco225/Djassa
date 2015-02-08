package org.apache.http.client.protocol;

import java.io.IOException;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.protocol.HttpContext;

@Immutable
public class ResponseContentEncoding
  implements HttpResponseInterceptor
{
  public static final String UNCOMPRESSED = "http.client.response.uncompressed";
  
  public void process(HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    HttpEntity localHttpEntity = paramHttpResponse.getEntity();
    int j;
    HeaderElement localHeaderElement;
    String str;
    if ((localHttpEntity != null) && (localHttpEntity.getContentLength() != 0L))
    {
      Header localHeader = localHttpEntity.getContentEncoding();
      if (localHeader != null)
      {
        HeaderElement[] arrayOfHeaderElement = localHeader.getElements();
        int i = arrayOfHeaderElement.length;
        j = 0;
        if (i < 0)
        {
          localHeaderElement = arrayOfHeaderElement[0];
          str = localHeaderElement.getName().toLowerCase(Locale.US);
          if ((!"gzip".equals(str)) && (!"x-gzip".equals(str))) {
            break label150;
          }
          paramHttpResponse.setEntity(new GzipDecompressingEntity(paramHttpResponse.getEntity()));
          j = 1;
        }
        if (j != 0)
        {
          paramHttpResponse.removeHeaders("Content-Length");
          paramHttpResponse.removeHeaders("Content-Encoding");
          paramHttpResponse.removeHeaders("Content-MD5");
        }
      }
    }
    label150:
    do
    {
      return;
      if ("deflate".equals(str))
      {
        paramHttpResponse.setEntity(new DeflateDecompressingEntity(paramHttpResponse.getEntity()));
        j = 1;
        break;
      }
    } while ("identity".equals(str));
    throw new HttpException("Unsupported Content-Coding: " + localHeaderElement.getName());
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.protocol.ResponseContentEncoding
 * JD-Core Version:    0.7.0.1
 */