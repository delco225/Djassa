package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpPut
  extends HttpEntityEnclosingRequestBase
{
  public static final String METHOD_NAME = "PUT";
  
  public HttpPut() {}
  
  public HttpPut(String paramString)
  {
    setURI(URI.create(paramString));
  }
  
  public HttpPut(URI paramURI)
  {
    setURI(paramURI);
  }
  
  public String getMethod()
  {
    return "PUT";
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.methods.HttpPut
 * JD-Core Version:    0.7.0.1
 */