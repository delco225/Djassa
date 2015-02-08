package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.message.BasicHeaderElement;
import org.apache.http.message.BasicHeaderValueFormatter;
import org.apache.http.message.BufferedHeader;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class BrowserCompatSpec
  extends CookieSpecBase
{
  private static final String[] DEFAULT_DATE_PATTERNS = { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy", "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z", "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z", "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z", "EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z", "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z" };
  private final String[] datepatterns;
  
  public BrowserCompatSpec()
  {
    this(null, BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_DEFAULT);
  }
  
  public BrowserCompatSpec(String[] paramArrayOfString)
  {
    this(paramArrayOfString, BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_DEFAULT);
  }
  
  public BrowserCompatSpec(String[] paramArrayOfString, BrowserCompatSpecFactory.SecurityLevel paramSecurityLevel)
  {
    if (paramArrayOfString != null) {}
    for (this.datepatterns = ((String[])paramArrayOfString.clone());; this.datepatterns = DEFAULT_DATE_PATTERNS) {
      switch (2.$SwitchMap$org$apache$http$impl$cookie$BrowserCompatSpecFactory$SecurityLevel[paramSecurityLevel.ordinal()])
      {
      default: 
        throw new RuntimeException("Unknown security level");
      }
    }
    registerAttribHandler("path", new BasicPathHandler());
    for (;;)
    {
      registerAttribHandler("domain", new BasicDomainHandler());
      registerAttribHandler("max-age", new BasicMaxAgeHandler());
      registerAttribHandler("secure", new BasicSecureHandler());
      registerAttribHandler("comment", new BasicCommentHandler());
      registerAttribHandler("expires", new BasicExpiresHandler(this.datepatterns));
      registerAttribHandler("version", new BrowserCompatVersionAttributeHandler());
      return;
      registerAttribHandler("path", new BasicPathHandler()
      {
        public void validate(Cookie paramAnonymousCookie, CookieOrigin paramAnonymousCookieOrigin)
          throws MalformedCookieException
        {}
      });
    }
  }
  
  private static boolean isQuoteEnclosed(String paramString)
  {
    return (paramString != null) && (paramString.startsWith("\"")) && (paramString.endsWith("\""));
  }
  
  public List<Header> formatCookies(List<Cookie> paramList)
  {
    Args.notEmpty(paramList, "List of cookies");
    CharArrayBuffer localCharArrayBuffer = new CharArrayBuffer(20 * paramList.size());
    localCharArrayBuffer.append("Cookie");
    localCharArrayBuffer.append(": ");
    int i = 0;
    if (i < paramList.size())
    {
      Cookie localCookie = (Cookie)paramList.get(i);
      if (i > 0) {
        localCharArrayBuffer.append("; ");
      }
      String str1 = localCookie.getName();
      String str2 = localCookie.getValue();
      if ((localCookie.getVersion() > 0) && (!isQuoteEnclosed(str2))) {
        BasicHeaderValueFormatter.INSTANCE.formatHeaderElement(localCharArrayBuffer, new BasicHeaderElement(str1, str2), false);
      }
      for (;;)
      {
        i++;
        break;
        localCharArrayBuffer.append(str1);
        localCharArrayBuffer.append("=");
        if (str2 != null) {
          localCharArrayBuffer.append(str2);
        }
      }
    }
    ArrayList localArrayList = new ArrayList(1);
    localArrayList.add(new BufferedHeader(localCharArrayBuffer));
    return localArrayList;
  }
  
  public int getVersion()
  {
    return 0;
  }
  
  public Header getVersionHeader()
  {
    return null;
  }
  
  public List<Cookie> parse(Header paramHeader, CookieOrigin paramCookieOrigin)
    throws MalformedCookieException
  {
    Args.notNull(paramHeader, "Header");
    Args.notNull(paramCookieOrigin, "Cookie origin");
    if (!paramHeader.getName().equalsIgnoreCase("Set-Cookie")) {
      throw new MalformedCookieException("Unrecognized cookie header '" + paramHeader.toString() + "'");
    }
    HeaderElement[] arrayOfHeaderElement1 = paramHeader.getElements();
    int i = 0;
    int j = 0;
    for (HeaderElement localHeaderElement : arrayOfHeaderElement1)
    {
      if (localHeaderElement.getParameterByName("version") != null) {
        i = 1;
      }
      if (localHeaderElement.getParameterByName("expires") != null) {
        j = 1;
      }
    }
    NetscapeDraftHeaderParser localNetscapeDraftHeaderParser;
    CharArrayBuffer localCharArrayBuffer;
    if ((j != 0) || (i == 0))
    {
      localNetscapeDraftHeaderParser = NetscapeDraftHeaderParser.DEFAULT;
      if (!(paramHeader instanceof FormattedHeader)) {
        break label222;
      }
      localCharArrayBuffer = ((FormattedHeader)paramHeader).getBuffer();
    }
    for (ParserCursor localParserCursor = new ParserCursor(((FormattedHeader)paramHeader).getValuePos(), localCharArrayBuffer.length());; localParserCursor = new ParserCursor(0, localCharArrayBuffer.length()))
    {
      arrayOfHeaderElement1 = new HeaderElement[1];
      arrayOfHeaderElement1[0] = localNetscapeDraftHeaderParser.parseHeader(localCharArrayBuffer, localParserCursor);
      return parse(arrayOfHeaderElement1, paramCookieOrigin);
      label222:
      String str = paramHeader.getValue();
      if (str == null) {
        throw new MalformedCookieException("Header value is null");
      }
      localCharArrayBuffer = new CharArrayBuffer(str.length());
      localCharArrayBuffer.append(str);
    }
  }
  
  public String toString()
  {
    return "compatibility";
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.cookie.BrowserCompatSpec
 * JD-Core Version:    0.7.0.1
 */