package org.apache.http.client.fluent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;

class InternalByteArrayEntity
  extends AbstractHttpEntity
  implements Cloneable
{
  private final byte[] b;
  private final int len;
  private final int off;
  
  public InternalByteArrayEntity(byte[] paramArrayOfByte)
  {
    this(paramArrayOfByte, null);
  }
  
  public InternalByteArrayEntity(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this(paramArrayOfByte, paramInt1, paramInt2, null);
  }
  
  public InternalByteArrayEntity(byte[] paramArrayOfByte, int paramInt1, int paramInt2, ContentType paramContentType)
  {
    Args.notNull(paramArrayOfByte, "Source byte array");
    if ((paramInt1 < 0) || (paramInt1 > paramArrayOfByte.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length)) {
      throw new IndexOutOfBoundsException("off: " + paramInt1 + " len: " + paramInt2 + " b.length: " + paramArrayOfByte.length);
    }
    this.b = paramArrayOfByte;
    this.off = paramInt1;
    this.len = paramInt2;
    if (paramContentType != null) {
      setContentType(paramContentType.toString());
    }
  }
  
  public InternalByteArrayEntity(byte[] paramArrayOfByte, ContentType paramContentType)
  {
    Args.notNull(paramArrayOfByte, "Source byte array");
    this.b = paramArrayOfByte;
    this.off = 0;
    this.len = this.b.length;
    if (paramContentType != null) {
      setContentType(paramContentType.toString());
    }
  }
  
  public InputStream getContent()
  {
    return new ByteArrayInputStream(this.b, this.off, this.len);
  }
  
  public long getContentLength()
  {
    return this.len;
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
    paramOutputStream.write(this.b, this.off, this.len);
    paramOutputStream.flush();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.fluent.InternalByteArrayEntity
 * JD-Core Version:    0.7.0.1
 */