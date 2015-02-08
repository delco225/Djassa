package org.apache.http.entity.mime.content;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;

public class ByteArrayBody
  extends AbstractContentBody
{
  private final byte[] data;
  private final String filename;
  
  public ByteArrayBody(byte[] paramArrayOfByte, String paramString)
  {
    this(paramArrayOfByte, "application/octet-stream", paramString);
  }
  
  @Deprecated
  public ByteArrayBody(byte[] paramArrayOfByte, String paramString1, String paramString2)
  {
    this(paramArrayOfByte, ContentType.create(paramString1), paramString2);
  }
  
  public ByteArrayBody(byte[] paramArrayOfByte, ContentType paramContentType, String paramString)
  {
    super(paramContentType);
    Args.notNull(paramArrayOfByte, "byte[]");
    this.data = paramArrayOfByte;
    this.filename = paramString;
  }
  
  public String getCharset()
  {
    return null;
  }
  
  public long getContentLength()
  {
    return this.data.length;
  }
  
  public String getFilename()
  {
    return this.filename;
  }
  
  public String getTransferEncoding()
  {
    return "binary";
  }
  
  public void writeTo(OutputStream paramOutputStream)
    throws IOException
  {
    paramOutputStream.write(this.data);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.entity.mime.content.ByteArrayBody
 * JD-Core Version:    0.7.0.1
 */