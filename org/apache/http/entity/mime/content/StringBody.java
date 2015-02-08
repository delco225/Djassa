package org.apache.http.entity.mime.content;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import org.apache.http.Consts;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;

public class StringBody
  extends AbstractContentBody
{
  private final byte[] content;
  
  @Deprecated
  public StringBody(String paramString)
    throws UnsupportedEncodingException
  {
    this(paramString, "text/plain", Consts.ASCII);
  }
  
  @Deprecated
  public StringBody(String paramString1, String paramString2, Charset paramCharset)
    throws UnsupportedEncodingException
  {
    this(paramString1, ContentType.create(paramString2, paramCharset));
  }
  
  @Deprecated
  public StringBody(String paramString, Charset paramCharset)
    throws UnsupportedEncodingException
  {
    this(paramString, "text/plain", paramCharset);
  }
  
  public StringBody(String paramString, ContentType paramContentType)
  {
    super(paramContentType);
    Charset localCharset = paramContentType.getCharset();
    if (localCharset != null) {}
    for (str = localCharset.name();; str = Consts.ASCII.name()) {
      try
      {
        this.content = paramString.getBytes(str);
        return;
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        throw new UnsupportedCharsetException(str);
      }
    }
  }
  
  @Deprecated
  public static StringBody create(String paramString)
    throws IllegalArgumentException
  {
    return create(paramString, null, null);
  }
  
  @Deprecated
  public static StringBody create(String paramString1, String paramString2, Charset paramCharset)
    throws IllegalArgumentException
  {
    try
    {
      StringBody localStringBody = new StringBody(paramString1, paramString2, paramCharset);
      return localStringBody;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new IllegalArgumentException("Charset " + paramCharset + " is not supported", localUnsupportedEncodingException);
    }
  }
  
  @Deprecated
  public static StringBody create(String paramString, Charset paramCharset)
    throws IllegalArgumentException
  {
    return create(paramString, null, paramCharset);
  }
  
  public long getContentLength()
  {
    return this.content.length;
  }
  
  public String getFilename()
  {
    return null;
  }
  
  public Reader getReader()
  {
    Charset localCharset = getContentType().getCharset();
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(this.content);
    if (localCharset != null) {}
    for (;;)
    {
      return new InputStreamReader(localByteArrayInputStream, localCharset);
      localCharset = Consts.ASCII;
    }
  }
  
  public String getTransferEncoding()
  {
    return "8bit";
  }
  
  public void writeTo(OutputStream paramOutputStream)
    throws IOException
  {
    Args.notNull(paramOutputStream, "Output stream");
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(this.content);
    byte[] arrayOfByte = new byte[4096];
    for (;;)
    {
      int i = localByteArrayInputStream.read(arrayOfByte);
      if (i == -1) {
        break;
      }
      paramOutputStream.write(arrayOfByte, 0, i);
    }
    paramOutputStream.flush();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.entity.mime.content.StringBody
 * JD-Core Version:    0.7.0.1
 */