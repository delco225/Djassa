package org.apache.http.impl.cookie;

import java.util.Locale;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

@Immutable
public class RFC2965DomainAttributeHandler
  implements CookieAttributeHandler
{
  public boolean domainMatch(String paramString1, String paramString2)
  {
    return (paramString1.equals(paramString2)) || ((paramString2.startsWith(".")) && (paramString1.endsWith(paramString2)));
  }
  
  public boolean match(Cookie paramCookie, CookieOrigin paramCookieOrigin)
  {
    Args.notNull(paramCookie, "Cookie");
    Args.notNull(paramCookieOrigin, "Cookie origin");
    String str1 = paramCookieOrigin.getHost().toLowerCase(Locale.ENGLISH);
    String str2 = paramCookie.getDomain();
    if (!domainMatch(str1, str2)) {}
    while (str1.substring(0, str1.length() - str2.length()).indexOf('.') != -1) {
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
    String str = paramString.toLowerCase(Locale.ENGLISH);
    if (!paramString.startsWith(".")) {
      str = '.' + str;
    }
    paramSetCookie.setDomain(str);
  }
  
  public void validate(Cookie paramCookie, CookieOrigin paramCookieOrigin)
    throws MalformedCookieException
  {
    Args.notNull(paramCookie, "Cookie");
    Args.notNull(paramCookieOrigin, "Cookie origin");
    String str1 = paramCookieOrigin.getHost().toLowerCase(Locale.ENGLISH);
    if (paramCookie.getDomain() == null) {
      throw new CookieRestrictionViolationException("Invalid cookie state: domain not specified");
    }
    String str2 = paramCookie.getDomain().toLowerCase(Locale.ENGLISH);
    if (((paramCookie instanceof ClientCookie)) && (((ClientCookie)paramCookie).containsAttribute("domain")))
    {
      if (!str2.startsWith(".")) {
        throw new CookieRestrictionViolationException("Domain attribute \"" + paramCookie.getDomain() + "\" violates RFC 2109: domain must start with a dot");
      }
      int i = str2.indexOf('.', 1);
      if (((i < 0) || (i == -1 + str2.length())) && (!str2.equals(".local"))) {
        throw new CookieRestrictionViolationException("Domain attribute \"" + paramCookie.getDomain() + "\" violates RFC 2965: the value contains no embedded dots " + "and the value is not .local");
      }
      if (!domainMatch(str1, str2)) {
        throw new CookieRestrictionViolationException("Domain attribute \"" + paramCookie.getDomain() + "\" violates RFC 2965: effective host name does not " + "domain-match domain attribute.");
      }
      if (str1.substring(0, str1.length() - str2.length()).indexOf('.') != -1) {
        throw new CookieRestrictionViolationException("Domain attribute \"" + paramCookie.getDomain() + "\" violates RFC 2965: " + "effective host minus domain may not contain any dots");
      }
    }
    else if (!paramCookie.getDomain().equals(str1))
    {
      throw new CookieRestrictionViolationException("Illegal domain attribute: \"" + paramCookie.getDomain() + "\"." + "Domain of origin: \"" + str1 + "\"");
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.cookie.RFC2965DomainAttributeHandler
 * JD-Core Version:    0.7.0.1
 */