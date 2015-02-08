package org.apache.http.impl.cookie;

import java.util.Locale;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

@Immutable
public class RFC2109DomainHandler
  implements CookieAttributeHandler
{
  public boolean match(Cookie paramCookie, CookieOrigin paramCookieOrigin)
  {
    Args.notNull(paramCookie, "Cookie");
    Args.notNull(paramCookieOrigin, "Cookie origin");
    String str1 = paramCookieOrigin.getHost();
    String str2 = paramCookie.getDomain();
    if (str2 == null) {}
    while ((!str1.equals(str2)) && ((!str2.startsWith(".")) || (!str1.endsWith(str2)))) {
      return false;
    }
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
    if (!str2.equals(str1))
    {
      if (str2.indexOf('.') == -1) {
        throw new CookieRestrictionViolationException("Domain attribute \"" + str2 + "\" does not match the host \"" + str1 + "\"");
      }
      if (!str2.startsWith(".")) {
        throw new CookieRestrictionViolationException("Domain attribute \"" + str2 + "\" violates RFC 2109: domain must start with a dot");
      }
      int i = str2.indexOf('.', 1);
      if ((i < 0) || (i == -1 + str2.length())) {
        throw new CookieRestrictionViolationException("Domain attribute \"" + str2 + "\" violates RFC 2109: domain must contain an embedded dot");
      }
      String str3 = str1.toLowerCase(Locale.ENGLISH);
      if (!str3.endsWith(str2)) {
        throw new CookieRestrictionViolationException("Illegal domain attribute \"" + str2 + "\". Domain of origin: \"" + str3 + "\"");
      }
      if (str3.substring(0, str3.length() - str2.length()).indexOf('.') != -1) {
        throw new CookieRestrictionViolationException("Domain attribute \"" + str2 + "\" violates RFC 2109: host minus domain may not contain any dots");
      }
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.cookie.RFC2109DomainHandler
 * JD-Core Version:    0.7.0.1
 */