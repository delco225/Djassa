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
import org.apache.http.message.BufferedHeader;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class NetscapeDraftSpec
  extends CookieSpecBase
{
  protected static final String EXPIRES_PATTERN = "EEE, dd-MMM-yy HH:mm:ss z";
  private final String[] datepatterns;
  
  public NetscapeDraftSpec()
  {
    this(null);
  }
  
  public NetscapeDraftSpec(String[] paramArrayOfString)
  {
    if (paramArrayOfString != null) {}
    for (this.datepatterns = ((String[])paramArrayOfString.clone());; this.datepatterns = new String[] { "EEE, dd-MMM-yy HH:mm:ss z" })
    {
      registerAttribHandler("path", new BasicPathHandler());
      registerAttribHandler("domain", new NetscapeDomainHandler());
      registerAttribHandler("max-age", new BasicMaxAgeHandler());
      registerAttribHandler("secure", new BasicSecureHandler());
      registerAttribHandler("comment", new BasicCommentHandler());
      registerAttribHandler("expires", new BasicExpiresHandler(this.datepatterns));
      return;
    }
  }
  
  public List<Header> formatCookies(List<Cookie> paramList)
  {
    Args.notEmpty(paramList, "List of cookies");
    CharArrayBuffer localCharArrayBuffer = new CharArrayBuffer(20 * paramList.size());
    localCharArrayBuffer.append("Cookie");
    localCharArrayBuffer.append(": ");
    for (int i = 0; i < paramList.size(); i++)
    {
      Cookie localCookie = (Cookie)paramList.get(i);
      if (i > 0) {
        localCharArrayBuffer.append("; ");
      }
      localCharArrayBuffer.append(localCookie.getName());
      String str = localCookie.getValue();
      if (str != null)
      {
        localCharArrayBuffer.append("=");
        localCharArrayBuffer.append(str);
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
    NetscapeDraftHeaderParser localNetscapeDraftHeaderParser = NetscapeDraftHeaderParser.DEFAULT;
    CharArrayBuffer localCharArrayBuffer;
    if ((paramHeader instanceof FormattedHeader)) {
      localCharArrayBuffer = ((FormattedHeader)paramHeader).getBuffer();
    }
    for (ParserCursor localParserCursor = new ParserCursor(((FormattedHeader)paramHeader).getValuePos(), localCharArrayBuffer.length());; localParserCursor = new ParserCursor(0, localCharArrayBuffer.length()))
    {
      HeaderElement[] arrayOfHeaderElement = new HeaderElement[1];
      arrayOfHeaderElement[0] = localNetscapeDraftHeaderParser.parseHeader(localCharArrayBuffer, localParserCursor);
      return parse(arrayOfHeaderElement, paramCookieOrigin);
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
    return "netscape";
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.cookie.NetscapeDraftSpec
 * JD-Core Version:    0.7.0.1
 */