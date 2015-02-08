package org.apache.http.client.fluent;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.http.Consts;
import org.apache.http.entity.ContentType;

public class Content
{
  public static final Content NO_CONTENT = new Content(new byte[0], ContentType.DEFAULT_BINARY);
  private final byte[] raw;
  private final ContentType type;
  
  Content(byte[] paramArrayOfByte, ContentType paramContentType)
  {
    this.raw = paramArrayOfByte;
    this.type = paramContentType;
  }
  
  public byte[] asBytes()
  {
    return (byte[])this.raw.clone();
  }
  
  public InputStream asStream()
  {
    return new ByteArrayInputStream(this.raw);
  }
  
  public String asString()
  {
    Charset localCharset = this.type.getCharset();
    if (localCharset == null) {
      localCharset = Consts.ISO_8859_1;
    }
    try
    {
      String str = new String(this.raw, localCharset.name());
      return str;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
    return new String(this.raw);
  }
  
  public ContentType getType()
  {
    return this.type;
  }
  
  public String toString()
  {
    return asString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.fluent.Content
 * JD-Core Version:    0.7.0.1
 */