package org.apache.http.entity.mime.content;

import java.nio.charset.Charset;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;

public abstract class AbstractContentBody
  implements ContentBody
{
  private final ContentType contentType;
  
  @Deprecated
  public AbstractContentBody(String paramString)
  {
    this(ContentType.parse(paramString));
  }
  
  public AbstractContentBody(ContentType paramContentType)
  {
    Args.notNull(paramContentType, "Content type");
    this.contentType = paramContentType;
  }
  
  public String getCharset()
  {
    Charset localCharset = this.contentType.getCharset();
    if (localCharset != null) {
      return localCharset.name();
    }
    return null;
  }
  
  public ContentType getContentType()
  {
    return this.contentType;
  }
  
  public String getMediaType()
  {
    String str = this.contentType.getMimeType();
    int i = str.indexOf('/');
    if (i != -1) {
      str = str.substring(0, i);
    }
    return str;
  }
  
  public String getMimeType()
  {
    return this.contentType.getMimeType();
  }
  
  public String getSubType()
  {
    String str = this.contentType.getMimeType();
    int i = str.indexOf('/');
    if (i != -1) {
      return str.substring(i + 1);
    }
    return null;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.entity.mime.content.AbstractContentBody
 * JD-Core Version:    0.7.0.1
 */