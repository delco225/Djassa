package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpGet
  extends HttpRequestBase
{
  public static final String METHOD_NAME = "GET";
  
  public HttpGet() {}
  
  public HttpGet(String paramString)
  {
    setURI(URI.create(paramString));
  }
  
  public HttpGet(URI paramURI)
  {
    setURI(paramURI);
  }
  
  public String getMethod()
  {
    return "GET";
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.methods.HttpGet
 * JD-Core Version:    0.7.0.1
 */