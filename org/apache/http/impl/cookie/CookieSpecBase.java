package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.util.Args;

@NotThreadSafe
public abstract class CookieSpecBase
  extends AbstractCookieSpec
{
  protected static String getDefaultDomain(CookieOrigin paramCookieOrigin)
  {
    return paramCookieOrigin.getHost();
  }
  
  protected static String getDefaultPath(CookieOrigin paramCookieOrigin)
  {
    String str = paramCookieOrigin.getPath();
    int i = str.lastIndexOf('/');
    if (i >= 0)
    {
      if (i == 0) {
        i = 1;
      }
      str = str.substring(0, i);
    }
    return str;
  }
  
  public boolean match(Cookie paramCookie, CookieOrigin paramCookieOrigin)
  {
    Args.notNull(paramCookie, "Cookie");
    Args.notNull(paramCookieOrigin, "Cookie origin");
    Iterator localIterator = getAttribHandlers().iterator();
    while (localIterator.hasNext()) {
      if (!((CookieAttributeHandler)localIterator.next()).match(paramCookie, paramCookieOrigin)) {
        return false;
      }
    }
    return true;
  }
  
  protected List<Cookie> parse(HeaderElement[] paramArrayOfHeaderElement, CookieOrigin paramCookieOrigin)
    throws MalformedCookieException
  {
    ArrayList localArrayList = new ArrayList(paramArrayOfHeaderElement.length);
    int i = paramArrayOfHeaderElement.length;
    for (int j = 0; j < i; j++)
    {
      HeaderElement localHeaderElement = paramArrayOfHeaderElement[j];
      String str1 = localHeaderElement.getName();
      String str2 = localHeaderElement.getValue();
      if ((str1 == null) || (str1.length() == 0)) {
        throw new MalformedCookieException("Cookie name may not be empty");
      }
      BasicClientCookie localBasicClientCookie = new BasicClientCookie(str1, str2);
      localBasicClientCookie.setPath(getDefaultPath(paramCookieOrigin));
      localBasicClientCookie.setDomain(getDefaultDomain(paramCookieOrigin));
      NameValuePair[] arrayOfNameValuePair = localHeaderElement.getParameters();
      for (int k = -1 + arrayOfNameValuePair.length; k >= 0; k--)
      {
        NameValuePair localNameValuePair = arrayOfNameValuePair[k];
        String str3 = localNameValuePair.getName().toLowerCase(Locale.ENGLISH);
        localBasicClientCookie.setAttribute(str3, localNameValuePair.getValue());
        CookieAttributeHandler localCookieAttributeHandler = findAttribHandler(str3);
        if (localCookieAttributeHandler != null) {
          localCookieAttributeHandler.parse(localBasicClientCookie, localNameValuePair.getValue());
        }
      }
      localArrayList.add(localBasicClientCookie);
    }
    return localArrayList;
  }
  
  public void validate(Cookie paramCookie, CookieOrigin paramCookieOrigin)
    throws MalformedCookieException
  {
    Args.notNull(paramCookie, "Cookie");
    Args.notNull(paramCookieOrigin, "Cookie origin");
    Iterator localIterator = getAttribHandlers().iterator();
    while (localIterator.hasNext()) {
      ((CookieAttributeHandler)localIterator.next()).validate(paramCookie, paramCookieOrigin);
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.cookie.CookieSpecBase
 * JD-Core Version:    0.7.0.1
 */