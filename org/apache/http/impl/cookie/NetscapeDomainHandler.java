package org.apache.http.impl.cookie;

import java.util.Locale;
import java.util.StringTokenizer;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.util.Args;

@Immutable
public class NetscapeDomainHandler
  extends BasicDomainHandler
{
  private static boolean isSpecialDomain(String paramString)
  {
    String str = paramString.toUpperCase(Locale.ENGLISH);
    return (str.endsWith(".COM")) || (str.endsWith(".EDU")) || (str.endsWith(".NET")) || (str.endsWith(".GOV")) || (str.endsWith(".MIL")) || (str.endsWith(".ORG")) || (str.endsWith(".INT"));
  }
  
  public boolean match(Cookie paramCookie, CookieOrigin paramCookieOrigin)
  {
    Args.notNull(paramCookie, "Cookie");
    Args.notNull(paramCookieOrigin, "Cookie origin");
    String str1 = paramCookieOrigin.getHost();
    String str2 = paramCookie.getDomain();
    if (str2 == null) {
      return false;
    }
    return str1.endsWith(str2);
  }
  
  public void validate(Cookie paramCookie, CookieOrigin paramCookieOrigin)
    throws MalformedCookieException
  {
    super.validate(paramCookie, paramCookieOrigin);
    String str1 = paramCookieOrigin.getHost();
    String str2 = paramCookie.getDomain();
    if (str1.contains("."))
    {
      int i = new StringTokenizer(str2, ".").countTokens();
      if (isSpecialDomain(str2))
      {
        if (i < 2) {
          throw new CookieRestrictionViolationException("Domain attribute \"" + str2 + "\" violates the Netscape cookie specification for " + "special domains");
        }
      }
      else if (i < 3) {
        throw new CookieRestrictionViolationException("Domain attribute \"" + str2 + "\" violates the Netscape cookie specification");
      }
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.cookie.NetscapeDomainHandler
 * JD-Core Version:    0.7.0.1
 */