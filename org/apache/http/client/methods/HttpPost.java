package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpPost
  extends HttpEntityEnclosingRequestBase
{
  public static final String METHOD_NAME = "POST";
  
  public HttpPost() {}
  
  public HttpPost(String paramString)
  {
    setURI(URI.create(paramString));
  }
  
  public HttpPost(URI paramURI)
  {
    setURI(paramURI);
  }
  
  public String getMethod()
  {
    return "POST";
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.methods.HttpPost
 * JD-Core Version:    0.7.0.1
 */