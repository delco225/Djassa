package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

@Immutable
public class BasicDomainHandler
  implements CookieAttributeHandler
{
  public boolean match(Cookie paramCookie, CookieOrigin paramCookieOrigin)
  {
    Args.notNull(paramCookie, "Cookie");
    Args.notNull(paramCookieOrigin, "Cookie origin");
    String str1 = paramCookieOrigin.getHost();
    String str2 = paramCookie.getDomain();
    if (str2 == null) {}
    do
    {
      return false;
      if (str1.equals(str2)) {
        return true;
      }
      if (!str2.startsWith(".")) {
        str2 = '.' + str2;
      }
    } while ((!str1.endsWith(str2)) && (!str1.equals(str2.substring(1))));
    return true;
  }
  
  public void parse(SetCookie paramSetCookie, String paramString)
    throws MalformedCookieException
  {
    Args.notNull(paramSetCookie, "Cookie");
    if (paramString == null) {
      throw new MalformedCookieException("Missing value for domain attribute");
    }
    if (paramString.trim().length() == 0) {
      throw new MalformedCookieException("Blank value for domain attribute");
    }
    paramSetCookie.setDomain(paramString);
  }
  
  public void validate(Cookie paramCookie, CookieOrigin paramCookieOrigin)
    throws MalformedCookieException
  {
    Args.notNull(paramCookie, "Cookie");
    Args.notNull(paramCookieOrigin, "Cookie origin");
    String str1 = paramCookieOrigin.getHost();
    String str2 = paramCookie.getDomain();
    if (str2 == null) {
      throw new CookieRestrictionViolationException("Cookie domain may not be null");
    }
    if (str1.contains("."))
    {
      if (!str1.endsWith(str2))
      {
        if (str2.startsWith(".")) {
          str2 = str2.substring(1, str2.length());
        }
        if (!str1.equals(str2)) {
          throw new CookieRestrictionViolationException("Illegal domain attribute \"" + str2 + "\". Domain of origin: \"" + str1 + "\"");
        }
      }
    }
    else if (!str1.equals(str2)) {
      throw new CookieRestrictionViolationException("Illegal domain attribute \"" + str2 + "\". Domain of origin: \"" + str1 + "\"");
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.cookie.BasicDomainHandler
 * JD-Core Version:    0.7.0.1
 */