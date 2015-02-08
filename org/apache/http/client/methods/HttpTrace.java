package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpTrace
  extends HttpRequestBase
{
  public static final String METHOD_NAME = "TRACE";
  
  public HttpTrace() {}
  
  public HttpTrace(String paramString)
  {
    setURI(URI.create(paramString));
  }
  
  public HttpTrace(URI paramURI)
  {
    setURI(paramURI);
  }
  
  public String getMethod()
  {
    return "TRACE";
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.methods.HttpTrace
 * JD-Core Version:    0.7.0.1
 */