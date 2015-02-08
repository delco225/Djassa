package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Immutable
public class IgnoreSpecFactory
  implements CookieSpecFactory, CookieSpecProvider
{
  public CookieSpec create(HttpContext paramHttpContext)
  {
    return new IgnoreSpec();
  }
  
  public CookieSpec newInstance(HttpParams paramHttpParams)
  {
    return new IgnoreSpec();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.cookie.IgnoreSpecFactory
 * JD-Core Version:    0.7.0.1
 */