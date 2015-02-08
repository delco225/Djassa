package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;

@NotThreadSafe
public class StringEntity
  extends AbstractHttpEntity
  implements Cloneable
{
  protected final byte[] content;
  
  public StringEntity(String paramString)
    throws UnsupportedEncodingException
  {
    this(paramString, ContentType.DEFAULT_TEXT);
  }
  
  public StringEntity(String paramString1, String paramString2)
    throws UnsupportedCharsetException
  {
    this(paramString1, ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), paramString2));
  }
  
  @Deprecated
  public StringEntity(String paramString1, String paramString2, String paramString3)
    throws UnsupportedEncodingException
  {
    Args.notNull(paramString1, "Source string");
    String str1;
    if (paramString2 != null)
    {
      str1 = paramString2;
      if (paramString3 == null) {
        break label72;
      }
    }
    label72:
    for (String str2 = paramString3;; str2 = "ISO-8859-1")
    {
      this.content = paramString1.getBytes(str2);
      setContentType(str1 + "; charset=" + str2);
      return;
      str1 = "text/plain";
      break;
    }
  }
  
  public StringEntity(String paramString, Charset paramCharset)
  {
    this(paramString, ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), paramCharset));
  }
  
  public StringEntity(String paramString, ContentType paramContentType)
    throws UnsupportedCharsetException
  {
    Args.notNull(paramString, "Source string");
    if (paramContentType != null) {}
    for (localCharset = paramContentType.getCharset();; localCharset = null)
    {
      if (localCharset == null) {
        localCharset = HTTP.DEF_CONTENT_CHARSET;
      }
      try
      {
        this.content = paramString.getBytes(localCharset.name());
        if (paramContentType != null) {
          setContentType(paramContentType.toString());
        }
        return;
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        throw new UnsupportedCharsetException(localCharset.name());
      }
    }
  }
  
  public Object clone()
    throws CloneNotSupportedException
  {
    return super.clone();
  }
  
  public InputStream getContent()
    throws IOException
  {
    return new ByteArrayInputStream(this.content);
  }
  
  public long getContentLength()
  {
    return this.content.length;
  }
  
  public boolean isRepeatable()
  {
    return true;
  }
  
  public boolean isStreaming()
  {
    return false;
  }
  
  public void writeTo(OutputStream paramOutputStream)
    throws IOException
  {
    Args.notNull(paramOutputStream, "Output stream");
    paramOutputStream.write(this.content);
    paramOutputStream.flush();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.entity.StringEntity
 * JD-Core Version:    0.7.0.1
 */