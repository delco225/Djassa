package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

@Immutable
public class BasicPathHandler
  implements CookieAttributeHandler
{
  public boolean match(Cookie paramCookie, CookieOrigin paramCookieOrigin)
  {
    Args.notNull(paramCookie, "Cookie");
    Args.notNull(paramCookieOrigin, "Cookie origin");
    String str1 = paramCookieOrigin.getPath();
    String str2 = paramCookie.getPath();
    if (str2 == null) {
      str2 = "/";
    }
    if ((str2.length() > 1) && (str2.endsWith("/"))) {
      str2 = str2.substring(0, -1 + str2.length());
    }
    boolean bool = str1.startsWith(str2);
    if ((bool) && (str1.length() != str2.length()) && (!str2.endsWith("/")))
    {
      if (str1.charAt(str2.length()) == '/') {
        bool = true;
      }
    }
    else {
      return bool;
    }
    return false;
  }
  
  public void parse(SetCookie paramSetCookie, String paramString)
    throws MalformedCookieException
  {
    Args.notNull(paramSetCookie, "Cookie");
    if (!TextUtils.isBlank(paramString)) {}
    for (;;)
    {
      paramSetCookie.setPath(paramString);
      return;
      paramString = "/";
    }
  }
  
  public void validate(Cookie paramCookie, CookieOrigin paramCookieOrigin)
    throws MalformedCookieException
  {
    if (!match(paramCookie, paramCookieOrigin)) {
      throw new CookieRestrictionViolationException("Illegal path attribute \"" + paramCookie.getPath() + "\". Path of origin: \"" + paramCookieOrigin.getPath() + "\"");
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.cookie.BasicPathHandler
 * JD-Core Version:    0.7.0.1
 */