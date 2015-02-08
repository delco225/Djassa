package org.apache.http.impl.cookie;

import java.util.Iterator;
import java.util.List;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie2;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class BestMatchSpec
  implements CookieSpec
{
  private BrowserCompatSpec compat;
  private final String[] datepatterns;
  private RFC2109Spec obsoleteStrict;
  private final boolean oneHeader;
  private RFC2965Spec strict;
  
  public BestMatchSpec()
  {
    this(null, false);
  }
  
  public BestMatchSpec(String[] paramArrayOfString, boolean paramBoolean)
  {
    if (paramArrayOfString == null) {}
    for (String[] arrayOfString = null;; arrayOfString = (String[])paramArrayOfString.clone())
    {
      this.datepatterns = arrayOfString;
      this.oneHeader = paramBoolean;
      return;
    }
  }
  
  private BrowserCompatSpec getCompat()
  {
    if (this.compat == null) {
      this.compat = new BrowserCompatSpec(this.datepatterns);
    }
    return this.compat;
  }
  
  private RFC2109Spec getObsoleteStrict()
  {
    if (this.obsoleteStrict == null) {
      this.obsoleteStrict = new RFC2109Spec(this.datepatterns, this.oneHeader);
    }
    return this.obsoleteStrict;
  }
  
  private RFC2965Spec getStrict()
  {
    if (this.strict == null) {
      this.strict = new RFC2965Spec(this.datepatterns, this.oneHeader);
    }
    return this.strict;
  }
  
  public List<Header> formatCookies(List<Cookie> paramList)
  {
    Args.notNull(paramList, "List of cookies");
    int i = 2147483647;
    int j = 1;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Cookie localCookie = (Cookie)localIterator.next();
      if (!(localCookie instanceof SetCookie2)) {
        j = 0;
      }
      if (localCookie.getVersion() < i) {
        i = localCookie.getVersion();
      }
    }
    if (i > 0)
    {
      if (j != 0) {
        return getStrict().formatCookies(paramList);
      }
      return getObsoleteStrict().formatCookies(paramList);
    }
    return getCompat().formatCookies(paramList);
  }
  
  public int getVersion()
  {
    return getStrict().getVersion();
  }
  
  public Header getVersionHeader()
  {
    return getStrict().getVersionHeader();
  }
  
  public boolean match(Cookie paramCookie, CookieOrigin paramCookieOrigin)
  {
    Args.notNull(paramCookie, "Cookie");
    Args.notNull(paramCookieOrigin, "Cookie origin");
    if (paramCookie.getVersion() > 0)
    {
      if ((paramCookie instanceof SetCookie2)) {
        return getStrict().match(paramCookie, paramCookieOrigin);
      }
      return getObsoleteStrict().match(paramCookie, paramCookieOrigin);
    }
    return getCompat().match(paramCookie, paramCookieOrigin);
  }
  
  public List<Cookie> parse(Header paramHeader, CookieOrigin paramCookieOrigin)
    throws MalformedCookieException
  {
    Args.notNull(paramHeader, "Header");
    Args.notNull(paramCookieOrigin, "Cookie origin");
    HeaderElement[] arrayOfHeaderElement1 = paramHeader.getElements();
    int i = 0;
    int j = 0;
    int k = arrayOfHeaderElement1.length;
    for (int m = 0; m < k; m++)
    {
      HeaderElement localHeaderElement = arrayOfHeaderElement1[m];
      if (localHeaderElement.getParameterByName("version") != null) {
        i = 1;
      }
      if (localHeaderElement.getParameterByName("expires") != null) {
        j = 1;
      }
    }
    if ((j != 0) || (i == 0))
    {
      NetscapeDraftHeaderParser localNetscapeDraftHeaderParser = NetscapeDraftHeaderParser.DEFAULT;
      CharArrayBuffer localCharArrayBuffer;
      if ((paramHeader instanceof FormattedHeader)) {
        localCharArrayBuffer = ((FormattedHeader)paramHeader).getBuffer();
      }
      for (ParserCursor localParserCursor = new ParserCursor(((FormattedHeader)paramHeader).getValuePos(), localCharArrayBuffer.length());; localParserCursor = new ParserCursor(0, localCharArrayBuffer.length()))
      {
        HeaderElement[] arrayOfHeaderElement2 = new HeaderElement[1];
        arrayOfHeaderElement2[0] = localNetscapeDraftHeaderParser.parseHeader(localCharArrayBuffer, localParserCursor);
        return getCompat().parse(arrayOfHeaderElement2, paramCookieOrigin);
        String str = paramHeader.getValue();
        if (str == null) {
          throw new MalformedCookieException("Header value is null");
        }
        localCharArrayBuffer = new CharArrayBuffer(str.length());
        localCharArrayBuffer.append(str);
      }
    }
    if ("Set-Cookie2".equals(paramHeader.getName())) {
      return getStrict().parse(arrayOfHeaderElement1, paramCookieOrigin);
    }
    return getObsoleteStrict().parse(arrayOfHeaderElement1, paramCookieOrigin);
  }
  
  public String toString()
  {
    return "best-match";
  }
  
  public void validate(Cookie paramCookie, CookieOrigin paramCookieOrigin)
    throws MalformedCookieException
  {
    Args.notNull(paramCookie, "Cookie");
    Args.notNull(paramCookieOrigin, "Cookie origin");
    if (paramCookie.getVersion() > 0)
    {
      if ((paramCookie instanceof SetCookie2))
      {
        getStrict().validate(paramCookie, paramCookieOrigin);
        return;
      }
      getObsoleteStrict().validate(paramCookie, paramCookieOrigin);
      return;
    }
    getCompat().validate(paramCookie, paramCookieOrigin);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.cookie.BestMatchSpec
 * JD-Core Version:    0.7.0.1
 */