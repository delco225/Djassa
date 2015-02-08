package org.apache.http.entity;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Locale;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.annotation.Immutable;
import org.apache.http.message.BasicHeaderValueFormatter;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.TextUtils;

@Immutable
public final class ContentType
  implements Serializable
{
  public static final ContentType APPLICATION_ATOM_XML = create("application/atom+xml", Consts.ISO_8859_1);
  public static final ContentType APPLICATION_FORM_URLENCODED = create("application/x-www-form-urlencoded", Consts.ISO_8859_1);
  public static final ContentType APPLICATION_JSON = create("application/json", Consts.UTF_8);
  public static final ContentType APPLICATION_OCTET_STREAM = create("application/octet-stream", (Charset)null);
  public static final ContentType APPLICATION_SVG_XML = create("application/svg+xml", Consts.ISO_8859_1);
  public static final ContentType APPLICATION_XHTML_XML = create("application/xhtml+xml", Consts.ISO_8859_1);
  public static final ContentType APPLICATION_XML = create("application/xml", Consts.ISO_8859_1);
  public static final ContentType DEFAULT_BINARY = APPLICATION_OCTET_STREAM;
  public static final ContentType DEFAULT_TEXT;
  public static final ContentType MULTIPART_FORM_DATA = create("multipart/form-data", Consts.ISO_8859_1);
  public static final ContentType TEXT_HTML = create("text/html", Consts.ISO_8859_1);
  public static final ContentType TEXT_PLAIN = create("text/plain", Consts.ISO_8859_1);
  public static final ContentType TEXT_XML = create("text/xml", Consts.ISO_8859_1);
  public static final ContentType WILDCARD = create("*/*", (Charset)null);
  private static final long serialVersionUID = -7768694718232371896L;
  private final Charset charset;
  private final String mimeType;
  private final NameValuePair[] params;
  
  static
  {
    DEFAULT_TEXT = TEXT_PLAIN;
  }
  
  ContentType(String paramString, Charset paramCharset)
  {
    this.mimeType = paramString;
    this.charset = paramCharset;
    this.params = null;
  }
  
  ContentType(String paramString, NameValuePair[] paramArrayOfNameValuePair)
    throws UnsupportedCharsetException
  {
    this.mimeType = paramString;
    this.params = paramArrayOfNameValuePair;
    String str = getParameter("charset");
    if (!TextUtils.isBlank(str)) {}
    for (Charset localCharset = Charset.forName(str);; localCharset = null)
    {
      this.charset = localCharset;
      return;
    }
  }
  
  public static ContentType create(String paramString)
  {
    return new ContentType(paramString, (Charset)null);
  }
  
  public static ContentType create(String paramString1, String paramString2)
    throws UnsupportedCharsetException
  {
    if (!TextUtils.isBlank(paramString2)) {}
    for (Charset localCharset = Charset.forName(paramString2);; localCharset = null) {
      return create(paramString1, localCharset);
    }
  }
  
  public static ContentType create(String paramString, Charset paramCharset)
  {
    String str = ((String)Args.notBlank(paramString, "MIME type")).toLowerCase(Locale.US);
    Args.check(valid(str), "MIME type may not contain reserved characters");
    return new ContentType(str, paramCharset);
  }
  
  private static ContentType create(HeaderElement paramHeaderElement)
  {
    String str = paramHeaderElement.getName();
    NameValuePair[] arrayOfNameValuePair = paramHeaderElement.getParameters();
    if ((arrayOfNameValuePair != null) && (arrayOfNameValuePair.length > 0)) {}
    for (;;)
    {
      return new ContentType(str, arrayOfNameValuePair);
      arrayOfNameValuePair = null;
    }
  }
  
  public static ContentType get(HttpEntity paramHttpEntity)
    throws ParseException, UnsupportedCharsetException
  {
    if (paramHttpEntity == null) {}
    HeaderElement[] arrayOfHeaderElement;
    do
    {
      Header localHeader;
      do
      {
        return null;
        localHeader = paramHttpEntity.getContentType();
      } while (localHeader == null);
      arrayOfHeaderElement = localHeader.getElements();
    } while (arrayOfHeaderElement.length <= 0);
    return create(arrayOfHeaderElement[0]);
  }
  
  public static ContentType getOrDefault(HttpEntity paramHttpEntity)
    throws ParseException, UnsupportedCharsetException
  {
    ContentType localContentType = get(paramHttpEntity);
    if (localContentType != null) {
      return localContentType;
    }
    return DEFAULT_TEXT;
  }
  
  public static ContentType parse(String paramString)
    throws ParseException, UnsupportedCharsetException
  {
    Args.notNull(paramString, "Content type");
    CharArrayBuffer localCharArrayBuffer = new CharArrayBuffer(paramString.length());
    localCharArrayBuffer.append(paramString);
    ParserCursor localParserCursor = new ParserCursor(0, paramString.length());
    HeaderElement[] arrayOfHeaderElement = BasicHeaderValueParser.INSTANCE.parseElements(localCharArrayBuffer, localParserCursor);
    if (arrayOfHeaderElement.length > 0) {
      return create(arrayOfHeaderElement[0]);
    }
    throw new ParseException("Invalid content type: " + paramString);
  }
  
  private static boolean valid(String paramString)
  {
    for (int i = 0; i < paramString.length(); i++)
    {
      int j = paramString.charAt(i);
      if ((j == 34) || (j == 44) || (j == 59)) {
        return false;
      }
    }
    return true;
  }
  
  public Charset getCharset()
  {
    return this.charset;
  }
  
  public String getMimeType()
  {
    return this.mimeType;
  }
  
  public String getParameter(String paramString)
  {
    Args.notEmpty(paramString, "Parameter name");
    if (this.params == null) {}
    for (;;)
    {
      return null;
      for (NameValuePair localNameValuePair : this.params) {
        if (localNameValuePair.getName().equalsIgnoreCase(paramString)) {
          return localNameValuePair.getValue();
        }
      }
    }
  }
  
  public String toString()
  {
    CharArrayBuffer localCharArrayBuffer = new CharArrayBuffer(64);
    localCharArrayBuffer.append(this.mimeType);
    if (this.params != null)
    {
      localCharArrayBuffer.append("; ");
      BasicHeaderValueFormatter.INSTANCE.formatParameters(localCharArrayBuffer, this.params, false);
    }
    for (;;)
    {
      return localCharArrayBuffer.toString();
      if (this.charset != null)
      {
        localCharArrayBuffer.append("; charset=");
        localCharArrayBuffer.append(this.charset.name());
      }
    }
  }
  
  public ContentType withCharset(String paramString)
  {
    return create(getMimeType(), paramString);
  }
  
  public ContentType withCharset(Charset paramCharset)
  {
    return create(getMimeType(), paramCharset);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.entity.ContentType
 * JD-Core Version:    0.7.0.1
 */