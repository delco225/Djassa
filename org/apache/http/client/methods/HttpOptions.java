package org.apache.http.client.methods;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@NotThreadSafe
public class HttpOptions
  extends HttpRequestBase
{
  public static final String METHOD_NAME = "OPTIONS";
  
  public HttpOptions() {}
  
  public HttpOptions(String paramString)
  {
    setURI(URI.create(paramString));
  }
  
  public HttpOptions(URI paramURI)
  {
    setURI(paramURI);
  }
  
  public Set<String> getAllowedMethods(HttpResponse paramHttpResponse)
  {
    Args.notNull(paramHttpResponse, "HTTP response");
    HeaderIterator localHeaderIterator = paramHttpResponse.headerIterator("Allow");
    HashSet localHashSet = new HashSet();
    while (localHeaderIterator.hasNext())
    {
      HeaderElement[] arrayOfHeaderElement = localHeaderIterator.nextHeader().getElements();
      int i = arrayOfHeaderElement.length;
      for (int j = 0; j < i; j++) {
        localHashSet.add(arrayOfHeaderElement[j].getName());
      }
    }
    return localHashSet;
  }
  
  public String getMethod()
  {
    return "OPTIONS";
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.methods.HttpOptions
 * JD-Core Version:    0.7.0.1
 */