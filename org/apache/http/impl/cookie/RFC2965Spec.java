package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class RFC2965Spec
  extends RFC2109Spec
{
  public RFC2965Spec()
  {
    this(null, false);
  }
  
  public RFC2965Spec(String[] paramArrayOfString, boolean paramBoolean)
  {
    super(paramArrayOfString, paramBoolean);
    registerAttribHandler("domain", new RFC2965DomainAttributeHandler());
    registerAttribHandler("port", new RFC2965PortAttributeHandler());
    registerAttribHandler("commenturl", new RFC2965CommentUrlAttributeHandler());
    registerAttribHandler("discard", new RFC2965DiscardAttributeHandler());
    registerAttribHandler("version", new RFC2965VersionAttributeHandler());
  }
  
  private static CookieOrigin adjustEffectiveHost(CookieOrigin paramCookieOrigin)
  {
    String str = paramCookieOrigin.getHost();
    int i = 1;
    for (int j = 0;; j++) {
      if (j < str.length())
      {
        int k = str.charAt(j);
        if ((k == 46) || (k == 58)) {
          i = 0;
        }
      }
      else
      {
        if (i != 0) {
          paramCookieOrigin = new CookieOrigin(str + ".local", paramCookieOrigin.getPort(), paramCookieOrigin.getPath(), paramCookieOrigin.isSecure());
        }
        return paramCookieOrigin;
      }
    }
  }
  
  private List<Cookie> createCookies(HeaderElement[] paramArrayOfHeaderElement, CookieOrigin paramCookieOrigin)
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
      BasicClientCookie2 localBasicClientCookie2 = new BasicClientCookie2(str1, str2);
      localBasicClientCookie2.setPath(getDefaultPath(paramCookieOrigin));
      localBasicClientCookie2.setDomain(getDefaultDomain(paramCookieOrigin));
      int[] arrayOfInt = new int[1];
      arrayOfInt[0] = paramCookieOrigin.getPort();
      localBasicClientCookie2.setPorts(arrayOfInt);
      NameValuePair[] arrayOfNameValuePair = localHeaderElement.getParameters();
      HashMap localHashMap = new HashMap(arrayOfNameValuePair.length);
      for (int k = -1 + arrayOfNameValuePair.length; k >= 0; k--)
      {
        NameValuePair localNameValuePair2 = arrayOfNameValuePair[k];
        localHashMap.put(localNameValuePair2.getName().toLowerCase(Locale.ENGLISH), localNameValuePair2);
      }
      Iterator localIterator = localHashMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        NameValuePair localNameValuePair1 = (NameValuePair)((Map.Entry)localIterator.next()).getValue();
        String str3 = localNameValuePair1.getName().toLowerCase(Locale.ENGLISH);
        localBasicClientCookie2.setAttribute(str3, localNameValuePair1.getValue());
        CookieAttributeHandler localCookieAttributeHandler = findAttribHandler(str3);
        if (localCookieAttributeHandler != null) {
          localCookieAttributeHandler.parse(localBasicClientCookie2, localNameValuePair1.getValue());
        }
      }
      localArrayList.add(localBasicClientCookie2);
    }
    return localArrayList;
  }
  
  protected void formatCookieAsVer(CharArrayBuffer paramCharArrayBuffer, Cookie paramCookie, int paramInt)
  {
    super.formatCookieAsVer(paramCharArrayBuffer, paramCookie, paramInt);
    if ((paramCookie instanceof ClientCookie))
    {
      String str = ((ClientCookie)paramCookie).getAttribute("port");
      if (str != null)
      {
        paramCharArrayBuffer.append("; $Port");
        paramCharArrayBuffer.append("=\"");
        if (str.trim().length() > 0)
        {
          int[] arrayOfInt = paramCookie.getPorts();
          if (arrayOfInt != null)
          {
            int i = arrayOfInt.length;
            for (int j = 0; j < i; j++)
            {
              if (j > 0) {
                paramCharArrayBuffer.append(",");
              }
              paramCharArrayBuffer.append(Integer.toString(arrayOfInt[j]));
            }
          }
        }
        paramCharArrayBuffer.append("\"");
      }
    }
  }
  
  public int getVersion()
  {
    return 1;
  }
  
  public Header getVersionHeader()
  {
    CharArrayBuffer localCharArrayBuffer = new CharArrayBuffer(40);
    localCharArrayBuffer.append("Cookie2");
    localCharArrayBuffer.append(": ");
    localCharArrayBuffer.append("$Version=");
    localCharArrayBuffer.append(Integer.toString(getVersion()));
    return new BufferedHeader(localCharArrayBuffer);
  }
  
  public boolean match(Cookie paramCookie, CookieOrigin paramCookieOrigin)
  {
    Args.notNull(paramCookie, "Cookie");
    Args.notNull(paramCookieOrigin, "Cookie origin");
    return super.match(paramCookie, adjustEffectiveHost(paramCookieOrigin));
  }
  
  public List<Cookie> parse(Header paramHeader, CookieOrigin paramCookieOrigin)
    throws MalformedCookieException
  {
    Args.notNull(paramHeader, "Header");
    Args.notNull(paramCookieOrigin, "Cookie origin");
    if (!paramHeader.getName().equalsIgnoreCase("Set-Cookie2")) {
      throw new MalformedCookieException("Unrecognized cookie header '" + paramHeader.toString() + "'");
    }
    return createCookies(paramHeader.getElements(), adjustEffectiveHost(paramCookieOrigin));
  }
  
  protected List<Cookie> parse(HeaderElement[] paramArrayOfHeaderElement, CookieOrigin paramCookieOrigin)
    throws MalformedCookieException
  {
    return createCookies(paramArrayOfHeaderElement, adjustEffectiveHost(paramCookieOrigin));
  }
  
  public String toString()
  {
    return "rfc2965";
  }
  
  public void validate(Cookie paramCookie, CookieOrigin paramCookieOrigin)
    throws MalformedCookieException
  {
    Args.notNull(paramCookie, "Cookie");
    Args.notNull(paramCookieOrigin, "Cookie origin");
    super.validate(paramCookie, adjustEffectiveHost(paramCookieOrigin));
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.cookie.RFC2965Spec
 * JD-Core Version:    0.7.0.1
 */