package org.apache.http.client.utils;

import java.io.Closeable;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils
{
  public static void closeQuietly(HttpResponse paramHttpResponse)
  {
    HttpEntity localHttpEntity;
    if (paramHttpResponse != null)
    {
      localHttpEntity = paramHttpResponse.getEntity();
      if (localHttpEntity == null) {}
    }
    try
    {
      EntityUtils.consume(localHttpEntity);
      return;
    }
    catch (IOException localIOException) {}
  }
  
  public static void closeQuietly(HttpClient paramHttpClient)
  {
    if ((paramHttpClient != null) && ((paramHttpClient instanceof Closeable))) {}
    try
    {
      ((Closeable)paramHttpClient).close();
      return;
    }
    catch (IOException localIOException) {}
  }
  
  public static void closeQuietly(CloseableHttpResponse paramCloseableHttpResponse)
  {
    if (paramCloseableHttpResponse != null) {
      try
      {
        EntityUtils.consume(paramCloseableHttpResponse.getEntity());
        return;
      }
      finally
      {
        try
        {
          paramCloseableHttpResponse.close();
          return;
        }
        catch (IOException localIOException) {}
        localObject = finally;
        paramCloseableHttpResponse.close();
      }
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.utils.HttpClientUtils
 * JD-Core Version:    0.7.0.1
 */