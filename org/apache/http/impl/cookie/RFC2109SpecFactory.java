package org.apache.http.impl.cookie;

import java.util.Collection;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Immutable
public class RFC2109SpecFactory
  implements CookieSpecFactory, CookieSpecProvider
{
  private final String[] datepatterns;
  private final boolean oneHeader;
  
  public RFC2109SpecFactory()
  {
    this(null, false);
  }
  
  public RFC2109SpecFactory(String[] paramArrayOfString, boolean paramBoolean)
  {
    this.datepatterns = paramArrayOfString;
    this.oneHeader = paramBoolean;
  }
  
  public CookieSpec create(HttpContext paramHttpContext)
  {
    return new RFC2109Spec(this.datepatterns, this.oneHeader);
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
      return new RFC2109Spec(arrayOfString, paramHttpParams.getBooleanParameter("http.protocol.single-cookie-header", false));
    }
    return new RFC2109Spec();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.cookie.RFC2109SpecFactory
 * JD-Core Version:    0.7.0.1
 */