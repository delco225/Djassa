package org.apache.http.impl.cookie;

import java.util.Collection;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Immutable
public class NetscapeDraftSpecFactory
  implements CookieSpecFactory, CookieSpecProvider
{
  private final String[] datepatterns;
  
  public NetscapeDraftSpecFactory()
  {
    this(null);
  }
  
  public NetscapeDraftSpecFactory(String[] paramArrayOfString)
  {
    this.datepatterns = paramArrayOfString;
  }
  
  public CookieSpec create(HttpContext paramHttpContext)
  {
    return new NetscapeDraftSpec(this.datepatterns);
  }
  
  public CookieSpec newInstance(HttpParams paramHttpParams)
  {
    if (paramHttpParams != null)
    {
      Collection localCollection = (Collection)paramHttpParams.getParameter("http.protocol.cookie-datepatterns");
      String[] arrayOfString = null;
      if (localCollection != null) {
        arrayOfString = (String[])localCollection.toArray(new String[localCollection.size()]);
      }
      return new NetscapeDraftSpec(arrayOfString);
    }
    return new NetscapeDraftSpec();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.cookie.NetscapeDraftSpecFactory
 * JD-Core Version:    0.7.0.1
 */