package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpHead
  extends HttpRequestBase
{
  public static final String METHOD_NAME = "HEAD";
  
  public HttpHead() {}
  
  public HttpHead(String paramString)
  {
    setURI(URI.create(paramString));
  }
  
  public HttpHead(URI paramURI)
  {
    setURI(paramURI);
  }
  
  public String getMethod()
  {
    return "HEAD";
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.methods.HttpHead
 * JD-Core Version:    0.7.0.1
 */