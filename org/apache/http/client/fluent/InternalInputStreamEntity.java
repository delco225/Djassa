package org.apache.http.client.fluent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;

class InternalInputStreamEntity
  extends AbstractHttpEntity
{
  private final InputStream content;
  private final long length;
  
  public InternalInputStreamEntity(InputStream paramInputStream, long paramLong, ContentType paramContentType)
  {
    this.content = ((InputStream)Args.notNull(paramInputStream, "Source input stream"));
    this.length = paramLong;
    if (paramContentType != null) {
      setContentType(paramContentType.toString());
    }
  }
  
  public InputStream getContent()
    throws IOException
  {
    return this.content;
  }
  
  public long getContentLength()
  {
    return this.length;
  }
  
  public boolean isRepeatable()
  {
    return false;
  }
  
  public boolean isStreaming()
  {
    return true;
  }
  
  public void writeTo(OutputStream paramOutputStream)
    throws IOException
  {
    Args.notNull(paramOutputStream, "Output stream");
    InputStream localInputStream = this.content;
    byte[] arrayOfByte;
    try
    {
      arrayOfByte = new byte[4096];
      if (this.length < 0L) {
        for (;;)
        {
          int i = localInputStream.read(arrayOfByte);
          if (i == -1) {
            break;
          }
          paramOutputStream.write(arrayOfByte, 0, i);
        }
      }
      l = this.length;
    }
    finally
    {
      localInputStream.close();
    }
    for (;;)
    {
      long l;
      int j;
      if (l > 0L)
      {
        j = localInputStream.read(arrayOfByte, 0, (int)Math.min(4096L, l));
        if (j != -1) {}
      }
      else
      {
        localInputStream.close();
        return;
      }
      paramOutputStream.write(arrayOfByte, 0, j);
      l -= j;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.fluent.InternalInputStreamEntity
 * JD-Core Version:    0.7.0.1
 */