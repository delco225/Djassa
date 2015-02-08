package org.apache.http.impl.cookie;

import java.util.Collection;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Immutable
public class BrowserCompatSpecFactory
  implements CookieSpecFactory, CookieSpecProvider
{
  private final String[] datepatterns;
  private final SecurityLevel securityLevel;
  
  public BrowserCompatSpecFactory()
  {
    this(null, SecurityLevel.SECURITYLEVEL_DEFAULT);
  }
  
  public BrowserCompatSpecFactory(String[] paramArrayOfString)
  {
    this(null, SecurityLevel.SECURITYLEVEL_DEFAULT);
  }
  
  public BrowserCompatSpecFactory(String[] paramArrayOfString, SecurityLevel paramSecurityLevel)
  {
    this.datepatterns = paramArrayOfString;
    this.securityLevel = paramSecurityLevel;
  }
  
  public CookieSpec create(HttpContext paramHttpContext)
  {
    return new BrowserCompatSpec(this.datepatterns);
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
      return new BrowserCompatSpec(arrayOfString, this.securityLevel);
    }
    return new BrowserCompatSpec(null, this.securityLevel);
  }
  
  public static enum SecurityLevel
  {
    static
    {
      SecurityLevel[] arrayOfSecurityLevel = new SecurityLevel[2];
      arrayOfSecurityLevel[0] = SECURITYLEVEL_DEFAULT;
      arrayOfSecurityLevel[1] = SECURITYLEVEL_IE_MEDIUM;
      $VALUES = arrayOfSecurityLevel;
    }
    
    private SecurityLevel() {}
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.cookie.BrowserCompatSpecFactory
 * JD-Core Version:    0.7.0.1
 */