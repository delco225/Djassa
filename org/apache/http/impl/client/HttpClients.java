package org.apache.http.impl.client;

import org.apache.http.annotation.Immutable;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

@Immutable
public class HttpClients
{
  public static CloseableHttpClient createDefault()
  {
    return HttpClientBuilder.create().build();
  }
  
  public static CloseableHttpClient createMinimal()
  {
    return new MinimalHttpClient(new PoolingHttpClientConnectionManager());
  }
  
  public static CloseableHttpClient createMinimal(HttpClientConnectionManager paramHttpClientConnectionManager)
  {
    return new MinimalHttpClient(paramHttpClientConnectionManager);
  }
  
  public static CloseableHttpClient createSystem()
  {
    return HttpClientBuilder.create().useSystemProperties().build();
  }
  
  public static HttpClientBuilder custom()
  {
    return HttpClientBuilder.create();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.HttpClients
 * JD-Core Version:    0.7.0.1
 */