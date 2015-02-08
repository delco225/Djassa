package org.apache.http.impl.cookie;

import java.util.StringTokenizer;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.cookie.SetCookie2;
import org.apache.http.util.Args;

@Immutable
public class RFC2965PortAttributeHandler
  implements CookieAttributeHandler
{
  private static int[] parsePortAttribute(String paramString)
    throws MalformedCookieException
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ",");
    int[] arrayOfInt = new int[localStringTokenizer.countTokens()];
    for (int i = 0;; i++) {
      try
      {
        if (!localStringTokenizer.hasMoreTokens()) {
          break;
        }
        arrayOfInt[i] = Integer.parseInt(localStringTokenizer.nextToken().trim());
        if (arrayOfInt[i] < 0) {
          throw new MalformedCookieException("Invalid Port attribute.");
        }
      }
      catch (NumberFormatException localNumberFormatException)
      {
        throw new MalformedCookieException("Invalid Port attribute: " + localNumberFormatException.getMessage());
      }
    }
    return arrayOfInt;
  }
  
  private static boolean portMatch(int paramInt, int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length;
    for (int j = 0;; j++)
    {
      boolean bool = false;
      if (j < i)
      {
        if (paramInt == paramArrayOfInt[j]) {
          bool = true;
        }
      }
      else {
        return bool;
      }
    }
  }
  
  public boolean match(Cookie paramCookie, CookieOrigin paramCookieOrigin)
  {
    Args.notNull(paramCookie, "Cookie");
    Args.notNull(paramCookieOrigin, "Cookie origin");
    int i = paramCookieOrigin.getPort();
    if (((paramCookie instanceof ClientCookie)) && (((ClientCookie)paramCookie).containsAttribute("port")))
    {
      if (paramCookie.getPorts() == null) {
        return false;
      }
      if (!portMatch(i, paramCookie.getPorts())) {
        return false;
      }
    }
    return true;
  }
  
  public void parse(SetCookie paramSetCookie, String paramString)
    throws MalformedCookieException
  {
    Args.notNull(paramSetCookie, "Cookie");
    if ((paramSetCookie instanceof SetCookie2))
    {
      SetCookie2 localSetCookie2 = (SetCookie2)paramSetCookie;
      if ((paramString != null) && (paramString.trim().length() > 0)) {
        localSetCookie2.setPorts(parsePortAttribute(paramString));
      }
    }
  }
  
  public void validate(Cookie paramCookie, CookieOrigin paramCookieOrigin)
    throws MalformedCookieException
  {
    Args.notNull(paramCookie, "Cookie");
    Args.notNull(paramCookieOrigin, "Cookie origin");
    int i = paramCookieOrigin.getPort();
    if (((paramCookie instanceof ClientCookie)) && (((ClientCookie)paramCookie).containsAttribute("port")) && (!portMatch(i, paramCookie.getPorts()))) {
      throw new CookieRestrictionViolationException("Port attribute violates RFC 2965: Request port not found in cookie's port list.");
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.cookie.RFC2965PortAttributeHandler
 * JD-Core Version:    0.7.0.1
 */